package com.scoretracker.service.impl;

import com.scoretracker.dto.common.PagedResponse;
import com.scoretracker.dto.match.MatchResponse;
import com.scoretracker.dto.team.TeamResponse;
import com.scoretracker.dto.tournament.*;
import com.scoretracker.entity.*;
import com.scoretracker.enums.MatchStatus;
import com.scoretracker.enums.SportType;
import com.scoretracker.enums.TournamentFormat;
import com.scoretracker.enums.TournamentStatus;
import com.scoretracker.exception.BadRequestException;
import com.scoretracker.exception.ConflictException;
import com.scoretracker.exception.ResourceNotFoundException;
import com.scoretracker.mapper.MatchMapper;
import com.scoretracker.mapper.TeamMapper;
import com.scoretracker.mapper.TournamentMapper;
import com.scoretracker.repository.*;
import com.scoretracker.service.TournamentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final TournamentTeamRepository tournamentTeamRepository;
    private final MatchRepository matchRepository;
    private final TeamStandingRepository teamStandingRepository;
    private final TournamentMapper tournamentMapper;
    private final TeamMapper teamMapper;
    private final MatchMapper matchMapper;

    public TournamentServiceImpl(TournamentRepository tournamentRepository,
                                 TeamRepository teamRepository,
                                 TournamentTeamRepository tournamentTeamRepository,
                                 MatchRepository matchRepository,
                                 TeamStandingRepository teamStandingRepository,
                                 TournamentMapper tournamentMapper,
                                 TeamMapper teamMapper,
                                 MatchMapper matchMapper) {
        this.tournamentRepository = tournamentRepository;
        this.teamRepository = teamRepository;
        this.tournamentTeamRepository = tournamentTeamRepository;
        this.matchRepository = matchRepository;
        this.teamStandingRepository = teamStandingRepository;
        this.tournamentMapper = tournamentMapper;
        this.teamMapper = teamMapper;
        this.matchMapper = matchMapper;
    }

    @Override
    @Transactional
    public TournamentResponse createTournament(TournamentRequest request) {
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BadRequestException("Tournament End Date cannot be before Start Date!");
        }

        Tournament tournament = new Tournament(
                request.getName().trim(),
                request.getSportType(),
                request.getDescription() != null ? request.getDescription().trim() : "",
                request.getStartDate(),
                request.getEndDate(),
                request.getVenue() != null ? request.getVenue().trim() : "",
                request.getMaxTeams(),
                request.getStatus(),
                request.getFormat(),
                request.getWinPoints(),
                request.getDrawPoints(),
                request.getLossPoints()
        );

        Tournament saved = tournamentRepository.save(tournament);
        return tournamentMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public TournamentResponse updateTournament(Long id, TournamentRequest request) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament", "id", id));

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BadRequestException("Tournament End Date cannot be before Start Date!");
        }

        tournament.setName(request.getName().trim());
        tournament.setSportType(request.getSportType());
        tournament.setDescription(request.getDescription() != null ? request.getDescription().trim() : "");
        tournament.setStartDate(request.getStartDate());
        tournament.setEndDate(request.getEndDate());
        tournament.setVenue(request.getVenue() != null ? request.getVenue().trim() : "");
        tournament.setMaxTeams(request.getMaxTeams());
        if (request.getStatus() != null) {
            tournament.setStatus(request.getStatus());
        }
        if (request.getFormat() != null) {
            tournament.setFormat(request.getFormat());
        }
        if (request.getWinPoints() != null) {
            tournament.setWinPoints(request.getWinPoints());
        }
        if (request.getDrawPoints() != null) {
            tournament.setDrawPoints(request.getDrawPoints());
        }
        if (request.getLossPoints() != null) {
            tournament.setLossPoints(request.getLossPoints());
        }

        Tournament updated = tournamentRepository.save(tournament);
        return tournamentMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteTournament(Long id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament", "id", id));
        tournamentRepository.delete(tournament);
    }

    @Override
    @Transactional(readOnly = true)
    public TournamentResponse getTournamentById(Long id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament", "id", id));
        return tournamentMapper.toResponse(tournament);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<TournamentResponse> getTournaments(String query, TournamentStatus status, SportType sportType,
                                                            int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Tournament> tournamentPage = tournamentRepository.searchTournaments(query, status, sportType, pageable);
        List<TournamentResponse> content = tournamentPage.getContent().stream()
                .map(tournamentMapper::toResponse)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                content,
                tournamentPage.getNumber(),
                tournamentPage.getSize(),
                tournamentPage.getTotalElements(),
                tournamentPage.getTotalPages(),
                tournamentPage.isLast()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamResponse> getRegisteredTeams(Long tournamentId) {
        if (!tournamentRepository.existsById(tournamentId)) {
            throw new ResourceNotFoundException("Tournament", "id", tournamentId);
        }

        return tournamentTeamRepository.findByTournamentId(tournamentId).stream()
                .map(tt -> teamMapper.toResponse(tt.getTeam()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void registerTeam(Long tournamentId, RegisterTeamRequest request) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament", "id", tournamentId));

        Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", request.getTeamId()));

        if (tournamentTeamRepository.existsByTournamentIdAndTeamId(tournamentId, team.getId())) {
            throw new ConflictException("Team '" + team.getName() + "' is already registered in this tournament!");
        }

        if (tournament.getMaxTeams() != null && tournament.getTournamentTeams().size() >= tournament.getMaxTeams()) {
            throw new BadRequestException("Tournament has reached the maximum capacity of " + tournament.getMaxTeams() + " teams!");
        }

        TournamentTeam tournamentTeam = new TournamentTeam(tournament, team, request.getSeedNumber());
        tournamentTeamRepository.save(tournamentTeam);

        // Also initialise a TeamStanding entry if not present
        if (!teamStandingRepository.findByTournamentIdAndTeamId(tournamentId, team.getId()).isPresent()) {
            TeamStanding standing = new TeamStanding(tournament, team);
            teamStandingRepository.save(standing);
        }
    }

    @Override
    @Transactional
    public void removeRegisteredTeam(Long tournamentId, Long teamId) {
        TournamentTeam tournamentTeam = tournamentTeamRepository.findByTournamentIdAndTeamId(tournamentId, teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found for team " + teamId + " in tournament " + tournamentId));

        tournamentTeamRepository.delete(tournamentTeam);

        teamStandingRepository.findByTournamentIdAndTeamId(tournamentId, teamId)
                .ifPresent(teamStandingRepository::delete);
    }

    @Override
    @Transactional
    public List<MatchResponse> generateFixtures(Long tournamentId, FixtureGenerateRequest request) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament", "id", tournamentId));

        List<TournamentTeam> registered = tournamentTeamRepository.findByTournamentId(tournamentId);
        if (registered.size() < 2) {
            throw new BadRequestException("At least 2 teams must be registered to generate fixtures!");
        }

        List<Team> teams = registered.stream().map(TournamentTeam::getTeam).collect(Collectors.toList());
        List<Match> generatedMatches = new ArrayList<>();

        TournamentFormat format = (request != null && request.getFormat() != null) ? request.getFormat() : tournament.getFormat();
        LocalDate startDate = tournament.getStartDate().isAfter(LocalDate.now()) ? tournament.getStartDate() : LocalDate.now().plusDays(1);
        int dayOffset = 0;
        int matchHour = 14; // Start at 2:00 PM

        if (format == TournamentFormat.KNOCKOUT) {
            // Seeded or shuffled bracket
            Collections.shuffle(teams);
            int roundMatchNum = 1;
            for (int i = 0; i < teams.size() - 1; i += 2) {
                Team teamA = teams.get(i);
                Team teamB = teams.get(i + 1);

                String roundName = teams.size() <= 4 ? "Semifinal " + roundMatchNum :
                                   teams.size() <= 8 ? "Quarterfinal " + roundMatchNum : "Round 1 Match " + roundMatchNum;

                LocalDate matchDate = startDate.plusDays(dayOffset);
                LocalTime matchTime = LocalTime.of(matchHour % 24, 0);

                Match match = new Match(tournament, teamA, teamB, matchDate, matchTime, tournament.getVenue(), roundName);
                generatedMatches.add(match);

                matchHour += 2;
                if (matchHour >= 20) {
                    matchHour = 14;
                    dayOffset++;
                }
                roundMatchNum++;
            }
        } else {
            // ROUND_ROBIN or LEAGUE (Circle method)
            List<Team> rotation = new ArrayList<>(teams);
            boolean hasGhost = false;
            if (rotation.size() % 2 != 0) {
                rotation.add(null); // dummy bye
                hasGhost = true;
            }

            int numTeams = rotation.size();
            int totalRounds = numTeams - 1;
            int matchesPerRound = numTeams / 2;

            for (int round = 0; round < totalRounds; round++) {
                String roundName = "Matchday " + (round + 1);
                LocalDate matchDate = startDate.plusDays(dayOffset);

                for (int matchIdx = 0; matchIdx < matchesPerRound; matchIdx++) {
                    Team teamA = rotation.get(matchIdx);
                    Team teamB = rotation.get(numTeams - 1 - matchIdx);

                    // Skip byes
                    if (teamA != null && teamB != null) {
                        LocalTime matchTime = LocalTime.of(matchHour % 24, 0);
                        Match match = new Match(tournament, teamA, teamB, matchDate, matchTime, tournament.getVenue(), roundName);
                        generatedMatches.add(match);

                        matchHour += 2;
                        if (matchHour >= 20) {
                            matchHour = 14;
                            dayOffset++;
                        }
                    }
                }

                // Rotate teams: fix index 0, rotate rest
                Team lastTeam = rotation.remove(numTeams - 1);
                rotation.add(1, lastTeam);
                dayOffset++; // next matchday
                matchHour = 14;
            }
        }

        List<Match> savedMatches = matchRepository.saveAll(generatedMatches);

        // Update tournament status to ONGOING if currently UPCOMING
        if (tournament.getStatus() == TournamentStatus.UPCOMING) {
            tournament.setStatus(TournamentStatus.ONGOING);
            tournamentRepository.save(tournament);
        }

        return savedMatches.stream().map(matchMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TournamentStatsResponse getTournamentStatistics(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament", "id", tournamentId));

        List<Match> matches = matchRepository.findByTournamentId(tournamentId);

        int totalMatches = matches.size();
        int completedMatches = 0;
        int liveMatches = 0;
        int upcomingMatches = 0;
        int totalPointsScored = 0;

        for (Match m : matches) {
            if (m.getStatus() == MatchStatus.COMPLETED) {
                completedMatches++;
                totalPointsScored += (m.getTeamAScore() + m.getTeamBScore());
            } else if (m.getStatus() == MatchStatus.LIVE) {
                liveMatches++;
                totalPointsScored += (m.getTeamAScore() + m.getTeamBScore());
            } else if (m.getStatus() == MatchStatus.UPCOMING) {
                upcomingMatches++;
            }
        }

        double avgPoints = completedMatches > 0 ? (double) totalPointsScored / completedMatches : 0.0;
        avgPoints = Math.round(avgPoints * 10.0) / 10.0;

        // Find leading team from standings
        List<TeamStanding> standings = teamStandingRepository.findByTournamentIdOrderByStandings(tournamentId);
        String leadingTeamName = standings.isEmpty() ? "None" : standings.get(0).getTeam().getName();
        Long leadingTeamId = standings.isEmpty() ? null : standings.get(0).getTeam().getId();

        return new TournamentStatsResponse(
                tournament.getId(),
                tournament.getName(),
                totalMatches,
                completedMatches,
                liveMatches,
                upcomingMatches,
                totalPointsScored,
                avgPoints,
                leadingTeamName,
                leadingTeamId
        );
    }
}

