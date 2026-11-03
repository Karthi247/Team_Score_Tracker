package com.scoretracker.service;

import com.scoretracker.dto.match.MatchLiveScoreResponse;
import com.scoretracker.dto.match.MatchRequest;
import com.scoretracker.dto.match.MatchResponse;
import com.scoretracker.entity.Match;
import com.scoretracker.entity.Team;
import com.scoretracker.enums.MatchStatus;
import com.scoretracker.exception.BadRequestException;
import com.scoretracker.mapper.MatchMapper;
import com.scoretracker.repository.MatchRepository;
import com.scoretracker.repository.TeamRepository;
import com.scoretracker.repository.TournamentRepository;
import com.scoretracker.service.impl.MatchServiceImpl;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private StandingService standingService;

    @Mock
    private MatchMapper matchMapper;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private MatchServiceImpl matchService;

    private Team teamA;
    private Team teamB;
    private Match match;

    @BeforeEach
    void setUp() {
        teamA = new Team("Titans FC", "", "", "Marcus");
        teamA.setId(1L);

        teamB = new Team("Thunderbolts SC", "", "", "David");
        teamB.setId(2L);

        match = new Match(null, teamA, teamB, LocalDate.now(), LocalTime.of(15, 0), "Stadium", "Final");
        match.setId(100L);
    }

    @Test
    @DisplayName("Prevent scheduling a match where a team plays against itself")
    void testPreventSelfPlay() {
        MatchRequest request = new MatchRequest(null, 1L, 1L, LocalDate.now(), LocalTime.of(15, 0), "Stadium", "Final");

        assertThrows(BadRequestException.class, () -> matchService.scheduleMatch(request));
        verify(matchRepository, never()).save(any(Match.class));
    }

    @Test
    @DisplayName("Complete match sets status to COMPLETED and determines winner")
    void testCompleteMatchWinnerCalculation() {
        match.setStatus(MatchStatus.LIVE);
        match.setTeamAScore(3);
        match.setTeamBScore(1);

        when(matchRepository.findById(100L)).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);
        when(matchMapper.toLiveScoreResponse(match)).thenReturn(new MatchLiveScoreResponse());

        MatchResponse matchResponse = new MatchResponse();
        matchResponse.setId(100L);
        matchResponse.setWinnerName("Titans FC");
        when(matchMapper.toResponse(match)).thenReturn(matchResponse);

        MatchResponse result = matchService.completeMatch(100L);

        assertNotNull(result);
        assertEquals(MatchStatus.COMPLETED, match.getStatus());
        assertEquals(teamA, match.getWinner());
        verify(matchRepository, times(1)).save(match);
    }
}

