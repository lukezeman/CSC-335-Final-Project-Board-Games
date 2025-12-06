package blackjack;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 * This is the controller class that uses all of the functionality in the model.
 * 
 * @author Aidin Miller
 * @version 1.0
 */
public class BlackjackController {
	private BlackjackModel m;
	/**
	 * Constructor for the controller
	 * @param model the BlackjackModel that will be used
	 */
	public BlackjackController(BlackjackModel model) {
		m = model;
	}
	/**
	 * Performs a hit action
	 */
	public void hit() {
		m.hit();
	}
	/**
	 * Performs a stand action
	 */
	public void stand() {
		m.stand();
	}
	/**
	 * Gets player 1's hand
	 * 
	 * @return player 1's hand object
	 */
	public Hand getP1() {
		return m.getP1();
	}
	/**
	 * Gets player 2's hand
	 * 
	 * @return player 2's hand object
	 */
	public Hand getP2() {
		return m.getP2();
	}
	/**
	 * Gets dealer's hand
	 * 
	 * @return dealer's hand
	 */
	public Hand getDealer() {
		return m.getDealer();
	}
	/**
	 * Gets player 1's money
	 * 
	 * @return an int representing player 1's money
	 */
	public int getP1money() {
		return m.getP1Money();
	}
	/**
	 * Gets player 2's money
	 * 
	 * @return an int representing player 2's money
	 */
	public int getP2money() {
		return m.getP2Money();
	}
	/**
	 * Gets the bet amount for player 1
	 * 
	 * @return Player 1's bet amount
	 */
	public int getP1Bet() {
		return m.getP1Bet();
	}
	/**
	 * Gets the bet amount for player 2
	 * 
	 * @return Player 2's bet amount
	 */
	public int getP2Bet() {
		return m.getP2Bet();
	}
	/**
	 * Gets which turn it is
	 * 
	 * @return an int based on whose turn it is
	 */
	public int getTurn() {
		return m.getTurn();
	}
	/**
	 * Checks to see if the game is over
	 * @return true if it is over, false otherwise
	 */
	public boolean isGameOver() {
		return m.getIsOver();
	}
	/**
	 * Checks to see if player 1 has finished their turn
	 * 
	 * @return true if the turn is over, false otherwise
	 */
	public boolean p1TurnOver() {
		return m.isP1Done();
	}
	/**
	 * Checks to see if player 2 has finished their turn
	 * 
	 * @return true if the turn is over, false otherwise
	 */
	public boolean p2TurnOver() {
		return m.isP2Done();
	}
	/**
	 * Gets status message for player 1
	 * 
	 * @return a string representing the status
	 */
	public String getP1Status() {
		return m.getStatus(1);
	}
	/**
	 * Gets status message for player 2
	 * 
	 * @return a string representing the status
	 */
	public String getP2Status() {
		return m.getStatus(2);
	}
	/**
	 * Sets the status of the game
	 */
	public void setGameOver() {
		m.setIsOver(getP1money() == 0 || getP2money() == 0);
	}
	/**
	 * Determines the winner of the game
	 * 
	 * @return "Player 1 wins", "Player 2 wins", or "Tie"
	 */
	public String getWinner() {
		if (getP1money() > getP2money()) {
			return "Player 1 wins";
		}
		else if (getP1money() < getP2money()) {
			return "Player 2 wins";
		}
		else {
			return "Tie";
		}
	}
	/**
	 * This function saves the current game state
	 */
	public void saveGame() {
		try {
			m.deleteObservers();
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("save_blackjack.dat"));
			out.writeObject(m.getInstance());
			out.close();
		} catch (IOException e) {
			System.err.println("Error saving game");
			e.printStackTrace();
		}
	}
	/**
	 * This function loads the game state
	 */
	public void loadGame() {
		File f = new File("save_blackjack.dat");
		if (!f.exists()) {
			return;
		}
		
		try (ObjectInputStream in = new ObjectInputStream( new FileInputStream("save_blackjack.dat"))) {
			BlackjackInstance instance = (BlackjackInstance) in.readObject();
			m.setInstance(instance);
		}
		catch (IOException e) {
			System.err.println("Error loading game");
		}
		catch (ClassNotFoundException e) {
			System.err.println("Save file corrupted");
		}
	}
	/**
	 * This function deletes the save state
	 */
	public void deleteSave() {
		File f = new File("save_blackjack.dat");
		if (f.exists()) {
			f.delete();
		}
	}
	/**
	 * This function resets the game to its initial state
	 */
	public void resetGame() {
		m.reset();
		deleteSave();
	}
	/**
	 * This function starts a new game
	 * 
	 * @param bet1 player 1's bet
	 * @param bet2 player 2's bet
	 * @return true if the game started, false otherwise
	 */
	public boolean startGame(int bet1, int bet2) {
		// TODO Auto-generated method stub
		return m.start(bet1, bet2);
	}
	/**
	 * This function checks to see if the game is in progress
	 * 
	 * @return true if it is, false otherwise
	 */
	public boolean isGameInProgress() {
		return getTurn() != 0 || !isGameOver();
	}
}