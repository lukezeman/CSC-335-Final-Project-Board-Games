package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import yahtzee.*;
import java.io.File;

class YahtzeeTests {
	
	private YahtzeeModel model;
	private YahtzeeController controller;
	
	@BeforeEach
	void setUp() {
		model = new YahtzeeModel("Player 1", "Player 2");
		controller = new YahtzeeController(model);
		
		// Clean up any existing save file
		File saveFile = new File("save_yahtzee.dat");
		if (saveFile.exists()) {
			saveFile.delete();
		}
	}
	
	// ========== CONTROLLER/MODEL INTEGRATION TESTS ==========
	
	@Test
	void testRollDice() {
		int[] dice = controller.rollDice();
		assertNotNull(dice);
		assertEquals(5, dice.length);
		assertEquals(2, controller.getRollsRemaining());
		
		for (int val : dice) {
			assertTrue(val >= 1 && val <= 6);
		}
	}
	
	@Test
	void testRollDiceThreeTimes() {
		controller.rollDice();
		assertEquals(2, controller.getRollsRemaining());
		
		controller.rollDice();
		assertEquals(1, controller.getRollsRemaining());
		
		controller.rollDice();
		assertEquals(0, controller.getRollsRemaining());
		
		int[] noMoreRolls = controller.rollDice();
		assertNull(noMoreRolls);
	}
	
	@Test
	void testGetCurrentPlayer() {
		assertEquals("Player 1", controller.getCurrentPlayer().getName());
	}
	
	@Test
	void testGetPlayers() {
		assertEquals("Player 1", controller.getPlayer1().getName());
		assertEquals("Player 2", controller.getPlayer2().getName());
	}
	
	// ========== CATEGORY SCORING TESTS ==========
	
	@Test
	void testOnesScoring() {
		int[] dice = {1, 1, 1, 2, 3};
		assertEquals(3, YahtzeeCategory.ONES.calculateScore(dice));
		
		int[] noDice = {2, 3, 4, 5, 6};
		assertEquals(0, YahtzeeCategory.ONES.calculateScore(noDice));
	}
	
	@Test
	void testTwosScoring() {
		int[] dice = {2, 2, 3, 4, 5};
		assertEquals(4, YahtzeeCategory.TWOS.calculateScore(dice));
	}
	
	@Test
	void testThreesScoring() {
		int[] dice = {3, 3, 3, 3, 1};
		assertEquals(12, YahtzeeCategory.THREES.calculateScore(dice));
	}
	
	@Test
	void testFoursScoring() {
		int[] dice = {4, 4, 1, 2, 3};
		assertEquals(8, YahtzeeCategory.FOURS.calculateScore(dice));
	}
	
	@Test
	void testFivesScoring() {
		int[] dice = {5, 5, 5, 1, 2};
		assertEquals(15, YahtzeeCategory.FIVES.calculateScore(dice));
	}
	
	@Test
	void testSixesScoring() {
		int[] dice = {6, 6, 6, 6, 6};
		assertEquals(30, YahtzeeCategory.SIXES.calculateScore(dice));
	}
	
	@Test
	void testThreeOfAKindValid() {
		int[] dice = {3, 3, 3, 4, 5};
		assertEquals(18, YahtzeeCategory.THREE_OF_A_KIND.calculateScore(dice));
	}
	
	@Test
	void testThreeOfAKindInvalid() {
		int[] dice = {1, 2, 3, 4, 5};
		assertEquals(0, YahtzeeCategory.THREE_OF_A_KIND.calculateScore(dice));
	}
	
	@Test
	void testFourOfAKindValid() {
		int[] dice = {2, 2, 2, 2, 6};
		assertEquals(14, YahtzeeCategory.FOUR_OF_A_KIND.calculateScore(dice));
	}
	
	@Test
	void testFourOfAKindInvalid() {
		int[] dice = {1, 1, 1, 2, 3};
		assertEquals(0, YahtzeeCategory.FOUR_OF_A_KIND.calculateScore(dice));
	}
	
	@Test
	void testFullHouseValid() {
		int[] dice = {3, 3, 3, 5, 5};
		assertEquals(25, YahtzeeCategory.FULL_HOUSE.calculateScore(dice));
	}
	
	@Test
	void testFullHouseInvalid() {
		int[] dice = {1, 2, 3, 4, 5};
		assertEquals(0, YahtzeeCategory.FULL_HOUSE.calculateScore(dice));
	}
	
	@Test
	void testFullHouseYahtzeeNotValid() {
		int[] dice = {4, 4, 4, 4, 4};
		assertEquals(0, YahtzeeCategory.FULL_HOUSE.calculateScore(dice));
	}
	
	@Test
	void testSmallStraightAtStart() {
		int[] dice = {1, 2, 3, 4, 6};
		assertEquals(30, YahtzeeCategory.SMALL_STRAIGHT.calculateScore(dice));
	}
	
	@Test
	void testSmallStraightAtEnd() {
		int[] dice = {1, 3, 4, 5, 6};
		assertEquals(30, YahtzeeCategory.SMALL_STRAIGHT.calculateScore(dice));
	}
	
	@Test
	void testSmallStraightWithDuplicates() {
		int[] dice = {2, 2, 3, 4, 5};
		assertEquals(30, YahtzeeCategory.SMALL_STRAIGHT.calculateScore(dice));
	}
	
	@Test
	void testSmallStraightInvalid() {
		int[] dice = {1, 1, 3, 4, 6};
		assertEquals(0, YahtzeeCategory.SMALL_STRAIGHT.calculateScore(dice));
	}
	
	@Test
	void testLargeStraight1to5() {
		int[] dice = {1, 2, 3, 4, 5};
		assertEquals(40, YahtzeeCategory.LARGE_STRAIGHT.calculateScore(dice));
	}
	
	@Test
	void testLargeStraight2to6() {
		int[] dice = {2, 3, 4, 5, 6};
		assertEquals(40, YahtzeeCategory.LARGE_STRAIGHT.calculateScore(dice));
	}
	
	@Test
	void testLargeStraightInvalid() {
		int[] dice = {1, 2, 3, 4, 6};
		assertEquals(0, YahtzeeCategory.LARGE_STRAIGHT.calculateScore(dice));
	}
	
	@Test
	void testYahtzeeValid() {
		int[] dice = {5, 5, 5, 5, 5};
		assertEquals(50, YahtzeeCategory.YAHTZEE.calculateScore(dice));
	}
	
	@Test
	void testYahtzeeInvalid() {
		int[] dice = {5, 5, 5, 5, 3};
		assertEquals(0, YahtzeeCategory.YAHTZEE.calculateScore(dice));
	}
	
	@Test
	void testChance() {
		int[] dice = {1, 2, 3, 4, 5};
		assertEquals(15, YahtzeeCategory.CHANCE.calculateScore(dice));
		
		int[] dice2 = {6, 6, 6, 6, 6};
		assertEquals(30, YahtzeeCategory.CHANCE.calculateScore(dice2));
	}
	
	// ========== RECORDING SCORES TESTS ==========
	
	@Test
	void testRecordScore() {
	    int[] dice = {1, 1, 1, 2, 3};
	    assertTrue(controller.isCategoryAvailable(YahtzeeCategory.ONES));
	    
	    controller.recordScore(YahtzeeCategory.ONES, dice);
	    
	    // After recording, we switched to Player 2, so check Player 1's scorecard directly
	    assertFalse(controller.getPlayer1().getScorecard().isCategoryAvailable(YahtzeeCategory.ONES));
	}
	
	@Test
	void testRecordScoreSwitchesPlayer() {
		assertEquals("Player 1", controller.getCurrentPlayer().getName());
		
		int[] dice = {1, 2, 3, 4, 5};
		controller.recordScore(YahtzeeCategory.ONES, dice);
		
		assertEquals("Player 2", controller.getCurrentPlayer().getName());
		assertEquals(3, controller.getRollsRemaining());
	}
	
	@Test
	void testUpperSectionBonusAwarded() {
		// Score 63+ in upper section
		controller.recordScore(YahtzeeCategory.ONES, new int[]{1, 1, 1, 1, 1});
		controller.recordScore(YahtzeeCategory.CHANCE, new int[]{1, 1, 1, 1, 1});
		
		controller.recordScore(YahtzeeCategory.TWOS, new int[]{2, 2, 2, 2, 2});
		controller.recordScore(YahtzeeCategory.CHANCE, new int[]{1, 1, 1, 1, 1});
		
		controller.recordScore(YahtzeeCategory.THREES, new int[]{3, 3, 3, 3, 3});
		controller.recordScore(YahtzeeCategory.CHANCE, new int[]{1, 1, 1, 1, 1});
		
		controller.recordScore(YahtzeeCategory.FOURS, new int[]{4, 4, 4, 4, 4});
		controller.recordScore(YahtzeeCategory.CHANCE, new int[]{1, 1, 1, 1, 1});
		
		controller.recordScore(YahtzeeCategory.FIVES, new int[]{5, 5, 5, 5, 5});
		controller.recordScore(YahtzeeCategory.CHANCE, new int[]{1, 1, 1, 1, 1});
		
		controller.recordScore(YahtzeeCategory.SIXES, new int[]{6, 6, 6, 1, 2});
		
		YahtzeeScorecard p1Scorecard = controller.getPlayer1().getScorecard();
		assertEquals(35, p1Scorecard.getUpperSectionBonus());
	}
	
	@Test
	void testUpperSectionBonusNotAwarded() {
		controller.recordScore(YahtzeeCategory.ONES, new int[]{1, 2, 3, 4, 5});
		controller.recordScore(YahtzeeCategory.CHANCE, new int[]{1, 1, 1, 1, 1});
		
		controller.recordScore(YahtzeeCategory.TWOS, new int[]{2, 3, 4, 5, 6});
		controller.recordScore(YahtzeeCategory.CHANCE, new int[]{1, 1, 1, 1, 1});
		
		controller.recordScore(YahtzeeCategory.THREES, new int[]{3, 4, 5, 6, 1});
		controller.recordScore(YahtzeeCategory.CHANCE, new int[]{1, 1, 1, 1, 1});
		
		controller.recordScore(YahtzeeCategory.FOURS, new int[]{4, 5, 6, 1, 2});
		controller.recordScore(YahtzeeCategory.CHANCE, new int[]{1, 1, 1, 1, 1});
		
		controller.recordScore(YahtzeeCategory.FIVES, new int[]{5, 6, 1, 2, 3});
		controller.recordScore(YahtzeeCategory.CHANCE, new int[]{1, 1, 1, 1, 1});
		
		controller.recordScore(YahtzeeCategory.SIXES, new int[]{6, 1, 2, 3, 4});
		
		YahtzeeScorecard p1Scorecard = controller.getPlayer1().getScorecard();
		assertEquals(0, p1Scorecard.getUpperSectionBonus());
	}
	
	@Test
	void testBonusYahtzeeValid() {
		// First score a yahtzee
		int[] yahtzee = {5, 5, 5, 5, 5};
		controller.recordScore(YahtzeeCategory.YAHTZEE, yahtzee);
		controller.recordScore(YahtzeeCategory.ONES, new int[]{1, 2, 3, 4, 5});
		
		// Roll another yahtzee
		assertTrue(controller.recordBonusYahtzee(yahtzee));
	}
	
	@Test
	void testBonusYahtzeeWithoutFirstYahtzee() {
		int[] yahtzee = {5, 5, 5, 5, 5};
		assertFalse(controller.recordBonusYahtzee(yahtzee));
	}
	
	@Test
	void testBonusYahtzeeWithZeroedYahtzee() {
		int[] notYahtzee = {1, 2, 3, 4, 5};
		controller.recordScore(YahtzeeCategory.YAHTZEE, notYahtzee);
		controller.recordScore(YahtzeeCategory.ONES, new int[]{1, 2, 3, 4, 5});
		
		int[] yahtzee = {5, 5, 5, 5, 5};
		assertFalse(controller.recordBonusYahtzee(yahtzee));
	}
	
	@Test
	void testBonusYahtzeeNotActuallyYahtzee() {
		int[] yahtzee = {5, 5, 5, 5, 5};
		controller.recordScore(YahtzeeCategory.YAHTZEE, yahtzee);
		controller.recordScore(YahtzeeCategory.ONES, new int[]{1, 2, 3, 4, 5});
		
		int[] notYahtzee = {1, 2, 3, 4, 5};
		assertFalse(controller.recordBonusYahtzee(notYahtzee));
	}
	
	// ========== GAME STATE TESTS ==========
	
	@Test
	void testGameNotOverInitially() {
		assertFalse(controller.isGameOver());
	}
	
	@Test
	void testGameOverAfterAllCategoriesScored() {
		YahtzeeCategory[] categories = YahtzeeCategory.values();
		int[] dice = {1, 2, 3, 4, 5};
		
		for (int i = 0; i < 13; i++) {
			controller.recordScore(categories[i], dice); // Player 1
			controller.recordScore(categories[i], dice); // Player 2
		}
		
		assertTrue(controller.isGameOver());
	}
	
	@Test
	void testGetWinnerPlayer1() {
		// Player 1 scores high
		controller.recordScore(YahtzeeCategory.YAHTZEE, new int[]{6, 6, 6, 6, 6});
		// Player 2 scores low
		controller.recordScore(YahtzeeCategory.ONES, new int[]{1, 2, 3, 4, 5});
		
		// Complete all remaining categories
		YahtzeeCategory[] categories = YahtzeeCategory.values();
		int[] dice = {1, 2, 3, 4, 5};
		
		for (int i = 1; i < 13; i++) {
			controller.recordScore(categories[i], dice);
			controller.recordScore(categories[i], dice);
		}
		
		assertNotNull(controller.getWinner());
		assertEquals("Player 1", controller.getWinner().getName());
	}
	
	@Test
	void testGetWinnerTie() {
		YahtzeeCategory[] categories = YahtzeeCategory.values();
		int[] dice = {3, 3, 3, 3, 3};
		
		for (int i = 0; i < 13; i++) {
			controller.recordScore(categories[i], dice);
			controller.recordScore(categories[i], dice);
		}
		
		assertNull(controller.getWinner());
	}
	
	@Test
	void testGetScorecard() {
		assertNotNull(controller.getScorecard());
		assertEquals(controller.getCurrentPlayer().getScorecard(), controller.getScorecard());
	}
	
	// ========== SAVE/LOAD TESTS ==========
	
	@Test
	void testSaveGame() {
		controller.recordScore(YahtzeeCategory.ONES, new int[]{1, 1, 1, 2, 3});
		controller.saveGame();
		
		File saveFile = new File("save_yahtzee.dat");
		assertTrue(saveFile.exists());
	}
	
	@Test
	void testLoadGame() {
		controller.recordScore(YahtzeeCategory.ONES, new int[]{1, 1, 1, 2, 3});
		controller.recordScore(YahtzeeCategory.TWOS, new int[]{2, 2, 2, 3, 4});
		controller.saveGame();
		
		YahtzeeInstance loaded = YahtzeeInstance.loadGame();
		assertNotNull(loaded);
		
		assertEquals("Player 1", loaded.getPlayer1().getName());
		assertEquals("Player 2", loaded.getPlayer2().getName());
		assertEquals("Player 1", loaded.getCurrentPlayer().getName());
	}
	
	@Test
	void testLoadGameWithNoSaveFile() {
		File saveFile = new File("save_yahtzee.dat");
		if (saveFile.exists()) {
			saveFile.delete();
		}
		
		YahtzeeInstance loaded = YahtzeeInstance.loadGame();
		assertNull(loaded);
	}
	
	@Test
	void testLoadedGameState() {
	    controller.recordScore(YahtzeeCategory.ONES, new int[]{1, 1, 1, 2, 3});
	    controller.recordScore(YahtzeeCategory.TWOS, new int[]{2, 2, 2, 3, 4});
	    controller.saveGame();
	    
	    YahtzeeInstance loaded = YahtzeeInstance.loadGame();
	    YahtzeeModel loadedModel = new YahtzeeModel(loaded);
	    YahtzeeController loadedController = new YahtzeeController(loadedModel);
	    
	    assertEquals("Player 1", loadedController.getCurrentPlayer().getName());
	    assertEquals(3, loadedController.getRollsRemaining());
	    
	    // Check that the scores were preserved in the right players
	    assertFalse(loadedController.getPlayer1().getScorecard().isCategoryAvailable(YahtzeeCategory.ONES));
	    assertFalse(loadedController.getPlayer2().getScorecard().isCategoryAvailable(YahtzeeCategory.TWOS));
	}
	
	// ========== PLAYER/DIE TESTS ==========
	
	@Test
	void testPlayerGetDiceValues() {
		int[] values = controller.getCurrentPlayer().getDieValues();
		assertEquals(5, values.length);
		
		for (int val : values) {
			assertTrue(val >= 1 && val <= 6);
		}
	}
	
	@Test
	void testPlayerRollAll() {
		YahtzeePlayer player = controller.getCurrentPlayer();
		player.rollAll();
		
		int[] values = player.getDieValues();
		for (int val : values) {
			assertTrue(val >= 1 && val <= 6);
		}
	}
	
	@Test
	void testDieHolding() {
		YahtzeeDie die = controller.getCurrentPlayer().getDice()[0];
		
		assertFalse(die.isHeld());
		die.toggleHold();
		assertTrue(die.isHeld());
		die.toggleHold();
		assertFalse(die.isHeld());
	}
	
	@Test
	void testDieReset() {
		YahtzeeDie die = controller.getCurrentPlayer().getDice()[0];
		die.toggleHold();
		assertTrue(die.isHeld());
		
		die.reset();
		assertFalse(die.isHeld());
	}
	
	@Test
	void testHeldDieDoesntRoll() {
		YahtzeeDie die = controller.getCurrentPlayer().getDice()[0];
		int originalValue = die.getValue();
		
		die.toggleHold();
		controller.getCurrentPlayer().rollAll();
		
		assertEquals(originalValue, die.getValue());
	}
	
	@Test
	void testDieValueInRange() {
		YahtzeeDie die = new YahtzeeDie();
		int value = die.getValue();
		assertTrue(value >= 1 && value <= 6);
	}
	
	// ========== SCORECARD DETAIL TESTS ==========
	
	@Test
	void testScorecardGetScores() {
		controller.recordScore(YahtzeeCategory.ONES, new int[]{1, 1, 1, 2, 3});
		
		assertNotNull(controller.getScorecard().getScores());
		assertEquals(3, controller.getPlayer1().getScorecard().getScores().get(YahtzeeCategory.ONES));
	}
	
	@Test
	void testScorecardGrandTotal() {
		controller.recordScore(YahtzeeCategory.ONES, new int[]{1, 1, 1, 1, 1});
		controller.recordScore(YahtzeeCategory.CHANCE, new int[]{1, 1, 1, 1, 1});
		
		controller.recordScore(YahtzeeCategory.CHANCE, new int[]{6, 6, 6, 6, 6});
		
		YahtzeeScorecard p1Scorecard = controller.getPlayer1().getScorecard();
		assertEquals(35, p1Scorecard.getGrandTotal());
	}
	
	@Test
	void testScorecardGetUpperSection() {
		controller.recordScore(YahtzeeCategory.ONES, new int[]{1, 1, 1, 1, 1});
		controller.recordScore(YahtzeeCategory.CHANCE, new int[]{1, 1, 1, 1, 1});
		
		assertEquals(5, controller.getPlayer1().getScorecard().getUpperSection());
	}
	
	@Test
	void testScorecardGetLowerSection() {
		controller.recordScore(YahtzeeCategory.CHANCE, new int[]{6, 6, 6, 6, 6});
		controller.recordScore(YahtzeeCategory.CHANCE, new int[]{1, 1, 1, 1, 1});
		
		assertEquals(30, controller.getPlayer1().getScorecard().getLowerSection());
	}
	
	@Test
	void testScorecardYahtzeeBonusTracking() {
		int[] yahtzee = {5, 5, 5, 5, 5};
		controller.recordScore(YahtzeeCategory.YAHTZEE, yahtzee);
		controller.recordScore(YahtzeeCategory.ONES, new int[]{1, 2, 3, 4, 5});
		
		controller.recordBonusYahtzee(yahtzee);
		
		assertEquals(100, controller.getPlayer1().getScorecard().getYahtzeeBonus());
	}
}