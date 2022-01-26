package core;
/* RayCasting
 * Basic raycasting demo
 * @author Jonathan Cai
 * @version Dec 2021
 */

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import gameObjects.Player;
import gameObjects.Projectile;
import gameObjects.Skeleton;
import gameObjects.Character;
import gameObjects.Door;
import gameObjects.Entity;
import gameObjects.Environment;
import gameObjects.Wall;
import gameObjects.Zombie;
import misc.Level;
import misc.TextureManager;
import threads.CharacterThread;
import threads.ProjectilesThread;
import util.Angle;
import util.Const;
import util.Vector;


public class Game {
    public static JFrame gameWindow;
    public static JFrame mapWindow;
    public static GraphicsPanel canvas;
    public static MapPanel mapThing;
    public static RayCaster rayCaster;
    public static MyKeyListener keyListener = new MyKeyListener();
    public static MyMouseListener mouseListener = new MyMouseListener();
    public static MyMouseMotionListener mouseMotionListener = new MyMouseMotionListener();
    public static TextureManager textures;
    public static TextureManager sprites;
    public static TextureManager fireBall;
    public static TextureManager personDirection;
    public volatile static char[][] bufferMap;
    
    public static Player player;
    public static Player other;
    public static boolean up, down, left, right, turnRight, turnLeft, shooting, otherShooting, twoPlayers = false;
    public static Vector cameraOffset = new Vector(0, 0);
    public static int playerID;
    public static ReadFromServer rfsThread;
    public static WriteToServer wtsThread;
    public static Socket socket;
    public static volatile Level map;
    // 0 = singe player, 1 = coop, 2 = multiplayer, 3 = map editor
    public  static int gameState;
    public static volatile ArrayList<Entity> entities = new ArrayList<Entity>();
    public static ArrayList<CharacterThread> characterThreads = new ArrayList<CharacterThread>();
    public static ProjectilesThread projectilesThread = new ProjectilesThread();
    public static double deltaX;
    public static Robot robot;
    public static boolean mouseMove = true;
    public static Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank cursor");
    
    
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
				try {
					robot = new Robot();
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
        
        try {
			bufferMap = getMapFile("maps/map.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        generateMap(bufferMap);
        
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
        rayCaster.updateInformation(player, cameraOffset, map);
        mapWindow.setVisible(true);
        gameWindow.setVisible(true);
       
        gameWindow.setCursor(blankCursor);
        
        runGameLoop();
    }
    
    public static char[][] getMapFile(String fileName) throws FileNotFoundException {
    	File myFile = new File(fileName);
        System.out.println("Attempting to read data from file: " + fileName);
        Scanner input = new Scanner(myFile);

        int x = input.nextInt();
        int y = input.nextInt();
        
        String line = "";
        
        input.nextLine();
        
        char[][] map = new char[y][x];
        
        for (int i = 0; i < y; i++) { 
          
          line = input.nextLine();
          
          for (int j = 0; j < x; j++) {
            
            map[i][j] = line.charAt(j);
            
          }
        }
        
        return map;
    }
    
    public static void generateMap(char[][] tempMap){
        map = new Level(new Environment[tempMap.length][tempMap[0].length]);
        for (int i = 0; i < tempMap.length; i++) {
            for (int j = 0; j < tempMap[0].length; j++) {
                
            	int xPosition = j * Const.BOX_SIZE + Const.BOX_SIZE / 2;
            	int yPosition = i * Const.BOX_SIZE + Const.BOX_SIZE / 2;
            	
                switch (tempMap[i][j]) {
                    case 'f': // Creates a Wall object
                        map.setMapTile(i, j, new Wall(new Vector(xPosition,yPosition), "wall", textures.getWallTexture(0)));                        
                        break;
                        
                    case 'b': // Creates a special wall object                      
                    	map.setMapTile(i, j, new Wall(new Vector(xPosition,yPosition), "wall", textures.getWallTexture(1)));                        
                        break;
                        
                    case 'p': 
                    	map.setMapTile(i, j, new Wall(new Vector(xPosition,yPosition), "wall", textures.getWallTexture(2)));                        
                        break;
                        
                    case 'd': 
                    	map.setMapTile(i, j, new Door(new Vector(xPosition,yPosition), "door", textures.getWallTexture(8)));
                        break;
                        
                    case 'c':
                    	map.setMapTile(i, j, new Wall(new Vector(xPosition,yPosition), "wall", textures.getWallTexture(3)));                        
                        break;
                        
                    case 'B': 
                    	map.setMapTile(i, j, new Wall(new Vector(xPosition,yPosition), "wall", textures.getWallTexture(4)));                        
                        break;  
                        
                    case 'C':
                    	map.setMapTile(i, j, new Wall(new Vector(xPosition,yPosition), "wall", textures.getWallTexture(5)));                        
                        break;
                    case 'w':
                    	map.setMapTile(i, j, new Wall(new Vector(xPosition,yPosition), "wall", textures.getWallTexture(6)));                        
                        break;
                    case 'r':
                    	map.setMapTile(i, j, new Wall(new Vector(xPosition,yPosition), "wall", textures.getWallTexture(7)));                        
                        break;
                    case 'P':
                    	if (twoPlayers) {
                            other = new Player(new Vector(xPosition,yPosition), 10, 10, "player", new Angle(3 * Math.PI / 2), sprites, 100, 4, 120, 0.75, null);
                            entities.add(other);
                            connect(sprites);
                        }
                        player = new Player(new Vector(xPosition,yPosition), 10, 10, "player", new Angle(3 * Math.PI / 2), sprites, 100, 4, 120, 0.75, null);
                        entities.add(player);
                        break;
                        
                    case 'S': 
                        addCharacterEntity(new Skeleton(new Vector(xPosition,yPosition) , 10, 10, "skeleton", new Angle(Math.PI/2),
                                new TextureManager(personDirection), 50, 4, 120, 0.75, null));
                    	break;  
                    	
                    case 'Z': 
                        addCharacterEntity(new Zombie(new Vector(xPosition,yPosition), 10, 10, "zombie", new Angle(Math.PI/2),
                                new TextureManager(personDirection), 50, 4, 120, 0.75, null));

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
            
            if (!mouseMove) {
            	gameWindow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
            	gameWindow.setCursor(blankCursor);

            }
            
            if (twoPlayers){
                if (otherShooting){
                    other.shoot(fireBall);
                    otherShooting = false;
                }
            }
            player.movement(up, down, left, right, turnLeft, turnRight, map, deltaX);
            if (shooting) {
                player.shoot(fireBall);
                shooting = false;
            }
            
            rayCaster.updateInformation(player, cameraOffset, map);
            gameWindow.repaint();
            mapWindow.repaint();

        }
    }
    
    public static void addCharacterEntity(Entity entity) {
        synchronized(entities) {
            entities.add(entity);
        }
        
        CharacterThread thread = new CharacterThread((Character) entity);
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
            for (int rows = 0; rows < map.getRows(); rows++) {
                for (int columns = 0; columns < map.getColumns(); columns++) {
                    if (map.getMapTile(rows, columns) == null) {
                        g2.setColor(Color.WHITE);
                        g2.drawRect(columns * Const.BOX_SIZE + (int) cameraOffset.getX(),
                                    rows * Const.BOX_SIZE + (int) cameraOffset.getY(), Const.BOX_SIZE, Const.BOX_SIZE);
                    } else if (map.getMapTile(rows, columns) instanceof Environment) {
                        g2.setColor(Color.BLACK);
                        g2.fillRect(columns * Const.BOX_SIZE + (int) cameraOffset.getX(),
                                    rows * Const.BOX_SIZE + (int) cameraOffset.getY(), Const.BOX_SIZE, Const.BOX_SIZE);
                    }
                    g2.setColor(Color.BLACK);
                    g2.drawRect(columns * Const.BOX_SIZE + (int) cameraOffset.getX(),
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
                case KeyEvent.VK_ESCAPE:
                	mouseMove = !mouseMove;
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
        	if (mouseMove) {
        		deltaX = e.getX() - Const.WIDTH/2.0;
        		robot.mouseMove(Const.SCREEN_WIDTH/2 + gameWindow.getX(),Const.SCREEN_HEIGHT/2 + gameWindow.getY());
        	} else {
        		deltaX = 0;
        	}
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