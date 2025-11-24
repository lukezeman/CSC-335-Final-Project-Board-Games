package yahtzee;

import java.util.Random;

public class Die {
	private int value;
	private boolean held;
	private Random random = new Random();
	
	public Die() {
		roll();
		held = false;
	}
	
	// Possibly another for loaded instance
//	public Die() {
//		
//	}
	
	protected void roll() {
		value = random.nextInt(6) + 1;
	}
	
	protected void toggleHold() {
		held = !held;
	}
	
	protected void reset() {
		held = false;
	}
	
	protected int getValue() {
		return value;
	}
	
	protected boolean isHeld() {
		return held;
	}
}
