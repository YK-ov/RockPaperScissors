package server;

import game.Duel;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();
    private Database db = new Database();
    private boolean activeDuel = false;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public Database getDb() {
        return db;
    }

    public void listen() throws IOException {
        while (true) {
            Socket clientSocket;
            try {
                System.out.println("Waiting for connection");

                clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
                clients.add(clientHandler);



                System.out.println("Connection accepted " + clientSocket.getInetAddress());

            } catch (RuntimeException e) {
                throw new RuntimeException(e.getMessage());
            }
        }


    }

    public void broadcastMessage(String message) {
        for (int i = 0; i < clients.size(); i++) {
            clients.get(i).sendMessage(message);
        }
    }

    public void removeClient(ClientHandler client) {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i) == client) {
                clients.remove(i);
                break; //for optimisation
            }
        }
    }

    public void challengeToDuel(ClientHandler challenger, String challengeeLogin){
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getUsername().equals(challengeeLogin)) {  //either getter for username or toString() used for object of an ClientHandler class
                System.out.println("Duel is possible");
                activeDuel = true;
                startDuel(challenger, clients.get(i));
            }
            else {
                challenger.sendMessage("No player with this login");
            }
        }
    }

    public void startDuel(ClientHandler challenger, ClientHandler challengee){
        challenger.sendMessage("Starting Duel with" + challengee.getUsername());  // same as challengeToDuel either toString() or getter for username
        challengee.sendMessage("Starting Duel with" + challenger.getUsername());
        Duel duel = new Duel(challenger, challengee);
        activeDuel = false;
    }




    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        Server server = new Server(serverSocket);
        server.listen();

    }


    public boolean isActiveDuel() {
        return activeDuel;
    }

}
