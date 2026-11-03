package com.scoretracker.dto.match;

import com.scoretracker.enums.MatchStatus;

public class MatchStatusUpdateRequest {
    private MatchStatus status;
    private String currentPeriod;
    private Integer elapsedMinutes;

    public MatchStatusUpdateRequest() {
    }

    public MatchStatusUpdateRequest(MatchStatus status, String currentPeriod, Integer elapsedMinutes) {
        this.status = status;
        this.currentPeriod = currentPeriod;
        this.elapsedMinutes = elapsedMinutes;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public String getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(String currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    public Integer getElapsedMinutes() {
        return elapsedMinutes;
    }

    public void setElapsedMinutes(Integer elapsedMinutes) {
        this.elapsedMinutes = elapsedMinutes;
    }
}

