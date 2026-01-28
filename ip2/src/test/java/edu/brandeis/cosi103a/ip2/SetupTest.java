package edu.brandeis.cosi103a.ip1;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

public class SetupTest {

    @Test
    public void testInitializeGame_createsTwoPlayers() {
        Player[] players = Setup.initializeGame();
        assertEquals(2, players.length, "Game should initialize with 2 players");
    }

    @Test
    public void testInitializeGame_playerNamesAreSet() {
        Player[] players = Setup.initializeGame();
        assertNotNull(players[0].getName(), "Player 1 should have a name");
        assertNotNull(players[1].getName(), "Player 2 should have a name");
        assertEquals("Player 1", players[0].getName());
        assertEquals("Player 2", players[1].getName());
    }

    @Test
    public void testInitializeGame_starterDeckSize() {
        Player[] players = Setup.initializeGame();
        
        // Each player should have 10 cards total (7 Bitcoin + 3 Method)
        int totalCards = players[0].getDrawPile().size() + players[0].getHand().size();
        assertEquals(10, totalCards, "Player should have 10 total cards (5 in hand + 5 in draw pile)");
    }

    @Test
    public void testInitializeGame_initialHandSize() {
        Player[] players = Setup.initializeGame();
        
        // Each player should be dealt 5 cards initially
        assertEquals(5, players[0].getHand().size(), "Player 1 should have 5 cards in hand");
        assertEquals(5, players[1].getHand().size(), "Player 2 should have 5 cards in hand");
    }

    @Test
    public void testInitializeGame_drawPileSize() {
        Player[] players = Setup.initializeGame();
        
        // After dealing 5 cards, 5 should remain in draw pile
        assertEquals(5, players[0].getDrawPile().size(), "Player 1 should have 5 cards in draw pile");
        assertEquals(5, players[1].getDrawPile().size(), "Player 2 should have 5 cards in draw pile");
    }

    @Test
    public void testInitializeGame_startingPlayerIsSet() {
        Player[] players = Setup.initializeGame();
        
        boolean player1IsStarting = players[0].isCurrentPlayer();
        boolean player2IsStarting = players[1].isCurrentPlayer();
        
        // Exactly one player should be the starting player
        assertTrue(player1IsStarting || player2IsStarting, "One player should be set as current player");
        assertNotEquals(player1IsStarting, player2IsStarting, "Exactly one player should be starting player");
    }

    @Test
    public void testInitializeGame_cardsAreShuffled() {
        // Run initialization multiple times to ensure shuffling occurs
        Player[] players1 = Setup.initializeGame();
        Player[] players2 = Setup.initializeGame();
        
        // Card order should be different (with very high probability)
        List<Card> hand1 = players1[0].getHand();
        List<Card> hand2 = players2[0].getHand();
        
        assertFalse(hand1.equals(hand2), "Shuffling should produce different hand orders");
    }

    @Test
    public void testInitializeGame_starterDeckComposition() {
        Player[] players = Setup.initializeGame();
        List<Card> allCards = new java.util.ArrayList<>();
        allCards.addAll(players[0].getHand());
        allCards.addAll(players[0].getDrawPile());
        
        long bitcoinCount = allCards.stream().filter(c -> c.getName().equals("Bitcoin")).count();
        long methodCount = allCards.stream().filter(c -> c.getName().equals("Method")).count();
        
        assertEquals(7, bitcoinCount, "Starter deck should have 7 Bitcoins");
        assertEquals(3, methodCount, "Starter deck should have 3 Methods");
    }
}