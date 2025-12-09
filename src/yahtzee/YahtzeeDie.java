/**
 * @author Luke Zeman
 * Assignment: CSC 335 Team Project
 * File: YahtzeeDie.java
 * Date: 12/08/2025
 * 
 * Description: This class represents a single die in the Yahtzee game. It includes methods
 * 			 	to roll the die, toggle its held status, reset it, and retrieve its value and held status.
 */

package yahtzee;

import java.io.Serializable;
import java.util.Random;

/**
 * This class represents a single die in the Yahtzee game.
 */
public class YahtzeeDie implements Serializable {
	private int value;
	private boolean held;
	private Random random = new Random();
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a YahtzeeDie and rolls it to initialize its value.
	 * The die is not held by default.
	 */
	public YahtzeeDie() {
		roll();
		held = false;
	}
	
	/**
	 * Rolls the die to generate a new random value between 1 and 6.
	 */
	protected void roll() {
		value = random.nextInt(6) + 1;
	}
	
	/**
	 * Toggles the held status of the die.
	 */
	public void toggleHold() {
		held = !held;
	}
	
	/**
	 * Resets the die to not held status.
	 */
	public void reset() {
		held = false;
	}
	
	/**
	 * Retrieves the current value of the die.
	 * 
	 * @return The integer value of the die (1-6).
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Checks if the die is currently held.
	 * 
	 * @return A boolean indicating whether the die is held.
	 */
	public boolean isHeld() {
		return held;
	}
}
