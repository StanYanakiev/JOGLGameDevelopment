package hw1;

import java.util.ArrayList;

public class Character {
	private float x, y;
	private int width, height;
	private int currentFrameTex;
	private boolean alive;
	private AABBCamera hitbox;
	private ArrayList<Projectile> projectiles;

	public Character(float x, float y, int width, int height, int tex) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		currentFrameTex = tex;
		alive = true;
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
	
	public void setAlive(boolean b)
	{
		alive = b;
	}
}