import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final int maxPlayers;
    // Server sockets
    private ServerSocket serverSocket;
    // Reading and Writing threads
    private ReadFromClient readFromP1;
    private WriteToClient writeToP1;
    // The x and y coords of both players
    private double player1x, player2x, player1y, player2y, angle1, angle2;
    private boolean p1shooting, p2shooting = false;
    // Tells us how many players are connected and how many are allowed to connect
    private int connectedPlayers;

    // Constructor
    public Server() {
        System.out.println("===== SERVER STARTED =====");
        connectedPlayers = 0;
        maxPlayers = 2;

        // Create a new server socket at port 45371
        try {
            serverSocket = new ServerSocket(45371);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Invalid Port");
        }
    }

    // Main
    // Start the server and await both players
    public static void main(String[] args) {
        Server server = new Server();
        server.awaitPlayers();
    }

    // This method waits for both players to connect and sets them up
    public void awaitPlayers() {
        try {
            System.out.println("Awaiting Connection...");

            // Loop until all players are connected
            while (connectedPlayers < maxPlayers) {
                // Accept the incoming connection
                Socket socket = serverSocket.accept();

                // These vars will send and receive data to and from the server and other client
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream());

                // Iterate
                connectedPlayers++;
                int playerID = connectedPlayers;

                // Debugging/Console messages
                out.writeInt(playerID);
                System.out.println("Player " + playerID + " Has Connected");

                // Create new reading and writing threads
                ReadFromClient readFromClient = new ReadFromClient(playerID, in);
                WriteToClient writeToClient = new WriteToClient(playerID, out);

                // When player1 connects, setup their threads
                if (playerID == 1) {
                    readFromP1 = readFromClient;
                    writeToP1 = writeToClient;
                }
                // When player2 connects, setup their threads
                else {

                    // Send the start msg when player two connects, this will give the client
                    // permission to run the game
                    writeToP1.sendStart();
                    writeToClient.sendStart();

                    // Create and start our reading and writing threads
                    Thread readOne = new Thread(readFromP1);
                    Thread readTwo = new Thread(readFromClient);
                    readOne.start();
                    readTwo.start();
                    Thread writeOne = new Thread(writeToP1);
                    Thread writeTwo = new Thread(writeToClient);
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

        private final int playerID;
        private final DataInputStream in;

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
                        player1x = in.readDouble();
                        player1y = in.readDouble();
                        angle1 = in.readDouble();
                        p1shooting = in.readBoolean();
                        // If the client is player2, assign its corresponding x and y variables
                    } else {
                        player2x = in.readDouble();
                        player2y = in.readDouble();
                        angle2 = in.readDouble();
                        if (in.readBoolean()) {
                            p2shooting = true;
                        } else {
                            p1shooting = false;
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // This method writes to our client
    private class WriteToClient implements Runnable {

        private final int playerID;
        private final DataOutputStream out;

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
                        out.writeDouble(player2x);
                        out.writeDouble(player2y);
                        out.writeDouble(angle2);
                        if (p2shooting) {
                            out.writeBoolean(true);
                            p2shooting = false;
                        } else {
                            out.writeBoolean(false);
                        }

                        // If the client is player2, tell it the x and y coordinates of player1
                    } else {
                        out.writeDouble(player1x);
                        out.writeDouble(player1y);
                        out.writeDouble(angle1);
                        if (p1shooting) {
                            out.writeBoolean(true);
                            p1shooting = false;
                        } else {
                            out.writeBoolean(false);
                        }
                    }
                    out.flush();
                    try {
                        // Sleep every time we send data, to give time for the clients to receive
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