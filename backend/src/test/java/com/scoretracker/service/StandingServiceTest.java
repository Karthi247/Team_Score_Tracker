package com.scoretracker.service;

import com.scoretracker.entity.*;
import com.scoretracker.enums.MatchStatus;
import com.scoretracker.enums.SportType;
import com.scoretracker.enums.TournamentFormat;
import com.scoretracker.enums.TournamentStatus;
import com.scoretracker.mapper.StandingMapper;
import com.scoretracker.repository.*;
import com.scoretracker.service.impl.StandingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StandingServiceTest {

    @Mock
    private TeamStandingRepository teamStandingRepository;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private TournamentTeamRepository tournamentTeamRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private StandingMapper standingMapper;

    @InjectMocks
    private StandingServiceImpl standingService;

    private Tournament tournament;
    private Team teamA;
    private Team teamB;
    private Match match;

    @BeforeEach
    void setUp() {
        tournament = new Tournament("Premier League", SportType.FOOTBALL, "", LocalDate.now(), LocalDate.now().plusMonths(1), "Stadium", 4, TournamentStatus.ONGOING, TournamentFormat.LEAGUE, 3, 1, 0);
        tournament.setId(10L);

        teamA = new Team("Titans FC", "", "", "Marcus");
        teamA.setId(1L);

        teamB = new Team("Thunderbolts SC", "", "", "David");
        teamB.setId(2L);

        match = new Match(tournament, teamA, teamB, LocalDate.now(), LocalTime.of(15, 0), "Stadium", "Matchday 1");
        match.setStatus(MatchStatus.COMPLETED);
        match.setTeamAScore(3);
        match.setTeamBScore(1);
        match.setWinner(teamA);
    }

    @Test
    @DisplayName("Recalculate standings gives winner 3 points, loser 0 points, and sorts properly")
    void testRecalculateStandingsSuccess() {
        TournamentTeam tt1 = new TournamentTeam(tournament, teamA, 1);
        TournamentTeam tt2 = new TournamentTeam(tournament, teamB, 2);

        when(tournamentRepository.findById(10L)).thenReturn(Optional.of(tournament));
        when(tournamentTeamRepository.findByTournamentId(10L)).thenReturn(Arrays.asList(tt1, tt2));
        when(matchRepository.findByTournamentId(10L)).thenReturn(Collections.singletonList(match));
        when(teamStandingRepository.findByTournamentIdAndTeamId(10L, 1L)).thenReturn(Optional.of(new TeamStanding(tournament, teamA)));
        when(teamStandingRepository.findByTournamentIdAndTeamId(10L, 2L)).thenReturn(Optional.of(new TeamStanding(tournament, teamB)));

        standingService.recalculateStandings(10L);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<TeamStanding>> captor = ArgumentCaptor.forClass(List.class);
        verify(teamStandingRepository).saveAll(captor.capture());

        List<TeamStanding> savedStandings = captor.getValue();
        assertEquals(2, savedStandings.size());

        TeamStanding firstPlace = savedStandings.get(0);
        assertEquals(teamA.getId(), firstPlace.getTeam().getId());
        assertEquals(1, firstPlace.getPositionRank());
        assertEquals(3, firstPlace.getTournamentPoints());
        assertEquals(1, firstPlace.getWins());
        assertEquals(2, firstPlace.getScoreDifference()); // 3 - 1 = +2

        TeamStanding secondPlace = savedStandings.get(1);
        assertEquals(teamB.getId(), secondPlace.getTeam().getId());
        assertEquals(2, secondPlace.getPositionRank());
        assertEquals(0, secondPlace.getTournamentPoints());
        assertEquals(1, secondPlace.getLosses());
        assertEquals(-2, secondPlace.getScoreDifference()); // 1 - 3 = -2
    }
}

