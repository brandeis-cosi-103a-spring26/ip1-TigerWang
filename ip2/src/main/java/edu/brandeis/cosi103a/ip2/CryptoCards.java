package edu.brandeis.cosi103a.ip2;

/**
 * Factory class to generate Crypto cards for Automation: The Game.
 * Crypto cards represent digital currency and blockchain-themed resources.
 */
public class CryptoCards {

    /**
     * Creates all available Crypto cards in the game deck.
     */
    public static Card[] createDeck() {
        Card[] deck = new Card[130]; // 60 + 40 + 30 = 130 total cards
        int index = 0;

        // Bitcoin: 60 cards, cost 0, value 1
        for (int i = 1; i <= 60; i++) {
            deck[index++] = new Card("bitcoin_" + i, "Bitcoin", "Crypto", 0, 1, "Worth 1 gold");
        }

        // Ethereum: 40 cards, cost 3, value 2
        for (int i = 1; i <= 40; i++) {
            deck[index++] = new Card("ethereum_" + i, "Ethereum", "Crypto", 3, 2, "Worth 2 gold");
        }

        // Dogecoin: 30 cards, cost 6, value 3
        for (int i = 1; i <= 30; i++) {
            deck[index++] = new Card("dogecoin_" + i, "Dogecoin", "Crypto", 6, 3, "Worth 3 gold");
        }

        return deck;
    }

    /**
     * Creates a starter deck for a new player.
     * Consists of 7 Bitcoins and 3 Methods.
     */
    public static Card[] createStarterDeck() {
        Card[] starterDeck = new Card[10]; // 7 + 3 = 10 cards
        int index = 0;

        // 7 Bitcoins
        for (int i = 1; i <= 7; i++) {
            starterDeck[index++] = new Card("bitcoin_starter_" + i, "Bitcoin", "Crypto", 0, 1, "Worth 1 gold");
        }

        // 3 Methods
        for (int i = 1; i <= 3; i++) {
            starterDeck[index++] = new Card("method_starter_" + i, "Method", "Automation", 2, 1, "Automation method");
        }

        return starterDeck;
    }
}