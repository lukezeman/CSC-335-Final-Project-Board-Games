package view;

import java.util.Observable;
import java.util.Observer;

import game.Game;
import javafx.application.Application;
import javafx.stage.Stage;

public class GameView extends Application implements Observer{

	

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Something happened");
		arg0.show();
	}

	
	
	@Override
	public void update(Observable o, Object arg) {
		Game gameState = (Game) arg;
		// TODO Auto-generated method stub
		
	}
}
