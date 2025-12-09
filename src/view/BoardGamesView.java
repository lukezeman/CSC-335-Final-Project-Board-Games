/**
 * @author Achilles Soto
 * @author Charlie Cain
 * @author Luke Zeman
 * @author Aidin Miller
 * Course: CSC335
 * File: BoardGamesView.java
 * Presents GUI starting at the main menu where a game can be selected
 */
package view;

import java.io.File;

import blackjack.BlackjackView;
import checkers.CheckersView;
import connect4.Connect4View;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import yahtzee.YahtzeeView;

/**
 * Creates and presents main menu GUI which allows for game selection
 */
public class BoardGamesView extends Application {
	private Stage guiStage;
	private Scene menuScene;
	private Image[] images = new Image[4];
	private ImageView[] imageViews = new ImageView[4];
	
	/**
	 * This helper method starts the main menu GUI
	 * @param stage -- the JavaFX stage
	 */
	@Override
	public void start(Stage stage) throws Exception {
		
		loadImages();
		
		guiStage = stage;
		
		BorderPane mainWindow = new BorderPane();
		Label label = new Label("WELCOME TO SUPER AWESOME BOARD GAMES!");
		label.setWrapText(true);
		label.setMaxWidth(400);
		label.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
		label.setPadding(new Insets(10));
		mainWindow.setTop(label);
		label.setTextAlignment(TextAlignment.CENTER);
		BorderPane.setAlignment(label, Pos.CENTER);
		
		setupMiddle(mainWindow, stage);
		
		Scene scene = new Scene(mainWindow, 500, 500);
		menuScene = scene;
		stage.setScene(scene);
		stage.setTitle("Super Awesome Board Games");
		stage.show();
		stage.centerOnScreen();
	}
	
	/**
	 * Loads Images that will be used for GUI
	 */
	private void loadImages() {
		images[0] = new Image(getClass().getResourceAsStream("/mainMenu/blackjack.png"));
		images[1] = new Image(getClass().getResourceAsStream("/mainMenu/checkers.png"));
		images[2] = new Image(getClass().getResourceAsStream("/mainMenu/connect4.png"));
		images[3] = new Image(getClass().getResourceAsStream("/mainMenu/yahtzee.png"));
		
		imageViews[0] = new ImageView(images[0]);
		imageViews[1] = new ImageView(images[1]);
		imageViews[2] = new ImageView(images[2]);
		imageViews[3] = new ImageView(images[3]);
		
		for (ImageView view : imageViews) {
			view.setFitHeight(125);
			view.setFitWidth(125);
		}
	}
	
	/**
	 * Portrays games on GUI to select from
	 * @param window - GUI presentation which is a BorderPane
	 * @param stage - the JavaFX stage
	 */
	private void setupMiddle(BorderPane window, Stage stage) {
		GridPane gridPane = new GridPane(25, 25);
		
		VBox blackjack = new VBox(10);
		VBox checkers = new VBox(10);
		VBox connect4 = new VBox(10);
		VBox yahtzee = new VBox(10);
		
		// Game labels
		Label label1 = new Label("Blackjack");
		label1.setFont(Font.font("Elephant"));
		Label label2 = new Label("Checkers");
		label2.setFont(Font.font("Elephant"));
		Label label3 = new Label("Connect 4");
		label3.setFont(Font.font("Elephant"));
		Label label4 = new Label("Yahtzee");
		label4.setFont(Font.font("Elephant"));
		
		// Adds images
		blackjack.getChildren().addAll(label1, imageViews[0]);
		checkers.getChildren().addAll(label2, imageViews[1]);
		connect4.getChildren().addAll(label3, imageViews[2]);
		yahtzee.getChildren().addAll(label4, imageViews[3]);

		gridPane.add(blackjack, 0, 0);
		gridPane.add(checkers, 1, 0);
		gridPane.add(connect4, 0, 1);
		gridPane.add(yahtzee, 1, 1);
		
		blackjack.setOnMouseClicked(e -> {
			if (!new File("save_blackjack.dat").exists()) {
				PlayerNamingScreen.namingScreen(this, "Blackjack");
			} else {
				BlackjackView newBlackjackGame = new BlackjackView(this);
				stage.setScene(newBlackjackGame.getScene());
				stage.centerOnScreen();
			}
		});
		
		checkers.setOnMouseClicked(e -> {
			if (!new File("save_checkers.dat").exists()) {
				PlayerNamingScreen.namingScreen(this, "Checkers");
			} else {
				CheckersView newCheckersGame = new CheckersView(this);
				stage.setScene(newCheckersGame.getScene());
				stage.centerOnScreen();
			}
		});
		
		connect4.setOnMouseClicked(e -> {
			if (!new File("save_connect4.dat").exists()) {
				PlayerNamingScreen.namingScreen(this, "Connect 4");
			} else {
				Connect4View newConnect4Game = new Connect4View(this);
				stage.setScene(newConnect4Game.getScene());
				stage.centerOnScreen();
			}
		});
		
		yahtzee.setOnMouseClicked(e -> {
			if (!new File("save_yahtzee.dat").exists()) {
				PlayerNamingScreen.namingScreen(this, "Yahtzee");
			} else {
				YahtzeeView newYahtzeeGame = new YahtzeeView(this);
				stage.setScene(newYahtzeeGame.getScene());
				stage.centerOnScreen();
			}
		});
		
		window.setCenter(gridPane);
		
		blackjack.setAlignment(Pos.CENTER);
		checkers.setAlignment(Pos.CENTER);
		connect4.setAlignment(Pos.CENTER);
		yahtzee.setAlignment(Pos.CENTER);
		
		gridPane.setAlignment(Pos.CENTER);
	}
	
	/**
	 * Retrieves the Stage which the GUI is presented on
	 * @return a Stage object where the GUI is presented on
	 */
	public Stage getStage() {
	    return guiStage;
	}
	
	/**
	 * Method which allows a game to return to the main menu
	 */
	public void exitToMenu() {
		guiStage.setScene(menuScene);
		guiStage.centerOnScreen();
	}
	
}