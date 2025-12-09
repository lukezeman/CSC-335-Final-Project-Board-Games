/**
 * @author Achilles Soto
 * Course: CSC 335
 * File: Connect4View.java
 * This program provides a UI to the user to interact to play Connect4 by 
 * communicating with model through the controller.
 */
package connect4;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import view.BoardGamesView;
import view.PlayerNamingScreen;

/**
 * This class creates a GUI for the user to interact with, following the 
 * functions of a Connect4 game.
 */
public class Connect4View implements Observer {
	private StackPane[][] stackPanes = new StackPane[6][7];
	private Connect4Model model = new Connect4Model();
	private Connect4Controller control;
	private File file = new File("save_connect4.dat");
	private int[] prevPair = {-1,-1};
	private GridPane grid;
	private HBox turnBox;

	private BoardGamesView menuView;
	private Scene scene;
	
	/**
	 * Allows for an instance of this class to be made and sets up variables when loading Connect4
	 * 
	 * @param menuView - a BoardsGameView object used to update the stage
	 */
	public Connect4View(BoardGamesView menuView) {
		this.menuView = menuView;
		scene = newGame(null, null);
	}
	
	/**
	 * Allows for an instance of this class to be made and sets up variables for a new Connect4 game
	 * 
	 * @param menuView - a BoardsGameView object used to update the stage
	 * @param name1 - a String which represents player 1's name
	 * @param name2 - a String which represents player 2's name
	 */
	public Connect4View(BoardGamesView menuView, String name1, String name2) {
		this.menuView = menuView;
		scene = newGame(name1, name2);
	}

	/**
	 * Retrieves the scene of the Connect4 Game
	 * @return Scene object which holds the GUI of the Connect4 Game
	 */
	public Scene getScene() {
		return scene;
	}

	/**
	 * This method updates the GUI based on the last inputted move which 
	 * is called whenever the observed object is changed.
	 *
	 * @param o -- the observable object
	 * @param arg -- expected an array of two ints which are the coordinates of the last move
	 */
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		// Converts the arg into an array and saves it
		int[] pair = (int[]) arg;
		prevPair = pair;
		char curP = control.getTurn();
		
		// Checks if piece was placed at the top of a column(meaning full column)
		if (pair[0] == 0) {
			for (StackPane[] row : stackPanes) {
				// Deactivates column
				row[pair[1]].setOnMouseClicked(null);
			}
		};
		
		String[] players = control.getPlayers();
		
		dropPiece(pair[0], pair[1]);
		
		//updateBoard(board);
		
		int status = control.isGameOver(pair[0], pair[1]);
		
		// Game is over
		if (status != 0) {
			Alert a = new Alert(Alert.AlertType.INFORMATION);
			disableAll();

			// Connect4 Winner
			if (status == 1) {
				String winner = "";
				if (curP == 'y') winner = players[0];
				else winner = players[1];
				a.setTitle("GAME OVER");
				a.setHeaderText("Winner: " + winner);
				a.setContentText(winner + " wins!");
			}
			// Connect4 Draw
			else if (status == -1) {
				a.setTitle("GAME OVER");
				a.setHeaderText("Draw");
				a.setContentText("Neither Player Connected Four Pieces And Board Is Full");
			}
			a.showAndWait();
			if (file.exists()) file.delete();
		}
		// Continue playing
		else {
			Label turn = (Label) turnBox.getChildren().get(0);
			Circle circle = (Circle) turnBox.getChildren().get(1);

			if (curP == 'y') {
				control.setTurn('r');
				circle.setFill(Color.RED);
				circle.setStroke(Color.DARKRED);
				turn.setText(players[1]+"'s Turn!");
				
			}
			else {
				control.setTurn('y');
				circle.setFill(Color.YELLOW);
				circle.setStroke(Color.DARKKHAKI);
				turn.setText(players[0]+"'s Turn!");

			}
			turn.setFont(new Font("Arial", 24));
		}
	}
	
	/**
	 * Animates a falling piece when the user makes their move.
	 * 
	 * @param x - an int which represents the row the piece lands in
	 * @param y - an int which represents the column the piece goes into
	 */
	private void dropPiece(int x, int y) {
	    
	    // Default Piece color is Yellow
	    Color fillColor = Color.YELLOW; 
	    Color strokeColor = Color.DARKKHAKI;
	    	    		
	    char curP = control.getTurn();
	    // Switches Circle colors for a red piece
	    if (curP == 'r') {
	    	fillColor = Color.RED;
	    	strokeColor = Color.DARKRED;
	    }

	    // Piece that will be dropped
	    Circle circle = new Circle(30, fillColor);
	    circle.setStroke(strokeColor);

	    // Add the circle to the top of the column
	    StackPane topCell = stackPanes[0][y];
	    topCell.getChildren().add(circle);

	    double dropDistance = 0;

	    // Adds the total height from top row to the row it lands into
	    for (int row = 0; row <= x; row++) {
	        dropDistance += stackPanes[row][y].getHeight();
	    }

	    // Animates dropping a piece
	    TranslateTransition drop = new TranslateTransition(Duration.seconds(0.5), circle);
	    drop.setByY(dropDistance - topCell.getHeight());
	    drop.setCycleCount(1);
	    drop.setAutoReverse(false);

	    drop.setOnFinished(e -> {
	        // Remove falling circle from stack pane
	        topCell.getChildren().remove(circle);

	        // Update GUI board based on text board
	        char[][] board = control.getBoard();
	        updateBoard(board);
	    });

	    drop.play();
	}
	
	/**
	 * Sets all setOnMouseClicked actions to null for each cell
	 */
	private void disableAll() {
		for (StackPane[] row : stackPanes) {
			for (StackPane cell : row) {
				cell.setOnMouseClicked(null);
			}
		}
	}
	
	/**
	 * Sets up Connect4 grid pane, 7x8 cell grid with Circle 
	 * object preplaced in each cell.
	 */
	private void setupBoard() {		
		grid.setGridLinesVisible(true);
		
		// Create 7x8 cells
		for (int i = 0; i < 6; i++) {
		    for (int j = 0; j < 7; j++) {
		        StackPane cell = new StackPane();
		        cell.setPrefSize(67.5, 67.5);
		        
		        // Adds circles for each cell
		        Circle circle = new Circle(30, Color.TRANSPARENT);
		        circle.setStroke(Color.TRANSPARENT);
		      
		        cell.getChildren().add(circle);
		        cell.setPadding(new Insets(2, 2, 2, 2));
		        
		        // Adds Blue board with black border
		        cell.setStyle(
		        	    "-fx-border-color: blue, black;" +
		        	    "-fx-border-width: 3, 3;" +
		        	    "-fx-border-insets: 0, 3;"
		        	);
				int row = j;
				
				// Clicking on a cell adds a piece
		        cell.setOnMouseClicked(e -> {
		        	control.setMove(row);
		        });
		        grid.add(cell, j, i);
		        stackPanes[i][j] = cell;
		    }
		}
		
		grid.setPadding(new Insets(8, 8, 8, 8));
		updateBoard(control.getBoard());
	}
	
	/**
	 * Updates the grid pane cells based on the model Connect4 board
	 * @param board - an array of arrays of characters(Connect4 board)
	 */
	private void updateBoard(char[][] board) {
		// Loops over contents of Connect4 board
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				
				// Sets up a cell's circle according to the current board's slot
				Circle circle = (Circle) stackPanes[i][j].getChildren().getFirst();
				char piece = board[i][j];
				if (piece == '\0') {
					circle.setFill(Color.TRANSPARENT);
					circle.setStroke(Color.TRANSPARENT);
				}
				else if (piece == 'y') {
					circle.setFill(Color.YELLOW);
					circle.setStroke(Color.DARKKHAKI);
				}
				else if (piece == 'r') {
					circle.setFill(Color.RED);
					circle.setStroke(Color.DARKRED);
				}
			}
		}
	}
	
	/**
	 * Adds menu items to menu bar
	 * @param menuBar - MenuBar object which will be added to
	 */
	private void createMenu(MenuBar menuBar) {
		Menu menu = new Menu("File");
		MenuItem newGame = new MenuItem("New Game");
		newGame.setOnAction(event -> {
			if (file.exists()) file.delete();
			PlayerNamingScreen.namingScreen(menuView, "Connect 4");
		
		});
		// Exit and save
		MenuItem saveExit = new MenuItem("Save And Exit");
		saveExit.setOnAction(e -> {
			int status = 0;
        	// Checks if theres at least one move that was made
        	if (prevPair[0] != -1) status = control.isGameOver(prevPair[0], prevPair[1]);
        	// Checks if the game is not over
			if (status == 0) {
				control.saveGame();
			}
			menuView.exitToMenu();
		});
		// Exit without saving
		MenuItem exit = new MenuItem("Exit Without Saving");
		exit.setOnAction(e -> {
			menuView.exitToMenu();
		});
		menu.getItems().add(newGame);
		menu.getItems().add(saveExit);
		menu.getItems().add(exit);
		menuBar.getMenus().add(menu);
	}
	
	/**
	 * Creates/loads a game of Connect4 to present as a GUI
	 * @return Scene object to be used on the Super Awesome Games main menu
	 */
	private Scene newGame(String player1, String player2) {
		BorderPane pane = new BorderPane();
		model = new Connect4Model();
		control = new Connect4Controller(model);
		String[] players;

		// Updates board according to the a previous saved game if available
		Connect4Instance loadedModel = Connect4Instance.loadGame();
		if (loadedModel != null) {
			control.setTurn(loadedModel.getTurn());
			control.setBoard(loadedModel.getBoard());
			players = loadedModel.getPlayers();
			control.setPlayers(players);
		}
		else {
			String[] temp = {player1, player2};
			players = temp;
			control.setPlayers(players);
		}
		
		model.addObserver(this);

		grid = new GridPane();
		grid.setAlignment(Pos.CENTER);

		// Sets up the game board on the GUI
		setupBoard();
		MenuBar menuBar = new MenuBar();
		createMenu(menuBar);
		
		char curPlayer = control.getTurn();
		turnBox = new HBox();
		turnBox.setAlignment(Pos.TOP_CENTER);
		
		// Default Starts with player 1
		Circle circle = new Circle(10, Color.YELLOW);
		circle.setStroke(Color.DARKKHAKI);
		Label turn = new Label(players[0]+"'s Turn!");
		
		// Switches to player 2 for loaded games
		if (curPlayer == 'r') {
			turn.setText(players[1]+"'s Turn!");
			circle.setFill(Color.RED);
			circle.setStroke(Color.DARKRED);
		}
		turn.setFont(new Font("Arial", 24));
        turnBox.getChildren().addAll(turn, circle);		
		
		// Adds grid and menuBar to BorderPane
		pane.setCenter(grid);
		pane.setTop(menuBar);
		pane.setBottom(turnBox);
		
		Scene scene = new Scene(pane, 550, 550);
		
        return scene;
	}
	
}
