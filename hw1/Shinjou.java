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
		hitbox = new AABBCamera(x + 25, y, width - 50, height);
		animationIdle = idle;
		animationMoveLeft = moveLeft;
		animationMoveRight = moveRight;
		animationAttackRight = attackRight;
		animationAttackLeft = attackLeft;
		health = 20;
		fireQuiverOn = false;
	}

	public int hitDetection(ArrayList<Enemy> enemies, int tex) {
		int tempScore = 0;
		// Arrow Collision with Enemies
		ArrayList<Enemy> deadEnemies = new ArrayList<>();
		for (int i = 0; i < this.getProjectiles().size(); i++) {
			ArrayList<Projectile> mainProj = (ArrayList<Projectile>) this.getProjectiles();
			AABBCamera projBox = mainProj.get(i).getCollisionBox();
			for (Enemy s : enemies) {
				if (DemoGame.AABBIntersect(projBox, s.getCollisionBox()) && !(i > mainProj.size() - 1)) {
					mainProj.get(i).setVisible(false);
					mainProj.remove(i);
					s.decHealth();
					if (s instanceof Slime)
						s.setCurrentTexture(tex);

					if (s.getHealth() < 1) {
						// enemies.get(j).setAlive(false);
						s.setAlive(false);
						deadEnemies.add(s);
						tempScore += s.getPoints();
						System.out.println(tempScore);
						if(s instanceof FireBoar)
						{
							double randomNum = Math.random();
							if (randomNum < .3) {
								DemoGame.glDrawSprite(DemoGame.gl, DemoGame.fireQuiver, s.x, s.y, DemoGame.projectileQuiver[0], DemoGame.projectileQuiver[1]);
							}
						}
					}
				}
			}
		}
		for (Enemy c : deadEnemies) {
			if (!c.isAlive()) {
				enemies.remove(c);
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
