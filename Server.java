import java.io.*;
import java.net.*;

public class Server {
    // Server sockets
    private ServerSocket serverSocket;
    private Socket p1Socket;
    private Socket p2Socket;

    // Reading and Writing threads
    private ReadFromClient readFromP1;
    private ReadFromClient readFromP2;

    private WriteToClient writeToP1;
    private WriteToClient writeToP2;

    // The x and y coords of both players
    private int player1x, player2x, player1y, player2y;

    // Tells us how many players are connected and how many are allowed to connect
    private int connectedPlayers;
    private int maxPlayers;

    // Main
    // Start the server and await both players
    public static void main(String[] args) {
        Server server = new Server();
        server.awaitPlayers();
    }

    // Constructor
    public Server() {
        System.out.println("===== SERVER STARTED =====");
        connectedPlayers = 0;
        maxPlayers = 2;

        // Initialize player coords
        player1x = 100;
        player1y = 100;
        player2x = 200;
        player2y = 200;

        // Create a new server socket at port 45371
        try {
            serverSocket = new ServerSocket(45371);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Invalid Port");
        }
    }

    // This method waits for both players to connect and sets them up
    public void awaitPlayers() {
        try {
            System.out.println("Awaiting Connection...");

            // Loop until all players are connected
            while (connectedPlayers < maxPlayers) {
                // Accept the incoming connection
                Socket socket = serverSocket.accept();

                // These vars will send and recieve data to and from the server and other client
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                // Iterate
                connectedPlayers++;
                int playerID = connectedPlayers;

                // Debugging/Console msgs
                out.writeInt(playerID);
                System.out.println("Player " + playerID + " Has Connected");

                // Create new reading and writing threads
                ReadFromClient readFromClient = new ReadFromClient(playerID, in);
                WriteToClient writeToClient = new WriteToClient(playerID, out);

                // When player1 connects, setup their threads
                if (playerID == 1) {
                    p1Socket = socket;
                    readFromP1 = readFromClient;
                    writeToP1 = writeToClient;
                }
                // When player2 connects, setup their threads
                else {
                    p2Socket = socket;
                    readFromP2 = readFromClient;
                    writeToP2 = writeToClient;

                    // Send the start msg when player two connects, this will give the client
                    // permission to run the game
                    writeToP1.sendStart();
                    writeToP2.sendStart();

                    // Create and start our reading and writing threads
                    Thread readOne = new Thread(readFromP1);
                    Thread readTwo = new Thread(readFromP2);
                    readOne.start();
                    readTwo.start();
                    Thread writeOne = new Thread(writeToP1);
                    Thread writeTwo = new Thread(writeToP2);
                    writeOne.start();
                    writeTwo.start();
                }
            }

            System.out.println("All Players Connected");

        } catch (IOException e) {
            System.out.println("Could Not Connect");
        }
    }

    // This method reads data from both clients
    private class ReadFromClient implements Runnable {

        private int playerID;
        private DataInputStream in;

        // Constructor
        public ReadFromClient(int playerID, DataInputStream in) {
            this.playerID = playerID;
            this.in = in;
            System.out.println("Thread ReadFromClient created for Player " + playerID);
        }

        @Override
        public void run() {
            try {
                // This thread runs forever in the background
                while (true) {
                    // If the client is player1, assign its corresponding x and y variables
                    if (playerID == 1) {
                        player1x = in.readInt();
                        player1y = in.readInt();
                        // If the client is player2, assign its corresponding x and y variables
                    } else {
                        player2x = in.readInt();
                        player2y = in.readInt();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                ;
            }
        }
    }

    // This method writes to our client
    private class WriteToClient implements Runnable {

        private int playerID;
        private DataOutputStream out;

        // Constructor
        public WriteToClient(int playerID, DataOutputStream out) {
            this.playerID = playerID;
            this.out = out;
            System.out.println("Thread WriteToClient created for Player " + playerID);
        }

        @Override
        public void run() {
            try {
                // This thread runs forever in the background
                while (true) {
                    // If the client is player1, tell it the x and y coordinates of player2
                    if (playerID == 1) {
                        out.writeInt(player2x);
                        out.writeInt(player2y);
                        out.flush();
                        // If the client is player2, tell it the x and y coordinates of player1
                    } else {
                        out.writeInt(player1x);
                        out.writeInt(player1y);
                        out.flush();
                    }
                    try {
                        // Sleep every time we send data, to give time for the clients to recieve
                        // information
                        Thread.sleep(25);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Send a start message that will tell the client when both players are
        // connected
        public void sendStart() {
            try {
                out.writeUTF("Both Players Connected. Ready to Start");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
