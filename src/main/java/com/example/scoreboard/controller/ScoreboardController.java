package com.example.scoreboard.controller;

import com.example.scoreboard.exception.DuplicateTeamException;
import com.example.scoreboard.exception.NegativeScoreException;
import com.example.scoreboard.exception.ValidationException;
import com.example.scoreboard.model.Match;
import com.example.scoreboard.service.ScoreboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/scoreboard")
public class ScoreboardController {

    private final ScoreboardService scoreboardService;

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

    @ExceptionHandler(DuplicateTeamException.class)
    public ResponseEntity<String> handleDuplicateTeamException(DuplicateTeamException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NegativeScoreException.class)
    public ResponseEntity<String> handleNegativeScoreException(NegativeScoreException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

