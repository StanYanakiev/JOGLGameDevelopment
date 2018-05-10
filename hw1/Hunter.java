package hw1;

public class Hunter extends Enemy{

	public Hunter(float x, float y, int width, int height, int tex, Animation texLeft, Animation texRight , Animation shootLeft, Animation shootRight) {
		super(x, y, width, height, tex, texLeft, texRight);
		this.shootLeft = shootLeft;
		this.shootRight = shootRight;
		
		health = 10;
		chase = 0.80; // 80%
		runAway = 1; // 20%
		damage = 5;
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
	
	



}
