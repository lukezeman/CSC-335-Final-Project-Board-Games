/**
 * Acts as a StackPane that also allows for placing pieces and legal move indicators.
 * Extends StackPane and effectively just acts like one, just streamlined to fit this
 * specifif case.
* The concept for this was ripped straight from stack overflow by this post:
* https://stackoverflow.com/questions/43761825/checkerboard-inside-a-window-using-javafx.
* It's not exact, but it gave me the idea of extending the StackPane class.
* @author Charlie Cain
*/

package checkers;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class BoardSquare extends StackPane {

	private CheckersPiece currPiece;
	private PotentialSelectedPieceMove isPotentialMove;
	private String backgroundColor;
	private double squareSize;
	
	/**
	 * Constructs an instance of the BoardSquare class and builds a square
	 * with color backgroundColor
	 * @param backgroundColor: The String of form "#xxxxxx" representing the
	 * rgb values the square should be
	 */
	public BoardSquare(String backgroundColor, double squareSize) {
		setMinSize(66, 66);
		setPrefSize(66, 66);
		setMaxSize(66, 66);
		
		
		Rectangle background = new Rectangle(squareSize, squareSize);
		background.setFill(Paint.valueOf(backgroundColor));
		
		getChildren().add(background);
		
		this.backgroundColor = backgroundColor;
		this.squareSize = squareSize;
		this.currPiece = null;
		this.isPotentialMove = null;
	}
	
	/**
	 * Places a checkers piece in this tile, with owner determined by
	 * isOwnedByPlayer1.
	 * @param isOwnedByPlayer1: A boolean determining whether player one
	 * owns the piece
	 */
	public void setCheckersPiece(boolean isOwnedByPlayer1) {
		
		clear();
		
		Pane circleHolder = new Pane();
		circleHolder.setPrefSize(66, 66);
		
		// Construct a new checkers piece to go in the middle of this pane
		this.currPiece = new CheckersPiece(isOwnedByPlayer1);
		Circle checkersCircle = createCircle(isOwnedByPlayer1);
		checkersCircle.setManaged(true);
		checkersCircle.setTranslateX(0);
		checkersCircle.setTranslateY(0);
		getChildren().add(checkersCircle);
		isPotentialMove = null;
	}
	
	/**
	 * Removes any piece from the board and leaves it the default color
	 */
	public void clear() {
		// There's probably a smarter way of doing this, but javaFX is hard :(
		getChildren().clear();
		Rectangle background = new Rectangle(squareSize, squareSize);
		background.setFill(Paint.valueOf(backgroundColor));
		getChildren().add(background);
		
		currPiece = null;
		isPotentialMove = null;
	}
	
	/**
	 * Creates a circle with a 30 pixel radius in the middle of the frame,
	 * with color determined by isOwnedByPlayer1
	 * @param isOwnedByPlayer1: A boolean detemrining if player 1 owns the
	 * piece on this tile
	 * @return The created Circle
	 */
	private Circle createCircle(boolean isOwnedByPlayer1) {
		Circle currPiece = new Circle(30);
		if (isOwnedByPlayer1) {
			currPiece.setFill(Color.WHITESMOKE);
		} else {
			// This is basically a deep brown
			currPiece.setFill(Paint.valueOf("#381d16"));
		}
		return currPiece;
	}
	
	/**
	 * Adds the like tiny black cicle on the square to indicate that the piece selected
	 * can move to this tile.
	 * @param isOwnedByPlayer1: A boolean determining whether the selected piece belongs
	 * to player 1.
	 */
	public void setPotentialMove(boolean isOwnedByPlayer1) {
		// Clear whatever was here currently
		clear();
		currPiece = null;
		// Add the tiny circle to the pane
		isPotentialMove = new PotentialSelectedPieceMove(isOwnedByPlayer1);
		Circle potentialCircle = setUpPotentialCircle();
		getChildren().add(potentialCircle);
	}
	
	/**
	 * Sets up an 8 pixel radius black circle with opacity set to .3
	 * @return Returns the circle
	 */
	private  Circle setUpPotentialCircle() {
		Circle markerSymbol = new Circle(8);
		markerSymbol.setFill(Color.BLACK);
		markerSymbol.setOpacity(.3);
		return markerSymbol;
	}
	
	/**
	 * Checks if this square is selected by the same player as given by inputCameFromPlayer1
	 * @param inputCameFromPlayer1: The player currently trying to move here.
	 * @return whether the player currently trying to move here owns the square.
	 */
	public boolean isPossibleMoveForSelectedPiece(boolean inputCameFromPlayer1) {
		if (isPotentialMove == null) {
			return false;
		} else {
			return isPotentialMove.isOwnerOfSelectedPiecePlayer1() == inputCameFromPlayer1;
		}
	}
	
	/**
	 * Checks if this square is either selected or has a piece owned 
	 * by the same player as given by inputCameFromPlayer1
	 * @param inputCameFromPlayer1: The player currently trying to move here.
	 * @return a boolean determining is this piece is either selected or has a piece owned 
	 * by the same player as given by inputCameFromPlayer1
	 */
	public boolean canBeModifiedBy(boolean inputCameFromPlayer1) {
		if (currPiece == null && isPotentialMove == null) {
			return false;
		} else if (currPiece != null) {
			return currPiece.isOwnedByPlayer1() == inputCameFromPlayer1;
		} else {
			return isPotentialMove.isOwnerOfSelectedPiecePlayer1() == inputCameFromPlayer1;
		}
	}
	
	/**
	 * Checks if this square has a piece owned by the same player as given by
	 * inputCameFromPlayer1.
	 * @return Returns a booleans determining whether this square has a piece 
	 * owned by the same player as given by inputCameFromPlayer1.
	 */
	public boolean isPieceOwnedByPlayer1() {
		if (this.currPiece == null) {
			throw new IllegalStateException("A Piece Must Exist for it to have an Owner");
		} else {
			return currPiece.isOwnedByPlayer1();
		}
	}
	
	/**
	 * Checks if the piece on this square is promoted. Throws an exception if it does
	 * not have a piece on it.
	 * @return A boolean determining whether the piece on this square is promoted
	 */
	public boolean isPiecePromoted() {
		if (this.currPiece == null) {
			throw new IllegalStateException("A Piece Must Exist for it to be Promoted");
		} else {
			return currPiece.isPromoted();
		}
	}
	
	/**
	 * Promotes the piece on this square. Throws an exception if the square doesn't exist.
	 */
	public void promote() {
		if (this.currPiece == null) {
			throw new IllegalStateException("A Piece Must Exist for it to be Promoted");
		}
		if (currPiece.isOwnedByPlayer1()) {
			promoteRedPiece();
		} else {
			promoteBlackPiece();
		}
	}
	
	/**
	 * Adds cool rings to the piece on this pane to indicate that it is now promoted. This
	 * method modifies player 1 pieces with gold rings.
	 */
	private void promoteRedPiece() {
		Circle currPiece = new Circle(30);
		currPiece.setFill(Color.WHITESMOKE);
		getChildren().add(currPiece);
		
		for (int i = 25; i > 0; i -= 10) {
			currPiece = new Circle(i);
			currPiece.setFill(Color.DARKGOLDENROD);
			getChildren().add(currPiece);
			
			currPiece = new Circle(i - 4);
			currPiece.setFill(Color.WHITESMOKE);
			getChildren().add(currPiece);
		}
	}
	
	/**
	 * Adds cool rings to the piece on this pane to indicate that it is now promoted. This
	 * method modifies player 2 pieces with gold rings.
	 */
	private void promoteBlackPiece() {
		Circle currPiece = new Circle(30);
		currPiece.setFill(Paint.valueOf("#381d16"));
		getChildren().add(currPiece);
		
		for (int i = 25; i > 0; i -= 10) {
			currPiece = new Circle(i);
			currPiece.setFill(Color.DARKGOLDENROD);
			getChildren().add(currPiece);
			
			currPiece = new Circle(i - 4);
			currPiece.setFill(Paint.valueOf("#381d16"));
			getChildren().add(currPiece);
		}
	}
	
	/**
	 * Acts as a record for the outer class. Stores nothing but a constructor and
	 * a getter method to determine which player selected this piece.
	 */
	private class PotentialSelectedPieceMove {
		
		private boolean pieceOwnedByPlayer1;
		
		/**
		 * Constructs an instance of the PotentialSelectedPieceMove class.
		 * @param pieceOwnedByPlayer1: A boolean determining whether player 1
		 * is the one who selected this piece.
		 */
		public PotentialSelectedPieceMove(boolean pieceOwnedByPlayer1) {
			this.pieceOwnedByPlayer1 = pieceOwnedByPlayer1;
		}
		
		/**
		 * Gets the value of field pieceOwnedByPlayer1.
		 * @return pieceOwnedByPlayer1
		 */
		public boolean isOwnerOfSelectedPiecePlayer1() {
			return pieceOwnedByPlayer1;
		}
	}
	
	/**
	 * Represents a checkers piece that is placed on this grid position.
	 * Contains fields such as whether it's promoted or is owned by player 1
	 */
	private class CheckersPiece {

		private boolean isOwnedByPlayer1;
		private boolean isPromoted;
		
		/**
		 * Constructs an instance of the CheckersPiece class, assuming it is not
		 * promoted and determining owner from isOwnedByPlayer1.
		 * @param isOwnedByPlayer1: a boolean determining which player owns the
		 * piece.
		 */
		public CheckersPiece(boolean isOwnedByPlayer1) {
			this.isOwnedByPlayer1 = isOwnedByPlayer1;
			this.isPromoted = false;
		}
		
		/**
		 * Promotes the piece.
		 */
		public void promote() {
			this.isPromoted = true;
		}
		
		/**
		 * Gets the value of field pieceOwnedByPlayer1.
		 * @return pieceOwnedByPlayer1
		 */
		public boolean isOwnedByPlayer1() {
			return isOwnedByPlayer1;
		}
		/**
		 * Checks whether the piece is promoted
		 * @return isPromoted
		 */
		public boolean isPromoted() {
			return isPromoted;
		}
	}
}







