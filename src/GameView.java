import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameView extends JFrame {

    // Create backend variable to store Game object
    private Game backend;

    // Set window dimensions and store images for cards
    private final int WINDOW_WIDTH = 1200;
    private final int WINDOW_HEIGHT = 750;
    private Image[] cardImages = new Image[52];
    private Image backCard = new ImageIcon("Resources/Cards/back.png").getImage();

    // Stores the winner for when the grand reveal happens
    private Player winner;

    // Stores the name of the next player in line for the black in-between screens
    private String nextPlayer = "";

    public GameView(Game backend) {
        this.backend = backend;

        // Fill image storage array with all the necessary images
        for (int i = 0; i < 52; i++) {
            cardImages[i] = new ImageIcon("Resources/Cards/" + (i+1) + ".png").getImage();
        }

        // Setup the window and make it visible
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Poker");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        // Create font and paint the screen green
        Font LargeFont = new Font("arial", Font.PLAIN, 60);
        g.setColor(new Color(9, 74, 34));
        g.fillRect(0,0,WINDOW_WIDTH, WINDOW_HEIGHT);

        // Have each player tell each of its cards to draw itself
        for (Player player : backend.getPlayers()) {
            player.drawCards(g);
        }

        switch (backend.getState()) {
            // Case 0: Normal betting process
            case 0:
                printText(g);
                printBets(g);
                drawMiddleCards(g);
                break;
            // Case 1: Start of the program, instruction text
            case 1:
                Font SmallFont = new Font("arial", Font.PLAIN, 25);
                g.setColor(Color.white);
                g.setFont(LargeFont);
                g.drawString("WELCOME TO POKER", 300, 100);
                g.setFont(SmallFont);

                // Print instructions to player
                g.drawString("After inputting the number of players and your names, you will each be able to privately view" , 50, 200);
                g.drawString("your hand. After viewing your pocket, you will each bet on your hand. You can either fold,", 50, 240);
                g.drawString("check/call, or raise. Then 3 cards in the middle will be revealed, then 1 more again, for a ", 50, 280);
                g.drawString("total of 5 cards. Each time new cards are revealed, you will each bet on your hands again.", 50, 320);
                g.drawString("Your goal is to make the best possible 5-card hand from the 5 cards in the middle, and the 2", 50, 360);
                g.drawString("cards you have in your hand. The ranks go as following:", 50, 400);

                g.drawString("<<Best>>        Royal Flush (flush + a straight that ends with a king)", 50, 470);
                g.drawString("Straight Flush (flush + a straight)", 215, 500);
                g.drawString("Quads (four of a kind)", 215, 530);
                g.drawString("Full House (three of a kind and a pair)", 215, 560);
                g.drawString("Flush (all the same suit)", 215, 590);
                g.drawString("Straight (all numbers are in an ascending order)", 215, 620);
                g.drawString("Three of a kind", 215, 650);
                g.drawString("Two Pair", 215, 680);
                g.drawString("Pair", 215, 710);
                g.drawString("<<Worst>>     High Card", 50, 735);

                // Layer this text to make it appear bold
                g.drawString("Please use the terminal to enter the number of players.", 450, 700);
                g.drawString("Please use the terminal to enter the number of players.", 450, 701);
                g.drawString("Please use the terminal to enter the number of players.", 451, 700);
                break;
            // Case 2: Black in-between screen that goes between the betting rounds
            case 2:
                g.setColor(Color.black);
                g.fillRect(0,0,WINDOW_WIDTH, WINDOW_HEIGHT);
                g.setColor(Color.white);
                g.setFont(LargeFont);
                g.drawString("Press Enter to show the ", 190, 350);
                g.drawString("next player their cards", 200, 450);
                break;
            // Case 3: Black in-between screen that goes at the start of the game, showing cards to each player
            case 3:
                g.setColor(Color.black);
                g.fillRect(0,0,WINDOW_WIDTH, WINDOW_HEIGHT);
                g.setColor(Color.white);
                g.setFont(LargeFont);
                g.drawString("Press Enter to show " + nextPlayer, 190, 350);
                g.drawString(" their cards", 365, 450);
                break;
            // Case 4: The grand reveal at the end of the game
            case 4:
                ArrayList<Player> playersCopy = backend.getPlayersCopy();
                for (Player player : playersCopy) {
                    player.setCardsVisibility(true);
                    player.drawCards(g);
                }
                printRevealText(g);
                drawMiddleCards(g);
                printBets(g);
                break;
        }


    }

    // Instructs the center cards to draw themselves
    public void drawMiddleCards(Graphics g) {
        ArrayList<Card> cards = backend.getMiddleCards();

        if (cards != null) {
            for (int i = 0; i < cards.size(); i++) {
                cards.get(i).draw(g, 340 + (100 * i), 300, this);
            }
        }
    }

    // Draws the text that displays the pot and current bet to the player
    public void printText(Graphics g) {
        Font smallFont = new Font("arial", Font.PLAIN, 30);
        g.setColor(Color.white);
        g.setFont(smallFont);
        g.drawString("Current Pot: $" + backend.getPot(), 500, 280);
        g.drawString("Current Bet: $" + backend.getBet(), 500, 460);
    }

    // Draws the text with the final pot and winner at the grand reveal
    public void printRevealText(Graphics g) {
        Font smallFont = new Font("arial", Font.PLAIN, 30);
        g.setColor(Color.white);
        g.setFont(smallFont);
        g.drawString("Final Pot: $" + backend.getPot(), 500, 280);

        // In the judging script, the rank is multiplied by 100 with the high-card value being added, so
        // dividing by 100 yields the original rank number
        g.drawString(winner.getName() + " won with a " + backend.ranks[winner.getRank() / 100], 420, 460);
    }

    // Draws the text showing each player's deposited money into the pot for the game
    public void printBets(Graphics g) {
        Font smallFont = new Font("arial", Font.PLAIN, 30);
        g.setColor(Color.white);
        g.setFont(smallFont);

        ArrayList<Player> playersCopy = backend.getPlayersCopy();

        // Switches from top and bottom side of screen
        for (int i = 0; i < playersCopy.size(); i++) {
            if (i < 3) {
                g.drawString("$" + playersCopy.get(i).getBet(), 150 + (420 * i), 560);
            } else {
                g.drawString("$" + playersCopy.get(i).getBet(), 150 + (420 * (i-3)), 220);
            }
        }


    }

    public void setNextPlayer(String nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public Image[] getCardImages() {
        return cardImages;
    }

    public Image getBackCard() {
        return backCard;
    }
}
