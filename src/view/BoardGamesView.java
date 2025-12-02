package view;

import blackjack.BlackjackView;
import checkers.CheckersView;
import connect4.Connect4View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import yahtzee.YahtzeeView;

public class BoardGamesView extends Application {

	private Stage guiStage;
	private Scene menuScene;
	// This will be for the main page when we run the prorgram.
	// When a certain button is clicked for whichever game to start,
	// we will call the other games' respective view.
	@Override
	public void start(Stage stage) throws Exception {
		// Filler code for now.
		
		guiStage = stage;
		
		BorderPane mainWindow = new BorderPane();
		Label label = new Label("GAMES PLACEHOLDER");
		mainWindow.setTop(label);
		
		setupMiddle(mainWindow, stage);
		
		Scene scene = new Scene(mainWindow, 500, 500);
		menuScene = scene;
		stage.setScene(scene);
		stage.setTitle("Super Awesome Board Games");
		stage.show();
	}
	
	private void setupMiddle(BorderPane window, Stage stage) {
		HBox mainBox = new HBox();
		VBox leftButtons = new VBox();
		VBox rightButtons = new VBox();
		
		Button checkersButton = new Button("Click to play Checkers");
		Button connect4Button = new Button("Click to play Connect 4");
		Button blackjackButton = new Button("Click to play Blackjack");
		Button yahtzeeButton = new Button("Click to play Yahtzee");
		
		checkersButton.setOnAction(e -> {
			CheckersView newCheckersGame = new CheckersView(this);
			stage.setScene(newCheckersGame.getScene());
		});
		
		connect4Button.setOnAction(e -> {
			Connect4View newConnect4Game = new Connect4View(this);
			stage.setScene(newConnect4Game.getScene());
		});
		
		blackjackButton.setOnAction(e -> {
			BlackjackView newBlackjackGame = new BlackjackView(this);
			stage.setScene(newBlackjackGame.getScene());
		});
		
		yahtzeeButton.setOnAction(e -> {
			YahtzeeView newYahtzeeGame = new YahtzeeView(this);
			stage.setScene(newYahtzeeGame.getScene());
		});
		
		leftButtons.getChildren().add(checkersButton);
		leftButtons.getChildren().add(connect4Button);
		rightButtons.getChildren().add(blackjackButton);
		rightButtons.getChildren().add(yahtzeeButton);
		mainBox.getChildren().add(leftButtons);
		mainBox.getChildren().add(rightButtons);
		window.setCenter(mainBox);
	}
	
	public Stage getStage() {
	    return guiStage;
	}
	
	public void exitToMenu() {
		guiStage.setScene(menuScene);
	}
	
}








