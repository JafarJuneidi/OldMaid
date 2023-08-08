package org.example;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayersTable {
    private final List<Player> players;

    public PlayersTable(int numPlayers, List<Card> deck) {
        players = new ArrayList<>(numPlayers);
        for (int i = 0; i < numPlayers; ++i) {
            Player player = new Player(this, i+1, createHand(deck, numPlayers));
            players.add(player);
        }
    }

    public void startGame() {
        players.forEach(Thread::start);
        players.get(0).playTurn();
        synchronized (this) {
            notifyAll();
        }
    }
    public void stopGame() {
        players.forEach(Player::gameOver);
        synchronized (this) {
            notifyAll();
        }
        players.forEach(p-> {
            try {
                p.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        players.forEach(Player::printState);
    }

    public void removePlayer(Player player) {
        players.remove(player);
        System.out.println("Player " + player.getPlayerId() + " has been removed!");
    }

    public Player getNextPlayer(int currentPlayerId) {
        int nextPlayer = (currentPlayerId -1 + 1) % players.size();
        return players.get(nextPlayer);
    }

    private static List<Card> createHand(List<Card> deck, int numPlayers) {
        Iterator<Card> deckIterator = deck.iterator();
        List<Card> hand = new ArrayList<>();

        while (hand.size() < (20 + 1) / numPlayers) {
            hand.add(deckIterator.next());
            deckIterator.remove();
        }
        return hand;
    }
}
