package blackjack;

import java.util.Observable;
/**
 * This class contains the game logic for a Blackjack game
 * 
 * @author Aidin Miller
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class BlackjackModel extends Observable {
	private Deck d;//deck object
	private Hand p1;//hand of player 1
	private Hand p2;//hand of player 2
	private Hand dealer;//hand of dealer
	private int p1money;
	private int p2money;
	private int p1bet;
	private int p2bet;
	private boolean isGameOver;
	private int turn;//1 is player 1's turn, 2 is player 2's turn, 0 is game over
	private boolean p1Done;
	private boolean p2Done;
	private String p1Name;
	private String p2Name;
	/**
	 * Constructor for the class
	 */
	public BlackjackModel() {
		d = new Deck();
		p1 = new Hand();
		p2 = new Hand();
		dealer = new Hand();
		
		p1money = 500;
		p2money = 500;
		p1bet = 0;
		p2bet = 0;
		
		isGameOver = false;
		turn = 1;
		p1Done = false;
		p2Done = false;
		p1Name = "Player 1";
		p2Name = "Player 2";
	}
	/**
	 * Starts a new round of Blackjack
	 * 
	 * @param bet1 Player 1's bet amount
	 * @param bet2 Player 2's bet amount
	 * @return true if the game starts, false otherwise
	 */
	public boolean start(int bet1, int bet2) {
		if (bet1 > p1money || bet1 <= 0 || bet2 > p2money || bet2 <= 0) {
			return false;
		}
		p1bet = bet1;
		p2bet = bet2;
		p1money -= bet1;
		p2money -= bet2;
		
		p1.clear();
		p2.clear();
		dealer.clear();
		
		p1.addCard(deal());
		p2.addCard(deal());
		dealer.addCard(deal());
		p1.addCard(deal());
		p2.addCard(deal());
		dealer.addCard(deal());
		
		p1Done = false;
		p2Done = false;
		
		turn = 1;
		isGameOver = false;
		
		if (p1.isBlackjack()) {
			p1Done = true;
			turn = 2;
		}
		if (p2.isBlackjack()) {
			p2Done = true;
			if (p1Done) {
				turn = 0;
				dealerTurn();
			}
		}
		notifyObserversOfChange();
		return true;
	}
	/**
	 * This function deals a card from the deck
	 * 
	 * @return the card that was dealt
	 */
	private Card deal() {
		if (d.getCards().isEmpty()) {
			d.resetDeck();
		}
		Card c = d.getCards().get(0);
		d.removeCard(c);
		return c;
	}
	/**
	 * This function deals a card to the current player
	 */
	public void hit() {
		if (isGameOver) {
			return;
		}
		if (turn == 1 && !p1Done) {
			p1.addCard(deal());
			if (p1.isBust()) {
				p1Done = true;
				turn = 2;
				if (p2Done || p2.isBlackjack()) {
					dealerTurn();
				}
			}
		}
		else if (turn == 2 && !p2Done) {
			p2.addCard(deal());
			if (p2.isBust()) {
				p2Done = true;
				turn = 0;
				dealerTurn();
			}
		}
		notifyObserversOfChange();
	}
	/**
	 * This function ends a player's turn
	 */
	public void stand() {
		if (isGameOver) {
			return;
		}
		if (turn == 1 && !p1Done) {
			p1Done = true;
			turn = 2;
			if (p2Done || p2.isBlackjack()) {
				p2Done = true;
				dealerTurn();
			}
		}
		else if (turn == 2 && !p2Done) {
			p2Done = true;
			turn = 0;
			dealerTurn();
		}
		notifyObserversOfChange();
	}
	/**
	 * This function does the dealer's turn
	 */
	private void dealerTurn() {
		// TODO Auto-generated method stub
		if (!p1.isBust() || !p2.isBust()) {
			while (dealer.getValue() < 17) {
				dealer.addCard(deal());
			}
		}
		turn = 0;
		updateMoney(1);
		updateMoney(2);
	}
	/**
	 * This function calculates the earnings for the given player
	 * 
	 * @param whichPlayer an int representing which player to calculate
	 */
	private void updateMoney(int whichPlayer) {
	    Hand handToCalc;
	    int bet;
	    
	    if (whichPlayer == 1) {
	        handToCalc = p1;
	        bet = p1bet;
	    } else {
	        handToCalc = p2;
	        bet = p2bet;
	    }
	    
	    if (handToCalc.isBust()) {
	        return;
	    }
	    
	    if (dealer.isBust() || (handToCalc.getValue() > dealer.getValue())) {
	        if (handToCalc.isBlackjack()) {
	            int winnings = bet + (int)(bet * 1.5);
	            if (whichPlayer == 1) {
	                p1money += winnings;
	            } else {
	                p2money += winnings;
	            }
	        } else {
	            if (whichPlayer == 1) {
	                p1money += bet * 2;
	            } else {
	                p2money += bet * 2;
	            }
	        }
	    } else if (handToCalc.getValue() == dealer.getValue()) {
	        // Push - return bet
	        if (whichPlayer == 1) {
	            p1money += bet;
	        } else {
	            p2money += bet;
	        }
	    }
	}
	/**
	 * Get player 1's hand
	 * 
	 * @return Player 1's hand object
	 */
	public Hand getP1() {
		return p1;
	}
	/**
	 * Get player 2's hand
	 * 
	 * @return Player 2's hand object
	 */
	public Hand getP2() {
		return p2;
	}
	/**
	 * Get dealer's hand
	 * 
	 * @return dealer's hand object
	 */
	public Hand getDealer() {
		return dealer;
	}
	/**
	 * This function gets player 1's money
	 * 
	 * @return the amount of money player 1 has
	 */
	public int getP1Money() {
		return p1money;
	}
	/**
	 * This function gets player 2's money
	 * 
	 * @return the amount of money player 2 has
	 */
	public int getP2Money() {
		return p2money;
	}
	/**
	 * This function gets the amount of player 1's bet
	 * 
	 * @return an int for player 1's bet
	 */
	public int getP1Bet() {
		return p1bet;
	}
	/**
	 * This function represents player 2's bet
	 * 
	 * @return an int for player 2's bet
	 */
	public int getP2Bet() {
		return p2bet;
	}
	/**
	 * Checks if the game is over
	 * 
	 * @return true if the game is over, false otherwise
	 */
	public boolean getIsOver() {
		return isGameOver;
	}
	/**
	 * This function sets the status of the game
	 * 
	 * @param boo true if over, false otherwise
	 */
	public void setIsOver(Boolean boo) {
		isGameOver = boo;
	}
	/**
	 * This function checks to see if player 1 finished
	 * 
	 * @return true if player 1 is done, false otherwise
	 */
	public boolean isP1Done() {
		return p1Done;
	}
	/**
	 * This function checks to see if player 2 finished
	 * 
	 * @return true if player 2 is done, false otherwise
	 */
	public boolean isP2Done() {
		return p2Done;
	}
	/**
	 * Gets the current turn
	 * 
	 * @return 1 if player 1, 2 if player 2, 0 otherwise
	 */
	public int getTurn() {
		return turn;
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
	/**
	 * Sets player 1 name
	 * 
	 * @param name player 1 name
	 */
	public void setP1Name(String name) {
		this.p1Name = name;
	}
	/**
	 * Sets player 2 name
	 * 
	 * @param name player 2 name
	 */
	public void setP2Name(String name) {
		this.p2Name = name;
	}
	/**
	 * Checks if the current player can double down
	 * 
	 * @param player 1 for Player 1, 2 for Player 2
	 * @return true if player can double down, false otherwise
	 */
	public boolean canDouble(int player) {
	    Hand h = (player == 1) ? p1 : p2;
	    int money = (player == 1) ? p1money : p2money;
	    int bet = (player == 1) ? p1bet : p2bet;
	    return h.cardsInHand() == 2 && money >= bet;
	}
	/**
	 * Performs a double down action
	 */
	public void doubleDown() {
	    if (isGameOver) {
	        return;
	    }
	    
	    if (turn == 1 && !p1Done && canDouble(1)) {
	        p1money -= p1bet;
	        p1bet *= 2;
	        p1.addCard(deal());
	        p1Done = true;
	        turn = 2;
	        
	        if (p2Done || p2.isBlackjack()) {
	            p2Done = true;
	            dealerTurn();
	        }
	        
	        notifyObserversOfChange();
	    }
	    else if (turn == 2 && !p2Done && canDouble(2)) {
	        p2money -= p2bet;
	        p2bet *= 2;
	        p2.addCard(deal());
	        p2Done = true;
	        turn = 0;
	        dealerTurn();
	        
	        notifyObserversOfChange();
	    }
	}
	/**
	 * This function checks the current status of the player
	 * 
	 * @param player 1 for player 1, 2 for player 2
	 * @return a string representing whether a player lost or won
	 */
	public String getStatus(int player) {
		Hand h;
		String playerName;
		if (player == 1) {
			h = p1;
			playerName = p1Name;
		}
		else {
			h = p2;
			playerName = p2Name;
		}
		if (h.isBlackjack()) {
			return "BLACKJACK!!!!!";
		}
		if (h.isBust()) {
			return "Bust";
		}
		if (dealer.isBust()) {
			return playerName + " wins";
		}
		
		if (player == 1 && !p1Done) {
			return playerName + "'s turn...";
		}
		else if (player == 2 && !p2Done) {
			return playerName + "'s turn...";
		}
		if (h.getValue() > dealer.getValue()) {
			return playerName + " wins";
		}
		else if (h.getValue() == dealer.getValue()) {
			return "Push";
		}
		else {
			return "Dealer wins";
		}
	}
	/**
	 * Gets an instance of the game for saving
	 * 
	 * @return a BlackjackInstance of the current game state
	 */
	public BlackjackInstance getInstance() {
		return new BlackjackInstance(d,p1,p2,dealer,p1money,p2money,p1bet,p2bet,isGameOver,turn,p1Done,p2Done, p1Name, p2Name);
	}
	/**
	 * Restores the game state
	 * 
	 * @param i the BlackjackInstance to restore from
	 */
	public void setInstance(BlackjackInstance i) {
		d = i.getD();
		p1 = i.getP1();
		p2 = i.getP2();
		dealer = i.getDealer();
		p1money = i.getP1Money();
		p2money = i.getP2Money();
		p1bet = i.getP1Bet();
		p2bet = i.getP2Bet();
		isGameOver = i.isGameOver();
		turn = i.getTurn();
		p1Done = i.getP1Done();
		p2Done = i.getP2Done();
		p1Name = i.getP1Name();
		p2Name = i.getP2Name();
		notifyObserversOfChange();
	}
	/**
	 * Resets the game to its initial state
	 */
	public void reset() {
		d = new Deck();
		p1 = new Hand();
		p2 = new Hand();
		dealer = new Hand();
		p1money = 500;
		p2money = 500;
		p1bet = 0;
		p2bet = 0;
		isGameOver = false;
		turn = 1;
		p1Done = false;
		p2Done = false;
		notifyObserversOfChange();
	}
	/**
	 * Notifies observers of changes
	 */
	private void notifyObserversOfChange() {
		// TODO Auto-generated method stub
		setChanged();
		notifyObservers();
	}
}
