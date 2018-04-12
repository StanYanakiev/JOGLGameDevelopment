package hw1;

import java.util.ArrayList;

public class Character {
	protected float x, y;
	protected int width, height;
	protected int currentFrameTex;
	protected boolean busy;
	protected boolean shooting;
	protected AABBCamera hitbox;
	protected ArrayList<Projectile> projectiles;

	public Character(float x, float y, int width, int height, int tex) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		currentFrameTex = tex;
		busy = false;
		shooting = false;
		hitbox = new AABBCamera(x, y, width, height);
		projectiles = new ArrayList<Projectile>();
	}

	public float getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		hitbox.setX(x);
	}

	public float getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
		hitbox.setY(y);
	}

	public int getCurrentTexture() {
		return currentFrameTex;
	}

	public void setCurrentTexture(int tex) {
		this.currentFrameTex = tex;
	}

	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	public void addProjectile(Projectile p) {
		projectiles.add(p);
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
	public AABBCamera getCollisionBox() {
		return hitbox;
	}
	
	public void setBusy(boolean b)
	{
		busy = b;
	}
	
	public boolean getBusy()
	{
		return busy;
	}
	public void setShooting(boolean s)
	{
		shooting = s;
	}
	
	public boolean getShooting()
	{
		return shooting;
	}
}