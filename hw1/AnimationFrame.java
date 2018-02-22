package hw1;

public class AnimationFrame {
	public int image;
	public float imageActiveTime;

	public AnimationFrame(int image, float imageActiveTime) {
		this.image = image;
		this.imageActiveTime = imageActiveTime;
	}
	
	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public float getImageActiveTime() {
		return imageActiveTime;
	}

	public void setSpriteActiveTime(float imageActiveTime) {
		this.imageActiveTime = imageActiveTime;
	}
}
