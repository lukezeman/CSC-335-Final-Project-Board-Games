package blackjack;

import java.io.Serializable;

public class Card implements Serializable {
	private static final long serialVersionUID = 1L;
	private String suit;
	private String rank;
	private int value;
	public Card(String suit, String rank, int value) {
		this.suit = suit;
		this.rank = rank;
		this.value = value;
	}
	public String getSuit() {
		return suit;
	}
	public String getRank() {
		return rank;
	}
	public int getVal() {
		return value;
	}
	
	@Override
	public String toString() {
		return rank + " of " + suit;
	}
	public String getImageFileName() {
        String rankLower = rank.toLowerCase();
        String suitLower = suit.toLowerCase();
        return rankLower + "_of_" + suitLower + ".png";
    }
}
