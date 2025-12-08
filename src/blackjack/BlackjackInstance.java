package blackjack;

import java.io.Serializable;
/**
 * This is a class used for saving and loading, and it is an instance of a Blackjack game
 */
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
	private String p1Name;
	private String p2Name;
	/**
	 * Constructor for the class
	 * 
	 * @param d deck object
	 * @param p1 player 1 hand
	 * @param p2 player 2 hand
	 * @param dealer dealer hand
	 * @param p1money player 1 money
	 * @param p2money player 2 money
	 * @param p1bet player 1 bet
	 * @param p2bet player 2 bet
	 * @param isGameOver boolean representing whether game is over or not
	 * @param turn int representing the current turn
	 * @param p1Done boolean representing whether player 1 is done
	 * @param p2Done boolean representing whether player 2 is done
	 * @param p1Name player 1 name
	 * @param p2Name player 2 name
	 */
	public BlackjackInstance(Deck d, Hand p1, Hand p2, Hand dealer, int p1money, int p2money, int p1bet, int p2bet,
			boolean isGameOver, int turn, boolean p1Done, boolean p2Done, String p1Name, String p2Name) {
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
		this.p1Name = p1Name;
		this.p2Name = p2Name;
	}

	/**
	 * Gets the deck
	 * 
	 * @return deck object
	 */
	public Deck getD() {
		// TODO Auto-generated method stub
		return d;
	}
	/**
	 * Gets player 1 hand
	 * 
	 * @return player 1's hand
	 */
	public Hand getP1() {
		// TODO Auto-generated method stub
		return p1;
	}
	/**
	 * Gets player 2 hand
	 * 
	 * @return player 2's hand
	 */
	public Hand getP2() {
		// TODO Auto-generated method stub
		return p2;
	}
	/**
	 * Gets dealer hand
	 * 
	 * @return dealer hand
	 */
	public Hand getDealer() {
		// TODO Auto-generated method stub
		return dealer;
	}
	/**
	 * Gets player 1 money
	 * 
	 * @return player 1 money
	 */
	public int getP1Money() {
		// TODO Auto-generated method stub
		return p1money;
	}
	/**
	 * Gets player 2 money
	 * 
	 * @return player 2 money
	 */
	public int getP2Money() {
		// TODO Auto-generated method stub
		return p2money;
	}
	/**
	 * Gets player 1 bet
	 * 
	 * @return player 1 bet
	 */
	public int getP1Bet() {
		// TODO Auto-generated method stub
		return p1bet;
	}
	/**
	 * Gets player 2 bet
	 * 
	 * @return player 2 bet
	 */
	public int getP2Bet() {
		// TODO Auto-generated method stub
		return p2bet;
	}
	/**
	 * Gets status of game
	 * 
	 * @return true if game is over, false otherwise
	 */
	public boolean isGameOver() {
		// TODO Auto-generated method stub
		return isGameOver;
	}
	/**
	 * Gets current turn
	 * 
	 * @return 1 if player 1 turn, 2 if player 2 turn, 0 otherwise
	 */
	public int getTurn() {
		// TODO Auto-generated method stub
		return turn;
	}
	/**
	 * Gets status of player 1
	 * 
	 * @return true if player 1 is finished with their turn, false otherwise
	 */
	public boolean getP1Done() {
		// TODO Auto-generated method stub
		return p1Done;
	}
	/**
	 * Gets status of player 2
	 * 
	 * @return true if player 2 is finished with their turn, false otherwise
	 */
	public boolean getP2Done() {
		// TODO Auto-generated method stub
		return p2Done;
	}
	/**
	 * Gets player 1 name
	 * 
	 * @return player 1 name
	 */
	public String getP1Name() {
		return p1Name;
	}
	/**
	 * Gets player 2 name
	 * 
	 * @return player 2 name
	 */
	public String getP2Name() {
		return p2Name;
	}

}
