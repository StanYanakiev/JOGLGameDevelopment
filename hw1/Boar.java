package hw1;

import java.util.Random;

public class Boar extends Character {
	public int health;

	Animation animateLeft;
	Animation animateRight;
	Animation currAnimation;
	double chase, runAway, random;
	int mood; // 0 = chase, 1 = runaway, 2 = surround;
	float deltaX, deltaY;
	boolean currDir; // left = false, right = true;
	boolean collision;
	boolean onX = false;
	float[] target = { -1, -1 };

	public Boar(float x, float y, int width, int height, int tex, Animation texLeft, Animation texRight) {
		super(x, y, width, height, tex);
		currAnimation = texLeft;
		animateLeft = texLeft;
		animateRight = texRight;
		mood = (int) Math.random() * 3;
		collision = false;
		health = 3;
		
		chase = 0.95; // 15%
		runAway = 0.70; // 70%
		random = 1; // 5%
	
	}

	public void makeDecision() {
		double random = Math.random();
		if (random < runAway)
			mood = 1;
		else if (random < chase)
			mood = 0;
		else
			mood = 2;

	}

	public void chase(float v, float[] spritePos) {
		
		if(x - spritePos[0] < 2 && x - spritePos[0] > -2)
		{	x = spritePos[0];
			onX = true;
			currAnimation = animateRight;
		}
		else if (x < spritePos[0]) {
			deltaX = v;
			currDir = true;
			onX = false;
			if (!onX)
				currAnimation = animateRight;
		} else {
			deltaX = -v;
			currDir = false;
			if (!onX)
				currAnimation = animateLeft;
			onX = false;
		}

		if (y < spritePos[1])
			deltaY = v;
		else
			deltaY = -v;
	}

	public void runAway(float v, float[] spritePos) {

		if (x <= spritePos[0]) {
			deltaX = -v;
			currDir = false;
			currAnimation = animateLeft;
		} else {
			deltaX = v;
			currDir = true;
			currAnimation = animateRight;
		}

		if (y < spritePos[1])
			deltaY = -v;
		else
			deltaY = +v;
	}

	public void random(float v) {
	
		if (x <= target[0]) {
			deltaX = v;
			currDir = true;
			currAnimation = animateRight;
		} else {
			deltaX = -v;
			currDir = false;
			currAnimation = animateLeft;
		}

		if (y < target[1])
			deltaY = v;
		else
			deltaY = -v;
	}

	public boolean closeEnough(float[] spritePos) {
		if ((x - spritePos[0] < 10 && x - spritePos[0] > -10) && (y - spritePos[1] < 10 && y - spritePos[1] > -10))
			return true;
		return false;
	}

	public void horizontalWallCollision(float v, Background bg) {

		// left collision
		if (bg.getTile((float) Math.floor((x) / 64), (float) Math.floor(y / 64)).getCollision()
				|| bg.getTile((float) Math.floor((x) / 64), (float) Math.floor((y + height / 2) / 64)).getCollision()
				|| bg.getTile((float) Math.floor((x) / 64), (float) Math.floor((y + height) / 64)).getCollision()) {
			x += v;
			collision = true;

		}

		// right collision
		else if (bg.getTile((float) Math.floor((x + width) / 64), (float) Math.floor(y / 64)).getCollision()
				|| bg.getTile((float) Math.floor((x + width) / 64), (float) Math.floor((y + height / 2) / 64))
						.getCollision()
				|| bg.getTile((float) Math.floor((x + width) / 64), (float) Math.floor((y + height) / 64))
						.getCollision()) {
			//System.out.println("left collision)");
			x -= v;
			collision = true;
		}

	}

	public void verticalWallCollision(float v, Background bg) {
		// top collision
		if (bg.getTile((float) Math.floor(x / 64), (float) Math.floor(y / 64)).getCollision()
				|| bg.getTile((float) Math.floor((x + width / 2) / 64), (float) Math.floor(y / 64)).getCollision()
				|| bg.getTile((float) Math.floor((x + width) / 64), (float) Math.floor(y / 64)).getCollision()) {
			y += v;
			collision = true;
		}

		// down collision
		else if (bg.getTile((float) Math.floor(x / 64), (float) Math.floor((y + height) / 64)).getCollision()
				|| bg.getTile((float) Math.floor((x + width / 2) / 64), (float) Math.floor((y + height) / 64))
						.getCollision()
				|| bg.getTile((float) Math.floor((x + width) / 64), (float) Math.floor((y + height) / 64))
						.getCollision()) {
			y -= v;
			collision = true;
		}

	}

	public float setVelocity(float dt) {

		return 100 * ((float) dt / 1000);
	}

	public void setTargetToSprite(float[] spritePos) {
		target[0] = spritePos[0];
		target[1] = spritePos[1];
	}

	public void update(float dt, float[] spritePos, Background bg) {
		float v = setVelocity(dt);
		int prevMood = mood;
		if (target[0] == -1 && target[1] == -1) {
			setTargetToSprite(spritePos);
		}

		if (closeEnough(target) || collision) {
			makeDecision();
			//System.out.println("New Decision Made");
		}
		collision = false;
		if (mood == 0) {
			setTargetToSprite(spritePos);
			chase(v, target);
		} else if (mood == 1) {
			setTargetToSprite(spritePos);
			runAway(v, target);
		} else if (prevMood == 2 && mood == 2) {
			random(v);
		} else {
			Random rand = new Random();
			float xPoint = rand.nextInt(1920);
			float yPoint = rand.nextInt(1920);
			target[0] = xPoint;
			target[1] = yPoint;
			random(v);
			
			
		}

		currAnimation.updateSprite(dt);
		// Actually move!
		x += deltaX;
		horizontalWallCollision(v, bg);
		y += deltaY;
		verticalWallCollision(v, bg);
	}

	public Animation getCurrAnimation() {
		return currAnimation;
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
