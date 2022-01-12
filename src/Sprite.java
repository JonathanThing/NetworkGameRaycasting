public class Sprite {
	private int type;
	private int state;
	private int texture[][];
	private Vector position;
	private int z;
	
	public Sprite(Vector position, int z ,int type, int state, int[][] texture) {
		this.type = type;
		this.state = state;
		this.texture = texture;
		this.position = position;
		this.z = z;
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

	public int[][] getTexture() {
		return texture;
	}

	public void setTexture(int[][] texture) {
		this.texture = texture;
	}

	public Vector getPosition() {
		return position;
	}

	public void setPosition(Vector position) {
		this.position = position;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}
	
	
	
	

	
}