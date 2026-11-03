package com.scoretracker.dto.tournament;

import com.scoretracker.enums.TournamentFormat;

public class FixtureGenerateRequest {
    private TournamentFormat format;
    private Integer matchesPerDay = 2;
    private Integer intervalDays = 1;

    public FixtureGenerateRequest() {
    }

    public FixtureGenerateRequest(TournamentFormat format, Integer matchesPerDay, Integer intervalDays) {
        this.format = format;
        this.matchesPerDay = matchesPerDay;
        this.intervalDays = intervalDays;
    }

    public TournamentFormat getFormat() {
        return format;
    }

    public void setFormat(TournamentFormat format) {
        this.format = format;
    }

    public Integer getMatchesPerDay() {
        return matchesPerDay;
    }

    public void setMatchesPerDay(Integer matchesPerDay) {
        this.matchesPerDay = matchesPerDay;
    }

    public Integer getIntervalDays() {
        return intervalDays;
    }

    public void setIntervalDays(Integer intervalDays) {
        this.intervalDays = intervalDays;
    }
}

