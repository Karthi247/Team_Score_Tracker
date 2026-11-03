package com.scoretracker.dto.player;

public class PlayerStatsResponse {
    private Long playerId;
    private String name;
    private Integer jerseyNumber;
    private String position;
    private Long teamId;
    private String teamName;
    private int scoreEventsCount;
    private int totalPoints;

    public PlayerStatsResponse() {
    }

    public PlayerStatsResponse(Long playerId, String name, Integer jerseyNumber, String position,
                               Long teamId, String teamName, int scoreEventsCount, int totalPoints) {
        this.playerId = playerId;
        this.name = name;
        this.jerseyNumber = jerseyNumber;
        this.position = position;
        this.teamId = teamId;
        this.teamName = teamName;
        this.scoreEventsCount = scoreEventsCount;
        this.totalPoints = totalPoints;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(Integer jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

    public int getScoreEventsCount() {
        return scoreEventsCount;
    }

    public void setScoreEventsCount(int scoreEventsCount) {
        this.scoreEventsCount = scoreEventsCount;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }
}

