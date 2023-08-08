package org.example;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(value, card.value) && Objects.equals(suit, card.suit);
    }
}