package blackjack;

import java.util.Observable;
import java.util.Observer;

//import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
import view.BoardGamesView;
import view.PlayerNamingScreen;
/**
 * This class represents what is needed to display
 * the Blackjack game in the GUI
 * 
 * @author Aidin Miller
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class BlackjackView implements Observer {
	private BoardGamesView menuView;
	
	private BlackjackModel model;
	private BlackjackController controller;
	
	private String p1Name;
	private String p2Name;
	
	//private Label p1cards;
	private Label p1CardsVal;
	private Label p1Money;
	private TextField p1Bet;
	
	//private Label p2cards;
	private Label p2CardsVal;
	private Label p2Money;
	private TextField p2Bet;
	
	//private Label dealerCards;
	private Label dealerValue;
	
	private Button start;
	private Button hit;
	private Button stand;
	private Label whoseTurn;
	private Label status;
	
	private HBox p1Display;
	private HBox p2Display;
	private HBox dealerDisplay;
	
	private Label p1TitleLabel;
	private Label p2TitleLabel;
	
	private boolean gameOverAlertShown = false;
	
	private Scene scene;
	/**
	 * Constructor for class that sets player names Player 1
	 * and Player 2 by default
	 * 
	 * @param menuView The main menu that will be used in the GUI
	 */
	public BlackjackView(BoardGamesView menuView) {
		
		this(menuView, "Player 1", "Player 2");
	}
	/**
	 * Another constructor that allows for entering player names
	 * 
	 * @param mainMenu The main menu that will be used in the GUI
	 * @param name1 Name of 1st player
	 * @param name2 Name of 2nd player
	 */
	public BlackjackView(BoardGamesView mainMenu, String name1, String name2) {
		// TODO Auto-generated constructor stub
		this.model = new BlackjackModel();
		this.controller = new BlackjackController(model);
		model.addObserver(this);
		
		this.menuView = mainMenu;
		this.p1Name = name1;
		this.p2Name = name2;
		controller.setP1Name(name1);
		controller.setP2Name(name2);
		scene = setupScene();
	}

	/**
	 * This function creates a new scene
	 * 
	 * @return The scene that will be displayed.
	 */
	private Scene setupScene() {
		
		BorderPane root = new BorderPane();

		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");
		
		MenuItem newGame = new MenuItem("New Game");
		newGame.setOnAction(e -> {
			controller.resetGame();
			PlayerNamingScreen.namingScreen(menuView, "Blackjack");
		});
		
		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(e -> {
			controller.saveGame();
			menuView.exitToMenu();
		});
		fileMenu.getItems().addAll(newGame, new SeparatorMenuItem(), exit);
		menuBar.getMenus().add(fileMenu);
		
		root.setTop(menuBar);
		
		VBox gameDisplay = new VBox(20);
		gameDisplay.setAlignment(Pos.CENTER);
		
		VBox house = new VBox(10);
		house.setAlignment(Pos.CENTER);
		
		Label dealLabel = new Label("DEALER");
		dealerDisplay = new HBox(5);
		dealerDisplay.setAlignment(Pos.CENTER);
		dealerValue = new Label("Value: ");
		
		house.getChildren().addAll(dealLabel, dealerDisplay, dealerValue);
		
		HBox players = new HBox(30);
		players.setAlignment(Pos.CENTER);
		
		VBox p1Box = newVbox(1);
		VBox p2Box = newVbox(2);
		
		players.getChildren().addAll(p1Box, p2Box);
		gameDisplay.getChildren().addAll(house, players);
		
		root.setCenter(gameDisplay);
		
		VBox buttonsCol = new VBox(10);
		buttonsCol.setAlignment(Pos.CENTER);
		
		HBox buttons = new HBox(10);
		buttons.setAlignment(Pos.CENTER);
		
		start = new Button("Start");
		start.setOnAction(e -> startGame());
		start.setDisable(false);
		
		hit = new Button("Hit");
		hit.setOnAction(e -> controller.hit());
		hit.setDisable(true);
		
		stand = new Button("Stand");
		stand.setOnAction(e -> controller.stand());
		stand.setDisable(true);
		
		buttons.getChildren().addAll(start, hit, stand);
		whoseTurn = new Label("Place bets");
		status = new Label("");
		
		buttonsCol.getChildren().addAll(buttons, whoseTurn, status);
		root.setBottom(buttonsCol);
		
		controller.loadGame();
		p1Name = controller.getP1Name();
		p2Name = controller.getP2Name();
		p1TitleLabel.setText(p1Name);
		p2TitleLabel.setText(p2Name);
		Scene scene = new Scene(root, 800, 600);
		return scene;
	}
	/**
	 * A getter that gets the scene
	 * 
	 * @return The current scene
	 */
	public Scene getScene() {
		return scene;
	}
	/**
	 * This function updates the display show on the GUI
	 */
	private void updateDisplay() {
		// TODO Auto-generated method stub
		Hand dealerHand = controller.getDealer();
		Hand p1Hand = controller.getP1();
		Hand p2Hand = controller.getP2();
		boolean gameStart = (p1Hand.cardsInHand() > 0 || p2Hand.cardsInHand() > 0);
		int turn = controller.getTurn();
		boolean inProg = (turn != 0) && gameStart;
		dealerDisplay.getChildren().clear();
	    if (dealerHand.cardsInHand() > 0) {
	        for (int i = 0; i < dealerHand.getCards().size(); i++) {
	            CardView cardView = new CardView();
	            if (inProg && i == 0) {
	                cardView.showCardBack();
	            } else {
	                cardView.setCard(dealerHand.getCards().get(i));
	            }
	            dealerDisplay.getChildren().add(cardView);
	        }
	        if (inProg) {
	            dealerValue.setText("Value: ??");
	        } else {
	            dealerValue.setText("Value: " + dealerHand.getValue());
	        }
	    } else {
	        dealerValue.setText("Value: 0");
	    }
	    
	    p1Display.getChildren().clear();
	    for (Card card : p1Hand.getCards()) {
	        CardView cardView = new CardView();
	        cardView.setCard(card);
	        p1Display.getChildren().add(cardView);
	    }
	    p1CardsVal.setText("Value: " + p1Hand.getValue());
	    p1Money.setText("Balance: $" + controller.getP1money());
	    
	    p2Display.getChildren().clear();
	    for (Card card : p2Hand.getCards()) {
	        CardView cardView = new CardView();
	        cardView.setCard(card);
	        p2Display.getChildren().add(cardView);
	    }
	    p2CardsVal.setText("Value: " + p2Hand.getValue());
	    p2Money.setText("Balance: $" + controller.getP2money());
	    
	    boolean hasNoMoney = (controller.getP1money() <= 0 || controller.getP2money() <= 0);
	    
	    start.setDisable(inProg || hasNoMoney);
	    p1Bet.setDisable(inProg || hasNoMoney);
	    p2Bet.setDisable(inProg || hasNoMoney);
	    hit.setDisable(!inProg);
	    stand.setDisable(!inProg);
	    
	    if (turn == 0 && gameStart) {
	        whoseTurn.setText("Game Over");
	        status.setText(controller.getP1Status() + " | " + controller.getP2Status());
	        
	        controller.setGameOver();
	       
	        if (controller.isGameOver() && !gameOverAlertShown) {
	            gameOverAlertShown = true;
	            Alert gameOver = new Alert(Alert.AlertType.INFORMATION);
	            gameOver.setTitle("Game Over!");
	            String winner = controller.getWinner();
	            String p1 = controller.getP1Name();
	            String p2 = controller.getP2Name();
	            if (winner.equals("Player 1 wins")) {
	                winner = p1 + " wins";
	            } else if (winner.equals("Player 2 wins")) {
	                winner = p2 + " wins";
	            }
	            gameOver.setContentText(winner + "!\n\n" +
	                p1 + ": $" + controller.getP1money() + "\n" +
	                p2 + ": $" + controller.getP2money());
	            gameOver.setHeaderText("Game Over!");
	            gameOver.showAndWait();
	        }
	    }
	    else if (turn == 1 && gameStart) {
	        whoseTurn.setText(p1Name + "'s turn");
	        status.setText("");
	        gameOverAlertShown = false;
	    }
	    else if (turn == 2 && gameStart) {
	        whoseTurn.setText(p2Name + "'s turn");
	        status.setText("");
	    }
	    else if (hasNoMoney) {
	        whoseTurn.setText("Game Over");
	        status.setText("Start a New Game to play again");
	    }
	    else {
	        whoseTurn.setText("Place your bets and click Start");
	        status.setText("");
	        gameOverAlertShown = false;
	    }
	}
	

	/**
	 * This function checks to see if the bet input is valid and starts the game if so.
	 */
	private void startGame() {
		// TODO Auto-generated method stub
		try {
		int bet1 = Integer.parseInt(p1Bet.getText());
		int bet2 = Integer.parseInt(p2Bet.getText());
		if (bet1 > controller.getP1money() || bet1 <= 0) {
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setTitle("Error");
			a.setHeaderText("Invalid Bet");
			a.setContentText("Player cannot bet the amount they put in");
			a.showAndWait();
			return;
		}
		if (bet2 > controller.getP2money() || bet2 <= 0) {
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setTitle("Error");
			a.setHeaderText("Invalid Bet");
			a.setContentText("Player cannot bet the amount they put in");
			a.showAndWait();
			return;
		}
		if (controller.startGame(bet1, bet2)) {
			p1Bet.clear();
			p2Bet.clear();
			updateDisplay();
		}
		else {
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setTitle("Error");
			a.setHeaderText("Could not start game");
			a.setContentText("");
			a.showAndWait();
		}
		} catch (NumberFormatException e) {
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setTitle("Error");
			a.setHeaderText("Invalid input");
			a.setContentText("The input entered must be an integer");
			a.showAndWait();
		}
	}
	/**
	 * This function creates a VBox based on  the current state of the game.
	 * 
	 * @param i an int representing the player
	 * @return a VBox of the player
	 */
	private VBox newVbox(int i) {
		VBox box = new VBox(10);
		box.setAlignment(Pos.CENTER);
		box.setPrefWidth(350);
		
		Label title = new Label(i == 1 ? p1Name : p2Name);
		
		if (i == 1) {
			p1TitleLabel = title;
			p1Display = new HBox(5);
			p1Display.setAlignment(Pos.CENTER);
	        
	        p1CardsVal = new Label("Value: 0");
	        p1Money = new Label("Balance: $500");
	        
	        HBox bet = new HBox(5);
	        bet.setAlignment(Pos.CENTER);
	        Label betLabel = new Label("Bet:");
	        p1Bet = new TextField("10");
	        p1Bet.setPrefWidth(80);
	        bet.getChildren().addAll(betLabel, p1Bet);
	        
	        box.getChildren().addAll(title, p1Display, p1CardsVal, p1Money, bet);
		}
		else {
			p2TitleLabel = title;
			p2Display = new HBox(5);
	        p2Display.setAlignment(Pos.CENTER);
	        
	        p2CardsVal = new Label("Value: 0");
	        p2Money = new Label("Balance: $500");
	        
	        HBox bet = new HBox(5);
	        bet.setAlignment(Pos.CENTER);
	        Label betLabel = new Label("Bet:");
	        p2Bet = new TextField("10");
	        p2Bet.setPrefWidth(80);
	        bet.getChildren().addAll(betLabel, p2Bet);
	        
	        box.getChildren().addAll(title, p2Display, p2CardsVal, p2Money, bet);
		}
		return box;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		updateDisplay();
	}
}
