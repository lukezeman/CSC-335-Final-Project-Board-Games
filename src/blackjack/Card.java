package blackjack;

import java.io.Serializable;
/**
 * This class represents an individual card
 * 
 * @author Aidin Miller
 * @version 1.0
 */
public class Card implements Serializable {
	private static final long serialVersionUID = 1L;
	private String suit;
	private String rank;
	private int value;
	/**
	 * Constructor for the class
	 * 
	 * @param suit A string representing the suit
	 * @param rank A string representing the rank
	 * @param value An int representing the value of the rank
	 */
	public Card(String suit, String rank, int value) {
		this.suit = suit;
		this.rank = rank;
		this.value = value;
	}
	/**
	 * This function gets the suit of the card
	 * 
	 * @return A string representing the suit
	 */
	public String getSuit() {
		return suit;
	}
	/**
	 * This function gets the rank of the card
	 * 
	 * @return A string representing the rank
	 */
	public String getRank() {
		return rank;
	}
	/**
	 * This function gets the value based on the rank
	 * 
	 * @return An integer that represents the value
	 */
	public int getVal() {
		return value;
	}
	/**
	 * This function gives a string representation of the card
	 * 
	 * @return A string representing the card name
	 */
	@Override
	public String toString() {
		return rank + " of " + suit;
	}
	/**
	 * This function represents a string for the file name of a card
	 * 
	 * @return A string representing the file name
	 */
	public String getImageFileName() {
        String rankLower = rank.toLowerCase();
        String suitLower = suit.toLowerCase();
        return rankLower + "_of_" + suitLower + ".png";
    }
}
