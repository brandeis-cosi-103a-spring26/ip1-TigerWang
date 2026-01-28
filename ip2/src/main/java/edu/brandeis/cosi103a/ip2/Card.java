package edu.brandeis.cosi103a.ip2;
import java.util.List;
import java.util.Objects;

/**
 * Simple immutable Card model suitable for board/party games.
 * Adjust fields (cost, power, type, etc.) to match your game's needs.
 */
public final class Card {
        private final String id;           // unique identifier
        private final String name;         // display name
        private final String type;         // arbitrary type/category
        private final int cost;            // cost/resource value
        private final int value;           // game-specific numeric value
        private final String description;  // short description / effect text
    
        public Card(String id, String name, String type, int cost, int value, String description) {
            this.id = Objects.requireNonNull(id, "id");
            this.name = Objects.requireNonNull(name, "name");
            this.type = type == null ? "" : type;
            this.cost = cost;
            this.value = value;
            this.description = description == null ? "" : description;
        }
    
        public String getId() {
            return id;
        }
    
        public String getName() {
            return name;
        }
    
        public String getType() {
            return type;
        }
    
        public int getCost() {
            return cost;
        }
    
        public int getValue() {
            return value;
        }
    
        public String getDescription() {
            return description;
        }

        public static int countFrameworkCards(List<Card> cards) {
            int count = 0;
            for (Card card : cards) {
                if (card.getName().equals("Framework")) {
                    count++;
                }
            }
            return count;
        }
    
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
    
            Card card = (Card) o;
            return id.equals(card.id);
        }
    
        @Override
        public int hashCode() {
            return id.hashCode();
        }
    
        @Override
        public String toString() {
            return "Card{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", cost=" + cost +
                    ", value=" + value +
                    '}';
        }
    
    // Convenience factory for minimal cards
    // Convenience factory for minimal cards
    public static Card of(String id, String name) {
        return new Card(id, name, "", 0, 0, "");
    }
    
}
