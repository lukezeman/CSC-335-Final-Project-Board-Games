/**
 * @author Luke Zeman
 * Assignment: CSC 335 Team Project
 * File: YahtzeeInstance.java
 * Date: 12/08/2025
 * 
 * Description: This class represents a saved instance of a Yahtzee game. It implements Serializable
 * 			 	to allow saving and loading the game state, including player information and current turn.
 */

package yahtzee;

import java.io.Serializable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This class represents a saved instance of a Yahtzee game.
 */
public class YahtzeeInstance implements Serializable {
	private YahtzeePlayer player1;
	private YahtzeePlayer player2;
	private YahtzeePlayer currentPlayer;	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a YahtzeeInstance from the given YahtzeeModel.
	 * 
	 * @param model - The YahtzeeModel to create the instance from.
	 */
	public YahtzeeInstance(YahtzeeModel model) {
		this.player1 = model.getPlayer1();
		this.player2 = model.getPlayer2();
		this.currentPlayer = model.getCurrentPlayer();
	}
	
	/**
	 * Retrieves player 1 of the game instance.
	 * 
	 * @return The YahtzeePlayer representing player 1.
	 */
	public YahtzeePlayer getPlayer1() {
		return player1;
	}
	
	/**
	 * Retrieves player 2 of the game instance.
	 * 
	 * @return The YahtzeePlayer representing player 2.
	 */
	public YahtzeePlayer getPlayer2() {
		return player2;
	}
	
	/**
	 * Retrieves the current player of the game instance.
	 * 
	 * @return The YahtzeePlayer representing the current player.
	 */
	public YahtzeePlayer getCurrentPlayer() {
		return currentPlayer;
	}
	
	/**
	 * Saves the current game instance to a file.
	 */
	protected void saveGame() {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("save_yahtzee.dat"))) {
			out.writeObject(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}
	
	/**
	 * Loads a saved game instance from a file.
	 * 
	 * @return The YahtzeeInstance representing the saved game, or null if no saved game exists.
	 */
	public static YahtzeeInstance loadGame() {
		File file = new File("save_yahtzee.dat");
		if (!file.exists()) {
			return null;
		}
		
		// Checks if a saved game exists
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
			return (YahtzeeInstance) in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			return null;
		}
		
	}
	
}
