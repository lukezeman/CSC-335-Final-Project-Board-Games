package yahtzee;

public class ScoreUpdate {
	protected YahtzeeCategory category;
	protected int score;
	protected boolean upperFull;
	protected YahtzeePlayer player;
	
	public ScoreUpdate(YahtzeeCategory category, int score, boolean upperFull, YahtzeePlayer player) {
		this.category = category;
		this.score = score;
		this.upperFull = upperFull;
		this.player = player;
	}
}
