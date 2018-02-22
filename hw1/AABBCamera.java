package hw1;

public class AABBCamera extends Camera{
	private float x, y;
	private int height, width;

	public AABBCamera(float spritePos, float spritePos2, int height, int width) {
		super(spritePos,spritePos2);
		this.x = spritePos;
		this.y = spritePos2;
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

	public void setX(int x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}

