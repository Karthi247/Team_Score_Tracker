package com.scoretracker.service;

import com.scoretracker.dto.common.PagedResponse;
import com.scoretracker.dto.match.MatchResponse;
import com.scoretracker.dto.team.TeamResponse;
import com.scoretracker.dto.tournament.*;
import com.scoretracker.enums.SportType;
import com.scoretracker.enums.TournamentStatus;

import java.util.List;

public interface TournamentService {
    TournamentResponse createTournament(TournamentRequest request);
    TournamentResponse updateTournament(Long id, TournamentRequest request);
    void deleteTournament(Long id);
    TournamentResponse getTournamentById(Long id);
    PagedResponse<TournamentResponse> getTournaments(String query, TournamentStatus status, SportType sportType, int page, int size, String sortBy, String sortDir);
    List<TeamResponse> getRegisteredTeams(Long tournamentId);
    void registerTeam(Long tournamentId, RegisterTeamRequest request);
    void removeRegisteredTeam(Long tournamentId, Long teamId);
    List<MatchResponse> generateFixtures(Long tournamentId, FixtureGenerateRequest request);
    TournamentStatsResponse getTournamentStatistics(Long tournamentId);
}

