package blackjack;

import java.util.Observable;

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
	}
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
		notifyObserversOfChange();
		return true;
	}
	private Card deal() {
		if (d.getCards().isEmpty()) {
			d.resetDeck();
		}
		Card c = d.getCards().get(0);
		d.removeCard(c);
		return c;
	}
	public void hit() {
		if (isGameOver) {
			return;
		}
		if (turn == 1 && !p1Done) {
			p1.addCard(deal());
			if (p1.isBust()) {
				p1Done = true;
				turn = 2;
				if (p2Done) {
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
	
	public Hand getP1() {
		return p1;
	}
	public Hand getP2() {
		return p2;
	}
	public Hand getDealer() {
		return dealer;
	}
	public int getP1Money() {
		return p1money;
	}
	public int getP2Money() {
		return p2money;
	}
	public int getP1Bet() {
		return p1bet;
	}
	public int getP2Bet() {
		return p2bet;
	}
	public boolean getIsOver() {
		return isGameOver;
	}
	public void setIsOver(Boolean boo) {
		isGameOver = boo;
	}
	public boolean isP1Done() {
		return p1Done;
	}
	public boolean isP2Done() {
		return p2Done;
	}
	public int getTurn() {
		return turn;
	}
	public String getStatus(int player) {
		Hand h;
		if (player == 1) {
			h = p1;
		}
		else {
			h = p2;
		}
		if (h.isBust()) {
			return "Bust";
		}
		if (dealer.isBust()) {
			return "Player "
					+ player + " wins";
		}
		if (h.isBlackjack()) {
			return "BLACKJACK!!!!!";
		}
		if (player == 1 && !p1Done) {
			return "Player 1's turn...";
		}
		else if (player == 2 && !p2Done) {
			return "Player 2's turn...";
		}
		if (h.getValue() > dealer.getValue()) {
			return "Player "
					+ player + " wins";
		}
		else if (h.getValue() == dealer.getValue()) {
			return "Push";
		}
		else {
			return "Dealer wins";
		}
	}
	public BlackjackInstance getInstance() {
		return new BlackjackInstance(d,p1,p2,dealer,p1money,p2money,p1bet,p2bet,isGameOver,turn,p1Done,p2Done);
	}
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
		notifyObserversOfChange();
	}
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
	private void notifyObserversOfChange() {
		// TODO Auto-generated method stub
		setChanged();
		notifyObservers();
	}
}
