 /* RayCasting
 * Basic raycasting demo
 * @author Jonathan Cai
 * @version Dec 2021
 */
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class Game {
	static JFrame gameWindow;
	static JFrame mapWindow;
	static GraphicsPanel canvas;
	static MapPanel mapThing;
	static RayCaster rayCaster; 
	static MyKeyListener keyListener = new MyKeyListener();
	static MyMouseListener mouseListener = new MyMouseListener();
	static MyMouseMotionListener mouseMotionListener = new MyMouseMotionListener();
	static int map[][] = {
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,0,0,0,1,2,2,1,0,0,0,0,0,0,0,1},
			{1,0,0,0,4,0,0,4,0,0,2,2,0,0,0,1},
			{1,0,0,0,1,2,0,1,0,0,0,0,0,0,2,1},
			{1,1,4,1,6,1,1,1,0,0,3,4,3,2,2,1},
			{1,0,0,0,0,0,0,1,3,3,3,0,3,3,3,1},
			{1,0,0,5,0,0,0,4,0,0,0,0,0,0,0,1},
			{1,2,0,0,0,0,0,6,3,0,3,0,3,0,0,1},
			{1,1,7,7,7,4,1,1,1,0,1,4,1,0,0,1},
			{1,0,0,0,0,0,0,1,1,1,1,0,1,1,4,1},
			{1,0,2,0,2,0,0,1,0,0,0,0,0,2,0,1},
			{1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1},
			{1,0,2,0,2,0,0,4,0,0,0,2,0,0,0,1},
			{1,0,0,0,0,0,0,1,0,0,0,2,0,0,0,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
	};
		
	static Player player = new Player((3)*Const.BOXSIZE - Const.BOXSIZE/2, (2)*Const.BOXSIZE - Const.BOXSIZE/2, 10, 10, "player", new Angle(3*Math.PI/2), null, 100, 4, null);
	static boolean up, down, left, right, turnRight, turnLeft;
	static int xOffset = 0;
	static int yOffset = 0;
	
	static int texture[][][] = {{{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,},
								 {1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1},
								 {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
								 {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
								 {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
								 {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
								 {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
								 {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
								 {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
								 {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
								 {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
								 {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
								 {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
								 {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
								 {1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1},
								 {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}},
			
							    {{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
								 {1,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1},
								 {1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1},
								 {1,0,1,1,1,0,0,0,0,0,0,1,1,1,0,1},
								 {1,0,0,1,1,1,0,0,0,0,1,1,1,0,0,1},
								 {1,0,0,0,1,1,1,0,0,1,1,1,0,0,0,1},
								 {1,0,0,0,0,1,1,1,1,1,1,0,0,0,0,1},
								 {1,0,0,0,0,0,1,1,1,1,0,0,0,0,0,1},
								 {1,0,0,0,0,0,1,1,1,1,0,0,0,0,0,1},
								 {1,0,0,0,0,1,1,1,1,1,1,0,0,0,0,1},
								 {1,0,0,0,1,1,1,0,0,1,1,1,0,0,0,1},
								 {1,0,0,1,1,1,0,0,0,0,1,1,1,0,0,1},
								 {1,0,1,1,1,0,0,0,0,0,0,1,1,1,0,1},
								 {1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1},
								 {1,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1},
								 {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}},
								
							    {{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,},
								 {1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1},
								 {1,0,1,1,1,1,1,0,0,0,0,0,0,0,0,1},
								 {1,0,1,1,0,1,1,0,1,1,1,1,1,1,0,1},
								 {1,0,1,1,1,0,1,0,1,0,1,0,1,1,0,1},
								 {1,0,1,0,1,1,1,0,1,1,1,1,1,1,0,1},
								 {1,0,1,1,1,1,1,0,0,0,0,0,0,0,0,1},
								 {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
								 {1,0,1,0,1,0,1,0,0,0,1,1,0,0,0,1},
								 {1,0,1,0,1,0,1,0,0,1,0,0,1,0,0,1},
								 {1,0,1,0,1,0,1,0,1,0,1,1,0,1,0,1},
								 {1,0,1,0,1,0,1,0,1,0,1,1,0,1,0,1},
								 {1,0,1,0,1,0,1,0,0,1,0,0,1,0,0,1},
								 {1,0,1,0,1,0,1,0,0,0,1,1,0,0,0,1},
								 {1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1},
								 {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}},	
								 
						    	{{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
								 {1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1},
								 {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
								 {1,0,0,1,1,1,1,1,1,1,1,1,1,0,0,1},
								 {1,0,0,1,0,0,0,0,0,0,0,0,1,0,0,1},
								 {1,0,0,1,1,1,1,1,1,1,1,1,1,0,0,1},
								 {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
								 {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
								 {1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,1},
								 {1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,1},
								 {1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,1},
								 {1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,1},
								 {1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,1},
								 {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
								 {1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1},
								 {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}},	 
								 
						    	{{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
								 {1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1},
								 {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
								 {1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1},
								 {1,0,0,0,0,0,0,0,1,1,0,0,0,0,0,1},
								 {1,0,0,0,0,0,0,0,1,1,1,0,0,0,0,1},
								 {1,0,0,1,1,1,1,1,1,1,1,1,0,0,0,1},
								 {1,0,0,1,1,1,1,1,1,1,1,1,1,0,0,1},
								 {1,0,0,1,1,1,1,1,1,1,1,1,1,0,0,1},
								 {1,0,0,1,1,1,1,1,1,1,1,1,0,0,0,1},
								 {1,0,0,0,0,0,0,0,1,1,1,0,0,0,0,1},
								 {1,0,0,0,0,0,0,0,1,1,0,0,0,0,0,1},
								 {1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1},
								 {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
								 {1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1},
								 {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}},
								 
						        {{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,},
								 {1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1},
								 {1,0,0,0,1,1,0,0,0,0,1,1,0,0,0,1},
								 {1,0,0,1,1,1,0,0,0,0,1,1,1,0,0,1},
								 {1,0,1,1,1,1,1,0,0,1,1,1,1,1,0,1},
								 {1,0,1,1,1,1,1,0,0,1,1,1,1,1,0,1},
								 {1,0,0,0,1,1,0,0,0,0,1,1,0,0,0,1},
								 {1,0,0,0,0,0,0,1,1,0,0,0,0,0,0,1},
								 {1,0,0,0,0,0,0,1,1,0,0,0,0,0,0,1},
								 {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
								 {1,0,0,0,0,0,0,1,1,0,0,0,0,0,0,1},
								 {1,0,0,0,0,0,1,1,1,1,0,0,0,0,0,1},
								 {1,0,0,0,0,1,1,1,1,1,1,0,0,0,0,1},
								 {1,0,0,0,0,1,1,1,1,1,1,0,0,0,0,1},
								 {1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1},
								 {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}},
						        
						        {{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
								 {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
								 {1,1,0,0,0,0,0,1,1,0,0,0,0,0,1,1},
								 {1,1,0,0,0,0,0,1,1,0,0,0,0,0,1,1},
								 {1,1,0,0,0,0,0,1,1,0,0,0,0,0,1,1},
								 {1,1,0,0,0,0,0,1,1,0,0,0,0,0,1,1},
								 {1,1,0,0,0,0,0,1,1,0,0,0,0,0,1,1},
								 {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
								 {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
								 {1,1,0,0,0,0,0,1,1,0,0,0,0,0,1,1},
								 {1,1,0,0,0,0,0,1,1,0,0,0,0,0,1,1},
								 {1,1,0,0,0,0,0,1,1,0,0,0,0,0,1,1},
								 {1,1,0,0,0,0,0,1,1,0,0,0,0,0,1,1},
								 {1,1,0,0,0,0,0,1,1,0,0,0,0,0,1,1},
								 {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
								 {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}} 
							};

	// --------------------------------------------------------------------------
	// declare the properties of all game objects here
	// --------------------------------------------------------------------------

//------------------------------------------------------------------------------    
	public static void main(String[] args) {
		gameWindow = new JFrame("Game Window");
		gameWindow.setSize(Const.WIDTH-62, Const.HEIGHT+40);
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
		
		rayCaster = new RayCaster(texture);
		
		mapWindow.setVisible(true);
		gameWindow.setVisible(true);
		runGameLoop();

	} // main method end

//------------------------------------------------------------------------------   
	public static void runGameLoop() {
		while (true) {
			gameWindow.repaint();
			mapWindow.repaint();
			try {
				Thread.sleep(25);
			} catch (Exception e) {
			}

			player.movement(up,down,left,right,turnLeft,turnRight,map);
		}
	} // runGameLoop method end

//------------------------------------------------------------------------------  
	static class GraphicsPanel extends JPanel {
		public GraphicsPanel() {
			setFocusable(true);
			requestFocusInWindow();
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g); // required
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.GRAY);
			g2.fillRect(0,0,Const.WIDTH,Const.HEIGHT/2);
			g2.setColor(Color.BLACK);
			g2.fillRect(0,Const.HEIGHT/2,Const.WIDTH,Const.HEIGHT);
			rayCaster.rayCast(g2, false, player.getX(), player.getY(), player.getAngle(), xOffset, yOffset, map);

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
			for (int rows = 0; rows < map.length; rows++) {
				for (int columns = 0; columns < map[0].length; columns++) {
					if (map[rows][columns] == 0) {
						g2.setColor(Color.WHITE);
						g2.drawRect(columns * Const.BOXSIZE + xOffset, rows * Const.BOXSIZE + yOffset, Const.BOXSIZE, Const.BOXSIZE);
					} else if (map[rows][columns] >= 1) {
						g2.setColor(Color.BLACK);
						g2.fillRect(columns * Const.BOXSIZE + xOffset, rows * Const.BOXSIZE + yOffset, Const.BOXSIZE, Const.BOXSIZE);
					}
					g2.setColor(Color.BLACK);
					g2.drawRect(columns * Const.BOXSIZE + xOffset, rows * Const.BOXSIZE + yOffset, Const.BOXSIZE, Const.BOXSIZE);
				}
			}
			
			rayCaster.rayCast(g2, true, player.getX(), player.getY(), player.getAngle(), xOffset, yOffset, map);

			g.setColor(Color.ORANGE);
			g2.rotate(-player.getAngle().getAngleValue(), player.getX()+ xOffset, player.getY()+ yOffset);
			g2.fillRect((int) player.getX() - player.getWidth() / 2 + xOffset, (int) player.getY() - player.getHeight() / 2 + yOffset, player.getWidth(), player.getHeight());
			
		
		} // paintComponent method end
	} // GraphicsPanel class end

//------------------------------------------------------------------------------     
	static class MyKeyListener implements KeyListener {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == 'W') { // If the player hits/holds 'W', move up until they stop
				up = true;
			}

			if (e.getKeyCode() == 'S') { // If the player hits/holds 'S', move down until they stop
				down = true;
			}

			if (e.getKeyCode() == 'A' ) { // If the player hits/holds 'A', move left until they stop
				left = true;
			}

			if (e.getKeyCode() == 'D' ) { // If the player hits/holds 'D', move right until they stop
				right = true;
			}

			if (e.getKeyCode() == 'E' || e.getKeyCode() == KeyEvent.VK_RIGHT) { // If the player hits/holds 'A', move left until they stop
				turnRight = true;
			}

			if (e.getKeyCode() == 'Q' || e.getKeyCode() == KeyEvent.VK_LEFT) { // If the player hits/holds 'D', move right until they stop
				turnLeft = true;
			}
			
			if (e.getKeyCode() == 'J'){
				xOffset += 50;
            } else if (e.getKeyCode() =='L'){
            	xOffset -= 50;
            } else if (e.getKeyCode() == 'I'){
            	yOffset += 50;
            } else if (e.getKeyCode() == 'K'){
            	yOffset -= 50;
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

			if (e.getKeyCode() == 'E'|| e.getKeyCode() == KeyEvent.VK_RIGHT) { // If the player hits/holds 'A', move left until they stop
				turnRight = false;
			}

			if (e.getKeyCode() == 'Q' || e.getKeyCode() == KeyEvent.VK_LEFT) { // If the player hits/holds 'D', move right until they stop
				turnLeft = false;
			}
		}

		public void keyTyped(KeyEvent e) {

		}
	} // MyKeyListener class end

//------------------------------------------------------------------------------ 
	static class MyMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {

		}

		public void mousePressed(MouseEvent e) {

		}

		public void mouseReleased(MouseEvent e) {

		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}
	} // MyMouseListener class end

//------------------------------------------------------------------------------     
	static class MyMouseMotionListener implements MouseMotionListener {
		public void mouseMoved(MouseEvent e) {
		}

		public void mouseDragged(MouseEvent e) {
		}
	} // MyMouseMotionListener class end

} // BasicGameTemplate class end
