package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Game;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BlackjackController {

    private final Game game;

    public BlackjackController(Game game) {
        this.game = game;
    }

    @GetMapping("/game")
    public String gameView(Model model) {
        populateGameViewInto(model);
        return "blackjack";
    }

    @PostMapping("/start-game")
    public String startGame() {
        game.initialDeal();
        return redirect();
    }

    @PostMapping("/hit")
    public String hitCommand() {
        game.playerHits();
        return redirect();
    }

    @PostMapping("/stand")
    public String standCommand() {
        game.playerStands();
        game.dealerTurn();
        return redirect();
    }

    // MAPS Game State to Adapter Redirect Page
    public String redirect() {
        if (game.isPlayerDone()) {
            return "redirect:/done";
        }
        return "redirect:/game";
    }

    @GetMapping("/done")
    public String doneView(Model model) {
        populateGameViewInto(model);
        model.addAttribute("outcome", game.determineOutcome().displayValue());
        return "done";
    }

    public void populateGameViewInto(Model model) {
        GameView gameView = GameView.of(game);
        model.addAttribute("gameView", gameView);
    }

}
