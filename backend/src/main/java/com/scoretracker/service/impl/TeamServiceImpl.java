package com.scoretracker.service.impl;

import com.scoretracker.dto.common.PagedResponse;
import com.scoretracker.dto.team.TeamRequest;
import com.scoretracker.dto.team.TeamResponse;
import com.scoretracker.dto.team.TeamStatsResponse;
import com.scoretracker.entity.Match;
import com.scoretracker.entity.Team;
import com.scoretracker.enums.MatchStatus;
import com.scoretracker.enums.TeamStatus;
import com.scoretracker.exception.ConflictException;
import com.scoretracker.exception.ResourceNotFoundException;
import com.scoretracker.mapper.TeamMapper;
import com.scoretracker.repository.MatchRepository;
import com.scoretracker.repository.TeamRepository;
import com.scoretracker.repository.TournamentTeamRepository;
import com.scoretracker.service.TeamService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final TournamentTeamRepository tournamentTeamRepository;
    private final TeamMapper teamMapper;

    public TeamServiceImpl(TeamRepository teamRepository,
                           MatchRepository matchRepository,
                           TournamentTeamRepository tournamentTeamRepository,
                           TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
        this.tournamentTeamRepository = tournamentTeamRepository;
        this.teamMapper = teamMapper;
    }

    @Override
    @Transactional
    public TeamResponse createTeam(TeamRequest request) {
        String teamName = request.getName().trim();
        if (teamRepository.existsByNameIgnoreCase(teamName)) {
            throw new ConflictException("A team with name '" + teamName + "' already exists!");
        }

        Team team = new Team(
                teamName,
                request.getLogoUrl() != null ? request.getLogoUrl().trim() : "",
                request.getDescription() != null ? request.getDescription().trim() : "",
                request.getCaptainName().trim()
        );
        if (request.getStatus() != null) {
            team.setStatus(request.getStatus());
        }

        Team savedTeam = teamRepository.save(team);
        return teamMapper.toResponse(savedTeam);
    }

    @Override
    @Transactional
    public TeamResponse updateTeam(Long id, TeamRequest request) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", id));

        String newName = request.getName().trim();
        if (!team.getName().equalsIgnoreCase(newName) && teamRepository.existsByNameIgnoreCase(newName)) {
            throw new ConflictException("A team with name '" + newName + "' already exists!");
        }

        team.setName(newName);
        team.setLogoUrl(request.getLogoUrl() != null ? request.getLogoUrl().trim() : "");
        team.setDescription(request.getDescription() != null ? request.getDescription().trim() : "");
        team.setCaptainName(request.getCaptainName().trim());
        if (request.getStatus() != null) {
            team.setStatus(request.getStatus());
        }

        Team updatedTeam = teamRepository.save(team);
        return teamMapper.toResponse(updatedTeam);
    }

    @Override
    @Transactional
    public void deleteTeam(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", id));
        teamRepository.delete(team);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamResponse getTeamById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", id));
        return teamMapper.toResponse(team);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamResponse> getAllActiveTeams() {
        return teamRepository.findByStatus(TeamStatus.ACTIVE).stream()
                .map(teamMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<TeamResponse> getTeams(String query, TeamStatus status, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Team> teamsPage = teamRepository.searchTeams(query, status, pageable);
        List<TeamResponse> content = teamsPage.getContent().stream()
                .map(teamMapper::toResponse)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                content,
                teamsPage.getNumber(),
                teamsPage.getSize(),
                teamsPage.getTotalElements(),
                teamsPage.getTotalPages(),
                teamsPage.isLast()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TeamStatsResponse getTeamStatistics(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", id));

        // Get completed matches involving this team
        List<Match> matches = matchRepository.findAll().stream()
                .filter(m -> m.getStatus() == MatchStatus.COMPLETED &&
                        (m.getTeamA().getId().equals(id) || m.getTeamB().getId().equals(id)))
                .toList();

        int matchesPlayed = matches.size();
        int wins = 0;
        int losses = 0;
        int draws = 0;
        int pointsScored = 0;
        int pointsConceded = 0;

        for (Match m : matches) {
            boolean isTeamA = m.getTeamA().getId().equals(id);
            int scored = isTeamA ? m.getTeamAScore() : m.getTeamBScore();
            int conceded = isTeamA ? m.getTeamBScore() : m.getTeamAScore();

            pointsScored += scored;
            pointsConceded += conceded;

            if (m.getWinner() != null) {
                if (m.getWinner().getId().equals(id)) {
                    wins++;
                } else {
                    losses++;
                }
            } else {
                draws++;
            }
        }

        double winRate = matchesPlayed > 0 ? ((double) wins / matchesPlayed) * 100.0 : 0.0;
        winRate = Math.round(winRate * 10.0) / 10.0; // round to 1 decimal place

        int tournamentsEntered = tournamentTeamRepository.findAll().stream()
                .filter(tt -> tt.getTeam().getId().equals(id))
                .toList().size();

        return new TeamStatsResponse(
                team.getId(),
                team.getName(),
                team.getLogoUrl(),
                matchesPlayed,
                wins,
                losses,
                draws,
                winRate,
                pointsScored,
                pointsConceded,
                tournamentsEntered
        );
    }
}

