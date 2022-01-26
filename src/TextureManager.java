import java.awt.image.BufferedImage;

public class TextureManager {

    private final BufferedImage[][] textures;

    private int animationNumber = 0;

    private long lastAnimationChange = 0;

    public TextureManager(BufferedImage textureSheet) {
        int numberOfDirections = textureSheet.getWidth() / Const.TEXTURE_SIZE;

        textures = new BufferedImage[numberOfDirections][];

        for (int i = 0; i < numberOfDirections; i++) {

            int numberOfAnimations = textureSheet.getHeight() / Const.TEXTURE_SIZE;

            textures[i] = new BufferedImage[numberOfAnimations];

            for (int j = 0; j < numberOfAnimations; j++) {

                textures[i][j] = textureSheet.getSubimage(i * (Const.TEXTURE_SIZE), j * (Const.TEXTURE_SIZE), Const.TEXTURE_SIZE, Const.TEXTURE_SIZE);

            }
        }
    }

    public TextureManager(TextureManager other) {
        textures = other.getTextures();
    }

    public BufferedImage[][] getTextures() {
        return textures;
    }

    public BufferedImage[] getAnimations(int direction) {
        return textures[direction];
    }

    public BufferedImage getSingleTexture(int direction, int animationNumber) {
        return textures[direction][animationNumber];
    }

    public int getNumberOfDirectionTextures() {
        return textures.length;
    }

    public int getNumberOfAnimationTextures(int direction) {
        return textures[direction].length;
    }

    public int getTexturePixel(int direction, int animationNumber, int rows, int columns) {
        return (textures[direction][animationNumber].getRGB(columns, rows));
    }

    public int getTextureHeight(int direction, int animationNumber) {
        return textures[direction][animationNumber].getHeight();
    }

    public int getTextureWidth(int direction, int animationNumber) {
        return textures[direction][animationNumber].getWidth();
    }

    public int getAnimationNumber() {
        return animationNumber;
    }

    public void setAnimationNumber(int animationNumber) {
        this.animationNumber = animationNumber;
    }

    public void changeAnimationNumber(int change) {
        this.animationNumber += change;
    }

    public long getLastAnimationChange() {
        return lastAnimationChange;
    }

    public void setLastAnimationChange(long lastAnimationChange) {
        this.lastAnimationChange = lastAnimationChange;
    }

}