package view;

import blackjack.BlackjackView;
import checkers.CheckersView;
import connect4.Connect4View;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import yahtzee.YahtzeeView;

public class PlayerNamingScreen {
	public static void namingScreen(BoardGamesView mainMenu, String game) {		
		BorderPane pane = new BorderPane();
		HBox hBox = new HBox(50);
		VBox player1 = new VBox(10);
		VBox player2 = new VBox(10);
		
		Label player1Label = new Label("Player 1");
		player1Label.setFont(Font.font("System", FontWeight.BOLD, 20));
		Label player2Label = new Label("Player 2");
		player2Label.setFont(Font.font("System", FontWeight.BOLD, 20));
		
		TextField player1Name = new TextField("Enter player 1's name!");
		TextField player2Name = new TextField("Enter player 2's name!");
		
		player1.getChildren().addAll(player1Label, player1Name);
		player2.getChildren().addAll(player2Label, player2Name);
		hBox.getChildren().addAll(player1, player2);
		hBox.setPadding(new Insets(50));
		hBox.setAlignment(Pos.CENTER);
		
		Label topLabel = new Label(game);
		topLabel.setFont(Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, 30));
		pane.setTop(topLabel);
		pane.setCenter(hBox);
		BorderPane.setAlignment(topLabel, Pos.CENTER);
		
		Button start = new Button("Start!");
		start.setOnAction(event -> {
			String name1 = player1Name.getText();
			String name2 = player2Name.getText();
			if (name1.equals("Enter player 1's name!") || name1.isBlank()) {
				name1 = "Player 1";
			} if (name2.equals("Enter player 2's name!") || name2.isBlank()) {
				name2 = "Player 2";
			}
			
			if (game.equals("Blackjack")) {
				BlackjackView newBlackjackGame = new BlackjackView(mainMenu, name1, name2);
				mainMenu.getStage().setScene(newBlackjackGame.getScene());
				mainMenu.getStage().centerOnScreen();
			} else if (game.equals("Checkers")) {
				CheckersView newCheckersGame = new CheckersView(mainMenu, name1, name2);
				mainMenu.getStage().setScene(newCheckersGame.getScene());
				mainMenu.getStage().centerOnScreen();
			} else if (game.equals("Connect 4")) {
				Connect4View newConnect4Game = new Connect4View(mainMenu, name1, name2);
				mainMenu.getStage().setScene(newConnect4Game.getScene());
				mainMenu.getStage().centerOnScreen();
			} else {
				YahtzeeView newYahtzeeGame = new YahtzeeView(mainMenu, name1, name2);
				mainMenu.getStage().setScene(newYahtzeeGame.getScene());
				mainMenu.getStage().centerOnScreen();
			} 

		});
		
		pane.setBottom(start);
		BorderPane.setAlignment(start, Pos.CENTER);
		start.setPrefSize(100, 50);
		start.setFont(Font.font("System", 15));
		pane.setPadding(new Insets(20));
				
		Scene scene = new Scene(pane);
		mainMenu.getStage().setScene(scene);
		mainMenu.getStage().centerOnScreen();
		
	}
}
