package hw1;

public class Projectile {
	
	private float x, y;
	private int width, height, projDir, currentFrameTex;
	private AABBCamera collisionBox;
	private boolean visible, active;

	
	public Projectile(float x, float y, int width, int height, int dir, int[] tex) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.projDir = dir;
		visible = true;
		active = true;
	
		collisionBox = new AABBCamera(x, y, width, height);
		
		if (tex.length > 2) {
			if (dir == 0)
				currentFrameTex = tex[0];
			else if (dir == 1)
				currentFrameTex = tex[1];
			else if (dir == 2)
				currentFrameTex = tex[2];
			else
				currentFrameTex = tex[3];
		}
		else if(tex.length == 2)
		{
			if (dir == 3)
				currentFrameTex = tex[0];
			else if (dir == 1)
				currentFrameTex = tex[1];
		}
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
	
	public int getCurrentTexture() {
		return currentFrameTex;
	}

	public void setCurrentTexture(int tex) {
		this.currentFrameTex = tex;
	}
	
	public int getDir() {
		return projDir;
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
