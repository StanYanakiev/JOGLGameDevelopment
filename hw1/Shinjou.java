package hw1;

public class Shinjou extends Character{
	Animation animationIdle;
	Animation animationMoveLeft;
	Animation animationMoveRight;
	Animation animationAttackLeft;
	Animation animationAttackRight;

	public Shinjou(float x, float y, int width, int height, int tex,  Animation idle, Animation moveLeft, Animation moveRight, Animation attackLeft, Animation attackRight) {
		super(x, y, width, height, tex);
		animationIdle = idle;
		animationMoveLeft = moveLeft;
		animationMoveRight = moveRight;
		animationAttackRight = attackRight;
		animationAttackLeft = attackLeft;
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

}
