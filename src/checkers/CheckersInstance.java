package checkers;

import java.io.Serializable;
import java.util.Arrays;

public class CheckersInstance implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final int BOARD_SIZE = 8;
	private int[][] board;
	private transient int[][] selectedPossibleMoves;
	private transient int[] selectedIndex;
	private transient int[] movedIndex;
	
	public CheckersInstance() {
		this.board = new int[BOARD_SIZE][BOARD_SIZE];
		setUpInitialBoardState();
		selectedPossibleMoves = new int[][] {{-1, -1}, {-1, -1}, {-1, -1}, {-1, -1}};
		selectedIndex = new int[] {-1, -1};
		movedIndex = new int[] {-1, -1};
	}
	
	private void setUpInitialBoardState() {
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				int currBoardState;
				// Set the initial color of the circles to transparent
				if (i <= 2 && (i + j) % 2 == 1) {
					currBoardState = -1;
					//piecePane.promote();
				} else if (i >= BOARD_SIZE - 3 && (i + j) % 2 == 1) {
					currBoardState = 1;
					//piecePane.promote();
				} else {
					currBoardState = 0;
					if ((i + j) % 2 == 0) {
						//currBoardSquare.setPotentialMove(true);
					}
				}
				board[i][j] = currBoardState;
			}
		}
	}
	
	
	public int[][] getBoard(){
		return board;
	}
	
	/**
	 * Returns whether the grid element at index rowNum/colNum contains a piece
	 * that is owned by the player taking the input.
	 * @param rowNum
	 * @param colNum
	 * @param inputCameFromPlayer1
	 * @return
	 */
	public boolean isOwnedPiece(int rowNum, int colNum, boolean inputCameFromPlayer1) {
		int selectedSquare = board[rowNum][colNum];
		// First check whether the square contains a piece or not
		if (selectedSquare == 0 || selectedSquare == 3 || selectedSquare == -3) {
			return false;
		} 
		// Next check all possible states that would return true
		else if ((selectedSquare > 0 && inputCameFromPlayer1) || 
				(selectedSquare < 0 && !inputCameFromPlayer1)){
			return true;
		} else {
			// If this is not one of the valid states, return false
			return false;
		}
	}
	
	/**
	 * Returns whether the grid element at index rowNum/colNum contains a valid location
	 * for the piece currently selected to move to, as well as the input coming from the
	 * user who owns the square being selected.
	 * @param rowNum
	 * @param colNum
	 * @param inputCameFromPlayer1
	 * @return
	 */
	public boolean isPossibleMoveForSelectedPiece(int rowNum, int colNum, boolean inputCameFromPlayer1) {
		int selectedSquare = board[rowNum][colNum];
		//System.out.println(selectedSquare);
		// Next check all possible states that would return true
		if ((selectedSquare == 3 && inputCameFromPlayer1) || 
				(selectedSquare == -3 && !inputCameFromPlayer1)){
			return true;
		} else {
			// If this is not one of the valid states, return false
			return false;
		}
	}
	
	public void clearSelectedPossibleMoves() {
		for (int[] currSelected: selectedPossibleMoves) {
			//System.out.println(Arrays.toString(currSelected));
			int currRowVal = currSelected[0];
			int currColVal = currSelected[1];
			if (currRowVal != -1 && currColVal != -1) {
				this.board[currRowVal][currColVal] = 0;
			}
		}
	}
	
	public void selectPossiblePieceMoves(int rowNum, int colNum, boolean inputCameFromPlayer1) {
		// Clear out the previously selected board elements
		clearSelectedPossibleMoves();
		if (inputCameFromPlayer1) {
			fillSelectedPossibleMoves(rowNum, colNum, inputCameFromPlayer1, 1);
		} else {
			fillSelectedPossibleMoves(rowNum, colNum, inputCameFromPlayer1, -1);
		}
		setSelectedPotentialMovesToCorrectScore(inputCameFromPlayer1);
	}
	
	/**
	 * 
	 * 
	 * 
	 * This method should only be called if the board element at index rowNum/colNum
	 * has a piece in it that belongs to the player the input came from. It doesn't
	 * explicitly check this, but it will 100% break everything if the above condition
	 * isn't true. This is why there are other methods that check this condition.
	 * @param rowNum
	 * @param colNum
	 * @param inputCameFromPlayer1
	 * @param scalar
	 */
	private void fillSelectedPossibleMoves(int rowNum, int colNum, boolean inputCameFromPlayer1, int scalar) {
		// Set the selected field to the location of the selected board element
		this.selectedIndex[0] = rowNum;
		this.selectedIndex[1] = colNum;
		int selected = board[rowNum][colNum];
		// Set the first index to be the output of the forward left possible movement
		this.selectedPossibleMoves[0] = checkValidJump(rowNum, colNum, inputCameFromPlayer1, scalar, 1, 1);
		// Set the first index to be the output of the forward right possible movement
		this.selectedPossibleMoves[1] = checkValidJump(rowNum, colNum, inputCameFromPlayer1, scalar, -1, 1);
		
		// Checkers pieces can only move backwards if they are promoted
		if ((selected == 2 && inputCameFromPlayer1) || (selected == -2 && !inputCameFromPlayer1)) {
			this.selectedPossibleMoves[2] = checkValidJump(rowNum, colNum, inputCameFromPlayer1, scalar, 1, -1);
			this.selectedPossibleMoves[3] = checkValidJump(rowNum, colNum, inputCameFromPlayer1, scalar, -1, -1);
		} else {
			// If the piece isn't promoted, it doesn't have any backwards moves
			this.selectedPossibleMoves[2][0] = -1;
			this.selectedPossibleMoves[2][1] = -1;
			this.selectedPossibleMoves[3][0] = -1;
			this.selectedPossibleMoves[3][1] = -1;
		}
	}
	
	/**
	 * 
	 * @param rowNum
	 * @param colNum
	 * @param inputCameFromPlayer1
	 * @param scalar
	 * @param leftRightScalar
	 * @param upDownScalar
	 * @return
	 */
	private int[] checkValidJump(int rowNum, int colNum, boolean inputCameFromPlayer1, int scalar, 
			int leftRightScalar, int upDownScalar) {
		
		// Get the row and column index currently being searched for
		int checkedRow = rowNum - 1 * scalar * upDownScalar;
		int checkedColumn = colNum + 1 * scalar * leftRightScalar;
		//System.out.println("Checked Row: " + checkedRow + ". " + "Checked Column: " + checkedColumn);
		
		// Check whether the row and column being checked are within the board
		if (checkedRow >= 0 && checkedRow < BOARD_SIZE && checkedColumn < BOARD_SIZE && checkedColumn >= 0) {
			int selectedSquare = board[checkedRow][checkedColumn];
			if (selectedSquare == 0) {
				// If the selected square is empty, it is a valid location to move to
				return new int[] {checkedRow, checkedColumn};
			} 
			// If the selected square has an enemy piece in it, it could be jumped over
			else if (!isPieceControlledByAlly(selectedSquare, inputCameFromPlayer1)) {
				// Compute the row/column position of the potential jumping location
				checkedRow = rowNum - 2 * scalar * upDownScalar;
				checkedColumn = colNum + 2 * scalar * leftRightScalar;
				// Check if the row and column being checked are within the board
				if (checkedRow >= 0 && checkedRow < BOARD_SIZE
						&& checkedColumn < BOARD_SIZE && checkedColumn >= 0) {
					// Check if the location the piece would jump to is within the board
					selectedSquare = board[checkedRow][checkedColumn];
					if (selectedSquare == 0) {
						// If the jumped node is empty, it is a valid location to move to
						return new int[] {checkedRow, checkedColumn};
					} else {
						// If not, then you can't jump, so no legal move here
						return new int[] {-1, -1};
					}
				} else {
					// If the jumped node is not within the board, return null
					return new int[] {-1, -1};
				}
			} else {
				// If the checked position has one of the player's pieces in it, return null
				return new int[] {-1, -1};
			}
		} else {
			// If the checked row and column are not within the board, return null
			return new int[] {-1, -1};
		}
	}
	
	/**
	 * Set the score of the potential moves for the selected piece to be 3 or
	 * -3 depending on the player who gave the input.
	 * @param inputCameFromPlayer1
	 */
	private void setSelectedPotentialMovesToCorrectScore(boolean inputCameFromPlayer1) {
		for (int[] currSelected: this.selectedPossibleMoves) {
			int currRowVal = currSelected[0];
			int currColVal = currSelected[1];
			if (currRowVal != -1 && currColVal != -1) {
				if (inputCameFromPlayer1) {
					board[currRowVal][currColVal] = 3;
				} else {
					board[currRowVal][currColVal] = -3;
				}
			}
		}
	}
	
	public void moveSelected(int rowNum, int colNum, boolean inputCameFromPlayer1) {
		// This is the value at the index selected by the player
		int val = board[selectedIndex[0]][selectedIndex[1]];
		// Since the piece is being moved, its initial spot must be emptied
		board[selectedIndex[0]][selectedIndex[1]] = 0;
		// Promote if a piece reached the end of the board. Just reassign otherwise
		if (rowNum == 0 && inputCameFromPlayer1 && val == 1) {
			board[rowNum][colNum] = 2;
		} else if (rowNum == BOARD_SIZE - 1 && !inputCameFromPlayer1 && val == -1) {
			board[rowNum][colNum] = -2;
		} else {
			board[rowNum][colNum] = val;
		}
		// If the piece moved more than one tile horizontally, it jumped a piece
		if (Math.abs(selectedIndex[0] - rowNum) > 1) {
			//System.out.println("Jumped a Piece!");
			// Bit of a trick, but piece jumped is in the middle of prev and new location
			int skippedRowVal = (selectedIndex[0] + rowNum) / 2;
			int skippedColVal = (selectedIndex[1] + colNum) / 2;
			board[skippedRowVal][skippedColVal] = 0;
			//TODO Check if the piece that jumped can jump another piece
		}
		
		// Assign the moved values if I ever get around to making animations
		this.movedIndex[0] = rowNum;
		this.movedIndex[1] = colNum;
	}
	
	public void resetMovedAndSelected() {
		this.selectedIndex[0] = -1;
		this.selectedIndex[1] = -1;
		this.movedIndex[0] = -1;
		this.movedIndex[1] = -1;
		for (int[] currSelected: selectedPossibleMoves) {
			//System.out.println(Arrays.toString(currSelected));
			currSelected[0] = -1;
			currSelected[1] = -1;
		}
	}
	
	public int[] getSelectedIndex() {
		return selectedIndex.clone();
	}
	
	public int[] getMovedIndex() {
		return movedIndex.clone();
	}
	
	public int[][] getSelectedPossibleMoves(){
		return selectedPossibleMoves.clone();
	}
	
	private boolean isPieceControlledByAlly(int pieceScore, boolean inputCameFromPlayer1) {
		if ((pieceScore == 1 || pieceScore == 2) && inputCameFromPlayer1) {
			return true;
		} else if ((pieceScore == -1 || pieceScore == -2) && !inputCameFromPlayer1) {
			return true;
		} else {
			return false;
		}
	}
}









