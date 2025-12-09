package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;

import org.junit.jupiter.api.Test;

import checkers.CheckersController;
import checkers.CheckersInstance;

class CheckersTests {

	private static final int BOARD_SIZE = 8;
	
	@Test
	void testDefaultGameFlow() {
		CheckersController gameController = new CheckersController();
		gameController.startGame(null, null, "Player 1", "Player 1");
		assertEquals(false, gameController.isGameOver());
		// Move piece from (5, 3) to (4, 1). Should be player 2's turn
		gameController.takeInput(4, 0);
		gameController.takeInput(5, 0);
		gameController.takeInput(1, 0);
		gameController.takeInput(5, 3);
		gameController.takeInput(4, 1);
		
		// Move black piece from (2, 3) to (3, 2). Player 1 has a legal capture
		gameController.takeInput(2, 3);
		gameController.takeInput(3, 2);
		// Should do nothing since this piece has a legal capture to do
		gameController.takeInput(4, 1);
		gameController.takeInput(3, 0);
		// Should do nothing since another piece has a legal capture to do
		gameController.takeInput(5, 6);
		gameController.takeInput(4, 7);
		// Should do the capture, moving white piece from (4, 1) to (2, 3) and removing black piece at (3, 2)
		gameController.takeInput(4, 1);
		gameController.takeInput(2, 3);
		assertEquals(false, gameController.isGameOver());
	}

	@Test
	void testEndGamePosition() throws IOException {
		CheckersController testedController = new CheckersController();
		ObjectInputStream testedStream = new ObjectInputStream(new FileInputStream(new File("checkers_test.dat")));
		assertEquals(false, testedController.isGameOver());
		testedController.startGame(null, testedStream, null, null);
		assertEquals(false, testedController.isGameOver());
		testedController.takeInput(3, 4);
		assertEquals(false, testedController.isGameOver());
		testedController.takeInput(4, 3);
		assertEquals(false, testedController.isGameOver());
		testedController.takeInput(5, 2);
		assertEquals(false, testedController.isGameOver());
		testedController.takeInput(3, 4);
		assertEquals(false, testedController.isGameOver());
		testedController.takeInput(3, 4);
		assertEquals(false, testedController.isGameOver());
		testedController.takeInput(1, 2);
		assertEquals(true, testedController.isGameOver());
		assertEquals(true, testedController.playerOneWon());
	}
}





















