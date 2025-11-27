/**
 * @author Achilles Soto
 * Course: CSC 335
 * File: Connect4Controller.java
 * Allows user to interact with Connect4 game, calling methods 
 * from the model
 */
package connect4;

/**
 * This class defines methods to call the model class to execute game commands,
 * connecting the view with the model.
 */
public class Connect4Controller {
	private Connect4Model model;
	
	/**
	 * Allows for an instance of this class to be made and sets the model
	 * 
	 * @param model Connect4Model which will handle all game logic
	 */
	public Connect4Controller (Connect4Model model) {
		this.model = model;
	}
	
	/**
	 * Calls method to retrieve the current Connect4 game
	 * 
	 * @return a char[][] which holds all player's pieces
	 */
	public char[][] getBoard(){
		return model.getBoard();
	}
	
	/**
	 * Calls method to retrieve the playerTurn field
	 * 
	 * @return a char which represents the player color
	 */
	public char getTurn() {
		return model.getTurn();
	}
	
	/**
	 * Calls method to set the playerTurn field
	 * 
	 * @param player - a char which represents the player color
	 */
	public void setTurn(char player) {
		model.setTurn(player);
	}
	
	/**
	 * Calls method to add a player move
	 * @param col - int that represents a board column
	 */
	public void setMove(int col) {
		model.setMove(col);
	}
	
	/**
	 * Calls method to check if the game is over
	 * 
	 * @param row - an int which is the row a piece landed in
	 * @param col - an int which is the column a piece was dropped in
	 * @return an int, 1(Game over by winner), 0 (Not game over), -1 (Game over by draw).
	 */
	public int isGameOver(int row, int col) {
		return model.isGameOver(row, col);
	}
	
}
