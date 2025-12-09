/**
 * This class implements the Model portion of the MVC design archetype. It
 * does virtually all of the logic for the game and is honestly pretty annoying to 
 * parse despite my best efforts to keep everything organized.
 */

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
	 * Constructs an instance of the CheckersModel class.
	 * 
	 * @param view:    The CheckersView object observing this instance
	 * @param input:   The saved game
	 * @param player1: The name of player 1
	 * @param player2: The name of player 2
	 */
	public CheckersModel(CheckersView view, ObjectInputStream input, String player1, String player2) {
		// This is here for testing purposes. The view should never actually be null
		if (view != null) {
			addObserver(view);
		}

		if (input != null) {
			try {
				// If there is a save, load all the values back from it
				currInstance = (CheckersInstance) input.readObject();
				this.board = currInstance.getBoard();
				this.selectedLegalMoves = currInstance.getSelectedLegalMoves();
				this.selectedIndex = currInstance.getSelectedIndex();
				this.movedIndex = currInstance.getMovedIndex();
				this.isPlayerOnesTurn = currInstance.isPlayerOnesTurn();
				this.playerOneLegalMoves = currInstance.getPlayerOneLegalMoves();
				this.playerTwoLegalMoves = currInstance.getPlayerTwoLegalMoves();
				this.playerOneLegalCaptures = currInstance.getPlayerOneLegalCaptures();
				this.playerTwoLegalCaptures = currInstance.getPlayerTwoLegalCaptures();
			} catch (ClassNotFoundException | IOException e) {
				// If something happens, just pretend like nothing's wrong and start default
				setUpDefault(player1, player2);
			}
		} else {
			// If there is not a save, start a default game.
			setUpDefault(player1, player2);
		}
		// Since stuff happened, notify the observers
		setChanged();
		notifyObservers(currInstance);

	}

	/**
	 * Sets up a default board. You know what a checkers game looks like. It's that
	 * 
	 * @param player1: The name of player 1
	 * @param player2: The name of player 2
	 */
	private void setUpDefault(String player1, String player2) {
		this.currInstance = new CheckersInstance(player1, player2);
		// Initialize all the values to their default values
		this.board = new int[BOARD_SIZE][BOARD_SIZE];
		this.selectedLegalMoves = new int[][] { { -1, -1 }, { -1, -1 }, { -1, -1 }, { -1, -1 } };
		this.selectedIndex = new int[] { -1, -1 };
		this.movedIndex = new int[] { -1, -1 };
		this.isPlayerOnesTurn = true;
		this.playerOneLegalMoves = new HashMap<>();
		this.playerTwoLegalMoves = new HashMap<>();
		this.playerOneLegalCaptures = new HashMap<>();
		this.playerTwoLegalCaptures = new HashMap<>();
		// Set up the board in its default state
		setUpDefaultBoardState();
		// Pass all these default values to the instance.
		currInstance.setPlayerOneLegalMoves(playerOneLegalMoves);
		currInstance.setPlayerTwoLegalMoves(playerTwoLegalMoves);
		currInstance.setPlayerOneLegalCaptures(playerOneLegalCaptures);
		currInstance.setPlayerTwoLegalCaptures(playerTwoLegalCaptures);
		currInstance.setIsPlayerOnesTurn(isPlayerOnesTurn);
		currInstance.setBoard(board);
		currInstance.setSelectedLegalMoves(selectedLegalMoves);
		currInstance.setSelectedIndex(selectedIndex);
		currInstance.setMovedIndex(movedIndex);

	}

	/**
	 * Sets all the values in the board to 0 except where the pieces should be on a
	 * default starting checkers board, whic are set to 1 for player 1 pieces and -1
	 * for player 2 pieces respectively.
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
	 * adds a piece to the board and initialize all its legal moves from there,
	 * which is then added to that player's map of legal moves.
	 * 
	 * @param rowNum:                  The row value this piece should be placed in.
	 * @param colNum:                  The column value this piece should be placed
	 *                                 in.
	 * @param pieceIsOwnedByPlayerOne: A boolean determining which player owns the
	 *                                 piece being added.
	 */
	private void addPiece(int rowNum, int colNum, boolean pieceIsOwnedByPlayerOne) {
		ArrayList<Integer> keyVal = new ArrayList<>();
		keyVal.add(rowNum);
		keyVal.add(colNum);
		// Add to the legal moves map corresponding to which player the piece belongs to
		if (pieceIsOwnedByPlayerOne) {
			playerOneLegalMoves.put(keyVal, getLegalMovesForPiece(rowNum, colNum, pieceIsOwnedByPlayerOne));
		} else {
			playerTwoLegalMoves.put(keyVal, getLegalMovesForPiece(rowNum, colNum, pieceIsOwnedByPlayerOne));
		}

	}

	/**
	 * Adds a piece move to the map of legal captures for the player that owns the
	 * piece. Legal captures take priority over other moves, so it needs to be
	 * computed distinctly
	 * 
	 * @param sourceRow:               The row the piece started in.
	 * @param sourceCol:               The column the piece started in.
	 * @param targetRow:               The row of the piece being taken
	 * @param targetColumn:            The column of the piece being taken.
	 * @param pieceIsOwnedByPlayerOne: A boolean determining which player is taking
	 *                                 the piece
	 */
	private void addPieceToLegalCaptures(int sourceRow, int sourceCol, int targetRow, int targetColumn,
			boolean pieceIsOwnedByPlayerOne) {
		// Get the correct player's legal captures map
		HashMap<ArrayList<Integer>, ArrayList<int[]>> legalCaptures;
		if (pieceIsOwnedByPlayerOne) {
			legalCaptures = playerOneLegalCaptures;
		} else {
			legalCaptures = playerTwoLegalCaptures;
		}
		// Add the source location to the key value and the target location to the value
		ArrayList<Integer> sourceVal = new ArrayList<>();
		sourceVal.add(sourceRow);
		sourceVal.add(sourceCol);
		int[] legalCapture = new int[] { targetRow, targetColumn };
		// Add the move to the map.
		if (legalCaptures.containsKey(sourceVal)) {
			legalCaptures.get(sourceVal).add(legalCapture);
		} else {
			ArrayList<int[]> pieceLegalCapture = new ArrayList<>();
			pieceLegalCapture.add(legalCapture);
			legalCaptures.put(sourceVal, pieceLegalCapture);
		}
		// Notify the instance that something has been updated
		if (pieceIsOwnedByPlayerOne) {
			currInstance.setPlayerOneLegalCaptures(legalCaptures);
		} else {
			currInstance.setPlayerTwoLegalCaptures(legalCaptures);
		}
	}

	/**
	 * Computes the legal moves for a given piece.
	 * 
	 * @param row:                  the row location of the piece.
	 * @param col:                  the column location of the piece.
	 * @param inputCameFromPlayer1: A boolean determining which player owns the
	 *                              piece. Technically unnecessary but I like it.
	 * @return A 2d array of size [4][2], representing all the possible legal moves
	 *         a piece can perform. Since arrays are fixed size, if a piece has only
	 *         <=3 moves, then some of the arrays of size two will look like [-1,
	 *         -1] to represent a null pointer.
	 */
	private int[][] getLegalMovesForPiece(int row, int col, boolean inputCameFromPlayer1) {
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
	 * Checks whether a move is legal or not. This method is annoying to read, so
	 * here is the criteria: 1) If the location is out of bounds, return the null
	 * array 2) If the location is free, return that index since it's a valid move
	 * 3) If the location contains the player's piece, return the null array 4) If
	 * the location contains the opponent's piece, check the piece after it. If it
	 * is empty, return that index and add the move to legal jumps. If it is not
	 * empty, regardless of the kind of piece, return the null array.
	 * 
	 * @param rowNum:               The row of the piece whose valid moves are being
	 *                              checked
	 * @param colNum:               The column of the piece whose valid moves are
	 *                              being checked
	 * @param inputCameFromPlayer1: A boolean determining which player owns the
	 *                              piece. Not necessary, but helpful.
	 * @param scalar:               Either 1 or -1 depending on if you're player 1
	 *                              or player 2, since they move up and down the
	 *                              board.
	 * @param leftRightScalar:      Either 1 or -1 depending on whether the move the
	 *                              method is testing for goes right or left.
	 * @param upDownScalar:         Either 1 or -1 depending on whether the move the
	 *                              method is testing for goes up or down. Only
	 *                              applicable for promoted pieces
	 * @return The location of the valid move or the null array.
	 */
	private int[] checkValidJump(int rowNum, int colNum, boolean inputCameFromPlayer1, int scalar, int leftRightScalar,
			int upDownScalar) {

		// Get the row and column index currently being searched for
		int checkedRow = rowNum - 1 * scalar * upDownScalar;
		int checkedColumn = colNum + 1 * scalar * leftRightScalar;

		// Check whether the row and column being checked are within the board
		if (checkedRow >= 0 && checkedRow < BOARD_SIZE && checkedColumn < BOARD_SIZE && checkedColumn >= 0) {
			int selectedSquare = board[checkedRow][checkedColumn];
			if (selectedSquare == 0) {
				// If the selected square is empty, it is a valid location to move to
				return new int[] { checkedRow, checkedColumn };
			}
			// If the selected square has an enemy piece in it, it could be jumped over
			else if (!isPieceControlledByAlly(selectedSquare, inputCameFromPlayer1)) {
				// Compute the row/column position of the potential jumping location
				checkedRow = rowNum - 2 * scalar * upDownScalar;
				checkedColumn = colNum + 2 * scalar * leftRightScalar;
				// Check if the row and column being checked are within the board
				if (checkedRow >= 0 && checkedRow < BOARD_SIZE && checkedColumn < BOARD_SIZE && checkedColumn >= 0) {
					// Check if the location the piece would jump to is within the board
					selectedSquare = board[checkedRow][checkedColumn];
					if (selectedSquare == 0) {
						// If the jumped node is empty, it is a valid location to move to
						addPieceToLegalCaptures(rowNum, colNum, checkedRow, checkedColumn, inputCameFromPlayer1);
						return new int[] { checkedRow, checkedColumn };
					} else {
						// If not, then you can't jump, so no legal move here
						return new int[] { -1, -1 };
					}
				} else {
					// If the jumped node is not within the board, return null
					return new int[] { -1, -1 };
				}
			} else {
				// If the checked position has one of the player's pieces in it, return null
				return new int[] { -1, -1 };
			}
		} else {
			// If the checked row and column are not within the board, return null
			return new int[] { -1, -1 };
		}
	}

	/**
	 * Take input from the user and pass it along to be processed.
	 * @param xPos: The x position on the board the player clicked
	 * @param yPos: The y position on the board the player clicked
	 */
	public void takeInput(int xPos, int yPos) {
		// Compute which legal captures map we should be working with.
		HashMap<ArrayList<Integer>, ArrayList<int[]>> currPlayerLegalCaptures;
		if (isPlayerOnesTurn) {
			currPlayerLegalCaptures = playerOneLegalCaptures;
		} else {
			currPlayerLegalCaptures = playerTwoLegalCaptures;
		}
		processInput(xPos, yPos, currPlayerLegalCaptures);
	}

	/**
	 * Processes the input, determining if it is legal, if it is clicking a piece,
	 * if it is telling where to move a piece, or is it does absolutely nothing. It
	 * also handles repeated jumping.
	 * @param xPos: The x position on the board the player clicked
	 * @param yPos: The y position on the board the player clicked
	 * @param legalCaptures: The map of legal captures the player making the input has
	 */
	private void processInput(int xPos, int yPos, HashMap<ArrayList<Integer>, ArrayList<int[]>> legalCaptures) {
		if (isOwnedPiece(xPos, yPos, isPlayerOnesTurn)) {
			// Deselect all other legal moves
			clearSelectedPossibleMoves();
			// Select the legal moves for this piece
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
			// Deselect all legal moves since one was chosen.
			clearSelectedPossibleMoves();
			// Move and determine if a piece was captured in the move
			boolean capturedAPiece = moveSelected(xPos, yPos, isPlayerOnesTurn);
			if (capturedAPiece && pieceCanCaptureAnother()) {
				removeNonMovedPieceLegalCaptures();
			} else {
				// Only swap a piece when the capturing streak is done.
				swapPlayer();
			}

			setChanged();
			notifyObservers(currInstance);

			// Reset the instance's stored selected and moved indices
			resetMovedAndSelected();
		}
	}

	/**
	 * Computes the legal moves a piece can have when filtered by the legal captures map
	 * @param rowNum: The row value of the input
	 * @param colNum: The col value of the input
	 * @param inputCameFromPlayer1: which player is doing the input
	 * @param legalCaptures: A map showing all legal captures.
	 */
	private void computeLegalMovesForPiece(int rowNum, int colNum, boolean inputCameFromPlayer1,
			HashMap<ArrayList<Integer>, ArrayList<int[]>> legalCaptures) {
		// Clear out the previously selected board elements
		int currBoardVal = board[rowNum][colNum];
		if ((currBoardVal != 1 && currBoardVal != 2 && inputCameFromPlayer1)
				|| (currBoardVal != -1 && currBoardVal != -2 && !inputCameFromPlayer1)) {
			throw new IllegalStateException("This method should only be run if the position has a piece");
		}

		this.selectedIndex[0] = rowNum;
		this.selectedIndex[1] = colNum;

		// Get the legal moves from our computed map.
		ArrayList<Integer> selectedKey = new ArrayList<>();
		selectedKey.add(rowNum);
		selectedKey.add(colNum);
		if (inputCameFromPlayer1) {
			this.selectedLegalMoves = playerOneLegalMoves.get(selectedKey);
		} else {
			this.selectedLegalMoves = playerTwoLegalMoves.get(selectedKey);
		}

		// Filter legal moves to disable non-captures when a capture is available
		filterLegalMoves(selectedKey, legalCaptures);

		// Update currInstance since stuff's being modified
		currInstance.setSelectedLegalMoves(selectedLegalMoves);
		setSelectedPotentialMovesToCorrectScore(inputCameFromPlayer1);
		currInstance.setSelectedLegalMoves(selectedLegalMoves);
	}

	/**
	 * This method's annoying so here's rundown:
	 * 1) If legalCaptures is empty, then no filtering is needed.
	 * 2) If legalCaptures is not empty, check if our piece is one of the captures.
	 * If it is, then keep it. If not, replace it with the null array.
	 * @param selectedKey: The row/col position being filtered.
	 * @param legalCaptures: The map of captures acting as a filter.
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
				for (int[] potentiallyLegalMove : selectedLegalMoves) {
					canInsert = false;
					for (int[] squareLegalCapture : currSquareLegalCaptures) {
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
				// Since the piece cannot capture and there are legal captures, don't let it
				// move
				for (int[] currSelected : selectedLegalMoves) {
					currSelected[0] = -1;
					currSelected[1] = -1;
				}
			}
		}
	}

	/**
	 * Swaps the player
	 */
	private void swapPlayer() {
		this.isPlayerOnesTurn = !this.isPlayerOnesTurn;
		currInstance.setIsPlayerOnesTurn(!currInstance.isPlayerOnesTurn());

	}

	/**
	 * Returns whether the grid element at index rowNum/colNum contains a piece that
	 * is owned by the player taking the input.
	 * 
	 * @param rowNum: The row value of the input
	 * @param colNum: The col value of the input
	 * @param inputCameFromPlayer1: which player is doing the input
	 * @return whether the piece at that square is owned by the player giving input
	 */
	private boolean isOwnedPiece(int rowNum, int colNum, boolean inputCameFromPlayer1) {
		int selectedSquare = board[rowNum][colNum];
		// First check whether the square contains a piece or not
		if (selectedSquare == 0 || selectedSquare == 3 || selectedSquare == -3) {
			return false;
		}
		// Next check all possible states that would return true
		else if ((selectedSquare > 0 && inputCameFromPlayer1) || (selectedSquare < 0 && !inputCameFromPlayer1)) {
			return true;
		} else {
			// If this is not one of the valid states, return false
			return false;
		}
	}

	/**
	 * Returns whether the grid element at index rowNum/colNum contains a valid
	 * location for the piece currently selected to move to, as well as the input
	 * coming from the user who owns the square being selected.
	 * 
	 * @param rowNum: The row value of the input
	 * @param colNum: The col value of the input
	 * @param inputCameFromPlayer1: which player is doing the input
	 * @return whether the piece is a legal move for the piece in terms of the board
	 * values.
	 */
	private boolean isLegalMoveForSelectedPiece(int rowNum, int colNum, boolean inputCameFromPlayer1) {
		int selectedSquare = board[rowNum][colNum];
		// Next check all possible states that would return true
		if ((selectedSquare == 3 && inputCameFromPlayer1) || (selectedSquare == -3 && !inputCameFromPlayer1)) {
			return true;
		} else {
			// If this is not one of the valid states, return false
			return false;
		}
	}

	/**
	 * Erases all legal move markers, which are values of 3/-3, on the map, 
	 * and sets all arrays in selectedLegalMoves to the null array.
	 */
	private void clearSelectedPossibleMoves() {
		for (int[] currSelected : selectedLegalMoves) {
			int currRowVal = currSelected[0];
			int currColVal = currSelected[1];
			if (currRowVal != -1 && currColVal != -1) {
				this.board[currRowVal][currColVal] = 0;
			}
		}
		currInstance.setBoard(board);
	}

	/**
	 * Checks if the piece is controlled by the player giving input
	 * @param pieceScore: the value of the piece
	 * @param inputCameFromPlayer1: the player giving input
	 * @return whether the piece is controlled by the player giving input
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
	 * Set the score of the potential moves for the selected piece to be 3 or -3
	 * depending on the player who gave the input.
	 * 
	 * @param inputCameFromPlayer1: The player giving the input
	 */
	private void setSelectedPotentialMovesToCorrectScore(boolean inputCameFromPlayer1) {
		for (int[] currSelected : this.selectedLegalMoves) {
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
	 * Moves the piece from from the value selectedIndex to rowNum to colNum.
	 * @param rowNum: The row value of the input
	 * @param colNum: The col value of the input
	 * @param inputCameFromPlayer1: which player is doing the input
	 * @return: whether the piece captured another
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
	 * Removes the piece from the board.
	 * @param rowNum: The row value of the input
	 * @param colNum: The col value of the input
	 * @param inputCameFromPlayer1: which player is doing the input
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
	 * Resets the movedIndex and selectedIndex fields.
	 */
	private void resetMovedAndSelected() {
		this.selectedIndex[0] = -1;
		this.selectedIndex[1] = -1;
		this.movedIndex[0] = -1;
		this.movedIndex[1] = -1;
		for (int[] currSelected : selectedLegalMoves) {
			currSelected[0] = -1;
			currSelected[1] = -1;
		}
		currInstance.setSelectedIndex(selectedIndex);
		currInstance.setMovedIndex(movedIndex);
		currInstance.setSelectedLegalMoves(selectedLegalMoves);
	}

	/**
	 * Clears the selected captures maps for player 1 and player 2.
	 */
	private void clearSelectedCaptures() {
		playerOneLegalCaptures.clear();
		playerTwoLegalCaptures.clear();
	}

	/**
	 * Remove all legal captures that don't come from the piece that was being moved,
	 * which basically prevents any other moves from occuring besides the chain capturing.
	 */
	private void removeNonMovedPieceLegalCaptures() {
		ArrayList<Integer> keyVal = new ArrayList<>();
		keyVal.add(movedIndex[0]);
		keyVal.add(movedIndex[1]);
		HashMap<ArrayList<Integer>, ArrayList<int[]>> currPlayerLegalCaptures = new HashMap<>();
		if (isPlayerOnesTurn) {
			currPlayerLegalCaptures = playerOneLegalCaptures;
		} else {
			currPlayerLegalCaptures = playerTwoLegalCaptures;
		}
		ArrayList<int[]> savedVal = currPlayerLegalCaptures.get(keyVal);
		currPlayerLegalCaptures.clear();
		currPlayerLegalCaptures.put(keyVal, savedVal);
	}

	/**
	 * Checks whether the piece at movedIndex can capture another piece
	 * @return: whether the piece at movedIndex can capture another piece
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

	/**
	 * Gives playerOneLegalMoves
	 * @return playerOneLegalMoves
	 */
	public HashMap<ArrayList<Integer>, int[][]> getPlayerOneLegalMoves() {
		return playerOneLegalMoves;
	}

	/**
	 * Gives playerTwoLegalMoves
	 * @return playerTwoLegalMoves
	 */
	public HashMap<ArrayList<Integer>, int[][]> getPlayerTwoLegalMoves() {
		return playerTwoLegalMoves;
	}

	/**
	 * Gives isPlayerOnesTurn
	 * @return isPlayerOnesTurn
	 */
	public boolean isPlayerOnesTurn() {
		return this.isPlayerOnesTurn;
	}
}
