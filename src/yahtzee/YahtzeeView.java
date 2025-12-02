package yahtzee;

import java.util.Observable;
import java.util.Observer;

import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
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

/*
 * 
 * REMOVE EXTENDS APPLICATION, ONLY FOR DEBUGGING
 * 
 */
@SuppressWarnings("deprecation")
public class YahtzeeView extends Application implements Observer {
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
	
//	public YahtzeeView(Stage stage) {
//		this.stage = stage;
//		this.model = new YahtzeeModel("Player 1", "Player 2");
//	
//		for (int i = 0; i < 6; i++) {
//			dieImages[i] = new Image("die" + (i+1) + ".png");
//		}
//
//		setupUI();
//	}
	
	private void startNewGame() {
		Stage playerScreen = new Stage();
		playerScreen.setTitle("Welcome to YAHTZEE!");
		BorderPane pane = new BorderPane();
		HBox hBox = new HBox(50);
		VBox player1 = new VBox(10);
		VBox player2 = new VBox(10);
		
		Label player1Label = new Label("Player 1");
		player1Label.setFont(Font.font("System", FontWeight.BOLD, 20));
		Label player2Label = new Label("Player 2");
		player2Label.setFont(Font.font("System", FontWeight.BOLD, 20));
		
		TextField player1Name = new TextField("Enter player 1's name!");
		TextField player2Name = new TextField("Enter player 2's name!");
		
		player1.getChildren().addAll(player1Label, player1Name);
		player2.getChildren().addAll(player2Label, player2Name);
		hBox.getChildren().addAll(player1, player2);
		hBox.setPadding(new Insets(50));
		hBox.setAlignment(Pos.CENTER);
		
		Label topLabel = new Label("YAHTZEE!");
		topLabel.setFont(Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, 30));
		pane.setTop(topLabel);
		pane.setCenter(hBox);
		BorderPane.setAlignment(topLabel, Pos.CENTER);
		
		Button start = new Button("Start!");
		start.setOnAction(event -> {
			playerScreen.close();
			
			String name1 = player1Name.getText();
			String name2 = player2Name.getText();
			if (name1.equals("Enter player 1's name!") || name1.isBlank()) {
				name1 = "Player 1";
			} if (name2.equals("Enter player 2's name!") || name2.isBlank()) {
				name2 = "Player 2";
			}
			setupUI(name1, name2);
		});
		
		pane.setBottom(start);
		BorderPane.setAlignment(start, Pos.CENTER);
		start.setPrefSize(100, 50);
		start.setFont(Font.font("System", 15));
		pane.setPadding(new Insets(20));
		
		Scene playerScene = new Scene(pane, 500, 300);
		playerScreen.setScene(playerScene);
		playerScreen.show();
	}
	
	private void setupUI(String player1Name, String player2Name) {
		for (int i = 0; i < 6; i++) {
			dieImages[i] = new Image(getClass().getResourceAsStream("die" + (i+1) + ".png"));
		}
		model = new YahtzeeModel(player1Name, player2Name);
		controller = new YahtzeeController(model);
		model.addObserver(this);
		borderPane = new BorderPane();
		setupDiceandMenu();
		setupButtonAndLabel();
		setupScorecards();
		
		categoryLabels1[0].setText(player1Name);
		categoryLabels2[0].setText(player2Name);
		
		Scene scene = new Scene(borderPane, 500, 800);
		stage.setScene(scene);
		stage.setTitle("Yahtzee");
		stage.show();
	}
	
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
			diceArea.setBackground(
				new Background(
					new BackgroundFill(
						Color.GREEN,
						CornerRadii.EMPTY,
						Insets.EMPTY
					)
				)
			);
			
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
	
	private void toggleHold(int index) {
		YahtzeeDie die = controller.getCurrentPlayer().getDice()[index];
		die.toggleHold();
		
		holdIndicators[index].setVisible(die.isHeld());
		
	}
	
	private void animateDiceRoll(int dieIndex, int finalValue) {
		ImageView dieView = diceViews[dieIndex];
		
		RotateTransition rotate = new RotateTransition(Duration.seconds(1), dieView);
		rotate.setByAngle(360 * 3);
		
		Timeline timeline = new Timeline();
		for (int i = 0; i < 10; i++) {
			int face = (int) (Math.random() * 6);
			KeyFrame keyFrame = new KeyFrame(
				Duration.millis(i * 100),
				e -> dieView.setImage(dieImages[face])
			);
			timeline.getKeyFrames().add(keyFrame);
		}
		
		rotate.setOnFinished(e -> dieView.setImage(dieImages[finalValue - 1]));
		
		rotate.play();
		timeline.play();
	}
	
	private boolean checkAllHeld() {
		for (int i = 0; i < 5; i++) {
			if (!holdIndicators[i].isVisible()) {
				return false;
			}
		}
		
		return true;
	}
	
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
			int[] values = model.rollDice();
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
				
			}));
			
			enable.play();
	
		});
		
		vBox.setAlignment(Pos.CENTER);
		vBox.getChildren().addAll(infoLabel, button);
		borderPane.setCenter(vBox);
	}
	
	private void setupScorecards() {
		String[] categories = {"PLAYER", "UPPER SECTION", "Ones", "Twos", "Threes",
								"Fours", "Fives", "Sixes", "TOTAL SCORE", "BONUS",
								"TOTAL", "LOWER SECTION", "3 of a kind", "4 of a kind",
								"Full House", "Sm. Straight", "Lg. Straight", "YAHTZEE",
								"Chance", "YAHTZEE BONUS", "TOTAL UPPER", "TOTAL LOWER",
								"GRAND TOTAL"};
		
		categoryMapping[0] = null;                         // 0: PLAYER
		categoryMapping[1] = null;                           // 1: UPPER SECTION
		categoryMapping[2] = YahtzeeCategory.ONES;           // 2: Ones
		categoryMapping[3] = YahtzeeCategory.TWOS;           // 3: Twos
		categoryMapping[4] = YahtzeeCategory.THREES;         // 4: Threes
		categoryMapping[5] = YahtzeeCategory.FOURS;          // 5: Fours
		categoryMapping[6] = YahtzeeCategory.FIVES;          // 6: Fives
		categoryMapping[7] = YahtzeeCategory.SIXES;          // 7: Sixes
		categoryMapping[8] = null;                           // 8: TOTAL SCORE
		categoryMapping[9] = null;                           // 9: BONUS
		categoryMapping[10] = null;                           // 10: TOTAL
		categoryMapping[11] = null;                           // 11: LOWER SECTION
		categoryMapping[12] = YahtzeeCategory.THREE_OF_A_KIND;// 12: 3 of a kind
		categoryMapping[13] = YahtzeeCategory.FOUR_OF_A_KIND; // 13: 4 of a kind
		categoryMapping[14] = YahtzeeCategory.FULL_HOUSE;     // 14: Full House
		categoryMapping[15] = YahtzeeCategory.SMALL_STRAIGHT; // 15: Sm. Straight
		categoryMapping[16] = YahtzeeCategory.LARGE_STRAIGHT; // 16: Lg. Straight
		categoryMapping[17] = YahtzeeCategory.YAHTZEE;        // 17: YAHTZEE
		categoryMapping[18] = YahtzeeCategory.CHANCE;         // 18: Chance
		categoryMapping[19] = null;                           // 19: YAHTZEE BONUS
		categoryMapping[20] = null;                           // 20: TOTAL UPPER
		categoryMapping[21] = null;                           // 21: TOTAL LOWER
		categoryMapping[22] = null;                            // 22: GRAND TOTAL
		
		player1Pane = new GridPane();
		player2Pane = new GridPane();
		HBox hBox = new HBox(60);
		
		for (int row = 0; row < categories.length; row++) {
			final int currRow = row;
			for (int col = 0; col < 2; col++) {
				Label label1 = new Label();
				label1.setPrefSize(100, 25);
				label1.setPadding(new Insets(0, 0, 0, 2));
				
				// Add border to label1
				label1.setBorder(new Border(new BorderStroke(
					Color.BLACK, 
					BorderStrokeStyle.SOLID, 
					CornerRadii.EMPTY, 
					new BorderWidths(0.5)
				)));
				
				player1Pane.add(label1, col, row);

				Label label2 = new Label();
				label2.setPrefSize(100, 25);
				label2.setPadding(new Insets(0, 0, 0, 2));
				
				// Add border to label2
				label2.setBorder(new Border(new BorderStroke(
					Color.BLACK, 
					BorderStrokeStyle.SOLID, 
					CornerRadii.EMPTY, 
					new BorderWidths(0.5)
				)));
				
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
							new Background(
								new BackgroundFill(
									Color.LIGHTGREEN,
									CornerRadii.EMPTY,
									Insets.EMPTY
								)
							)
						);
						
					label2.setBackground(
						new Background(
							new BackgroundFill(
								Color.LIGHTGREEN,
								CornerRadii.EMPTY,
								Insets.EMPTY
							)
						)
					);
						
					label1.setOnMouseClicked(e -> {
						handleScoreClick(categoryMapping[currRow], label1);
					});
					
					label2.setOnMouseClicked(e -> {
						handleScoreClick(categoryMapping[currRow], label2);
					});
				} else if (!categories[row].equals("YAHTZEE BONUS")){
					
					label1.setBackground(
						new Background(
							new BackgroundFill(
								Color.LIGHTGRAY,
								CornerRadii.EMPTY,
								Insets.EMPTY
							)
						)
					);
					
					label2.setBackground(
						new Background(
							new BackgroundFill(
								Color.LIGHTGRAY,
								CornerRadii.EMPTY,
								Insets.EMPTY
							)
						)
					);
				} else if (categories[row].equals("YAHTZEE BONUS")) {
					label1.setOnMouseClicked(e -> {
						handleYahtzeeBonusClick(label1);
					});
					
					label2.setOnMouseClicked(e -> {
						handleYahtzeeBonusClick(label2);
					});
					
					label1.setBackground(
						new Background(
							new BackgroundFill(
								Color.GOLD,
								CornerRadii.EMPTY,
								Insets.EMPTY
							)
						)
					);
						
					label2.setBackground(
						new Background(
							new BackgroundFill(
								Color.GOLD,
								CornerRadii.EMPTY,
								Insets.EMPTY
							)
						)
					);
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
		
		player1Pane.setDisable(true);
		player2Pane.setDisable(true);
	}
	
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
		
		scoreLabel.setBackground(
			new Background(
				new BackgroundFill(
					Color.LIGHTBLUE,
					CornerRadii.EMPTY,
					Insets.EMPTY
				)
			)
		);
	}
	
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
					categoryLabels1[8].setText(String.valueOf(scoreUpdate.player.getScorecard().getUpperSection() - 35));
					categoryLabels1[9].setText("35");
					categoryLabels1[10].setText(String.valueOf(scoreUpdate.player.getScorecard().getUpperSection()));
				} else {
					categoryLabels2[8].setText(String.valueOf(scoreUpdate.player.getScorecard().getUpperSection() - 35));
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
					categoryLabels1[10].setText(String.valueOf(scoreUpdate.player.getScorecard().getUpperSection()));
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
			a.setContentText(winner + "wins!");
			a.showAndWait();
			
		}
		
		player1Pane.setDisable(true);
		player2Pane.setDisable(true);
		
		infoLabel.setText(controller.getCurrentPlayer().getName() + "'s Turn! Click Roll!");
		
	}
	
	private MenuBar setupMenu() {
		MenuBar menuBar = new MenuBar();
		Menu file = new Menu("File");
		MenuItem newGame = new MenuItem("New Game");
		MenuItem rules = new MenuItem("How To Play");
		file.getItems().addAll(newGame, rules);
		menuBar.getMenus().add(file);
		
		newGame.setOnAction(event -> {
			startNewGame();
		});
		
		rules.setOnAction(event -> {
			showHowToPlay();
		});
		
		return menuBar;
		
	}
	
	private void showHowToPlay() {
		Stage howToStage = new Stage();
		howToStage.setTitle("How to play YAHTZEE!");
		
		VBox content = new VBox(10);
		content.setPadding(new Insets(20));
		
		Label title = new Label("YAHTZEE Rules");
		title.setFont(Font.font("System", FontWeight.BOLD, 20));
		
		Label rules = new Label(
			"1. Roll the dice up to 3 times per turn.\n" +
			"2. Click the dice to hold them between rolls.\n" +
			"3. Click on the desired category to score points based on the die rolled.\n" +
			"4. Try to get the highest score!\n\n" +
			
			"Ones | Sixes: Adds up all the die in the category." +
			"Ex: If you roll 3 Fours and take Fours, you will score 12 points.\n" +
			"BONUS | If you score a 63 or over on the Upper Section, you will gain an extra 35 points.\n" +
			"3 of a kind | If you roll 3 or more matching die, you score the sum of ALL die.\n" +
			"4 of a kind | If you roll 4 or more matching die, you score the sum of ALL die.\n" +
			"Full House | Rolling 3 matching die, and 2 more matching die of a different number.\n" +
			"Ex: Rolling 3 Fives and 2 Fours. 25 points.\n" +
			"Sm. Straight | Sequence of 4 numbers. Ex: Rolling 1, 2, 3, 4. 30 points.\n" +
			"Lg. Straight | Sequence of 5 numbers. Ex: Rolling 1, 2, 3, 4, 5. 40 points.\n" +
			"YAHTZEE | 5 of a kind. 50 points.\n" +
			"Chance | Score the total of all 5 dice, a 'chance'.\n" +
			"YAHTZEE BONUS | After an initial YAHTZEE is scored, if you roll another YAHTZEE" +
			" you may score 100 bonus points.\n\n" +
			
			"You may take a 0 on any category, for example if you can't take any score."
		);
		
		content.getChildren().addAll(title, rules);
		
		Scene scene = new Scene(content, 650, 500);
	    howToStage.setScene(scene);
	    howToStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		this.stage = arg0;
		startNewGame();
		
	}
}
