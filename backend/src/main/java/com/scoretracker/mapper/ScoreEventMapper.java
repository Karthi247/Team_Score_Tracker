package com.scoretracker.mapper;

import com.scoretracker.dto.score.ScoreEventResponse;
import com.scoretracker.entity.ScoreEvent;
import org.springframework.stereotype.Component;

@Component
public class ScoreEventMapper {

    public ScoreEventResponse toResponse(ScoreEvent event) {
        if (event == null) {
            return null;
        }

        Long matchId = event.getMatch() != null ? event.getMatch().getId() : null;
        Long teamId = event.getTeam() != null ? event.getTeam().getId() : null;
        String teamName = event.getTeam() != null ? event.getTeam().getName() : null;
        Long playerId = event.getPlayer() != null ? event.getPlayer().getId() : null;
        String playerName = event.getPlayer() != null ? event.getPlayer().getName() : null;
        String createdByName = event.getCreatedBy() != null ? event.getCreatedBy().getFullName() : "Referee / Official";

        return new ScoreEventResponse(
                event.getId(),
                matchId,
                teamId,
                teamName,
                playerId,
                playerName,
                event.getPoints(),
                event.getEventType(),
                event.getMinuteOrPeriod(),
                event.getDescription(),
                createdByName,
                event.getTimestamp()
        );
    }
}

