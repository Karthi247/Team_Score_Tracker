package com.scoretracker.service;

import com.scoretracker.dto.common.PagedResponse;
import com.scoretracker.dto.team.TeamRequest;
import com.scoretracker.dto.team.TeamResponse;
import com.scoretracker.dto.team.TeamStatsResponse;
import com.scoretracker.enums.TeamStatus;

import java.util.List;

public interface TeamService {
    TeamResponse createTeam(TeamRequest request);
    TeamResponse updateTeam(Long id, TeamRequest request);
    void deleteTeam(Long id);
    TeamResponse getTeamById(Long id);
    List<TeamResponse> getAllActiveTeams();
    PagedResponse<TeamResponse> getTeams(String query, TeamStatus status, int page, int size, String sortBy, String sortDir);
    TeamStatsResponse getTeamStatistics(Long id);
}

