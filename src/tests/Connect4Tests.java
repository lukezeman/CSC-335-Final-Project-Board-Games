package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;

import connect4.Connect4Controller;
import connect4.Connect4Instance;
import connect4.Connect4Model;

class Connect4Tests {

	@Test
	void testModelSetTurn() {
		Connect4Model model = new Connect4Model();
		model.setMove(0);
		
		char[][] board = model.getBoard();
		
		assertTrue(board[5][0] == 'y');

	}
	
	@Test
	void testModelPlayers() {
		Connect4Model model = new Connect4Model();
		String[] testPlayers = {"TestP1", "TestP2"};
		model.setPlayers(testPlayers);
		
		assertEquals(testPlayers, model.getPlayers());

	}
	
	@Test
	void testModelGameOver() {
		System.out.println("<------------------------->");

		Connect4Model model = new Connect4Model();
		
		model.setMove(0);
		assertEquals(0 ,model.isGameOver(5, 0));

		char[][] board = new char[6][7];
		
		char p = 'y';
		int count = 0;
		for (char[] row : board) {
			for (int i = 0; i < row.length; i++) {
				if (count == 2 && p == 'y') {
					count = 0;
					p = 'r';
				}
				else if (count == 2 && p == 'r') {
					count = 0;
					p = 'y';
				}
				row[i] = p;
				count++;
			}
			count = 0;
		}
		
		for (char[] row1 : board) {
			for (char col2 : row1) {
				if (col2 == '\0')System.out.print(".");
				else System.out.print(col2);
			}
			System.out.println();
		}
		
		System.out.println();
		
		model.setBoard(board);
		assertEquals(-1 ,model.isGameOver(0, 0));
		System.out.println("<------------------------->");

	}
	
	@Test
	void testModelDiagWin() {
		System.out.println("<------------------------->");
		Connect4Model model = new Connect4Model();
		char[][] board = new char[6][7];
		board[5][6] = 'r';
		board[4][5] = 'r';
		board[3][4] = 'r';
		board[2][3] = 'r';
		board[1][2] = 'r';
		board[0][1] = 'r';
		
		for (char[] row1 : board) {
			for (char col2 : row1) {
				if (col2 == '\0')System.out.print(".");
				else System.out.print(col2);
			}
			System.out.println();
		}
		
		System.out.println();

		
		model.setBoard(board);
		
		System.out.println("Win D1 check");
		assertEquals(1 ,model.isGameOver(4, 5));
		
		board = new char[6][7];
		board[5][0] = 'r';
		board[4][1] = 'r';
		board[3][2] = 'r';
		board[2][3] = 'r';
		board[1][4] = 'r';
		board[0][5] = 'r';
		
		for (char[] row1 : board) {
			for (char col2 : row1) {
				if (col2 == '\0')System.out.print(".");
				else System.out.print(col2);
			}
			System.out.println();
		}
		
		System.out.println();
		
		model.setBoard(board);
		
		System.out.println("Win D2 check");
		assertEquals(1 ,model.isGameOver(4, 1));
		System.out.println("<------------------------->");
	}
	
	@Test
	void testModelHoriWin() {
		System.out.println("<------------------------->");
		Connect4Model model = new Connect4Model();
		char[][] board = new char[6][7];
		board[5][6] = 'r';
		board[5][5] = 'r';
		board[5][4] = 'r';
		board[5][3] = 'r';
		board[5][2] = 'r';
		
		for (char[] row1 : board) {
			for (char col2 : row1) {
				if (col2 == '\0')System.out.print(".");
				else System.out.print(col2);
			}
			System.out.println();
		}
		
		System.out.println();
		
		model.setBoard(board);
		
		System.out.println("Win Hori check");
		assertEquals(1 , model.isGameOver(5, 4));
		System.out.println("<------------------------->");

	}
	
	@Test
	void testModelVertWin() {
		System.out.println("<------------------------->");
		Connect4Model model = new Connect4Model();
		char[][] board = new char[6][7];
		board[5][5] = 'r';
		board[4][5] = 'r';
		board[3][5] = 'r';
		board[2][5] = 'r';
		board[1][5] = 'r';
		board[0][5] = 'r';
		
		for (char[] row1 : board) {
			for (char col2 : row1) {
				if (col2 == '\0')System.out.print(".");
				else System.out.print(col2);
			}
			System.out.println();
		}
		
		System.out.println();

		
		model.setBoard(board);
		
		System.out.println("Win Vert check");
		assertEquals(1 ,model.isGameOver(4, 5));
		
		System.out.println("<------------------------->");

	}
	
	@Test
	void testTurns() {
		Connect4Model model = new Connect4Model();
		assertTrue(model.getTurn() == 'y');
		model.setTurn('r');
		assertTrue(model.getTurn() == 'r');
	}
	
	@Test
	void testGameSave() {
		Connect4Model model = new Connect4Model();
		assertTrue(model.saveGame());
	}
	
	@Test 
	void testController() {
		Connect4Model model = new Connect4Model();
		Connect4Controller control = new Connect4Controller(model);
		assertEquals(model.getBoard(), control.getBoard());
		control.setMove(0);
		assertEquals(model.isGameOver(5,0), control.isGameOver(5,0));
		control.setTurn('r');
		assertEquals(model.getTurn(), control.getTurn());
		char[][] board = new char[6][7];
				
		char p = 'y';
		int count = 0;
		for (char[] row : board) {
			for (int i = 0; i < row.length; i++) {
				if (count == 2 && p == 'y') {
					count = 0;
					p = 'r';
				}
				else if (count == 2 && p == 'r') {
					count = 0;
					p = 'y';
				}
				row[i] = p;
				count++;
			}
			count = 0;
		}
		control.setBoard(board);
		assertEquals(model.getBoard(), control.getBoard());
		
		assertTrue(control.saveGame());
		
		String[] testPlayers = {"TestP1", "TestP2"};
		control.setPlayers(testPlayers);
		
		assertEquals(testPlayers, control.getPlayers());


	}
	
	@Test
	void testInstance() {
		File file = new File("save_connect4.dat");
		if (file.exists()) file.delete();
		Connect4Model model = new Connect4Model();
		Connect4Instance i = new Connect4Instance(model);
		assertEquals(model.getPlayers(), i.getPlayers());
		assertEquals(model.getBoard(), i.getBoard());
		assertEquals(model.getTurn(), i.getTurn());
		assertNull(Connect4Instance.loadGame());
		model.saveGame();
		Object loaded = Connect4Instance.loadGame();
		assertTrue(loaded instanceof Connect4Instance);


	}
	

}
