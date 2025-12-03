package checkers;

import javafx.geometry.Pos;
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
	
	public void setCheckersPiece(boolean isOwnedByPlayer1) {
		
		clear();
		
		Pane circleHolder = new Pane();
		circleHolder.setPrefSize(66, 66);

		this.currPiece = new CheckersPiece(isOwnedByPlayer1);
		Circle checkersCircle = createCircle(isOwnedByPlayer1);
		checkersCircle.setManaged(true);
		checkersCircle.setTranslateX(0);
		checkersCircle.setTranslateY(0);
		getChildren().add(checkersCircle);
		isPotentialMove = null;
	}
	
	public void clear() {
		getChildren().clear();
		Rectangle background = new Rectangle(squareSize, squareSize);
		background.setFill(Paint.valueOf(backgroundColor));
		getChildren().add(background);
		
		currPiece = null;
		isPotentialMove = null;
	}
	
	private Circle createCircle(boolean isOwnedByPlayer1) {
		Circle currPiece = new Circle(30);
		if (isOwnedByPlayer1) {
			currPiece.setFill(Color.WHITESMOKE);
		} else {
			currPiece.setFill(Paint.valueOf("#381d16"));
		}
		return currPiece;
	}
	
	public void setPotentialMove(boolean isOwnedByPlayer1) {
		clear();
		currPiece = null;
		isPotentialMove = new PotentialSelectedPieceMove(isOwnedByPlayer1);
		Circle potentialCircle = setUpPotentialCircle();
		potentialCircle.setManaged(true);
		potentialCircle.setTranslateX(0);
		potentialCircle.setTranslateY(0);
		
		getChildren().add(potentialCircle);
		potentialCircle.setCenterX(33);
		potentialCircle.setCenterY(33);
	}
	
	private  Circle setUpPotentialCircle() {
		Circle markerSymbol = new Circle(8);
		markerSymbol.setFill(Color.BLACK);
		markerSymbol.setOpacity(.3);
		return markerSymbol;
	}
	
	public void promotePiece() {
		if (this.currPiece == null) {
			throw new IllegalStateException("Cannot Promote a Square Without a Checkers Piece");
		} else {
			this.currPiece.promote();
			if (currPiece.isOwnedByPlayer1) {
				promoteRedPiece();
			} else {
				promoteBlackPiece();
			}
		}
	}
	
	public boolean isPossibleMoveForSelectedPiece(boolean inputCameFromPlayer1) {
		if (isPotentialMove == null) {
			return false;
		} else {
			return isPotentialMove.isOwnerOfSelectedPiecePlayer1() == inputCameFromPlayer1;
		}
	}
	
	public boolean canBeModifiedBy(boolean inputCameFromPlayer1) {
		if (currPiece == null && isPotentialMove == null) {
			return false;
		} else if (currPiece != null) {
			return currPiece.isOwnedByPlayer1() == inputCameFromPlayer1;
		} else {
			return isPotentialMove.isOwnerOfSelectedPiecePlayer1() == inputCameFromPlayer1;
		}
	}
	
	public boolean isPieceOwnedByPlayer1() {
		if (this.currPiece == null) {
			throw new IllegalStateException("A Piece Must Exist for it to have an Owner");
		} else {
			return currPiece.isOwnedByPlayer1();
		}
	}
	
	public boolean isPiecePromoted() {
		if (this.currPiece == null) {
			throw new IllegalStateException("A Piece Must Exist for it to be Promoted");
		} else {
			return currPiece.isPromoted();
		}
	}
	
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
	
	private class PotentialSelectedPieceMove {
		
		private boolean pieceOwnedByPlayer1;
		
		public PotentialSelectedPieceMove(boolean pieceOwnedByPlayer1) {
			this.pieceOwnedByPlayer1 = pieceOwnedByPlayer1;
		}
		
		public boolean isOwnerOfSelectedPiecePlayer1() {
			return pieceOwnedByPlayer1;
		}
	}
	
	/**
	 * The concept for this was ripped straight from stack overflow by this post:
	 * https://stackoverflow.com/questions/43761825/checkerboard-inside-a-window-using-javafx.
	 * It's not exact, but it gave me the idea of extending the StackPane class.
	 */
	private class CheckersPiece {

		private boolean isOwnedByPlayer1;
		private boolean isPromoted;
		
		public CheckersPiece(boolean isOwnedByPlayer1) {
			this.isOwnedByPlayer1 = isOwnedByPlayer1;
			this.isPromoted = false;
		}
		
		public void promote() {
			this.isPromoted = true;
		}
		
		public boolean isOwnedByPlayer1() {
			return isOwnedByPlayer1;
		}
		
		public boolean isPromoted() {
			return isPromoted;
		}
	}
}







