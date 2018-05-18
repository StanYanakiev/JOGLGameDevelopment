package hw1;

import java.util.Random;

public class Enemy extends Character {
	Animation animateLeft;
	Animation animateRight;
	Animation currAnimation;
	Animation shootLeft;
	Animation shootRight;
	double chase, runAway, random;
	int count;
	int mood; // 0 = chase, 1 = runaway, 2 = random;
	float deltaX, deltaY, v;
	boolean currDir; // left = false, right = true;
	boolean collision, busy, droppedFireQuiver;

	float[] target = { -1, -1 };

	public Enemy(float x, float y, int width, int height, int tex, Animation texLeft, Animation texRight) {
		super(x, y, width, height, tex);
		currAnimation = texLeft;
		animateLeft = texLeft;
		animateRight = texRight;
		mood = (int) Math.random() * 3;
		collision = false;
		busy = false;
		droppedFireQuiver = false;
	}

	public Enemy(float x, float y, int width, int height, int tex) {
		super(x, y, width, height, tex);
	}

	public void makeDecision() {
		double randomNum = Math.random();
		if (randomNum < runAway)
			mood = 1;
		else if (randomNum < chase)
			mood = 0;
		else
			mood = 2;
	}

	public boolean closeEnough(float[] spritePos) {
		if ((x - spritePos[0] < 10 && x - spritePos[0] > -10) && (y - spritePos[1] < 10 && y - spritePos[1] > -10))
			return true;
		return false;
	}

	public void update(float dt, float[] spritePos, Background bg) {
		if (this.x < 0 || this.x >= 1920)
			this.setX(65);
		if (this.y < 0 || this.y >= 1920)
			this.setY(65);

		if (isBusy()) {
			shootDecision(spritePos);
		} else {

			v = setVelocity(dt);
			if (target[0] < 0 && target[1] < 0) {
				setTargetToSprite(spritePos);
			}

			if (closeEnough(target) || collision) {

				makeDecision();

			}
			collision = false;
			if (mood == 0) {
				setTargetToSprite(spritePos);
				chase(v, spritePos);
			} else if (mood == 1) {
				setTargetToSprite(spritePos);
				runAway(v, spritePos);
			} else {
				random(v, spritePos);
			}

			currAnimation.updateSprite(dt);
			// Actually move!
			setX(x + deltaX);
			horizontalWallCollision(v, bg);
			setY(y + deltaY);
			verticalWallCollision(v, bg);
		}
	}

	public void chase(float v, float[] spritePos) {
		deltaX = spritePos[0] - x;
		deltaY = spritePos[1] - y;
		float norm = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		deltaX *= (v / norm);
		deltaY *= (v / norm);

		if (x < spritePos[0])
			currAnimation = animateRight;
		else
			currAnimation = animateLeft;
		if (this instanceof Hunter) {
			if ((x - spritePos[0] > -500 || x - spritePos[0] < 500)
					&& (y - spritePos[1] > -200 || y - spritePos[1] < 200)) {
				shootDecision(spritePos);
			}
		}

	}
	public void hitDetection(Shinjou c){
	}

	public void shootDecision(float[] spritePos) {
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

	public void random(float v, float[] target1) {
		Random rand = new Random();
		float rDistance = rand.nextInt(64);
		int rLocation = rand.nextInt(4);

		if (target[0] > rDistance && target[1] > rDistance) {
			switch (rLocation) {
			case 0:
				target[0] = (target[0] - rDistance);
				target[1] = target[1] - rDistance;
				break;
			case 1:
				target[0] = (target[0] + rDistance);
				target[1] = target[1] - rDistance;
				break;
			case 2:
				target[0] = (target[0] + rDistance);
				target[1] = target[1] + rDistance;
				break;
			case 3:
				target[0] = (target[0] - rDistance);
				target[1] = target[1] + rDistance;
				break;
			}
		}
		chase(v, target);
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

			x -= v;
			collision = true;
		}

	}

	public void verticalWallCollision(float v, Background bg) {
		if (y < 0 || y > 1900)
			setY(64);
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

	public boolean hasDroppedFireQuiver() {
		return droppedFireQuiver;
	}

	public void setDroppedFireQuiver(boolean droppedFireQuiver) {
		this.droppedFireQuiver = droppedFireQuiver;
	}
	
	public float setVelocity(float dt) {

		return 100 * ((float) dt / 1000);
	}

	public void setTargetToSprite(float[] spritePos) {
		target[0] = spritePos[0];
		target[1] = spritePos[1];
	}

	public Animation getCurrAnimation() {
		return currAnimation;
	}

	public void decHealth(int damage) {
		health -= damage;
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}

}
