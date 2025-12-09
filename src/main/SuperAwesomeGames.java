/**
<<<<<<< HEAD
 * @author Achilles Soto
 * @author Charlie Cain
 * @author Luke Zeman
 * @author Aidin Miller
 * Course: CSC335
 * File: SuperAwesomeGames.java
 * Launches a GUI presenting the main menu where a game can be selected
 */
package main;

import javafx.application.Application;
import view.BoardGamesView;

/**
 * Launches the main menu view which portrays all games which can be selected
 */
public class SuperAwesomeGames {

	/**
	 * Starts this program
	 * @param args : a String[] that is meant for command line arguments
	 */
	public static void main(String[] args) {
		Application.launch(BoardGamesView.class, args);
	}

}
