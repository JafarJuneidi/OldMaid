package org.example;

import java.util.ArrayList;
import java.util.List;

public class PlayersTable {
    public final List<Player> players;

    public PlayersTable(int numPlayers, List<Card> deck) {
        players = new ArrayList<>(numPlayers);
        for (int i = 0; i < numPlayers; ++i) {
            Player player = new Player(this, i + 1);
            players.add(player);
        }
        createHand(deck);
    }

    public void startGame() {
        players.forEach(Thread::start);
        players.get(0).playTurn();
        synchronized (this) {
            notifyAll();
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
        System.out.println("Player " + player.getPlayerId() + " has been removed!");
    }

    public Player getNextPlayer(Player currentPlayer) {
        int currentPlayerIndex = players.indexOf(currentPlayer);
        int nextPlayerIndex = (currentPlayerIndex + 1) % players.size();
        return players.get(nextPlayerIndex);
    }

    public Player getPreviousPlayer(Player currentPlayer) {
        int currentPlayerIndex = players.indexOf(currentPlayer);
        int previousPlayerIndex = Math.floorMod(currentPlayerIndex - 1, players.size());
        return players.get(previousPlayerIndex);
    }

    public void createHand(List<Card> deck) {
        int playerCount = players.size();
        int cardsPerPlayer = deck.size() / playerCount;
        int extraCards = deck.size() % playerCount;

        for (Player player : players) {
            for (int i = 0; i < cardsPerPlayer; i++) {
                player.receiveCard(deck.remove(0));
            }
            if (extraCards > 0) {
                player.receiveCard(deck.remove(0));
                extraCards--;
            }
        }
    }
}