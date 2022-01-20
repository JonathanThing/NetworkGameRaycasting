
/* RayCasting
 * Basic raycasting demo
 * @author Jonathan Cai
 * @version Dec 2021
 */
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Game {
    static JFrame gameWindow;
    static JFrame mapWindow;
    static GraphicsPanel canvas;
    static MapPanel mapThing;
    static RayCaster rayCaster;
    static MyKeyListener keyListener = new MyKeyListener();
    static MyMouseListener mouseListener = new MyMouseListener();
    static MyMouseMotionListener mouseMotionListener = new MyMouseMotionListener();
    static TextureList textures;
    static TextureList sprites;
    static Level currentLevel = new Level(new int[][] { { 2, 2, 2, 1, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 },
        { 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2 }, { 1, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 6, 6, 0, 2 },
        { 2, 0, 0, 0, 2, 2, 0, 0, 2, 0, 0, 0, 0, 6, 0, 2 }, { 1, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 6, 6, 0, 2 },
        { 2, 0, 0, 0, 1, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 2 }, { 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2 },
        { 2, 2, 4, 2, 1, 2, 4, 2, 2, 0, 0, 0, 3, 3, 0, 2 }, { 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 3, 3, 0, 2 },
        { 2, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 2 }, { 2, 0, 0, 5, 5, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2 },
        { 2, 0, 0, 5, 5, 0, 0, 0, 2, 0, 0, 0, 5, 5, 0, 2 }, { 2, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 5, 5, 0, 2 },
        { 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2 }, { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 } });
    
    public static volatile Player player = new Player(
                                             new Vector((3) * Const.BOXSIZE - Const.BOXSIZE / 2, (2) * Const.BOXSIZE - Const.BOXSIZE / 2), 10, 10,
                                             "player", new Angle(3 * Math.PI / 2), null, 100, 4, 20, 0.75, null);
    static boolean up, down, left, right, turnRight, turnLeft, shooting;
    static double deltaX;
    static Vector cameraOffset = new Vector(0, 0);
    static Robot robot;
    public static volatile ArrayList<Entity> entities = new ArrayList<Entity>();
    static ArrayList<CharacterThread> characterThreads = new ArrayList<CharacterThread>();
    static ProjectilesThread projectilesThread = new ProjectilesThread();
    
    
    // ------------------------------------------------------------------------------
    public static void main(String[] args) {
        
        try {
            textures = new TextureList(
                                       ImageIO.read(new File("textures.png")));
            sprites = new TextureList(
                                      ImageIO.read(new File("spriteSheet.png")));
        } catch (IOException e) {
            System.out.println("failed to get image");
            e.printStackTrace();
        }
        
        Game.addCharacterEntity(new Zombie(new Vector(400, 300), 30, 30, "zombie", new Angle(2),
                                           sprites.getSingleTexture(0), 100, 4, 20, 0.75, null));
        
        // Skeleton skeleton = new Skeleton(new Vector(200, 200), 30, 30, "skeleton",
        // new Angle(2), sprites.getSingleTexture(0), 100, 4, 0,0.75, null);
        Game.addCharacterEntity(new Skeleton(new Vector(200, 200), 30, 30, "skeleton", new Angle(2),
                                             sprites.getSingleTexture(0), 100, 4, 0, 0.75, null));
        entities.add(player);
        
        projectilesThread.start();
        
        gameWindow = new JFrame("Game Window");
        gameWindow.setSize(Const.TRUE_WIDTH, Const.TRUE_HEIGHT);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new GraphicsPanel();
        canvas.addMouseListener(mouseListener);
        canvas.addMouseMotionListener(mouseMotionListener);
        canvas.addKeyListener(keyListener);
        gameWindow.add(canvas);
        
        mapWindow = new JFrame("Map");
        mapWindow.setSize(Const.WIDTH, Const.HEIGHT);
        mapWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mapThing = new MapPanel();
        mapWindow.add(mapThing);
        
        rayCaster = new RayCaster(textures);
        rayCaster.updateInformation(player, cameraOffset, currentLevel, Math.PI / 2);
        mapWindow.setVisible(true);
        gameWindow.setVisible(true);
        runGameLoop();
        
    } // main method end
    
    public static synchronized void addCharacterEntity(Character entity) {
        entities.add(entity);
        CharacterThread thread = new CharacterThread(entity);
        thread.start();
        characterThreads.add(thread);
    }
    
    public static synchronized void removeCharacterEntity(Entity entity) {
        // to be implemented
    }
    
    public static synchronized void addProjectileEntity(Projectile entity) {
        entities.add(entity);
        projectilesThread.addProjectile(entity);
       // System.out.println("added projectile entity");
    }
    
    public static synchronized void removeProjectileEntity(Entity entity) {
        // to be implemented
    }
    
    public static synchronized ArrayList<Entity> copyEntities() {
        ArrayList<Entity> allEntities = new ArrayList<Entity>();
        for (Entity thing : entities) {
            allEntities.add(thing);
        }
        return allEntities;
    }
    
    
    
    // ------------------------------------------------------------------------------
    public static void runGameLoop() {
        while (true) {
            try {
                Thread.sleep(25);
            } catch (Exception e) {
            }
            player.movement(up, down, left, right, turnLeft, turnRight, currentLevel);

            if (shooting) {
                player.shoot(sprites.getSingleTexture(6));
            }
            
            rayCaster.updateInformation(player, cameraOffset, currentLevel, Math.PI / 2);
            gameWindow.repaint();
            mapWindow.repaint();
        }
    } // runGameLoop method end
    
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
            g2.fillRect(0, 0, Const.WIDTH, Const.HEIGHT / 2);
            g2.setColor(Color.BLACK);
            g2.fillRect(0, Const.HEIGHT / 2, Const.WIDTH, Const.HEIGHT);
            rayCaster.rayCast(g2, false);
            
            ArrayList<Entity> allEntities = copyEntities();
            
            rayCaster.drawSprite(g2, allEntities);
            //rayCaster.drawSprite(g2, projectilesThread.getProjectiles());
            
        } // paintComponent method end
    } // GraphicsPanel class end
    
    static class MapPanel extends JPanel {
        public MapPanel() {
            setFocusable(true);
            requestFocusInWindow();
            try {
                robot = new Robot();
            } catch (AWTException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        public void paintComponent(Graphics g) {
            super.paintComponent(g); // required
            
            Graphics2D g2 = (Graphics2D) g;
            
            g2.rotate(0);
            g2.setStroke(new BasicStroke(4));
            g2.setColor(Color.BLACK);
            for (int rows = 0; rows < currentLevel.getRows(); rows++) {
                for (int columns = 0; columns < currentLevel.getColumns(); columns++) {
                    if (currentLevel.getMapTile(rows, columns) == 0) {
                        g2.setColor(Color.WHITE);
                        g2.drawRect(columns * Const.BOXSIZE + (int) cameraOffset.getX(),
                                    rows * Const.BOXSIZE + (int) cameraOffset.getY(), Const.BOXSIZE, Const.BOXSIZE);
                    } else if (currentLevel.getMapTile(rows, columns) >= 1) {
                        g2.setColor(Color.BLACK);
                        g2.fillRect(columns * Const.BOXSIZE + +(int) cameraOffset.getX(),
                                    rows * Const.BOXSIZE + (int) cameraOffset.getY(), Const.BOXSIZE, Const.BOXSIZE);
                    }
                    g2.setColor(Color.BLACK);
                    g2.drawRect(columns * Const.BOXSIZE + +(int) cameraOffset.getX(),
                                rows * Const.BOXSIZE + (int) cameraOffset.getY(), Const.BOXSIZE, Const.BOXSIZE);
                }
            }
            
            rayCaster.rayCast(g2, true);
            
            for (Entity thing : entities) {
                
                if (thing instanceof Skeleton) {
                    g.setColor(Color.RED);
                    //g.setColor(Color.RED);
                    //((Skeleton) thing).drawEnemyProjectile(g2, cameraOffset.getX(), cameraOffset.getY());
                    
                } else if (thing instanceof Zombie) {
                    g.setColor(Color.GREEN);
                    //System.out.println("Zombie detected");
                } else if (thing instanceof Projectile){
                    g.setColor(Color.RED);
                 
                    //System.out.println("Projectile detected");
                }
                
                g2.fillRect((int) (thing.getPosition().getX() - thing.getWidth() / 2 + cameraOffset.getX()),
                            (int) (thing.getPosition().getY() - thing.getHeight() / 2 + cameraOffset.getY()),
                            thing.getWidth(), thing.getHeight());
            }
            
            g.setColor(Color.RED);
            // player.drawPlayerProjectile(g2, cameraOffset.getX(), cameraOffset.getY());
            
            g.setColor(Color.ORANGE);
            g2.rotate(-player.getAngle().getValue(), player.getPosition().getX() + cameraOffset.getX(),
                      player.getPosition().getY() + cameraOffset.getY());
            g2.fillRect((int) (player.getPosition().getX() - player.getWidth() / 2 + cameraOffset.getX()),
                        (int) (player.getPosition().getY() - player.getHeight() / 2 + cameraOffset.getY()),
                        player.getWidth(), player.getHeight());
            
        } // paintComponent method end
    } // GraphicsPanel class end
    
    // ------------------------------------------------------------------------------
    static class MyKeyListener implements KeyListener {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 'W') { // If the player hits/holds 'W', mo)ve up until they stop
                up = true;
            }
            
            if (e.getKeyCode() == 'S') { // If the player hits/holds 'S', move down until they stop
                down = true;
            }
            
            if (e.getKeyCode() == 'A') { // If the player hits/holds 'A', move left until they stop
                left = true;
            }
            
            if (e.getKeyCode() == 'D') { // If the player hits/holds 'D', move right until they stop
                right = true;
            }
            
            if (e.getKeyCode() == 'E') { // If the player hits/holds 'A', move left until they stop
                turnRight = true;
            }
            
            if (e.getKeyCode() == 'Q') { // If the player hits/holds 'D', move right until they stop
                turnLeft = true;
            }
            
            if (e.getKeyCode() == 'J') {
                cameraOffset.changeX(Const.BOXSIZE);
            } else if (e.getKeyCode() == 'L') {
                cameraOffset.changeX(-Const.BOXSIZE);
            } else if (e.getKeyCode() == 'I') {
                cameraOffset.changeY(Const.BOXSIZE);
            } else if (e.getKeyCode() == 'K') {
                cameraOffset.changeY(-Const.BOXSIZE);
            }
        }
        
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == 'W') { // If the player hits/holds 'W', move up until they stop
                up = false;
            }
            
            if (e.getKeyCode() == 'S') { // If the player hits/holds 'S', move down until they stop
                down = false;
            }
            
            if (e.getKeyCode() == 'A') { // If the player hits/holds 'A', move left until they stop
                left = false;
            }
            
            if (e.getKeyCode() == 'D') { // If the player hits/holds 'D', move right until they stop
                right = false;
            }
            
            if (e.getKeyCode() == 'E') { // If the player hits/holds 'A', move left until they stop
                turnRight = false;
            }
            
            if (e.getKeyCode() == 'Q') { // If the player hits/holds 'D', move right until they stop
                turnLeft = false;
            }
        }
        
        public void keyTyped(KeyEvent e) {
            
        }
    } // MyKeyListener class end
    
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
    } // MyMouseListener class end
    
    // ------------------------------------------------------------------------------
    static class MyMouseMotionListener implements MouseMotionListener {
        public void mouseMoved(MouseEvent e) {
            
        }
        
        public void mouseDragged(MouseEvent e) {
        }
    } // MyMouseMotionListener class end
    
} // BasicGameTemplate class end