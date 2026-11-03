package com.scoretracker.service.impl;

import com.scoretracker.dto.common.PagedResponse;
import com.scoretracker.dto.match.MatchLiveScoreResponse;
import com.scoretracker.dto.match.MatchRequest;
import com.scoretracker.dto.match.MatchResponse;
import com.scoretracker.dto.match.MatchStatusUpdateRequest;
import com.scoretracker.entity.Match;
import com.scoretracker.entity.Team;
import com.scoretracker.entity.Tournament;
import com.scoretracker.enums.MatchStatus;
import com.scoretracker.exception.BadRequestException;
import com.scoretracker.exception.ResourceNotFoundException;
import com.scoretracker.mapper.MatchMapper;
import com.scoretracker.repository.MatchRepository;
import com.scoretracker.repository.TeamRepository;
import com.scoretracker.repository.TournamentRepository;
import com.scoretracker.service.MatchService;
import com.scoretracker.service.StandingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final StandingService standingService;
    private final MatchMapper matchMapper;
    private final SimpMessagingTemplate messagingTemplate;

    public MatchServiceImpl(MatchRepository matchRepository,
                            TournamentRepository tournamentRepository,
                            TeamRepository teamRepository,
                            StandingService standingService,
                            MatchMapper matchMapper,
                            SimpMessagingTemplate messagingTemplate) {
        this.matchRepository = matchRepository;
        this.tournamentRepository = tournamentRepository;
        this.teamRepository = teamRepository;
        this.standingService = standingService;
        this.matchMapper = matchMapper;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    @Transactional
    public MatchResponse scheduleMatch(MatchRequest request) {
        if (request.getTeamAId().equals(request.getTeamBId())) {
            throw new BadRequestException("A team cannot play against itself!");
        }

        Team teamA = teamRepository.findById(request.getTeamAId())
                .orElseThrow(() -> new ResourceNotFoundException("Team A", "id", request.getTeamAId()));

        Team teamB = teamRepository.findById(request.getTeamBId())
                .orElseThrow(() -> new ResourceNotFoundException("Team B", "id", request.getTeamBId()));

        Tournament tournament = null;
        if (request.getTournamentId() != null) {
            tournament = tournamentRepository.findById(request.getTournamentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tournament", "id", request.getTournamentId()));
        }

        String venue = request.getVenue();
        if ((venue == null || venue.isBlank()) && tournament != null) {
            venue = tournament.getVenue();
        }

        Match match = new Match(
                tournament,
                teamA,
                teamB,
                request.getMatchDate(),
                request.getMatchTime(),
                venue != null ? venue.trim() : "",
                request.getRoundName() != null ? request.getRoundName().trim() : "Regular Match"
        );

        Match savedMatch = matchRepository.save(match);
        return matchMapper.toResponse(savedMatch);
    }

    @Override
    @Transactional
    public MatchResponse updateMatch(Long id, MatchRequest request) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", id));

        if (request.getTeamAId().equals(request.getTeamBId())) {
            throw new BadRequestException("A team cannot play against itself!");
        }

        Team teamA = teamRepository.findById(request.getTeamAId())
                .orElseThrow(() -> new ResourceNotFoundException("Team A", "id", request.getTeamAId()));

        Team teamB = teamRepository.findById(request.getTeamBId())
                .orElseThrow(() -> new ResourceNotFoundException("Team B", "id", request.getTeamBId()));

        Tournament tournament = null;
        if (request.getTournamentId() != null) {
            tournament = tournamentRepository.findById(request.getTournamentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tournament", "id", request.getTournamentId()));
        }

        match.setTournament(tournament);
        match.setTeamA(teamA);
        match.setTeamB(teamB);
        match.setMatchDate(request.getMatchDate());
        match.setMatchTime(request.getMatchTime());
        match.setVenue(request.getVenue() != null ? request.getVenue().trim() : match.getVenue());
        match.setRoundName(request.getRoundName() != null ? request.getRoundName().trim() : match.getRoundName());

        Match updated = matchRepository.save(match);
        return matchMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteMatch(Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", id));
        matchRepository.delete(match);
    }

    @Override
    @Transactional(readOnly = true)
    public MatchResponse getMatchById(Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", id));
        return matchMapper.toResponse(match);
    }

    @Override
    @Transactional(readOnly = true)
    public MatchLiveScoreResponse getLiveMatchDetails(Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", id));
        return matchMapper.toLiveScoreResponse(match);
    }

    @Override
    @Transactional
    public MatchResponse startMatch(Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", id));

        match.setStatus(MatchStatus.LIVE);
        if (match.getCurrentPeriod() == null || match.getCurrentPeriod().equalsIgnoreCase("Scheduled")) {
            match.setCurrentPeriod("1st Half / Q1");
        }

        Match updated = matchRepository.save(match);
        MatchLiveScoreResponse liveResponse = matchMapper.toLiveScoreResponse(updated);
        messagingTemplate.convertAndSend("/topic/match/" + id, liveResponse);

        return matchMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public MatchResponse completeMatch(Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", id));

        match.setStatus(MatchStatus.COMPLETED);
        match.setCurrentPeriod("Full Time / Ended");

        // Automatically calculate the winner
        if (match.getTeamAScore() > match.getTeamBScore()) {
            match.setWinner(match.getTeamA());
        } else if (match.getTeamBScore() > match.getTeamAScore()) {
            match.setWinner(match.getTeamB());
        } else {
            match.setWinner(null); // Draw
        }

        Match updated = matchRepository.save(match);

        // If part of tournament, update standings!
        if (match.getTournament() != null) {
            standingService.recalculateStandings(match.getTournament().getId());
        }

        MatchLiveScoreResponse liveResponse = matchMapper.toLiveScoreResponse(updated);
        messagingTemplate.convertAndSend("/topic/match/" + id, liveResponse);

        return matchMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public MatchResponse cancelMatch(Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", id));

        match.setStatus(MatchStatus.CANCELLED);
        match.setCurrentPeriod("Cancelled");

        Match updated = matchRepository.save(match);
        MatchLiveScoreResponse liveResponse = matchMapper.toLiveScoreResponse(updated);
        messagingTemplate.convertAndSend("/topic/match/" + id, liveResponse);

        return matchMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public MatchResponse updateMatchStatus(Long id, MatchStatusUpdateRequest request) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", id));

        if (request.getStatus() != null) {
            match.setStatus(request.getStatus());
        }
        if (request.getCurrentPeriod() != null) {
            match.setCurrentPeriod(request.getCurrentPeriod());
        }
        if (request.getElapsedMinutes() != null) {
            match.setElapsedMinutes(request.getElapsedMinutes());
        }

        Match updated = matchRepository.save(match);
        MatchLiveScoreResponse liveResponse = matchMapper.toLiveScoreResponse(updated);
        messagingTemplate.convertAndSend("/topic/match/" + id, liveResponse);

        return matchMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<MatchResponse> getMatches(Long tournamentId, MatchStatus status, LocalDate date, Long teamId,
                                                   int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Match> matchPage = matchRepository.searchMatches(tournamentId, status, date, teamId, pageable);
        List<MatchResponse> content = matchPage.getContent().stream()
                .map(matchMapper::toResponse)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                content,
                matchPage.getNumber(),
                matchPage.getSize(),
                matchPage.getTotalElements(),
                matchPage.getTotalPages(),
                matchPage.isLast()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchResponse> getLiveMatches() {
        return matchRepository.findLiveMatches().stream()
                .map(matchMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchResponse> getRecentCompletedMatches(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return matchRepository.findRecentCompletedMatches(pageable).stream()
                .map(matchMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchResponse> getUpcomingMatches(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return matchRepository.findUpcomingMatches(pageable).stream()
                .map(matchMapper::toResponse)
                .collect(Collectors.toList());
    }
}

