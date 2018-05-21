package hw1;

import java.util.ArrayList;

public class Hunter extends Enemy{
	int[] arrowDir;
	public Hunter(float x, float y, int width, int height, int tex, Animation texLeft, Animation texRight , Animation shootLeft, Animation shootRight, int[] arrowDir) {
		super(x, y, width, height, tex, texLeft, texRight);
		this.shootLeft = shootLeft;
		this.shootRight = shootRight;
		this.arrowDir = arrowDir;
		hitbox = new AABBCamera(x + 25, y, width - 50, height);
		health = 10;
		chase = 0.80; // 80%
		runAway = 1; // 20%
		damage = 4;
		points = 20;
	}
	public void makeDecision() {
		double randomNum = Math.random();
		if (randomNum < chase)
			mood = 0;
		else
			mood = 1;
	}
	
	public float setVelocity(float dt)
	{
		return 100 * ((float) dt / 1000);
	}
	
	public void shootDecision(float[] spritePos) {
		// if the difference in X is 500 and difference in Y is 200
		if (isBusy()) {
			currAnimation.updateSprite(DemoGame.deltaTimeMS);
			setCurrentTexture(currAnimation.getCurrentFrame());
			if (currAnimation.finished || currAnimation.finished || count > 5) {
				setBusy(false);
				shootRight.setFinished(false);
				shootLeft.setFinished(false);
				currAnimation.setFinished(false);
				if (count > 5) {
					count = 0;
					makeDecision();
				}
			}

		}
		// decide to shoot or not
		else {
			double randomNum = Math.random();
			if (randomNum < .03) {
				count++;
				// stop moving
				deltaX = 0;
				deltaY = 0;
				// start shooting
				setBusy(true);
				// set Dir
				if (x < spritePos[0]) {
					currAnimation = shootRight;
					currDir = false;
				} else {
					currAnimation = shootLeft;
					currDir = true;
				}
				//create projectile
				Projectile proj;
				if (currDir) {
					proj = new Projectile(x + (width/ 4),
							y + (height/ 4), DemoGame.projectileSizeV[0], DemoGame.projectileSizeV[1],
							3, arrowDir);
				} else
					proj = new Projectile(x + (width/ 4),
							y + (height/ 4), DemoGame.projectileSizeV[0], DemoGame.projectileSizeV[1],
							1, arrowDir);
				addProjectile(proj);
			}
		}
	}
	
	public void hitDetection(Shinjou c)
	{
		 // Hunter Arrow's Collision with Character
					for (int i = 0; i < this.getProjectiles().size(); i++) {
						ArrayList<Projectile> mainProj = (ArrayList<Projectile>) this.getProjectiles();
						AABBCamera projBox = mainProj.get(i).getCollisionBox();
							if (DemoGame.AABBIntersect(projBox, c.getCollisionBox()) && !(i > mainProj.size() - 1) && !c.getIsHit()) {
								mainProj.get(i).setVisible(false);
								mainProj.remove(i);
								c.decHealth(this.getDamage());
								c.setHit(true);
							}
						}
	}
	
	



}
