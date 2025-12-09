/**
 * @author Luke Zeman
 * Assignment: CSC 335 Team Project
 * File: ScoreUpdate.java
 * Date: 12/08/2025
 * 
 * Description: This class represents a score update in the Yahtzee game. It contains
 * 				information about the category scored, the score achieved, whether
 * 				the upper section is full, and the player who scored. This is the object
 * 				passed to observers when a score is recorded.
 */

package yahtzee;

/**
 * This class is the object passed to observers when a score is recorded.
 * It represents a score update in the Yahtzee game.
 */
public class ScoreUpdate {
	protected YahtzeeCategory category;
	protected int score;
	protected boolean upperFull;
	protected YahtzeePlayer player;
	
	/**
	 * Constructs a ScoreUpdate object with the specified details.
	 * 
	 * @param category -The YahtzeeCategory that was scored.
	 * @param score - The score achieved in that category.
	 * @param upperFull - A boolean indicating if the upper section is now full.
	 * @param player - The YahtzeePlayer who scored.
	 */
	public ScoreUpdate(YahtzeeCategory category, int score, boolean upperFull, YahtzeePlayer player) {
		this.category = category;
		this.score = score;
		this.upperFull = upperFull;
		this.player = player;
	}
}
