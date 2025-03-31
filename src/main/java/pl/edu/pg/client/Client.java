package pl.edu.pg.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try (
                Socket clientSocket = new Socket("localhost", 3000);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            System.out.println("Connected to server");

            // Example usage of out and in
            while (true) {
                String userInput = System.console().readLine("Enter message (or '.' to exit):\n");
                out.println(userInput);
                String response = in.readLine();
                System.out.println("Server response: " + response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}