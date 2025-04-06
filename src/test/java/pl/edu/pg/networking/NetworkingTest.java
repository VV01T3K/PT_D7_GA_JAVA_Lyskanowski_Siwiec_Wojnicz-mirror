package pl.edu.pg.networking;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.pg.Czlowiek;
import pl.edu.pg.Plec;

import static org.junit.jupiter.api.Assertions.*;

public class NetworkingTest {
    private static Server server;
    private Client client;
    private static final int PORT = 2137;
    private static Thread serverThread;

    @BeforeAll
    static void setUpServer() {
        server = new Server().setPort(PORT);
        serverThread = new Thread(() -> server.start());
        serverThread.start(); //powinno dzialac bez sleepa
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @BeforeEach
    void setUpClient() {
        client = new Client();
        client.setPort(PORT).setHost("localhost");
        client.connect();
    }

    @AfterEach
    void cleanUpClient() {
        if (client.isConnected()) {
            client.disconnect();
        }
    }

    @AfterAll
    static void cleanUpServer() {
        server.stop();
        serverThread.interrupt();
    }
    @Test
    void isConnected() {
        assertTrue(client.isConnected());
        client.disconnect();
        assertFalse(client.isConnected());
    }

    @Test
    void correctPortAndHost() {
        client.disconnect();
        client.setPort(2138);
        client.connect();
        assertFalse(client.isConnected());
        client.disconnect();
        client.setPort(PORT).setHost("pl.edu.pg");//szybko??
        client.connect();
        assertFalse(client.isConnected());
    }
    @Test
    void sendMessage()
    {
        String message = "Hello, Server!";
        Message.Response ans1 = client.send(Message.Prefix.TEXT,message);
        Czlowiek czlowiek = new Czlowiek("Kowalski", "Jan", 20, Plec.MEZCZYZNA);
        Message.Response ans2 = client.send(Message.Prefix.HUMAN, czlowiek);
        Message.Response ans3 = client.send(Message.Prefix.COMMAND, Message.Command.PING);
        assertSame(Message.Response.OK, ans1);
        assertSame(Message.Response.OK, ans2);
        assertSame(Message.Response.PING, ans3);
    }
}