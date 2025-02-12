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
        g.setColor(new Color(9, 74, 34));
        g.fillRect(0,0,WINDOW_WIDTH, WINDOW_HEIGHT);

        for (Player player : players) {
            player.drawCards(g);
        }

        ArrayList<Card> cards = backend.getMiddleCards();

        if (backend.getState() == 4) {
            ArrayList<Player> playersCopy = backend.getPlayersCopy();
            for (Player player : playersCopy) {
                player.setCardsVisibility(true);
                player.drawCards(g);
            }
        }

        printText(g);

        if (cards != null) {
            for (int i = 0; i < cards.size(); i++) {
                cards.get(i).draw(g, 250 + (100 * i), 250, this);
            }
        }
    }

    public void printText(Graphics g) {
        Font smallFont = new Font("arial", Font.PLAIN, 30);
        g.setColor(Color.white);
        g.setFont(smallFont);
        g.drawString("Current Pot: " + backend.getPot(), 400, 220);
    }
}
