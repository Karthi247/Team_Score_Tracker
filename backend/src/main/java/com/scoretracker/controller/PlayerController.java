package com.scoretracker.controller;

import com.scoretracker.dto.common.ApiResponse;
import com.scoretracker.dto.common.PagedResponse;
import com.scoretracker.dto.player.PlayerRequest;
import com.scoretracker.dto.player.PlayerResponse;
import com.scoretracker.dto.player.PlayerStatsResponse;
import com.scoretracker.enums.PlayerStatus;
import com.scoretracker.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<PlayerResponse>>> getPlayers(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long teamId,
            @RequestParam(required = false) PlayerStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        PagedResponse<PlayerResponse> response = playerService.getPlayers(query, teamId, status, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<ApiResponse<List<PlayerResponse>>> getPlayersByTeam(@PathVariable Long teamId) {
        List<PlayerResponse> players = playerService.getPlayersByTeam(teamId);
        return ResponseEntity.ok(ApiResponse.success(players));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PlayerResponse>> getPlayerById(@PathVariable Long id) {
        PlayerResponse player = playerService.getPlayerById(id);
        return ResponseEntity.ok(ApiResponse.success(player));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PlayerResponse>> addPlayer(@Valid @RequestBody PlayerRequest request) {
        PlayerResponse created = playerService.addPlayer(request);
        return new ResponseEntity<>(ApiResponse.success("Player added successfully", created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PlayerResponse>> updatePlayer(@PathVariable Long id, @Valid @RequestBody PlayerRequest request) {
        PlayerResponse updated = playerService.updatePlayer(id, request);
        return ResponseEntity.ok(ApiResponse.success("Player updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.ok(ApiResponse.success("Player removed successfully", "DELETED"));
    }

    @GetMapping("/{id}/statistics")
    public ResponseEntity<ApiResponse<PlayerStatsResponse>> getPlayerStatistics(@PathVariable Long id) {
        PlayerStatsResponse stats = playerService.getPlayerStatistics(id);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}

