package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BoardGamesView extends Application {

	// This will be for the main page when we run the prorgram.
	// When a certain button is clicked for whichever game to start,
	// we will call the other games' respective view.
	@Override
	public void start(Stage stage) throws Exception {
		System.out.println("Stuff happened!");
		// Filler code for now.
		BorderPane borderPane = new BorderPane();
		Label label = new Label("Hello JavaFX!");
		borderPane.setCenter(label);
		
		Scene scene = new Scene(borderPane, 500, 500);
		stage.setScene(scene);
		stage.setTitle("Super Awesome Board Games");
		stage.show();
	}
	
}
