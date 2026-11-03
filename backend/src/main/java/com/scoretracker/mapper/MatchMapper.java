package com.scoretracker.mapper;

import com.scoretracker.dto.match.MatchLiveScoreResponse;
import com.scoretracker.dto.match.MatchResponse;
import com.scoretracker.dto.score.ScoreEventResponse;
import com.scoretracker.entity.Match;
import com.scoretracker.enums.SportType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MatchMapper {

    private final ScoreEventMapper scoreEventMapper;

    public MatchMapper(ScoreEventMapper scoreEventMapper) {
        this.scoreEventMapper = scoreEventMapper;
    }

    public MatchResponse toResponse(Match match) {
        if (match == null) {
            return null;
        }

        Long tournamentId = match.getTournament() != null ? match.getTournament().getId() : null;
        String tournamentName = match.getTournament() != null ? match.getTournament().getName() : null;
        SportType sportType = match.getTournament() != null ? match.getTournament().getSportType() : SportType.CUSTOM;

        Long teamAId = match.getTeamA() != null ? match.getTeamA().getId() : null;
        String teamAName = match.getTeamA() != null ? match.getTeamA().getName() : "Team A";
        String teamALogo = match.getTeamA() != null ? match.getTeamA().getLogoUrl() : null;

        Long teamBId = match.getTeamB() != null ? match.getTeamB().getId() : null;
        String teamBName = match.getTeamB() != null ? match.getTeamB().getName() : "Team B";
        String teamBLogo = match.getTeamB() != null ? match.getTeamB().getLogoUrl() : null;

        Long winnerId = match.getWinner() != null ? match.getWinner().getId() : null;
        String winnerName = match.getWinner() != null ? match.getWinner().getName() : null;

        return new MatchResponse(
                match.getId(),
                tournamentId,
                tournamentName,
                sportType,
                teamAId,
                teamAName,
                teamALogo,
                teamBId,
                teamBName,
                teamBLogo,
                match.getMatchDate(),
                match.getMatchTime(),
                match.getVenue(),
                match.getStatus(),
                match.getTeamAScore(),
                match.getTeamBScore(),
                winnerId,
                winnerName,
                match.getRoundName(),
                match.getCurrentPeriod(),
                match.getElapsedMinutes(),
                match.getCreatedAt(),
                match.getUpdatedAt()
        );
    }

    public MatchLiveScoreResponse toLiveScoreResponse(Match match) {
        if (match == null) {
            return null;
        }

        Long tournamentId = match.getTournament() != null ? match.getTournament().getId() : null;
        String tournamentName = match.getTournament() != null ? match.getTournament().getName() : null;
        SportType sportType = match.getTournament() != null ? match.getTournament().getSportType() : SportType.CUSTOM;

        Long teamAId = match.getTeamA() != null ? match.getTeamA().getId() : null;
        String teamAName = match.getTeamA() != null ? match.getTeamA().getName() : "Team A";
        String teamALogo = match.getTeamA() != null ? match.getTeamA().getLogoUrl() : null;

        Long teamBId = match.getTeamB() != null ? match.getTeamB().getId() : null;
        String teamBName = match.getTeamB() != null ? match.getTeamB().getName() : "Team B";
        String teamBLogo = match.getTeamB() != null ? match.getTeamB().getLogoUrl() : null;

        Long winnerId = match.getWinner() != null ? match.getWinner().getId() : null;
        String winnerName = match.getWinner() != null ? match.getWinner().getName() : null;

        List<ScoreEventResponse> eventResponses = match.getScoreEvents() != null ?
                match.getScoreEvents().stream().map(scoreEventMapper::toResponse).collect(Collectors.toList()) :
                new ArrayList<>();

        return new MatchLiveScoreResponse(
                match.getId(),
                tournamentId,
                tournamentName,
                sportType,
                teamAId,
                teamAName,
                teamALogo,
                teamBId,
                teamBName,
                teamBLogo,
                match.getTeamAScore(),
                match.getTeamBScore(),
                match.getStatus(),
                match.getCurrentPeriod(),
                match.getElapsedMinutes(),
                winnerId,
                winnerName,
                match.getMatchDate(),
                match.getMatchTime(),
                match.getVenue(),
                eventResponses
        );
    }
}

