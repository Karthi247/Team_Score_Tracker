package com.scoretracker.service;

import com.scoretracker.dto.match.MatchLiveScoreResponse;
import com.scoretracker.dto.score.ScoreEventRequest;
import com.scoretracker.dto.score.ScoreEventResponse;
import com.scoretracker.security.UserPrincipal;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface ScoreService {
    MatchLiveScoreResponse addScoreEvent(Long matchId, ScoreEventRequest request, UserPrincipal currentUser);
    MatchLiveScoreResponse deleteScoreEvent(Long matchId, Long scoreEventId, UserPrincipal currentUser);
    List<ScoreEventResponse> getScoreEvents(Long matchId);
    MatchLiveScoreResponse getLiveMatchScore(Long matchId);
    SseEmitter registerSseClient(Long matchId);
}

