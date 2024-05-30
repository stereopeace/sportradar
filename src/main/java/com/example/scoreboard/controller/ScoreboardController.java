package com.example.scoreboard.controller;

import com.example.scoreboard.model.Match;
import com.example.scoreboard.service.ScoreboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scoreboard")
public class ScoreboardController {

    @Autowired
    private ScoreboardService scoreboardService;

    @PostMapping("/start")
    public void startMatch(@RequestParam String homeTeam, @RequestParam String awayTeam) {
        scoreboardService.startMatch(homeTeam, awayTeam);
    }

    @PostMapping("/update")
    public void updateScore(@RequestParam String homeTeam, @RequestParam String awayTeam,
                            @RequestParam int homeScore, @RequestParam int awayScore) {
        scoreboardService.updateScore(homeTeam, awayTeam, homeScore, awayScore);
    }

    @PostMapping("/finish")
    public void finishMatch(@RequestParam String homeTeam, @RequestParam String awayTeam) {
        scoreboardService.finishMatch(homeTeam, awayTeam);
    }

    @GetMapping("/summary")
    public List<Match> getSummary() {
        return scoreboardService.getSummary();
    }
}

