package edu.brandeis.cosi103a.ip2;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in Automation: The Game.
 * Manages the player's hand, draw pile, discard pile, and game state.
 */
public class Player {
    private String name;
    private List<Card> hand;
    private List<Card> drawPile;
    private List<Card> discardPile;
    private boolean isCurrentPlayer;
    private int gold; // Gold accumulated in current turn
    private int score; // Victory points

    /**
     * Creates a new player with the given name.
     * @param name the player's name
     */
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.drawPile = new ArrayList<>();
        this.discardPile = new ArrayList<>();
        this.isCurrentPlayer = false;
        this.gold = 0;
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public List<Card> getDrawPile() {
        return drawPile;
    }

    public void setDrawPile(List<Card> drawPile) {
        this.drawPile = drawPile;
    }

    public List<Card> getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(List<Card> discardPile) {
        this.discardPile = discardPile;
    }

    public boolean isCurrentPlayer() {
        return isCurrentPlayer;
    }

    public void setIsCurrentPlayer(boolean currentPlayer) {
        isCurrentPlayer = currentPlayer;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void addGold(int amount) {
        this.gold += amount;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int amount) {
        this.score += amount;
    }

    /**
     * Returns the total number of cards the player has.
     */
    public int getTotalCards() {
        return hand.size() + drawPile.size() + discardPile.size();
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", hand=" + hand.size() +
                ", drawPile=" + drawPile.size() +
                ", discardPile=" + discardPile.size() +
                ", gold=" + gold +
                ", score=" + score +
                "}";
    }
}