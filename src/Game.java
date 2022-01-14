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
	static Sprite[] test = new Sprite[5];	
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

	static Zombie zombie = new Zombie(new Vector(400, 300), 30, 30, "skeleton", new Angle(2), null, 100, 4, null); 
	static Skeleton skeleton = new Skeleton(new Vector(200, 200), 30, 30, "skeleton", new Angle(2), null, 100, 4, null); 
	static Player player = new Player(new Vector ((3)*Const.BOXSIZE - Const.BOXSIZE/2, (2)*Const.BOXSIZE - Const.BOXSIZE/2), 10, 10, "player", new Angle(3*Math.PI/2), null, 100, 4, null);
	static boolean up, down, left, right, turnRight, turnLeft, shooting;
	static Vector cameraOffset = new Vector(0,0);

	// --------------------------------------------------------------------------
	// declare the properties of all game objects here
	// --------------------------------------------------------------------------

	// ------------------------------------------------------------------------------
	public static void main(String[] args) {

		try {
			textures = new TextureList(ImageIO.read(new File("images/textures.png")));
			sprites = new TextureList(ImageIO.read(new File("images/spriteSheet.png")));
		} catch (IOException e) {
			System.out.println("failed to get image");
			e.printStackTrace();
		}
		
		test[0] = new Sprite(new Vector ((3)*Const.BOXSIZE - Const.BOXSIZE/2, (3)*Const.BOXSIZE - Const.BOXSIZE/2), sprites.getSingleTexture(5),20,0.75);
		test[1] = new Sprite(new Vector ((2)*Const.BOXSIZE - Const.BOXSIZE/2, (2)*Const.BOXSIZE - Const.BOXSIZE/2), sprites.getSingleTexture(4),0,1);
		test[2] = new Sprite(new Vector ((3)*Const.BOXSIZE - Const.BOXSIZE/2, (2)*Const.BOXSIZE - Const.BOXSIZE/2), sprites.getSingleTexture(4),0,1);
		test[3] = new Sprite(new Vector ((2)*Const.BOXSIZE - Const.BOXSIZE/2, (4)*Const.BOXSIZE - Const.BOXSIZE/2), sprites.getSingleTexture(4),0,1);
		test[4] = new Sprite(new Vector ((4)*Const.BOXSIZE - Const.BOXSIZE/2, (3)*Const.BOXSIZE - Const.BOXSIZE/2), sprites.getSingleTexture(1),20,0.75);
		
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

		mapWindow.setVisible(true);
		gameWindow.setVisible(true);
		runGameLoop();

	} // main method end

	// ------------------------------------------------------------------------------
	public static void runGameLoop() {
		while (true) {
			gameWindow.repaint();
			mapWindow.repaint();
			try {
				Thread.sleep(25);
			} catch (Exception e) {
			}

			player.movement(up, down, left, right, turnLeft, turnRight, currentLevel);
            player.moveProjectile();
            
            //skeleton.shoot(player);
            skeleton.moveProjectile();
            zombie.attack(player);
            if (shooting){
                player.shoot();
                skeleton.shoot(player);
            }
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
			g2.fillRect(0,Const.HEIGHT/2,Const.WIDTH,Const.HEIGHT);
			rayCaster.rayCast(g2, false, player.getPosition(), player.getAngle(), cameraOffset, currentLevel);
			rayCaster.drawSprite(g2, test);

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

			g2.rotate(0);
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

			rayCaster.rayCast(g2, true, player.getPosition(), player.getAngle(),
					cameraOffset, currentLevel);
			
			g.setColor(Color.GREEN);
            g2.fillRect((int) (zombie.getPosition().getX() - zombie.getWidth() / 2 + cameraOffset.getX()),
                        (int) (zombie.getPosition().getY() - zombie.getHeight() / 2 + cameraOffset.getY()), zombie.getWidth(),
                        zombie.getHeight());
            
            g.setColor(Color.RED);
            player.drawPlayerProjectile(g2, cameraOffset.getX(), cameraOffset.getY());
            
            g2.fillRect((int) (skeleton.getPosition().getX() - skeleton.getWidth() / 2 + cameraOffset.getX()),
                        (int) (skeleton.getPosition().getY() - skeleton.getHeight() / 2 + cameraOffset.getY()), skeleton.getWidth(),
                        skeleton.getHeight());
            
            skeleton.drawEnemyProjectile(g2, cameraOffset.getX(), cameraOffset.getY());
			
			g.setColor(Color.ORANGE);	
			g2.rotate(-player.getAngle().getAngleValue(), player.getPosition().getX()+ cameraOffset.getX(), player.getPosition().getY()+ cameraOffset.getY());
			g2.fillRect((int) (player.getPosition().getX() - player.getWidth() / 2 + cameraOffset.getX()), (int) (player.getPosition().getY() - player.getHeight() / 2 + cameraOffset.getY()), player.getWidth(), player.getHeight());
			
		
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
		}

		public void mouseDragged(MouseEvent e) {
		}
	} // MyMouseMotionListener class end

} // BasicGameTemplate class end
