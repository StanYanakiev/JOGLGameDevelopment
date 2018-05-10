package hw1;

public class WildBoar extends Enemy{
	public WildBoar(float x, float y, int width, int height, int tex, Animation texLeft, Animation texRight) {
		super(x, y, width, height, tex, texLeft, texRight);		
		health = 3;
		damage = 2;
		points = 2;
		
		chase = 0.95; // 15%
		runAway = 0.70; // 70%
		random = 1; // 5%
	}
	public void makeDecision() {
		double randomNum = Math.random();
		if (randomNum < runAway)
			mood = 1;
		else if (randomNum < chase)
			mood = 0;
		else
			mood = 2;

	}

}
