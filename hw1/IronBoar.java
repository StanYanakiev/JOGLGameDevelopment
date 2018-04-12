package hw1;

public class IronBoar extends Boar
{

	public IronBoar(float x, float y, int width, int height, int tex, Animation texLeft, Animation texRight) {
		super(x, y, width, height, tex, texLeft, texRight);
		chase = 0.90; // 35%
		runAway = 1; // 10%d
		random = .55; // 55%
		health = 5;
	}
	public void makeDecision() {
		double random = Math.random();
		if (random < random)
			mood = 2;
		else if (random < chase)
			mood = 0;
		else
			mood = 1;

	}
	
	public float setVelocity(float dt)
	{
		return 80 * ((float) dt / 1000);
	}
}
