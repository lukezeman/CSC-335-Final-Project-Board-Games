/**
 * This class effectively just stores the current state of the checkers game at any given
 * moment. This class is serializable, so it is what is being stored when the game is savd.
 * Due to how much information is being stored here by necessity, the view can do a lot of cool
 * optimizations. The entire class is just getter and setter methods for like 12 seperate
 * objects whose states should be preserved through saving and loading
 * @author Charlie Cain
 */

package checkers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CheckersInstance implements Serializable {

	private static final long serialVersionUID = 1L;
	private int[][] board;
	private int[][] selectedLegalMoves;
	private int[] selectedIndex;
	private int[] movedIndex;
	private boolean isPlayerOnesTurn;
	private String player1;
	private String player2;

	private HashMap<ArrayList<Integer>, int[][]> playerOneLegalMoves;
	private HashMap<ArrayList<Integer>, int[][]> playerTwoLegalMoves;
	private HashMap<ArrayList<Integer>, ArrayList<int[]>> playerOneLegalCaptures;
	private HashMap<ArrayList<Integer>, ArrayList<int[]>> playerTwoLegalCaptures;

	/**
	 * Constructs an instance of the CheckersInstance class, with the names of
	 * player 1 and player 2.
	 * @param player1: A string containing the name of player 1
	 * @param player2: A string containing the name of player 2
	 */
	public CheckersInstance(String player1, String player2) {
		this.player1 = player1;
		this.player2 = player2;
		// This is initialized to true, since all games start with player 1 going first
		isPlayerOnesTurn = true;
	}

	/**
	 * Returns the stored board
	 * @return this.board
	 */
	public int[][] getBoard() {
		return board;
	}

	/**
	 * Returns the index of the piece the user selected
	 * @return this.selectedIndex
	 */
	public int[] getSelectedIndex() {
		return selectedIndex;
	}

	/**
	 * Returns the index of the grid that the moved piece moved to.
	 * @return this.movedIndex
	 */
	public int[] getMovedIndex() {
		return movedIndex;
	}

	/**
	 * Returns the legal moves that the piece selecte by the user can make.
	 * @return this.selectedLegalMoves
	 */
	public int[][] getSelectedLegalMoves() {
		return selectedLegalMoves;
	}

	/**
	 * Returns the name of player 1
	 * @return this.player1
	 */
	public String getPlayer1() {
		return player1;
	}

	/**
	 * Returns the name of player 2
	 * @return this.player2
	 */
	public String getPlayer2() {
		return player2;
	}

	/**
	 * Returns a HashMap of all legal moves of every piece that player 1 owns
	 * @return this.playerOneLegalMoves
	 */
	public HashMap<ArrayList<Integer>, int[][]> getPlayerOneLegalMoves() {
		return playerOneLegalMoves;
	}

	/**
	 * Returns a HashMap of all legal moves of every piece that player 2 owns
	 * @return this.playerTwoLegalMoves
	 */
	public HashMap<ArrayList<Integer>, int[][]> getPlayerTwoLegalMoves() {
		return playerTwoLegalMoves;
	}

	/**
	 * Returns whether it is currently player 1's turn
	 * @return this.isPlayerOnesTurn
	 */
	public boolean isPlayerOnesTurn() {
		return isPlayerOnesTurn;
	}

	/**
	 * Returns a HashMap of all legal captures of every piece that player 1 owns
	 * @return this.playerOneLegalCaptures
	 */
	public HashMap<ArrayList<Integer>, ArrayList<int[]>> getPlayerOneLegalCaptures() {
		return playerOneLegalCaptures;
	}

	/**
	 * Returns a HashMap of all legal captures of every piece that player 2 owns
	 * @return this.playerTwoLegalCaptures
	 */
	public HashMap<ArrayList<Integer>, ArrayList<int[]>> getPlayerTwoLegalCaptures() {
		return playerTwoLegalCaptures;
	}

	/**
	 * Updates the board
	 * @param board: the new value for board
	 */
	public void setBoard(int[][] board) {
		this.board = board;
	}
	
	/**
	 * Updates isPlayerOnesTurn
	 * @param isPlayerOnesTurn: the new value for isPlayerOnesTurn
	 */
	public void setIsPlayerOnesTurn(boolean isPlayerOnesTurn) {
		this.isPlayerOnesTurn = isPlayerOnesTurn;
	}

	/**
	 * Updates selectedLegalMoves
	 * @param selectedLegalMoves: the new value for selectedLegalMoves
	 */
	public void setSelectedLegalMoves(int[][] selectedLegalMoves) {
		this.selectedLegalMoves = selectedLegalMoves;
	}

	/**
	 * Updates selectedIndex
	 * @param selectedIndex: the new value for selectedIndex
	 */
	public void setSelectedIndex(int[] selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	/**
	 * Updates movedIndex
	 * @param movedIndex: the new value for movedIndex
	 */
	public void setMovedIndex(int[] movedIndex) {
		this.movedIndex = movedIndex;
	}

	/**
	 * Updates playerOneLegalMoves
	 * @param playerOneLegalMoves: the new value for playerOneLegalMoves
	 */
	public void setPlayerOneLegalMoves(HashMap<ArrayList<Integer>, int[][]> playerOneLegalMoves) {
		this.playerOneLegalMoves = playerOneLegalMoves;
	}

	/**
	 * Updates playerTwoLegalMoves
	 * @param playerTwoLegalMoves: the new value for playerTwoLegalMoves
	 */
	public void setPlayerTwoLegalMoves(HashMap<ArrayList<Integer>, int[][]> playerTwoLegalMoves) {
		this.playerTwoLegalMoves = playerTwoLegalMoves;
	}

	/**
	 * Updates playerOneLegalCaptures
	 * @param playerOneLegalCaptures: the new value for playerOneLegalCaptures
	 */
	public void setPlayerOneLegalCaptures(HashMap<ArrayList<Integer>, ArrayList<int[]>> playerOneLegalCaptures) {
		this.playerOneLegalCaptures = playerOneLegalCaptures;
	}

	/**
	 * Updates playerTwoLegalCaptures
	 * @param playerTwoLegalCaptures: the new value for playerTwoLegalCaptures
	 */
	public void setPlayerTwoLegalCaptures(HashMap<ArrayList<Integer>, ArrayList<int[]>> playerTwoLegalCaptures) {
		this.playerTwoLegalCaptures = playerTwoLegalCaptures;
	}
}
