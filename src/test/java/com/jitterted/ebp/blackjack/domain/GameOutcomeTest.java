package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameOutcomeTest {

    @Test
    public void playerHasBetterHandThanDealerOutcomeIsPlayerBeatsDealer() throws Exception {
        Deck playerBeatsDealerDeck = StubDeck.playerBeatsDealer();
        Game game = new Game(playerBeatsDealerDeck);
        game.initialDeal();

        game.playerStands();
        game.dealerTurn();

        assertThat(game.determineOutcome())
                .isEqualByComparingTo(GameOutcome.PLAYER_BEATS_DEALER);
    }

    @Test
    public void playerHitsAndGoesBustOutcomeIsPlayerBusted() throws Exception {
        Deck playerHitsAndGoesBustDeck = StubDeck.playerHitsAndGoesBust();
        Game game = new Game(playerHitsAndGoesBustDeck);
        game.initialDeal();

        game.playerHits();

        assertThat(game.determineOutcome())
                .isEqualByComparingTo(GameOutcome.PLAYER_BUSTED);
    }

    @Test
    public void playerDealtBlackjackUponInitialDealThenImmediatelyWinsBlackjack() throws Exception {
        Deck playerDealtBlackjackDeck = StubDeck.playerDealtBlackjack();
        Game game = new Game(playerDealtBlackjackDeck);

        game.initialDeal();

        assertThat(game.determineOutcome())
                .isEqualByComparingTo(GameOutcome.PLAYER_WINS_BLACKJACK);
        assertThat(game.isPlayerDone())
                .isTrue();
    }

    @Test
    public void newGameThenIsPlayerDoneIsFalse() throws Exception {
        Game game = new Game(null); // <-- "crash test" Dummy (placeholder) Test Double

        assertThat(game.isPlayerDone())
                .isFalse();
    }

    @Test
    public void newGameInitialDealNotBlackjackThenPlayerDoneIsFalse() throws Exception {
        Deck playerNotDealtBlackjackDeck = StubDeck.playerNotDealtBlackjack();
        Game game = new Game(playerNotDealtBlackjackDeck);

        game.initialDeal();

        assertThat(game.isPlayerDone())
                .isFalse();
    }

}