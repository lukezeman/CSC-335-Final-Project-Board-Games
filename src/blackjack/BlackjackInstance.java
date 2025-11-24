package blackjack;

import java.io.Serializable;

public class BlackjackInstance implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Deck d;
	private Hand p1;
	private Hand p2;
	private Hand dealer;
	private int p1money;
	private int p2money;
	private int p1bet;
	private int p2bet;
	private boolean isGameOver;
	private int turn;
	private boolean p1Done;
	private boolean p2Done;

	public BlackjackInstance(Deck d, Hand p1, Hand p2, Hand dealer, int p1money, int p2money, int p1bet, int p2bet,
			boolean isGameOver, int turn, boolean p1Done, boolean p2Done) {
		// TODO Auto-generated constructor stub
		this.d = d;
		this.p1 = p1;
		this.p2 = p2;
		this.dealer = dealer;
		this.p1money = p1money;
		this.p2money = p2money;
		this.p1bet = p1bet;
		this.p2bet = p2bet;
		this.isGameOver = isGameOver;
		this.turn = turn;
		this.p1Done = p1Done;
		this.p2Done = p2Done;
	}


	public Deck getD() {
		// TODO Auto-generated method stub
		return d;
	}

	public Hand getP1() {
		// TODO Auto-generated method stub
		return p1;
	}

	public Hand getP2() {
		// TODO Auto-generated method stub
		return p2;
	}

	public Hand getDealer() {
		// TODO Auto-generated method stub
		return dealer;
	}

	public int getP1Money() {
		// TODO Auto-generated method stub
		return p1money;
	}

	public int getP2Money() {
		// TODO Auto-generated method stub
		return p2money;
	}

	public int getP1Bet() {
		// TODO Auto-generated method stub
		return p1bet;
	}

	public int getP2Bet() {
		// TODO Auto-generated method stub
		return p2bet;
	}

	public boolean isGameOver() {
		// TODO Auto-generated method stub
		return isGameOver;
	}

	public int getTurn() {
		// TODO Auto-generated method stub
		return turn;
	}

	public boolean getP1Done() {
		// TODO Auto-generated method stub
		return p1Done;
	}

	public boolean getP2Done() {
		// TODO Auto-generated method stub
		return p2Done;
	}

}
