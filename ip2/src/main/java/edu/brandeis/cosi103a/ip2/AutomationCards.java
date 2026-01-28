package edu.brandeis.cosi103a.ip2;

/**
 * Factory class to generate Automation cards for Automation: The Game.
 * These are action/method cards specific to the Automation game theme.
 */
public class AutomationCards {

    /**
     * Creates all available Automation cards in the game deck.
     */
    public static Card[] createDeck() {
        Card[] deck = new Card[30]; // 14 + 8 + 8 = 30 total cards
        int index = 0;

        // Method: 14 cards, cost 2, value 1
        for (int i = 1; i <= 14; i++) {
            deck[index++] = new Card("method_" + i, "Method", "Automation", 2, 1, "Automation method");
        }

        // Module: 8 cards, cost 5, value 3
        for (int i = 1; i <= 8; i++) {
            deck[index++] = new Card("module_" + i, "Module", "Automation", 5, 3, "Automation module");
        }

        // Framework: 8 cards, cost 8, value 6
        for (int i = 1; i <= 8; i++) {
            deck[index++] = new Card("framework_" + i, "Framework", "Automation", 8, 6, "Automation framework");
        }

        return deck;
    }

    /**
     * Creates a starter Automation deck for a new player.
     */
    public static Card[] createStarterDeck() {
        return new Card[] {
            new Card("method_start1", "Method", "Automation", 2, 1, "Automation method"),
            new Card("method_start2", "Method", "Automation", 2, 1, "Automation method"),
            new Card("module_start", "Module", "Automation", 5, 3, "Automation module"),
            new Card("framework_start", "Framework", "Automation", 8, 6, "Automation framework")
        };
    }
}