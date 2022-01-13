import java.awt.image.BufferedImage;

public class Sprite {
	private int type;
	private int state;
	private BufferedImage texture;
	private Vector position;
	
	public Sprite(Vector position,int type, int state, BufferedImage texture) {
		this.type = type;
		this.state = state;
		this.texture = texture;
		this.position = position;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
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