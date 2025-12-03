package yahtzee;

import java.util.Observable;

@SuppressWarnings("deprecation")
public class YahtzeeModel extends Observable {
	private YahtzeePlayer player1;
	private YahtzeePlayer player2;
	private YahtzeePlayer currentPlayer;
	private int rollsRemaining;
	
	public YahtzeeModel(String player1, String player2) {
		this.player1 = new YahtzeePlayer(player1);
		this.player2 = new YahtzeePlayer(player2);
		currentPlayer = this.player1;
		rollsRemaining = 3;
	}
	
	// Loaded game
	public YahtzeeModel(YahtzeeInstance instance) {
		this.player1 = instance.getPlayer1();
		this.player2 = instance.getPlayer2();
		this.currentPlayer = instance.getCurrentPlayer();
		// Loads game at "checkpoint," saves at beginning of player's turn
		this.rollsRemaining = 3;
		
		for (YahtzeeDie die : currentPlayer.getDice()) {
			if (die.isHeld()) {
				die.toggleHold();
			}
		}
	}
	
	protected int[] rollDice() {
		if (rollsRemaining > 0) {
			currentPlayer.rollAll();
			rollsRemaining--;
		} else {
			return null;
		}
		
		return currentPlayer.getDieValues();
	}
	
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
	
	private void nextTurn() {
		rollsRemaining = 3;
		if (currentPlayer == player1 && !player2.getScorecard().isComplete()) {
			currentPlayer = player2;
		} else if (currentPlayer == player2 && !player1.getScorecard().isComplete()) {
			currentPlayer = player1;
		}
	}
	
	protected boolean isGameOver() {
		return player1.getScorecard().isComplete() && player2.getScorecard().isComplete();
	}
	
	
	protected YahtzeePlayer getCurrentPlayer() {
		return currentPlayer;
	}
	
	protected int getRollsRemaining() {
		return rollsRemaining;
	}
	
	protected YahtzeePlayer getWinner() {
		if (player1.getScorecard().getGrandTotal() > player2.getScorecard().getGrandTotal()) {
			return player1;
		}
		
		if (player2.getScorecard().getGrandTotal() > player1.getScorecard().getGrandTotal()) {
			return player2;
		}
		
		return null;
	}
	
	protected YahtzeeScorecard getScorecard() {
		return currentPlayer.getScorecard();
	}
	
	protected YahtzeePlayer getPlayer1() {
		return player1;
	}
	
	protected YahtzeePlayer getPlayer2() {
		return player2;
	}
	
	protected boolean isCategoryAvailable(YahtzeeCategory category) {
		return currentPlayer.getScorecard().isCategoryAvailable(category);
	}
	
	protected void saveGame() {
		YahtzeeInstance instance = new YahtzeeInstance(this);
		instance.saveGame();
	}
	
}
