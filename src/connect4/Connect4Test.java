package connect4;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Connect4Test {

	@Test
	void testModelSetTurn() {
		Connect4Model model = new Connect4Model();
		assertTrue(model.setTurn(0, 'r'));
		model.setTurn(5, 'r');
		
		char[][] board = model.getBoard();
		
		assertTrue(board[5][6] == 'r');

		model.setTurn(5, 'r');
		model.setTurn(5, 'r');
		model.setTurn(5, 'r');
		model.setTurn(5, 'r');
		assertFalse(model.setTurn(5, 'r'));

	}
	
	@Test
	void testModelGameOver() {
		System.out.println("<------------------------->");

		Connect4Model model = new Connect4Model();
		
		assertEquals(0 ,model.isGameOver(5, 6));

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

}
