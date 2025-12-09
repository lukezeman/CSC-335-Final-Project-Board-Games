/**
 * @author Luke Zeman
 * Assignment: CSC 335 Team Project
 * File: YahtzeeController.java
 * Date: 12/08/2025
 * 
 * Description: This class represents the controller in the MVC design for the Yahtzee game.
 * 			 	It defines methods to interact with the model, such as rolling dice, recording scores,
 * 			 	checking game status, and retrieving player information.
 */

package yahtzee;

/**
 * This class defines methods to interact with the Yahtzee model, serving as the controller in the MVC design.
 */
public class YahtzeeController {
	private YahtzeeModel model;
	
	/**
	 * Constructs a YahtzeeController with the specified YahtzeeModel.
	 * 
	 * @param model - The YahtzeeModel to be controlled.
	 */
	public YahtzeeController(YahtzeeModel model) {
		this.model = model;
	}
	
	/**
	 * Rolls the dice and returns the resulting values.
	 * 
	 * @return An array of integers representing the rolled dice values.
	 */
	public int[] rollDice() {
		return model.rollDice();
	}
	
	/**
	 * Records the score for the specified category based on the rolled dice.
	 * 
	 * @param category - The YahtzeeCategory to record the score for.
	 * @param dice - An array of integers representing the rolled dice values.
	 */
	public void recordScore(YahtzeeCategory category, int[] dice) {
		model.recordScore(category, dice);
	}
	
	/**
	 * Records a bonus Yahtzee if applicable based on the rolled dice.
	 * 
	 * @param dice - An array of integers representing the rolled dice values.
	 * @return A boolean indicating whether a bonus Yahtzee was recorded.
	 */
	public boolean recordBonusYahtzee(int[] dice) {
		return model.recordBonusYahtzee(dice);
	}
	
	/**
	 * Checks if the game is over.
	 * 
	 * @return A boolean indicating whether the game is over.
	 */
	public boolean isGameOver() {
		return model.isGameOver();
	}
	
	/**
	 * Retrieves the current player.
	 * 
	 * @return The YahtzeePlayer who is the current player.
	 */
	public YahtzeePlayer getCurrentPlayer() {
		return model.getCurrentPlayer();
	}
	
	/**
	 * Retrieves the number of rolls remaining for the current turn.
	 * 
	 * @return An integer representing the number of rolls remaining.
	 */
	public int getRollsRemaining() {
		return model.getRollsRemaining();
	}
	
	/**
	 * Retrieves the winner of the game.
	 * 
	 * @return The YahtzeePlayer who is the winner.
	 */
	public YahtzeePlayer getWinner() {
		return model.getWinner();
	}
	
	/**
	 * Retrieves the scorecard of the game.
	 * 
	 * @return The YahtzeeScorecard representing the game's scorecard.
	 */
	public YahtzeeScorecard getScorecard() {
		return model.getScorecard();
	}
	
	/**
	 * Retrieves player 1.
	 * 
	 * @return The YahtzeePlayer who is player 1.
	 */
	public YahtzeePlayer getPlayer1() {
		return model.getPlayer1();
	}
	
	/**
	 * Retrieves player 2.
	 * 
	 * @return The YahtzeePlayer who is player 2.
	 */
	public YahtzeePlayer getPlayer2() {
		return model.getPlayer2();
	}
	
	/**
	 * Checks if the specified category is available for scoring.
	 * 
	 * @param category - The YahtzeeCategory to check.
	 * @return A boolean indicating whether the category is available.
	 */
	public boolean isCategoryAvailable(YahtzeeCategory category) {
		return model.isCategoryAvailable(category);
	}
	
	/**
	 * Saves the current game state.
	 */
	public void saveGame() {
		model.saveGame();
	}
}
