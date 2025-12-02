package checkers;

import java.util.Observable;
import java.util.Observer;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import view.BoardGamesView;

public class CheckersView implements Observer {

	private BoardGamesView menuView;
	
	private Scene scene;
	
	public CheckersView(BoardGamesView menuView) {
		this.menuView = menuView;
		scene = setupScene();
	}
	
	
	/*
	 * When making your GUI, instead of setting up a stage and stuff, just
	 * set up the scene you want in here and the BoardGamesView class will 
	 * handle displaying this to the screen. Feel free to use the exitToMenu
	 * methods in the menuView, as well as whatever other methods are added
	 */
	private Scene setupScene() {
		
		BorderPane borderPane = new BorderPane();
		Label label = new Label("NOT FINISHED!!!");
		borderPane.setCenter(label);
		
		Button exitButton = new Button("Exit");
		exitButton.setOnAction(e -> {
			menuView.exitToMenu();
		});
		
		borderPane.setTop(exitButton);
		
		Scene scene = new Scene(borderPane, 500, 500);
		
		return scene;
	}
	
	public Scene getScene() {
		return scene;
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
