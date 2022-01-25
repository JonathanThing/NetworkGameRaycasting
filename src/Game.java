/* RayCasting
 * Basic raycasting demo
 * @author Jonathan Cai
 * @version Dec 2021
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

public class Game {
    static JFrame gameWindow;
    static JFrame mapWindow;
    static GraphicsPanel canvas;
    static MapPanel mapThing;
    static RayCaster rayCaster;
    static MyKeyListener keyListener = new MyKeyListener();
    static MyMouseListener mouseListener = new MyMouseListener();
    static MyMouseMotionListener mouseMotionListener = new MyMouseMotionListener();
    static TextureManager textures;
    static TextureManager sprites;
    static TextureManager fireBall;
    static TextureManager personDirection;
    public static volatile Level currentLevel = new Level(new int[][] { { 2, 2, 2, 1, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 },
																        { 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2 }, 
																        { 1, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 6, 6, 0, 2 },
																        { 2, 0, 0, 0, 2, 2, 0, 0, 2, 0, 0, 0, 0, 6, 0, 2 }, 
																        { 1, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 6, 6, 0, 2 },
																        { 2, 0, 0, 0, 1, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 2 }, 
																        { 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2 },
																        { 2, 2, 4, 2, 1, 2, 4, 2, 2, 0, 0, 0, 3, 3, 0, 2 }, 
																        { 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 3, 3, 0, 2 },
																        { 2, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 2 }, 
																        { 2, 0, 0, 5, 5, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2 },
																        { 2, 0, 0, 5, 5, 0, 0, 0, 2, 0, 0, 0, 5, 5, 0, 2 }, 
																        { 2, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 5, 5, 0, 2 },
																        { 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2 }, 
																        { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 } });
    
    static Player player;
    static Player other;
    static boolean up, down, left, right, turnRight, turnLeft, shooting, otherShooting, twoPlayers = false;
    static Vector cameraOffset = new Vector(0, 0);
    static int playerID;
    static ReadFromServer rfsThread;
    static WriteToServer wtsThread;
    static Socket socket;
    public static volatile Environment[][] map;
    // 0 = singe player, 1 = coop, 2 = multiplayer, 3 = map editor
    static int gameState;
    public static volatile ArrayList<Entity> entities = new ArrayList<Entity>();
    static ArrayList<CharacterThread> characterThreads = new ArrayList<CharacterThread>();
    static ProjectilesThread projectilesThread = new ProjectilesThread();
    
    // ------------------------------------------------------------------------------
    public static void main(String[] args) {
        
        Menu menu = new Menu();
        while (menu.getState() == -1){
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        gameState = menu.getState();
        switch(gameState){
            case 0:
                init();
                break;
            case 3:
                // map editor
                init();
                break;
            default:
                twoPlayers = true;
                init();
                break;
        }
    }
    
    // ------------------------------------------------------------------------------
    public static void init(){
        try {
            textures = new TextureManager(ImageIO.read(new File("images/WallTextures.png")));
            sprites = new TextureManager(ImageIO.read(new File("images/spriteSheet.png")));
            personDirection = new TextureManager(ImageIO.read(new File("images/PersonDirectionAnimation.png")));
            fireBall = new TextureManager(ImageIO.read(new File("images/FireBallAnimation.png")));
        } catch (IOException e) {
            System.out.println("failed to get image");
            e.printStackTrace();
        }
        generateMap(currentLevel.getMap());
        if (twoPlayers) {
            other = new Player(new Vector((3) * Const.BOX_SIZE - Const.BOX_SIZE / 2, (2) * Const.BOX_SIZE - Const.BOX_SIZE / 2), 10, 10, "player", new Angle(3 * Math.PI / 2), sprites, 100, 4, 120, 0.75, null);
            entities.add(other);
            connect(sprites);
        }
        player = new Player(new Vector((3) * Const.BOX_SIZE - Const.BOX_SIZE / 2, (2) * Const.BOX_SIZE - Const.BOX_SIZE / 2), 10, 10, "player", new Angle(3 * Math.PI / 2), sprites, 100, 4, 120, 0.75, null);

        Game.addCharacterEntity(new Zombie(new Vector(400, 300), 10, 10, "zombie", new Angle(Math.PI/2),
                                          new TextureManager(personDirection), 50, 4, 120, 0.75, null));
        Game.addCharacterEntity(new Skeleton(new Vector(200, 200), 10, 10, "skeleton", new Angle(Math.PI/2),
                                             new TextureManager(personDirection), 50, 4, 120, 0.75, null));
        projectilesThread.start();
        gameWindow = new JFrame("Game Window");
        gameWindow.setSize(Const.SCREEN_WIDTH, Const.SCREEN_HEIGHT);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new GraphicsPanel();
        canvas.addMouseListener(mouseListener);
        canvas.addMouseMotionListener(mouseMotionListener);
        canvas.addKeyListener(keyListener);
        gameWindow.add(canvas);
        
        mapWindow = new JFrame("Map");
        mapWindow.setSize(Const.SCREEN_WIDTH, Const.SCREEN_HEIGHT);
        mapWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        entities.add(player);
        mapThing = new MapPanel();
        mapWindow.add(mapThing);
        
        rayCaster = new RayCaster(textures);
        rayCaster.updateInformation(player, cameraOffset, currentLevel, Math.PI / 2);
        mapWindow.setVisible(true);
        gameWindow.setVisible(true);
        runGameLoop();
    }
    
    public static void generateMap(int[][] tempMap){
        map = new Environment[tempMap.length][tempMap[0].length];
        for (int i = 0; i < tempMap.length; i++) {
            for (int j = 0; j < tempMap[0].length; j++) {
                
                switch (tempMap[i][j]) {
                    case 1: // Creates a Wall object
                        map[i][j] = new Wall(new Vector(j * Const.BOX_SIZE + Const.BOX_SIZE / 2, i * Const.BOX_SIZE + Const.BOX_SIZE / 2), "wall", textures.getSingleTexture(1, 0));                        
                        break;
                        
                    case 2: // Creates a special wall object                      
                        map[i][j] = new Wall(new Vector(j * Const.BOX_SIZE + Const.BOX_SIZE / 2, i * Const.BOX_SIZE + Const.BOX_SIZE / 2), "wall", textures.getSingleTexture(2, 0));
                        break;
                        
                    case 3: 
                        map[i][j] = new Wall(new Vector(j * Const.BOX_SIZE + Const.BOX_SIZE / 2, i * Const.BOX_SIZE + Const.BOX_SIZE / 2), "wall", textures.getSingleTexture(3, 0));
                        break;
                        
                    case 4: 
                        map[i][j] = new Door(new Vector(j * Const.BOX_SIZE + Const.BOX_SIZE / 2, i * Const.BOX_SIZE + Const.BOX_SIZE / 2), "door", textures.getSingleTexture(4, 0));
                        break;
                        
                    case 5:
                        map[i][j] = new Wall(new Vector(j * Const.BOX_SIZE + Const.BOX_SIZE / 2, i * Const.BOX_SIZE + Const.BOX_SIZE / 2), "wall", textures.getSingleTexture(5, 0));
                        break;
                        
                    case 6: 
                        map[i][j] = new Wall(new Vector(j * Const.BOX_SIZE + Const.BOX_SIZE / 2, i * Const.BOX_SIZE + Const.BOX_SIZE / 2), "wall", textures.getSingleTexture(6, 0));
                        break;       
                }
            }
        }
    }
    
    // ------------------------------------------------------------------------------
    public static void connect(TextureManager sprites) {
        try {
            // Connects to port 45371
            socket = new Socket("localhost", 45371);
            System.out.println(socket);
            
            // Input and Output streams to send and receive information to and from the server
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            
            playerID = in.readInt();
            System.out.println("You Are Player" + playerID);
            gameWindow.setTitle("Player " + playerID);
            
            // Create the read and write threads and then await start
            rfsThread = new ReadFromServer(in);
            wtsThread = new WriteToServer(out);
            rfsThread.waitForStart();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // ------------------------------------------------------------------------------
    public static void runGameLoop() {
        while (true) {
            try {
                Thread.sleep(25);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (twoPlayers){
                if (otherShooting){
                    other.shoot(fireBall);
                    otherShooting = false;
                }
            }
            player.movement(up, down, left, right, turnLeft, turnRight, currentLevel);
            if (shooting) {
                player.shoot(fireBall);
                shooting = false;
            }
            
            rayCaster.updateInformation(player, cameraOffset, currentLevel, Const.FOV);
            gameWindow.repaint();
            mapWindow.repaint();
        }
    }
    
    public static void addCharacterEntity(Character entity) {
        synchronized(entities) {
            entities.add(entity);
        }
        
        CharacterThread thread = new CharacterThread(entity);
        thread.start();
        
        characterThreads.add(thread);
    }
    
    public static void removeCharacterEntity(Entity entity) {
        synchronized(entities) {
            entities.remove(entity);
        }
    }
    
    public static void addProjectileEntity(Projectile entity) {
        synchronized(entities) {
            entities.add(entity);
        }
        projectilesThread.addProjectile(entity);
    }
    
    public static void removeProjectileEntity(UUID uuid) {
        
        synchronized(entities) {
            for (int i = 0; i < entities.size(); i++){
                Entity temp = entities.get(i);
                if (uuid.equals(temp.getUUID())){
                    entities.remove(temp);                   
                }
            }
        }      
    }
    
    public static synchronized ArrayList<Entity> copyEntities() {
        ArrayList<Entity> allEntities = new ArrayList<Entity>();
        for (Entity thing : entities) {
            allEntities.add(thing);
        }
        return allEntities;
    }
    
    // ------------------------------------------------------------------------------
    static class GraphicsPanel extends JPanel {
        public GraphicsPanel() {
            setFocusable(true);
            requestFocusInWindow();
        }
        
        public void paintComponent(Graphics g) {
            super.paintComponent(g); // required
            
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.GRAY);
            g2.fillRect(0, 0, Const.WIDTH+1, Const.HEIGHT / 2);
            g2.setColor(Color.GRAY.darker().darker());
            g2.fillRect(0, Const.HEIGHT / 2, Const.WIDTH+1, Const.HEIGHT/2+1);
            rayCaster.rayCastWalls(g2, false);
            
            ArrayList<Entity> allEntities = copyEntities();
            
            rayCaster.drawSprite(g2, allEntities);

            g2.setColor(Color.GREEN);
            g2.fillRect(Const.WIDTH/2-1, Const.HEIGHT/2-4, 2,8);
            g2.fillRect(Const.WIDTH/2-4, Const.HEIGHT/2-1, 8,2);
            
            g2.setColor(Color.RED);
            g2.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
            g2.drawString(Double.toString(player.getHealth()), 10,Const.HEIGHT-20);
            g2.setColor(Color.YELLOW);
            g2.drawString(Double.toString(player.getAmmo()), 120,Const.HEIGHT-20);
        } // paintComponent method end
    } // GraphicsPanel class end
    
    static class MapPanel extends JPanel {
        public MapPanel() {
            setFocusable(true);
            requestFocusInWindow();
        }
        
        public void paintComponent(Graphics g) {
            super.paintComponent(g); // required
            
            Graphics2D g2 = (Graphics2D) g;
           
            g2.setStroke(new BasicStroke(4));
            g2.setColor(Color.BLACK);
            for (int rows = 0; rows < currentLevel.getRows(); rows++) {
                for (int columns = 0; columns < currentLevel.getColumns(); columns++) {
                    if (currentLevel.getMapTile(rows, columns) == 0) {
                        g2.setColor(Color.WHITE);
                        g2.drawRect(columns * Const.BOX_SIZE + (int) cameraOffset.getX(),
                                    rows * Const.BOX_SIZE + (int) cameraOffset.getY(), Const.BOX_SIZE, Const.BOX_SIZE);
                    } else if (currentLevel.getMapTile(rows, columns) >= 1) {
                        g2.setColor(Color.BLACK);
                        g2.fillRect(columns * Const.BOX_SIZE + +(int) cameraOffset.getX(),
                                    rows * Const.BOX_SIZE + (int) cameraOffset.getY(), Const.BOX_SIZE, Const.BOX_SIZE);
                    }
                    g2.setColor(Color.BLACK);
                    g2.drawRect(columns * Const.BOX_SIZE + +(int) cameraOffset.getX(),
                                rows * Const.BOX_SIZE + (int) cameraOffset.getY(), Const.BOX_SIZE, Const.BOX_SIZE);
                }
            }
            
            rayCaster.rayCastWalls(g2, true);
            
            for (Entity entity : entities) {
                
                if (entity instanceof Skeleton) {
                    g.setColor(Color.RED);
                   
                } else if (entity instanceof Zombie) {
                    g.setColor(Color.GREEN);
                } else if (entity instanceof Projectile) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.ORANGE);
                }
                
                g2.fillRect((int) (entity.getPosition().getX() - entity.getWidth() / 2 + cameraOffset.getX()),
                            (int) (entity.getPosition().getY() - entity.getHeight() / 2 + cameraOffset.getY()),
                            entity.getWidth(), entity.getHeight());
            
                g2.drawLine((int) (entity.getPosition().getX() + cameraOffset.getX()), (int) (entity.getPosition().getY() + cameraOffset.getY()), (int) (entity.getPosition().getX() + cameraOffset.getX() + Math.cos(entity.getAngle().getValue())*200), (int) (entity.getPosition().getY() + cameraOffset.getY() - Math.sin(entity.getAngle().getValue())*200));
            }       

            
        } // paintComponent method end
    } // GraphicsPanel class end
    
    // ------------------------------------------------------------------------------
    static class MyKeyListener implements KeyListener {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case 'W':
                    up = true;
                    break;
                case 'S':
                    down = true;
                    break;
                case 'A':
                    left = true;
                    break;
                case 'D':
                    right = true;
                    break;
                case 'E':
                    turnRight = true;
                    break;
                case 'Q':
                    turnLeft = true;
                    break; 
            }
            
			if (e.getKeyCode() == 'J'){
				cameraOffset.changeX(Const.BOX_SIZE);
            } else if (e.getKeyCode() =='L'){
            	cameraOffset.changeX(-Const.BOX_SIZE);
            } else if (e.getKeyCode() == 'I'){
            	cameraOffset.changeY(Const.BOX_SIZE);
            } else if (e.getKeyCode() == 'K'){
            	cameraOffset.changeY(-Const.BOX_SIZE);
            }   
        }
        
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case 'W':
                    up = false;
                    break;
                case 'S':
                    down = false;
                    break;
                case 'A':
                    left = false;
                    break;
                case 'D':
                    right = false;
                    break;
                case 'E':
                    turnRight = false;
                    break;
                case 'Q':
                    turnLeft = false;
                    break;
            }
        }
        
        public void keyTyped(KeyEvent e) {

        }
    }
    
    // ------------------------------------------------------------------------------
    static class MyMouseListener implements MouseListener {
        public void mouseClicked(MouseEvent e) {
        }
        
        public void mousePressed(MouseEvent e) {
            shooting = true;
        }
        
        public void mouseReleased(MouseEvent e) {
            shooting = false;
        }
        
        public void mouseEntered(MouseEvent e) {
        }
        
        public void mouseExited(MouseEvent e) {
        }
    }
    
    // ------------------------------------------------------------------------------
    static class MyMouseMotionListener implements MouseMotionListener {
        public void mouseMoved(MouseEvent e) {
        }
        
        public void mouseDragged(MouseEvent e) {
        }
    }
    
    // ------------------------------------------------------------------------------
    private static class ReadFromServer implements Runnable {
        
        private final DataInputStream in;
        
        // Constructor
        public ReadFromServer(DataInputStream in) {
            this.in = in;
            System.out.println("Read From Server Thread Created");
        }
        
        @Override
        public void run() {
            try {
                // Runs forever in the background
                while (true) {
                    if (other != null) {
                        // Set the location of the other sprite based on data from the server
                        other.setPosition(new Vector(in.readDouble(), in.readDouble()));
                        other.setAngle(new Angle(in.readDouble()));
                        if (in.readBoolean()) {
                            otherShooting = true;
                        } else {
                            otherShooting = false;
                        }
                        Thread.sleep(25);
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // ------------------------------------------------------------------------------
        public void waitForStart() {
            try {
                String start = in.readUTF();
                System.out.println("Msg From Server: " + start);
                
                // Create and start read and write threads when the server allows us to start
                Thread readThread = new Thread(rfsThread);
                Thread writeThread = new Thread(wtsThread);
                System.out.println("Threads Started");
                readThread.start();
                writeThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    // This method writes data to the server
    private static class WriteToServer implements Runnable {
        
        private final DataOutputStream out;
        
        // Constructor
        public WriteToServer(DataOutputStream out) {
            this.out = out;
            System.out.println("Write To Server Thread Created");
        }
        
        @Override
        public void run() {
            try {
                // Run forever in the background
                while (true) {
                    if (player != null) {
                        // Tell the server your x and y coordinates
                        out.writeDouble(player.getPosition().getX());
                        out.writeDouble(player.getPosition().getY());
                        out.writeDouble(player.getAngle().getValue());
                        out.writeBoolean(shooting);
                        out.flush();
                    }
                    try {
                        // Sleep for 25ms to allow time for the server to receive the data
                        Thread.sleep(25);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}