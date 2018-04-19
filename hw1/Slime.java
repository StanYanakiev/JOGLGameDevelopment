package hw1;

public class Slime extends Character {
	private int health;

	public Slime(float x, float y, int width, int height, int tex) {
		super(x, y, width, height, tex);
		health = 2;
		
		points = 1;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int h) {
		this.health = h;
	}

	public void decHealth() {
		health -= 1;
	}
}


