package org.example;

import java.util.ArrayList;
import java.util.List;

public class OldMaid {
    public static void main(String[] args) throws InterruptedException {
        PlayersTable playersTable = new PlayersTable(4, createDeck());
        playersTable.startGame();
    }

    private static List<Card> createDeck() {
        List<Card> deck = new ArrayList<>();
        String[] suits = {"Spades", "Clubs", "Hearts", "Diamonds"};
//        String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        String[] values = {"2", "3", "4", "5", "6"};

        for (String suit : suits) {
            for (String value : values) {
                deck.add(new Card(value, suit));
            }
        }

        deck.add(new Card("Joker", ""));  // Joker has no suit
        return deck;
    }
}
