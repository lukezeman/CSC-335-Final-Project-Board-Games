package connect4;

import java.util.Observable;

public class Connect4Model extends Observable {
	private char[][] board;
	private char playerTurn;
	
	public Connect4Model() {
		board = new char[6][7];
		board[5][6] = 'r';
		playerTurn = 'r';
		
		for (char[] row : board) {
			for (char col : row) {
				if (col == '\0')System.out.print(".");
				else System.out.println(col);
			}
			System.out.println();
		}
	}
	
	public char[][] getBoard(){
		return board;
	}
	
	public char getTurn() {
		return playerTurn;
	}
	
	public boolean setTurn(int col, char player) {
		int row = 0;
		
		// Verifies that column isn't full and that its a valid move
		if (board[row][col] != '\0') return false;
		
		// Looks for a empty slot that has a slot under that isn't empty
		while (board[row + 1][col] != '0') row ++;
		
		board[row][col] = player;
		
		setChanged();
		notifyObservers(board);
		
		return true;
	}
	
	public boolean isGameOver(int row, int col) {		
		if (isWinner(row, col)) return true;
		
		for (char item : board[row]) {
			if (item == '\0') break;
		}	
		return false;
	}
	
	public boolean isWinner(int row, int col) {
		char player = board[row][col];
		int[][] dirs = {{-1,-1}, {-1,0}, {-1,1}, {0,-1}, {0,1}, {1,-1}, {1,0},{1,1}};
		
		return false;
	}
}
