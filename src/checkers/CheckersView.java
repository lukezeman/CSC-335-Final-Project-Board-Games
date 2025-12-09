/**
 * Creates a Scene object for the checkers game and passes it to the main view.
 * It also speaks to the checkers controller, so this class effectively becomes
 * the main View piece while this game is being played
 * @Author: Charlie Cain.
 */

package checkers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
import view.BoardGamesView;
import view.PlayerNamingScreen;

@SuppressWarnings("deprecation")
public class CheckersView implements Observer {

	private BoardGamesView menuView;
	private CheckersInstance currInstance;

	private static final int gridLength = 8;

	private CheckersController controller;
	private String player1;
	private String player2;

	private BorderPane mainWindow;
	private GridPane gameGrid;
	private BoardSquare[][] cellPanes;

	private Label playerTurnTracker;

	private Scene scene;

	/**
	 * Constructs an instance of the CheckersView class
	 * 
	 * @param menuView: A reference to the main View class
	 */
	public CheckersView(BoardGamesView menuView) {
		this.menuView = menuView;
		this.player1 = "Player 1";
		this.player2 = "Player 2";
		scene = setupScene();
		startGame();
	}

	/**
	 * Constructs an instance of the CheckersView class with names for player 1 and
	 * player 2.
	 * 
	 * @param menuView: A reference to the main View class
	 * @param name1:    The name of player one
	 * @param name2:    The name of player two
	 */
	public CheckersView(BoardGamesView menuView, String name1, String name2) {
		this.menuView = menuView;
		this.player1 = name1;
		this.player2 = name2;
		scene = setupScene();
		startGame();
	}

	/**
	 * Creates a scene object with all the functionality required to play the
	 * checkers game. Does not create a Stage for itself, but rather just creates
	 * the scene to put on the stage
	 */
	private Scene setupScene() {
		this.controller = new CheckersController();
		this.mainWindow = new BorderPane();
		this.gameGrid = new GridPane();
		// Set up the Nodes for the top, middle, and bottom parts of mainWindow
		setUpTop();
		setUpMiddle();
		setUpBottom();
		// Set up size of the scene in terms of the size of the board
		Scene gameScene = new Scene(mainWindow, 66 * gridLength + 16, (66 * gridLength + 16) + 40);

		// Process the mouse clicks of the user and add the move it corresponds to
		gameScene.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				handleUserInput(arg0.getX(), arg0.getY());
			}
		});
		return gameScene;
	}

	/**
	 * Loads whatever instance of the checkers game is currently saved in the file
	 * "save_checkers.dat". If the file does not exist, then it will simply return
	 * null. Finally, it deletes the file to prevent save corruption issues.
	 * 
	 * @return The ObjectInputStream representing the saved game.
	 */
	private ObjectInputStream loadSave() {
		File currSave = new File("save_checkers.dat");
		ObjectInputStream save;
		if (currSave.exists()) {
			try {
				save = new ObjectInputStream(new FileInputStream(currSave));
			} catch (IOException e) {
				save = null;
			}
		} else {
			save = null;
		}
		return save;
	}

	/**
	 * Stores the current CheckersInstance object into the file "save_checkers.dat"
	 * as an ObjectOutputStream
	 */
	private void saveGame() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("save_checkers.dat"));
			out.writeObject(currInstance);
			out.close();
		} catch (IOException e) {
			return;
		}
	}

	/**
	 * Deletes whatever saved instance of the game currently exists. This is
	 * typically done once a game is finished or a new save is instanciated.
	 */
	private void deleteSave() {
		File currSave = new File("save_checkers.dat");
		if (currSave.exists()) {
			currSave.delete();
		}
	}

	/**
	 * Sets the top bar of the main window
	 * <p>
	 * Creates a bar menu item collection with three menu items, allowing you to
	 * start a new game, save and exit, or exit without saving. If the game is over,
	 * then pressing the button to exit while saving will just delete the file
	 * again.
	 */
	private void setUpTop() {

		// Creates the bar menu item collection object
		MenuBar bar = new MenuBar();
		Menu menu = new Menu("File");
		MenuItem newGame = new MenuItem("New Game");
		MenuItem saveExit = new MenuItem("Exit and Save");
		MenuItem noSaveExit = new MenuItem("Exit without Saving");
		// Start a new game when the item is pressed
		newGame.setOnAction(e -> {
			deleteSave();
			PlayerNamingScreen.namingScreen(menuView, "Checkers");
		});
		// Saves and exits the game when the item is pressed
		saveExit.setOnAction(e -> {
			if (!controller.isGameOver()) {
				saveGame();
			} else {
				deleteSave();
			}
			menuView.exitToMenu();
		});
		// Deletes the current save and exits the game when the item is pressed
		noSaveExit.setOnAction(e -> {
			deleteSave();
			menuView.exitToMenu();
		});

		// Connect the bar menu item objects to each other and add to top of window
		menu.getItems().add(newGame);
		menu.getItems().add(saveExit);
		menu.getItems().add(noSaveExit);
		bar.getMenus().add(menu);
		mainWindow.setTop(bar);
	}

	/**
	 * Sets up the main checkers board
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
		boardHolder.setBackground(
				new Background(new BackgroundFill(Color.rgb(216, 179, 126), CornerRadii.EMPTY, Insets.EMPTY)));
		gameGrid.setStyle("-fx-border-color: black; -fx-border-width: 1;");
		mainWindow.setCenter(boardHolder);
	}

	/**
	 * Sets up a BoardSquare object to place in the GridPane object at row/col
	 * location rowNum/colNum
	 * 
	 * @param rowNum: the row location this piece is going in
	 * @param colNum: the column location this piece is going to
	 * @return the BoardSquare constructed for this location
	 */
	private BoardSquare setupCurrPiece(int rowNum, int colNum) {
		if ((rowNum + colNum) % 2 == 0) {
			// Dark brown
			return new BoardSquare("#d8b37e", 66);
		} else {
			// Lighter brown
			return new BoardSquare("#986f4f", 66);
		}
	}

	/**
	 * adds a label that tracks the score of the white pieces and the black pieces
	 */
	private void setUpBottom() {
		playerTurnTracker = new Label(player1 + "'s Turn");
		mainWindow.setBottom(playerTurnTracker);
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
	private void handleUserInput(double xPos, double yPos) {
		// 8=6 pixel offset + 1 pixel border + 1. 30 is just best guess for offset
		if (xPos > 8 && yPos > 34 && xPos - 8 < 66 * gridLength && yPos - 34 < 66 * gridLength) {
			// Remove 8 pixel offset from boundary and make first valid pixel 0
			int roundedX = (int) Math.round(xPos) - 9;
			// Remove 48 pixel offset from boundary and make first valid pixel 0
			int roundedY = (int) Math.round(yPos) - 35;
			// Deactivate the board once the game has finished
			if (!controller.isGameOver()) {
				// Because of weird row/column order things, you gotta swap the x/y order
				controller.takeInput(roundedY / 66, roundedX / 66);
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

	/**
	 * Starts the game, passing the saved data from "checkers_save.dat"
	 */
	private void startGame() {
		ObjectInputStream savedGame = loadSave();
		controller.startGame(this, savedGame, player1, player2);
	}

	/**
	 * Checks whether the passed array is equal to the value {-1, -1}, which is the
	 * null location, or not.
	 * 
	 * @param selectedIndex: The array whose location is being checked.
	 * @return: Whether the value equals th null location
	 */
	private boolean isNull(int[] selectedIndex) {
		return selectedIndex[0] == -1 && selectedIndex[1] == -1;
	}

	/**
	 * Moves a piece on the gui from the location the user clicked the piece on to
	 * the location the user decided to move the piece to.
	 * 
	 * @param newBoardState: The CheckersInstance containing the information to
	 *                       determine the user choices.
	 */
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
			// TODO Check if the piece that jumped can jump another piece
		}
		// Remove all the potental move dots from the view
		clearSelectedMoves(newBoardState);
	}

	@Override
	/**
	 * Takes the passed CheckersInstance object from the model and uses it to
	 * update the GUI.
	 */
	public void update(Observable o, Object arg) {
		CheckersInstance newBoardState = (CheckersInstance) arg;
		this.currInstance = newBoardState;
		this.player1 = currInstance.getPlayer1();
		this.player2 = currInstance.getPlayer2();
		boolean isPlayerOnesTurn = currInstance.isPlayerOnesTurn();
		// Set the bottom text field to accurately determine whose turn it is
		if (isPlayerOnesTurn) {
			playerTurnTracker.setText(player1 + "'s Turn");
		} else {
			playerTurnTracker.setText(player2 + "'s Turn");
		}
		// If a piece has been moved, we can simplify the updating process.
		if (!isNull(newBoardState.getMovedIndex())) {
			movePiece(newBoardState);
		} else {
			int[][] newBoardGrid = newBoardState.getBoard();
			for (int i = 0; i < newBoardGrid.length; i++) {
				int[] currRow = newBoardGrid[i];
				for (int j = 0; j < currRow.length; j++) {
					int currSquareVal = currRow[j];
					setCellPane(cellPanes[i][j], currSquareVal);
				}
			}
		}
		// End the game once the controller determines it's over.
		if (controller.isGameOver()) {
			endGame();
		}

	}

	/**
	 * Ends the game by showing an alert to the user telling them which
	 * player won.
	 */
	private void endGame() {
		Alert endGameAlert = new Alert(AlertType.INFORMATION);
		endGameAlert.setTitle("GAME OVER!");
		endGameAlert.setHeaderText("GAME OVER!");
		if (controller.playerOneWon()) {
			endGameAlert.setContentText(player1 + " Won");
		} else {
			endGameAlert.setContentText(player2 + " Won");
		}
		endGameAlert.show();
	}

	/**
	 * Clears all the selected moves currently on the screen. This should occur
	 * whenever a new owned piece was clicked by the current player or when the
	 * player moves a piece.
	 */
	private void clearSelectedMoves(CheckersInstance newBoardState) {
		int[][] board = newBoardState.getBoard();
		int[][] selectedPossibleMoves = newBoardState.getSelectedLegalMoves();

		int rowVal;
		int colVal;
		for (int[] currPossibleMove : selectedPossibleMoves) {
			if (!isNull(currPossibleMove)) {
				// Clear all the dots from the view since a move was taken
				rowVal = currPossibleMove[0];
				colVal = currPossibleMove[1];
				setCellPane(cellPanes[rowVal][colVal], board[rowVal][colVal]);
			}
		}
	}

	/**
	 * Literally just goes over all possible values the currBoardState could be
	 * and modifies modifiedSquare accordingly
	 * @param modifiedSquare: the BoardSquare being modified
	 * @param currBoardState: an int stating the curr value of the board at the
	 * index of modifiedSquare.
	 */
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
			modifiedSquare.promote();
			break;
		case -2:
			modifiedSquare.setCheckersPiece(false);
			modifiedSquare.promote();
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

	/**
	 * Gives the Scene object this class created
	 * @return this.scene
	 */
	public Scene getScene() {
		return this.scene;
	}
}
