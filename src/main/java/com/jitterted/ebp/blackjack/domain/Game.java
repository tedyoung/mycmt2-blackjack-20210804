package com.jitterted.ebp.blackjack.domain;

import com.jitterted.ebp.blackjack.domain.port.GameMonitor;

public class Game {

    private final Deck deck;
    private final GameMonitor gameMonitor;

    private final Hand dealerHand = new Hand();
    private final Hand playerHand = new Hand();

    private boolean playerDone;


    public Game(Deck deck) {
        this.deck = deck;
        this.gameMonitor = game -> {};
    }

    public Game(Deck deck, GameMonitor gameMonitor) {
        this.deck = deck;
        this.gameMonitor = gameMonitor;
    }

    public void initialDeal() {
        dealRoundOfCards();
        dealRoundOfCards();
        updatePlayerDoneStateTo(playerHand.isBlackjack());
    }

    public void playerHits() {
        // PRE-CONDITION: if player is done, throw exception
        playerHand.drawFrom(deck);
        updatePlayerDoneStateTo(playerHand.isBusted());
    }

    public void playerStands() {
        dealerTurn();
        updatePlayerDoneStateTo(true);
    }

    public void updatePlayerDoneStateTo(boolean isPlayerDone) {
        playerDone = isPlayerDone;
        if (playerDone) {
            gameMonitor.roundCompleted(this);
        }
    }

    private void dealRoundOfCards() {
        // why: players first because this is the rule
        playerHand.drawFrom(deck);
        dealerHand.drawFrom(deck);
    }

    public GameOutcome determineOutcome() {
        if (playerHand.isBusted()) {
            return GameOutcome.PLAYER_BUSTED;
        } else if (dealerHand.isBusted()) {
            return GameOutcome.DEALER_BUSTED;
        } else if (playerHand.isBlackjack()) {
            return GameOutcome.PLAYER_WINS_BLACKJACK;
        } else if (playerHand.beats(dealerHand)) {
            return GameOutcome.PLAYER_BEATS_DEALER;
        } else if (playerHand.pushes(dealerHand)) {
            return GameOutcome.PLAYER_PUSHES;
        } else {
            return GameOutcome.PLAYER_LOSES;
        }
    }

    private void dealerTurn() {
        // Dealer makes its choice automatically based on a simple heuristic (<=16 must hit, =>17 must stand)
        if (!playerHand.isBusted()) {
            while (dealerHand.dealerMustDrawCard()) {
                dealerHand.drawFrom(deck);
            }
        }
    }

    // Query: no access to internals, point-in-time snapshot of state
    // 1) leave as-is
    // 2) clone: modifiable?
    // 3) "view-only" class: interface ReadOnlyHand (doesn't have drawCardFrom)
    //    "data transfer object" -- View/Data-only
    // --> HandView or ReadOnlyHand: value(), cards() (copy of original)
    public Hand playerHand() {
        return playerHand;
    }

    public Hand dealerHand() {
        return dealerHand;
    }

    public boolean isPlayerDone() {
        return playerDone;
    }


}
