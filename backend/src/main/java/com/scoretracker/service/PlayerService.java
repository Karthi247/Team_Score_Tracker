package com.scoretracker.service;

import com.scoretracker.dto.common.PagedResponse;
import com.scoretracker.dto.player.PlayerRequest;
import com.scoretracker.dto.player.PlayerResponse;
import com.scoretracker.dto.player.PlayerStatsResponse;
import com.scoretracker.enums.PlayerStatus;

import java.util.List;

public interface PlayerService {
    PlayerResponse addPlayer(PlayerRequest request);
    PlayerResponse updatePlayer(Long id, PlayerRequest request);
    void deletePlayer(Long id);
    PlayerResponse getPlayerById(Long id);
    List<PlayerResponse> getPlayersByTeam(Long teamId);
    PagedResponse<PlayerResponse> getPlayers(String query, Long teamId, PlayerStatus status, int page, int size, String sortBy, String sortDir);
    PlayerStatsResponse getPlayerStatistics(Long id);
}

