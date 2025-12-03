package yahtzee;

import java.io.Serializable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class YahtzeeInstance implements Serializable {
	private YahtzeePlayer player1;
	private YahtzeePlayer player2;
	private YahtzeePlayer currentPlayer;	
	private static final long serialVersionUID = 1L;
	
	public YahtzeeInstance(YahtzeeModel model) {
		this.player1 = model.getPlayer1();
		this.player2 = model.getPlayer2();
		this.currentPlayer = model.getCurrentPlayer();
	}
	
	protected YahtzeePlayer getPlayer1() {
		return player1;
	}
	
	protected YahtzeePlayer getPlayer2() {
		return player2;
	}
	
	protected YahtzeePlayer getCurrentPlayer() {
		return currentPlayer;
	}
	
	protected void saveGame() {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("save_yahtzee.dat"))) {
			out.writeObject(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}
	
	public static YahtzeeInstance loadGame() {
		File file = new File("save_yahtzee.dat");
		if (!file.exists()) {
			return null;
		}
		
		// Checks if a saved game exists
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
			return (YahtzeeInstance) in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			return null;
		}
		
	}
	
	
}
