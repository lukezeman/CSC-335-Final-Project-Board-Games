package main;

import java.lang.management.ManagementFactory;

import javafx.application.Application;
import view.BoardGamesView;

public class SuperAwesomeGames {

	public static void main(String[] args) {
		
		 // Error checking for getting javaFX to run.
		 //Make sure the two boxes below VM arguments are unchecked. It won't run if 
		 //they're checked.
		/*
		 System.out.println("Actual VM args:");
	     for (String arg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
	     	System.out.println("  " + arg);
	     }
		 */
		  
		 
		
		
		Application.launch(BoardGamesView.class, args);

	}

}
