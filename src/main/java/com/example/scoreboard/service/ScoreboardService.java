package com.example.scoreboard.service;

import com.example.scoreboard.model.Match;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScoreboardService {
    private final Map<String, Match> matches = new LinkedHashMap<>();

    public void startMatch(String homeTeam, String awayTeam) {
        String matchKey = getMatchKey(homeTeam, awayTeam);
        matches.put(matchKey, new Match(homeTeam, awayTeam));
    }

    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        String matchKey = getMatchKey(homeTeam, awayTeam);
        Match match = matches.get(matchKey);
        if (match != null) {
            match.setHomeScore(homeScore);
            match.setAwayScore(awayScore);
        }
    }

    public void finishMatch(String homeTeam, String awayTeam) {
        String matchKey = getMatchKey(homeTeam, awayTeam);
        matches.remove(matchKey);
    }

    public List<Match> getSummary() {
        return matches.values().stream()
                .sorted(Comparator.comparingInt(Match::totalScore).reversed()
                        .thenComparing(Match::getStartTime).reversed())
                .collect(Collectors.toList());
    }

    private String getMatchKey(String homeTeam, String awayTeam) {
        return homeTeam + " vs " + awayTeam;
    }
}
