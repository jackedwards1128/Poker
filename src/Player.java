import java.awt.*;
import java.util.ArrayList;

public class Player {
    // Define instance variables
    private ArrayList<Card> hand;
    private String name;
    private GameView window;
    private int bet;
    private int rank;
    private int order;

    public Player(String name, GameView window, int order) {
        hand = new ArrayList<Card>();
        this.name = name;
        this.order = order;
        this.window = window;
    }

    // Give player pocket
    public void createHand(ArrayList<Card> pocket) {
        hand = pocket;
    }

    // Getters and Setters
    public String getHandString() {
        return hand.get(0).toString() + " and a " + hand.get(1).toString();
    }
    public int getBet() {
        return bet;
    }
    public void setBet(int bet) {
        this.bet = bet;
    }
    public String getName() {
        return name;
    }
    public int getRank() {
        return rank;
    }

    // Sets the player's rank variable to the highest possible rank given their pocket and middle cards
    public void determineRank(ArrayList<Card> middleCards) {
        // Create a copy of the full hand which will then be reset after use
        ArrayList<Card> fullHand = new ArrayList<Card>();
        fullHand.addAll(middleCards);
        fullHand.addAll(hand);
        ArrayList<Card> tempFullHand = new ArrayList<Card>();
        tempFullHand.addAll(fullHand);

        // Since seven choose five is the same as seven choose two, choose two indexes to skip when selecting cards
        // and cycle through all possible choices for these skips
        rank = -1;
        for (int skipOne = 0; skipOne < 7; skipOne++) {
            for (int skipTwo = 0; skipTwo < 7; skipTwo++) {
                // If the two skips are the same, jump over that case
                if (skipOne == skipTwo) {
                    continue;
                }
                ArrayList<Card> supposedHand = new ArrayList<Card>();

                supposedHand.addAll(fullHand);

                // Set the cards to skip as dud-cards with a value of -1
                supposedHand.set(skipOne, new Card("Ace", "Spades", -1, "back", window));
                supposedHand.set(skipTwo, new Card("Ace", "Spades", -1, "back", window));

                // Remove these dud cards. These processes are done seperately to avoid the complexity
                // of the size of the array changing after the skip card is remove
                for (int i = 0; i < supposedHand.size(); i++) {
                    if (supposedHand.get(i).getValue() == -1) {
                        supposedHand.remove(i--);
                    }
                }

                // If the given hand is better than previous hands, set the rank to the new rank
                rank = Math.max(judgeHand(supposedHand), rank);

            }



        }


    }

    // Judge a given five card hand and return an int corresponding to the value of that hand
    private int judgeHand(ArrayList<Card> givenHand) {
        /// Values: ///
        // 9 royal flush
        // 8 straight flush
        // 7 quads
        // 6 full house
        // 5 flush
        // 4 straight
        // 3 trips
        // 2 two pair
        // 1 pair
        // 0 high

        // Determine the frequency at which each number/face card appears
        int[] frequency = new int[13];
        boolean straight = false;
        boolean flush = false;

        for (Card Card : givenHand) {
            frequency[Card.getValue()]++;
        }


        // Determine presence of straight
        int straightCounter = 0;
        for (int i = 0; i < 13; i++) {
            if(frequency[i] > 0) {
                straightCounter++;
            } else {
                straightCounter = 0;
            }

            // If five cards in an ascending row are present, straight present
            if(straightCounter == 5) {
                straight = true;
                break;
            }
        }

        // Determine presence of flush
        flush = true;
        for (int i = 1; i < 5; i++) {
            // Ff the suit of the given card is not the same as the suit of the first card then no flush
            if (!givenHand.get(i).getSuit().equals(givenHand.get(0).getSuit())) {
                flush = false;
                break;
            }
        }


        // ROYAL FLUSH & STRAIGHT FLUSH
        if (flush && straight) {
            boolean royal = false;
            for (Card Card : givenHand) {
                if (Card.getValue() == 12) {
                    royal = true;
                    break;
                }
            }
            if (royal) {
                return 9;
            } else {
                return 8;
            }
        }

        // Flush and straight. These return statements must be done after the royal and straight flush to make
        // sure the best rank possible is submitted first
        if (flush) {
            return 5;
        }
        if (straight) {
            return 4;
        }

        // QUADS, TRIPS, DUBS, 2DUBS, FULL HOUSE
        int trips = 0;
        int pairs = 0;
        for (int freq : frequency) {
            switch (freq){
                case 4:
                    return 7;
                case 3:
                    trips++;
                    break;
                case 2:
                    pairs++;
            }
        }
        if (trips == 1 && pairs == 1) {
            return 6;
        }
        else if (trips == 1) {
            return 3;
        }
        else if (pairs == 2) {
            return 2;
        }
        else if (pairs == 1) {
            return 1;
        }

        return 0;

    }

    public void setCardsVisibility(boolean visible) {
        for (int i = 0; i < hand.size(); i++) {
            hand.get(i).setShown(visible);
        }
    }

    public void drawCards(Graphics g) {
        if (order < 3) {
            for (int i = 0; i < hand.size(); i++) {
                hand.get(i).draw(g, 100 + (60*i) + (315 * order), 450, window);
            }
        } else {
            for (int i = 0; i < hand.size(); i++) {
                hand.get(i).draw(g, 100 + (60*i) + (315 * (order-3)), 60, window);
            }
        }

    }
}













