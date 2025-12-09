/**
 * @author Luke Zeman
 * Assignment: CSC 335 Team Project
 * File: YahtzeeModel.java
 * Date: 12/08/2025
 * 
 * Description: This class represents the model in the MVC design for the Yahtzee game.
 * 			 	It manages the game state, including players, turns, dice rolls, and scoring.
 */

package yahtzee;

import java.util.Observable;

/**
 * This class manages the game state for the Yahtzee game, serving as the model in the MVC design.
 */
@SuppressWarnings("deprecation")
public class YahtzeeModel extends Observable {
	private YahtzeePlayer player1;
	private YahtzeePlayer player2;
	private YahtzeePlayer currentPlayer;
	private int rollsRemaining;
	
	/**
	 * Starts a new Yahtzee game with the specified player names.
	 * 
	 * @param player1 - The name of player 1.
	 * @param player2 - The name of player 2.
	 */
	public YahtzeeModel(String player1, String player2) {
		this.player1 = new YahtzeePlayer(player1);
		this.player2 = new YahtzeePlayer(player2);
		currentPlayer = this.player1;
		rollsRemaining = 3;
	}
	
	/**
	 * Loads a saved Yahtzee game from the given YahtzeeInstance.
	 * 
	 * @param instance - The YahtzeeInstance representing the saved game state.
	 */
	public YahtzeeModel(YahtzeeInstance instance) {
		this.player1 = instance.getPlayer1();
		this.player2 = instance.getPlayer2();
		this.currentPlayer = instance.getCurrentPlayer();
		// Loads game at "checkpoint," saves at beginning of player's turn.
		this.rollsRemaining = 3;
		
		for (YahtzeeDie die : currentPlayer.getDice()) {
			if (die.isHeld()) {
				die.toggleHold();
			}
		}
	}
	
	/**
	 * Rolls the dice for the current player if rolls remain.
	 * 
	 * @return An array of integers representing the rolled dice values, or null if no rolls remain.
	 */
	protected int[] rollDice() {
		if (rollsRemaining > 0) {
			currentPlayer.rollAll();
			rollsRemaining--;
		} else {
			return null;
		}
		
		return currentPlayer.getDieValues();
	}
	
	/**
	 * Records the score for the specified category based on the rolled dice.
	 * 
	 * @param category - The YahtzeeCategory to record the score for.
	 * @param dice - An array of integers representing the rolled dice values.
	 */
	protected void recordScore(YahtzeeCategory category, int[] dice) {
		int score = category.calculateScore(dice);
		currentPlayer.getScorecard().recordScore(category, dice);
		boolean checkUpper = false;
		
		if (currentPlayer.getScorecard().isUpperSectionFull() && !currentPlayer.getScorecard().upperBonusChecked()) {
			currentPlayer.getScorecard().checkEligibilityUpperBonus();
			checkUpper = true;
		}
	
		YahtzeeDie[] diceObjs = currentPlayer.getDice();
		for (YahtzeeDie die : diceObjs) {
			if (die.isHeld()) {
				die.toggleHold();
			}
		}
		
		YahtzeePlayer scoringPlayer = currentPlayer;
		
		nextTurn();
		
		setChanged();
		notifyObservers(new ScoreUpdate(category, score, checkUpper, scoringPlayer));
	}
	
	/**
	 * Records a bonus Yahtzee if applicable based on the rolled dice.
	 * 
	 * @param dice - An array of integers representing the rolled dice values.
	 * @return A boolean indicating whether a bonus Yahtzee was recorded.
	 */
	protected boolean recordBonusYahtzee(int[] dice) {
		if (currentPlayer.getScorecard().recordBonusYahtzee(dice)) {
			YahtzeeDie[] diceObjs = currentPlayer.getDice();
			for (YahtzeeDie die : diceObjs) {
				if (die.isHeld()) {
					die.toggleHold();
				}
			}
			
			YahtzeePlayer scoringPlayer = currentPlayer;
			nextTurn();
			
			setChanged();
			notifyObservers(new ScoreUpdate(null, 100, false, scoringPlayer));
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Advances the turn to the next player and resets rolls remaining.
	 */
	private void nextTurn() {
		rollsRemaining = 3;
		// In case of a Yahtzee Bonus, skip player with complete scorecards.
		if (currentPlayer == player1 && !player2.getScorecard().isComplete()) {
			currentPlayer = player2;
		} else if (currentPlayer == player2 && !player1.getScorecard().isComplete()) {
			currentPlayer = player1;
		}
	}
	
	/**
	 * Checks if the game is over (both players' scorecards are complete).
	 * 
	 * @return A boolean indicating whether the game is over.
	 */
	protected boolean isGameOver() {
		return player1.getScorecard().isComplete() && player2.getScorecard().isComplete();
	}
	
	/**
	 * Retrieves the current player.
	 * 
	 * @return The YahtzeePlayer who is the current player.
	 */
	protected YahtzeePlayer getCurrentPlayer() {
		return currentPlayer;
	}
	
	/**
	 * Retrieves the number of rolls remaining for the current turn.
	 * 
	 * @return An integer representing the number of rolls remaining.
	 */
	protected int getRollsRemaining() {
		return rollsRemaining;
	}
	
	/**
	 * Determines the winner of the game based on grand total scores.
	 * 
	 * @return The YahtzeePlayer who is the winner, or null in case of a tie.
	 */
	protected YahtzeePlayer getWinner() {
		if (player1.getScorecard().getGrandTotal() > player2.getScorecard().getGrandTotal()) {
			return player1;
		}
		
		if (player2.getScorecard().getGrandTotal() > player1.getScorecard().getGrandTotal()) {
			return player2;
		}
		
		return null;
	}
	
	/**
	 * Retrieves the scorecard of the current player.
	 * 
	 * @return The YahtzeeScorecard representing the current player's scorecard.
	 */
	protected YahtzeeScorecard getScorecard() {
		return currentPlayer.getScorecard();
	}
	
	/**
	 * Retrieves player 1.
	 * 
	 * @return The YahtzeePlayer who is player 1.
	 */
	protected YahtzeePlayer getPlayer1() {
		return player1;
	}
	
	/**
	 * Retrieves player 2.
	 * 
	 * @return The YahtzeePlayer who is player 2.
	 */
	protected YahtzeePlayer getPlayer2() {
		return player2;
	}
	
	/**
	 * Checks if the specified category is available for scoring for the current player.
	 * 
	 * @param category - The YahtzeeCategory to check availability for.
	 * @return A boolean indicating whether the category is available.
	 */
	protected boolean isCategoryAvailable(YahtzeeCategory category) {
		return currentPlayer.getScorecard().isCategoryAvailable(category);
	}
	
	/**
	 * Saves the current game state to a file.
	 */
	protected void saveGame() {
		YahtzeeInstance instance = new YahtzeeInstance(this);
		instance.saveGame();
	}
	
}
