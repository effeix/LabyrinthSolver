public class Crumb {
	private int x;
	private int y;
	private int passes;
	
	public Crumb(int x, int y) {
		this.x = x;
		this.y = y;
		this.passes = 0;
	}
	
	public int getPasses() {
		return passes;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void incrementPasses() {
		passes++;
	}
}
