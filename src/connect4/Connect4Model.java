/**
 * @author Achilles Soto
 * Course: CSC 335
 * File: Connect4Model.java
 * This program handles all functionality of connect4, making 
 * moves for both players, updating the game board accordingly.
 */
package connect4;

import java.util.Observable;

/**
 * This class defines methods to handle game logic, including making moves, 
 * updating the game board, and checking for game-over conditions.
 */
public class Connect4Model extends Observable {
	private char[][] board;
	private char playerTurn;
	
	/**
	 * Allows for an instance of this class to be made and sets up variables
	 */
	public Connect4Model() {
		board = new char[6][7];
		playerTurn = 'y';
	}
	
	/**
	 * Saves the current game board and player turn to a .dat file as a Connect4Instance object
	 */
	public void saveGame() {
		Connect4Instance instance = new Connect4Instance(this);
		instance.saveGame();
	}
	
	/**
	 * Retrieves the current Connect4 game
	 * 
	 * @return a char[][] which holds all player's pieces
	 */
	public char[][] getBoard(){
		return board;
	}
	
	/**
	 * Sets a board to a different game board
	 * @param board - an array of arrays of chars which is a game board
	 */
	public void setBoard(char[][] board){
		this.board = board;
	}
	
	/**
	 * Retrieves which ever player's turn it is
	 * 
	 * @return a char which represents the player color
	 */
	public char getTurn() {
		return playerTurn;
	}
	
	/**
	 * Sets a player's turn if the player doesn't want to start first
	 * 
	 * @param player - a char which represents the player color
	 */
	public void setTurn(char player) {
		playerTurn = player;
	}
	
	/**
	 * Adds a player piece to game board
	 * @param col - int that represents a board column
	 */
	public void setMove(int col) {
		int row = 0;
		
		// Looks for a empty slot that has a slot under that isn't empty
		while ((row + 1) <= 5 && board[row + 1][col] != 'r' && board[row + 1][col] != 'y') row ++;
		
		board[row][col] = playerTurn;
		
		int[] pair = {row, col};
		
		setChanged();
		notifyObservers(pair);
	}
	
	/**
	 * Checks if board is full of pieces or if a player has connected 4 pieces
	 * 
	 * @param row - an int which is the row a piece landed in
	 * @param col - an int which is the column a piece was dropped in
	 * @return an int, 1(Game over by winner), 0 (Not game over), -1 (Game over by draw).
	 */
	public int isGameOver(int row, int col) {	
		
		// Checks if theres a winner yet
		if (isWinner(row, col)) return 1;
		
		// First row still has open slots
		for (char item : board[0]) {
			if (item == '\0') return 0;
		}	
		
		// Theres no open slots or winner so its a draw
		return -1;
	}
	
	/**
	 * Checks if there's a winner from all possible directions for the last move
	 * 
	 * @param row - an int which is the row a piece landed in
	 * @param col - an int which is the column a piece was dropped in
	 * @return a boolean where true is the player has won and false no winner.
	 */
	private boolean isWinner(int row, int col) {
		char player = board[row][col];
		
		if (checkDiagonal(row, col, player)) return true;
		if (checkVertical(row, col, player)) return true;
		if (checkHorizontal(row, col, player)) return true;

		return false;
	}
	
	/**
	 * Checks if there are four connected pieces diagonally from a coordinate
	 * 
	 * @param row - an int which is the row a piece landed in
	 * @param col - an int which is the column a piece was dropped in
	 * @param player - a char which represents the player color
	 * @return a boolean, true if there are at least four connected player pieces diagonally
	 */
	private boolean checkDiagonal(int row, int col, char player){
		// Counts pieces from upper left and lower right of the coordinate
		int diagDir1 = 1;
		diagDir1 += countConnected(row, col, -1, -1, player);
		diagDir1 += countConnected(row, col, 1, 1, player);
		
		// Checks if theres at least four connected
		if (diagDir1 >= 4) return true;
		
		// Counts pieces from upper right to lower left of the coordinate
		int diagDir2 = 1;
		diagDir2 += countConnected(row, col, -1, 1, player);
		diagDir2 += countConnected(row, col, 1, -1, player);
		
		// Checks if theres at least four connected
		if (diagDir2 >= 4) return true;
		
		return false;
	}
	
	/**
	 * Checks if there are four connected pieces horizontally from a coordinate
	 * 
	 * @param row - an int which is the row a piece landed in
	 * @param col - an int which is the column a piece was dropped in
	 * @param player - a char which represents the player color
	 * @return a boolean, true if there are at least four connected player pieces horizontally
	 */
	private boolean checkHorizontal(int row, int col, char player) {
		// Counts pieces to the left and right of the coordinate
		int count = 1;
		count += countConnected(row, col, 0, -1, player);
		count += countConnected(row, col, 0, 1, player);
		
		// Checks if theres at least four connected
		if (count >= 4) return true;
		
		return false;
	}
	
	/**
	 * Checks if there are four connected pieces vertically from a coordinate
	 * 
	 * @param row - an int which is the row a piece landed in
	 * @param col - an int which is the column a piece was dropped in
	 * @param player - a char which represents the player color
	 * @return a boolean, true if there are at least four connected player pieces vertically
	 */
	private boolean checkVertical(int row, int col, char player) {
		// Counts pieces above and below the coordinate
		int count = 1;
		count += countConnected(row, col, -1, 0, player);
		count += countConnected(row, col, 1, 0, player);
	
		// Checks if theres at least four connected
		if (count >= 4) return true;
		
		return false;
	}
	
	/**
	 * Checks if there are four of a players pieces in a row going a certain direction
	 * 
	 * @param row - an int which is the row a piece landed in
	 * @param col - an int which is the column a piece was dropped in
	 * @param xDir - an int which is the direction x is going to move towards
	 * @param yDir - an int which is the direction y is going to move towards
	 * @param player - a char which represents the player color
	 * @return a int which is the count of the number of connected pieces in a direction
	 */
	private int countConnected(int row, int col, int xDir, int yDir, char player) {
		int x = row + xDir;
		int y = col + yDir;
		int counter = 0;
		
		// Loops over pieces in a direction while staying inbounds
		while ((x >= 0 && x <= 5 && y >= 0 && y <= 6) && board[x][y] == player) {
			counter += 1;
			x += xDir;
			y += yDir;
		}
		
		return counter;
	}
}
