package com.scoretracker.service.impl;

import com.scoretracker.dto.common.PagedResponse;
import com.scoretracker.dto.player.PlayerRequest;
import com.scoretracker.dto.player.PlayerResponse;
import com.scoretracker.dto.player.PlayerStatsResponse;
import com.scoretracker.entity.Player;
import com.scoretracker.entity.ScoreEvent;
import com.scoretracker.entity.Team;
import com.scoretracker.enums.PlayerStatus;
import com.scoretracker.exception.ConflictException;
import com.scoretracker.exception.ResourceNotFoundException;
import com.scoretracker.mapper.PlayerMapper;
import com.scoretracker.repository.PlayerRepository;
import com.scoretracker.repository.ScoreEventRepository;
import com.scoretracker.repository.TeamRepository;
import com.scoretracker.service.PlayerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final ScoreEventRepository scoreEventRepository;
    private final PlayerMapper playerMapper;

    public PlayerServiceImpl(PlayerRepository playerRepository,
                             TeamRepository teamRepository,
                             ScoreEventRepository scoreEventRepository,
                             PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.scoreEventRepository = scoreEventRepository;
        this.playerMapper = playerMapper;
    }

    @Override
    @Transactional
    public PlayerResponse addPlayer(PlayerRequest request) {
        Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", request.getTeamId()));

        // Prevent duplicate jersey numbers within the same team
        if (playerRepository.existsByTeamIdAndJerseyNumber(team.getId(), request.getJerseyNumber())) {
            throw new ConflictException("Jersey number #" + request.getJerseyNumber() + " is already taken in " + team.getName() + "!");
        }

        Player player = new Player(
                request.getName().trim(),
                request.getJerseyNumber(),
                request.getAge(),
                request.getPosition().trim(),
                team,
                request.getProfileImageUrl() != null ? request.getProfileImageUrl().trim() : "",
                request.getStatus()
        );

        Player savedPlayer = playerRepository.save(player);
        return playerMapper.toResponse(savedPlayer);
    }

    @Override
    @Transactional
    public PlayerResponse updatePlayer(Long id, PlayerRequest request) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player", "id", id));

        Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", request.getTeamId()));

        // Prevent duplicate jersey numbers within the same team (ignoring this player's current id)
        if (playerRepository.existsByTeamIdAndJerseyNumberAndIdNot(team.getId(), request.getJerseyNumber(), id)) {
            throw new ConflictException("Jersey number #" + request.getJerseyNumber() + " is already taken in " + team.getName() + "!");
        }

        player.setName(request.getName().trim());
        player.setJerseyNumber(request.getJerseyNumber());
        player.setAge(request.getAge());
        player.setPosition(request.getPosition().trim());
        player.setTeam(team);
        player.setProfileImageUrl(request.getProfileImageUrl() != null ? request.getProfileImageUrl().trim() : "");
        if (request.getStatus() != null) {
            player.setStatus(request.getStatus());
        }

        Player updatedPlayer = playerRepository.save(player);
        return playerMapper.toResponse(updatedPlayer);
    }

    @Override
    @Transactional
    public void deletePlayer(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player", "id", id));
        playerRepository.delete(player);
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerResponse getPlayerById(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player", "id", id));
        return playerMapper.toResponse(player);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerResponse> getPlayersByTeam(Long teamId) {
        if (!teamRepository.existsById(teamId)) {
            throw new ResourceNotFoundException("Team", "id", teamId);
        }
        return playerRepository.findByTeamId(teamId).stream()
                .map(playerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<PlayerResponse> getPlayers(String query, Long teamId, PlayerStatus status, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Player> playerPage = playerRepository.searchPlayers(query, teamId, status, pageable);
        List<PlayerResponse> content = playerPage.getContent().stream()
                .map(playerMapper::toResponse)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                content,
                playerPage.getNumber(),
                playerPage.getSize(),
                playerPage.getTotalElements(),
                playerPage.getTotalPages(),
                playerPage.isLast()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerStatsResponse getPlayerStatistics(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player", "id", id));

        List<ScoreEvent> events = scoreEventRepository.findAll().stream()
                .filter(se -> se.getPlayer() != null && se.getPlayer().getId().equals(id))
                .toList();

        int scoreEventsCount = events.size();
        int totalPoints = events.stream().mapToInt(ScoreEvent::getPoints).sum();

        return new PlayerStatsResponse(
                player.getId(),
                player.getName(),
                player.getJerseyNumber(),
                player.getPosition(),
                player.getTeam().getId(),
                player.getTeam().getName(),
                scoreEventsCount,
                totalPoints
        );
    }
}

