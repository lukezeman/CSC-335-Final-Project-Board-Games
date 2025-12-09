/**
 * @author Luke Zeman
 * Assignment: CSC 335 Team Project
 * File: YahtzeeView.java
 * Date: 12/08/2025
 * 
 * Description: This class represents the view component of the Yahtzee game. It implements
 * 			 	the Observer interface to update the UI based on changes in the YahtzeeModel.
 */

package yahtzee;

import view.BoardGamesView;
import view.PlayerNamingScreen;
import java.io.File;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;

/**
 * This class represents the view component of the Yahtzee game.
 */
@SuppressWarnings("deprecation")
public class YahtzeeView implements Observer {
	private BoardGamesView mainMenu;
	private Scene scene;
	private Stage stage;
	private YahtzeeController controller;
	private YahtzeeModel model;
	private Image[] dieImages = new Image[6];
	private ImageView[] diceViews = new ImageView[5];
	private Circle[] holdIndicators = new Circle[5];
	private BorderPane borderPane;
	private Label[] categoryLabels1 = new Label[23];
	private Label[] categoryLabels2 = new Label[23];
	private YahtzeeCategory[] categoryMapping = new YahtzeeCategory[23];
	private GridPane player1Pane;
	private GridPane player2Pane;
	private Button button;
	private Label infoLabel;
	private File saveFile = new File("save_yahtzee.dat");

	/**
	 * Constructor for loading a saved Yahtzee game.
	 * 
	 * @param mainMenu - The main menu view to return to after the game.
	 */
	public YahtzeeView(BoardGamesView mainMenu) {
		this.mainMenu = mainMenu;
		this.stage = mainMenu.getStage();

		for (int i = 0; i < 6; i++) {
			dieImages[i] = new Image(getClass().getResourceAsStream("/dice/die" + (i + 1) + ".png"));
		}

		YahtzeeInstance loadedGame = YahtzeeInstance.loadGame();

		setupUI(null, null, loadedGame);

	}

	/**
	 * Constructor for starting a new Yahtzee game.
	 * 
	 * @param mainMenu - The main menu view to return to after the game.
	 * @param player1  - The name of player 1.
	 * @param player2  - The name of player 2.
	 */
	public YahtzeeView(BoardGamesView mainMenu, String player1, String player2) {
		this.mainMenu = mainMenu;
		this.stage = mainMenu.getStage();

		for (int i = 0; i < 6; i++) {
			dieImages[i] = new Image(getClass().getResourceAsStream("/dice/die" + (i + 1) + ".png"));
		}

		setupUI(player1, player2, null);
	}

	/**
	 * Sets up the user interface for the Yahtzee game.
	 * 
	 * @param player1Name - The name of player 1 (null if loading a game).
	 * @param player2Name - The name of player 2 (null if loading a game).
	 * @param loadedGame  - The loaded YahtzeeInstance (null if starting a new game).
	 */
	private void setupUI(String player1Name, String player2Name, YahtzeeInstance loadedGame) {
		if (loadedGame == null) {
			model = new YahtzeeModel(player1Name, player2Name);
			controller = new YahtzeeController(model);
		} else {
			model = new YahtzeeModel(loadedGame);
			controller = new YahtzeeController(model);
		}
		model.addObserver(this);
		borderPane = new BorderPane();
		setupDiceandMenu();
		setupButtonAndLabel();
		setupScorecards();

		if (loadedGame == null) {
			categoryLabels1[0].setText(player1Name);
			categoryLabels2[0].setText(player2Name);
		} else {
			loadScorecard();
		}

		categoryLabels1[0].setText(player1Name);
		categoryLabels2[0].setText(player2Name);

		scene = new Scene(borderPane, 500, 800);
		stage.setScene(scene);
		stage.show();
		stage.centerOnScreen();

		stage.setOnCloseRequest(event -> {
			if (!controller.isGameOver()) {
				controller.saveGame();
			}
		});
	}

	/**
	 * Sets up the dice area and menu bar.
	 */
	private void setupDiceandMenu() {
		VBox top = new VBox();
		HBox diceArea = new HBox(20);
		diceArea.setAlignment(Pos.CENTER);

		for (int i = 0; i < 5; i++) {
			diceViews[i] = new ImageView(dieImages[0]);
			diceViews[i].setFitWidth(60);
			diceViews[i].setFitHeight(60);
			diceViews[i].setVisible(false);

			holdIndicators[i] = new Circle(5);
			holdIndicators[i].setFill(Color.ORANGE);
			holdIndicators[i].setVisible(false);

			VBox dieContainer = new VBox(5);
			dieContainer.setAlignment(Pos.CENTER);
			dieContainer.getChildren().addAll(diceViews[i], holdIndicators[i]);

			diceArea.getChildren().add(dieContainer);
			diceArea.setPadding(new Insets(10, 5, 5, 5));
			diceArea.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));

			final int index = i;
			diceViews[i].setOnMouseClicked(e -> {
				toggleHold(index);

				if (checkAllHeld()) {
					toggleHold(index);
					Alert a = new Alert(Alert.AlertType.WARNING);
					a.setHeaderText("Cannot hold all die!");
					a.setContentText("Hold some die to keep or score a category!");
					a.showAndWait();
				}

			});

		}

		MenuBar menuBar = setupMenu();
		top.getChildren().addAll(menuBar, diceArea);

		borderPane.setTop(top);
	}

	/**
	 * Toggles the hold status of a die at the specified index.
	 * 
	 * @param index - The index of the die to toggle hold status.
	 */
	private void toggleHold(int index) {
		YahtzeeDie die = controller.getCurrentPlayer().getDice()[index];
		die.toggleHold();

		holdIndicators[index].setVisible(die.isHeld());

	}

	/**
	 * Animates the rolling of a die at the specified index to its final value.
	 * 
	 * @param dieIndex   - The index of the die to animate.
	 * @param finalValue - The final value of the die after rolling.
	 */
	private void animateDiceRoll(int dieIndex, int finalValue) {
		ImageView dieView = diceViews[dieIndex];

		RotateTransition rotate = new RotateTransition(Duration.seconds(1), dieView);
		rotate.setByAngle(360 * 3);

		Timeline timeline = new Timeline();
		for (int i = 0; i < 10; i++) {
			int face = (int) (Math.random() * 6);
			KeyFrame keyFrame = new KeyFrame(Duration.millis(i * 100), e -> dieView.setImage(dieImages[face]));
			timeline.getKeyFrames().add(keyFrame);
		}

		rotate.setOnFinished(e -> dieView.setImage(dieImages[finalValue - 1]));

		rotate.play();
		timeline.play();
	}

	/**
	 * Checks if all dice are currently held.
	 * 
	 * @return true if all dice are held, false otherwise.
	 */
	private boolean checkAllHeld() {
		for (int i = 0; i < 5; i++) {
			if (!holdIndicators[i].isVisible()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Sets up the roll button and information label.
	 */
	private void setupButtonAndLabel() {
		VBox vBox = new VBox(10);
		infoLabel = new Label(controller.getCurrentPlayer().getName() + "'s Turn! Click Roll!");
		infoLabel.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.ITALIC, 20));
		infoLabel.setTextFill(Color.RED);
		button = new Button("Roll!");

		button.setPrefWidth(100);
		button.setPrefHeight(50);
		button.setFont(Font.font("System", 20));

		button.setOnAction(e -> {
			int[] values = controller.rollDice();
			button.setDisable(true);
			for (int i = 0; i < 5; i++) {
				diceViews[i].setVisible(true);
				if (!controller.getCurrentPlayer().getDice()[i].isHeld()) {
					animateDiceRoll(i, values[i]);
				}
			}

			player1Pane.setDisable(true);
			player2Pane.setDisable(true);
			infoLabel.setText("");

			// Disable button, holds, and scorecards for 1 second to allow animation to finish.
			Timeline enable = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
				if (controller.getRollsRemaining() == 0) {
					button.setDisable(true);
					for (int i = 0; i < 5; i++) {
						diceViews[i].setDisable(true);
						holdIndicators[i].setVisible(false);
					}
				} else {
					button.setDisable(false);
				}

				if (controller.getCurrentPlayer() == controller.getPlayer1()) {
					player1Pane.setDisable(false);
				} else {
					player2Pane.setDisable(false);
				}

				infoLabel.setText("Click a die to hold it or score a category!");

			}));

			enable.play();

		});

		vBox.setAlignment(Pos.CENTER);
		vBox.getChildren().addAll(infoLabel, button);
		borderPane.setCenter(vBox);
	}

	/**
	 * Sets up the scorecards for both players.
	 */
	private void setupScorecards() {
		String[] categories = { "PLAYER", "UPPER SECTION", "Ones", "Twos", "Threes", "Fours", "Fives", "Sixes",
				"TOTAL SCORE", "BONUS", "TOTAL", "LOWER SECTION", "3 of a kind", "4 of a kind", "Full House",
				"Sm. Straight", "Lg. Straight", "YAHTZEE", "Chance", "YAHTZEE BONUS", "TOTAL UPPER", "TOTAL LOWER",
				"GRAND TOTAL" };

		categoryMapping[0] = null;
		categoryMapping[1] = null;
		categoryMapping[2] = YahtzeeCategory.ONES;
		categoryMapping[3] = YahtzeeCategory.TWOS;
		categoryMapping[4] = YahtzeeCategory.THREES;
		categoryMapping[5] = YahtzeeCategory.FOURS;
		categoryMapping[6] = YahtzeeCategory.FIVES;
		categoryMapping[7] = YahtzeeCategory.SIXES;
		categoryMapping[8] = null;
		categoryMapping[9] = null;
		categoryMapping[10] = null;
		categoryMapping[11] = null;
		categoryMapping[12] = YahtzeeCategory.THREE_OF_A_KIND;
		categoryMapping[13] = YahtzeeCategory.FOUR_OF_A_KIND;
		categoryMapping[14] = YahtzeeCategory.FULL_HOUSE;
		categoryMapping[15] = YahtzeeCategory.SMALL_STRAIGHT;
		categoryMapping[16] = YahtzeeCategory.LARGE_STRAIGHT;
		categoryMapping[17] = YahtzeeCategory.YAHTZEE;
		categoryMapping[18] = YahtzeeCategory.CHANCE;
		categoryMapping[19] = null;
		categoryMapping[20] = null;
		categoryMapping[21] = null;
		categoryMapping[22] = null;

		player1Pane = new GridPane();
		player2Pane = new GridPane();
		HBox hBox = new HBox(60);

		for (int row = 0; row < categories.length; row++) {
			final int currRow = row;
			for (int col = 0; col < 2; col++) {
				Label label1 = new Label();
				label1.setPrefSize(100, 25);
				label1.setPadding(new Insets(0, 0, 0, 2));

				// Add border to label1.
				label1.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
						new BorderWidths(0.5))));

				player1Pane.add(label1, col, row);

				Label label2 = new Label();
				label2.setPrefSize(100, 25);
				label2.setPadding(new Insets(0, 0, 0, 2));

				// Add border to label2.
				label2.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
						new BorderWidths(0.5))));

				player2Pane.add(label2, col, row);

				if (col == 0) {
					label1.setText(categories[row]);
					label2.setText(categories[row]);

					if (categoryMapping[row] == null && row != 19) {
						label1.setFont(Font.font("System", FontWeight.BOLD, 12));
						label2.setFont(Font.font("System", FontWeight.BOLD, 12));
					}
				} else if (col == 1 && categoryMapping[row] != null) {
					label1.setBackground(
							new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));

					label2.setBackground(
							new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));

					label1.setOnMouseClicked(e -> {
						handleScoreClick(categoryMapping[currRow], label1);
					});

					label2.setOnMouseClicked(e -> {
						handleScoreClick(categoryMapping[currRow], label2);
					});
				} else if (!categories[row].equals("YAHTZEE BONUS")) {

					label1.setBackground(
							new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

					label2.setBackground(
							new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
				} else if (categories[row].equals("YAHTZEE BONUS")) {
					label1.setOnMouseClicked(e -> {
						handleYahtzeeBonusClick(label1);
					});

					label2.setOnMouseClicked(e -> {
						handleYahtzeeBonusClick(label2);
					});

					label1.setBackground(
							new Background(new BackgroundFill(Color.GOLD, CornerRadii.EMPTY, Insets.EMPTY)));

					label2.setBackground(
							new Background(new BackgroundFill(Color.GOLD, CornerRadii.EMPTY, Insets.EMPTY)));
				}

				if (col == 1) {
					label1.setAlignment(Pos.CENTER);
					label2.setAlignment(Pos.CENTER);
				}

				categoryLabels1[row] = label1;
				categoryLabels2[row] = label2;

			}

		}

		hBox.setPadding(new Insets(0, 20, 10, 20));
		hBox.getChildren().addAll(player1Pane, player2Pane);
		borderPane.setBottom(hBox);
		hBox.setAlignment(Pos.CENTER);

		player1Pane.setDisable(true);
		player2Pane.setDisable(true);
	}

	/**
	 * Handles the click event for scoring a category.
	 * 
	 * @param category - The YahtzeeCategory to score.
	 * @param scoreLabel - The Label representing the score for the category.
	 */
	private void handleScoreClick(YahtzeeCategory category, Label scoreLabel) {
		if (!controller.isCategoryAvailable(category)) {
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setHeaderText("Category already scored!");
			a.setContentText("Choose a category that isn't already scored!");
			a.showAndWait();
			return;
		}

		int[] dice = controller.getCurrentPlayer().getDieValues();

		controller.recordScore(category, dice);
		scoreLabel.setOnMouseClicked(null);

		scoreLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
	}

	/**
	 * Handles the click event for scoring a Yahtzee bonus.
	 * 
	 * @param scoreLabel - The Label representing the score for the Yahtzee bonus.
	 */
	private void handleYahtzeeBonusClick(Label scoreLabel) {
		int[] dice = controller.getCurrentPlayer().getDieValues();

		if (!controller.recordBonusYahtzee(dice)) {
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setHeaderText("Need to score a YAHTZEE first!");
			a.setContentText("Click this box if you score more than one YAHTZEE!");
			a.showAndWait();
			return;
		}
	}

	/**
	 * Updates the GUI based on the score update from the model.
	 */
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		ScoreUpdate scoreUpdate = (ScoreUpdate) arg;
		int index = 0;
		for (int i = 0; i < categoryMapping.length; i++) {
			if (categoryMapping[i] == scoreUpdate.category) {
				index = i;
				break;
			}
		}

		if (scoreUpdate.player == controller.getPlayer1()) {
			categoryLabels1[index].setText(String.valueOf(scoreUpdate.score));
		} else {
			categoryLabels2[index].setText(String.valueOf(scoreUpdate.score));
		}

		if (scoreUpdate.upperFull) {
			if (scoreUpdate.player.getScorecard().getUpperSectionBonus() == 35) {
				if (scoreUpdate.player == controller.getPlayer1()) {
					categoryLabels1[8]
							.setText(String.valueOf(scoreUpdate.player.getScorecard().getUpperSection() - 35));
					categoryLabels1[9].setText("35");
					categoryLabels1[10].setText(String.valueOf(scoreUpdate.player.getScorecard().getUpperSection()));
				} else {
					categoryLabels2[8]
							.setText(String.valueOf(scoreUpdate.player.getScorecard().getUpperSection() - 35));
					categoryLabels2[9].setText("35");
					categoryLabels2[10].setText(String.valueOf(scoreUpdate.player.getScorecard().getUpperSection()));

				}
			} else {
				if (scoreUpdate.player == controller.getPlayer1()) {
					categoryLabels1[8].setText(String.valueOf(scoreUpdate.player.getScorecard().getUpperSection()));
					categoryLabels1[9].setText("0");
					categoryLabels1[10].setText(String.valueOf(scoreUpdate.player.getScorecard().getUpperSection()));
				} else {
					categoryLabels2[8].setText(String.valueOf(scoreUpdate.player.getScorecard().getUpperSection()));
					categoryLabels2[9].setText("0");
					categoryLabels2[10].setText(String.valueOf(scoreUpdate.player.getScorecard().getUpperSection()));
				}
			}
		}

		if (scoreUpdate.player.getScorecard().isComplete()) {
			if (scoreUpdate.player == controller.getPlayer1()) {
				categoryLabels1[20].setText(String.valueOf(scoreUpdate.player.getScorecard().getUpperSection()));
				categoryLabels1[21].setText(String.valueOf(scoreUpdate.player.getScorecard().getLowerSection()));
				categoryLabels1[22].setText(String.valueOf(scoreUpdate.player.getScorecard().getGrandTotal()));
			} else {
				categoryLabels2[20].setText(String.valueOf(scoreUpdate.player.getScorecard().getUpperSection()));
				categoryLabels2[21].setText(String.valueOf(scoreUpdate.player.getScorecard().getLowerSection()));
				categoryLabels2[22].setText(String.valueOf(scoreUpdate.player.getScorecard().getGrandTotal()));
			}
		}

		button.setDisable(false);
		for (int i = 0; i < 5; i++) {
			diceViews[i].setDisable(false);
			diceViews[i].setVisible(false);
			holdIndicators[i].setVisible(false);
		}

		if (controller.isGameOver()) {
			player1Pane.setDisable(true);
			player2Pane.setDisable(true);
			button.setDisable(true);
			for (int i = 0; i < 5; i++) {
				diceViews[i].setDisable(true);
				holdIndicators[i].setVisible(false);
			}
			String winner = controller.getWinner().getName();
			Alert a = new Alert(Alert.AlertType.INFORMATION);
			a.setHeaderText("WINNER: " + winner + "!");
			a.setContentText(winner + " wins!");
			a.showAndWait();

			if (saveFile.exists()) {
				saveFile.delete();
			}

		}

		player1Pane.setDisable(true);
		player2Pane.setDisable(true);

		if (!controller.isGameOver()) {
			infoLabel.setText(controller.getCurrentPlayer().getName() + "'s Turn! Click Roll!");
		}
		
	}

	/**
	 * Sets up the menu bar for the Yahtzee game.
	 * 
	 * @return The configured MenuBar.
	 */
	private MenuBar setupMenu() {
		MenuBar menuBar = new MenuBar();
		Menu file = new Menu("File");
		MenuItem newGame = new MenuItem("New Game");
		MenuItem rules = new MenuItem("How To Play");
		MenuItem exitNoSave = new MenuItem("Exit Without Saving");
		MenuItem exit = new MenuItem("Save And Exit");
		file.getItems().addAll(newGame, rules, new SeparatorMenuItem(), exitNoSave, new SeparatorMenuItem(), exit);
		menuBar.getMenus().add(file);

		newGame.setOnAction(event -> {
			if (saveFile.exists()) {
				saveFile.delete();
			}

			PlayerNamingScreen.namingScreen(mainMenu, "Yahtzee");
		});

		rules.setOnAction(event -> {
			showHowToPlay();
		});

		exit.setOnAction(event -> {
			if (!controller.isGameOver()) {
				controller.saveGame();
			}
			mainMenu.exitToMenu();
		});
		
		exitNoSave.setOnAction(event -> {
			mainMenu.exitToMenu();
		});

		return menuBar;

	}

	/**
	 * Displays the "How to Play" rules in a new window.
	 */
	private void showHowToPlay() {
		Stage howToStage = new Stage();
		howToStage.setTitle("How to play YAHTZEE!");

		VBox content = new VBox(10);
		content.setPadding(new Insets(20));

		Label title = new Label("YAHTZEE Rules");
		title.setFont(Font.font("System", FontWeight.BOLD, 20));

		Label rules = new Label(
				"1. Roll the dice up to 3 times per turn.\n" + "2. Click the dice to hold them between rolls.\n"
						+ "3. Click on the desired category to score points based on the die rolled.\n"
						+ "4. Try to get the highest score!\n\n" +

						"Ones | Sixes: Adds up all the die in the category."
						+ "Ex: If you roll 3 Fours and take Fours, you will score 12 points.\n"
						+ "BONUS | If you score a 63 or over on the Upper Section, you will gain an extra 35 points.\n"
						+ "3 of a kind | If you roll 3 or more matching die, you score the sum of ALL die.\n"
						+ "4 of a kind | If you roll 4 or more matching die, you score the sum of ALL die.\n"
						+ "Full House | Rolling 3 matching die, and 2 more matching die of a different number.\n"
						+ "Ex: Rolling 3 Fives and 2 Fours. 25 points.\n"
						+ "Sm. Straight | Sequence of 4 numbers. Ex: Rolling 1, 2, 3, 4. 30 points.\n"
						+ "Lg. Straight | Sequence of 5 numbers. Ex: Rolling 1, 2, 3, 4, 5. 40 points.\n"
						+ "YAHTZEE | 5 of a kind. 50 points.\n"
						+ "Chance | Score the total of all 5 dice, a 'chance'.\n"
						+ "YAHTZEE BONUS | After an initial YAHTZEE is scored, if you roll another YAHTZEE"
						+ " you may score 100 bonus points.\n\n" +

						"You may take a 0 on any category, for example if you can't take any score.");

		content.getChildren().addAll(title, rules);

		scene = new Scene(content, 650, 500);
		howToStage.setScene(scene);
		howToStage.show();
	}

	/**
	 * Loads the scorecard from a saved game into the UI.
	 */
	private void loadScorecard() {
		// Load player 1's scores.
		HashMap<YahtzeeCategory, Integer> player1Scores = controller.getPlayer1().getScorecard().getScores();
		for (int i = 0; i < categoryMapping.length; i++) {
			if (categoryMapping[i] != null && player1Scores.containsKey(categoryMapping[i])) {
				categoryLabels1[i].setText(String.valueOf(player1Scores.get(categoryMapping[i])));
				categoryLabels1[i].setBackground(
						new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
			}
		}

		// Load player 2's scores.
		HashMap<YahtzeeCategory, Integer> player2Scores = controller.getPlayer2().getScorecard().getScores();
		for (int i = 0; i < categoryMapping.length; i++) {
			if (categoryMapping[i] != null && player2Scores.containsKey(categoryMapping[i])) {
				categoryLabels2[i].setText(String.valueOf(player2Scores.get(categoryMapping[i])));
				categoryLabels2[i].setBackground(
						new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
			}
		}

		// Update player names.
		categoryLabels1[0].setText(controller.getPlayer1().getName());
		categoryLabels2[0].setText(controller.getPlayer2().getName());

		// Update totals/bonuses if applicable.
		updateTotals(controller.getPlayer1(), categoryLabels1);
		updateTotals(controller.getPlayer2(), categoryLabels2);
	}

	/**
	 * Updates the total scores and bonuses for a player in the UI.
	 * 
	 * @param player - The YahtzeePlayer whose totals to update.
	 * @param labels - The array of Labels representing the scorecard UI.
	 */
	private void updateTotals(YahtzeePlayer player, Label[] labels) {
		YahtzeeScorecard scorecard = player.getScorecard();

		if (scorecard.isUpperSectionFull()) {
			labels[8].setText(String.valueOf(scorecard.getUpperSection() - scorecard.getUpperSectionBonus()));
			labels[9].setText(String.valueOf(scorecard.getUpperSectionBonus()));
			labels[10].setText(String.valueOf(scorecard.getUpperSection()));
		}

		if (scorecard.isComplete()) {
			labels[20].setText(String.valueOf(scorecard.getUpperSection()));
			labels[21].setText(String.valueOf(scorecard.getLowerSection()));
			labels[22].setText(String.valueOf(scorecard.getGrandTotal()));
		}
	}

	/**
	 * Returns the current scene of the Yahtzee game.
	 * 
	 * @return The current Scene.
	 */
	public Scene getScene() {
		return scene;
	}

}