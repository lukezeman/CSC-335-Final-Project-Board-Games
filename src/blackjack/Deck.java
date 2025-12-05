package blackjack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
/**
 * This class represents a deck of cards
 * 
 * @author Aidin Miller
 */
public class Deck implements Serializable {
	private static final long serialVersionUID = 1L;
	private String[] suits = {"Clubs", "Diamonds", "Spades", "Hearts"};
	private String[] ranks = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
	private ArrayList<Card> cards;
	private ArrayList<Card> cardsInPlay;
	/**
	 * Constructor for the class
	 */
	public Deck() {
		cards = new ArrayList<Card>();
		for (int i = 0; i < suits.length; i++) {
			for (int j = 0; j < ranks.length; j++) {
				Card tempCard = new Card(suits[i], ranks[j], valOfRank(ranks[j]));
				cards.add(tempCard);
			}
		}
		cardsInPlay = new ArrayList<Card>();
		shuffleDeck();
	}
	/**
	 * This function gets the ArrayList of cards
	 * 
	 * @return an ArrayList of cards
	 */
	public ArrayList<Card> getCards() {
		return cards;
	}
	/**
	 * This function removes a card from the deck
	 * 
	 * @param c The card that will be removed
	 */
	public void removeCard(Card c) {
		cardsInPlay.add(c);
		cards.remove(c);
	}
	/**
	 * This function resets the deck
	 */
	public void resetDeck() {
		cards.addAll(cardsInPlay);
		cardsInPlay.clear();
		shuffleDeck();
	}
	/**
	 * This function shuffles the cards
	 */
	private void shuffleDeck() {
		Collections.shuffle(cards);
	}
	/**
	 * This function represents the value based on the rank given
	 * 
	 * @param s The string representing the rank
	 * @return The value based on the given rank
	 */
	private int valOfRank(String s) {
		if (s.equals("Ace")) {
			return 11;
		}
		else if (s.equals("Two")) {
			return 2;
		}
		else if (s.equals("Three")) {
			return 3;
		}
		else if (s.equals("Four")) {
			return 4;
		}
		else if (s.equals("Five")) {
			return 5;
		}
		else if (s.equals("Six")) {
			return 6;
		}
		else if (s.equals("Seven")) {
			return 7;
		}
		else if (s.equals("Eight")) {
			return 8;
		}
		else if (s.equals("Nine")) {
			return 9;
		}
		return 10;
	}
}
