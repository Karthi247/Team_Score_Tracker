package com.scoretracker.service;

import com.scoretracker.dto.match.MatchLiveScoreResponse;
import com.scoretracker.dto.score.ScoreEventRequest;
import com.scoretracker.entity.*;
import com.scoretracker.enums.MatchStatus;
import com.scoretracker.enums.ScoreEventType;
import com.scoretracker.exception.BadRequestException;
import com.scoretracker.mapper.MatchMapper;
import com.scoretracker.mapper.ScoreEventMapper;
import com.scoretracker.repository.*;
import com.scoretracker.security.UserPrincipal;
import com.scoretracker.service.impl.ScoreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScoreServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private ScoreEventRepository scoreEventRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StandingService standingService;

    @Mock
    private MatchMapper matchMapper;

    @Mock
    private ScoreEventMapper scoreEventMapper;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ScoreServiceImpl scoreService;

    private Team teamA;
    private Team teamB;
    private Match match;
    private UserPrincipal principal;

    @BeforeEach
    void setUp() {
        teamA = new Team("Titans FC", "", "", "Marcus");
        teamA.setId(1L);

        teamB = new Team("Thunderbolts SC", "", "", "David");
        teamB.setId(2L);

        match = new Match(null, teamA, teamB, LocalDate.now(), LocalTime.of(15, 0), "Stadium", "Final");
        match.setId(100L);
        match.setStatus(MatchStatus.LIVE);
        match.setTeamAScore(1);
        match.setTeamBScore(0);

        principal = new UserPrincipal(1L, "Admin User", "admin@test.com", "123", "pass", Collections.emptyList());
    }

    @Test
    @DisplayName("Add score event adds points to team A and saves event")
    void testAddScoreEventSuccess() {
        ScoreEventRequest request = new ScoreEventRequest(1L, null, 1, ScoreEventType.GOAL, "25'", "Goal by team A");

        when(matchRepository.findById(100L)).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        MatchLiveScoreResponse liveResponse = new MatchLiveScoreResponse();
        liveResponse.setTeamAScore(2);
        liveResponse.setTeamBScore(0);
        when(matchMapper.toLiveScoreResponse(any(Match.class))).thenReturn(liveResponse);

        MatchLiveScoreResponse response = scoreService.addScoreEvent(100L, request, principal);

        assertNotNull(response);
        assertEquals(2, match.getTeamAScore());
        verify(scoreEventRepository, times(1)).save(any(ScoreEvent.class));
        verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/match/100"), any(MatchLiveScoreResponse.class));
    }

    @Test
    @DisplayName("Reject score event for team not playing in this match")
    void testScoreEventInvalidTeam() {
        ScoreEventRequest request = new ScoreEventRequest(999L, null, 1, ScoreEventType.GOAL, "25'", "Goal");

        when(matchRepository.findById(100L)).thenReturn(Optional.of(match));

        assertThrows(BadRequestException.class, () -> scoreService.addScoreEvent(100L, request, principal));
        verify(scoreEventRepository, never()).save(any(ScoreEvent.class));
    }
}

