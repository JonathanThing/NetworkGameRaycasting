// pls remember to fix later


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;

public class RayCaster {

	private Level map;
	private Angle playerAngle;
	private int wallType1;
	private int wallType2;
	private TextureList textures;
	private Vector playerPosition;
	private double fov;
	private int numberOfRays = 360;
	private double dist[] = new double[numberOfRays];
			
	public RayCaster (TextureList textures) { 
		this.textures = textures;
	}
	
	public void drawSprite(Graphics2D g2, Sprite[] sprites, boolean drawMap) {

		
		class DistanceComparator implements Comparator<Sprite> {
            
            @Override
            public int compare(Sprite a, Sprite b) {
            	double dist1 = a.getPosition().distance(playerPosition);
            	double dist2 = b.getPosition().distance(playerPosition);

            	if (dist1 == dist2) {
            		return 0;
            	} else if (dist1 > dist2) {
            		return -1;
            	} else {
            		return 1;
            	}
            }
            
        }
		Arrays.sort(sprites, new DistanceComparator());
		
		for (Sprite sprite: sprites) {
			
			Vector spritePosition = sprite.getPosition();
			Vector spriteVectorFromPlayer = spritePosition.subtract(playerPosition);
			Vector rotatedVector = spriteVectorFromPlayer.rotateVector(playerAngle.getAngleValue());
			Angle spriteAngle = new Angle(-Math.atan2(spriteVectorFromPlayer.getY(),spriteVectorFromPlayer.getX()));
	
			double rightViewEdge = Angle.checkLimit(playerAngle.getAngleValue() + fov/2);
			double leftViewEdge = Angle.checkLimit(playerAngle.getAngleValue() - fov/2);	
			
			double distance = spriteVectorFromPlayer.magnitude();	
			double ray = Math.cos(playerAngle.getAngleValue()-spriteAngle.getAngleValue())*distance;
			double section = Math.sin(playerAngle.getAngleValue()-spriteAngle.getAngleValue())*distance;
			double plane = Math.abs(Math.tan(playerAngle.getAngleValue() - leftViewEdge)*ray)*2;
			double distanceToPlane = rotatedVector.getX();
			double cameraX = (double)Const.TRUE_WIDTH/2 + (double)Const.TRUE_WIDTH*(section/plane) ;
			int scale=(int)(Const.TEXTURE_SIZE*Const.HEIGHT/distanceToPlane);		
			double step = scale/(double)Const.TEXTURE_SIZE;
			double strokeWidth = Math.abs(scale/(double)Const.TEXTURE_SIZE);		
			g2.setStroke(new BasicStroke((int) strokeWidth+1));
			double yOffset;
			rightViewEdge = Angle.checkLimit(playerAngle.getAngleValue() + 2*fov/3);
			leftViewEdge = Angle.checkLimit(playerAngle.getAngleValue() - 2*fov/3);
			
			scale *= sprite.getSize();
			if (sprite.getZ() == 0) {
				yOffset = 0;
			} else {
				yOffset = scale/sprite.getZ();
			}
			for (int j = 0; j < Const.TEXTURE_SIZE; j++) {
				double thing = cameraX +j*step - scale/2 -5;
				double thingCheck = cameraX +j*step- scale/2;
				if ((spriteAngle.getAngleValue() < rightViewEdge && spriteAngle.getAngleValue() > leftViewEdge) || (leftViewEdge > rightViewEdge && (leftViewEdge < spriteAngle.getAngleValue() || rightViewEdge > spriteAngle.getAngleValue()))) {
					if ((((thingCheck)/(double)Const.TRUE_WIDTH)*numberOfRays) >= 0 && (((thingCheck)/(double)Const.TRUE_WIDTH)*numberOfRays) < 360 && Math.abs(rotatedVector.getY()) < dist[(int)(((thingCheck)/Const.TRUE_WIDTH)*numberOfRays)]) {
						for (int k = 0; k < Const.TEXTURE_SIZE; k++) { 		
							g2.setColor(new Color(sprite.getTexture().getRGB(j,k)));
							if (!g2.getColor().equals(new Color (74,65,42))) {
								g2.drawLine((int) (thing), (int)((double)Const.HEIGHT/2 - scale/2 + k*step+ yOffset) , (int) (thing), (int) ((double)Const.HEIGHT/2 - scale/2 + k*step+yOffset));
							}
						}						
					}
				}
			}
		}
	}
	
	public void rayCast(Graphics2D g2, boolean drawMap, Vector playerPosition, Angle playerAngle, Vector cameraOffset, Level map) {
		this.map = map;
		this.playerAngle = playerAngle;
		this.playerPosition = playerPosition;
		
		double dist1;
		double dist2;
		double fov = Math.toRadians(90); 
		double rayAngle;
		double plane = Math.tan(fov/2)*2;
		double inc = plane/numberOfRays;
		this.fov = fov;
		
		for (int rays = 0; rays < numberOfRays; rays++) {
			
			rayAngle = Angle.checkLimit(playerAngle.getAngleValue() + Math.atan(inc*(rays-numberOfRays/2)));

			dist1 = rayCastVerticalSides(rayAngle);
			dist2 = rayCastHorizontalSides(rayAngle);
					
			boolean horizontal = false;
			
			if (dist1 > dist2 && dist2 != Const.NONE) { //hit veritcal side
				dist1 = dist2;
				wallType1 = wallType2;
			} else if (dist1 < dist2 && dist1 != Const.NONE) { //hit horizontal side
				horizontal = true;
			}
			
			Vector rayVector = new Vector(playerPosition.getX() +  dist1*Math.cos(rayAngle), playerPosition.getY() -  dist1*Math.sin(rayAngle));
			
			if (drawMap) {
			
				g2.setColor(new Color(255, 165, 0));
				g2.setStroke(new BasicStroke(1));
				g2.drawLine((int) (playerPosition.getX() + cameraOffset.getX()), (int) (playerPosition.getY() + cameraOffset.getY()),(int)(playerPosition.getX() + dist1*Math.cos(rayAngle) + cameraOffset.getX()), (int)(playerPosition.getY() - dist1*Math.sin(rayAngle) + cameraOffset.getY()));
				
			} else {
					
				wallType1--;
				
				dist1 = dist1*Math.cos(rayAngle-playerAngle.getAngleValue());
				dist[numberOfRays-rays-1] = dist1;
				int strokeWidth = Const.WIDTH/numberOfRays;
				g2.setStroke(new BasicStroke(strokeWidth));

				
				int numbPixel = textures.getTextureHeight(wallType1);
				double lineH = (Const.HEIGHT*Const.BOXSIZE/2)/dist1;
				double middle = lineH/2;
				double stepPattern = (lineH/numbPixel);
				int textureX;
				int textureY = 0;
				
				if (!horizontal) {
					if (rayAngle < Math.PI) {
						textureX = (int)(rayVector.getX() / (Const.BOXSIZE/textures.getTextureWidth(wallType1)) % textures.getTextureWidth(wallType1));
					} else {
						textureX = numbPixel- (int)(rayVector.getX() / (Const.BOXSIZE/textures.getTextureWidth(wallType1)) % textures.getTextureWidth(wallType1))-1;
					}
				} else {
					if (rayAngle < Math.PI/2 || rayAngle > 3*Math.PI/2) {
						textureX = (int)(rayVector.getY() / (Const.BOXSIZE/textures.getTextureWidth(wallType1)) % textures.getTextureWidth(wallType1));
					} else {
						textureX = numbPixel- (int)(rayVector.getY() / (Const.BOXSIZE/textures.getTextureWidth(wallType1)) % textures.getTextureWidth(wallType1))-1;
					}
				}
				
				for (int i = 0; i < numbPixel; i++) {
					textureY = (numbPixel-1-i);
					
					BufferedImage thing = textures.getSingleTexture(wallType1);
					int value = thing.getRGB((int)textureX,(int)textureY);
					
					if (dist1 == Const.NONE || wallType1 < 0) {
						g2.setColor(Color.WHITE);
					} else {
						if (horizontal) {
							g2.setColor(new Color(value));
						} else {
							g2.setColor(new Color(value).darker().darker());
						}
					}

					g2.drawLine(Const.WIDTH-rays*strokeWidth - 80, (int) (Const.HEIGHT/2+(middle) - i*stepPattern) , Const.WIDTH-rays*strokeWidth - 80, (int) (Const.HEIGHT/2 + (middle) - (i+1)*stepPattern ));
				}		
			}	
		}
	}
	
	public double rayCastVerticalSides(double rayAngle) {
		
		double distance = Const.NONE;
		double xStep;
		double yStep;
		double xInt;
		double yInt;
		int width = map.getColumns()*Const.BOXSIZE;
		int height = map.getRows()*Const.BOXSIZE;
		
		if (rayAngle == Math.PI/2 || rayAngle == 3*Math.PI/2) {
			return distance;
		} else if (rayAngle < Math.PI/2 || rayAngle > 3*Math.PI/2) { //right
			xInt = Const.BOXSIZE * (Math.ceil(playerPosition.getX() / Const.BOXSIZE));
			yInt = playerPosition.getY() - (xInt-playerPosition.getX()) * Math.tan(rayAngle);
			xStep = Const.BOXSIZE;
			yStep = Math.tan(rayAngle) * xStep;			
		} else { //left
			xInt = Const.BOXSIZE * (Math.floor(playerPosition.getX() / Const.BOXSIZE));
			yInt = playerPosition.getY() - (xInt-playerPosition.getX()) * Math.tan(rayAngle);
			xStep = -Const.BOXSIZE;
			yStep = Math.tan(rayAngle) * xStep;
		}
		
		while (xInt < width && xInt >= 0 && yInt < height && yInt >= 0) {
			if (map.getMapTile((int) yInt / Const.BOXSIZE,(int) xInt / Const.BOXSIZE-1) >= 1) {
				wallType1 = map.getMapTile((int) yInt / Const.BOXSIZE,(int) xInt / Const.BOXSIZE-1);
				distance = playerPosition.distance(new Vector(xInt, yInt));
				break;
			} else if (map.getMapTile((int) yInt / Const.BOXSIZE,(int) xInt / Const.BOXSIZE) >= 1) {
				wallType1 = map.getMapTile((int) yInt / Const.BOXSIZE,(int) xInt / Const.BOXSIZE);
				distance = playerPosition.distance(new Vector(xInt, yInt));
				break;
			} else {
				xInt += xStep;
				yInt -= yStep;
			}
		}
		
		return distance;
	}
	
	public double rayCastHorizontalSides(double rayAngle) {
		double distance = Const.NONE;
		double xStep;
		double yStep;
		double xInt;
		double yInt;
		int width = map.getColumns()*Const.BOXSIZE;
		int height = map.getRows()*Const.BOXSIZE;
		
		if (rayAngle == Math.PI || rayAngle == 0 || rayAngle == 2*Math.PI) {
			return distance;
		} else if (rayAngle < Math.PI) { //up
			yInt = Const.BOXSIZE * (Math.floor(playerPosition.getY() / Const.BOXSIZE));
			xInt = playerPosition.getX() + (yInt - playerPosition.getY()) / Math.tan(-rayAngle);
			yStep = -Const.BOXSIZE;
			xStep = yStep / Math.tan(rayAngle) ;			
		} else { //down
			yInt = Const.BOXSIZE * (Math.ceil(playerPosition.getY() / Const.BOXSIZE));
			xInt = playerPosition.getX() + (yInt - playerPosition.getY()) / Math.tan(-rayAngle);
			yStep = Const.BOXSIZE;
			xStep = yStep / Math.tan(rayAngle);
		}

		while (xInt < width && xInt >= 0 && yInt < height && yInt >= 0) {
			if (map.getMapTile((int) yInt / Const.BOXSIZE-1,(int) xInt / Const.BOXSIZE) >= 1) {
				wallType2 = map.getMapTile((int) yInt / Const.BOXSIZE-1,(int) xInt / Const.BOXSIZE);
				distance = playerPosition.distance(new Vector(xInt, yInt));
				break;
			} else if (map.getMapTile((int) yInt / Const.BOXSIZE,(int) xInt / Const.BOXSIZE) >= 1) {
				wallType2 = map.getMapTile((int) yInt / Const.BOXSIZE,(int) xInt / Const.BOXSIZE);
				distance = playerPosition.distance(new Vector(xInt, yInt));
				break;
			} else {
				xInt -= xStep;
				yInt += yStep;
			}
		}
		
		return distance;
	}
}
