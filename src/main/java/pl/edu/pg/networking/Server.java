package pl.edu.pg.networking;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pg.Czlowiek;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {

  private static final Logger logger = LogManager.getLogger(Server.class);
  private final List<ClientHandler> clientHandlers = Collections.synchronizedList(new ArrayList<>());
  private ServerSocket serverSocket;
  private int PORT = 2137;

  public Server() {
    // Default constructor
  }

  public static void main(String[] args) {
    Server server = new Server()
            .cleanLogs()
            .setPort(2137);
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      logger.info("Shutting down server...");
      server.stop();
    }));
    server.start();
    server.stop();
  }

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

  public Server setPort(int port) {
    this.PORT = port;
    return this;
  }

  public Server start() {
    try {
      serverSocket = new ServerSocket(PORT);
      String hostAddress = InetAddress.getLocalHost().getHostAddress();
      logger.info("Server started on {}:{}", hostAddress, PORT);
    } catch (UnknownHostException e) {
      logger.warn("Could not determine hostname", e);
      System.exit(1);
    } catch (BindException e) {
      logger.fatal("Could not listen on port: " + PORT, e);
      System.exit(1);
    } catch (IOException e) {
      logger.error("Could not start server", e);
      System.exit(1);
    }

    try {
      while (!serverSocket.isClosed()) {
        Socket clientSocket = serverSocket.accept();
        ClientHandler clientHandler = new ClientHandler(clientSocket);
        clientHandlers.add(clientHandler);
        clientHandler.start();
      }
    } catch (IOException e) {
      logger.error("Could not accept connection", e);
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
      logger.warn("Could not close server socket", e);
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
        logger.info("New connection from: {}", clientAddress);

        Object receivedObject;
        while ((receivedObject = in.readObject()) != null) {
          if (receivedObject instanceof Message message) {
            processMessage(message);
          } else {
            logger.warn(clientName + " - Received unknown Message type: {}", receivedObject.getClass());
            sendResponse(Message.Response.INVALID_PREFIX);
          }
        }
      } catch (EOFException e) {
        logger.info("Client disconnected: {}", clientSocket.getInetAddress().getHostAddress());
      } catch (IOException e) {
        logger.warn("Error handling client connection", e);
      } catch (ClassNotFoundException e) {
        logger.warn("Error deserializing message from client", e);
        sendResponse(Message.Response.ERROR);
      } finally {
        closeConnection();
      }
    }

    private void processMessage(Message message) {
      try {
        switch (message.prefix) {
          case null:
            logger.warn("Message has no prefix");
            sendResponse(Message.Response.INVALID_PREFIX);
            return;
          case NAME:
            clientName = (String) message.content;
            logger.info("Client name set to: {}", clientName);
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
            logger.info(clientName + " - Received HUMAN: {}", human);
            human.printRecursively();
            sendMessage(Message.Prefix.HUMAN, human);
            break;
          case RESPONSE:
            logger.warn("Received RESPONSE message, which is not expected (yet)");
            sendResponse(Message.Response.INVALID_PREFIX);
            break;
        }
      } catch (ClassCastException e) {
        logger.warn("Error casting message content", e);
        sendResponse(Message.Response.INVALID_CONTENT);
      }
    }

    private void handleTextMessage(String message) {
      logger.info(clientName + " - Received TEXT message: {}", message);
      sendResponse(Message.Response.OK);
    }

    private void handleCommand(Message.Command command) {
      logger.info(clientName + " - Received SERVER command: {}", command);

      switch (command) {
        case CLEAN_LOGS:
          logger.info("Cleaning logs...");
          sendResponse(Message.Response.OK);
          cleanLogs();
          break;
        case EXIT:
          logger.info("Server is shutting down...");
          sendResponse(Message.Response.EXITED);
          System.exit(0);
          break;
        case PING:
          logger.info("Pinging client...");
          sendResponse(Message.Response.PONG);
          break;
        default:
          logger.warn("Unknown server command: {}", command);
          sendResponse(Message.Response.INVALID_COMMAND);
          break;
      }
    }

    private void sendMessage(Message.Prefix prefix, Object content) {
      try {
        out.writeObject(new Message(prefix, content));
        out.flush();
        logger.info("Sent message to {}: {}", clientName, content);
      } catch (IOException e) {
        logger.warn("Error sending message to client", e);
      }
    }

    private void sendResponse(Message.Response response) {
      try {
        out.writeObject(new Message(Message.Prefix.RESPONSE, response));
        out.flush();
        logger.info("Sent response to {}: {}", clientName, response.name());
      } catch (Exception e) {
        logger.warn("Error sending response to client", e);
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
        logger.info("Connection closed: {}", clientSocket.getInetAddress().getHostAddress());
      } catch (IOException e) {
        logger.warn("Error closing connection: ", e);
      }
    }
  }
}