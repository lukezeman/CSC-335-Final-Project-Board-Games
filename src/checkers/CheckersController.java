/**
 * This class creates the controller part of the MVC archetype for the Checkers game. It takes
 * input from the View and passes it along to the model while giving the View some methods
 * to use in return.
 * @author Charlie Cain
 */

package checkers;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CheckersController {
	private CheckersModel model;

	/**
	 * The game over is checkers occurs when one side has no legal moves left to
	 * make, regardless if that comes from not having any legal pieces or just not
	 * having any legal moves due to some truly insane board state. This method
	 * checks for both.
	 * 
	 * @return a boolean determining if the game is over
	 */
	public boolean isGameOver() {
		int count = 0;
		if (model == null) {
			return false;
		}
		// Checks to see if player one has any legal moves
		if (model.isPlayerOnesTurn()) {
			HashMap<ArrayList<Integer>, int[][]> playerOnePieces = model.getPlayerOneLegalMoves();
			for (int[][] currPieceMoves : playerOnePieces.values()) {
				for (int[] currMove : currPieceMoves) {
					if (!isNull(currMove)) {
						count += 1;
						break;
					}
				}
			}
			// If player one does not have any legal moves, return true
			if (count == 0) {
				return true;
			}
		} else {
			// If player two does not have any legal moves, return true
			HashMap<ArrayList<Integer>, int[][]> playerTwoPieces = model.getPlayerTwoLegalMoves();
			for (int[][] currPieceMoves : playerTwoPieces.values()) {
				for (int[] currMove : currPieceMoves) {
					if (!isNull(currMove)) {
						count += 1;
						break;
					}
				}
			}

			if (count == 0) {
				return true;
			}
		}
		// If neither condition was triggered, the game isn't over.
		return false;
	}

	/**
	 * Checks whether the passed array is equal to the value {-1, -1}, which is the
	 * null location, or not.
	 * 
	 * @param move: The array whose location is being checked.
	 * @return: Whether the value equals th null location
	 */
	private boolean isNull(int[] move) {
		return move[0] == -1 && move[1] == -1;
	}

	/**
	 * Determines if player 1 was the winner by checking if they have any legal
	 * moves. If they do not, then assume player 2 is the winner. This method
	 * assumes that the game is over.
	 * 
	 * @return a boolean determining if player 1 won.
	 */
	public boolean playerOneWon() {
		int count = 0;
		HashMap<ArrayList<Integer>, int[][]> playerOnePieces = model.getPlayerOneLegalMoves();
		for (int[][] currPieceMoves : playerOnePieces.values()) {
			for (int[] currMove : currPieceMoves) {
				if (!isNull(currMove)) {
					count += 1;
				}
			}
		}
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Takes an x and y coordinate from the view and sends it to the model to
	 * process
	 * 
	 * @param xCoord: The row value
	 * @param yCoord: The column value
	 */
	public void takeInput(int xCoord, int yCoord) {
		model.takeInput(xCoord, yCoord);
	}

	/**
	 * Starts the game. Passes the saved game along to the model
	 * 
	 * @param view:     The view that works with this controller
	 * @param currSave: The ObjectInputStream containing the saved game
	 * @param player1:  The name of player 1
	 * @param player2:  The name of player 2
	 */
	public void startGame(CheckersView view, ObjectInputStream currSave, String player1, String player2) {
		this.model = new CheckersModel(view, currSave, player1, player2);
	}
}
