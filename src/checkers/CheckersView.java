package checkers;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javafx.animation.PathTransition;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import view.BoardGamesView;

@SuppressWarnings("deprecation")
public class CheckersView implements Observer {

	private BoardGamesView menuView;
	
	private static final int gridLength = 8;

	private CheckersController controller;

	private BorderPane mainWindow;
	private Scene gameScene;
	// 8*8 grid
	private GridPane gameGrid;
	private BoardSquare[][] cellPanes;
	private BoardSquare[] currSelectedAsPotentialMoves;

	private Label playerTurnTracker;
	
	private Scene scene;
	
	public CheckersView(BoardGamesView menuView) {
		this.menuView = menuView;
		currSelectedAsPotentialMoves = new BoardSquare[4];
		scene = setupScene();
		startGame();
	}
	
	
	/*
	 * When making your GUI, instead of setting up a stage and stuff, just
	 * set up the scene you want in here and the BoardGamesView class will 
	 * handle displaying this to the screen. Feel free to use the exitToMenu
	 * methods in the menuView, as well as whatever other methods are added
	 */
	private Scene setupScene() {
		
		this.controller = new CheckersController();
		this.mainWindow = new BorderPane();
		this.gameGrid = new GridPane();
		
		// Set up the Nodes for the top, middle, and bottom parts of mainWindow
		setUpTop();
		setUpMiddle();
		setUpBottom();
		//System.out.println("Set up the main window");
		// Set up size of the scene in terms of the size of the board
		Scene gameScene = new Scene(mainWindow, 66 * gridLength + 16, (66 * gridLength + 16) + 40);
		
		
		
		//System.out.println("Set up the scene");
		// Process the mouse clicks of the user and add the move it corresponds to
		gameScene.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				handleUserInput(arg0.getX(), arg0.getY());
			}
		});
		//System.out.println("Set up the mouse handler");
		return gameScene;
	}
	
	/**
	 * Sets the top bar of the main window
	 * <p>
	 * Creates a bar menu item collection with a single menu item, which begins a
	 * new game and erases the current game, as well as any saved games. It then
	 * sets this to the top of our BorderPane object serving as the GUI's main
	 * window.
	 */
	private void setUpTop() {

		// Creates the bar menu item collection object
		MenuBar bar = new MenuBar();
		Menu menu = new Menu("File");
		MenuItem newGame = new MenuItem("New Game");
		MenuItem exit = new MenuItem("Exit");
		// Start a new game when the item is pressed
		newGame.setOnAction(e -> {
			//startNewGame();
		});
		
		exit.setOnAction(e ->{
			menuView.exitToMenu();
		});
		// Connect the bar menu item objects to each other and add to top of window
		menu.getItems().add(newGame);
		menu.getItems().add(exit);
		bar.getMenus().add(menu);
		mainWindow.setTop(bar);
	}
	
	/**
	 * Sets up the main reversi board
	 * <p>
	 * 
	 * StackPane is centered within the BorderPane mainWindow with a 6 pixel
	 * boundary on all sides. Has a green background with a 1 pixel black bar along
	 * each grid pane.
	 * 
	 * 20 pixel radius circles centered in GridPanes of radius 23, giving a 2 pixel
	 * boundary between the end of the circle and the one pixel black border around
	 * each pane.
	 * 
	 * Circles are initially transparent, but color can change depending on the
	 * state of the board to either black or white.
	 */
	private void setUpMiddle() {
		// Fix the size of the grid to
		gameGrid.setPrefSize(64 * gridLength, 64 * gridLength);
		gameGrid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		// Initialize the cell panes field here since its data is constructed here
		this.cellPanes = new BoardSquare[gridLength][gridLength];
		for (int i = 0; i < gridLength; i++) {
			for (int j = 0; j < gridLength; j++) {
				// GridPane works in column major order apparently
				BoardSquare gridElement = setupCurrPiece(i, j);
				cellPanes[i][j] = gridElement;
				gameGrid.add(gridElement, j, i);
			}
		}
		// A bunch of stuff designed to actually center the stupid thingies
		StackPane boardHolder = new StackPane();
		boardHolder.getChildren().add(gameGrid);
		boardHolder.setAlignment(Pos.CENTER);
		boardHolder.setBackground(new Background(new BackgroundFill(Color.rgb(216, 179, 126)
				, CornerRadii.EMPTY, Insets.EMPTY)));
		gameGrid.setStyle("-fx-border-color: black; -fx-border-width: 1;");
		mainWindow.setCenter(boardHolder);
	}
	
	/**
	 * 
	 * @param rowNum
	 * @param colNum
	 * @return
	 */
	private BoardSquare setupCurrPiece(int rowNum, int colNum) {
		// I shamelessly googled this thing because I don't know CSS strings :(
		if ((rowNum + colNum) % 2 == 0) {
			return new BoardSquare("#d8b37e", 66);
		} else {
			return new BoardSquare("#986f4f", 66);
		}
	}
	
	/**
	 * adds a label that tracks the score of the white pieces and the black pieces
	 */
	private void setUpBottom() {
		playerTurnTracker = new Label("Player One's Turn");
		mainWindow.setBottom(playerTurnTracker);
	}
	
	
	public Scene getScene() {
		return scene;
	}
	
	/**
	 * Processes the user's click
	 * <p>
	 * Takes the exact location the user clicked on the GUI and determines if the
	 * click was on a tile within the grid. If so, then it finds which tile was
	 * clicked and sends those values to controller to see if it is a valid input or
	 * should be ignored.
	 * 
	 * @param xPos: The x-coordinate of the pixel the user clicked.
	 * @param yPos: The y-coordinate of the pixel the user clicked.
	 */
	public void handleUserInput(double xPos, double yPos) {
		// 8=6 pixel offset + 1 pixel border + 1. 30 is just best guess for offset
		if (xPos > 8 && yPos > 34 && xPos - 8 < 66 * gridLength && yPos - 34 < 66 * gridLength) {
			// Remove 8 pixel offset from boundary and make first valid pixel 0
			int roundedX = (int) Math.round(xPos) - 9;
			// Remove 48 pixel offset from boundary and make first valid pixel 0
			int roundedY = (int) Math.round(yPos) - 35;
			//System.out.println("X: " + roundedX / 66 + ". Y: " + roundedY / 66);
			// Because of weird row/column order things, you gotta swap the x/y order
			controller.takeInput(roundedY / 66, roundedX / 66);
			if (controller.isPlayerOne()) {
				playerTurnTracker.setText("Player One's Turn");
			} else {
				playerTurnTracker.setText("Player Two's Turn");
			}
		}
	}
	
	/**
	 * Displays an error message to the app via an ERROR-type Alert. Solely used for
	 * testing, and should never be called during expected gameplay.
	 * 
	 * @param errorMessage: The message to be displayed in the Alert. Should be
	 *                      informative regarding what went wrong.
	 */
	private void handleError(String errorMessage) {
		Alert userBlocker = new Alert(Alert.AlertType.ERROR);
		userBlocker.setTitle("ERROR!");
		userBlocker.setHeaderText("Something went wrong :(");
		userBlocker.setContentText(errorMessage);
		userBlocker.showAndWait();
	}
	
	private void startGame() {
		controller.startGame(this);
	}
	
	private boolean isNull(int[] selectedIndex) {
		return selectedIndex[0] == -1 && selectedIndex[1] == -1;
	}
	
	private void movePiece(CheckersInstance newBoardState) {
		int[][] newBoardGrid = newBoardState.getBoard();
		int[] sourceIndex = newBoardState.getSelectedIndex();
		int[] targetIndex = newBoardState.getMovedIndex();
		int currSourceVal = newBoardGrid[sourceIndex[0]][sourceIndex[1]];
		int currTargetVal = newBoardGrid[targetIndex[0]][targetIndex[1]];
		setCellPane(cellPanes[sourceIndex[0]][sourceIndex[1]], currSourceVal);
		setCellPane(cellPanes[targetIndex[0]][targetIndex[1]], currTargetVal);
		if (Math.abs(sourceIndex[0] - targetIndex[0]) > 1) {
			// Bit of a trick, but piece jumped is in the middle of prev and new location
			int skippedRowVal = (sourceIndex[0] + targetIndex[0]) / 2;
			int skippedColVal = (sourceIndex[1] + targetIndex[1]) / 2;
			int currJumpedVal = newBoardGrid[skippedRowVal][skippedColVal];
			// Set the square with the jumped piece to be empty
			setCellPane(cellPanes[skippedRowVal][skippedColVal], currJumpedVal);
			//TODO Check if the piece that jumped can jump another piece
		}
		// Remove all the potental move dots from the view
		clearSelectedMoves(newBoardState);
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		CheckersInstance newBoardState = (CheckersInstance) arg;
		//gameGrid.getChildren().clear();
		if (!isNull(newBoardState.getMovedIndex())) {
			movePiece(newBoardState);
		} else {
			int[][] newBoardGrid = newBoardState.getBoard();
			for (int i = 0; i < newBoardGrid.length; i++) {
				int[] currRow = newBoardGrid[i];
				for (int j = 0; j < currRow.length; j++) {
					int currSquareVal = currRow[j];
					setCellPane(cellPanes[i][j], currSquareVal);
					//gameGrid.add(cellPanes[i][j], j, i);
				}
			}
		}
		
		/*
		 if (!isNull(newBoardState.getMovedIndex())) {
			
			System.out.println("We should be moving");
			animateTravel(newBoardState.getSelectedIndex()[0], newBoardState.getSelectedIndex()[1],
					newBoardState.getMovedIndex()[0], newBoardState.getMovedIndex()[1]);
			System.out.println("We should be done moving");
		} else if (!isNull(newBoardState.getSelectedIndex())) {
			
			System.out.println("We should be showing the potential moves");
			showPotentialMoves(newBoardState);
		} else {
		 */
	}
	
	/**
	 * 
	 * @param newBoardState
	
	private void showPotentialMoves(CheckersInstance newBoardState) {
		clearSelectedMoves();
		int[][] newPotentialCoords = newBoardState.getSelectedPossibleMoves();
		for (int i = 0; i < newPotentialCoords.length; i++) {
			int[] currCoord = newPotentialCoords[i];
			if (!isNull(currCoord)) {
				int rowIndex = currCoord[0];
				int colIndex = currCoord[1];
				BoardSquare currSelectedPotentialPane = cellPanes[rowIndex][colIndex];
				currSelectedPotentialPane.setPotentialMove(controller.isPlayerOne());
				currSelectedAsPotentialMoves[i] = currSelectedPotentialPane;
			}
		}
	}
	 */
	
	/**
	 * Clears all the selected moves currently on the screen. This should occur
	 * whenever a new owned piece was clicked by the current player or when the
	 * player moves a piece.
	 */
	public void clearSelectedMoves(CheckersInstance newBoardState) {
		int[][] board = newBoardState.getBoard();
		int[][] selectedPossibleMoves = newBoardState.getSelectedPossibleMoves();
		
		int rowVal;
		int colVal;
		for (int[] currPossibleMove: selectedPossibleMoves) {
			//System.out.println(Arrays.toString(currPossibleMove));
			if (!isNull(currPossibleMove)) {
				// Clear all the dots from the view since a move was taken
				rowVal = currPossibleMove[0];
				colVal = currPossibleMove[1];
				setCellPane(cellPanes[rowVal][colVal], board[rowVal][colVal]);
			}
		}
	}
	
	private void setCellPane(BoardSquare modifiedSquare, int currBoardState) {
		switch (currBoardState) {
		case 0:
			modifiedSquare.clear();
			break;
		case 1:
			modifiedSquare.setCheckersPiece(true);
			break;
		case -1:
			modifiedSquare.setCheckersPiece(false);
			break;
		case 2:
			modifiedSquare.setCheckersPiece(true);
			modifiedSquare.promotePiece();
			break;
		case -2:
			modifiedSquare.setCheckersPiece(false);
			modifiedSquare.promotePiece();
			break;
		case 3:
			modifiedSquare.setPotentialMove(true);
			break;
		case -3:
			modifiedSquare.setPotentialMove(false);
			break;
		default:
			handleError("ERROR: Unrecognized board state val: " + currBoardState);
		}
	}
	
	private Node getNodeAt(GridPane grid, int row, int col) {
	    for (Node n : grid.getChildren()) {
	        Integer r = GridPane.getRowIndex(n);
	        Integer c = GridPane.getColumnIndex(n);
	        if ((r == null ? 0 : r) == row && (c == null ? 0 : c) == col) {
	            return n;
	        }
	    }
	    return null;
	}
	
	private void animateTravel(int startX, int startY, int endX, int endY) {
		
		System.out.println("startX: " + startX + ". startY: " + startY);
		System.out.println("endX: " + endX + ". endY: " + endY);
		BoardSquare source = cellPanes[startX][startY];
		BoardSquare target = cellPanes[endX][endY];

		
		//System.out.println("Target sanity check:");
		//System.out.println("cellPanes ref = " + target);
		//System.out.println("GridPane lookup = " + getNodeAt(gameGrid, endX, endY));
		
		System.out.println("\n\nChildren of source square:");
		for (Node n : source.getChildren()) {
		    System.out.println(" - " + n + " (class " + n.getClass().getSimpleName() + ")");
		    System.out.println(n + "  opacity=" + n.getOpacity() + "\n\n");
		}
		
		gameGrid.layout();
		
		System.out.println("\n\nChildren of target square:");
		for (Node n : target.getChildren()) {
		    System.out.println(" - " + n + " (class " + n.getClass().getSimpleName() + ")");
		    System.out.println(n + "  opacity=" + n.getOpacity() + "\n\n");
		}
		target.setCheckersPiece(false);
		
		
		
		//System.out.println("Updating target square at: " + endX + ", " + endY);
		//System.out.println("Actual square reference: " + target);
		//System.out.println(gameGrid.getChildren());
		//target.setCheckersPiece(source.isPieceOwnedByPlayer1());
		//if (source.isPiecePromoted()) {
			//target.promotePiece();
		//}
		//target.promotePiece();
		//source.promotePiece();
		//System.out.println("Target size: " + target.getWidth() + " x " + target.getHeight());
		
		
		//source.clear();
		//System.out.println(target.getChildren());
		/*
		Node piece = source.getCheckersPiece();
		
		
		Path path = new Path();
		path.getElements().add(new MoveTo(500, 500));
		path.getElements().add(new LineTo(300, 200));
		path.getElements().add(new LineTo(300, 400));
		path.getElements().add(new LineTo(450, 350));
		PathTransition pathTransition = new PathTransition();
		pathTransition.setDuration(Duration.millis(5000));
		pathTransition.setNode(piece);
		pathTransition.setPath(path);
		pathTransition.play();
		Group root = new Group(piece);
		Scene scene = new Scene(root, 600, 300);
		System.out.println("Worked perfectly");
		*/
	}

}













