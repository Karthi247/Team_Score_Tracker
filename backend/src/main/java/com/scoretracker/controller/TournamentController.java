package com.scoretracker.controller;

import com.scoretracker.dto.common.ApiResponse;
import com.scoretracker.dto.common.PagedResponse;
import com.scoretracker.dto.match.MatchResponse;
import com.scoretracker.dto.team.TeamResponse;
import com.scoretracker.dto.tournament.*;
import com.scoretracker.enums.SportType;
import com.scoretracker.enums.TournamentStatus;
import com.scoretracker.service.TournamentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<TournamentResponse>>> getTournaments(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) TournamentStatus status,
            @RequestParam(required = false) SportType sportType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        PagedResponse<TournamentResponse> response = tournamentService.getTournaments(query, status, sportType, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TournamentResponse>> getTournamentById(@PathVariable Long id) {
        TournamentResponse tournament = tournamentService.getTournamentById(id);
        return ResponseEntity.ok(ApiResponse.success(tournament));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TournamentResponse>> createTournament(@Valid @RequestBody TournamentRequest request) {
        TournamentResponse created = tournamentService.createTournament(request);
        return new ResponseEntity<>(ApiResponse.success("Tournament created successfully", created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TournamentResponse>> updateTournament(@PathVariable Long id, @Valid @RequestBody TournamentRequest request) {
        TournamentResponse updated = tournamentService.updateTournament(id, request);
        return ResponseEntity.ok(ApiResponse.success("Tournament updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return ResponseEntity.ok(ApiResponse.success("Tournament deleted successfully", "DELETED"));
    }

    @GetMapping("/{id}/teams")
    public ResponseEntity<ApiResponse<List<TeamResponse>>> getRegisteredTeams(@PathVariable Long id) {
        List<TeamResponse> teams = tournamentService.getRegisteredTeams(id);
        return ResponseEntity.ok(ApiResponse.success(teams));
    }

    @PostMapping("/{id}/teams")
    public ResponseEntity<ApiResponse<String>> registerTeam(@PathVariable Long id, @Valid @RequestBody RegisterTeamRequest request) {
        tournamentService.registerTeam(id, request);
        return ResponseEntity.ok(ApiResponse.success("Team registered to tournament successfully", "REGISTERED"));
    }

    @DeleteMapping("/{id}/teams/{teamId}")
    public ResponseEntity<ApiResponse<String>> removeRegisteredTeam(@PathVariable Long id, @PathVariable Long teamId) {
        tournamentService.removeRegisteredTeam(id, teamId);
        return ResponseEntity.ok(ApiResponse.success("Team removed from tournament successfully", "REMOVED"));
    }

    @PostMapping("/{id}/generate-fixtures")
    public ResponseEntity<ApiResponse<List<MatchResponse>>> generateFixtures(@PathVariable Long id,
                                                                             @RequestBody(required = false) FixtureGenerateRequest request) {
        List<MatchResponse> fixtures = tournamentService.generateFixtures(id, request);
        return ResponseEntity.ok(ApiResponse.success("Fixtures generated successfully", fixtures));
    }

    @GetMapping("/{id}/statistics")
    public ResponseEntity<ApiResponse<TournamentStatsResponse>> getTournamentStatistics(@PathVariable Long id) {
        TournamentStatsResponse stats = tournamentService.getTournamentStatistics(id);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}

