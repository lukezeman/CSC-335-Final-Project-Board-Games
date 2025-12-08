package yahtzee;

import java.io.Serializable;
import java.util.Random;

public class YahtzeeDie implements Serializable {
	private int value;
	private boolean held;
	private Random random = new Random();
	private static final long serialVersionUID = 1L;
	
	public YahtzeeDie() {
		roll();
		held = false;
	}
	
	protected void roll() {
		value = random.nextInt(6) + 1;
	}
	
	public void toggleHold() {
		held = !held;
	}
	
	public void reset() {
		held = false;
	}
	
	public int getValue() {
		return value;
	}
	
	public boolean isHeld() {
		return held;
	}
}
