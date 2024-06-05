package com.example.scoreboard.controller;

import com.example.scoreboard.model.Match;
import com.example.scoreboard.service.ScoreboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScoreboardController.class)
public class ScoreboardControllerTest {

    public static final String SPAIN = "Spain";
    public static final String BRAZIL = "Brazil";
    public static final String MEXICO = "Mexico";
    public static final String CANADA = "Canada";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScoreboardService scoreboardService;


    private Match match1;
    private Match match2;

    @BeforeEach
    void setUp() {

        match1 = new Match(SPAIN, BRAZIL);
        match1.setHomeScore(10);
        match1.setAwayScore(2);

        match2 = new Match(MEXICO, CANADA);
        match2.setHomeScore(0);
        match2.setAwayScore(5);
    }

    @Test
    void testStartMatch() throws Exception {
        mockMvc.perform(post("/scoreboard/start")
                        .param("homeTeam", SPAIN)
                        .param("awayTeam", BRAZIL))
                .andExpect(status().isOk());

        Mockito.verify(scoreboardService, Mockito.times(1)).startMatch(SPAIN, BRAZIL);
    }

    @Test
    void testUpdateScore() throws Exception {
        String homeScore = "10";
        String awayScore = "2";
        mockMvc.perform(post("/scoreboard/update")
                        .param("homeTeam", SPAIN)
                        .param("awayTeam", BRAZIL)
                        .param("homeScore", homeScore)
                        .param("awayScore", awayScore))
                .andExpect(status().isOk());

        Mockito.verify(scoreboardService, Mockito.times(1)).updateScore(SPAIN, BRAZIL, Integer.parseInt(homeScore), Integer.parseInt(awayScore));
    }

    @Test
    void testFinishMatch() throws Exception {
        mockMvc.perform(post("/scoreboard/finish")
                        .param("homeTeam", SPAIN)
                        .param("awayTeam", BRAZIL))
                .andExpect(status().isOk());

        Mockito.verify(scoreboardService, Mockito.times(1)).finishMatch(SPAIN, BRAZIL);
    }

    @Test
    void testGetSummary() throws Exception {
        List<Match> matches = Arrays.asList(match1, match2);
        Mockito.when(scoreboardService.getSummary()).thenReturn(matches);

        mockMvc.perform(get("/scoreboard/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].homeTeam").value(SPAIN))
                .andExpect(jsonPath("$[0].awayTeam").value(BRAZIL))
                .andExpect(jsonPath("$[0].homeScore").value(10))
                .andExpect(jsonPath("$[0].awayScore").value(2))
                .andExpect(jsonPath("$[1].homeTeam").value(MEXICO))
                .andExpect(jsonPath("$[1].awayTeam").value(CANADA))
                .andExpect(jsonPath("$[1].homeScore").value(0))
                .andExpect(jsonPath("$[1].awayScore").value(5));
    }
}
