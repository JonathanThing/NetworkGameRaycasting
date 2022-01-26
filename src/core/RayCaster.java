package core;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import gameObjects.Entity;
import gameObjects.Environment;
import gameObjects.Player;
import misc.Level;
import misc.TextureManager;
import util.Angle;
import util.Const;
import util.Vector;

public class RayCaster {
	
	private Vector playerPosition;
	private Angle playerAngle;
	private double fov = Const.FOV;
	private Vector cameraOffset;
	private Level map;
	private int numberOfRays = 360;
	private double dist[] = new double[numberOfRays];
	private Environment wallHorizontal;
	private Environment wallVertical;
	
	public RayCaster (TextureManager textures) { 

	}
	
	public void updateInformation(Player player, Vector cameraOffset, Level map) {
		this.playerPosition = player.getPosition();
		this.playerAngle = player.getAngle();
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

			double cameraX = Const.WIDTH/2 + Const.WIDTH*(section/cameraPlaneLength);
						
			//Use a rotated camera plane to face right so that x is distance between sprite and player (not euclidean distance)
			Vector rotatedVector = spriteVectorFromPlayer.rotateVector(playerAngle.getValue());
			double distanceToPlane = rotatedVector.getX();
			
			double scale = Const.TEXTURE_SIZE*Const.HEIGHT/distanceToPlane;	
			double yOffset = (scale/Const.HEIGHT) * entity.getSpriteZOffset();
			scale *= entity.getSpriteScale();
			double stepPerPixel = scale/Const.TEXTURE_SIZE;
			double strokeWidth = Math.abs(scale/Const.TEXTURE_SIZE);
			if ((int) strokeWidth+1 == Integer.MIN_VALUE) {
				strokeWidth = 0;
			}

			g2.setStroke(new BasicStroke((int) strokeWidth+1));

			
			for (int j = 0; j < Const.TEXTURE_SIZE; j++) {
				double spriteXPosition = cameraX + j*stepPerPixel - scale/2;	
				int rayNumber = (int) (((spriteXPosition)/Const.WIDTH)*numberOfRays);
					if (rayNumber >= 0 && rayNumber < 360 && Math.abs(distanceToPlane) < dist[rayNumber]) {
						for (int k = 0; k < Const.TEXTURE_SIZE; k++) { 	
							TextureManager textures = entity.getSprites();
							double spriteYPosition = Const.HEIGHT/2.0 - scale/2 + k*stepPerPixel+ yOffset;
							int directionSprite = (int) (spriteAngleDirection.getValue()/directionalStepAngle);
							int animationNumber = textures.getAnimationNumber() % textures.getNumberOfAnimationTextures(directionSprite);
							g2.setColor(new Color(textures.getSingleTexture(directionSprite, animationNumber).getRGB(j,k)));
							if (!g2.getColor().equals(new Color (74,65,42))) {
								g2.drawLine((int) (spriteXPosition), (int)(spriteYPosition), (int) (spriteXPosition), (int) (spriteYPosition));
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
	
//	//It doesnt work well if you want to use it, use it at your own risk
//	public void rayCastFloors(Graphics2D g2) {
//		
//		double posZ;
//		int p;
//		int numberOfRays = 150;
//		double step = ((Const.HEIGHT/2.0)/numberOfRays);
//
//		for (int y = 0; y < numberOfRays; y ++) {
//
//			p = (int) ((Const.HEIGHT/2.0)- (y*step));
//			posZ = (Const.HEIGHT/2.0);
//			double rowDistance = (posZ/p) * Const.BOX_SIZE;
//
//			Vector leftSide = new Vector(rowDistance* Math.cos(Angle.checkLimit(playerAngle.getValue()+Const.FOV/2)), (rowDistance* Math.sin(Angle.checkLimit(playerAngle.getValue()+Const.FOV/2))));
//			Vector rightSide = new Vector(rowDistance* Math.cos(Angle.checkLimit(playerAngle.getValue()-Const.FOV/2)), (rowDistance* Math.sin(Angle.checkLimit(playerAngle.getValue()-Const.FOV/2))));
//			
//			double stepX = (leftSide.getX() - rightSide.getX())/numberOfRays;
//			double stepY = (rightSide.getY() - leftSide.getY())/numberOfRays;
//			
//			double floorX = playerPosition.getX() + (rowDistance) * Math.cos(Angle.checkLimit(playerAngle.getValue()+Const.FOV/2));
//			double floorY = playerPosition.getY() - (rowDistance) * Math.sin(Angle.checkLimit(playerAngle.getValue()+Const.FOV/2));
//			
//			int widthStep = (int) (Const.D_WIDTH/numberOfRays);
//					
//				for(int x = 0; x < numberOfRays; x++) {
//				
//					int tileX = (int)(floorX/Const.D_BOX_SIZE);
//					int tileY = (int)(floorY/Const.D_BOX_SIZE);
//					
//					int pixelX = (int) Math.abs(floorX % Const.D_TEXTURE_SIZE);
//					int pixelY = (int) Math.abs(floorY % Const.D_TEXTURE_SIZE);
//					
//					floorX -= stepX;
//					floorY -= stepY;
//					
//					g2.setColor(new Color(textures.getSingleTexture(0,0).getRGB(pixelX, pixelY)));
//					g2.setStroke(new BasicStroke((int)step+1));
//					g2.drawLine(x*widthStep, Const.HEIGHT - (int)(y*step), (x+1)*widthStep,  Const.HEIGHT - (int)(y*step));
//					g2.drawLine(x*widthStep, (int)(y*step), (x+1)*widthStep, (int)(y*step));
//			}	
//		}	
//	}
	
	public void rayCastWalls(Graphics2D g2, boolean drawingMap) {

		double distVerticalSide;
		double distHorizontalSide;
		Environment wall = null;
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
				wall = wallHorizontal;
				verticalWall = false;
			} else if (distVerticalSide < distHorizontalSide && distVerticalSide != Const.NONE) { //vertical side is closer
				rayDistance = distVerticalSide;
				wall = wallVertical;
				verticalWall = true;
			}
			
			Vector rayVector = new Vector(playerPosition.getX() +  rayDistance*Math.cos(rayAngle), playerPosition.getY() -  rayDistance*Math.sin(rayAngle));
			
			if (drawingMap) {
			
				g2.setColor(new Color(255, 165, 0));
				g2.setStroke(new BasicStroke(1));
				g2.drawLine((int) (playerPosition.getX() + cameraOffset.getX()), (int) (playerPosition.getY() + cameraOffset.getY()),(int)(playerPosition.getX() + rayDistance*Math.cos(rayAngle) + cameraOffset.getX()), (int)(playerPosition.getY() - rayDistance*Math.sin(rayAngle) + cameraOffset.getY()));
				
			} else if (wall != null){
					
				rayDistance = rayDistance*Math.cos(rayAngle-playerAngle.getValue());
				dist[numberOfRays-rays-1] = rayDistance;
				double strokeWidth = (double)Const.WIDTH/numberOfRays;
				g2.setStroke(new BasicStroke((int)strokeWidth+1));
				
				int numbPixel = wall.getSprites().getTextureHeight(0, 0);
				double lineH = (Const.HEIGHT*Const.BOX_SIZE/2)/rayDistance;
				double middle = lineH/2;
				double stepPattern = (lineH/numbPixel);
				int textureX;
				int textureY = 0;
				
				int textureWidth = wall.getSprites().getTextureWidth(0, 0);
				
				if (!verticalWall) {
					if (rayAngle < Math.PI) {
						textureX = (int)(rayVector.getX() / (Const.BOX_SIZE/textureWidth) % textureWidth);
					} else {
						textureX = numbPixel- (int)(rayVector.getX() / (Const.BOX_SIZE/textureWidth) % textureWidth)-1;
					}
				} else {
					if (rayAngle < Math.PI/2 || rayAngle > 3*Math.PI/2) {
						textureX = (int)(rayVector.getY() / (Const.BOX_SIZE/textureWidth) % textureWidth);
					} else {
						textureX = numbPixel- (int)(rayVector.getY() / (Const.BOX_SIZE/textureWidth) % textureWidth)-1;
					}
				}
				
				for (int i = 0; i < numbPixel; i++) {
					textureY = (numbPixel-i-1);
					
					BufferedImage texture = wall.getSprites().getSingleTexture(0, 0);
					int valueRGB = texture.getRGB((int)textureX,(int)textureY);
					
					if (rayDistance == Const.NONE || wall == null) {
						g2.setColor(Color.WHITE);
					} else {
						if (verticalWall) {
							g2.setColor(new Color(valueRGB));
						} else {
							g2.setColor(new Color(valueRGB).darker().darker());
						}
					}

					int xPosition = (int) (Const.WIDTH-rays*strokeWidth);
					int yPosition = (int) (Const.HEIGHT/2+(middle) - i*stepPattern); 
					g2.drawLine(xPosition, yPosition , xPosition, yPosition + (int)stepPattern);
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
			gridLines.setX(Const.BOX_SIZE * (Math.ceil(playerPosition.getX() / Const.BOX_SIZE))); 
			gridLines.setY(playerPosition.getY() - ((gridLines.getX()-playerPosition.getX()) * Math.tan(rayAngle)));
			step.setX(Const.BOX_SIZE);
			step.setY(-Math.tan(rayAngle) * step.getX());			
		} else { //Looking left
			gridLines.setX(Const.BOX_SIZE * (Math.floor(playerPosition.getX() / Const.BOX_SIZE)));
			gridLines.setY(playerPosition.getY() - ((gridLines.getX()-playerPosition.getX()) * Math.tan(rayAngle)));
			step.setX(-Const.BOX_SIZE);
			step.setY(-Math.tan(rayAngle) * step.getX());		
		}

		while (gridLines.inRange(map)) {
			//Get current tile in relation to the row and column of the level
			Vector currentTile = gridLines.divideByScalar(Const.BOX_SIZE).flipXY();
			Vector currentTileBehind = currentTile.subtract(new Vector(0,1));
			
			if (map.getMapTile(currentTileBehind) instanceof Environment) {
				wallVertical = map.getMapTile(currentTileBehind);
				distance = playerPosition.distance(gridLines);
				break;
			} else if (map.getMapTile(currentTile) instanceof Environment) {
				wallVertical = map.getMapTile(currentTile);
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
			gridLines.setY(Const.BOX_SIZE * (Math.floor(playerPosition.getY() / Const.BOX_SIZE)));
			gridLines.setX(playerPosition.getX() + ((gridLines.getY() - playerPosition.getY()) / Math.tan(-rayAngle)));
			step.setY(-Const.BOX_SIZE);
			step.setX(-step.getY() / Math.tan(rayAngle));			
		} else { //Looking down
			gridLines.setY(Const.BOX_SIZE * (Math.ceil(playerPosition.getY() / Const.BOX_SIZE)));
			gridLines.setX(playerPosition.getX() + ((gridLines.getY() - playerPosition.getY()) / Math.tan(-rayAngle)));
			step.setY(Const.BOX_SIZE);
			step.setX(-step.getY()/ Math.tan(rayAngle));
		}

		while (gridLines.inRange(map)) {
			
			Vector currentTile = gridLines.divideByScalar(Const.BOX_SIZE).flipXY();
			Vector currentTileBehind = currentTile.subtract(new Vector(1,0));
			
			if (map.getMapTile(currentTileBehind) instanceof Environment) {
				wallHorizontal = map.getMapTile(currentTileBehind);
				distance = playerPosition.distance(gridLines);
				break;
			} else if (map.getMapTile(currentTile) instanceof Environment) {
				wallHorizontal = map.getMapTile(currentTile);
				distance = playerPosition.distance(gridLines);
				break;
			} else {
				gridLines = gridLines.add(step);
			}
		}
		
		return distance;
	}
}
