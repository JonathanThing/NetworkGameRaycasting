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
	
	// ------------------------------------------------------------------------------
	public static void main(String[] args) {

		try {
			textures = new TextureManager(ImageIO.read(new File("images/WallTextures.png")));
			sprites = new TextureManager(ImageIO.read(new File("images/spriteSheet.png")));
			personDirection = new TextureManager(ImageIO.read(new File("images/PersonDirectionAnimation.png")));
			fireball = new TextureManager(ImageIO.read(new File("images/FireBallAnimation.png")));
		} catch (IOException e) {
			System.out.println("failed to get image");
			e.printStackTrace();
		}
		
//		entities.add(new Zombie(new Vector(400, 300), 30, 30, "skeleton", new Angle(2), sprites.getSingleTexture(0), 100, 4, 20,0.75, null));
//		entities.add(new Skeleton(new Vector((3)*Const.BOXSIZE - Const.BOXSIZE/2, (4)*Const.BOXSIZE - Const.BOXSIZE/2), 30, 30, "skeleton", new Angle(Math.PI/2), personDirection, 100, 4, 0,0.75, null));
		entities.add(new Skeleton(new Vector((3)*Const.BOXSIZE - Const.BOXSIZE/2, (6)*Const.BOXSIZE - Const.BOXSIZE/2), 30, 30, "skeleton", new Angle(3*Math.PI/2), personDirection, 100, 4, 0,0.75, null, fireball));
//		entities.add(new Skeleton(new Vector((6)*Const.BOXSIZE - Const.BOXSIZE/2, (2)*Const.BOXSIZE - Const.BOXSIZE/2), 30, 30, "skeleton", new Angle(Math.PI), personDirection, 100, 4, 0,0.75, null));
//		entities.add(new Skeleton(new Vector((6)*Const.BOXSIZE - Const.BOXSIZE/2, (5)*Const.BOXSIZE - Const.BOXSIZE/2), 30, 30, "skeleton", new Angle(Math.PI/2), personDirection, 100, 4, 0,0.75, null));
//		entities.add(new Skeleton(new Vector((8)*Const.BOXSIZE - Const.BOXSIZE/2, (4)*Const.BOXSIZE - Const.BOXSIZE/2), 30, 30, "skeleton", new Angle(2*Math.PI), personDirection, 100, 4, 0,0.75, null));

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
            player.moveProjectile();
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
