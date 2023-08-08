package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player extends Thread {
    private final PlayersTable playersTable;
    private final int playerId;
    private final List<Card> hand;
    private boolean isMyTurn;

    public Player(PlayersTable playersTable, int playerId) {
        this.playersTable = playersTable;
        this.playerId = playerId;
        this.hand = new ArrayList<>();
        this.isMyTurn = false;
    }

    public int getPlayerId() {
        return playerId;
    }

    @Override
    public void run() {
        while (!hand.isEmpty()) {
            try {
                synchronized (playersTable) {
                    if (isMyTurn) {
                        System.out.println("Player " + playerId + " turn");
                        Player previousPlayer = playersTable.getPreviousPlayer(this);
                        while (previousPlayer.hand.isEmpty()) {
                            playersTable.wait();
                            previousPlayer = previousPlayer.playersTable.getPreviousPlayer(this);
                        }
                        receiveCard(previousPlayer.pickCard());

                        checkForPairs();

                        if (playersTable.players.size() == 1) {
                            System.out.println("Player " + playerId + " is the Old Maid!");
                            return;
                        }

                        isMyTurn = false;
                        Player nextPlayer = playersTable.getNextPlayer(this);
                        nextPlayer.playTurn();
                        playersTable.notifyAll();
                    } else {
                        playersTable.wait();
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        synchronized (playersTable) {
            if (isMyTurn) {
                isMyTurn = false;
                Player nextPlayer = playersTable.getNextPlayer(this);
                nextPlayer.playTurn();
            }
            playersTable.removePlayer(this);
            playersTable.notifyAll();
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
                        hand.remove(j);
                        hand.remove(i);
                        continue OUTER_LOOP;
                    }
                }
            }
            break;
        }
    }

    public void playTurn() {
        isMyTurn = true;
    }
}