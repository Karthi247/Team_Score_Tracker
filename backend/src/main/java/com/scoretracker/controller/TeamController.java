package com.scoretracker.controller;

import com.scoretracker.dto.common.ApiResponse;
import com.scoretracker.dto.common.PagedResponse;
import com.scoretracker.dto.team.TeamRequest;
import com.scoretracker.dto.team.TeamResponse;
import com.scoretracker.dto.team.TeamStatsResponse;
import com.scoretracker.enums.TeamStatus;
import com.scoretracker.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<TeamResponse>>> getTeams(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) TeamStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        PagedResponse<TeamResponse> response = teamService.getTeams(query, status, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<TeamResponse>>> getActiveTeams() {
        List<TeamResponse> activeTeams = teamService.getAllActiveTeams();
        return ResponseEntity.ok(ApiResponse.success(activeTeams));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeamResponse>> getTeamById(@PathVariable Long id) {
        TeamResponse team = teamService.getTeamById(id);
        return ResponseEntity.ok(ApiResponse.success(team));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TeamResponse>> createTeam(@Valid @RequestBody TeamRequest request) {
        TeamResponse created = teamService.createTeam(request);
        return new ResponseEntity<>(ApiResponse.success("Team created successfully", created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TeamResponse>> updateTeam(@PathVariable Long id, @Valid @RequestBody TeamRequest request) {
        TeamResponse updated = teamService.updateTeam(id, request);
        return ResponseEntity.ok(ApiResponse.success("Team updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.ok(ApiResponse.success("Team deleted successfully", "DELETED"));
    }

    @GetMapping("/{id}/statistics")
    public ResponseEntity<ApiResponse<TeamStatsResponse>> getTeamStatistics(@PathVariable Long id) {
        TeamStatsResponse stats = teamService.getTeamStatistics(id);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}

