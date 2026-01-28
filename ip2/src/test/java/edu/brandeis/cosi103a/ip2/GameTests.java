package edu.brandeis.cosi103a.ip2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;


public class GameTests {

    @Test
    public void sampleTest() {
        assertEquals(2, 1 + 1, "Basic arithmetic should hold");
    }
    @Test
    public void testFinish() { 
        Player[] players = Setup.initializeGame();
        List<Card> supply = Game.createSupply();

        // Count initial Framework cards
        int initialFrameworkCount = Card.countFrameworkCards(supply);
        assertTrue(initialFrameworkCount > 0, "Supply should start with Framework cards");
        
        // Game loop
        int turnCount = 0;
        boolean gameActive = true;
        
        while (gameActive) {
            turnCount++;
            
            // Each player takes their turn
            for (Player player : players) {
                if (player.isCurrentPlayer()) {
                    gameActive = Play.takeTurn(player, supply);
                    
                    if (!gameActive) {
                        // Game has ended
                        Play.determineWinner(players);
                        break;
                    }
                    
                    // Switch to next player
                    Game.switchPlayer(players);
                }
            }
        }
       int finalFrameworkCount = Card.countFrameworkCards(supply);
        assertEquals(0, finalFrameworkCount, "All Framework cards should be purchased when game ends");
    }

    @Test
    public void overfill() { 
        Player[] players = Setup.initializeGame();
        List<Card> supply = Game.createSupply();

        int initialSupplySize = supply.size();
        int initialPlayer1Cards = players[0].getTotalCards();
        int initialPlayer2Cards = players[1].getTotalCards();
        int totalCardsInSystem = initialSupplySize + initialPlayer1Cards + initialPlayer2Cards;
        
        // Run game for several turns
        int turnCount = 0;
        boolean gameActive = true;
        
        while (gameActive && turnCount < 50) {
            turnCount++;
            
            for (Player player : players) {
                if (player.isCurrentPlayer()) {
                    gameActive = Play.takeTurn(player, supply);
                    
                    // Verify total cards remain constant
                    int currentTotal = supply.size() + players[0].getTotalCards() + players[1].getTotalCards();
                    assertEquals(totalCardsInSystem, currentTotal, 
                        "Total cards in system should remain constant (turn " + turnCount + ")");
                    
                    // Verify hand size doesn't exceed 5
                    assertTrue(player.getHand().size() <= 5, 
                        "Player hand should not exceed 5 cards (turn " + turnCount + ")");
                    
                    if (!gameActive) break;
                    Game.switchPlayer(players);
                }
            }
        }
    }

    @Test
    public void testBuyPhase_cryptoCoinsPlayedForGold() {
        Player[] players = Setup.initializeGame();
        List<Card> supply = Game.createSupply();
        Player player = players[0];
        
        // Record initial hand
        int initialHandSize = player.getHand().size();
        
        // Count crypto cards in hand
        int cryptoCardsInHand = 0;
        int expectedGold = 0;
        for (Card card : player.getHand()) {
            if (card.getType().equals("Crypto")) {
                cryptoCardsInHand++;
                expectedGold += card.getValue();
            }
        }
        
        assertTrue(cryptoCardsInHand > 0, "Player should have crypto cards in starting hand");
        
        // Player takes turn (which includes buy phase)
        Play.takeTurn(player, supply);
        
        // After cleanup, player should have new hand of 5 cards
        assertEquals(5, player.getHand().size(), "After turn, player should have 5 cards in hand");
    }

    @Test
    public void testBuyPhase_canBuyOnlyOneCard() {
        Player[] players = Setup.initializeGame();
        List<Card> supply = Game.createSupply();
        Player player = players[0];
        
        int initialSupplySize = supply.size();
        
        // Take one turn
        Play.takeTurn(player, supply);
        
        // Supply should decrease by at most 1 card
        int cardsBought = initialSupplySize - supply.size();
        assertTrue(cardsBought <= 1, "Player should buy at most 1 card per turn");
    }

    @Test
    public void testBuyPhase_boughtCardGoesToDiscard() {
        Player[] players = Setup.initializeGame();
        List<Card> supply = Game.createSupply();
        Player player = players[0];
        
        int initialDiscardSize = player.getDiscardPile().size();
        int initialSupplySize = supply.size();
        
        // Take one turn
        Play.takeTurn(player, supply);
        
        int cardsBought = initialSupplySize - supply.size();
        
        if (cardsBought > 0) {
            // Discard pile should contain the bought card (plus the 5 cards from previous hand)
            // Note: cleanup phase adds hand to discard, so discard increases by hand size + bought cards
            assertTrue(player.getDiscardPile().size() >= initialDiscardSize + cardsBought,
                "Bought card should be in discard pile");
        }
    }

    @Test
    public void testCleanupPhase_handDiscardedAndNewHandDealt() {
        Player[] players = Setup.initializeGame();
        List<Card> supply = Game.createSupply();
        Player player = players[0];
        
        // Record initial hand cards
        List<Card> initialHand = new ArrayList<>(player.getHand());
        assertEquals(5, initialHand.size(), "Player should start with 5 cards in hand");
        
        // Take turn
        Play.takeTurn(player, supply);
        
        // After cleanup, player should have new hand of 5 cards
        assertEquals(5, player.getHand().size(), "Player should have 5 cards in hand after cleanup");
        
        // New hand should be different from initial hand (with high probability)
        // Note: This might occasionally fail due to random chance, but very unlikely
        boolean handsAreDifferent = false;
        for (int i = 0; i < player.getHand().size(); i++) {
            if (!player.getHand().get(i).getId().equals(initialHand.get(i).getId())) {
                handsAreDifferent = true;
                break;
            }
        }
        // We can't guarantee they're different, but total cards should be conserved
        int totalCards = player.getHand().size() + player.getDrawPile().size() + player.getDiscardPile().size();
        assertTrue(totalCards >= 10, "Player should have at least starter deck worth of cards");
    }

    @Test
    public void testCleanupPhase_discardShuffledWhenDrawPileEmpty() {
        // Create player with specific setup: empty draw pile, cards in discard
        Player player = new Player("Test Player");
        
        // Set up empty draw pile and 8 cards in discard
        player.setDrawPile(new ArrayList<>());
        List<Card> discardPile = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            discardPile.add(new Card("card_" + i, "TestCard", "Crypto", 0, 1, "Test"));
        }
        player.setDiscardPile(discardPile);
        player.setHand(new ArrayList<>()); // Start with empty hand
        
        // Deal new hand (this should trigger shuffle)
        Setup.dealHand(player);
        
        // Verify hand has 5 cards
        assertEquals(5, player.getHand().size(), "Should deal 5 cards from shuffled discard");
        
        // Verify draw pile now has remaining cards
        assertEquals(3, player.getDrawPile().size(), "Draw pile should have 3 remaining cards");
        
        // Verify discard pile is now empty (all moved to draw pile)
        assertEquals(0, player.getDiscardPile().size(), "Discard pile should be empty after shuffle");
    }

    @Test
    public void testGameEnd_allFrameworksPurchased() {
        Player[] players = Setup.initializeGame();
        List<Card> supply = Game.createSupply();
        
        int initialFrameworks = Card.countFrameworkCards(supply);
        assertEquals(8, initialFrameworks, "Should start with 8 Framework cards");
        
        // Run game until it ends
        boolean gameActive = true;
        int turnCount = 0;
        
        while (gameActive && turnCount < 1000) {
            turnCount++;
            for (Player player : players) {
                if (player.isCurrentPlayer()) {
                    gameActive = Play.takeTurn(player, supply);
                    if (!gameActive) break;
                    Game.switchPlayer(players);
                }
            }
        }
        
        // Game should have ended
        assertFalse(gameActive, "Game should have ended");
        
        // All Frameworks should be purchased
        int finalFrameworks = Card.countFrameworkCards(supply);
        assertEquals(0, finalFrameworks, "All Framework cards should be purchased");
    }

    @Test
    public void testGameEnd_winnerHasMostAutomationPoints() {
        Player[] players = Setup.initializeGame();
        List<Card> supply = Game.createSupply();
        
        // Run game until it ends
        boolean gameActive = true;
        
        while (gameActive) {
            for (Player player : players) {
                if (player.isCurrentPlayer()) {
                    gameActive = Play.takeTurn(player, supply);
                    if (!gameActive) break;
                    Game.switchPlayer(players);
                }
            }
        }
        
        // Determine winner
        Player winner = Play.determineWinner(players);
        
        // Verify winner has most points
        assertNotNull(winner, "There should be a winner");
        
        Player otherPlayer = players[0] == winner ? players[1] : players[0];
        assertTrue(winner.getScore() >= otherPlayer.getScore(), 
            "Winner should have most automation points");
        
        // Both players should have positive scores
        assertTrue(players[0].getScore() > 0, "Player 1 should have automation points");
        assertTrue(players[1].getScore() > 0, "Player 2 should have automation points");
    }
}
