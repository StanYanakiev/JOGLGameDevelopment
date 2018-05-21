package hw1;

public class Item {
	float x, y;
	int width, height;
	AABBCamera hitbox;
	boolean visible;
	
	public Item(float x, float y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		hitbox = new AABBCamera(x, y, width, height);
		visible = true;
	}
	public AABBCamera getCollisionBox() {
		return hitbox;
	}
	public void setCollisionBox(AABBCamera hitbox) {
		this.hitbox = hitbox;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
