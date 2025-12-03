package checkers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class CheckersModel extends Observable {

	private static final int BOARD_SIZE = 8;
	private CheckersInstance currInstance;

	public CheckersModel(CheckersView view, ObjectInputStream input) {
		addObserver(view);
		if (input != null) {
			try {
				currInstance = (CheckersInstance) input.readObject();
			} catch (ClassNotFoundException | IOException e) {
				currInstance = new CheckersInstance();
			}
		} else {
			currInstance = new CheckersInstance();
		}
		setChanged();
		notifyObservers(currInstance);
	}

	public CheckersModel(CheckersView view) {
		addObserver(view);
		currInstance = new CheckersInstance();
		setChanged();
		notifyObservers(currInstance);
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
	public boolean takeInput(int xPos, int yPos, boolean isPlayerOne) {
		boolean shouldSwitch;
		if (currInstance.isOwnedPiece(xPos, yPos, isPlayerOne)) {
			
			/*
			 * We want to select all possible locations it can travel to and deselect all
			 *  others
			 */
			currInstance.selectPossiblePieceMoves(xPos, yPos, isPlayerOne);
			
			setChanged();
			notifyObservers(currInstance);
			shouldSwitch = false;

		} else if (currInstance.isPossibleMoveForSelectedPiece(xPos, yPos, isPlayerOne)) {
			/*
			 * We want to move the selected piece from the current location to the selected
			 * location. If this involves jumping over an enemy piece, then that enemy piece
			 * must also be removed from the board. Then, if the piece still has potential
			 * jumps, restrict the number of legal moves to only those and ignore everything
			 * else
			 */
			currInstance.clearSelectedPossibleMoves();
			currInstance.moveSelected(xPos, yPos, isPlayerOne);
			setChanged();
			notifyObservers(currInstance);
			// Reset the instance's stored selected and moved indices
			currInstance.resetMovedAndSelected();
			
			shouldSwitch = true;
		} else {
			//System.out.println("Player did not own that tile");
			shouldSwitch = false;
		}
		return shouldSwitch;
	}
}







