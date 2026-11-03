package com.scoretracker.service.impl;

import com.scoretracker.dto.match.MatchLiveScoreResponse;
import com.scoretracker.dto.score.ScoreEventRequest;
import com.scoretracker.dto.score.ScoreEventResponse;
import com.scoretracker.entity.*;
import com.scoretracker.enums.MatchStatus;
import com.scoretracker.enums.ScoreEventType;
import com.scoretracker.exception.BadRequestException;
import com.scoretracker.exception.ResourceNotFoundException;
import com.scoretracker.mapper.MatchMapper;
import com.scoretracker.mapper.ScoreEventMapper;
import com.scoretracker.repository.*;
import com.scoretracker.security.UserPrincipal;
import com.scoretracker.service.ScoreService;
import com.scoretracker.service.StandingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
public class ScoreServiceImpl implements ScoreService {

    private static final Logger logger = LoggerFactory.getLogger(ScoreServiceImpl.class);

    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;
    private final ScoreEventRepository scoreEventRepository;
    private final UserRepository userRepository;
    private final StandingService standingService;
    private final MatchMapper matchMapper;
    private final ScoreEventMapper scoreEventMapper;
    private final SimpMessagingTemplate messagingTemplate;

    // SSE emitter registry per match ID
    private final Map<Long, List<SseEmitter>> sseEmitters = new ConcurrentHashMap<>();

    public ScoreServiceImpl(MatchRepository matchRepository,
                            TeamRepository teamRepository,
                            PlayerRepository playerRepository,
                            ScoreEventRepository scoreEventRepository,
                            UserRepository userRepository,
                            StandingService standingService,
                            MatchMapper matchMapper,
                            ScoreEventMapper scoreEventMapper,
                            SimpMessagingTemplate messagingTemplate) {
        this.matchRepository = matchRepository;
        this.playerRepository = playerRepository;
        this.scoreEventRepository = scoreEventRepository;
        this.userRepository = userRepository;
        this.standingService = standingService;
        this.matchMapper = matchMapper;
        this.scoreEventMapper = scoreEventMapper;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    @Transactional
    public MatchLiveScoreResponse addScoreEvent(Long matchId, ScoreEventRequest request, UserPrincipal currentUser) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", matchId));

        if (match.getStatus() == MatchStatus.CANCELLED) {
            throw new BadRequestException("Cannot add scores to a cancelled match!");
        }

        // Validate team belongs to this match
        boolean isTeamA = match.getTeamA().getId().equals(request.getTeamId());
        boolean isTeamB = match.getTeamB().getId().equals(request.getTeamId());

        if (!isTeamA && !isTeamB) {
            throw new BadRequestException("Team " + request.getTeamId() + " is not playing in this match!");
        }

        Team scoringTeam = isTeamA ? match.getTeamA() : match.getTeamB();

        Player player = null;
        if (request.getPlayerId() != null) {
            player = playerRepository.findById(request.getPlayerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Player", "id", request.getPlayerId()));
            if (!player.getTeam().getId().equals(scoringTeam.getId())) {
                throw new BadRequestException("Player does not belong to team " + scoringTeam.getName() + "!");
            }
        }

        User user = null;
        if (currentUser != null) {
            user = userRepository.findById(currentUser.getId()).orElse(null);
        }

        ScoreEventType eventType = request.getEventType() != null ? request.getEventType() :
                (request.getPoints() < 0 ? ScoreEventType.SUBTRACT : ScoreEventType.POINT);

        String minuteOrPeriod = request.getMinuteOrPeriod();
        if (minuteOrPeriod == null || minuteOrPeriod.isBlank()) {
            minuteOrPeriod = match.getCurrentPeriod() != null ? match.getCurrentPeriod() : "Live";
        }

        String description = request.getDescription();
        if (description == null || description.isBlank()) {
            String scorer = player != null ? player.getName() + " (#" + player.getJerseyNumber() + ")" : scoringTeam.getName();
            description = (request.getPoints() >= 0 ? "+" : "") + request.getPoints() + " points for " + scorer;
        }

        ScoreEvent scoreEvent = new ScoreEvent(
                match,
                scoringTeam,
                player,
                request.getPoints(),
                eventType,
                minuteOrPeriod,
                description,
                user
        );
        scoreEventRepository.save(scoreEvent);

        // Update match status to LIVE if UPCOMING
        if (match.getStatus() == MatchStatus.UPCOMING) {
            match.setStatus(MatchStatus.LIVE);
        }

        // Update score
        if (isTeamA) {
            int newScore = Math.max(0, match.getTeamAScore() + request.getPoints());
            match.setTeamAScore(newScore);
        } else {
            int newScore = Math.max(0, match.getTeamBScore() + request.getPoints());
            match.setTeamBScore(newScore);
        }

        Match updatedMatch = matchRepository.save(match);

        // If tournament and match is COMPLETED, recalculate standings
        if (updatedMatch.getTournament() != null && updatedMatch.getStatus() == MatchStatus.COMPLETED) {
            standingService.recalculateStandings(updatedMatch.getTournament().getId());
        }

        MatchLiveScoreResponse liveResponse = matchMapper.toLiveScoreResponse(updatedMatch);

        // Broadcast to WebSocket subscribers
        broadcastScoreUpdate(matchId, liveResponse);

        return liveResponse;
    }

    @Override
    @Transactional
    public MatchLiveScoreResponse deleteScoreEvent(Long matchId, Long scoreEventId, UserPrincipal currentUser) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", matchId));

        ScoreEvent event = scoreEventRepository.findById(scoreEventId)
                .orElseThrow(() -> new ResourceNotFoundException("ScoreEvent", "id", scoreEventId));

        if (!event.getMatch().getId().equals(matchId)) {
            throw new BadRequestException("Score event does not belong to this match!");
        }

        // Revert points
        if (match.getTeamA().getId().equals(event.getTeam().getId())) {
            match.setTeamAScore(Math.max(0, match.getTeamAScore() - event.getPoints()));
        } else if (match.getTeamB().getId().equals(event.getTeam().getId())) {
            match.setTeamBScore(Math.max(0, match.getTeamBScore() - event.getPoints()));
        }

        scoreEventRepository.delete(event);
        Match updatedMatch = matchRepository.save(match);

        MatchLiveScoreResponse liveResponse = matchMapper.toLiveScoreResponse(updatedMatch);
        broadcastScoreUpdate(matchId, liveResponse);

        return liveResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScoreEventResponse> getScoreEvents(Long matchId) {
        if (!matchRepository.existsById(matchId)) {
            throw new ResourceNotFoundException("Match", "id", matchId);
        }

        return scoreEventRepository.findByMatchIdOrderByTimestampDesc(matchId).stream()
                .map(scoreEventMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MatchLiveScoreResponse getLiveMatchScore(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", matchId));
        return matchMapper.toLiveScoreResponse(match);
    }

    @Override
    public SseEmitter registerSseClient(Long matchId) {
        SseEmitter emitter = new SseEmitter(1800000L); // 30 minutes timeout
        List<SseEmitter> emitters = sseEmitters.computeIfAbsent(matchId, k -> new CopyOnWriteArrayList<>());
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));

        // Send initial state immediately
        try {
            MatchLiveScoreResponse current = getLiveMatchScore(matchId);
            emitter.send(SseEmitter.event().name("SCORE_UPDATE").data(current));
        } catch (Exception ex) {
            emitters.remove(emitter);
        }

        return emitter;
    }

    private void broadcastScoreUpdate(Long matchId, MatchLiveScoreResponse liveResponse) {
        // 1. WebSocket STOMP broadcast
        try {
            messagingTemplate.convertAndSend("/topic/match/" + matchId, liveResponse);
        } catch (Exception ex) {
            logger.error("Failed to send WebSocket score update for match {}", matchId, ex);
        }

        // 2. Server-Sent Events (SSE) broadcast
        List<SseEmitter> emitters = sseEmitters.get(matchId);
        if (emitters != null && !emitters.isEmpty()) {
            List<SseEmitter> deadEmitters = new ArrayList<>();
            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event().name("SCORE_UPDATE").data(liveResponse));
                } catch (IOException e) {
                    deadEmitters.add(emitter);
                }
            }
            emitters.removeAll(deadEmitters);
        }
    }
}

