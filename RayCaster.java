import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class RayCaster {

	private Graphics2D g2;
	private int map[][];
	private double pX, pY;
	private Angle pAngle;
	private int xOffset, yOffset;
	private int wallType1;
	private int wallType2;
	private int texture[][][];
			
	public RayCaster (int[][][] texture) {
		
		this.texture = texture;
	}
	
	public void rayCast(Graphics2D g2, boolean bruh, double pX,double pY, Angle pAngle, int xOffset, int yOffset, int [][] map) {
		this.g2 = g2;
		this.map = map;
		this.pX = pX;
		this.pY = pY;
		this.pAngle = pAngle;
		this.xOffset = xOffset;
		this.yOffset= yOffset;
		
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
				
				int strokeWidth = Const.WIDTH/numb;
				g2.setStroke(new BasicStroke(strokeWidth));
				
				int numbPixel = texture[0].length;
				double lineH = (Const.HEIGHT*Const.BOXSIZE/2)/dist1;
				double middle = lineH/2;
				double stepPattern = (lineH/numbPixel);
				int textureX;
				int textureY = 0;
				
				
				if (!horizontal) {
					if (rAngle < Math.PI) {
						textureX = (int)(rx / (Const.BOXSIZE/texture[0][0].length) % texture[0][0].length);
					} else {
						textureX = numbPixel- (int)(rx / (Const.BOXSIZE/texture[0][0].length) % texture[0][0].length)-1;
					}
				} else {
					if (rAngle < Math.PI/2 || rAngle > 3*Math.PI/2) {
						textureX = (int)(ry / (Const.BOXSIZE/texture[0][0].length) % texture[0][0].length);
					} else {
						textureX = numbPixel- (int)(ry / (Const.BOXSIZE/texture[0][0].length) % texture[0][0].length)-1;
					}
				}

				if (lineH > Const.HEIGHT) {
					lineH = Const.HEIGHT;
				}
					
				wallType1--;
				
				for (int i = 0; i < numbPixel; i++) {
					textureY = (numbPixel-1-i);
					
				 if (dist1 == Const.NONE || wallType1 < 0) {
						g2.setColor(Color.WHITE);
					} else if (texture[wallType1][(int)textureY][(int)textureX] == 1 && horizontal) {
						g2.setColor(new Color(170, 110, 0));
					} else if (texture[wallType1][(int)textureY][(int)textureX] == 0 && horizontal) {
						g2.setColor(new Color(255, 165, 0));
					} else if (texture[wallType1][(int)textureY][(int)textureX] == 1 && !horizontal) {
						g2.setColor(new Color(64, 41, 0));
					} else if (texture[wallType1][(int)textureY][(int)textureX] == 0 && !horizontal) {
						g2.setColor(new Color(127, 82, 0));
					}

					g2.drawLine(Const.WIDTH-rays*strokeWidth - 80, (int) (Const.HEIGHT/2+(middle) - i*stepPattern) , Const.WIDTH-rays*strokeWidth - 80, (int) (Const.HEIGHT/2 + (middle) - (i+1)*stepPattern ));

				}
				
				int tx,ty;
				
				for (int i = 0; i < numb/2; i++) {
					
				}
				
			}
			
		}

	}
	
	public double rayCastVerticalSides(double rAngle) {
		
		double distance = Const.NONE;
		double xStep;
		double yStep;
		double xInt;
		double yInt;
		int width = map[0].length*Const.BOXSIZE;
		int height = map.length*Const.BOXSIZE;
		
		if (rAngle == Math.PI/2 || rAngle == 3*Math.PI/2) {
			return distance;
		} else if (rAngle < Math.PI/2 || rAngle > 3*Math.PI/2) { //right
			xInt = Const.BOXSIZE * (Math.ceil(pX / Const.BOXSIZE));
			yInt = pY - (xInt-pX) * Math.tan(rAngle);
			xStep = Const.BOXSIZE;
			yStep = Math.tan(rAngle) * xStep;			
		} else { //left
			xInt = Const.BOXSIZE * (Math.floor(pX / Const.BOXSIZE));
			yInt = pY - (xInt-pX) * Math.tan(rAngle);
			xStep = -Const.BOXSIZE;
			yStep = Math.tan(rAngle) * xStep;
		}
		
		while (xInt < width && xInt >= 0 && yInt < height && yInt >= 0) {
			if (map[(int) yInt / Const.BOXSIZE][(int) xInt / Const.BOXSIZE-1] >= 1) {
				wallType1 = map[(int) yInt / Const.BOXSIZE][(int) xInt / Const.BOXSIZE-1];
				distance = distance(pX, pY, xInt, yInt);
				break;
			} else if (map[(int) yInt / Const.BOXSIZE][(int) xInt / Const.BOXSIZE] >= 1) {
				wallType1 = map[(int) yInt / Const.BOXSIZE][(int) xInt / Const.BOXSIZE];
				distance = distance(pX, pY, xInt, yInt);
				break;
			} else {
				xInt += xStep;
				yInt -= yStep;
			}
		}
		
		return distance;
	}
	
	public double rayCastHorizontalSides(double rAngle) {
		double distance = Const.NONE;
		double xStep;
		double yStep;
		double xInt;
		double yInt;
		int width = map[0].length*Const.BOXSIZE;
		int height = map.length*Const.BOXSIZE;
		
		if (rAngle == Math.PI || rAngle == 0 || rAngle == 2*Math.PI) {
			return distance;
		} else if (rAngle < Math.PI) { //up
			yInt = Const.BOXSIZE * (Math.floor(pY / Const.BOXSIZE));
			xInt = pX + (yInt - pY) / Math.tan(-rAngle);
			yStep = -Const.BOXSIZE;
			xStep = yStep / Math.tan(rAngle) ;			
		} else { //down
			yInt = Const.BOXSIZE * (Math.ceil(pY / Const.BOXSIZE));
			xInt = pX + (yInt - pY) / Math.tan(-rAngle);
			yStep = Const.BOXSIZE;
			xStep = yStep / Math.tan(rAngle);
		}

		while (xInt < width && xInt >= 0 && yInt < height && yInt >= 0) {
			if (map[(int) yInt / Const.BOXSIZE-1][(int) xInt / Const.BOXSIZE] >= 1) {
				wallType2 = map[(int) yInt / Const.BOXSIZE-1][(int) xInt / Const.BOXSIZE];
				distance = distance(pX, pY, xInt, yInt);
				break;
			} else if (map[(int) yInt / Const.BOXSIZE][(int) xInt / Const.BOXSIZE] >= 1) {
				wallType2 = map[(int) yInt / Const.BOXSIZE][(int) xInt / Const.BOXSIZE];
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
	
}
