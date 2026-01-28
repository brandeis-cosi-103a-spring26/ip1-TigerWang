package edu.brandeis.cosi103a.ip2;

import java.util.ArrayList;
import java.util.List;

/**
 * Main game class for Automation: The Game.
 * Runs a simulated game between 2 automated players.
 */
public class Game {
    
    public static void main(String[] args) {
        System.out.println("=== AUTOMATION: THE GAME ===");
        System.out.println("Starting new game...\n");
        
        // Initialize game and players
        Player[] players = Setup.initializeGame();
        
        // Create supply of cards that can be purchased
        List<Card> supply = createSupply();
        
        System.out.println("\nSupply contains:");
        System.out.println("- 60 Bitcoin (cost: 0, value: 1)");
        System.out.println("- 40 Ethereum (cost: 3, value: 2)");
        System.out.println("- 30 Dogecoin (cost: 6, value: 3)");
        System.out.println("- 14 Method (cost: 2, value: 1)");
        System.out.println("- 8 Module (cost: 5, value: 3)");
        System.out.println("- 8 Framework (cost: 8, value: 6)");
        System.out.println("\nGame ends when all Framework cards are purchased.\n");
        
        // Game loop
        int turnCount = 0;
        boolean gameActive = true;
        
        while (gameActive) {
            turnCount++;
            System.out.println("\n========== TURN " + turnCount + " ==========");
            
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
                    switchPlayer(players);
                }
            }
            
            // Safety limit to prevent infinite loops during testing
            if (turnCount > 1000) {
                System.out.println("\nGame ended after 1000 turns (safety limit)");
                Play.determineWinner(players);
                break;
            }
        }
        
        System.out.println("\nGame completed in " + turnCount + " turns.");
    }
    
    /**
     * Creates the supply of cards available for purchase.
     * @return list of all cards in the supply
     */
    public static List<Card> createSupply() {
        List<Card> supply = new ArrayList<>();
        
        // Add Crypto cards
        Card[] cryptoCards = CryptoCards.createDeck();
        for (Card card : cryptoCards) {
            supply.add(card);
        }
        
        // Add Automation cards
        Card[] automationCards = AutomationCards.createDeck();
        for (Card card : automationCards) {
            supply.add(card);
        }
        
        return supply;
    }
    
    /**
     * Switches the current player to the next player.
     * @param players array of all players
     */
    public static void switchPlayer(Player[] players) {
        for (int i = 0; i < players.length; i++) {
            if (players[i].isCurrentPlayer()) {
                players[i].setIsCurrentPlayer(false);
                players[(i + 1) % players.length].setIsCurrentPlayer(true);
                break;
            }
        }
    }
}