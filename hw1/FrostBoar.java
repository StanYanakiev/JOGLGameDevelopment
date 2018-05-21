package hw1;

public class FrostBoar extends Enemy
{
	public FrostBoar(float x, float y, int width, int height, int tex, Animation texLeft, Animation texRight) {
		super(x, y, width, height, tex, texLeft, texRight);
		health = 3;
		chase = 0.60; // 60%
		random = 0.95; // 35%
		runAway = 1; // 5%
		damage = 3;
		points = 5;
	}
	
	public void makeDecision() {
		double randomNum = Math.random();
		if (randomNum < chase)
			mood = 0;
		else if (randomNum < random)
			mood = 2;
		else
			mood = 1;
	}
	
	public float setVelocity(float dt)
	{
		return 130 * ((float) dt / 1000);
	}
	
}
