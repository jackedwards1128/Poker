import java.util.ArrayList;
import java.util.Scanner;

public class Game {

    private GameView window;

    private int state = 0;

    private int playerCount;
    private Deck pile;
    private int bet;
    private int pot;
    private ArrayList<Card> middleCards;
    private ArrayList<Player> players;
    private ArrayList<Player> playersCopy;
    public final String[] ranks = new String[] {"High Card", "Pair", "Two Pair", "Three of a Kind",
            "Straight", "Flush", "Full House", "Quads", "Straight Flush", "Royal Flush"};

    public Game() {

        state = 1;

        // Begin game by creating arraylist of players and the deck
        players = new ArrayList<Player>();
        playersCopy = new ArrayList<Player>();
        window = new GameView(this);
        Deck deck = new Deck(window);
        deck.shuffle();
        this.pile = deck;


        middleCards = new ArrayList<Card>();


        this.beginGame();
    }

    private void beginGame() {
        // Print instructions to the player
        printInstructions();

        // Determine player count
        Scanner input = new Scanner(System.in);
        System.out.println("How many players will be participating ? ");
        playerCount = input.nextInt();

        // Gather names of each player, add them to players arraylist, and give them a pocket
        for (int i = 0; i < playerCount; i++) {
            System.out.println("What is player " + (i+1) + "'s name? ");

            // fixes an input bug
            if (i == 0) {
                input.nextLine();
            }
            String playerName = input.nextLine();

            players.add(new Player(playerName, window, i));

            ArrayList<Card> pocket = new ArrayList<Card>();

            // pull two cards out of the pile and put them into a player's pocket
            pocket.add(pile.remove(0));
            pocket.add(pile.remove(0));

            players.get(i).createHand(pocket);
            playersCopy.add(players.get(i));
        }
        state = 0;
        window.repaint();

        // Display hands to players privately
        for(Player player : players) {
            player.setCardsVisibility(false);
        }
        window.repaint();
        showHands();
        gatherBets(0);

        // Flop
        flop();
        gatherBets(0);

        // Turn
        revealCard();
        showCards();
        gatherBets(0);

        // River
        revealCard();
        showCards();
        gatherBets(0);

        // Grand reveal
        grandReveal();
    }

    // Privately show pockets to all players
    private void showHands() {
        for (int i = 0; i < playerCount; i++) {
            window.setNextPlayer(players.get(i).getName());
            state = 3;
            window.repaint();
            Scanner input = new Scanner(System.in);

            // Warn player
            System.out.println(players.get(i).getName() + ", click enter once you are ready to see your hand");
            input.nextLine();

            players.get(i).setCardsVisibility(true);
            state = 0;
            window.repaint();

            System.out.print(players.get(i).getName() + " has a ");
            System.out.println(players.get(i).getHandString());
            System.out.println("click enter once you done seeing your hand");

            // When enter is pressed it pushes the pocket out of view of the next player
            input.nextLine();
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            players.get(i).setCardsVisibility(false);
            window.repaint();
        }

        window.setNextPlayer(players.get(0).getName());
        state = 3;
        window.repaint();
        Scanner input = new Scanner(System.in);

        // Warn player
        System.out.println("click enter once you are ready to start betting");
        input.nextLine();

        players.get(0).setCardsVisibility(true);
        state = 0;
        window.repaint();
    }

    // Run flop by revealing the middle three cards and displaying them
    private void flop() {
        for(int i = 0; i < 3; i++) {
            revealCard();
        }
        showCards();
    }

    // Display cards to all players
    private void showCards() {
        System.out.print("//////// MIDDLE CARDS: ");

        for(int i = 0; i < middleCards.size(); i++) {
            System.out.print(middleCards.get(i).toString());

            // Makes sure the print doesn't end with a comma
            if (i != middleCards.size() - 1) {
                System.out.print(", ");
            }

        }

        window.repaint();

        System.out.println(" ////////");
    }

    // Add a card from the deck to the middle cards
    private void revealCard() {
        middleCards.add(pile.remove(0));
    }

    // Ask each player their desired move, and discern bets accordingly
    private void gatherBets(int startingSpot) {
        for(int i = startingSpot; i < playerCount; i++) {
            // If only one person is left, skip the betting
            if (playerCount == 1) {
                break;
            }
            char choice = '0';

            players.get(i).setCardsVisibility(true);
            window.repaint();


            // Gather player's valid choice
            while (choice != 'c' && choice != 'r' && choice != 'f') {
                System.out.println(players.get(i).getName() + ", do you want to (f)old, (c)heck/call, or (r)aise?");
                Scanner input = new Scanner(System.in);
                choice = input.nextLine().charAt(0);
            }

            // Fold / Check or Call / Raise
            switch (choice) {
                case 'f':
                    // Remove player from players arraylist
                    System.out.println(players.get(i).getName() + " folded out of the round");
                    players.remove(i);
                    playerCount--;
                    i--;
                    break;
                case 'c':
                    // Set bet of player to overall bet
                    pot += bet - players.get(i).getBet();
                    players.get(i).setBet(bet);
                    playersCopy.get(i).setBet(bet);
                    System.out.println(players.get(i).getName() + " matched to a bet of $" + bet);
                    players.get(i).setCardsVisibility(false);
                    window.repaint();
                    break;
                case 'r':
                    // Wait for user to enter a bet higher than current bet
                    int newBet = -1;

                    while (newBet <= bet) {
                        System.out.println("what would you like to raise the bet to? Must be greater than the current bet. ");
                        Scanner input = new Scanner(System.in);
                        newBet = input.nextInt();
                    }

                    // Set the overall bet to the raised value
                    bet = newBet;
                    pot += bet - players.get(i).getBet();
                    players.get(i).setBet(bet);
                    playersCopy.get(i).setBet(bet);
                    System.out.println(players.get(i).getName() + " raise the a bet to $" + bet);
                    players.get(i).setCardsVisibility(false);
                    window.repaint();
                    break;
            }

            System.out.println("press enter to move on");
            state = 2;
            window.repaint();
            Scanner input = new Scanner(System.in);
            input.nextLine();
            state = 0;
            window.repaint();
        }
        // Make sure all players have an even bet. If not, recursively recall the function

        for(int k = 0; k < playerCount; k++) {
            if(players.get(k).getBet() != bet) {
                gatherBets(k);
            }
        }
    }

    // Reveal the ranks of the players and their representative bets
    private void grandReveal() {
        showCards();
        for (int i = 0; i < playersCopy.size(); i++) {
            System.out.print(playersCopy.get(i).getName() + " had a ");
            System.out.println(playersCopy.get(i).getHandString());
            playersCopy.get(i).determineRank(middleCards);
            System.out.println("They got a " + ranks[playersCopy.get(i).getRank() / 100]);
            System.out.println("They bet $" + playersCopy.get(i).getBet());
            System.out.println(playersCopy.get(i).getRank());

        }

        int bestPlayer = 0;
        for (int i = 1; i < players.size(); i++) {
            if (players.get(i).getRank() > players.get(bestPlayer).getRank()) {
                bestPlayer = i;
            }
        }
        window.setWinner(players.get(bestPlayer));
        state = 4;
        window.repaint();
    }

    // Print the instructions of poker
    private static void printInstructions() {
        System.out.println("\n\nWELCOME TO POKER");
        System.out.println("After inputting the number of players and your names, you will each be able to " +
                " privately view your hand. After viewing your pocket, ");
        System.out.println("you will each bet on your hand. You can either fold," +
                " check/call, or raise. Then 3 cards in the middle will be revealed, then 1 more");
        System.out.println(", then 1 more again, for a total of 5 cards. Each time new cards are revealed, you will each" +
                " bet on your hands again. Your goal is to");
        System.out.println("make the best possible 5-card hand from the 5 cards" +
                " in the middle, and the 2 cards you have in your hand. Aces are always valued as 1.");
        System.out.println("The ranks go as following:");
        System.out.println("<<Worst>>");
        System.out.println("High Card");
        System.out.println("Pair");
        System.out.println("Two Pair");
        System.out.println("Three of a kind");
        System.out.println("Straight (all numbers are in an ascending order)");
        System.out.println("Flush (all the same suit)");
        System.out.println("Full House (three of a kind and a pair)");
        System.out.println("Quads (four of a kind)");
        System.out.println("Straight Flush (flush + a straight)");
        System.out.println("Royal Flush (flush + a straight that ends with a king])");
        System.out.println("<<Best>>\n\n");
    }

    // Getter
    public ArrayList<Card> getMiddleCards() {
        return middleCards;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getState() {
        return state;
    }

    public int getPot() {
        return pot;
    }

    public int getBet() {
        return bet;
    }

    public ArrayList<Player> getPlayersCopy() {
        return playersCopy;
    }

    public static void main(String[] args) {
        Game poker = new Game();
    }
}
