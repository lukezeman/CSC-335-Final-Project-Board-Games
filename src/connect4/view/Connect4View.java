package connect4.view;

import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Connect4View extends Application implements Observer {
	
	@Override
	public void start(Stage stage) throws Exception {
		startNewGame(stage);
	}
	
	// Helper function, when user clicks new game, call this function
	private void startNewGame(Stage stage) {
		// Filler code for now.
		BorderPane borderPane = new BorderPane();
		Label label = new Label("Hello JavaFX!");
		borderPane.setCenter(label);
		
		Scene scene = new Scene(borderPane, 500, 500);
		stage.setScene(scene);
		stage.setTitle("Super Awesome Board Games");
		stage.show();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
	// Remove the main after the view works, will be called from
	// BoardGamesView
	public static void main(String[] args) {
		launch();

	}
}
