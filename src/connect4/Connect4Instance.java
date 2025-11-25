package connect4;

import java.io.Serializable;

public class Connect4Instance implements Serializable {
	
	private char[][] board;
	private char playerTurn;
	
	private static final long serialVersionUID = 1L;
	
	public Connect4Instance(Connect4Model model) {
		board = model.getBoard();
		playerTurn = model.getTurn();
	}

}
