package hw1;

public class Tile {
	int image;
	boolean collision = true;
	AABBCamera box;
		
	public Tile(int image, boolean collision) {
		this.image = image;
		this.collision = collision;
	}
		
	public int getImage() {
		return image;
	}
	
	public void setImage(int image) {
		this.image = image;
	}
	
	public boolean getCollision() {
		return collision;
	}
	
	public void setCollision(boolean collision)
	{
		this.collision = collision;
	}
}
