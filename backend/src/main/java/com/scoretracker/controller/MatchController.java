package com.scoretracker.controller;

import com.scoretracker.dto.common.ApiResponse;
import com.scoretracker.dto.common.PagedResponse;
import com.scoretracker.dto.match.MatchLiveScoreResponse;
import com.scoretracker.dto.match.MatchRequest;
import com.scoretracker.dto.match.MatchResponse;
import com.scoretracker.dto.match.MatchStatusUpdateRequest;
import com.scoretracker.enums.MatchStatus;
import com.scoretracker.service.MatchService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<MatchResponse>>> getMatches(
            @RequestParam(required = false) Long tournamentId,
            @RequestParam(required = false) MatchStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Long teamId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "matchDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        PagedResponse<MatchResponse> response = matchService.getMatches(tournamentId, status, date, teamId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/live")
    public ResponseEntity<ApiResponse<List<MatchResponse>>> getLiveMatches() {
        List<MatchResponse> live = matchService.getLiveMatches();
        return ResponseEntity.ok(ApiResponse.success(live));
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<MatchResponse>>> getRecentCompletedMatches(@RequestParam(defaultValue = "5") int limit) {
        List<MatchResponse> recent = matchService.getRecentCompletedMatches(limit);
        return ResponseEntity.ok(ApiResponse.success(recent));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<List<MatchResponse>>> getUpcomingMatches(@RequestParam(defaultValue = "5") int limit) {
        List<MatchResponse> upcoming = matchService.getUpcomingMatches(limit);
        return ResponseEntity.ok(ApiResponse.success(upcoming));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MatchResponse>> getMatchById(@PathVariable Long id) {
        MatchResponse match = matchService.getMatchById(id);
        return ResponseEntity.ok(ApiResponse.success(match));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<ApiResponse<MatchLiveScoreResponse>> getMatchDetails(@PathVariable Long id) {
        MatchLiveScoreResponse match = matchService.getLiveMatchDetails(id);
        return ResponseEntity.ok(ApiResponse.success(match));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MatchResponse>> scheduleMatch(@Valid @RequestBody MatchRequest request) {
        MatchResponse created = matchService.scheduleMatch(request);
        return new ResponseEntity<>(ApiResponse.success("Match scheduled successfully", created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MatchResponse>> updateMatch(@PathVariable Long id, @Valid @RequestBody MatchRequest request) {
        MatchResponse updated = matchService.updateMatch(id, request);
        return ResponseEntity.ok(ApiResponse.success("Match updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteMatch(@PathVariable Long id) {
        matchService.deleteMatch(id);
        return ResponseEntity.ok(ApiResponse.success("Match deleted successfully", "DELETED"));
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<ApiResponse<MatchResponse>> startMatch(@PathVariable Long id) {
        MatchResponse started = matchService.startMatch(id);
        return ResponseEntity.ok(ApiResponse.success("Match started successfully", started));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<MatchResponse>> completeMatch(@PathVariable Long id) {
        MatchResponse completed = matchService.completeMatch(id);
        return ResponseEntity.ok(ApiResponse.success("Match completed successfully", completed));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<MatchResponse>> cancelMatch(@PathVariable Long id) {
        MatchResponse cancelled = matchService.cancelMatch(id);
        return ResponseEntity.ok(ApiResponse.success("Match cancelled successfully", cancelled));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<MatchResponse>> updateStatus(@PathVariable Long id,
                                                                  @Valid @RequestBody MatchStatusUpdateRequest request) {
        MatchResponse updated = matchService.updateMatchStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success("Match status updated successfully", updated));
    }
}

