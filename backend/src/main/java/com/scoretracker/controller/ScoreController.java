package com.scoretracker.controller;

import com.scoretracker.dto.common.ApiResponse;
import com.scoretracker.dto.match.MatchLiveScoreResponse;
import com.scoretracker.dto.score.ScoreEventRequest;
import com.scoretracker.dto.score.ScoreEventResponse;
import com.scoretracker.security.CurrentUser;
import com.scoretracker.security.UserPrincipal;
import com.scoretracker.service.ScoreService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/matches/{id}")
public class ScoreController {

    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @PostMapping("/scores")
    public ResponseEntity<ApiResponse<MatchLiveScoreResponse>> recordScoreEvent(
            @PathVariable Long id,
            @Valid @RequestBody ScoreEventRequest request,
            @CurrentUser UserPrincipal currentUser) {
        MatchLiveScoreResponse updated = scoreService.addScoreEvent(id, request, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Score event recorded successfully", updated));
    }

    @DeleteMapping("/scores/{scoreEventId}")
    public ResponseEntity<ApiResponse<MatchLiveScoreResponse>> deleteScoreEvent(
            @PathVariable Long id,
            @PathVariable Long scoreEventId,
            @CurrentUser UserPrincipal currentUser) {
        MatchLiveScoreResponse updated = scoreService.deleteScoreEvent(id, scoreEventId, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Score event reverted successfully", updated));
    }

    @GetMapping("/scores")
    public ResponseEntity<ApiResponse<List<ScoreEventResponse>>> getScoreEvents(@PathVariable Long id) {
        List<ScoreEventResponse> events = scoreService.getScoreEvents(id);
        return ResponseEntity.ok(ApiResponse.success(events));
    }

    @GetMapping("/live")
    public ResponseEntity<ApiResponse<MatchLiveScoreResponse>> getLiveMatch(@PathVariable Long id) {
        MatchLiveScoreResponse live = scoreService.getLiveMatchScore(id);
        return ResponseEntity.ok(ApiResponse.success(live));
    }

    @GetMapping(value = "/live-sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeLiveMatchSSE(@PathVariable Long id) {
        return scoreService.registerSseClient(id);
    }
}

