import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameView extends JFrame {

    private Game backend;

    private ArrayList<Player> players;

    private final int WINDOW_WIDTH = 1200;
    private final int WINDOW_HEIGHT = 750;

    public Image backCard = new ImageIcon("Resources/Cards/back.png").getImage();

    private Player winner;

    public GameView(Game backend) {
        this.backend = backend;
        this.players = backend.getPlayers();

        // Setup the window and the buffer strategy.
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Poker");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        Font LargeFont = new Font("arial", Font.PLAIN, 60);
        g.setColor(new Color(9, 74, 34));
        g.fillRect(0,0,WINDOW_WIDTH, WINDOW_HEIGHT);

        for (Player player : players) {
            player.drawCards(g);
        }

        switch (backend.getState()) {
            case 3:
                g.setColor(Color.black);
                g.fillRect(0,0,WINDOW_WIDTH, WINDOW_HEIGHT);
                g.setColor(Color.white);
                g.setFont(LargeFont);
                g.drawString("Press Enter to show the next", 190, 350);
                g.drawString("player their cards", 365, 450);
                break;
            case 4:
                ArrayList<Player> playersCopy = backend.getPlayersCopy();
                for (Player player : playersCopy) {
                    player.setCardsVisibility(true);
                    player.drawCards(g);
                }
                printRevealText(g);
                printBets(g);
                break;
            case 1:
                break;
            case 0:
                printText(g);
                printBets(g);
                break;
        }

        ArrayList<Card> cards = backend.getMiddleCards();

        if (cards != null) {
            for (int i = 0; i < cards.size(); i++) {
                cards.get(i).draw(g, 340 + (100 * i), 300, this);
            }
        }
    }

    public void printText(Graphics g) {
        Font smallFont = new Font("arial", Font.PLAIN, 30);
        g.setColor(Color.white);
        g.setFont(smallFont);
        g.drawString("Current Pot: $" + backend.getPot(), 500, 280);
        g.drawString("Current Bet: $" + backend.getBet(), 500, 460);
    }

    public void printRevealText(Graphics g) {
        Font smallFont = new Font("arial", Font.PLAIN, 30);
        g.setColor(Color.white);
        g.setFont(smallFont);
        g.drawString("Final Pot: $" + backend.getPot(), 500, 280);
        g.drawString(winner.getName() + " won with a " + backend.ranks[winner.getRank() / 100], 420, 460);
    }

    public void printBets(Graphics g) {
        Font smallFont = new Font("arial", Font.PLAIN, 30);
        g.setColor(Color.white);
        g.setFont(smallFont);

        for (int i = 0; i < players.size(); i++) {
            if (i < 3) {
                g.drawString("$" + players.get(i).getBet(), 150 + (420 * i), 560);
            } else {
                g.drawString("$" + players.get(i).getBet(), 150 + (420 * (i-3)), 220);
            }
        }


    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
}
