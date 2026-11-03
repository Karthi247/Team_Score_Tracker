package com.scoretracker.service;

import com.scoretracker.dto.standing.StandingResponse;

import java.util.List;

public interface StandingService {
    List<StandingResponse> getStandingsByTournament(Long tournamentId);
    void recalculateStandings(Long tournamentId);
}

