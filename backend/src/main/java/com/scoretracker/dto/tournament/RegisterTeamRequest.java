package com.scoretracker.dto.tournament;

import jakarta.validation.constraints.NotNull;

public class RegisterTeamRequest {
    @NotNull(message = "Team ID is required")
    private Long teamId;

    private Integer seedNumber;

    public RegisterTeamRequest() {
    }

    public RegisterTeamRequest(Long teamId, Integer seedNumber) {
        this.teamId = teamId;
        this.seedNumber = seedNumber;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Integer getSeedNumber() {
        return seedNumber;
    }

    public void setSeedNumber(Integer seedNumber) {
        this.seedNumber = seedNumber;
    }
}

