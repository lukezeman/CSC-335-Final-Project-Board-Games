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
	
	
	public CheckersInstance(String player1, String player2) {
		this.player1 = player1;
		this.player2 = player2;
		isPlayerOnesTurn = true;
	}
	
	public void setBoard(int[][] board) {
		this.board = board;
	}
	
	public int[][] getBoard(){
		return board;
	}
	
	public int[] getSelectedIndex() {
		return selectedIndex;
	}
	
	public int[] getMovedIndex() {
		return movedIndex;
	}
	
	public int[][] getSelectedPossibleMoves(){
		return selectedLegalMoves;
	}
	
	public String getPlayer1() {
		return player1;
	}
	
	public String getPlayer2() {
		return player2;
	}
	
	public HashMap<ArrayList<Integer>, int[][]> getPlayerOneLegalMoves(){
		return playerOneLegalMoves;
	}
	
	public HashMap<ArrayList<Integer>, int[][]> getPlayerTwoLegalMoves(){
		return playerTwoLegalMoves;
	}
	
	public boolean isPlayerOnesTurn() {
		return isPlayerOnesTurn;
	}
	
	public HashMap<ArrayList<Integer>, ArrayList<int[]>> getPlayerOneLegalCaptures(){
		return playerOneLegalCaptures;
	}
	
	public HashMap<ArrayList<Integer>, ArrayList<int[]>> getPlayerTwoLegalCaptures(){
		return playerTwoLegalCaptures;
	}
	
	
	public void setIsPlayerOnesTurn(boolean isPlayerOnesTurn) {
		this.isPlayerOnesTurn = isPlayerOnesTurn;
	}
	
	public void setSelectedLegalMoves(int[][] selectedLegalMoves) {
		this.selectedLegalMoves = selectedLegalMoves;
	}
	
	public void setSelectedIndex(int[] selectedIndex) {
		this.selectedIndex = selectedIndex;
	}
	
	public void setMovedIndex(int[] movedIndex) {
		this.movedIndex = movedIndex;
	}
	
	public void setPlayerOneLegalMoves( HashMap<ArrayList<Integer>, int[][]> playerOneLegalMoves) {
		this.playerOneLegalMoves = playerOneLegalMoves;
	}
	
	public void setPlayerTwoLegalMoves( HashMap<ArrayList<Integer>, int[][]> playerTwoLegalMoves) {
		this.playerTwoLegalMoves = playerTwoLegalMoves;
	}
	
	public void setPlayerOneLegalCaptures(HashMap<ArrayList<Integer>, ArrayList<int[]>> playerOneLegalCaptures) {
		this.playerOneLegalCaptures = playerOneLegalCaptures;
	}
	
	public void setPlayerTwoLegalCaptures(HashMap<ArrayList<Integer>, ArrayList<int[]>> playerTwoLegalCaptures) {
		this.playerTwoLegalCaptures = playerTwoLegalCaptures;
	}
}