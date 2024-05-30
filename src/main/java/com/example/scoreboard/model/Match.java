package com.example.scoreboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Match {
    private String homeTeam;
    private String awayTeam;
    private int homeScore;
    private int awayScore;
    private LocalDateTime startTime;

    public int totalScore() {
        return homeScore + awayScore;
    }
}