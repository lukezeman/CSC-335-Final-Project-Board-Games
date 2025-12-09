package yahtzee;

public class YahtzeeController {
	private YahtzeeModel model;
	
	public YahtzeeController(YahtzeeModel model) {
		this.model = model;
	}
	
	public int[] rollDice() {
		return model.rollDice();
	}
	
	public void recordScore(YahtzeeCategory category, int[] dice) {
		model.recordScore(category, dice);
	}
	
	public boolean recordBonusYahtzee(int[] dice) {
		return model.recordBonusYahtzee(dice);
	}
	
	public boolean isGameOver() {
		return model.isGameOver();
	}
	
	public YahtzeePlayer getCurrentPlayer() {
		return model.getCurrentPlayer();
	}
	
	public int getRollsRemaining() {
		return model.getRollsRemaining();
	}
	
	public YahtzeePlayer getWinner() {
		return model.getWinner();
	}
	
	public YahtzeeScorecard getScorecard() {
		return model.getScorecard();
	}
	
	public YahtzeePlayer getPlayer1() {
		return model.getPlayer1();
	}
	
	public YahtzeePlayer getPlayer2() {
		return model.getPlayer2();
	}
	
	public boolean isCategoryAvailable(YahtzeeCategory category) {
		return model.isCategoryAvailable(category);
	}
	
	public void saveGame() {
		model.saveGame();
	}
}
