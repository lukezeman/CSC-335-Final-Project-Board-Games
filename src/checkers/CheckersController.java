package checkers;

public class CheckersController {

	private CheckersView view;
	private CheckersModel model;
	private boolean isPlayerOne;
	
	public CheckersController() {
		this.isPlayerOne = true;
	}
	
	public boolean isGameOver() {
		/*
		 * The game over scene should be fairly simple to calculate, since
		 * the game is over only when every single piece of one of the two
		 * players is removed. 
		 * ACTUALLY NO!!!!!! A game can end if one side has no legal moves. 
		 * This is not counted as a draw, but rather as a victory for the
		 * other side.
		 */
		return false;
	}
	
	
	public void takeInput(int xCoord, int yCoord) {
		boolean switchPlayer = model.takeInput(xCoord, yCoord, isPlayerOne);
		// If the input given by the user passed the turn somehow, switch the player
		if (switchPlayer) {
			//System.out.println("here :)");
			isPlayerOne = !(isPlayerOne);
			// Clear 
		}
	}
	
	/**
	 * Starts the game. Should probably add a "saved" game state here or
	 * something, but that shouldn't be very hard to implement once the 
	 * core stuff starts working.
	 * @param view
	 */
	public void startGame(CheckersView view) {
		this.view = view;
		this.model = new CheckersModel(view);
	}
	
	public boolean isPlayerOne() {
		return isPlayerOne;
	}
}
