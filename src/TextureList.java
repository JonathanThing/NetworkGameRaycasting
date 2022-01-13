import java.awt.image.BufferedImage;

public class TextureList {
	
	private BufferedImage[] textures;
	
	public TextureList(BufferedImage textureSheet) {
		int numberOfTextures = textureSheet.getWidth()/Const.TEXTURE_SIZE;
		textures = new BufferedImage[numberOfTextures];
		
		for (int i = 0; i < numberOfTextures; i++) {
			textures[i] = textureSheet.getSubimage(i*(Const.TEXTURE_SIZE), 0, Const.TEXTURE_SIZE, Const.TEXTURE_SIZE);
		}
	}

	public BufferedImage[] getTextures() {
		return textures;
	}
	
	public BufferedImage getSingleTexture(int index) {
		return textures[index];
	}
	
	public int getTextureTile(int index, int rows, int columns) {
		return textures[index].getRGB(columns, rows);
	}
	
	public int getTextureHeight(int index) {
		return textures[index].getHeight();
	}
	
	public int getTextureWidth(int index) {
		return textures[index].getWidth();
	}
}
