
/* BasicGameTemplate
 * Desc: Template for a basic arcade game
 * @author ICS3U
 * @version Dec 2017
 */
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class RayCasting {
	static JFrame gameWindow;
	static JFrame mapWindow;
	static GraphicsPanel canvas;
	static MapPanel mapThing;
	static final int WIDTH = 800;
	static final int HEIGHT = 600;
	static final int widthScreen = WIDTH;
	static final int heightScreen = HEIGHT;
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
	
	static int floor[][] = {
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
	};
			
	static int boxSize = 64;
	static double pX = (3)*boxSize - boxSize/2;
	static double pY = (2)*boxSize - boxSize/2;
	static int pSize = 10;
	static Angle pAngle = new Angle(3*Math.PI/2);
	static boolean up, down, left, right, turnRight, turnLeft;
	static int xOffset = 0;
	static int yOffset = 0;
	static int wallType1;
	static int wallType2;
	
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
		gameWindow.setSize(WIDTH-62, HEIGHT+40);
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas = new GraphicsPanel();
		canvas.addMouseListener(mouseListener);
		canvas.addMouseMotionListener(mouseMotionListener);
		canvas.addKeyListener(keyListener);
		gameWindow.add(canvas);
		
		mapWindow = new JFrame("Map");
		mapWindow.setSize(WIDTH, HEIGHT);
		mapWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mapThing = new MapPanel();
		mapWindow.add(mapThing);
		
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

			double xRaw = 0;
			double yRaw = 0;
			double xForward = 0;
			double yForward = 0;
			double xSide = 0;
			double ySide = 0;
			double forward = 0;
			double side = 0;

			if (up) {
				yRaw += 1;
			}

			if (down) {
				yRaw -= 1;
			}

			if (right) {
				xRaw += 1;
			}

			if (left) {
				xRaw -= 1;
			}

			if (turnLeft) {
				pAngle.changeAngle(Math.toRadians(5));
			}

			if (turnRight) {
				pAngle.changeAngle(Math.toRadians(-5));
			}

			xForward = Math.cos(pAngle.getAngle()) * yRaw;
			yForward = Math.sin(pAngle.getAngle()) * yRaw;

			ySide = Math.sin(Angle.checkLimit(pAngle.getAngle() - Math.PI / 2)) * xRaw;
			xSide = Math.cos(Angle.checkLimit(pAngle.getAngle() - Math.PI / 2)) * xRaw;

			forward = yForward + ySide;
			side = xForward + xSide;

			double hyp = Math.sqrt(Math.pow(forward, 2) + Math.pow(side, 2));

			int speed = 4;		
			
			if (hyp != 0) {
				
				if (map[(int) ((pY - ((forward/hyp) * speed*4))/boxSize)][(int) ((pX + ((side/hyp) * speed*4))/boxSize)] == 4) {
					map[(int) ((pY - ((forward/hyp) * speed*4))/boxSize)][(int) ((pX + ((side/hyp) * speed*4))/boxSize)] = 0;
				}
				
				if (map[(int) (pY/boxSize)][(int) ((pX + ((side/hyp) * speed*2))/boxSize)] < 1) {
					pX += ((side / hyp) * speed);
					
				}
					
				if (map[(int) ((pY - ((forward/hyp) * speed*2))/boxSize)][(int) (pX/boxSize)] < 1) {
					pY -= ((forward / hyp) * speed);
				}
			}
		}
	} // runGameLoop method end

	public static void rayCast(Graphics g, boolean bruh) {
		
		Graphics2D g2 = (Graphics2D) g;
		int numb = 360;
		double dist1;
		double dist2;
		double fov = Math.toRadians(90); 
		double rAngle;
		double plane = Math.tan(fov/2)*2;
		double inc = plane/numb;
		
		for (int rays = 0; rays < numb; rays++) {
			
			rAngle = Angle.checkLimit(pAngle.getAngle() + Math.atan(inc*(rays-numb/2)));

			dist1 = rayCastVerticalSides(rAngle);
			dist2 = rayCastHorizontalSides(rAngle);
					
			boolean horizontal = false;
			
			if (dist1 > dist2 && dist2 != Const.NONE) { //hit veritcal side
				dist1 = dist2;
				wallType1 = wallType2;
			} else if (dist1 < dist2 && dist1 != Const.NONE) { //hit horizontal side
				horizontal = true;
				
			}
			
			double rx = pX + dist1*Math.cos(rAngle);
			double ry = pY - dist1*Math.sin(rAngle);
			
			if (bruh) {
			
				g2.setColor(new Color(255, 165, 0));
				g2.setStroke(new BasicStroke(1));
				g2.drawLine((int)pX + xOffset, (int)pY + yOffset,(int)(pX + dist1*Math.cos(rAngle)) + xOffset, (int)(pY - dist1*Math.sin(rAngle) + yOffset));
				
			} else {
							
				dist1 = dist1*Math.cos(rAngle-pAngle.getAngle());
				
				int strokeWidth = WIDTH/numb;
				g2.setStroke(new BasicStroke(strokeWidth));
				
				int numbPixel = texture[0].length;
				double lineH = (HEIGHT*boxSize/2)/dist1;
				double middle = lineH/2;
				double stepPattern = (lineH/numbPixel);
				int textureX;
				int textureY = 0;
				
				if (!horizontal) {
					if (rAngle < Math.PI) {
						textureX = (int)(rx / (boxSize/texture[0][0].length) % texture[0][0].length);
					} else {
						textureX = numbPixel- (int)(rx / (boxSize/texture[0][0].length) % texture[0][0].length)-1;
					}
				} else {
					if (rAngle < Math.PI/2 || rAngle > 3*Math.PI/2) {
						textureX = (int)(ry / (boxSize/texture[0][0].length) % texture[0][0].length);
					} else {
						textureX = numbPixel- (int)(ry / (boxSize/texture[0][0].length) % texture[0][0].length)-1;
					}
				}

				if (lineH > HEIGHT) {
					lineH = HEIGHT;
				}
					
				wallType1--;
				
				for (int i = 0; i < numbPixel; i++) {
					textureY = (numbPixel-1-i);
					
					if (dist1 == Const.NONE || wallType1 < 0) {
						g.setColor(Color.WHITE);
					} else if (texture[wallType1][(int)textureY][(int)textureX] == 1 && horizontal) {
						g.setColor(new Color(170, 110, 0));
					} else if (texture[wallType1][(int)textureY][(int)textureX] == 0 && horizontal) {
						g.setColor(new Color(255, 165, 0));
					} else if (texture[wallType1][(int)textureY][(int)textureX] == 1 && !horizontal) {
						g.setColor(new Color(64, 41, 0));
					} else if (texture[wallType1][(int)textureY][(int)textureX] == 0 && !horizontal) {
						g.setColor(new Color(127, 82, 0));
					}
	
					g.drawLine(WIDTH-rays*strokeWidth- 80, (int) (HEIGHT/2+(middle) - i*stepPattern) , WIDTH-rays*strokeWidth - 80, (int) (HEIGHT/2 + (middle) - (i+1)*stepPattern ));
					
				}
				
				
				
			}
		}
	}
	
	public static double rayCastVerticalSides(double rAngle) {
		
		double distance = Const.NONE;
		double xStep;
		double yStep;
		double xInt;
		double yInt;
		int width = map[0].length*boxSize;
		int height = map.length*boxSize;
		
		if (rAngle == Math.PI/2 || rAngle == 3*Math.PI/2) {
			return distance;
		} else if (rAngle < Math.PI/2 || rAngle > 3*Math.PI/2) { //right
			xInt = boxSize * (Math.ceil(pX / boxSize));
			yInt = pY - (xInt-pX) * Math.tan(rAngle);
			xStep = boxSize;
			yStep = Math.tan(rAngle) * xStep;			
		} else { //left
			xInt = boxSize * (Math.floor(pX / boxSize));
			yInt = pY - (xInt-pX) * Math.tan(rAngle);
			xStep = -boxSize;
			yStep = Math.tan(rAngle) * xStep;
		}
		
		while (xInt < width && xInt >= 0 && yInt < height && yInt >= 0) {
			if (map[(int) yInt / boxSize][(int) xInt / boxSize-1] >= 1) {
				wallType1 = map[(int) yInt / boxSize][(int) xInt / boxSize-1];
				distance = distance(pX, pY, xInt, yInt);
				break;
			} else if (map[(int) yInt / boxSize][(int) xInt / boxSize] >= 1) {
				wallType1 = map[(int) yInt / boxSize][(int) xInt / boxSize];
				distance = distance(pX, pY, xInt, yInt);
				break;
			} else {
				xInt += xStep;
				yInt -= yStep;
			}
		}
		
		return distance;
	}
	
	public static double rayCastHorizontalSides(double rAngle) {
		double distance = Const.NONE;
		double xStep;
		double yStep;
		double xInt;
		double yInt;
		int width = map[0].length*boxSize;
		int height = map.length*boxSize;
		
		if (rAngle == Math.PI || rAngle == 0 || rAngle == 2*Math.PI) {
			return distance;
		} else if (rAngle < Math.PI) { //up
			yInt = boxSize * (Math.floor(pY / boxSize));
			xInt = pX + (yInt - pY) / Math.tan(-rAngle);
			yStep = -boxSize;
			xStep = yStep / Math.tan(rAngle) ;			
		} else { //down
			yInt = boxSize * (Math.ceil(pY / boxSize));
			xInt = pX + (yInt - pY) / Math.tan(-rAngle);
			yStep = boxSize;
			xStep = yStep / Math.tan(rAngle);
		}

		while (xInt < width && xInt >= 0 && yInt < height && yInt >= 0) {
			if (map[(int) yInt / boxSize-1][(int) xInt / boxSize] >= 1) {
				wallType2 = map[(int) yInt / boxSize-1][(int) xInt / boxSize];
				distance = distance(pX, pY, xInt, yInt);
				break;
			} else if (map[(int) yInt / boxSize][(int) xInt / boxSize] >= 1) {
				wallType2 = map[(int) yInt / boxSize][(int) xInt / boxSize];
				distance = distance(pX, pY, xInt, yInt);
				break;
			} else {
				xInt -= xStep;
				yInt += yStep;
			}
		}
		
		return distance;
	}
	
	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
	}

//------------------------------------------------------------------------------  
	static class GraphicsPanel extends JPanel {
		public GraphicsPanel() {
			setFocusable(true);
			requestFocusInWindow();
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g); // required
//			Graphics2D g2 = (Graphics2D) g;
//			g2.setColor(Color.GRAY);
//			g2.fillRect(0,0,widthScreen,heightScreen/2);
//			g2.setColor(Color.BLACK);
//			g2.fillRect(0,heightScreen/2,widthScreen,heightScreen);
			rayCast(g, false);
			
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
						g2.drawRect(columns * boxSize + xOffset, rows * boxSize + yOffset, boxSize, boxSize);
					} else if (map[rows][columns] >= 1) {
						g2.setColor(Color.BLACK);
						g2.fillRect(columns * boxSize + xOffset, rows * boxSize + yOffset, boxSize, boxSize);
					}
					g2.setColor(Color.BLACK);
					g2.drawRect(columns * boxSize + xOffset, rows * boxSize + yOffset, boxSize, boxSize);
				}
			}
			
			rayCast(g, true);

			g.setColor(Color.ORANGE);
			g2.rotate(-pAngle.getAngle(), pX+ xOffset, pY+ yOffset);
			g2.fillRect((int) pX - pSize / 2 + xOffset, (int) pY - pSize / 2 + yOffset, pSize, pSize);
			
			
			
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
