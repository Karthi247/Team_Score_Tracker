package com.scoretracker.controller;

import com.scoretracker.dto.common.ApiResponse;
import com.scoretracker.dto.standing.StandingResponse;
import com.scoretracker.service.StandingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments/{id}/standings")
public class StandingController {

    private final StandingService standingService;

    public StandingController(StandingService standingService) {
        this.standingService = standingService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StandingResponse>>> getStandings(@PathVariable Long id) {
        List<StandingResponse> standings = standingService.getStandingsByTournament(id);
        return ResponseEntity.ok(ApiResponse.success(standings));
    }

    @PostMapping("/recalculate")
    public ResponseEntity<ApiResponse<List<StandingResponse>>> recalculateStandings(@PathVariable Long id) {
        standingService.recalculateStandings(id);
        List<StandingResponse> standings = standingService.getStandingsByTournament(id);
        return ResponseEntity.ok(ApiResponse.success("Standings recalculated successfully", standings));
    }
}

