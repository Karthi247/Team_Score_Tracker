package com.scoretracker.service;

import com.scoretracker.dto.team.TeamRequest;
import com.scoretracker.dto.team.TeamResponse;
import com.scoretracker.entity.Team;
import com.scoretracker.enums.TeamStatus;
import com.scoretracker.exception.ConflictException;
import com.scoretracker.mapper.TeamMapper;
import com.scoretracker.repository.MatchRepository;
import com.scoretracker.repository.TeamRepository;
import com.scoretracker.repository.TournamentTeamRepository;
import com.scoretracker.service.impl.TeamServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private TournamentTeamRepository tournamentTeamRepository;

    @Mock
    private TeamMapper teamMapper;

    @InjectMocks
    private TeamServiceImpl teamService;

    private Team sampleTeam;

    @BeforeEach
    void setUp() {
        sampleTeam = new Team("Titans FC", "logo.png", "A top team", "Marcus");
        sampleTeam.setId(1L);
    }

    @Test
    @DisplayName("Create team successfully")
    void testCreateTeamSuccess() {
        TeamRequest request = new TeamRequest("Titans FC", "logo.png", "A top team", "Marcus", TeamStatus.ACTIVE);
        when(teamRepository.existsByNameIgnoreCase("Titans FC")).thenReturn(false);
        when(teamRepository.save(any(Team.class))).thenReturn(sampleTeam);

        TeamResponse responseDto = new TeamResponse(1L, "Titans FC", "logo.png", "A top team", "Marcus", TeamStatus.ACTIVE, 0, LocalDateTime.now(), LocalDateTime.now());
        when(teamMapper.toResponse(sampleTeam)).thenReturn(responseDto);

        TeamResponse result = teamService.createTeam(request);

        assertNotNull(result);
        assertEquals("Titans FC", result.getName());
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    @DisplayName("Create team throws ConflictException if name already exists")
    void testCreateTeamDuplicateName() {
        TeamRequest request = new TeamRequest("Titans FC", "logo.png", "A top team", "Marcus", TeamStatus.ACTIVE);
        when(teamRepository.existsByNameIgnoreCase("Titans FC")).thenReturn(true);

        assertThrows(ConflictException.class, () -> teamService.createTeam(request));
        verify(teamRepository, never()).save(any(Team.class));
    }
}

