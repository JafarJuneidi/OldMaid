package org.example;

public class Card {
    String value;
    String suit;

    public Card(String value, String suit) {
        this.value = value;
        this.suit = suit;
    }

    public boolean matches(Card c) {
        return this.value.equals(c.value) && ((this.suit.equals("Spades") && c.suit.equals("Clubs")) ||
                (this.suit.equals("Clubs") && c.suit.equals("Spades")) ||
                (this.suit.equals("Hearts") && c.suit.equals("Diamonds")) ||
                (this.suit.equals("Diamonds") && c.suit.equals("Hearts")));
    }

    @Override
    public String toString() {
        return "Card{" +
                "value='" + value + '\'' +
                ", suit='" + suit + '\'' +
                '}';
    }
}
