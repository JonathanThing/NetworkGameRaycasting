import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class RayCaster {
	
	private Vector playerPosition;
	private Angle playerAngle;
	private double fov;
	private Vector cameraOffset;
	private Level map;
	private TextureManager textures;
	private int numberOfRays = 360;
	private double dist[] = new double[numberOfRays];
	private int wallTypeHorizontal;
	private int wallTypeVertical;
	
	public RayCaster (TextureManager textures) { 
		this.textures = textures;
		this.playerAngle = new Angle(0);
	}
	
	public void updateInformation(Player player, Vector cameraOffset, Level map, double fov) {
		this.playerPosition = player.getPosition();
		this.playerAngle = player.getAngle();
		this.fov = fov;
		this.cameraOffset = cameraOffset;
		this.map = map;
	}
	
	public void drawSprite(Graphics2D g2, ArrayList<Entity> entities) {

		//1. Sort the sprites from closest to fathest from player
		//2. Find position of the sprites relative to the camera
		//3. Draw the sprite
		
		class DistanceComparator implements Comparator<Entity> {
            
            @Override
            public int compare(Entity a, Entity b) {
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
	
		Collections.sort(entities, new DistanceComparator());
				
		//Left and Right edges of the player Fov, with a little extra space to allow sprites to be partially drawn when offscreen
		double rightEdgeView = Angle.checkLimit(playerAngle.getValue() + 2*fov/3);
		double leftEdgeView = Angle.checkLimit(playerAngle.getValue() - 2*fov/3);	
		
		for (Entity entity : entities) {
						
			//Do not draw if the entity position is on the same position as the player
			if (entity.getPosition().equals(playerPosition)) {
				continue;
			}
					
			Vector spriteVectorFromPlayer = entity.getPosition().subtract(playerPosition);
			Angle spriteAngle = new Angle(-Math.atan2(spriteVectorFromPlayer.getY(),spriteVectorFromPlayer.getX()));
			
		if (inView(leftEdgeView, rightEdgeView, spriteAngle)) {

			//Calculate the step angles for the entity's directional sprites
			double directionalStepAngle = (2*Math.PI)/entity.getSprites().getNumberOfDirectionTextures();
			//Calculate what direction the player sees
			double angleDifference = Math.round((spriteAngle.getValue()-entity.getAngle().getValue())/directionalStepAngle);
			Angle spriteAngleDirection = new Angle (Angle.checkLimit((directionalStepAngle * angleDifference) - Math.PI)); 
			
			double spriteAngleFromCentre = playerAngle.getValue()-spriteAngle.getValue();
			double distance = spriteVectorFromPlayer.magnitude();	
			double cameraRayLength = Math.cos(spriteAngleFromCentre)*distance; //Distance of player to camera plane
			double section = Math.sin(spriteAngleFromCentre)*distance; //length from player sprite on the camera plane from middle
			double cameraPlaneLength = Math.tan(fov/2)*cameraRayLength*2; //Length of the entire camera plane

			double cameraX = (double)Const.WIDTH/2 + (double)Const.WIDTH*(section/cameraPlaneLength) ;
						
			//Rotate camera plane to face right so that x is distance between camera plane and sprite (not eculidean distance)
			Vector rotatedVector = spriteVectorFromPlayer.rotateVector(playerAngle.getValue());
			double distanceToPlane = rotatedVector.getX();
			
			int scale=(int)(Const.TEXTURE_SIZE*Const.HEIGHT/distanceToPlane);		
			double step = scale/(double)Const.TEXTURE_SIZE;
			double strokeWidth = Math.abs(scale/(double)Const.TEXTURE_SIZE);		
			g2.setStroke(new BasicStroke((int) strokeWidth+1));
						
			double yOffset = (scale/(double)Const.HEIGHT) * entity.getSpriteZOffset();
			scale *= entity.getSpriteScale();
			
			for (int j = 0; j < Const.TEXTURE_SIZE; j++) {
				double cameraXPosition = cameraX +j*step - scale/2-7;
				double cameraXPositionCheck = cameraX +j*step- scale/2;
					if ((((cameraXPositionCheck)/(double)Const.WIDTH)*numberOfRays) >= 0 && (((cameraXPositionCheck)/(double)Const.WIDTH)*numberOfRays) < 360 && Math.abs(distanceToPlane) < dist[(int)(((cameraXPositionCheck)/Const.WIDTH)*numberOfRays)]) {
						for (int k = 0; k < Const.TEXTURE_SIZE; k++) { 	
							int direction = (int) (spriteAngleDirection.getValue()/directionalStepAngle);
							int animationNumber = entity.getSprites().getAnimationNumber() % entity.getSprites().getNumberOfAnimationTextures(direction);
							g2.setColor(new Color(entity.getSprites().getSingleTexture(direction, animationNumber).getRGB(j,k)));
							if (!g2.getColor().equals(new Color (74,65,42))) {
								g2.drawLine((int) (cameraXPosition), (int)((double)Const.HEIGHT/2 - scale/2 + k*step+ yOffset), (int) (cameraXPosition), (int) ((double)Const.HEIGHT/2 - scale/2 + k*step+yOffset));
							}
						}						
					}
				}
			}
		}
	}
	
	public boolean inView(double leftEdgeView, double rightEdgeView, Angle spriteAngle) {
		return (leftEdgeView < spriteAngle.getValue() && spriteAngle.getValue() < rightEdgeView ) || (leftEdgeView > rightEdgeView && (leftEdgeView < spriteAngle.getValue() || rightEdgeView > spriteAngle.getValue()));		
	}
	
	//It doesnt work well if you want to use it, use it at your own risk
	public void rayCastFloors(Graphics2D g2) {
		
		double posZ;
		int p;
		int numberOfRays = 150;
		double step = (((double)Const.HEIGHT/2.0)/numberOfRays);

		for (int y = 0; y < numberOfRays; y ++) {

			p = (int) (((double)Const.HEIGHT/2)- (y*step));
			posZ = ((double)Const.HEIGHT/2.0);
			double rowDistance = (posZ/p) * (double)Const.BOXSIZE;

			Vector leftSide = new Vector(rowDistance* Math.cos(Angle.checkLimit(playerAngle.getValue()+Const.FOV/2)), (rowDistance* Math.sin(Angle.checkLimit(playerAngle.getValue()+Const.FOV/2))));
			Vector rightSide = new Vector(rowDistance* Math.cos(Angle.checkLimit(playerAngle.getValue()-Const.FOV/2)), (rowDistance* Math.sin(Angle.checkLimit(playerAngle.getValue()-Const.FOV/2))));
			
			double stepX = (leftSide.getX() - rightSide.getX())/numberOfRays;
			double stepY = (rightSide.getY() - leftSide.getY())/numberOfRays;
			
			double floorX = playerPosition.getX() + (rowDistance) * Math.cos(Angle.checkLimit(playerAngle.getValue()+Const.FOV/2));
			double floorY = playerPosition.getY() - (rowDistance) * Math.sin(Angle.checkLimit(playerAngle.getValue()+Const.FOV/2));
			
			int widthStep = (int) ((double)Const.WIDTH/numberOfRays);
					
				for(int x = 0; x < numberOfRays; x++) {
				
					int tileX = (int)(floorX/(double)Const.BOXSIZE);
					int tileY = (int)(floorY/(double)Const.BOXSIZE);
					
					int pixelX = (int) Math.abs(floorX % (double)Const.TEXTURE_SIZE);
					int pixelY = (int) Math.abs(floorY % (double)Const.TEXTURE_SIZE);
					
					floorX -= stepX;
					floorY -= stepY;
					
					g2.setColor(new Color(textures.getSingleTexture(0,0).getRGB(pixelX, pixelY)));
					g2.setStroke(new BasicStroke((int)step+1));
					g2.drawLine(x*widthStep, Const.HEIGHT - (int)(y*step), (x+1)*widthStep,  Const.HEIGHT - (int)(y*step));
					g2.drawLine(x*widthStep, (int)(y*step), (x+1)*widthStep, (int)(y*step));
			}	
		}
			
			
	}

	
	public void rayCastWalls(Graphics2D g2, boolean drawingMap) {

		double distVerticalSide;
		double distHorizontalSide;
		int wallType = 0;
		double rayDistance = 0;
		double rayAngle;

		double cameraPlane = Math.tan(fov/2)*2; //Distance of camera plane
		double incrementPlane = cameraPlane/numberOfRays; //Distance between every ray on the plane
		
		for (int rays = 0; rays < numberOfRays; rays++) {
			
			rayAngle = Angle.checkLimit(playerAngle.getValue() + Math.atan(incrementPlane*(rays-numberOfRays/2)));

			distVerticalSide = rayCastVerticalSides(rayAngle);
			distHorizontalSide = rayCastHorizontalSides(rayAngle);
					
			boolean verticalWall = false;
			
			if (distHorizontalSide < distVerticalSide && distHorizontalSide != Const.NONE) { //horizontal side is closer
				rayDistance = distHorizontalSide;
				wallType = wallTypeHorizontal;
				verticalWall = false;
			} else if (distVerticalSide < distHorizontalSide && distVerticalSide != Const.NONE) { //vertical side is closer
				rayDistance = distVerticalSide;
				wallType = wallTypeVertical;
				verticalWall = true;
			}
			
			Vector rayVector = new Vector(playerPosition.getX() +  rayDistance*Math.cos(rayAngle), playerPosition.getY() -  rayDistance*Math.sin(rayAngle));
			
			if (drawingMap) {
			
				g2.setColor(new Color(255, 165, 0));
				g2.setStroke(new BasicStroke(1));
				g2.drawLine((int) (playerPosition.getX() + cameraOffset.getX()), (int) (playerPosition.getY() + cameraOffset.getY()),(int)(playerPosition.getX() + rayDistance*Math.cos(rayAngle) + cameraOffset.getX()), (int)(playerPosition.getY() - rayDistance*Math.sin(rayAngle) + cameraOffset.getY()));
				
			} else {
					
				rayDistance = rayDistance*Math.cos(rayAngle-playerAngle.getValue());
				dist[numberOfRays-rays-1] = rayDistance;
				double strokeWidth = (double)Const.WIDTH/numberOfRays;
				g2.setStroke(new BasicStroke((int)strokeWidth+1));
				
				int numbPixel = textures.getTextureHeight(wallType, 0);
				double lineH = (Const.HEIGHT*Const.BOXSIZE/2)/rayDistance;
				double middle = lineH/2;
				double stepPattern = (lineH/numbPixel);
				int textureX;
				int textureY = 0;
				
				if (!verticalWall) {
					if (rayAngle < Math.PI) {
						textureX = (int)(rayVector.getX() / (Const.BOXSIZE/textures.getTextureWidth(wallType, 0)) % textures.getTextureWidth(wallType, 0));
					} else {
						textureX = numbPixel- (int)(rayVector.getX() / (Const.BOXSIZE/textures.getTextureWidth(wallType, 0)) % textures.getTextureWidth(wallType, 0))-1;
					}
				} else {
					if (rayAngle < Math.PI/2 || rayAngle > 3*Math.PI/2) {
						textureX = (int)(rayVector.getY() / (Const.BOXSIZE/textures.getTextureWidth(wallType, 0)) % textures.getTextureWidth(wallType, 0));
					} else {
						textureX = numbPixel- (int)(rayVector.getY() / (Const.BOXSIZE/textures.getTextureWidth(wallType, 0)) % textures.getTextureWidth(wallType, 0))-1;
					}
				}
				
				for (int i = 0; i < numbPixel; i++) {
					textureY = (numbPixel-1-i);
					
					BufferedImage thing = textures.getSingleTexture(wallType, 0);
					int value = thing.getRGB((int)textureX,(int)textureY);
					
					if (rayDistance == Const.NONE || wallType < 0) {
						g2.setColor(Color.WHITE);
					} else {
						if (verticalWall) {
							g2.setColor(new Color(value));
						} else {
							g2.setColor(new Color(value).darker().darker());
						}
					}

					g2.drawLine((int) (Const.WIDTH-rays*strokeWidth), (int) (Const.HEIGHT/2+(middle) - i*stepPattern) ,(int) (Const.WIDTH-rays*strokeWidth), (int) (Const.HEIGHT/2 + (middle) - (i+1)*stepPattern ));
				}		

			}	
		}
	}
	
	public double rayCastVerticalSides(double rayAngle) {
		double distance = Const.NONE;
		Vector step = new Vector();
		Vector gridLines = new Vector();
				
		if (rayAngle == Math.PI/2 || rayAngle == 3*Math.PI/2) { // Looking up or down
			return distance;
		} else if (rayAngle < Math.PI/2 || rayAngle > 3*Math.PI/2) { //Looking right
			gridLines.setX(Const.BOXSIZE * (Math.ceil(playerPosition.getX() / Const.BOXSIZE))); 
			gridLines.setY(playerPosition.getY() - ((gridLines.getX()-playerPosition.getX()) * Math.tan(rayAngle)));
			step.setX(Const.BOXSIZE);
			step.setY(-Math.tan(rayAngle) * step.getX());			
		} else { //Looking left
			gridLines.setX(Const.BOXSIZE * (Math.floor(playerPosition.getX() / Const.BOXSIZE)));
			gridLines.setY(playerPosition.getY() - ((gridLines.getX()-playerPosition.getX()) * Math.tan(rayAngle)));
			step.setX(-Const.BOXSIZE);
			step.setY(-Math.tan(rayAngle) * step.getX());		
		}

		while (gridLines.inRange(map)) {
			//Get current tile in relation to the row and column of the level
			Vector currentTile = gridLines.divideByScalar(Const.BOXSIZE).flipXY();
			Vector currentTileBehind = currentTile.subtract(new Vector(0,1));
			
			if (map.getMapTile(currentTileBehind) >= 1) {
				wallTypeVertical = map.getMapTile(currentTileBehind) -1;
				distance = playerPosition.distance(gridLines);
				break;
			} else if (map.getMapTile(currentTile) >= 1) {
				wallTypeVertical = map.getMapTile(currentTile)-1;
				distance = playerPosition.distance(gridLines);
				break;
			} else {
				gridLines = gridLines.add(step);
			}
		}
		
		return distance;
	}
	
	public double rayCastHorizontalSides(double rayAngle) {
		double distance = Const.NONE;
		Vector step = new Vector();
		Vector gridLines = new Vector();

		if (rayAngle == Math.PI || rayAngle == 0 || rayAngle == 2*Math.PI) { //Looking left or right
			return distance;
		} else if (rayAngle < Math.PI) { //Looking up
			gridLines.setY(Const.BOXSIZE * (Math.floor(playerPosition.getY() / Const.BOXSIZE)));
			gridLines.setX(playerPosition.getX() + ((gridLines.getY() - playerPosition.getY()) / Math.tan(-rayAngle)));
			step.setY(-Const.BOXSIZE);
			step.setX(-step.getY() / Math.tan(rayAngle));			
		} else { //Looking down
			gridLines.setY(Const.BOXSIZE * (Math.ceil(playerPosition.getY() / Const.BOXSIZE)));
			gridLines.setX(playerPosition.getX() + ((gridLines.getY() - playerPosition.getY()) / Math.tan(-rayAngle)));
			step.setY(Const.BOXSIZE);
			step.setX(-step.getY()/ Math.tan(rayAngle));
		}

		while (gridLines.inRange(map)) {
			
			Vector currentTile = gridLines.divideByScalar(Const.BOXSIZE).flipXY();
			Vector currentTileBehind = currentTile.subtract(new Vector(1,0));
			
			if (map.getMapTile(currentTileBehind) >= 1) {
				wallTypeHorizontal = map.getMapTile(currentTileBehind)-1;
				distance = playerPosition.distance(gridLines);
				break;
			} else if (map.getMapTile(currentTile) >= 1) {
				wallTypeHorizontal = map.getMapTile(currentTile) -1;
				distance = playerPosition.distance(gridLines);
				break;
			} else {
				gridLines = gridLines.add(step);
			}
		}
		
		return distance;
	}
}
