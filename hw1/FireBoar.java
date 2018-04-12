package hw1;

public class FireBoar extends Boar
{
	
	
	public FireBoar(float x, float y, int width, int height, int tex, Animation texLeft, Animation texRight) {
		super(x, y, width, height, tex, texLeft, texRight);
		health = 2;
		chase = 0.80; // 80%
		runAway = 1; // 5%
		random = .95; // 15%
	}
	
	public void makeDecision() {
		double random = Math.random();
		if (random < chase)
			mood = 0;
		else if (random < random)
			mood = 2;
		else
			mood = 1;
	}
	
	public float setVelocity(float dt)
	{
		return 150 * ((float) dt / 1000);
	}
	
}