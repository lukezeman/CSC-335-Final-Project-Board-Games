package yahtzee;

import java.util.HashMap;

public class YahtzeeScorecard {
	private HashMap<Category, Integer> scores = new HashMap<>();
	private int yahtzeeBonus = 0;
	private int upperSectionBonus = 0;
	private int upperSection = 0;
	private int lowerSection = 0;
	
//	// On loaded scorecard
//	public YahtzeeScorecard() {
//		
//	}
	
	protected void recordScore(Category category, int[] dice) {
		int score = category.calculateScore(dice);
		
		if (category == Category.ONES || category == Category.TWOS || category == Category.THREES ||
			category == Category.FOURS || category == Category.FIVES || category == Category.SIXES) {
			
			upperSection += score;
		} else {
			lowerSection += score;
		}
		
		scores.put(category, score);
		
	}
	
	protected boolean recordBonusYahtzee(int[] dice) {
		if (Category.YAHTZEE.calculateScore(dice) != 50) {
			return false;
		}
		
		if (!scores.containsKey(Category.YAHTZEE) || scores.get(Category.YAHTZEE) != 50) {
			return false;
		}
		
		yahtzeeBonus += 100;
		lowerSection += 100;
		
		return true;
	}
	
	protected boolean isCategoryAvailable(Category category) {
		return !scores.containsKey(category);
	}
	
	protected boolean isUpperSectionFull() {
		return scores.containsKey(Category.ONES) && scores.containsKey(Category.TWOS) &&
			scores.containsKey(Category.THREES) && scores.containsKey(Category.FOURS) &&
			scores.containsKey(Category.FIVES) && scores.containsKey(Category.SIXES);
	}
	
	protected void checkEligibilityUpperBonus() {
		if (upperSection >= 63 && upperSectionBonus == 0) {
			upperSectionBonus = 35;
			upperSection += 35;
		}
	}
	
	protected HashMap<Category, Integer> getScores() {
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
	
}
