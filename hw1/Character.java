package hw1;

import java.util.ArrayList;

public class Character {
	protected float x, y;
	protected int width, height;
	protected int currentFrameTex;
	protected int points, damage, health;
	protected boolean isHit, shooting, alive, visible;

	protected AABBCamera hitbox;
	protected ArrayList<Projectile> projectiles;

	public Character(float x, float y, int width, int height, int tex) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		currentFrameTex = tex;
		isHit = false;
		shooting = false;
		alive = true;
		visible = true;
		hitbox = new AABBCamera(x, y, width, height);
		projectiles = new ArrayList<Projectile>();
	
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
		hitbox.setX(x);
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
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

	public void setHit(boolean h) {
		isHit = h;
	}

	public boolean getIsHit() {
		return isHit;
	}

	public void setShooting(boolean s) {
		shooting = s;
	}

	public boolean getShooting(){
		return shooting;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	public boolean isAlive(){
		return alive;
	}
	public void decHealth() {
		health -= 1;
	}
}