package hw1;

public class Animation {
	AnimationFrame[] animations;
	int currentFrame;
	float timeLeft;
	boolean finished;
	
	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public Animation(AnimationFrame[] a) {
		animations = a;
		currentFrame = 0;
		timeLeft = animations[0].imageActiveTime;
		finished = false;
	}
	
	public void updateSprite(float deltaTime) {
		timeLeft -= deltaTime;
		if (animations.length > 0) {
			
			while (timeLeft <= 0) {
				currentFrame++;
				if (currentFrame > animations.length - 1) {
					finished = true;
					currentFrame = 0;
				}
				timeLeft += animations[currentFrame].imageActiveTime;
			}
		}
	}
	
	
	public int getCurrentFrame() {
		return animations[currentFrame].getImage();
		
	}
	
	
}
