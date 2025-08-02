package server;

import game.Gesture;
import game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Player implements Runnable{
    private Socket socket;
    private PrintWriter out;
    //private List<ClientHandler> clients;
    private String username = "User";
    private Server server;

    public String getUsername() {
        return username;
    }

    public Socket getSocket() {
        return socket;
    }

    public PrintWriter getOut() {
        return out;
    }

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.server = server;
    }

    public void sendMessage(String message) {
        out.println(message);
    }


    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            sendMessage("Welcome " + username + "!");
            String message;
            System.out.println("Enter your login");
            String login = in.readLine();
            System.out.println("Enter your password");
            String password = in.readLine();

            server.getDb().authenticate(login, password);

            if (server.getDb().authenticate(login, password) == false) {
                System.out.println("Invalid login or password");
                server.removeClient(this);
            }
            else {
                username = login;
            }
            String buffer;

            while ((message = in.readLine()) != null) {
                server.broadcastMessage(username + ":" + message);
                server.challengeToDuel(this, message);

                while (server.isActiveDuel()){ //add receive message
                    switch (message) {
                        case "p":
                            this.makeGesture(Gesture.PAPER);
                            break;
                        case "r":
                            this.makeGesture(Gesture.ROCK);
                            break;
                        case "s":
                            this.makeGesture(Gesture.SCISSORS);
                            break;
                        default:
                            break;
                    }
                }

            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        finally {
            server.removeClient(this);
            server.broadcastMessage("Goodbye!" + username);
        }
    }
}
