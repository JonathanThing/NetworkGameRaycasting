public class Sprite {
	private int type;
	private int state;
	private int texture[][];
	private int x, y, z;
	
	public Sprite(int type, int state, int[][] texture, int x, int y, int z) {
		this.type = type;
		this.state = state;
		this.texture = texture;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	

	
}
