package com.scoretracker.service;

import com.scoretracker.dto.common.PagedResponse;
import com.scoretracker.dto.match.MatchLiveScoreResponse;
import com.scoretracker.dto.match.MatchRequest;
import com.scoretracker.dto.match.MatchResponse;
import com.scoretracker.dto.match.MatchStatusUpdateRequest;
import com.scoretracker.enums.MatchStatus;

import java.time.LocalDate;
import java.util.List;

public interface MatchService {
    MatchResponse scheduleMatch(MatchRequest request);
    MatchResponse updateMatch(Long id, MatchRequest request);
    void deleteMatch(Long id);
    MatchResponse getMatchById(Long id);
    MatchLiveScoreResponse getLiveMatchDetails(Long id);
    MatchResponse startMatch(Long id);
    MatchResponse completeMatch(Long id);
    MatchResponse cancelMatch(Long id);
    MatchResponse updateMatchStatus(Long id, MatchStatusUpdateRequest request);
    PagedResponse<MatchResponse> getMatches(Long tournamentId, MatchStatus status, LocalDate date, Long teamId, int page, int size, String sortBy, String sortDir);
    List<MatchResponse> getLiveMatches();
    List<MatchResponse> getRecentCompletedMatches(int limit);
    List<MatchResponse> getUpcomingMatches(int limit);
}

