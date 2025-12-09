/**
 * @author Luke Zeman
 * Assignment: CSC 335 Team Project
 * File: YahtzeeScorecard.java
 * Date: 12/08/2025
 * 
 * Description: This class represents a player's scorecard in the Yahtzee game. It includes methods
 * 			 	to record scores in various categories, check for bonuses, and retrieve total scores.
 */

package yahtzee;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class represents a player's scorecard in the Yahtzee game.
 */
public class YahtzeeScorecard implements Serializable {
	private HashMap<YahtzeeCategory, Integer> scores = new HashMap<>();
	private int yahtzeeBonus = 0;
	private int upperSectionBonus = 0;
	private int upperSection = 0;
	private int lowerSection = 0;
	private boolean upperBonusChecked = false;
	private static final long serialVersionUID = 1L;
	
	/**
	 * Records a score for the specified category based on the given dice values.
	 * Updates the upper or lower section totals.
	 * 
	 * @param category - The YahtzeeCategory to record the score in.
	 * @param dice - An array of integers representing the current dice values.
	 */
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
	
	/**
	 * Records a bonus Yahtzee if the given dice values represent a Yahtzee
	 * and the Yahtzee category has already been scored with 50 points.
	 * 
	 * @param dice - An array of integers representing the current dice values.
	 * @return A boolean indicating whether a bonus Yahtzee was recorded.
	 */
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
	
	/**
	 * Checks if the specified category is available for scoring.
	 * 
	 * @param category - The YahtzeeCategory to check.
	 * @return A boolean indicating whether the category is available.
	 */
	public boolean isCategoryAvailable(YahtzeeCategory category) {
		return !scores.containsKey(category);
	}
	
	/**
	 * Checks if the upper section of the scorecard is full.
	 * 
	 * @return A boolean indicating whether the upper section is full.
	 */
	protected boolean isUpperSectionFull() {
		return scores.containsKey(YahtzeeCategory.ONES) && scores.containsKey(YahtzeeCategory.TWOS) &&
			scores.containsKey(YahtzeeCategory.THREES) && scores.containsKey(YahtzeeCategory.FOURS) &&
			scores.containsKey(YahtzeeCategory.FIVES) && scores.containsKey(YahtzeeCategory.SIXES);
	}
	
	/**
	 * Checks if the player is eligible for the upper section bonus
	 * and applies it if they are.
	 */
	protected void checkEligibilityUpperBonus() {
		if (!upperBonusChecked && upperSection >= 63) {
			upperSectionBonus = 35;
			upperSection += 35;
		}
		
		upperBonusChecked = true;
	}
	
	/**
	 * Retrieves the scores recorded in the scorecard.
	 * 
	 * @return A HashMap containing the YahtzeeCategory and corresponding scores.
	 */
	public HashMap<YahtzeeCategory, Integer> getScores() {
		return scores;
	}
	
	/**
	 * Retrieves the total Yahtzee bonus points.
	 * 
	 * @return An integer representing the Yahtzee bonus points.
	 */
	public int getYahtzeeBonus() {
		return yahtzeeBonus;
	}
	
	/**
	 * Retrieves the upper section bonus points.
	 * 
	 * @return An integer representing the upper section bonus points.
	 */
	public int getUpperSectionBonus() {
		return upperSectionBonus;
	}
	
	/**
	 * Retrieves the grand total score of the scorecard.
	 * 
	 * @return An integer representing the grand total score.
	 */
	public int getGrandTotal() {
		return upperSection + lowerSection;
	}
	
	/**
	 * Checks if the scorecard is complete.
	 * 
	 * @return A boolean indicating whether the scorecard is complete.
	 */
	protected boolean isComplete() {
		return scores.size() == 13;
	}
	
	/**
	 * Checks if the upper section bonus has been checked.
	 * 
	 * @return A boolean indicating whether the upper section bonus has been checked.
	 */
	protected boolean upperBonusChecked() {
		return upperBonusChecked;
	}
	
	/**
	 * Retrieves the total score of the upper section.
	 * 
	 * @return An integer representing the upper section score.
	 */
	public int getUpperSection() {
		return upperSection;
	}
	
	/**
	 * Retrieves the total score of the lower section.
	 * 
	 * @return An integer representing the lower section score.
	 */
	public int getLowerSection() {
		return lowerSection;
	}
	
}
