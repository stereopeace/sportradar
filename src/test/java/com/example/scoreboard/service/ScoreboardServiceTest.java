package com.example.scoreboard.service;

import com.example.scoreboard.exception.DuplicateTeamException;
import com.example.scoreboard.exception.NegativeScoreException;
import com.example.scoreboard.exception.ValidationException;
import com.example.scoreboard.model.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScoreboardServiceTest {

    public static final String MEXICO = "Mexico";
    public static final String SPAIN = "Spain";
    public static final String CANADA = "Canada";
    public static final String BRAZIL = "Brazil";
    public static final String GERMANY = "Germany";
    public static final String FRANCE = "France";
    public static final String URUGUAY = "Uruguay";
    public static final String ITALY = "Italy";
    public static final String ARGENTINA = "Argentina";
    public static final String AUSTRALIA = "Australia";
    private ScoreboardService scoreboardService;

    @BeforeEach
    void setUp() {
        scoreboardService = new ScoreboardService();
    }

    @Test
    void testStartAndFinishMatch() {
        scoreboardService.startMatch(MEXICO, CANADA);
        scoreboardService.startMatch(SPAIN, BRAZIL);
        List<Match> matches = scoreboardService.getSummary();
        assertEquals(2, matches.size());

        scoreboardService.finishMatch(MEXICO, CANADA);
        matches = scoreboardService.getSummary();
        assertEquals(1, matches.size());
        assertEquals(SPAIN, matches.get(0).getHomeTeam());
    }

    @Test
    void testUpdateScore() {
        scoreboardService.startMatch(GERMANY, FRANCE);
        scoreboardService.updateScore(GERMANY, FRANCE, 2, 2);
        Match match = scoreboardService.getSummary().get(0);
        assertEquals(2, match.getHomeScore());
        assertEquals(2, match.getAwayScore());
    }

    @Test
    void testGetSummary() {
        scoreboardService.startMatch(MEXICO, CANADA);
        scoreboardService.startMatch(SPAIN, BRAZIL);
        scoreboardService.startMatch(GERMANY, FRANCE);
        scoreboardService.startMatch(URUGUAY, ITALY);
        scoreboardService.startMatch(ARGENTINA, AUSTRALIA);

        scoreboardService.updateScore(MEXICO, CANADA, 0, 5);
        scoreboardService.updateScore(SPAIN, BRAZIL, 10, 2);
        scoreboardService.updateScore(GERMANY, FRANCE, 2, 2);
        scoreboardService.updateScore(URUGUAY, ITALY, 6, 6);
        scoreboardService.updateScore(ARGENTINA, AUSTRALIA, 3, 1);

        List<Match> summary = scoreboardService.getSummary();

        assertEquals(URUGUAY, summary.get(0).getHomeTeam());
        assertEquals(SPAIN, summary.get(1).getHomeTeam());
        assertEquals(MEXICO, summary.get(2).getHomeTeam());
        assertEquals(ARGENTINA, summary.get(3).getHomeTeam());
        assertEquals(GERMANY, summary.get(4).getHomeTeam());
    }

    @Test
    void testStartMatchWithSameTeams() {
        DuplicateTeamException exception = assertThrows(DuplicateTeamException.class,
                () -> scoreboardService.startMatch(SPAIN, SPAIN));

        assertEquals("The same team cannot play against itself: " + SPAIN, exception.getMessage());
    }

    @Test
    void testStartMatchWithTeamAlreadyInGame() {
        scoreboardService.startMatch(MEXICO, CANADA);


        DuplicateTeamException exception = assertThrows(DuplicateTeamException.class,
                () -> scoreboardService.startMatch(SPAIN, CANADA));

        assertEquals("A match with this team is already in progress: " + CANADA, exception.getMessage());
    }

    @Test
    void testStartMatchWithEmptyAwayTeamName() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> scoreboardService.startMatch(SPAIN, ""));

        assertEquals("The name of the team cannot be empty", exception.getMessage());
    }

    @Test
    void testStartMatchWithNullAwayTeamName() {

        ValidationException exception = assertThrows(ValidationException.class,
                () -> scoreboardService.startMatch(SPAIN, null));

        assertEquals("The name of the team cannot be empty", exception.getMessage());
    }

    @Test
    void testStartMatchWithEmptyHomeTeamName() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> scoreboardService.startMatch("", SPAIN));

        assertEquals("The name of the team cannot be empty", exception.getMessage());
    }

    @Test
    void testStartMatchWithNullHomeTeamName() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> scoreboardService.startMatch(null, SPAIN));

        assertEquals("The name of the team cannot be empty", exception.getMessage());
    }

    @Test
    void testUpdateForNegativeScore() {
        scoreboardService.startMatch(GERMANY, FRANCE);
        NegativeScoreException exception = assertThrows(NegativeScoreException.class,
                () -> scoreboardService.updateScore(GERMANY, FRANCE, 2, -2));

        assertEquals("The score cannot be negative", exception.getMessage());
    }

    @Test
    void testUpdateScoreWithEmptyAwayTeamName() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> scoreboardService.updateScore(SPAIN, "", 2, 2));

        assertEquals("The name of the team cannot be empty", exception.getMessage());
    }

    @Test
    void testUpdateScoreWithNullAwayTeamName() {

        ValidationException exception = assertThrows(ValidationException.class,
                () -> scoreboardService.updateScore(SPAIN, null, 2, 2));

        assertEquals("The name of the team cannot be empty", exception.getMessage());
    }

    @Test
    void testUpdateScoreWithEmptyHomeTeamName() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> scoreboardService.updateScore("", SPAIN, 2, 2));

        assertEquals("The name of the team cannot be empty", exception.getMessage());
    }

    @Test
    void testUpdateScoreWithNullHomeTeamName() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> scoreboardService.updateScore(null, SPAIN, 2, 2));

        assertEquals("The name of the team cannot be empty", exception.getMessage());
    }

    @Test
    void testFinishMatchWithEmptyAwayTeamName() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> scoreboardService.finishMatch(SPAIN, ""));

        assertEquals("The name of the team cannot be empty", exception.getMessage());
    }

    @Test
    void testFinishMatchWithNullAwayTeamName() {

        ValidationException exception = assertThrows(ValidationException.class,
                () -> scoreboardService.finishMatch(SPAIN, null));

        assertEquals("The name of the team cannot be empty", exception.getMessage());
    }

    @Test
    void testFinishMatchWithEmptyHomeTeamName() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> scoreboardService.finishMatch("", SPAIN));

        assertEquals("The name of the team cannot be empty", exception.getMessage());
    }

    @Test
    void testFinishMatchWithNullHomeTeamName() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> scoreboardService.finishMatch(null, SPAIN));

        assertEquals("The name of the team cannot be empty", exception.getMessage());
    }
}
