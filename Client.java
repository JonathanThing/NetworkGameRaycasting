import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.io.*;
import java.net.*;

public class Client extends JFrame {

    // Both Players
    private Player me;
    private Player other;

    // Server Socket
    private Socket socket;
    private int playerID;

    // Reading and Writing threads
    private ReadFromServer rfsThread;
    private WriteToServer wtsThread;

    final int MAX_X = (int) getToolkit().getScreenSize().getWidth();
    final int MAX_Y = (int) getToolkit().getScreenSize().getHeight();

    // Main (runs a client instance)
    public static void main(String[] args) throws IOException {
        Client gameClient = new Client();
    }

    // Constructor
    Client() {
        Panel panel = new Panel();

        // Graphics Things
        Dimension frameSize = new Dimension(MAX_X / 2, MAX_Y / 2);
        connect();
        this.add(panel);
        this.setSize(frameSize);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(this.getMinimumSize());
        this.addKeyListener(new keyListener());
        this.setVisible(true);
    }

    // Connects the client to the server
    public void connect(){
        try {
            // Connects to port 45371
            socket = new Socket("localhost", 45371);

            // Input and Output streams to send and recieve information to and from the server
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            playerID = in.readInt();
            System.out.println("You Are Player" + playerID);
            this.setTitle("Player " + playerID);

            // Create the read and write threads and then await start
            rfsThread = new ReadFromServer(in);
            wtsThread = new WriteToServer(out);
            rfsThread.waitForStart();

            // Create the player objects, based on who's who
            if (playerID == 1){
                me = new Player(100, 100, 100, 100, Color.RED);
                other = new Player(100, 100, 200, 200, Color.BLUE);
            }
            else {
                me = new Player(100, 100, 200, 200, Color.BLUE);
                other = new Player(100, 100, 100, 100, Color.RED);
            }

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    // JPanel
    private class Panel extends JPanel {
        // Constructor
        public Panel() {
          this.setBackground (Color.WHITE);
        }
    
        public void paintComponent(java.awt.Graphics g){ 
            super.paintComponent(g);
            
            // Draw rects based on the locations of the players and their size
            g.setColor(Color.BLACK);
            me.drawSprite(g);
            other.drawSprite(g);
            this.repaint();
        }
    }

    // Keyboard Event Listener
    private class keyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {

            // --------------------Move (Arrow Keys)--------------------
            // Right Arrow
            if (e.getKeyCode() == 39){
                // Move 20 on the x axis
                me.changeX(20);
            }
            // Left Arrow
            if (e.getKeyCode() == 37){
                // Move -20 on the x axis
                me.changeX(-20);
            }
            // Up Arrow
            if (e.getKeyCode() == 38){
                // Move 20 on the y axis
                me.changeY(-20);
            }

            // Down Arrow
            if (e.getKeyCode() == 40){
                // Move -20 on the y axis
                me.changeY(20);
            }
        }
    }

    // This thread reads data from the server
    private class ReadFromServer implements Runnable {

        private DataInputStream in;

        // Constructor
        public ReadFromServer(DataInputStream in){
            this.in = in;
            System.out.println("Read From Server Thread Created");
        }

        @Override
        public void run() {
            try {
                // Runs forever in the background
                while (true){
                    if (other != null){
                        // Set the location of the other sprite based on data from the server
                        other.setX(in.readInt());
                        other.setY(in.readInt());
                    }
                }
            } catch(IOException e){
                e.printStackTrace();
            }
        }

        public void waitForStart(){
            try {
                String start = in.readUTF();
                System.out.println("Msg From Server: " + start);

                // Create and start read and write threads when the server allows us to start
                Thread readThread = new Thread(rfsThread);
                Thread writeThread = new Thread(wtsThread);
                readThread.start();
                writeThread.start();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    // This method writes data to the server
    private class WriteToServer implements Runnable {

        private DataOutputStream out;

        // Constructor
        public WriteToServer(DataOutputStream out){
            this.out = out;
            System.out.println("Write To Server Thread Created");
        }

        @Override
        public void run() {
            try {
                // Run forever in the background
                while (true){
                    if (me != null){
                        // Tell the server your x and y coordinates
                        out.writeInt(me.getX());
                        out.writeInt(me.getY());
                        out.flush();
                    }
                    try {
                        // Sleep for 25ms to allow time for the server to recieve the data
                        Thread.sleep(25);
                    } catch(InterruptedException ex){
                        ex.printStackTrace();
                    }
                }
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}

