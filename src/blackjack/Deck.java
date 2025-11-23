package blackjack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Deck implements Serializable {
	private static final long serialVersionUID = 1L;
	private String[] suits = {"Clubs", "Diamonds", "Spades", "Hearts"};
	private String[] ranks = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
	private ArrayList<Card> cards;
	private ArrayList<Card> cardsInPlay;
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
	public ArrayList<Card> getCards() {
		return cards;
	}
	public void removeCard(Card c) {
		cardsInPlay.add(c);
		cards.remove(c);
	}
	public void resetDeck() {
		for (int i = 0; i < cardsInPlay.size(); i++) {
			cards.add(cardsInPlay.get(i));
			cardsInPlay.remove(i);
		}
		shuffleDeck();
	}
	private void shuffleDeck() {
		Collections.shuffle(cards);
	}
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
