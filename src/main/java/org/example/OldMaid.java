package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OldMaid {
    public static void main(String[] args) throws InterruptedException {
        int numberOfPlayers = Integer.parseInt(args[0]);
        if (numberOfPlayers < 2 || numberOfPlayers > 26) {
            System.out.println("Players should be 2 - 26");
        }
        List<Card> deck = createDeck();

        Collections.shuffle(deck);
        PlayersTable playersTable = new PlayersTable(numberOfPlayers, deck);
        playersTable.players.forEach(Player::checkForPairs);
        playersTable.startGame();
    }

    private static List<Card> createDeck() {
        List<Card> deck = new ArrayList<>();
        String[] suits = {"Spades", "Clubs", "Hearts", "Diamonds"};
        String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

        for (String suit : suits) {
            for (String value : values) {
                deck.add(new Card(value, suit));
            }
        }

        deck.add(new Card("Joker", ""));
        return deck;
    }
}