package edu.brandeis.cosi103a.ip2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Setup class for initializing the Automation: The Game.
 * Handles player creation, deck initialization, and game start.
 */
public class Setup {
    private static final int STARTER_DECK_SIZE = 10;
    private static final int INITIAL_HAND_SIZE = 5;
    private static final int NUM_PLAYERS = 2;

    public static Player[] initializeGame() {
        Player[] players = new Player[NUM_PLAYERS];

        for (int i = 0; i < NUM_PLAYERS; i++) {
            players[i] = new Player("Player " + (i + 1));
            
            List<Card> starterDeck = createStarterDeck();
            Collections.shuffle(starterDeck);
            players[i].setDrawPile(starterDeck);
            players[i].setDiscardPile(new ArrayList<>());
            dealHand(players[i]);
        }

        int startingPlayerIndex = new Random().nextInt(NUM_PLAYERS);
        players[startingPlayerIndex].setIsCurrentPlayer(true);

        return players;
    }

    private static List<Card> createStarterDeck() {
        List<Card> starterDeck = new ArrayList<>();

        for (int i = 1; i <= 7; i++) {
            starterDeck.add(new Card("bitcoin_starter_" + i, "Bitcoin", "Crypto", 0, 1, "Worth 1 gold"));
        }

        for (int i = 1; i <= 3; i++) {
            starterDeck.add(new Card("method_starter_" + i, "Method", "Automation", 2, 1, "Automation method"));
        }

        return starterDeck;
    }

    /**
     * Deals cards from the player's draw pile to their hand.
     * If draw pile is empty, shuffles the discard pile and makes it the new draw pile.
     * 
     * @param player the player to deal cards to
     */
    public static void dealHand(Player player) {
        List<Card> hand = new ArrayList<>();

        for (int i = 0; i < INITIAL_HAND_SIZE; i++) {
            // If draw pile is empty, shuffle discard pile and make it draw pile
            if (player.getDrawPile().isEmpty()) {
                List<Card> discardPile = player.getDiscardPile();
                if (discardPile.isEmpty()) {
                    break; // No more cards available
                }
                Collections.shuffle(discardPile);
                player.setDrawPile(new ArrayList<>(discardPile));
                player.setDiscardPile(new ArrayList<>());
            }

            // Deal one card from draw pile to hand
            if (!player.getDrawPile().isEmpty()) {
                hand.add(player.getDrawPile().remove(0));
            }
        }

        player.setHand(hand);
    }
}
