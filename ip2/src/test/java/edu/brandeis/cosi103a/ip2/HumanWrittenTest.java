package edu.brandeis.cosi103a.ip2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;


public class HumanWrittenTest {

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

    
}
