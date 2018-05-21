package hw1;

import java.util.ArrayList;

public class Shinjou extends Character{
	Animation animationIdle;
	Animation animationMoveLeft;
	Animation animationMoveRight;
	Animation animationAttackLeft;
	Animation animationAttackRight;
	int health;
	boolean fireQuiverOn;

	public Shinjou(float x, float y, int width, int height, int tex,  Animation idle, Animation moveLeft, Animation moveRight, Animation attackLeft, Animation attackRight) {
		super(x, y, width, height, tex);
		hitbox = new AABBCamera(x + 25, y + 50 , width - 50, height + 50);
		animationIdle = idle;
		animationMoveLeft = moveLeft;
		animationMoveRight = moveRight;
		animationAttackRight = attackRight;
		animationAttackLeft = attackLeft;
		health = 20; damage = 1;
		fireQuiverOn = false;
	}

	public int hitDetection(ArrayList<Enemy> enemies, int tex) {
		int tempScore = 0;
		// Arrow Collision with Enemies
		for (int i = 0; i < this.getProjectiles().size(); i++) {
			ArrayList<Projectile> mainProj = (ArrayList<Projectile>) this.getProjectiles();
			AABBCamera projBox = mainProj.get(i).getCollisionBox();
			for (Enemy s : enemies) {
				if (DemoGame.AABBIntersect(projBox, s.getCollisionBox()) && !(i > mainProj.size() - 1) && s.isAlive()) {
					mainProj.get(i).setVisible(false);
					mainProj.remove(i);
					
					if (s instanceof Slime) {
						s.setCurrentTexture(tex);
						s.decHealth(1);
					} else
						s.decHealth(damage);

					if (s.getHealth() < 1) {
						s.setAlive(false);
						tempScore += s.getPoints();
						if (s instanceof FireBoar) {
							double randomNum = Math.random();
							if (randomNum < .8)
								DemoGame.quiverList.add(new Item(s.getX(), s.getY(), DemoGame.projectileQuiver[0],
										DemoGame.projectileQuiver[1]));
						}
						if(s instanceof Slime)
						{
							DemoGame.healthList.add(new Item(s.getX() + 16, s.getY() + 16, DemoGame.healthSize[0],
									DemoGame.healthSize[1]));
						}
					}
				}
			}
		}
		return tempScore;
	}
	
	public boolean isFireQuiverOn() {
		return fireQuiverOn;
	}

	public void setFireQuiverOn(boolean fireQuiverOn) {
		this.fireQuiverOn = fireQuiverOn;
	}
	
	public void setDamage() {
		if(fireQuiverOn)
			damage = 2;
		else
			damage = 1;
			
	}
	
	public Animation getIdleAnimation() {
		return animationIdle;
	}
	
public void setIdleAnimation(Animation idleAnimation) {
		this.animationIdle = idleAnimation;
	}

	public Animation getLeftAnimation() {
		return animationMoveLeft;
	}

	public void setLeftAnimation(Animation leftAnimation) {
		this.animationMoveLeft = leftAnimation;
	}

	public Animation getRightAnimation() {
		return animationMoveRight;
	}

	public void setRightAnimation(Animation rightAnimation) {
		this.animationMoveRight = rightAnimation;
	}
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int h) {
		this.health = h;
	}

	public void decHealth(int hitPoints) {
		health -= hitPoints;
	}

}
