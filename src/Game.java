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
import java.util.concurrent.TimeUnit;

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
	static TextureManager personDirection;
	static TextureManager fireball;
	public static Robot robo;
	static Level currentLevel = new Level(new int[][]{
												{2,2,2,1,2,1,2,2,2,2,2,2,2,2,2,2},
												{2,0,0,0,0,0,0,0,2,0,0,0,0,0,0,2},
												{1,0,0,0,0,0,0,0,4,0,0,0,6,6,0,2},
												{2,0,0,0,2,2,0,0,2,0,0,0,0,6,0,2},
												{1,0,0,0,2,0,0,0,2,0,0,0,6,6,0,2},
												{2,0,0,0,1,0,0,0,4,0,0,0,0,0,0,2},
												{2,0,0,0,2,0,0,0,2,0,0,0,0,0,0,2},
												{2,2,4,2,1,2,4,2,2,0,0,0,3,3,0,2},
												{2,0,0,0,0,0,0,0,2,0,0,0,3,3,0,2},
												{2,0,0,0,0,0,0,0,4,0,0,0,0,0,0,2},
												{2,0,0,5,5,0,0,0,2,0,0,0,0,0,0,2},
												{2,0,0,5,5,0,0,0,2,0,0,0,5,5,0,2},
												{2,0,0,0,0,0,0,0,4,0,0,0,5,5,0,2},
												{2,0,0,0,0,0,0,0,2,0,0,0,0,0,0,2},
												{2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2}}); 

	static Player player = new Player(new Vector ((3)*Const.BOXSIZE - Const.BOXSIZE/2, (2)*Const.BOXSIZE - Const.BOXSIZE/2), 10, 10, "player", new Angle(3*Math.PI/2), null, 100, 4, 20,0.75, null);
	static boolean up, down, left, right, turnRight, turnLeft, shooting;
	static Vector cameraOffset = new Vector(0,0);	
	static ArrayList<Entity> entities = new ArrayList<Entity>();

	// --------------------------------------------------------------------------
	// declare the properties of all game objects here
	// --------------------------------------------------------------------------

	// ------------------------------------------------------------------------------
	public static void main(String[] args) {
			try {
	            robo = new Robot();
	        } catch (AWTException e) {
	            e.printStackTrace();
	        }
            try {
    			textures = new TextureManager(ImageIO.read(new File("C://WallTextures.png")));
    			sprites = new TextureManager(ImageIO.read(new File("C://spriteSheet.png")));
    			personDirection = new TextureManager(ImageIO.read(new File("C://PersonDirectionAnimation.png")));
    			fireball = new TextureManager(ImageIO.read(new File("C://FireBallAnimation.png")));
    		} catch (IOException e1) {
    			System.out.println("failed to get image");
    			e1.printStackTrace();
    		}
    		
//    		entities.add(new Zombie(new Vector(400, 300), 30, 30, "skeleton", new Angle(2), sprites.getSingleTexture(0), 100, 4, 20,0.75, null));
//    		entities.add(new Skeleton(new Vector((3)*Const.BOXSIZE - Const.BOXSIZE/2, (4)*Const.BOXSIZE - Const.BOXSIZE/2), 30, 30, "skeleton", new Angle(Math.PI/2), personDirection, 100, 4, 0,0.75, null));
    		entities.add(new Skeleton(new Vector((3)*Const.BOXSIZE - Const.BOXSIZE/2, (6)*Const.BOXSIZE - Const.BOXSIZE/2), 30, 30, "skeleton", new Angle(3*Math.PI/2), personDirection, 100, 4, 0,0.75, null, fireball));
//    		entities.add(new Skeleton(new Vector((6)*Const.BOXSIZE - Const.BOXSIZE/2, (2)*Const.BOXSIZE - Const.BOXSIZE/2), 30, 30, "skeleton", new Angle(Math.PI), personDirection, 100, 4, 0,0.75, null));
//    		entities.add(new Skeleton(new Vector((6)*Const.BOXSIZE - Const.BOXSIZE/2, (5)*Const.BOXSIZE - Const.BOXSIZE/2), 30, 30, "skeleton", new Angle(Math.PI/2), personDirection, 100, 4, 0,0.75, null));
//    		entities.add(new Skeleton(new Vector((8)*Const.BOXSIZE - Const.BOXSIZE/2, (4)*Const.BOXSIZE - Const.BOXSIZE/2), 30, 30, "skeleton", new Angle(2*Math.PI), personDirection, 100, 4, 0,0.75, null));

    		entities.add(player);
    		gameWindow = new JFrame("Game Window");
    		gameWindow.setSize(Const.SCREEN_WIDTH, Const.SCREEN_HEIGHT);
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
            rayCaster.updateInformation(player, cameraOffset, currentLevel, Math.PI/2);
    		mapWindow.setVisible(true);
    		gameWindow.setVisible(true);
    		runGameLoop();

    	} // main method end

	// ------------------------------------------------------------------------------
	public static void runGameLoop() {
		while (true) {
			try {
				Thread.sleep(25);
			} catch (Exception e) {		
			}
			player.movement(up, down, left, right, turnLeft, turnRight, currentLevel);
            player.moveProjectiles();
            if (shooting){
                player.shoot(fireball);
            }
            
            for (Entity thing : entities) {
            	if (thing instanceof Skeleton) {
	            	((Skeleton) thing).moveProjectile();
		            if (shooting){
		            	((Skeleton) thing).attack(player);
		            }	            
		            
		        	long currentTime = System.currentTimeMillis();       	
		        	if (thing.getSprites().getLastAnimationChange() + 500 <= currentTime) {
		        		thing.getSprites().setLastAnimationChange(currentTime);
		        		thing.getSprites().changeAnimationNumber(1);
		        	}
            	} else if (thing instanceof Zombie) {
                	((Zombie) thing).attack(player);
            	}
            }    
            
            rayCaster.updateInformation(player, cameraOffset, currentLevel, Const.FOV);
			gameWindow.repaint();
			mapWindow.repaint();
		}
	} // runGameLoop method end
	// ------------------------------------------------------------------------------
	
	private static void MouseController(MouseEvent e) {
		gameWindow.setCursor(Cursor.CROSSHAIR_CURSOR);
		final int midX = Const.WIDTH/2;
		final int midY = Const.HEIGHT/2;
		int adjfactor = -7;		
        
        robo.mouseMove(midX + gameWindow.getX(), midY + gameWindow.getY());
        int moveX = e.getX() - midX;
        
        if(moveX > adjfactor+3) {
        	turnRight = true;
        	turnLeft = false;
        }
        else if (moveX < adjfactor-3){
        	turnLeft = true;
        	turnRight = false;    	
        }
        else{
        	try {
				Thread.sleep(20);
			} catch (Exception e1){} 
        	turnRight = false;
        	turnLeft = false;
        }
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
			g2.fillRect(0,Const.HEIGHT/2,Const.WIDTH+1,Const.HEIGHT);
//			rayCaster.rayCastFloors(g2);
			rayCaster.rayCastWalls(g2, false);
			
			ArrayList<Entity> allEntities = new ArrayList<Entity>();
			
			for (Entity thing : entities) {
				allEntities.add(thing);
				if (thing instanceof Character) {
					for (Entity projectile : ((Character) thing).getProjectilesList()) {
						allEntities.add(projectile);
					}					
				}
			}

			rayCaster.drawSprite(g2, allEntities);

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
						g2.drawRect(columns * Const.BOXSIZE + (int)cameraOffset.getX(), rows * Const.BOXSIZE +  (int)cameraOffset.getY(), Const.BOXSIZE,
								Const.BOXSIZE);
					} else if (currentLevel.getMapTile(rows, columns) >= 1) {
						g2.setColor(Color.BLACK);
						g2.fillRect(columns * Const.BOXSIZE + +  (int)cameraOffset.getX(), rows * Const.BOXSIZE +  (int)cameraOffset.getY(), Const.BOXSIZE,
								Const.BOXSIZE);
					}
					g2.setColor(Color.BLACK);
					g2.drawRect(columns * Const.BOXSIZE + +  (int)cameraOffset.getX(), rows * Const.BOXSIZE +  (int)cameraOffset.getY(), Const.BOXSIZE,
							Const.BOXSIZE);
				}
			}

			rayCaster.rayCastWalls(g2, true);
			
			for (Entity entity : entities) {

				if (entity instanceof Skeleton) {
					g.setColor(Color.RED);
		            g.setColor(Color.RED);
		            ((Skeleton) entity).drawEnemyProjectile(g2, cameraOffset.getX(), cameraOffset.getY());

            	} else if (entity instanceof Zombie) {
            		g.setColor(Color.GREEN);
            	}
								
	            g2.fillRect((int) (entity.getPosition().getX() - entity.getWidth() / 2 + cameraOffset.getX()),
	                        (int) (entity.getPosition().getY() - entity.getHeight() / 2 + cameraOffset.getY()), entity.getWidth(),
	                        entity.getHeight());
	            
	            g2.drawLine((int) (entity.getPosition().getX() + cameraOffset.getX()), (int) (entity.getPosition().getY() + cameraOffset.getY()), (int) (entity.getPosition().getX() + cameraOffset.getX() + Math.cos(entity.getAngle().getValue())*200), (int) (entity.getPosition().getY() + cameraOffset.getY() - Math.sin(entity.getAngle().getValue())*200));
			}
			
			g.setColor(Color.RED);
            player.drawPlayerProjectile(g2, cameraOffset.getX(), cameraOffset.getY());

		
		} // paintComponent method end
	} // GraphicsPanel class end

	// ------------------------------------------------------------------------------
	static class MyKeyListener implements KeyListener {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == 'W') { // If the player hits/holds 'W', move up until they stop
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
			
			if (e.getKeyCode() == '1') { // If the player hits/holds 'D', move right until they stop
				player.setSlot1(true);
				player.setSlot2(false);
				player.setSlot3(false);
			}
			if (e.getKeyCode() == '2') { // If the player hits/holds 'D', move right until they stop
				player.setSlot1(false);
				player.setSlot2(true);
				player.setSlot3(false);
			}
			if (e.getKeyCode() == '3') { // If the player hits/holds 'D', move right until they stop
				player.setSlot1(false);
				player.setSlot2(false);
				player.setSlot3(true);
			}
			
			if (e.getKeyCode() == 'J'){
				cameraOffset.changeX(Const.BOXSIZE);
            } else if (e.getKeyCode() =='L'){
            	cameraOffset.changeX(-Const.BOXSIZE);
            } else if (e.getKeyCode() == 'I'){
            	cameraOffset.changeY(Const.BOXSIZE);
            } else if (e.getKeyCode() == 'K'){
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
			MouseController(e);
		}

		public void mouseDragged(MouseEvent e) {
			MouseController(e);
		}
	} // MyMouseMotionListener class end

} // BasicGameTem
