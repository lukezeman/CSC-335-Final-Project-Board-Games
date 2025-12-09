/**
 * @author Luke Zeman
 * Assignment: CSC 335 Team Project
 * File: YahtzeePlayer.java
 * Date: 12/08/2025
 * 
 * Description: This class represents a player in the Yahtzee game. It includes methods
 * 			 	to roll dice, retrieve die values, and access the player's scorecard and name.
 */

package yahtzee;

import java.io.Serializable;

/**
 * This class represents a player in the Yahtzee game.
 */
public class YahtzeePlayer implements Serializable {
	private String name;
	private YahtzeeScorecard scorecard;
	private YahtzeeDie[] dice;
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a YahtzeePlayer with the specified name.
	 * Initializes the player's scorecard and dice.
	 * 
	 * @param name - The name of the player.
	 */
	public YahtzeePlayer(String name) {
		this.name = name;
		scorecard = new YahtzeeScorecard();
		dice = new YahtzeeDie[5];
		for (int i = 0; i < 5; i++) {
			dice[i] = new YahtzeeDie();
		}
	}
	
	/**
	 * Rolls all unheld dice for the player.
	 */
	public void rollAll() {
		for (YahtzeeDie die : dice) {
			if (!die.isHeld()) {
				die.roll();
			}
		}
	}
	
	/**
	 * Retrieves the current values of all dice for the player.
	 * 
	 * @return An array of integers representing the die values.
	 */
	public int[] getDieValues() {
		int[] values = new int[5];
		for (int i = 0; i < 5; i++) {
			values[i] = dice[i].getValue();
		}
		
		return values;
	}
	
	/**
	 * Retrieves the name of the player.
	 * 
	 * @return The name of the player.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Retrieves the scorecard of the player.
	 * 
	 * @return The YahtzeeScorecard of the player.
	 */
	public YahtzeeScorecard getScorecard() {
		return scorecard;
	}
	
	/**
	 * Retrieves the array of dice for the player.
	 * 
	 * @return An array of YahtzeeDie representing the player's dice.
	 */
	public YahtzeeDie[] getDice() {
		return dice;
	}
	
}
