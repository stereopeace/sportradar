package com.example.scoreboard.service;

import com.example.scoreboard.exception.DuplicateTeamException;
import com.example.scoreboard.exception.NegativeScoreException;
import com.example.scoreboard.exception.ValidationException;
import com.example.scoreboard.model.Match;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScoreboardService {
    public static final String VS = " vs ";
    private final Map<String, Match> matches = new LinkedHashMap<>();
    private final Set<String> teamsInGame = new HashSet<>();

    public void startMatch(String homeTeam, String awayTeam) {

        if (homeTeam == null || homeTeam.isEmpty() || awayTeam == null || awayTeam.isEmpty()) {
            throw new ValidationException("The name of the team cannot be empty");
        }

        if (homeTeam.equals(awayTeam)) {
            throw new DuplicateTeamException("The same team cannot play against itself: " + homeTeam);
        }

        if (teamsInGame.contains(homeTeam) || teamsInGame.contains(awayTeam)) {
            throw new DuplicateTeamException("A match with this team is already in progress: "
                    + (teamsInGame.contains(homeTeam) ? homeTeam : awayTeam));
        }

        String matchKey = getMatchKey(homeTeam, awayTeam);
        matches.put(matchKey, new Match(homeTeam, awayTeam));
        teamsInGame.add(homeTeam);
        teamsInGame.add(awayTeam);
    }

    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        if (homeTeam == null || homeTeam.isEmpty() || awayTeam == null || awayTeam.isEmpty()) {
            throw new ValidationException("The name of the team cannot be empty");
        }

        if (homeScore < 0 || awayScore < 0) {
            throw new NegativeScoreException("The score cannot be negative");
        }

        String matchKey = getMatchKey(homeTeam, awayTeam);
        Match match = matches.get(matchKey);
        if (match != null) {
            match.setHomeScore(homeScore);
            match.setAwayScore(awayScore);
        }
    }

    public void finishMatch(String homeTeam, String awayTeam) {
        if (homeTeam == null || homeTeam.isEmpty() || awayTeam == null || awayTeam.isEmpty()) {
            throw new ValidationException("The name of the team cannot be empty");
        }

        String matchKey = getMatchKey(homeTeam, awayTeam);
        if (Objects.nonNull(matches.remove(matchKey))) {
            teamsInGame.remove(homeTeam);
            teamsInGame.remove(awayTeam);
        }

    }

    public List<Match> getSummary() {
        return matches.values().stream()
                .sorted(Comparator.comparingInt(Match::totalScore).reversed()
                        .thenComparing(Match::getStartTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    private String getMatchKey(String homeTeam, String awayTeam) {
        return homeTeam + VS + awayTeam;
    }
}
