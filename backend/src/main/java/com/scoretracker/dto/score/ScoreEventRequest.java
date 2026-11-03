package com.scoretracker.dto.score;

import com.scoretracker.enums.ScoreEventType;
import jakarta.validation.constraints.NotNull;

public class ScoreEventRequest {
    @NotNull(message = "Team ID is required")
    private Long teamId;

    private Long playerId;

    @NotNull(message = "Points value is required")
    private Integer points;

    private ScoreEventType eventType;
    private String minuteOrPeriod;
    private String description;

    public ScoreEventRequest() {
    }

    public ScoreEventRequest(Long teamId, Long playerId, Integer points,
                             ScoreEventType eventType, String minuteOrPeriod, String description) {
        this.teamId = teamId;
        this.playerId = playerId;
        this.points = points;
        this.eventType = eventType;
        this.minuteOrPeriod = minuteOrPeriod;
        this.description = description;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public ScoreEventType getEventType() {
        return eventType;
    }

    public void setEventType(ScoreEventType eventType) {
        this.eventType = eventType;
    }

    public String getMinuteOrPeriod() {
        return minuteOrPeriod;
    }

    public void setMinuteOrPeriod(String minuteOrPeriod) {
        this.minuteOrPeriod = minuteOrPeriod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

