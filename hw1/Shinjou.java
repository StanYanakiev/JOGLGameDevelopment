package hw1;

public class Shinjou extends Character{
	Animation animationIdle;
	Animation animationLeft;
	Animation animationRight;

	public Shinjou(float x, float y, int width, int height, int tex,  Animation idle, Animation left, Animation right) {
		super(x, y, width, height, tex);
		animationIdle = idle;
		animationLeft = left;
		animationRight = right;
	}
	
	public Animation getIdleAnimation() {
		return animationIdle;
	}
	public void setIdleAnimation(Animation idleAnimation) {
		this.animationIdle = idleAnimation;
	}
	public Animation getLeftAnimation() {
		return animationLeft;
	}
	public void setLeftAnimation(Animation leftAnimation) {
		this.animationLeft = leftAnimation;
	}
	public Animation getRightAnimation() {
		return animationRight;
	}
	public void setRightAnimation(Animation rightAnimation) {
		this.animationRight = rightAnimation;
	}

}
