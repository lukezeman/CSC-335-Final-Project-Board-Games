/**
 * @author Luke Zeman
 * Assignment: CSC 335 Team Project
 * File: YahtzeeCategory.java
 * Date: 12/08/2025
 * 
 * Description: This enum represents the different scoring categories in Yahtzee and provides
 * 			 	methods to calculate the score for each category based on the dice rolled.
 */

package yahtzee;

import java.util.Arrays;
import java.util.HashMap;

/**
 * This enum represents the different scoring categories in Yahtzee and provides
 * methods to calculate the score for each category based on the dice rolled.
 */
public enum YahtzeeCategory {
	ONES,
	TWOS,
	THREES,
	FOURS,
	FIVES,
	SIXES,
	THREE_OF_A_KIND,
	FOUR_OF_A_KIND,
	FULL_HOUSE,
	SMALL_STRAIGHT,
	LARGE_STRAIGHT,
	YAHTZEE,
	CHANCE;
	
	/**
	 * Calculates the score for the given category based on the rolled dice.
	 * 
	 * @param dice - An array of integers representing the values of the rolled dice.
	 * @return The calculated score for the category.
	 */
	public int calculateScore(int[] dice) {
		Arrays.sort(dice);
		
		HashMap<Integer, Integer> counts = new HashMap<>();
				
		for (int i : dice) {
			if (!counts.containsKey(i)) {
				counts.put(i, 1);
			} else {
				counts.put(i, counts.get(i) + 1);
			}
		}
		
		switch(this) {
			case ONES:
				return sumOfDice(dice, 1, 0, counts);
			case TWOS:
				return sumOfDice(dice, 2, 0, counts);
			case THREES:
				return sumOfDice(dice, 3, 0, counts);
			case FOURS:
				return sumOfDice(dice, 4, 0, counts);
			case FIVES:
				return sumOfDice(dice, 5, 0, counts);
			case SIXES:
				return sumOfDice(dice, 6, 0, counts);
			case THREE_OF_A_KIND:
				return sumOfDice(dice, 0, 3, counts);
			case FOUR_OF_A_KIND:
				return sumOfDice(dice, 0, 4, counts);
			case FULL_HOUSE:
				return checkFullHouse(dice, counts);
			case SMALL_STRAIGHT:
				return checkStraight(dice, 1);
			case LARGE_STRAIGHT:
				return checkStraight(dice, 2);
			case YAHTZEE:
				return checkYahtzee(dice);
			case CHANCE:
				return sumOfDice(dice, 0, 0, counts);
			default:
				return 0;
		}
	}
	
	/**
	 * Sums the values of the dice based on the specified criteria.
	 * 
	 * @param dice - An array of integers representing the values of the rolled dice.
	 * @param value - The specific die value to sum (0 to sum all).
	 * @param ofAKind - The number of identical dice required (3 or 4) or 0 for no requirement.
	 * @param counts - A HashMap containing the counts of each die value.
	 * @return The calculated sum based on the criteria.
	 */
	private int sumOfDice(int[] dice, int value, int ofAKind, HashMap<Integer, Integer> counts) {
		// If value == 0, sum them all
		switch(ofAKind) {
			case 3:
				if (!checkOfAKind(dice, 3, counts)) {
					return 0;
				}
			 break;
			case 4:
				if (!checkOfAKind(dice, 4, counts)) {
					return 0;
				}
				break;
		}
		int sum = 0;
		for (int i : dice) {
	        if (value == 0 || i == value) {
	            sum += i;
	        }
	    }
		
		return sum;
	}
	
	/**
	 * Checks if there are at least 'ofAKind' identical dice in the roll.
	 * 
	 * @param dice - An array of integers representing the values of the rolled dice.
	 * @param ofAKind - The number of identical dice required (3 or 4).
	 * @param counts - A HashMap containing the counts of each die value.
	 * @return True if the condition is met, false otherwise.
	 */
	private boolean checkOfAKind(int[] dice, int ofAKind, HashMap<Integer, Integer> counts) {
		for (int val : counts.values()) {
			if (ofAKind == 3 && val >= 3) {
				return true;
			} else if (ofAKind == 4 && val >= 4) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Checks if the roll is a full house (a pair and a three of a kind).
	 * 
	 * @param dice - An array of integers representing the values of the rolled dice.
	 * @param counts - A HashMap containing the counts of each die value.
	 * @return 25 if it's a full house, 0 otherwise.
	 */
	private int checkFullHouse(int[] dice, HashMap<Integer, Integer> counts) {
		if (counts.size() != 2) {
			return 0;
		}
		
		if (counts.containsValue(2) && counts.containsValue(3)) {
			return 25;
		} else {
			return 0;
		}
	}
	
	/**
	 * Checks for small or large straights in the roll.
	 * 
	 * @param dice - An array of integers representing the values of the rolled dice.
	 * @param straight - 1 for small straight, 2 for large straight.
	 * @return The score for the straight if found, 0 otherwise.
	 */
	private int checkStraight(int[] dice, int straight) {
		int inARow = 1;
		int maxInARow = 1;
		for (int i = 0; i < dice.length - 1; i++) {
			if (dice[i] + 1 == dice[i+1]) {
				inARow++;
				maxInARow = Math.max(maxInARow, inARow);
			} else if (dice[i] != dice[i+1]){
				inARow = 1;
			}
		}
		
		if (straight == 1 && maxInARow >= 4) {
			return 30;
		} else if (straight == 2 && maxInARow == 5) {
			return 40;
		}
		
		return 0;
	}
	
	/**
	 * Checks if all dice have the same value (Yahtzee).
	 * 
	 * @param dice - An array of integers representing the values of the rolled dice.
	 * @return 50 if it's a Yahtzee, 0 otherwise.
	 */
	private int checkYahtzee(int[] dice) {
		int first = dice[0];
		
		for (int i : dice) {
			if (i != first) {
				return 0;
			}
		}
		
		return 50;
	}
	
}
