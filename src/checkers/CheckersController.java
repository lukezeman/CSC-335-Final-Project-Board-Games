/**
 * 
 */

package checkers;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CheckersController {
	private CheckersModel model;
	
	public boolean isGameOver() {
		/*
		 * The game over scene should be fairly simple to calculate, since
		 * the game is over only when every single piece of one of the two
		 * players is removed. 
		 * ACTUALLY NO!!!!!! A game can end if one side has no legal moves. 
		 * This is not counted as a draw, but rather as a victory for the
		 * other side.
		 */
		int count = 0;
		
		if (model == null) {
			return false;
		}
		HashMap<ArrayList<Integer>, int[][]> playerOnePieces = model.getPlayerOneLegalMoves();
		for (int[][] currPieceMoves: playerOnePieces.values()) {
			for (int[] currMove: currPieceMoves) {
				if (!isNull(currMove)) {
					count += 1;
				}
			}
		}
		if (count == 0) {
			return true;
		}
		count = 0;
		HashMap<ArrayList<Integer>, int[][]> playerTwoPieces = model.getPlayerTwoLegalMoves();
		for (int[][] currPieceMoves: playerTwoPieces.values()) {
			for (int[] currMove: currPieceMoves) {
				if (!isNull(currMove)) {
					count += 1;
				}
			}
		}
		if (count == 0) {
			return true;
		}
		
		
		return false;
	}
	
	private boolean isNull(int[] move) {
		return move[0] == -1 && move[1] == -1;
	}
	
	public boolean playerOneWon() {
		int count = 0;
		HashMap<ArrayList<Integer>, int[][]> playerOnePieces = model.getPlayerOneLegalMoves();
		for (int[][] currPieceMoves: playerOnePieces.values()) {
			for (int[] currMove: currPieceMoves) {
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
	
	public void takeInput(int xCoord, int yCoord) {
		model.takeInput(xCoord, yCoord);
	}
	
	/**
	 * Starts the game. Should probably add a "saved" game state here or
	 * something, but that shouldn't be very hard to implement once the 
	 * core stuff starts working.
	 * @param view
	 */
	public void startGame(CheckersView view, ObjectInputStream currSave, String player1, String player2) {
		this.model = new CheckersModel(view, currSave, player1, player2);
	}
}
