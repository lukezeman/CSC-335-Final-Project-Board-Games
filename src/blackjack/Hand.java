package blackjack;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a hand for an individual player
 * or the dealer. It is simply an ArrayList of card objects.
 * 
 * @author Aidin Miller
 */

public class Hand implements Serializable {
	private static final long serialVersionUID = 1L;
	private ArrayList<Card> cards;
	/**
	 * Constructor for the class
	 */
	public Hand() {
		cards = new ArrayList<Card>();
	}
	/**
	 * This function adds a card to the hand.
	 * 
	 * @param c is the card object that will be added
	 */
	public void addCard(Card c) {
		cards.add(c);
	}
	/**
	 * This function gets the cards in the hand
	 * 
	 * @return an ArrayList of cards
	 */
	public ArrayList<Card> getCards() {
		return cards;
	}
	/**
	 * This function gets the current value of the hand
	 * based on the cards
	 * 
	 * @return an integer representing the total value of the hand
	 */
	public int getValue() {
		int value = 0;
		int aceAmount = 0;
		
		for (Card card : cards) {
			value += card.getVal();
			if (card.getRank().equals("Ace")) {
				aceAmount += 1;
			}
		}
		while (value > 21 && aceAmount > 0) {
			value -= 10;
			aceAmount -= 1;
		}
		return value;	
	}
	/**
	 * This function checks to see if the hand is a bust
	 * 
	 * @return true if the value is greater than 21, false otherwise
	 */
	public boolean isBust() {
		return getValue() > 21;
	}
	/**
	 * This function checks to see if the hand is a blackjack
	 * 
	 * @return true if the hand is a blackjack, false otherwise
	 */
	public boolean isBlackjack() {
		return cards.size() == 2 && getValue() == 21;
	}
	/**
	 * This function clears the hand
	 */
	public void clear() {
		cards.clear();
	}
	/**
	 * This function represents the amount of cards in the hand.
	 * 
	 * @return an integer representing the amount of cards in the hand
	 */
	public int cardsInHand() {
		return cards.size();
	}
	
}
