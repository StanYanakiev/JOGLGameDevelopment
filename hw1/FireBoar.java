package hw1;

public class FireBoar extends Boar
{
	
	
	public FireBoar(float x, float y, int width, int height, int tex, Animation texLeft, Animation texRight) {
		super(x, y, width, height, tex, texLeft, texRight);
		health = 2;
		chase = 0.80; // 80%
		runAway = 1; // 5%
		random = .95; // 15%
		
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
		return 150 * ((float) dt / 1000);
	}
	
}