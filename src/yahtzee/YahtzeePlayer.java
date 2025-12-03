package yahtzee;

import java.io.Serializable;

public class YahtzeePlayer implements Serializable {
	private String name;
	private YahtzeeScorecard scorecard;
	private YahtzeeDie[] dice;
	private static final long serialVersionUID = 1L;
	
	public YahtzeePlayer(String name) {
		this.name = name;
		scorecard = new YahtzeeScorecard();
		dice = new YahtzeeDie[5];
		for (int i = 0; i < 5; i++) {
			dice[i] = new YahtzeeDie();
		}
	}
	
	protected void rollAll() {
		for (YahtzeeDie die : dice) {
			if (!die.isHeld()) {
				die.roll();
			}
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
