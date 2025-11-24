package yahtzee;

import java.util.HashMap;

public class YahtzeeScorecard {
	private HashMap<YahtzeeCategory, Integer> scores = new HashMap<>();
	private int yahtzeeBonus = 0;
	private int upperSectionBonus = 0;
	private int upperSection = 0;
	private int lowerSection = 0;
	private boolean upperBonusChecked = false;
	
//	// On loaded scorecard
//	public YahtzeeScorecard() {
//		
//	}
	
	protected void recordScore(YahtzeeCategory category, int[] dice) {
		int score = category.calculateScore(dice);
		
		if (category == YahtzeeCategory.ONES || category == YahtzeeCategory.TWOS || category == YahtzeeCategory.THREES ||
			category == YahtzeeCategory.FOURS || category == YahtzeeCategory.FIVES || category == YahtzeeCategory.SIXES) {
			
			upperSection += score;
		} else {
			lowerSection += score;
		}
		
		scores.put(category, score);
		
	}
	
	protected boolean recordBonusYahtzee(int[] dice) {
		if (YahtzeeCategory.YAHTZEE.calculateScore(dice) != 50) {
			return false;
		}
		
		if (!scores.containsKey(YahtzeeCategory.YAHTZEE) || scores.get(YahtzeeCategory.YAHTZEE) != 50) {
			return false;
		}
		
		yahtzeeBonus += 100;
		lowerSection += 100;
		
		return true;
	}
	
	protected boolean isCategoryAvailable(YahtzeeCategory category) {
		return !scores.containsKey(category);
	}
	
	protected boolean isUpperSectionFull() {
		return scores.containsKey(YahtzeeCategory.ONES) && scores.containsKey(YahtzeeCategory.TWOS) &&
			scores.containsKey(YahtzeeCategory.THREES) && scores.containsKey(YahtzeeCategory.FOURS) &&
			scores.containsKey(YahtzeeCategory.FIVES) && scores.containsKey(YahtzeeCategory.SIXES);
	}
	
	protected void checkEligibilityUpperBonus() {
		if (!upperBonusChecked && upperSection >= 63) {
			upperSectionBonus = 35;
			upperSection += 35;
		}
		
		upperBonusChecked = true;
	}
	
	protected HashMap<YahtzeeCategory, Integer> getScores() {
		return scores;
	}
	
	protected int getYahtzeeBonus() {
		return yahtzeeBonus;
	}
	
	protected int getUpperSectionBonus() {
		return upperSectionBonus;
	}
	
	protected int getGrandTotal() {
		return upperSection + lowerSection;
	}
	
	protected boolean isComplete() {
		return scores.size() == 13;
	}
	
	protected boolean upperBonusChecked() {
		return upperBonusChecked;
	}
	
}
