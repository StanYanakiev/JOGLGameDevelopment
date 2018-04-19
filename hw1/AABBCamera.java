package hw1;

public class AABBCamera extends Camera  {
	
	private int height, width;
	private float x, y;

	public AABBCamera(float x, float y, int height, int width) {
		super(x,y);
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

}