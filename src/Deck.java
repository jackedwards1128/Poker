import java.util.ArrayList;

public class Deck {
    // Define instance variables
    // Deck holds the shuffled deck that cards are drawn out of
    private ArrayList<Card> deck;
    private GameView window;
    private static final int size = 52;
    private final String[] suits = new String[] {"Spades", "Hearts", "Diamonds", "Clubs"};
    private final String[] ranks = new String[] {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven",
            "Eight", "Nine", "Ten", "Jack", "Queen", "King"};

    public Deck(GameView window) {
        // Add the non-shuffled cards to deck
        deck = new ArrayList<Card>();
        this.window = window;
        for(int i = 0; i <= 12; i++) {
            for (int j = 0; j < 4; j++) {
                int CardNum = (i*4) + (j + 1);
                if (i == 0) {
                    // Aces have a ranking higher than the other cards so set the value = 13
                    deck.add(new Card(ranks[i], suits[j], 13, "" + CardNum, window));
                } else {
                    deck.add(new Card(ranks[i], suits[j], i, "" + CardNum, window));
                }
            }
        }
    }

    public void add(int index, Card nCard) {
        // add a card to deck (used for debugging)
        deck.add(index, nCard);
    }


    // Getters
    public int getSize() {
        return size;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public Card getRandomCard() {
        return deck.get((int)(Math.random()*52));
    }

    // Shuffle deck
    public void shuffle() {
        for (int i = 0; i < 52; i++) {
            // Go through each card and swap it with a random index
            Card temp = deck.get(i); // c = a
            int swap = (int)(Math.random() * 52); // b

            deck.set(i, deck.get(swap)); // a = b
            deck.set(swap, temp); // b = c
        }
    }

    // Remove given index
    public Card remove(int index) {
        return deck.remove(index);
    }
}


