package com.scoretracker.dto.score;

import com.scoretracker.enums.ScoreEventType;
import java.time.LocalDateTime;

public class ScoreEventResponse {
    private Long id;
    private Long matchId;
    private Long teamId;
    private String teamName;
    private Long playerId;
    private String playerName;
    private Integer points;
    private ScoreEventType eventType;
    private String minuteOrPeriod;
    private String description;
    private String createdByName;
    private LocalDateTime timestamp;

    public ScoreEventResponse() {
    }

    public ScoreEventResponse(Long id, Long matchId, Long teamId, String teamName,
                              Long playerId, String playerName, Integer points,
                              ScoreEventType eventType, String minuteOrPeriod,
                              String description, String createdByName, LocalDateTime timestamp) {
        this.id = id;
        this.matchId = matchId;
        this.teamId = teamId;
        this.teamName = teamName;
        this.playerId = playerId;
        this.playerName = playerName;
        this.points = points;
        this.eventType = eventType;
        this.minuteOrPeriod = minuteOrPeriod;
        this.description = description;
        this.createdByName = createdByName;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
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

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

