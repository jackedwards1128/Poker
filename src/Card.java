import javax.swing.*;
import java.awt.*;

public class Card {
    private String rank;
    private String suit;
    private int value;
    private int cardNum;
    private boolean shown;

    // Constructor
    public Card(String rank, String suit, int value, int cardNum, GameView window) {
        this.rank = rank;
        this.suit = suit;
        this.value = value;
        this.cardNum = cardNum;
        shown = true;

    }

    // Getters and setters
    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }

    // Draws the card itself, pulling the image from the images array in the frontend
    public void draw(Graphics g, int x, int y, GameView window) {
        if (shown) {
            g.drawImage(window.getCardImages()[cardNum], x, y, 80, 121, window);
        } else {
            // Displays back card if being hidden
            g.drawImage(window.getBackCard(), x, y, 80, 121, window);
        }
    }
}
