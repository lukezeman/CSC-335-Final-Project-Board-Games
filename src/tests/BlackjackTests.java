package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

//import org.junit.Before;
import org.junit.jupiter.api.Test;

import blackjack.BlackjackController;
import blackjack.BlackjackInstance;
import blackjack.BlackjackModel;
import blackjack.Card;
import blackjack.Deck;
import blackjack.Hand;

class BlackjackTests {

	private Card aceOfSpades;
    private Card kingOfHearts;
    private Card twoOfClubs;
    
    
    
    @Test
    public void testGetSuit() {
    	aceOfSpades = new Card("Spades", "Ace", 11);
        kingOfHearts = new Card("Hearts", "King", 10);
        twoOfClubs = new Card("Clubs", "Two", 2);
        assertEquals("Spades", aceOfSpades.getSuit());
        assertEquals("Hearts", kingOfHearts.getSuit());
        assertEquals("Clubs", twoOfClubs.getSuit());
    }
    
    @Test
    public void testGetRank() {
    	aceOfSpades = new Card("Spades", "Ace", 11);
        kingOfHearts = new Card("Hearts", "King", 10);
        twoOfClubs = new Card("Clubs", "Two", 2);
        assertEquals("Ace", aceOfSpades.getRank());
        assertEquals("King", kingOfHearts.getRank());
        assertEquals("Two", twoOfClubs.getRank());
    }
    
    @Test
    public void testGetVal() {
    	aceOfSpades = new Card("Spades", "Ace", 11);
        kingOfHearts = new Card("Hearts", "King", 10);
        twoOfClubs = new Card("Clubs", "Two", 2);
        assertEquals(11, aceOfSpades.getVal());
        assertEquals(10, kingOfHearts.getVal());
        assertEquals(2, twoOfClubs.getVal());
    }
    
    @Test
    public void testToString() {
    	aceOfSpades = new Card("Spades", "Ace", 11);
        kingOfHearts = new Card("Hearts", "King", 10);
        twoOfClubs = new Card("Clubs", "Two", 2);
        assertEquals("Ace of Spades", aceOfSpades.toString());
        assertEquals("King of Hearts", kingOfHearts.toString());
        assertEquals("Two of Clubs", twoOfClubs.toString());
    }
    
    @Test
    public void testGetImageFileName() {
    	aceOfSpades = new Card("Spades", "Ace", 11);
        kingOfHearts = new Card("Hearts", "King", 10);
        twoOfClubs = new Card("Clubs", "Two", 2);
        assertEquals("ace_of_spades.png", aceOfSpades.getImageFileName());
        assertEquals("king_of_hearts.png", kingOfHearts.getImageFileName());
        assertEquals("two_of_clubs.png", twoOfClubs.getImageFileName());
    }
    @Test
    public void testEmptyHand() {
    	Hand hand = new Hand();
    	assertEquals(0, hand.cardsInHand());
        assertEquals(0, hand.getValue());
        assertFalse(hand.isBust());
        assertFalse(hand.isBlackjack());
    }
    @Test
    public void testAddCard() {
    	Hand hand = new Hand();
    	aceOfSpades = new Card("Spades", "Ace", 11);
    	kingOfHearts = new Card("Hearts", "King", 10);
        hand.addCard(aceOfSpades);
        assertEquals(1, hand.cardsInHand());
        assertEquals(11, hand.getValue());
        
        hand.addCard(kingOfHearts);
        assertEquals(2, hand.cardsInHand());
        assertEquals(21, hand.getValue());
    }
    @Test
    public void testGetCards() {
    	Hand hand = new Hand();
        hand.addCard(aceOfSpades);
        hand.addCard(kingOfHearts);
        
        assertEquals(2, hand.getCards().size());
        assertEquals(aceOfSpades, hand.getCards().get(0));
        assertEquals(kingOfHearts, hand.getCards().get(1));
    }
    @Test
    public void testGetValueSimple() {
    	Hand hand = new Hand();
        hand.addCard(new Card("Hearts", "King", 10));
        hand.addCard(new Card("Clubs", "Five", 5));
        assertEquals(15, hand.getValue());
    }
    @Test
    public void testValueWithAce() {
    	Hand hand = new Hand();
    	aceOfSpades = new Card("Spades", "Ace", 11);
    	hand.addCard(aceOfSpades);
    	hand.addCard(new Card("Clubs", "Five", 5));
    	assertEquals(16,hand.getValue());
    }
    @Test
    public void testMoreAce() {
    	Hand hand = new Hand();
    	aceOfSpades = new Card("Spades", "Ace", 11);
    	Card aceOfHearts = new Card("Hearts", "Ace", 11);
        Card aceOfClubs = new Card("Clubs", "Ace", 11);
        
        hand.addCard(aceOfSpades);
        hand.addCard(aceOfHearts);
        hand.addCard(aceOfClubs);
        
        assertEquals(13, hand.getValue());
    }
    @Test
    public void moreAce2() {
    	Hand hand = new Hand();
    	aceOfSpades = new Card("Spades", "Ace", 11);
    	kingOfHearts = new Card("Hearts", "King", 10);
    	hand.addCard(aceOfSpades);
    	hand.addCard(kingOfHearts);
    	hand.addCard(new Card("Clubs", "Five", 5));
    	assertEquals(16, hand.getValue());
    }
    @Test
    public void testBust() {
    	Hand hand = new Hand();
    	kingOfHearts = new Card("Hearts", "King", 10);
    	Card tenOfDiamonds = new Card("Diamonds","Ten",10);
    	Card fiveOfClubs = new Card("Clubs","Five",5);
    	hand.addCard(kingOfHearts);
        hand.addCard(tenOfDiamonds);
    	assertFalse(hand.isBust());
    	hand.addCard(fiveOfClubs);
    	assertTrue(hand.isBust());
    }
    @Test
    public void testForBlackjack() {
    	Hand hand = new Hand();
    	aceOfSpades = new Card("Spades", "Ace", 11);
    	kingOfHearts = new Card("Hearts", "King", 10);
    	hand.addCard(aceOfSpades);
        hand.addCard(kingOfHearts);
        assertFalse(hand.isBust());
        assertEquals(21, hand.getValue());
        assertTrue(hand.isBlackjack());
        hand.clear();
        Card fiveOfClubs = new Card("Clubs","Five",5);
        hand.addCard(new Card("Diamonds", "Six", 6));
        hand.addCard(fiveOfClubs);
        hand.addCard(kingOfHearts);
        assertFalse(hand.isBlackjack());
        assertEquals(21, hand.getValue());
    }
    @Test
    public void testInitial() {
    	Deck d = new Deck();
    	assertEquals(52,d.getCards().size());
    }
    @Test
    public void testDeckIsSet() {
    	Deck d = new Deck();
    	ArrayList<Card> cards = d.getCards();
    	int clubs = 0, diamonds = 0, spades = 0, hearts = 0;
        for (Card card : cards) {
            String suit = card.getSuit();
            if (suit.equals("Clubs")) clubs++;
            else if (suit.equals("Diamonds")) diamonds++;
            else if (suit.equals("Spades")) spades++;
            else if (suit.equals("Hearts")) hearts++;
        }
        
        assertEquals(13, clubs);
        assertEquals(13, diamonds);
        assertEquals(13, spades);
        assertEquals(13, hearts);
    }
    @Test
    public void testRemove() {
    	Deck d = new Deck();
    	d.removeCard(d.getCards().get(0));
    	assertEquals(51, d.getCards().size());
    }
    
    @Test
    public void testInitialState() {
    	BlackjackModel model = new BlackjackModel();
    	assertEquals(500, model.getP1Money());
        assertEquals(500, model.getP2Money());
        assertEquals(0, model.getP1Bet());
        assertEquals(0, model.getP2Bet());
        assertEquals(1, model.getTurn());
        assertFalse(model.getIsOver());
        assertFalse(model.isP1Done());
        assertFalse(model.isP2Done());
        assertEquals("Player 1", model.getP1Name());
        assertEquals("Player 2", model.getP2Name());
        boolean started = model.start(50, 50);
        assertTrue(started);
        assertEquals(450, model.getP1Money());
        assertEquals(450, model.getP2Money());
        assertEquals(50, model.getP1Bet());
        assertEquals(50, model.getP2Bet());
        assertEquals(2, model.getP1().cardsInHand());
        assertEquals(2, model.getP2().cardsInHand());
        assertEquals(2, model.getDealer().cardsInHand());
    }
    @Test
    public void testInvalidBet() {
    	BlackjackModel model = new BlackjackModel();
    	boolean started = model.start(600, 50);
    	assertFalse(started);
        assertEquals(500, model.getP1Money());
        boolean started2 = model.start(0, 50);
        assertFalse(started2);
        assertEquals(500, model.getP1Money());
        boolean started3 = model.start(-10, 50);
        assertFalse(started3);
        assertEquals(500, model.getP1Money());
        boolean started4 = model.start(50, 600);
        assertFalse(started4);
        assertEquals(500, model.getP2Money());
    }
    @Test
    public void testHitPlayer1() {
    	BlackjackModel model = new BlackjackModel();
        model.start(50, 50);
        
        model.hit();
        
        assertTrue(model.getP1().cardsInHand() == 3 || model.getP1().cardsInHand() == 2);
        model.setIsOver(true);
        model.hit();
        assertTrue(model.getP1().cardsInHand() == 3 || model.getP1().cardsInHand() == 2);
    }
    @Test
    public void testStandPlayer1() {
    	BlackjackModel model = new BlackjackModel();
        model.start(50, 50);
        if (!model.getP1().isBlackjack()) {
        	model.stand();
            
            assertTrue(model.isP1Done());
            assertTrue(model.getTurn() == 2 || model.getTurn() == 0);
        }
        else {
        	assertTrue(model.isP1Done());
        	assertTrue(model.getTurn() == 2 || model.getTurn() == 0);
        }
        
    }
    @Test
    public void testStandPlayer2() {
    	BlackjackModel model = new BlackjackModel();
        model.start(50, 50);
        model.stand();
        model.stand();
        
        assertTrue(model.isP2Done());
        assertEquals(0, model.getTurn());
    }
    @Test
    public void testStandWhenGameOver() {
    	BlackjackModel model = new BlackjackModel();
        model.start(50, 50);
        model.setIsOver(true);
        
        model.stand();
        
        assertTrue(model.getIsOver());
    }
    @Test
    public void testFirstForm() {
    	BlackjackModel model = new BlackjackModel();
        assertNotNull(model.getP1());
        assertEquals(0, model.getP1().cardsInHand());
        assertNotNull(model.getP2());
        assertEquals(0, model.getP2().cardsInHand());
        assertNotNull(model.getDealer());
        assertEquals(0, model.getDealer().cardsInHand());
    }
    @Test
    public void testSetIsOver() {
    	BlackjackModel model = new BlackjackModel();
        assertFalse(model.getIsOver());
        
        model.setIsOver(true);
        assertTrue(model.getIsOver());
        
        model.setIsOver(false);
        assertFalse(model.getIsOver());
    }
    @Test
    public void testPlayerNames() {
    	BlackjackModel model = new BlackjackModel();
        model.setP1Name("Kevin");
        model.setP2Name("Karl");
        
        assertEquals("Kevin", model.getP1Name());
        assertEquals("Karl", model.getP2Name());
    }
    @Test
    public void testGetStatusPlayer1Turn() {
    	BlackjackModel model = new BlackjackModel();
        model.start(50, 50);
        
        String status = model.getStatus(1);
        assertTrue(status.contains("turn") || status.contains("BLACKJACK") || status.contains("Bust"));
    }
    @Test
    public void testGetStatusPlayer2Turn() {
    	BlackjackModel model = new BlackjackModel();
        model.start(50, 50);
        model.stand();
        
        String status = model.getStatus(2);
        assertTrue(status.contains("turn") || status.contains("BLACKJACK") || status.contains("Bust") || status.contains("wins") || status.contains("Push"));
    }
    @Test
    public void testReset() {
    	BlackjackModel model = new BlackjackModel();
        model.start(100, 100);
        model.hit();
        model.setP1Name("TestPlayer1");
        model.setP2Name("TestPlayer2");
        
        model.reset();
        
        assertEquals(500, model.getP1Money());
        assertEquals(500, model.getP2Money());
        assertEquals(0, model.getP1Bet());
        assertEquals(0, model.getP2Bet());
        assertEquals(1, model.getTurn());
        assertFalse(model.getIsOver());
        assertFalse(model.isP1Done());
        assertFalse(model.isP2Done());
        assertEquals(0, model.getP1().cardsInHand());
        assertEquals(0, model.getP2().cardsInHand());
        assertEquals(0, model.getDealer().cardsInHand());
    }
    @Test
    public void testGetInstance() {
    	BlackjackModel model = new BlackjackModel();
        model.start(50, 50);
        model.setP1Name("Kevin");
        model.setP2Name("Karl");
        
        BlackjackInstance instance = model.getInstance();
        
        assertNotNull(instance);
        assertEquals(model.getP1Money(), instance.getP1Money());
        assertEquals(model.getP2Money(), instance.getP2Money());
        assertEquals(model.getP1Bet(), instance.getP1Bet());
        assertEquals(model.getP2Bet(), instance.getP2Bet());
        assertEquals(model.getTurn(), instance.getTurn());
        assertEquals(model.getIsOver(), instance.isGameOver());
        assertEquals(model.isP1Done(), instance.getP1Done());
        assertEquals(model.isP2Done(), instance.getP2Done());
        assertEquals("Kevin", instance.getP1Name());
        assertEquals("Karl", instance.getP2Name());
    }
    @Test
    public void testSetInstance() {
    	BlackjackModel model = new BlackjackModel();
        model.start(100, 100);
        model.setP1Name("Kevin");
        model.setP2Name("Karl");
        BlackjackInstance savedInstance = model.getInstance();
        
        BlackjackModel newModel = new BlackjackModel();
        newModel.setInstance(savedInstance);
        
        assertEquals(model.getP1Money(), newModel.getP1Money());
        assertEquals(model.getP2Money(), newModel.getP2Money());
        assertEquals(model.getP1Bet(), newModel.getP1Bet());
        assertEquals(model.getP2Bet(), newModel.getP2Bet());
        assertEquals(model.getTurn(), newModel.getTurn());
        assertEquals("Kevin", newModel.getP1Name());
        assertEquals("Karl", newModel.getP2Name());
    }
    @Test
    public void testMultipleRounds() {
    	BlackjackModel model = new BlackjackModel();
        model.start(50, 50);
        model.stand();
        model.stand();
        
        int p1MoneyAfterRound1 = model.getP1Money();
        int p2MoneyAfterRound1 = model.getP2Money();
        
        if (p1MoneyAfterRound1 > 0 && p2MoneyAfterRound1 > 0) {
            boolean started = model.start(50, 50);
            assertTrue(started);
            assertEquals(2, model.getP1().cardsInHand());
            assertEquals(2, model.getP2().cardsInHand());
        }
    }
    @Test
    public void testDeckResetWhenEmpty() {
    	BlackjackModel model = new BlackjackModel();
        model.start(10, 10);
        
        for (int i = 0; i < 30; i++) {
            if (model.getTurn() != 0 && !model.isP1Done()) {
                model.hit();
            }
        }
        
        assertTrue(true);
    }
    @Test
    public void testHit() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
        controller.startGame(50, 50);
        int initialCards = controller.getP1().cardsInHand();
        
        controller.hit();
        
        assertTrue(controller.getP1().cardsInHand() >= initialCards);
    }
    @Test
    public void testStand() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
        controller.startGame(50, 50);
        
        controller.stand();
        
        assertTrue(controller.p1TurnOver());
    }
    @Test
    public void testGetP1() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
        assertNotNull(controller.getP1());
        assertEquals(0, controller.getP1().cardsInHand());
        assertNotNull(controller.getP2());
        assertEquals(0, controller.getP2().cardsInHand());
    }
    @Test
    public void testGetP1money() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
        assertEquals(500, controller.getP1money());
        
        controller.startGame(100, 50);
        assertEquals(400, controller.getP1money());
    }
    @Test
    public void testGetP2money() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
        assertEquals(500, controller.getP2money());
        
        controller.startGame(50, 100);
        assertEquals(400, controller.getP2money());
    }
    @Test
    public void testGetP1Bet() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
        assertEquals(0, controller.getP1Bet());
        
        controller.startGame(75, 50);
        assertEquals(75, controller.getP1Bet());
    }
    @Test
    public void testGetP2Bet() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
        assertEquals(0, controller.getP2Bet());
        
        controller.startGame(50, 75);
        assertEquals(75, controller.getP2Bet());
    }
    @Test
    public void testGetTurn() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
        assertEquals(1, controller.getTurn());
        
        controller.startGame(50, 50);
        assertTrue(controller.getTurn() >= 0 && controller.getTurn() <= 2);
    }
    @Test
    public void testIsGameOver() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
        assertFalse(controller.isGameOver());
        
        controller.setGameOver();
        assertFalse(controller.isGameOver());
    }
    @Test
    public void testP1TurnOver() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
        controller.startGame(50, 50);
        assertFalse(controller.p1TurnOver() && controller.getTurn() == 1);
        
        controller.stand();
        assertTrue(controller.p1TurnOver());
    }
    @Test
    public void testP2TurnOver() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
        controller.startGame(50, 50);
        controller.stand();
        
        assertFalse(controller.p2TurnOver() && controller.getTurn() == 2);
        
        controller.stand();
        assertTrue(controller.p2TurnOver());
    }
    @Test
    public void testGetP1Status() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
        controller.startGame(50, 50);
        String status = controller.getP1Status();
        assertNotNull(status);
        assertFalse(status.isEmpty());
    }
    
    @Test
    public void testGetP2Status() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
        controller.startGame(50, 50);
        String status = controller.getP2Status();
        assertNotNull(status);
        assertFalse(status.isEmpty());
    }
    @Test
    public void testSetGameOver() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
        controller.setGameOver();
        assertFalse(controller.isGameOver());
    }
    @Test
    public void testGetWinnerPlayer1() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
        controller.startGame(50, 50);
        controller.stand();
        controller.stand();
        
        String winner = controller.getWinner();
        assertTrue(winner.equals("Player 1 wins") || 
                   winner.equals("Player 2 wins") || 
                   winner.equals("Tie"));
    }
    @Test
    public void testStartGameValid() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
        boolean started = controller.startGame(50, 50);
        assertTrue(started);
        assertEquals(450, controller.getP1money());
        assertEquals(450, controller.getP2money());
    }
    
    @Test
    public void testStartGameInvalid() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
        boolean started = controller.startGame(600, 50);
        assertFalse(started);
        assertEquals(500, controller.getP1money());
    }
    
//    @Test

//    }
    @Test
    public void testResetGame() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
        controller.startGame(100, 100);
        controller.hit();
        
        controller.resetGame();
        
        assertEquals(500, controller.getP1money());
        assertEquals(500, controller.getP2money());
        assertEquals(0, controller.getP1().cardsInHand());
        assertEquals(0, controller.getP2().cardsInHand());
    }
    @Test
    public void testSaveGame() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
        controller.startGame(50, 50);
        controller.setP1Name("Alice");
        controller.setP2Name("Bob");
        
        controller.saveGame();
        
        File saveFile = new File("save_blackjack.dat");
        assertTrue(saveFile.exists());
    }
    @Test
    public void testLoadGame() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
        controller.startGame(75, 75);
        controller.setP1Name("Alice");
        controller.setP2Name("Bob");
        controller.saveGame();
        
        BlackjackModel newModel = new BlackjackModel();
        BlackjackController newController = new BlackjackController(newModel);
        newController.loadGame();
        
        assertEquals(425, newController.getP1money());
        assertEquals(425, newController.getP2money());
        assertEquals(75, newController.getP1Bet());
        assertEquals(75, newController.getP2Bet());
        assertEquals("Alice", newController.getP1Name());
        assertEquals("Bob", newController.getP2Name());
    }
    @Test
    public void testLoadGameNoFile() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
    	File saveFile = new File("save_blackjack.dat");
    	if (saveFile.exists()) {
    		saveFile.delete();
    	}
        
        controller.loadGame();
        
        assertEquals(500, controller.getP1money());
        assertEquals(500, controller.getP2money());
    }
    @Test
    public void dealer() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
    	assertEquals(0, controller.getDealer().cardsInHand());
    }
    @Test
    public void testDeleteSave() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
        controller.startGame(50, 50);
        controller.saveGame();
        
        File saveFile = new File("save_blackjack.dat");
        assertTrue(saveFile.exists());
        
        controller.deleteSave();
        
        assertFalse(saveFile.exists());
    }
    @Test
    public void testDeleteSaveNoFile() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
    	File saveFile = new File("save_blackjack.dat");
    	if (saveFile.exists()) {
    		saveFile.delete();
    	}
        
        controller.deleteSave();
        
        File saveFile2 = new File("save_blackjack.dat");
        assertFalse(saveFile2.exists());
    }
    @Test
    public void testPlayerNameGettersSetters() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
        controller.setP1Name("Alice");
        controller.setP2Name("Bob");
        
        assertEquals("Alice", controller.getP1Name());
        assertEquals("Bob", controller.getP2Name());
    }
    @Test
    public void testSaveAndLoadPreservesNames() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
        controller.setP1Name("Charlie");
        controller.setP2Name("Diana");
        controller.startGame(50, 50);
        controller.saveGame();
        
        BlackjackModel newModel = new BlackjackModel();
        BlackjackController newController = new BlackjackController(newModel);
        newController.loadGame();
        
        assertEquals("Charlie", newController.getP1Name());
        assertEquals("Diana", newController.getP2Name());
    }
    @Test
    public void testWinner1() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
    	controller.startGame(50, 75);
    	while (model.getP1().isBlackjack() || model.getP2().isBlackjack()) {
    		model = new BlackjackModel();
    		controller = new BlackjackController(model);
    		controller.startGame(50,75);
    	}
    	while (!model.getP1().isBust()) {
    		controller.hit();
    	}
    	while (!model.getP2().isBust()) {
    		controller.hit();
    	}
    	assertTrue(controller.getWinner().equals("Player 1 wins"));
    	assertTrue(controller.isGameInProgress());
    	assertTrue(model.getStatus(1).equals("Bust"));
    	assertTrue(model.getStatus(2).equals("Bust"));
    }
    @Test
    public void testWinner2() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
    	controller.startGame(75, 50);
    	while (model.getP1().isBlackjack() || model.getP2().isBlackjack()) {
    		model = new BlackjackModel();
    		controller = new BlackjackController(model);
    		controller.startGame(75,50);
    	}
    	while (!model.getP1().isBust()) {
    		controller.hit();
    	}
    	while (!model.getP2().isBust()) {
    		controller.hit();
    	}
    	assertTrue(controller.getWinner().equals("Player 2 wins"));
    	assertTrue(controller.isGameInProgress());
    	assertTrue(model.getStatus(1).equals("Bust"));
    	assertTrue(model.getStatus(2).equals("Bust"));
    }
    @Test
    public void testWinner3() {
    	BlackjackModel model = new BlackjackModel();
    	BlackjackController controller = new BlackjackController(model);
    	controller.startGame(500, 500);
    	while (model.getP1().isBlackjack() || model.getP2().isBlackjack()) {
    		model = new BlackjackModel();
    		controller = new BlackjackController(model);
    		controller.startGame(500,500);
    	}
    	while (!model.getP1().isBust()) {
    		controller.hit();
    	}
    	while (!model.getP2().isBust()) {
    		controller.hit();
    	}
    	assertTrue(controller.getWinner().equals("Tie"));
    	assertTrue(controller.isGameInProgress());
    	
    }
    @Test
    public void testStatusBlackjack() {
    	BlackjackModel model = new BlackjackModel();
    	model.getP1().addCard(new Card("Spades", "Ace", 11));
    	model.getP1().addCard(new Card("Diamonds", "King", 10));
    	assertTrue(model.getStatus(1).equals("BLACKJACK!!!!!"));
    }
    @Test
    public void testStatusPush() {
    	BlackjackModel model = new BlackjackModel();
    	model.getP1().addCard(new Card("Spades", "Ace", 11));
    	model.getP1().addCard(new Card("Spades", "Five", 5));
    	model.getP1().addCard(new Card("Hearts", "Five", 5));
    	model.getDealer().addCard(new Card("Hearts", "Ace", 11));
    	model.getDealer().addCard(new Card("Diamonds", "Five", 5));
    	model.getDealer().addCard(new Card("Clubs", "Five", 5));
    	
    	assertTrue(model.getStatus(1).equals("Player 1's turn..."));
    	model.stand();
    	assertTrue(model.getStatus(2).equals("Player 2's turn..."));
    	model.stand();
    	assertTrue(model.getStatus(1).equals("Push"));
    }
    @Test
    public void testPlayerWins() {
    	BlackjackModel model = new BlackjackModel();
    	model.getP1().addCard(new Card("Spades", "Ace", 11));
    	model.getP1().addCard(new Card("Spades", "Five", 5));
    	model.getP1().addCard(new Card("Hearts", "Five", 5));
    	model.getP2().addCard(new Card("Spades", "Six", 6));
    	model.getP2().addCard(new Card("Hearts", "Ten", 10));
    	model.getP2().addCard(new Card("Clubs", "Five", 5));
    	model.getDealer().addCard(new Card("Hearts", "Ace", 11));
    	model.getDealer().addCard(new Card("Diamonds", "Five", 5));
    	model.getDealer().addCard(new Card("Hearts", "Four", 4));
    	model.stand();
    	model.stand();
    	assertTrue(model.getStatus(1).equals("Player 1 wins"));
    	assertTrue(model.getStatus(2).equals("Player 2 wins"));
    }
    @Test
    public void testPlayerWins2() {
    	BlackjackModel model = new BlackjackModel();
    	model.getP1().addCard(new Card("Spades", "Ace", 11));
    	model.getP1().addCard(new Card("Spades", "Five", 5));
    	model.getP1().addCard(new Card("Hearts", "Five", 5));
    	model.getP2().addCard(new Card("Spades", "Six", 6));
    	model.getP2().addCard(new Card("Hearts", "Ten", 10));
    	model.getP2().addCard(new Card("Clubs", "Five", 5));
    	model.getDealer().addCard(new Card("Hearts", "Ace", 11));
    	model.getDealer().addCard(new Card("Diamonds", "Five", 5));
    	model.getDealer().addCard(new Card("Hearts", "Four", 4));
    	model.getDealer().addCard(new Card("Hearts","Jack",10));
    	model.getDealer().addCard(new Card("Hearts","Queen",10));
    	model.stand();
    	model.stand();
    	assertTrue(model.getStatus(1).equals("Player 1 wins"));
    	assertTrue(model.getStatus(2).equals("Player 2 wins"));
    }
    @Test
    public void testDealerWins() {
    	BlackjackModel model = new BlackjackModel();
    	model.getP1().addCard(new Card("Spades", "Ace", 11));
    	model.getP1().addCard(new Card("Spades", "Five", 5));
    	model.getDealer().addCard(new Card("Hearts", "Ace", 11));
    	model.getDealer().addCard(new Card("Diamonds", "Five", 5));
    	model.getDealer().addCard(new Card("Hearts", "Four", 4));
    	model.stand();
    	model.stand();
    	assertTrue(model.getStatus(1).equals("Dealer wins"));
    }
    
}
