package blackjack;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
/**
 * This class represents the display of card images
 * 
 * @author Aidin Miller
 * @version 1.0
 */
public class CardView extends VBox {
	private static final int CARD_WIDTH = 75;
    private static final int CARD_HEIGHT = 105;
    
    private StackPane cardPane;
    private ImageView imageView;
    private Rectangle cardBack;
    private Label cardNameLabel;
    /**
     * Constructor for the class
     */
    public CardView() {
    	this.setAlignment(Pos.CENTER);
    	this.setSpacing(3);
    	
    	cardPane = new StackPane();
        imageView = new ImageView();
        imageView.setFitWidth(CARD_WIDTH);
        imageView.setFitHeight(CARD_HEIGHT);
        imageView.setPreserveRatio(false);
        
        cardBack = new Rectangle(CARD_WIDTH, CARD_HEIGHT);
        cardBack.setFill(Color.DARKBLUE);
        cardBack.setStroke(Color.WHITE);
        cardBack.setStrokeWidth(2);
        cardBack.setArcWidth(10);
        cardBack.setArcHeight(10);
        
        cardPane.getChildren().add(imageView);
        
        cardNameLabel = new Label("");
        
        this.getChildren().addAll(cardPane, cardNameLabel);
    }
    /**
     * This function takes a card input and displays the image on the GUI
     * 
     * @param card The card object that will be displayed
     */
    public void setCard(Card card) {
    	try {
            String imagePath = "/cards/" + card.getImageFileName();
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            
            if (image.isError()) {
                throw new Exception("Image failed to load");
            }
            
            imageView.setImage(image);
            cardNameLabel.setText(card.toString());
            //System.out.println("Loaded card image: " + imagePath);
        } catch (Exception e) {
            System.err.println("Could not load image: " + card.getImageFileName());
            System.err.println("Error: " + e.getMessage());
            showTextCard(card);
        }
    }
    /**
     * This method is for if the image cannot be loaded
     * 
     * @param card The card to represent as text
     */
	private void showTextCard(Card card) {
		// TODO Auto-generated method stub
		Rectangle bg = new Rectangle(CARD_WIDTH, CARD_HEIGHT);
        bg.setFill(Color.WHITE);
        bg.setStroke(Color.BLACK);
        bg.setStrokeWidth(2);
        bg.setArcWidth(10);
        bg.setArcHeight(10);
        
        Text text = new Text(card.getRank() + "\n" + card.getSuit());
        
        if (card.getSuit().equals("Hearts") || card.getSuit().equals("Diamonds")) {
            text.setFill(Color.RED);
        }
        
        cardPane.getChildren().clear();
        cardPane.getChildren().addAll(bg, text);
        cardNameLabel.setText(card.toString());
	}
	/**
	 * This function displays the back of a card
	 */
	public void showCardBack() {
        try {
            Image image = new Image(getClass().getResourceAsStream("/cards/card_back.png"));
            
            if (image.isError()) {
                throw new Exception("Card back image failed to load");
            }
            
            imageView.setImage(image);
            cardNameLabel.setText("Hidden");
            //System.out.println("Loaded card back");
        } catch (Exception e) {
            System.err.println("Could not load card back: " + e.getMessage());
            cardPane.getChildren().clear();
            cardPane.getChildren().add(cardBack);
            cardNameLabel.setText("Hidden");
        }
    }
	/**
	 * This function clears the cards being displayed
	 */
	public void clear() {
        imageView.setImage(null);
        cardNameLabel.setText("");
    }
}
