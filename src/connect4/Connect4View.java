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

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * This class creates a GUI for the user to interact with, following the 
 * functions of a Connect4 game.
 */
public class Connect4View extends Application implements Observer {
	private StackPane[][] stackPanes = new StackPane[6][7];
	private Connect4Model model = new Connect4Model();
	private Connect4Controller control;
	private File file = new File("save_connect4.dat");
	private int[] prevPair = {-1,-1};
	private GridPane grid;

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
		
		char[][] board = control.getBoard();

		// Checks if piece was placed at the top of a column(meaning full column)
		if (pair[0] == 0) {
			for (StackPane[] row : stackPanes) {
				// Deactivates column
				row[pair[1]].setOnMouseClicked(null);
			}
		};
		
		updateBoard(board);
		
		int status = control.isGameOver(pair[0], pair[1]);
		
		// Game is over
		if (status != 0) {
			Alert a = new Alert(Alert.AlertType.INFORMATION);
			disableAll();

			// Connect4 Winner
			if (status == 1) {
				String winner = "";
				if (curP == 'y') winner = "Yellow";
				else winner = "Red";
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
			if (curP == 'y') control.setTurn('r');
			else control.setTurn('y');
		}
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
		        
		        Circle circle = new Circle(30, Color.TRANSPARENT);
		        circle.setStroke(Color.TRANSPARENT);
		      
		        cell.getChildren().add(circle);
		        cell.setPadding(new Insets(2, 2, 2, 2));
		        
		        // Blue board with black border
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
		MenuItem menuItem = new MenuItem("New Game");
		menu.getItems().add(menuItem);
		menuBar.getMenus().add(menu);
	}
	
	/**
	 * This method sets up the scene and calls other methods to setup the GUI
	 */
	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		newGame(stage);
	}
	
	/**
	 * Creates a new game of 
	 * 
	 * @param stage - the JavaFX stage
	 */
	private void newGame(Stage stage) {
		BorderPane pane = new BorderPane();
		model = new Connect4Model();
		control = new Connect4Controller(model);

		// Updates board according to the a previous saved game if available
		Connect4Instance loadedModel = Connect4Instance.loadGame();
		if (loadedModel != null) {
			control.setTurn(loadedModel.getTurn());
			control.setBoard(loadedModel.getBoard());
		}
		
		model.addObserver(this);

		grid = new GridPane();
		
		// Sets up the game board on the GUI
		setupBoard();
		MenuBar menuBar = new MenuBar();
		createMenu(menuBar);
		
		// Adds functionality to menu item
		menuBar.getMenus().getFirst().getItems().getFirst().setOnAction(event -> {
			if (file.exists()) file.delete();
			newGame(stage);
		});
		
		// Adds grid and menuBar to BorderPane
		pane.setCenter(grid);
		pane.setTop(menuBar);
		
		Scene scene = new Scene(pane, 555, 505);
		
        stage.setScene(scene);
        stage.setTitle("Connect4");
        stage.show();
        
        // Saves game if window is closed while game is not over
        stage.setOnCloseRequest(event -> {
        	int status = 0;
        	// Checks if theres at least one move that was made
        	if (prevPair[0] != -1) status = control.isGameOver(prevPair[0], prevPair[1]);
        	// Checks if the game is not over
			if (status == 0) {
				control.saveGame();
			}
		});
	}
	
	/**
	 * Starts this program
	 * @param args - a String[] that is meant for command line arguments
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);

	}
	
}
