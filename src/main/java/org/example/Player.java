package org.example;

import java.util.List;
import java.util.Random;

public class Player extends Thread {
    private final PlayersTable playersTable;

    public int getPlayerId() {
        return playerId;
    }

    private final int playerId;
    public List<Card> hand;
    private boolean isMyTurn;
    private boolean gameOver;

    public Player(PlayersTable playersTable, int playerId, List<Card> hand) {
        this.playersTable = playersTable;
        this.playerId = playerId;
        this.hand = hand;
        this.isMyTurn = false;
        this.gameOver = false;
    }

    @Override
    public void run() {
        while (!gameOver) {
            try {
                synchronized (playersTable) {
                    if (isMyTurn) {
                        checkForPairs();

                        Thread.sleep(1000);

                        Player nextPlayer = playersTable.getNextPlayer(playerId);
                        nextPlayer.receiveCard(pickCard());
                        isMyTurn = false;
                        nextPlayer.playTurn();

                        if (hand.isEmpty()) {
                            playersTable.removePlayer(this);
                            return;
                        }

                        playersTable.notifyAll();

//                        if (playersTable.numberOfPlayers() == 1 && hand.size() == 1 && hand.get(0).suit.equals("Joker")) {
//                            playersTable.stopGame();
//                        }
                    } else {
                        playersTable.wait();
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public synchronized void receiveCard(Card c) {
        hand.add(c);
    }

    private synchronized Card pickCard() {
        Random rand = new Random();
        return hand.remove(rand.nextInt(hand.size()));
    }

    public void checkForPairs() {
        OUTER_LOOP:
        while (true) {
            for (int i = 0; i < hand.size(); i++) {
                for (int j = i + 1; j < hand.size(); j++) {
                    if (hand.get(i).matches(hand.get(j))) {
                        System.out.println("Player " + playerId + " discarded a pair.");
                        System.out.println(hand.get(j) + " " + hand.get(i));
                        hand.remove(j);  // Always remove the latter element first
                        hand.remove(i);
                        continue OUTER_LOOP;  // Go back and start checking from the beginning
                    }
                }
            }
            break;  // Exit the OUTER_LOOP if no pairs are found
        }
    }

    public void playTurn() {
        isMyTurn = true;
        System.out.println("Turn switched to player " + playerId);
        System.out.println("Hand: " + hand);
    }

    public void gameOver() {
        gameOver = true;
    }

    public void printState() {
        System.out.println("Player " + playerId + (isMyTurn ? " has turn." : " does NOT have turn."));
    }
}

