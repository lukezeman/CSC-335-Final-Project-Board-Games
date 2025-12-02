package blackjack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BlackjackController {
	private BlackjackModel m;
	
	public BlackjackController(BlackjackModel model) {
		m = model;
	}
	public void hit() {
		m.hit();
	}
	public void stand() {
		m.stand();
	}
	public Hand getP1() {
		return m.getP1();
	}
	public Hand getP2() {
		return m.getP2();
	}
	public Hand getDealer() {
		return m.getDealer();
	}
	public int getP1money() {
		return m.getP1Money();
	}
	public int getP2money() {
		return m.getP2Money();
	}
	public int getP1Bet() {
		return m.getP1Bet();
	}
	public int getP2Bet() {
		return m.getP2Bet();
	}
	public int getTurn() {
		return m.getTurn();
	}
	public boolean isGameOver() {
		return m.getIsOver();
	}
	public boolean p1TurnOver() {
		return m.isP1Done();
	}
	public boolean p2TurnOver() {
		return m.isP2Done();
	}
	public String getP1Status() {
		return m.getStatus(1);
	}
	public String getP2Status() {
		return m.getStatus(2);
	}
	public void setGameOver() {
		m.setIsOver(getP1money() == 0 || getP2money() == 0);
	}
	public String getWinner() {
		if (getP1money() > getP2money()) {
			return "Player 1 wins";
		}
		else if (getP1money() < getP2money()) {
			return "Player 2 wins";
		}
		else {
			return "Tie";
		}
	}
	public void saveGame() {
		try {
			m.deleteObservers();
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("blackjack_save.dat"));
			out.writeObject(m.getInstance());
			out.close();
		} catch (IOException e) {
			System.err.println("Error saving game");
			e.printStackTrace();
		}
	}
	public void loadGame() {
		File f = new File("blackjack_save.dat");
		if (!f.exists()) {
			return;
		}
		
		try (ObjectInputStream in = new ObjectInputStream( new FileInputStream("blackjack_save.dat"))) {
			BlackjackInstance instance = (BlackjackInstance) in.readObject();
			m.setInstance(instance);
		}
		catch (IOException e) {
			System.err.println("Error loading game");
		}
		catch (ClassNotFoundException e) {
			System.err.println("Save file corrupted");
		}
	}
	public void deleteSave() {
		File f = new File("blackjack_save.dat");
		if (f.exists()) {
			f.delete();
		}
	}
	public void resetGame() {
		m.reset();
		deleteSave();
	}
	public boolean startGame(int bet1, int bet2) {
		// TODO Auto-generated method stub
		return m.start(bet1, bet2);
	}
	public boolean isGameInProgress() {
		return getTurn() != 0 || !isGameOver();
	}
}
