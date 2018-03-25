package hw1;

public class Projectile {
	
	private float x, y;
	private int width, height, projDir;
	private AABBCamera collisionBox;
	private boolean visible, active;

	
	public Projectile(float x, float y, int width, int height, int dir) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.projDir = dir;
		visible = false;
		active = true;
	
		collisionBox = new AABBCamera(x, y, width, height);
	}
	
	public void update(float velocity) {
		
		//shoot up or down
		if( projDir == 0 || projDir == 2)  {
			if(projDir == 0)
				y -= velocity;
			else y+= velocity;
			setY(y);
			collisionBox.setY(y);
		}
		
		if (projDir == 1 || projDir == 3) {
			if (projDir == 1)
				x += velocity;
			else
				x -= velocity;
			setX(x);
			collisionBox.setX(x);
		}
	}
	public int getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
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
