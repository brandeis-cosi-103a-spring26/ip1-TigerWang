package edu.brandeis.cosi103a.ip2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Handles game turns for Automation: The Game.
 * Each turn consists of a Buy phase and a Cleanup phase.
 */
public class Play {
    
    /**
     * Executes a complete turn for the given player.
     * @param player the player taking their turn
     * @param supply the available cards that can be purchased
     * @return true if the game should continue, false if the game has ended
     */
    public static boolean takeTurn(Player player, List<Card> supply) {
        System.out.println("\n" + player.getName() + "'s turn:");
        
        // Buy phase
        buyPhase(player, supply);
        
        // Check if game has ended (all Framework cards purchased)
        if (isGameOver(supply)) {
            return false;
        }
        
        // Cleanup phase
        cleanupPhase(player);
        
        return true;
    }
    
    /**
     * Checks if the game has ended.
     * Game ends when all Framework cards have been purchased from the supply.
     * 
     * @param supply the available cards in the supply
     * @return true if all Framework cards are gone, false otherwise
     */
    private static boolean isGameOver(List<Card> supply) {
        for (Card card : supply) {
            if (card.getName().equals("Framework")) {
                return false; // At least one Framework card remains
            }
        }
        return true; // No Framework cards left in supply
    }
    
    /**
     * Determines the winner by counting Automation Points in each player's deck.
     * Automation Points = sum of all card values in the player's deck.
     * 
     * @param players array of all players in the game
     * @return the winning player
     */
    public static Player determineWinner(Player[] players) {
        System.out.println("\n=== GAME OVER ===");
        System.out.println("All Framework cards have been purchased!");
        System.out.println("\nCalculating Automation Points...\n");
        
        Player winner = null;
        int maxPoints = -1;
        
        for (Player player : players) {
            int automationPoints = calculateAutomationPoints(player);
            player.setScore(automationPoints);
            
            System.out.println(player.getName() + ": " + automationPoints + " Automation Points");
            
            if (automationPoints > maxPoints) {
                maxPoints = automationPoints;
                winner = player;
            }
        }
        
        System.out.println("\n=== WINNER: " + winner.getName() + " with " + maxPoints + " Automation Points! ===");
        return winner;
    }
    
    /**
     * Calculates total Automation Points for a player.
     * Automation Points = sum of all card values in hand, draw pile, and discard pile.
     * 
     * @param player the player to calculate points for
     * @return total Automation Points
     */
    private static int calculateAutomationPoints(Player player) {
        int total = 0;
        
        // Count points from hand
        for (Card card : player.getHand()) {
            total += card.getValue();
        }
        
        // Count points from draw pile
        for (Card card : player.getDrawPile()) {
            total += card.getValue();
        }
        
        // Count points from discard pile
        for (Card card : player.getDiscardPile()) {
            total += card.getValue();
        }
        
        return total;
    }
    
    /**
     * Buy phase: player plays cryptocoins from their hand and can buy up to 1 card.
     * Bought cards go directly into the player's discard pile.
     * 
     * @param player the player in their buy phase
     * @param supply the available cards that can be purchased
     */
    private static void buyPhase(Player player, List<Card> supply) {
        System.out.println("--- Buy Phase ---");
        
        // Calculate total gold from crypto cards in hand
        int totalGold = 0;
        List<Card> playedCards = new ArrayList<>();
        
        for (Card card : player.getHand()) {
            if (card.getType().equals("Crypto")) {
                totalGold += card.getValue();
                playedCards.add(card);
            }
        }
        
        System.out.println("Played crypto cards for " + totalGold + " gold");
        player.setGold(totalGold);
        
        // Find the most expensive card the player can afford
        Card cardToBuy = null;
        int maxCost = 0;
        
        for (Card card : supply) {
            if (card.getCost() <= totalGold && card.getCost() > maxCost) {
                cardToBuy = card;
                maxCost = card.getCost();
            }
        }
        
        // Buy the card if one was found
        if (cardToBuy != null) {
            System.out.println(player.getName() + " buys " + cardToBuy.getName() + " (cost: " + cardToBuy.getCost() + ")");
            
            // Remove card from supply and add to player's discard pile
            supply.remove(cardToBuy);
            player.getDiscardPile().add(cardToBuy);
        } else {
            System.out.println(player.getName() + " cannot afford any cards");
        }
        
        // Reset gold for next turn
        player.setGold(0);
    }
    
    /**
     * Cleanup phase: player discards their hand and all played cards, 
     * then deals a new hand from their deck.
     * 
     * @param player the player in their cleanup phase
     */
    private static void cleanupPhase(Player player) {
        System.out.println("--- Cleanup Phase ---");
        
        // Add current hand to discard pile
        player.getDiscardPile().addAll(player.getHand());
        player.getHand().clear();
        
        System.out.println("Hand discarded. Drawing new hand...");
        
        // Deal new hand
        dealNewHand(player);
        
        System.out.println("New hand size: " + player.getHand().size());
    }
    
    /**
     * Deals a new hand to the player from their draw pile.
     * If draw pile is empty, shuffles discard pile and makes it the new draw pile.
     * 
     * @param player the player to deal cards to
     */
    private static void dealNewHand(Player player) {
        List<Card> newHand = new ArrayList<>();
        int handSize = 5;
        
        for (int i = 0; i < handSize; i++) {
            // If draw pile is empty, shuffle discard pile and make it draw pile
            if (player.getDrawPile().isEmpty()) {
                if (player.getDiscardPile().isEmpty()) {
                    // No more cards available
                    break;
                }
                
                System.out.println("Draw pile empty. Shuffling discard pile...");
                Collections.shuffle(player.getDiscardPile());
                player.setDrawPile(new ArrayList<>(player.getDiscardPile()));
                player.getDiscardPile().clear();
            }
            
            // Deal one card from draw pile to hand
            if (!player.getDrawPile().isEmpty()) {
                newHand.add(player.getDrawPile().remove(0));
            }
        }
        
        player.setHand(newHand);
    }
}