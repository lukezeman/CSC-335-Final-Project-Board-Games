package blackjack;
import java.io.Serializable;
import java.util.ArrayList;

public class Hand implements Serializable {
	private static final long serialVersionUID = 1L;
	private ArrayList<Card> cards;
	public Hand() {
		cards = new ArrayList<Card>();
	}
	public void addCard(Card c) {
		cards.add(c);
	}
	public ArrayList<Card> getCards() {
		return cards;
	}
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
	public boolean isBust() {
		return getValue() > 21;
	}
	public boolean isBlackjack() {
		return cards.size() == 2 && getValue() == 21;
	}
	public void clear() {
		cards.clear();
	}
	public int cardsInHand() {
		return cards.size();
	}
	
}
