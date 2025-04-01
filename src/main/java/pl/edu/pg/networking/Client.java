package pl.edu.pg.networking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import pl.edu.pg.Czlowiek;
import pl.edu.pg.TestRepoJsonLoader;

public class Client {

    private Socket clientSocket;
    private ObjectOutputStream out;
    private BufferedReader in;
    private int PORT = 2137;
    private String HOST = "localhost";
    private boolean connected = false;

    public Client() {
        // Default constructor
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
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            connected = true;
            System.out.println("Connected to server");
        } catch (Exception e) {
            connected = false;
            System.err.println("Could not connect to server: " + e.getMessage());
        }
        return this;
    }

    public void disconnect() {
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
                connected = false;
                System.out.println("Disconnected from server");
            }
        } catch (Exception e) {
            System.err.println("Could not close client socket: " + e.getMessage());
        }
    }

    public Message.Response send(Message.Prefix prefix, Object message) {
        if (!connected) {
            System.err.println("Not connected to server");
            return Message.Response.CONNECTION_ERROR;
        }
        try {
            out.writeObject(new Message(prefix, message));
            out.flush();
            String response = in.readLine();
            if (response == null) {
                connected = false;
                System.out.println("No response from server");
                return Message.Response.CONNECTION_ERROR;
            }
            System.out.println("Server response: " + response);
            return Message.Response.valueOf(response);
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
            return Message.Response.CONNECTION_ERROR;
        }
    }

    public static void main(String[] args) {
        TestRepoJsonLoader testRepoJsonLoader = new TestRepoJsonLoader(1.0, "", null);
        Czlowiek human = testRepoJsonLoader.readJson("Data/171815751.json");
        human.printRecursively();

        Client client = new Client()
                .setHost("localhost")
                .setPort(2137)
                .connect();

        if (!client.isConnected()) {
            System.err.println("Failed to connect to server\nExiting...");
            return;
        }

        // Example usage
        client.send(Message.Prefix.HUMAN, human);
        client.send(Message.Prefix.TEXT, human);
        client.send(Message.Prefix.COMMAND, Message.Command.PING);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter message to send to server (type 'exit' to quit):");
        while (client.isConnected()) {
            String message = scanner.nextLine();
            if (message.equalsIgnoreCase("exit")) {
                break;
            } else if (message.equalsIgnoreCase("/ping")) {
                client.send(Message.Prefix.COMMAND, Message.Command.PING);
                continue;
            } else if (message.equalsIgnoreCase("/exit")) {
                client.send(Message.Prefix.COMMAND, Message.Command.EXIT);
                break;
            } else {
                client.send(Message.Prefix.TEXT, message);
            }
        }

        scanner.close();
        client.disconnect();
    }
}