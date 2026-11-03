package com.scoretracker.service;

import com.scoretracker.dto.player.PlayerRequest;
import com.scoretracker.dto.player.PlayerResponse;
import com.scoretracker.entity.Player;
import com.scoretracker.entity.Team;
import com.scoretracker.enums.PlayerStatus;
import com.scoretracker.exception.ConflictException;
import com.scoretracker.mapper.PlayerMapper;
import com.scoretracker.repository.PlayerRepository;
import com.scoretracker.repository.ScoreEventRepository;
import com.scoretracker.repository.TeamRepository;
import com.scoretracker.service.impl.PlayerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private ScoreEventRepository scoreEventRepository;

    @Mock
    private PlayerMapper playerMapper;

    @InjectMocks
    private PlayerServiceImpl playerService;

    private Team sampleTeam;
    private Player samplePlayer;

    @BeforeEach
    void setUp() {
        sampleTeam = new Team("Titans FC", "logo.png", "Top team", "Marcus");
        sampleTeam.setId(1L);

        samplePlayer = new Player("Marcus Sterling", 9, 27, "Forward", sampleTeam, "", PlayerStatus.ACTIVE);
        samplePlayer.setId(10L);
    }

    @Test
    @DisplayName("Add player successfully")
    void testAddPlayerSuccess() {
        PlayerRequest request = new PlayerRequest("Marcus Sterling", 9, 27, "Forward", 1L, "", PlayerStatus.ACTIVE);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(sampleTeam));
        when(playerRepository.existsByTeamIdAndJerseyNumber(1L, 9)).thenReturn(false);
        when(playerRepository.save(any(Player.class))).thenReturn(samplePlayer);

        PlayerResponse responseDto = new PlayerResponse(10L, "Marcus Sterling", 9, 27, "Forward", 1L, "Titans FC", "", PlayerStatus.ACTIVE, LocalDateTime.now());
        when(playerMapper.toResponse(samplePlayer)).thenReturn(responseDto);

        PlayerResponse result = playerService.addPlayer(request);

        assertNotNull(result);
        assertEquals(9, result.getJerseyNumber());
        assertEquals("Marcus Sterling", result.getName());
    }

    @Test
    @DisplayName("Add player fails if jersey number is already taken in the same team")
    void testAddPlayerDuplicateJersey() {
        PlayerRequest request = new PlayerRequest("Another Player", 9, 22, "Forward", 1L, "", PlayerStatus.ACTIVE);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(sampleTeam));
        when(playerRepository.existsByTeamIdAndJerseyNumber(1L, 9)).thenReturn(true);

        assertThrows(ConflictException.class, () -> playerService.addPlayer(request));
        verify(playerRepository, never()).save(any(Player.class));
    }
}

