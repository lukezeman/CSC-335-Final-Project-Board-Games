package checkers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class CheckersModel extends Observable {

	private static final int BOARD_SIZE = 8;
	
	private int[][] board;
	private int[][] selectedLegalMoves;
	private int[] selectedIndex;
	private int[] movedIndex;
	private boolean isPlayerOnesTurn;
	
	private CheckersInstance currInstance;
	
	private HashMap<ArrayList<Integer>, int[][]> playerOneLegalMoves;
	private HashMap<ArrayList<Integer>, int[][]> playerTwoLegalMoves;
	
	private HashMap<ArrayList<Integer>, ArrayList<int[]>> playerOneLegalCaptures;
	private HashMap<ArrayList<Integer>, ArrayList<int[]>> playerTwoLegalCaptures;

	/**
	 * 
	 * @param view
	 * @param input
	 * @param player1
	 * @param player2
	 */
	public CheckersModel(CheckersView view, ObjectInputStream input, String player1, String player2) {
		addObserver(view);
		if (input != null) {
			try {
				currInstance = (CheckersInstance) input.readObject();
				this.board = currInstance.getBoard();
				this.selectedLegalMoves = currInstance.getSelectedPossibleMoves();
				this.selectedIndex = currInstance.getSelectedIndex();
				this.movedIndex = currInstance.getMovedIndex();
				this.isPlayerOnesTurn = currInstance.isPlayerOnesTurn();
				this.playerOneLegalMoves = currInstance.getPlayerOneLegalMoves();
				this.playerTwoLegalMoves = currInstance.getPlayerTwoLegalMoves();
				this.playerOneLegalCaptures = currInstance.getPlayerOneLegalCaptures();
				this.playerTwoLegalCaptures = currInstance.getPlayerTwoLegalCaptures();
			} catch (ClassNotFoundException | IOException e) {
				setUpDefault(player1, player2);
			}
		} else {
			setUpDefault(player1, player2);
		}
		setChanged();
		notifyObservers(currInstance);
		
	}
	
	/**
	 * 
	 * @param player1
	 * @param player2
	 */
	private void setUpDefault(String player1, String player2) {
		this.currInstance = new CheckersInstance(player1, player2);
		this.board = new int[BOARD_SIZE][BOARD_SIZE];
		this.selectedLegalMoves = new int[][] {{-1, -1}, {-1, -1}, {-1, -1}, {-1, -1}};
		this.selectedIndex = new int[] {-1, -1};
		this.movedIndex = new int[] {-1, -1};
		this.isPlayerOnesTurn = true;
		this.playerOneLegalMoves = new HashMap<>();
		this.playerTwoLegalMoves = new HashMap<>();
		this.playerOneLegalCaptures = new HashMap<>();
		this.playerTwoLegalCaptures = new HashMap<>();
		
		setUpDefaultBoardState();
		
		currInstance.setPlayerOneLegalCaptures(playerOneLegalCaptures);
		currInstance.setPlayerTwoLegalCaptures(playerTwoLegalCaptures);
		currInstance.setIsPlayerOnesTurn(isPlayerOnesTurn);
		currInstance.setBoard(board);
		currInstance.setSelectedLegalMoves(selectedLegalMoves);
		currInstance.setSelectedIndex(selectedIndex);
		currInstance.setMovedIndex(movedIndex);
		
	}
	
	/**
	 * 
	 */
	private void setUpDefaultBoardState() {
		
		// Add player one's pieces to the board
		for (int i = BOARD_SIZE - 3; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				if ((i + j) % 2 == 1) {
					board[i][j] = 1;
					addPiece(i, j, true);
				}
			}
		}
		// Add player two's pieces to the board 
		for (int i = 2; i >= 0; i--) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				if ((i + j) % 2 == 1) {
					board[i][j] = -1;
					addPiece(i, j, false);
				}
			}
		}
		
		currInstance.setPlayerOneLegalMoves(playerOneLegalMoves);
		currInstance.setPlayerTwoLegalMoves(playerTwoLegalMoves);
	}
	
	/**
	 * 
	 * @param rowNum
	 * @param colNum
	 * @param pieceIsOwnedByPlayerOne
	 */
	private void addPiece(int rowNum, int colNum, boolean pieceIsOwnedByPlayerOne) {
		ArrayList<Integer> keyVal = new ArrayList<>();
		keyVal.add(rowNum);
		keyVal.add(colNum);
		if (pieceIsOwnedByPlayerOne) {
			playerOneLegalMoves.put(keyVal, getLegalMovesForPiece(rowNum, colNum, pieceIsOwnedByPlayerOne));
		} else {
			playerTwoLegalMoves.put(keyVal, getLegalMovesForPiece(rowNum, colNum, pieceIsOwnedByPlayerOne));
		}
		
	}
	
	/**
	 * 
	 * @param sourceRow
	 * @param sourceCol
	 * @param targetRow
	 * @param targetColumn
	 * @param pieceIsOwnedByPlayerOne
	 */
	private void addPieceToLegalCaptures(int sourceRow, int sourceCol, int targetRow,
			int targetColumn, boolean pieceIsOwnedByPlayerOne) {
		HashMap<ArrayList<Integer>, ArrayList<int[]>> legalCaptures;
		if (pieceIsOwnedByPlayerOne) {
			legalCaptures = playerOneLegalCaptures;
		} else {
			legalCaptures = playerTwoLegalCaptures;
		}
		ArrayList<Integer> sourceVal = new ArrayList<>();
		sourceVal.add(sourceRow);
		sourceVal.add(sourceCol);
		
		
		int[] legalCapture = new int[] {targetRow, targetColumn};
		if (legalCaptures.containsKey(sourceVal)) {
			legalCaptures.get(sourceVal).add(legalCapture);
		} else {
			ArrayList<int[]> pieceLegalCapture = new ArrayList<>();
			pieceLegalCapture.add(legalCapture);
			legalCaptures.put(sourceVal, pieceLegalCapture);
		}
		if (pieceIsOwnedByPlayerOne) {
			currInstance.setPlayerOneLegalCaptures(legalCaptures);
		} else {
			currInstance.setPlayerTwoLegalCaptures(legalCaptures);
		}
	}
	
	/**
	 * 
	 * @param row
	 * @param col
	 * @param inputCameFromPlayer1
	 * @return
	 */
	private int[][] getLegalMovesForPiece(int row, int col, boolean inputCameFromPlayer1){
		int scalar;
		if (inputCameFromPlayer1) {
			scalar = 1;
		} else {
			scalar = -1;
		}
		int[][] moveIndices = new int[4][2];
		// Set the selected field to the location of the selected board element
		int selected = board[row][col];
		// Set the first index to be the output of the forward left possible movement
		moveIndices[0] = checkValidJump(row, col, inputCameFromPlayer1, scalar, 1, 1);
		// Set the first index to be the output of the forward right possible movement
		moveIndices[1] = checkValidJump(row, col, inputCameFromPlayer1, scalar, -1, 1);
		
		// Checkers pieces can only move backwards if they are promoted
		if ((selected == 2 && inputCameFromPlayer1) || (selected == -2 && !inputCameFromPlayer1)) {
			moveIndices[2] = checkValidJump(row, col, inputCameFromPlayer1, scalar, 1, -1);
			moveIndices[3] = checkValidJump(row, col, inputCameFromPlayer1, scalar, -1, -1);
		} else {
			// If the piece isn't promoted, it doesn't have any backwards moves
			moveIndices[2][0] = -1;
			moveIndices[2][1] = -1;
			moveIndices[3][0] = -1;
			moveIndices[3][1] = -1;
		}
		return moveIndices;
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
						addPieceToLegalCaptures(rowNum, colNum, checkedRow, checkedColumn, inputCameFromPlayer1);
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
	 * 
	 * @param xPos
	 * @param yPos
	 * @param isPlayerOne
	 * @return a boolean determining if a player's turn is over. It should be true
	 * if the player either moved a piece without taking another piece or took a piece
	 * and the same piece at that same location does not have the ability to take another
	 * piece. It should be false if the input did nothing, if the input selected a piece
	 * to move, or if the selected piece took an opponent's piece and has the ability to
	 * take another.
	 */
 	public void takeInput(int xPos, int yPos) {
 		HashMap<ArrayList<Integer>, ArrayList<int[]>> currPlayerLegalCaptures;
		if (isPlayerOnesTurn) {
			currPlayerLegalCaptures = playerOneLegalCaptures;
		} else {
			currPlayerLegalCaptures = playerTwoLegalCaptures;
		}
 		processInput(xPos, yPos, currPlayerLegalCaptures);
	}
 	
 	
 	private void processInput(int xPos, int yPos, HashMap<ArrayList<Integer>, ArrayList<int[]>> legalCaptures) {
 		if (isOwnedPiece(xPos, yPos, isPlayerOnesTurn)) {
			/*
			 * We want to select all possible locations it can travel to and deselect all
			 *  others
			 */
			clearSelectedPossibleMoves();
			computeLegalMovesForPiece(xPos, yPos, isPlayerOnesTurn, legalCaptures);
			setChanged();
			notifyObservers(currInstance);
			
		} else if (isLegalMoveForSelectedPiece(xPos, yPos, isPlayerOnesTurn)) {
			/*
			 * We want to move the selected piece from the current location to the selected
			 * location. If this involves jumping over an enemy piece, then that enemy piece
			 * must also be removed from the board. Then, if the piece still has potential
			 * jumps, restrict the number of legal moves to only those and ignore everything
			 * else
			 */
			clearSelectedPossibleMoves();
			boolean capturedAPiece = moveSelected(xPos, yPos, isPlayerOnesTurn);
			if (capturedAPiece && pieceCanCaptureAnother()) {
				removeNonMovedPieceLegalCaptures();
			} else {
				swapPlayer();
				
			}
			
			setChanged();
			notifyObservers(currInstance);
			
			// Reset the instance's stored selected and moved indices
			resetMovedAndSelected();
		}
 	}
	
	/**
	 * 
	 * @param rowNum
	 * @param colNum
	 * @param inputCameFromPlayer1
	 */
	private void computeLegalMovesForPiece(int rowNum, int colNum, 
			boolean inputCameFromPlayer1, HashMap<ArrayList<Integer>, ArrayList<int[]>> legalCaptures) {
		// Clear out the previously selected board elements
		int currBoardVal = board[rowNum][colNum];
		if ((currBoardVal != 1 && currBoardVal != 2 && inputCameFromPlayer1) ||
				(currBoardVal != -1 && currBoardVal != -2 && !inputCameFromPlayer1)) {
			throw new IllegalStateException("This method should only be run if the position has a piece");
		}
		
		this.selectedIndex[0] = rowNum;
		this.selectedIndex[1] = colNum;
		
		ArrayList<Integer> selectedKey = new ArrayList<>();
		selectedKey.add(rowNum);
		selectedKey.add(colNum);
		if (inputCameFromPlayer1) {
			this.selectedLegalMoves = playerOneLegalMoves.get(selectedKey);
		} else {
			this.selectedLegalMoves = playerTwoLegalMoves.get(selectedKey);
		}
		
		filterLegalMoves(selectedKey, legalCaptures);
		
		currInstance.setSelectedLegalMoves(selectedLegalMoves);
		setSelectedPotentialMovesToCorrectScore(inputCameFromPlayer1);
		currInstance.setSelectedLegalMoves(selectedLegalMoves);
	}
 	
	/**
	 * 
	 * @param selectedKey
	 * @param legalCaptures
	 */
	private void filterLegalMoves(ArrayList<Integer> selectedKey, 
			HashMap<ArrayList<Integer>, ArrayList<int[]>> legalCaptures) {
		// If legalCaptures is empty, then no filtering is needed
		if (!legalCaptures.isEmpty()) {
			// Check if the selected piece can capture something
			if (legalCaptures.containsKey(selectedKey)) {
				boolean canInsert;
				// Filter the legal moves to only allow legal captures through
				ArrayList<int[]> currSquareLegalCaptures = legalCaptures.get(selectedKey);
				for (int[] potentiallyLegalMove: selectedLegalMoves) {
					canInsert = false;
					for (int[] squareLegalCapture: currSquareLegalCaptures) {
						if (Arrays.equals(potentiallyLegalMove, squareLegalCapture)) {
							canInsert = true;
						}
					}
					// If a move isn't a legal capture, remove it from the possible choices.
					if (!canInsert) {
						potentiallyLegalMove[0] = -1;
						potentiallyLegalMove[1] = -1;
					}
				}
			} else {
				// Since the piece cannot capture and there are legal captures, don't let it move
				for (int[] currSelected: selectedLegalMoves) {
					currSelected[0] = -1;
					currSelected[1] = -1;
				}
			}
		}
	}
 	
 	/**
 	 * 
 	 */
	private void swapPlayer() {
		this.isPlayerOnesTurn = !this.isPlayerOnesTurn;
		currInstance.setIsPlayerOnesTurn(!currInstance.isPlayerOnesTurn());
		
	}
	
	/**
	 * Returns whether the grid element at index rowNum/colNum contains a piece
	 * that is owned by the player taking the input.
	 * @param rowNum
	 * @param colNum
	 * @param inputCameFromPlayer1
	 * @return
	 */
	private boolean isOwnedPiece(int rowNum, int colNum, boolean inputCameFromPlayer1) {
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
	private boolean isLegalMoveForSelectedPiece(int rowNum, int colNum, boolean inputCameFromPlayer1) {
		int selectedSquare = board[rowNum][colNum];
		// Next check all possible states that would return true
		if ((selectedSquare == 3 && inputCameFromPlayer1) || 
				(selectedSquare == -3 && !inputCameFromPlayer1)){
			return true;
		} else {
			// If this is not one of the valid states, return false
			return false;
		}
	}
	
	/**
	 * 
	 */
	private void clearSelectedPossibleMoves() {
		for (int[] currSelected: selectedLegalMoves) {
			int currRowVal = currSelected[0];
			int currColVal = currSelected[1];
			if (currRowVal != -1 && currColVal != -1) {
				this.board[currRowVal][currColVal] = 0;
			}
		}
		currInstance.setBoard(board);
	}
	
	
	/**
	 * 
	 * @param pieceScore
	 * @param inputCameFromPlayer1
	 * @return
	 */
	private boolean isPieceControlledByAlly(int pieceScore, boolean inputCameFromPlayer1) {
		if ((pieceScore == 1 || pieceScore == 2) && inputCameFromPlayer1) {
			return true;
		} else if ((pieceScore == -1 || pieceScore == -2) && !inputCameFromPlayer1) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Set the score of the potential moves for the selected piece to be 3 or
	 * -3 depending on the player who gave the input.
	 * @param inputCameFromPlayer1
	 */
	private void setSelectedPotentialMovesToCorrectScore(boolean inputCameFromPlayer1) {
		for (int[] currSelected: this.selectedLegalMoves) {
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
		currInstance.setBoard(board);
	}
	
	/**
	 * 
	 * @param rowNum
	 * @param colNum
	 * @param inputCameFromPlayer1
	 */
	public boolean moveSelected(int rowNum, int colNum, boolean inputCameFromPlayer1) {
		boolean capturedAPiece = false;
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
		removePiece(selectedIndex[0], selectedIndex[1], inputCameFromPlayer1);
		// If the piece moved more than one tile horizontally, it jumped a piece
		if (Math.abs(selectedIndex[0] - rowNum) > 1) {
			// Bit of a trick, but piece jumped is in the middle of prev and new location
			int skippedRowVal = (selectedIndex[0] + rowNum) / 2;
			int skippedColVal = (selectedIndex[1] + colNum) / 2;
			removePiece(skippedRowVal, skippedColVal, !inputCameFromPlayer1);
			board[skippedRowVal][skippedColVal] = 0;
			capturedAPiece = true;
		}
		addPiece(rowNum, colNum, inputCameFromPlayer1);
		currInstance.setBoard(board);
		// Assign the moved values if I ever get around to making animations
		this.movedIndex[0] = rowNum;
		this.movedIndex[1] = colNum;
		currInstance.setSelectedIndex(selectedIndex);
		currInstance.setMovedIndex(movedIndex);
		updateBoardLegalMoves();
		return capturedAPiece;
	}
	
	/**
	 * 
	 * @param rowNum
	 * @param colNum
	 * @param pieceIsOwnedByPlayerOne
	 */
	private void removePiece(int rowNum, int colNum, boolean pieceIsOwnedByPlayerOne) {
		ArrayList<Integer> keyVal = new ArrayList<>();
		keyVal.add(rowNum);
		keyVal.add(colNum);
		if (pieceIsOwnedByPlayerOne) {
			playerOneLegalMoves.remove(keyVal);
			currInstance.setPlayerOneLegalMoves(playerOneLegalMoves);
		} else {
			playerTwoLegalMoves.remove(keyVal);
			currInstance.setPlayerTwoLegalMoves(playerTwoLegalMoves);
		}
	}
	
	/**
	 * This method works on the principle that the only possible pieces that could
	 * possibly change their legal moves besides the moved piece are those that
	 * immediately border the location the moved piece initially was, the location
	 * the moved piece moved to, and the location of the piece that was taken, if
	 * one was.
	 */
	private void updateBoardLegalMoves() {
		clearSelectedCaptures();
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				int boardVal = board[i][j];
				if (boardVal == 1 || boardVal == 2) {
					removePiece(i, j, true);
					addPiece(i, j, true);
				} else if (boardVal == -1 || boardVal == -2) {
					removePiece(i, j, false);
					addPiece(i, j, false);
				}
			}
		}
		currInstance.setPlayerOneLegalMoves(playerOneLegalMoves);
		currInstance.setPlayerTwoLegalMoves(playerTwoLegalMoves);
	}
	
	
	/**
	 * 
	 */
	private void resetMovedAndSelected() {
		this.selectedIndex[0] = -1;
		this.selectedIndex[1] = -1;
		this.movedIndex[0] = -1;
		this.movedIndex[1] = -1;
		for (int[] currSelected: selectedLegalMoves) {
			currSelected[0] = -1;
			currSelected[1] = -1;
		}
		currInstance.setSelectedIndex(selectedIndex);
		currInstance.setMovedIndex(movedIndex);
		currInstance.setSelectedLegalMoves(selectedLegalMoves);
	}
	
	/**
	 * 
	 */
	private void clearSelectedCaptures() {
		playerOneLegalCaptures.clear();
		playerTwoLegalCaptures.clear();
	}
	
	
	private void removeNonMovedPieceLegalCaptures() {
		ArrayList<Integer> keyVal = new ArrayList<>();
		keyVal.add(movedIndex[0]);
		keyVal.add(movedIndex[1]);
		HashMap<ArrayList<Integer>, ArrayList<int[]>> currPlayerLegalCaptures = new HashMap<>();
		if (isPlayerOnesTurn) {
			currPlayerLegalCaptures = playerOneLegalCaptures;
		} else {
			currPlayerLegalCaptures =  playerTwoLegalCaptures;
		}
		ArrayList<int[]> savedVal = currPlayerLegalCaptures.get(keyVal);
		currPlayerLegalCaptures.clear();
		currPlayerLegalCaptures.put(keyVal, savedVal);
	}
	
	
	/**
	 * 
	 * @return
	 */
	private boolean pieceCanCaptureAnother() {
		ArrayList<Integer> keyVal = new ArrayList<>();
		keyVal.add(movedIndex[0]);
		keyVal.add(movedIndex[1]);
		if (isPlayerOnesTurn) {
			return playerOneLegalCaptures.containsKey(keyVal);
		} else {
			return playerTwoLegalCaptures.containsKey(keyVal);
		}
	}
	
	public HashMap<ArrayList<Integer>, int[][]> getPlayerOneLegalMoves(){
		return playerOneLegalMoves;
	}
	
	public HashMap<ArrayList<Integer>, int[][]> getPlayerTwoLegalMoves(){
		return playerTwoLegalMoves;
	}
}







