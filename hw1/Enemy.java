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
	float deltaX, deltaY;
	boolean currDir; // left = false, right = true;
	boolean collision, busy;

	float[] target = { -1, -1 };

	public Enemy(float x, float y, int width, int height, int tex, Animation texLeft, Animation texRight) {
		super(x, y, width, height, tex);
		currAnimation = texLeft;
		animateLeft = texLeft;
		animateRight = texRight;
		mood = (int) Math.random() * 3;
		collision = false;
		busy = false;
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

			float v = setVelocity(dt);
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
			shootDecision(spritePos);
		}

	}

	public void shootDecision(float[] spritePos) {
		// if the difference in X is 500 and difference in Y is 200
		if (isBusy()) {
			if (!currDir) {
				shootLeft.updateSprite(DemoGame.deltaTimeMS);
				setCurrentTexture(shootLeft.getCurrentFrame());
			} else {
				shootRight.updateSprite(DemoGame.deltaTimeMS);
				setCurrentTexture(shootRight.getCurrentFrame());
			}
			if (shootRight.finished || shootLeft.finished || count > 5) {
				setBusy(false);
				shootRight.setFinished(false);
				shootLeft.setFinished(false);
				if (count > 5) {
					count = 0;
				}
			}

		}
		// decide to shoot or not
		else {
			double randomNum = Math.random();
			System.out.println("Random for shooting: " + randomNum);
			if (randomNum < .30) {
				count++;
				// shoot
				// stop moving
				// deltaX = 0;
				// deltaY = 0;
				setBusy(true);
				if (x < spritePos[0]) {
					currAnimation = shootRight;
					currDir = false;
				} else {
					currAnimation = shootLeft;
					currDir = true;
				}

			}
		}
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

	public void decHealth() {
		health -= 1;
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}

}
