/**
 * 
 */

package checkers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CheckersInstance implements Serializable{

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
	 * 
	 * @param player1
	 * @param player2
	 */
	public CheckersInstance(String player1, String player2) {
		this.player1 = player1;
		this.player2 = player2;
		
		isPlayerOnesTurn = true;
	}
	
	/**
	 * 
	 * @param board
	 */
	public void setBoard(int[][] board) {
		this.board = board;
	}
	
	/**
	 * 
	 * @return
	 */
	public int[][] getBoard(){
		return board;
	}
	
	/**
	 * 
	 * @return
	 */
	public int[] getSelectedIndex() {
		return selectedIndex;
	}
	
	/**
	 * 
	 * @return
	 */
	public int[] getMovedIndex() {
		return movedIndex;
	}
	
	/**
	 * 
	 * @return
	 */
	public int[][] getSelectedLegalMoves(){
		return selectedLegalMoves;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPlayer1() {
		return player1;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPlayer2() {
		return player2;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap<ArrayList<Integer>, int[][]> getPlayerOneLegalMoves(){
		return playerOneLegalMoves;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap<ArrayList<Integer>, int[][]> getPlayerTwoLegalMoves(){
		return playerTwoLegalMoves;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isPlayerOnesTurn() {
		return isPlayerOnesTurn;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap<ArrayList<Integer>, ArrayList<int[]>> getPlayerOneLegalCaptures(){
		return playerOneLegalCaptures;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap<ArrayList<Integer>, ArrayList<int[]>> getPlayerTwoLegalCaptures(){
		return playerTwoLegalCaptures;
	}
	
	/**
	 * 
	 * @param isPlayerOnesTurn
	 */
	public void setIsPlayerOnesTurn(boolean isPlayerOnesTurn) {
		this.isPlayerOnesTurn = isPlayerOnesTurn;
	}
	
	/**
	 * 
	 * @param selectedLegalMoves
	 */
	public void setSelectedLegalMoves(int[][] selectedLegalMoves) {
		this.selectedLegalMoves = selectedLegalMoves;
	}
	
	/**
	 * 
	 * @param selectedIndex
	 */
	public void setSelectedIndex(int[] selectedIndex) {
		this.selectedIndex = selectedIndex;
	}
	
	/**
	 * 
	 * @param movedIndex
	 */
	public void setMovedIndex(int[] movedIndex) {
		this.movedIndex = movedIndex;
	}
	
	/**
	 * 
	 * @param playerOneLegalMoves
	 */
	public void setPlayerOneLegalMoves( HashMap<ArrayList<Integer>, int[][]> playerOneLegalMoves) {
		this.playerOneLegalMoves = playerOneLegalMoves;
	}
	
	/**
	 * 
	 * @param playerTwoLegalMoves
	 */
	public void setPlayerTwoLegalMoves( HashMap<ArrayList<Integer>, int[][]> playerTwoLegalMoves) {
		this.playerTwoLegalMoves = playerTwoLegalMoves;
	}
	
	/**
	 * 
	 * @param playerOneLegalCaptures
	 */
	public void setPlayerOneLegalCaptures(HashMap<ArrayList<Integer>, ArrayList<int[]>> playerOneLegalCaptures) {
		this.playerOneLegalCaptures = playerOneLegalCaptures;
	}
	
	/**
	 * 
	 * @param playerTwoLegalCaptures
	 */
	public void setPlayerTwoLegalCaptures(HashMap<ArrayList<Integer>, ArrayList<int[]>> playerTwoLegalCaptures) {
		this.playerTwoLegalCaptures = playerTwoLegalCaptures;
	}
}






