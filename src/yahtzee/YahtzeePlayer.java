package yahtzee;

public class YahtzeePlayer {
	private String name;
	private YahtzeeScorecard scorecard;
	private YahtzeeDie[] dice;
	
	public YahtzeePlayer(String name) {
		this.name = name;
		scorecard = new YahtzeeScorecard();
		dice = new YahtzeeDie[5];
		for (int i = 0; i < 5; i++) {
			dice[i] = new YahtzeeDie();
		}
	}
	
	// For loaded game, will load at start of turn, will not save if middle of turn
//	public YahtzeePlayer(String name, YahtzeeScorecard scorecard) {
//	        this.name = name;
//	        this.scorecard = scorecard;
//	        this.dice = new YahtzeeDie[5];
//	        for (int i = 0; i < 5; i++) {
//	            dice[i] = new YahtzeeDie();
//	        }
//	}
	
	
	protected void rollAll() {
		for (YahtzeeDie die : dice) {
			die.roll();
		}
	}
	
	protected int[] getDieValues() {
		int[] values = new int[5];
		for (int i = 0; i < 5; i++) {
			values[i] = dice[i].getValue();
		}
		
		return values;
	}
	
	protected String getName() {
		return name;
	}
	
	protected YahtzeeScorecard getScorecard() {
		return scorecard;
	}
	
	protected YahtzeeDie[] getDice() {
		return dice;
	}
	
}
