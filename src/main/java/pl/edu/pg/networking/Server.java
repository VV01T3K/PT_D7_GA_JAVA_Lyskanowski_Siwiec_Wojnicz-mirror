package pl.edu.pg.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.edu.pg.Czlowiek;

public class Server {

    private static Logger LOGGER = LogManager.getLogger(Server.class);

    private ServerSocket serverSocket;
    private int PORT = 2137;

    private final List<ClientHandler> clientHandlers = Collections.synchronizedList(new ArrayList<>());

    public Server setPort(int port) {
        this.PORT = port;
        return this;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            // LOGGER.log(Level.SEVERE, "Could not listen on port: " + PORT, e);
            LOGGER.fatal("Could not listen on port: ", PORT, e);
            System.exit(1);
        }

        LOGGER.info("Server started on port: " + PORT);

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandlers.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            // LOGGER.log(Level.SEVERE, "Could not accept connection", e);
            System.exit(1);
        }
    }

    public void stop() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            // LOGGER.log(Level.WARNING, "Could not close server socket", e);
        }
    }

    private class ClientHandler extends Thread {
        private final Socket clientSocket;
        private PrintWriter out;
        private ObjectInputStream in;
        private BufferedReader textIn;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new ObjectInputStream(clientSocket.getInputStream());
                // textIn = new BufferedReader(new
                // InputStreamReader(clientSocket.getInputStream()));

                String clientAddress = clientSocket.getInetAddress().getHostAddress();
                LOGGER.info("New connection from: " + clientAddress);

                Object receivedObject;
                while ((receivedObject = in.readObject()) != null) {
                    if (receivedObject instanceof Message) {
                        Message message = (Message) receivedObject;
                        LOGGER.info("Received message: " + message);
                        processMessage(message);
                    } else {
                        // LOGGER.warning("Received unknown object type: " + receivedObject.getClass());
                        sendResponse(Message.Response.INVALID_PREFIX);
                    }
                }
            } catch (IOException e) {
                // LOGGER.log(Level.WARNING, "Error handling client connection", e);
            } catch (ClassNotFoundException e) {
                // LOGGER.log(Level.WARNING, "Error deserializing object from client", e);
                sendResponse(Message.Response.ERROR);
            } finally {
                closeConnection();
            }
        }

        private void processMessage(Message message) {
            Message.Prefix prefix = message.prefix;
            if (prefix == null) {
                // LOGGER.warning("Message has no prefix");
                sendResponse(Message.Response.INVALID_PREFIX);
                return;
            }

            try {
                switch (prefix) {
                    case TEXT:
                        handleTextMessage((String) message.content);
                        break;
                    case COMMAND:
                        handleCommand((Message.Command) message.content);
                        break;
                    case HUMAN:
                        Czlowiek human = (Czlowiek) message.content;
                        LOGGER.info("Received HUMAN object");
                        human.printRecursively();
                        sendResponse(Message.Response.OK);
                        break;
                }
            } catch (ClassCastException e) {
                // LOGGER.warning("Error casting message content: " + e.getMessage());
                sendResponse(Message.Response.INVALID_CONTENT);
            }
        }

        private void handleTextMessage(String message) {
            LOGGER.info("Received TEXT message: " + message);
            sendResponse(Message.Response.OK);
        }

        private void handleCommand(Message.Command command) {
            LOGGER.info("Received SERVER command: " + command);

            switch (command) {
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
                    // LOGGER.warning("Unknown server command: " + command);
                    sendResponse(Message.Response.INVALID_COMMAND);
                    break;
            }
        }

        private void sendResponse(Message.Response response) {
            out.println(response.name());
            LOGGER.info("Sent response: " + response.name());
        }

        private void closeConnection() {
            try {
                if (in != null)
                    in.close();
                if (textIn != null)
                    textIn.close();
                if (out != null)
                    out.close();
                if (clientSocket != null)
                    clientSocket.close();
                clientHandlers.remove(this);
                LOGGER.info("Connection closed: " + clientSocket.getInetAddress().getHostAddress());
            } catch (IOException e) {
                // LOGGER.log(Level.WARNING, "Error closing connection", e);
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server().setPort(2137);
        server.start();
    }
}