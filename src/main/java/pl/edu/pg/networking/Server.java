package pl.edu.pg.networking;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.edu.pg.Czlowiek;

public class Server {

    private static Logger LOGGER = LogManager.getLogger(Server.class);

    private Server cleanLogs() {
        try {
            Files.walk(Paths.get("Logs"))
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            Files.write(path, new byte[0]);
                        } catch (IOException e) {
                            System.err.println("Error clearing log file " + path + ": " + e.getMessage());
                        }
                    });
            System.out.println("Log files cleared.");
        } catch (IOException e) {
            System.err.println("Error accessing log directory: " + e.getMessage());
            System.exit(1);
        }
        return this;
    }

    private ServerSocket serverSocket;
    private int PORT = 2137;

    private final List<ClientHandler> clientHandlers = Collections.synchronizedList(new ArrayList<>());

    private Czlowiek lastReceivedHuman;

    public Server() {
        // Default constructor
    }

    public Server setPort(int port) {
        this.PORT = port;
        return this;
    }

    public boolean isRunning() {
        return serverSocket != null && !serverSocket.isClosed();
    }

    public Server start() {
        try {
            serverSocket = new ServerSocket(PORT);
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            LOGGER.info("Server started on {}:{}", hostAddress, PORT);
        } catch (UnknownHostException e) {
            LOGGER.warn("Could not determine hostname", e);
            System.exit(1);
        } catch (BindException e) {
            LOGGER.fatal("Could not listen on port: " + PORT, e);
            System.exit(1);
        } catch (IOException e) {
            LOGGER.error("Could not start server", e);
            System.exit(1);
        }

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandlers.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            LOGGER.error("Could not accept connection", e);
            System.exit(1);
        }

        return this;
    }

    public void stop() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            LOGGER.warn("Could not close server socket", e);
        }
    }

    private class ClientHandler extends Thread {
        private final Socket clientSocket;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private String clientName;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                in = new ObjectInputStream(clientSocket.getInputStream());

                String clientAddress = clientSocket.getLocalAddress().getHostAddress();
                LOGGER.info("New connection from: {}", clientAddress);

                Object receivedObject;
                while ((receivedObject = in.readObject()) != null) {
                    if (receivedObject instanceof Message) {
                        Message message = (Message) receivedObject;
                        processMessage(message);
                    } else {
                        LOGGER.warn(clientName + " - Received unknown Message type: {}", receivedObject.getClass());
                        sendResponse(Message.Response.INVALID_PREFIX);
                    }
                }
            } catch (EOFException e) {
                LOGGER.info("Client disconnected: {}", clientSocket.getInetAddress().getHostAddress());
            } catch (IOException e) {
                LOGGER.warn("Error handling client connection", e);
            } catch (ClassNotFoundException e) {
                LOGGER.warn("Error deserializing message from client", e);
                sendResponse(Message.Response.ERROR);
            } finally {
                closeConnection();
            }
        }

        private void processMessage(Message message) {
            try {
                switch (message.prefix) {
                    case null:
                        LOGGER.warn("Message has no prefix");
                        sendResponse(Message.Response.INVALID_PREFIX);
                        return;
                    case NAME:
                        clientName = (String) message.content;
                        LOGGER.info("Client name set to: {}", clientName);
                        sendResponse(Message.Response.OK);
                        break;
                    case TEXT:
                        handleTextMessage((String) message.content);
                        break;
                    case COMMAND:
                        handleCommand((Message.Command) message.content);
                        break;
                    case HUMAN:
                        Czlowiek human = (Czlowiek) message.content;
                        LOGGER.info(clientName + " - Received HUMAN: {}", human);
                        human.printRecursively();
                        lastReceivedHuman = human;
                        sendResponse(Message.Response.OK);
                        break;
                    case RESPONSE:
                        LOGGER.warn("Received RESPONSE message, which is not expected (yet)");
                        sendResponse(Message.Response.INVALID_PREFIX);
                        break;

                }
            } catch (ClassCastException e) {
                LOGGER.warn("Error casting message content", e);
                sendResponse(Message.Response.INVALID_CONTENT);
            }
        }

        private void handleTextMessage(String message) {
            LOGGER.info(clientName + " - Received TEXT message: {}", message);
            sendResponse(Message.Response.OK);
        }

        private void handleCommand(Message.Command command) {
            LOGGER.info(clientName + " - Received SERVER command: {}", command);

            switch (command) {
                case CLEAN_LOGS:
                    LOGGER.info("Cleaning logs...");
                    sendResponse(Message.Response.OK);
                    cleanLogs();
                    break;
                case LAST_HUMAN:
                    if (lastReceivedHuman != null) {
                        sendMessage(Message.Prefix.HUMAN, lastReceivedHuman);
                    } else {
                        LOGGER.warn("No human received yet");
                        sendResponse(Message.Response.INVALID_CONTENT);
                    }
                    break;
                case EXIT:
                    LOGGER.info("Server is shutting down...");
                    sendResponse(Message.Response.EXITED);
                    System.exit(0);
                    break;
                case PING:
                    LOGGER.info("Pinging client...");
                    sendResponse(Message.Response.PING);
                    break;
                default:
                    LOGGER.warn("Unknown server command: {}", command);
                    sendResponse(Message.Response.INVALID_COMMAND);
                    break;
            }
        }

        private void sendMessage(Message.Prefix prefix, Object content) {
            try {
                out.writeObject(new Message(prefix, content));
                out.flush();
                LOGGER.info("Sent message to {}: {}", clientName, content);
            } catch (IOException e) {
                LOGGER.warn("Error sending message to client", e);
            }
        }

        private void sendResponse(Message.Response response) {
            try {
                out.writeObject(new Message(Message.Prefix.RESPONSE, response));
                out.flush();
                LOGGER.info("Sent response to {}: {}", clientName, response.name());
            } catch (Exception e) {
                LOGGER.warn("Error sending response to client", e);
            }
        }

        private void closeConnection() {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
                if (clientSocket != null)
                    clientSocket.close();
                clientHandlers.remove(this);
                LOGGER.info("Connection closed: {}", clientSocket.getInetAddress().getHostAddress());
            } catch (IOException e) {
                LOGGER.warn("Error closing connection: ", e);
            }
        }
    }

    public static void main(String[] args) {

        Server server = new Server()
                .cleanLogs()
                .setPort(2137)
                .start();
        server.stop();
    }
}