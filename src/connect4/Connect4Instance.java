/**
 * @author Achilles Soto
 * Course: CSC 335
 * File: Connect4Controller.java
 * Allows for saving/loading a Connect4 Game by holding the contents of the game
 */
package connect4;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * This class defines methods that allow saving/loading a Connect4 and getters 
 * for the game board and a player's turn.
 */
public class Connect4Instance implements Serializable {
	
	private char[][] board;
	private char playerTurn;
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Allows for an instance of this class to be made and sets up variables
	 * @param model - type Connect4Model which holds a player's turn and the game board
	 */
	public Connect4Instance(Connect4Model model) {
		board = model.getBoard();
		playerTurn = model.getTurn();
	}
	
	/**
	 * Retrieves and returns the upcoming player's turn
	 * @return a char which represents a player
	 */
	protected char getTurn() {
		return playerTurn;
	}
	
	/**
	 * Retrieves and returns a Connect4 game board
	 * @return an array of arrays of chars which is the game board
	 */
	protected char[][] getBoard(){
		return board;
	}
	
	/**
	 * Saves a Connect4 game into a .dat file which will hold a Connect4Instance.
	 */
	protected void saveGame() {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("save_connect4.dat"))) {
			out.writeObject(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}
	
	/*
	 * Loads in a Connect4 game from a .dat file if it exists, returning the Connect4Instance object
	 * @return the board game object, ReversiBoard
	 */
	public static Connect4Instance loadGame() {
		File file = new File("save_connect4.dat");
		if (file.exists()) {
			try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
				return (Connect4Instance) in.readObject();
			} catch (IOException | ClassNotFoundException e) {
				return null;
			}
		}
		// File doesn't exist
		return null;
	}

}
