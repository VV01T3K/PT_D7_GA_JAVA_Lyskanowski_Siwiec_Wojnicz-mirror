package pl.edu.pg.networking;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pg.Czlowiek;
import pl.edu.pg.TestRepoGenerator;
import pl.edu.pg.TestRepoJsonLoader;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

  private static Logger LOGGER = LogManager.getLogger(Client.class);
  public final String name;
  private Socket clientSocket;
  private ObjectOutputStream out;
  private ObjectInputStream in;
  private int PORT = 2137;
  private String HOST = "localhost";
  private boolean connected = false;

  public Client() {
    this.name = System.getProperty("user.name");
    LOGGER.info("Client initialized with name: {}", name);
    // Default constructor
  }

  public static void main(String[] args) {
    System.out.println("hgr");
    TestRepoGenerator testRepoGenerator = new TestRepoGenerator();
    TestRepoJsonLoader testRepoJsonLoader = new TestRepoJsonLoader(1.0);
    Czlowiek human = testRepoGenerator.generateTestData(5).iterator().next();
    human.printRecursively();

    Client client = new Client()
            .setHost("localhost")
            .setPort(2137)
            .connect();

    if (!client.isConnected()) {
      System.err.println("Failed to connect to server\nExiting...");
      return;
    }

    client.send(Message.Prefix.NAME, client.name);

    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter message to send to server (type 'exit' to quit):");
    while (client.isConnected() || client.reconnect().isConnected()) {
      String message = scanner.nextLine();
      if (message.equalsIgnoreCase("/human")) {
        System.out.println("Enter valid action:");
        System.out.println("1. --test");
        System.out.println("2. --random");
        System.out.println("3. --json");
        String action = scanner.nextLine();

        if (action.equalsIgnoreCase("1")) {
          message = "/human --test";
        } else if (action.equalsIgnoreCase("2")) {
          message = "/human --random";
        } else if (action.equalsIgnoreCase("3")) {
          message = "/human --json";
        } else {
          System.err.println("Invalid action. Please try again.");
          continue;
        }
      }
      if (message.equalsIgnoreCase("exit")) {
        break;
      } else if (message.equalsIgnoreCase("/ping")) {
        client.send(Message.Prefix.COMMAND, Message.Command.PING);
      } else if (message.equalsIgnoreCase("/exit")) {
        client.send(Message.Prefix.COMMAND, Message.Command.EXIT);
        break;
      } else if (message.equalsIgnoreCase("/human --test")) {
        client.send(Message.Prefix.HUMAN, human);
      } else if (message.equalsIgnoreCase("/human --random")) {
        client.send(Message.Prefix.HUMAN, testRepoGenerator.generateCzlowiek());
      } else if (message.equalsIgnoreCase("/human --json")) {
        System.out.println("Enter path to JSON file:");
        String homanPath = scanner.nextLine();
        if (homanPath.isEmpty() || !homanPath.endsWith(".json")) {
          System.err.println("Invalid path. Please provide a valid JSON file path.");
          continue;
        }
        Czlowiek humanObject = testRepoJsonLoader.readJson(homanPath);
        client.send(Message.Prefix.HUMAN, humanObject);
      } else {
        client.send(Message.Prefix.TEXT, message);
      }

    }

    scanner.close();
    client.disconnect();
  }

  public boolean isConnected() {
    return connected;
  }

  public Client setPort(int port) {
    this.PORT = port;
    return this;
  }

  public Client setHost(String host) {
    this.HOST = host;
    return this;
  }

  public Client connect() {
    try {
      clientSocket = new Socket(HOST, PORT);
      out = new ObjectOutputStream(clientSocket.getOutputStream());
      in = new ObjectInputStream(clientSocket.getInputStream());
      connected = true;
      send(Message.Prefix.NAME, name);
      LOGGER.info("Connected to server at {}:{}", HOST, PORT);
    } catch (Exception e) {
      connected = false;
      LOGGER.error("Could not connect to server: ", e);
    }
    return this;
  }

  public Client reconnect() {
    LOGGER.info("Reconnecting to server...");
    return connect();
  }

  public Client disconnect() {
    try {
      if (clientSocket != null && !clientSocket.isClosed()) {
        clientSocket.close();
        connected = false;
        LOGGER.info("Disconnected from server");
      }
    } catch (Exception e) {
      LOGGER.error("Could not close client socket: ", e);
    }
    return this;
  }

  private Message.Response proccessResponse() {
    try {
      Message message = (Message) in.readObject();
      if (message == null) {
        connected = false;
        LOGGER.error("No response from server");
        return Message.Response.CONNECTION_ERROR;
      }
      switch (message.prefix) {
        case HUMAN:
          Czlowiek human = (Czlowiek) message.content;
          LOGGER.info("Received human object: {}", human);
          human.printRecursively();
          return Message.Response.OK;
        default:
          break;
      }
      return (Message.Response) message.content;
    } catch (EOFException e) {
      connected = false;
      return Message.Response.CONNECTION_ERROR;
    } catch (Exception e) {
      connected = false;
      LOGGER.error("Error processing response: ", e);
      return Message.Response.ERROR;
    }
  }

  public Message.Response send(Message.Prefix prefix, Object message) {
    if (!connected) {
      LOGGER.error("Not connected to server");
      return Message.Response.CONNECTION_ERROR;
    }
    LOGGER.info("Sending - Prefix: {}, Payload: {}", prefix, message.getClass().getName());
    try {
      out.writeObject(new Message(prefix, message));
      out.flush();
      Message.Response response = proccessResponse();
      if (response == null) {
        connected = false;
        LOGGER.error("No response from server");
        return Message.Response.CONNECTION_ERROR;
      }
      LOGGER.info("Server response: {}", response);
      return response;
    } catch (Exception e) {
      LOGGER.error("Error sending message: ", e);
      return Message.Response.CONNECTION_ERROR;
    }
  }
}