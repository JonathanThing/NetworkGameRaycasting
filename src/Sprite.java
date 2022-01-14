import java.awt.image.BufferedImage;

public class Sprite {
	private BufferedImage texture;
	private Vector position;
	private double z;
	private double size;
	

	public Sprite(Vector position, BufferedImage texture, double z, double size) {
		this.texture = texture;
		this.position = position;
		this.z = z;
		this.size = size;
	}

	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
	public BufferedImage getTexture() {
		return texture;
	}
	public void setTexture(BufferedImage texture) {
		this.texture = texture;
	}
	public Vector getPosition() {
		return position;
	}
	public void setPosition(Vector position) {
		this.position = position;
	}
	
}