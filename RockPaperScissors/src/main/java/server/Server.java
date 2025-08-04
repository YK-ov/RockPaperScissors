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
        if (challenger.getUsername().equals(challengeeLogin)) {
            challenger.sendMessage("Can't duel yourself");
        }

        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getUsername().equals(challengeeLogin) && clients.get(i).isDuelling() == true) {  //either getter for username or toString() used for object of an ClientHandler class
                challenger.sendMessage("The challengee is already duelling");
            }
            else if (clients.get(i).getUsername().equals(challengeeLogin)) {  //either getter for username or toString() used for object of an ClientHandler class
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
        duel.setOnEnd(new Duel.End() {
            @Override
            public void duelEnd() {
                duel.evaluate();
                Duel.Result duelResult = duel.evaluate();
                //duelResult.winner(); -- possible to get all lines (fields, winner and loser in this case) from a record like this
                if (duelResult == null) {
                    broadcastMessage("Duel between " + challenger.getUsername() + " and " + challengee.getUsername() + " ended in a tie");
                }
                else {
                    for (int i = 0; i < clients.size(); i++) {
                        if (clients.get(i).getUsername().equals(duelResult.winner().toString())) {
                            clients.get(i).sendMessage("You've won the duel");
                        }
                        if (clients.get(i).getUsername().equals(duelResult.loser().toString())) {
                            clients.get(i).sendMessage("You've lost the duel");
                        }
                    }
                    broadcastMessage(duelResult.winner() + " has defeated " + duelResult.loser() + " in a duel");
                }
            }
        });  // with anonymous block (way), can be done with a lambda statement

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
