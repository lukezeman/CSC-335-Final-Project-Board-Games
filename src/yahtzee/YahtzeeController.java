package yahtzee;

public class YahtzeeController {
	private YahtzeeModel model;
	
	public YahtzeeController(YahtzeeModel model) {
		this.model = model;
	}
	
	protected int[] rollDice() {
		return model.rollDice();
	}
	
	protected void recordScore(YahtzeeCategory category, int[] dice) {
		model.recordScore(category, dice);
	}
	
	protected boolean recordBonusYahtzee(int[] dice) {
		return model.recordBonusYahtzee(dice);
	}
	
	protected boolean isGameOver() {
		return model.isGameOver();
	}
	
	protected YahtzeePlayer getCurrentPlayer() {
		return model.getCurrentPlayer();
	}
	
	protected int getRollsRemaining() {
		return model.getRollsRemaining();
	}
	
	protected YahtzeePlayer getWinner() {
		return model.getWinner();
	}
	
	protected YahtzeeScorecard getScorecard() {
		return model.getScorecard();
	}
	
	protected YahtzeePlayer getPlayer1() {
		return model.getPlayer1();
	}
	
	protected YahtzeePlayer getPlayer2() {
		return model.getPlayer2();
	}
	
	protected void saveGame() {
		model.saveGame();
	}
}
