package hw1;

public class Projectile {
	
	private float x, y;
	private int width, height;
	private AABBCamera collisionBox;
	private boolean visible, active;

	
	public Projectile(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		visible = false;
		active = true;
	
		collisionBox = new AABBCamera(x, y, width, height);
	}
	
	public void update(float velocity) {
		x += velocity;
		setX(x);
		collisionBox.setX(x);
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
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean b){
		visible = b;
	}
	
	public boolean isActive()
	{
		return active;
	}
	
	public AABBCamera getCollisionBox() {
		return collisionBox;
	}
	

}
