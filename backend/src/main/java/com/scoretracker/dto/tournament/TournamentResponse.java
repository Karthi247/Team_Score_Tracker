package com.scoretracker.dto.tournament;

import com.scoretracker.enums.SportType;
import com.scoretracker.enums.TournamentFormat;
import com.scoretracker.enums.TournamentStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TournamentResponse {
    private Long id;
    private String name;
    private SportType sportType;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String venue;
    private Integer maxTeams;
    private int registeredTeamsCount;
    private TournamentStatus status;
    private TournamentFormat format;
    private Integer winPoints;
    private Integer drawPoints;
    private Integer lossPoints;
    private LocalDateTime createdAt;

    public TournamentResponse() {
    }

    public TournamentResponse(Long id, String name, SportType sportType, String description,
                              LocalDate startDate, LocalDate endDate, String venue,
                              Integer maxTeams, int registeredTeamsCount, TournamentStatus status,
                              TournamentFormat format, Integer winPoints, Integer drawPoints,
                              Integer lossPoints, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.sportType = sportType;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.venue = venue;
        this.maxTeams = maxTeams;
        this.registeredTeamsCount = registeredTeamsCount;
        this.status = status;
        this.format = format;
        this.winPoints = winPoints;
        this.drawPoints = drawPoints;
        this.lossPoints = lossPoints;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SportType getSportType() {
        return sportType;
    }

    public void setSportType(SportType sportType) {
        this.sportType = sportType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Integer getMaxTeams() {
        return maxTeams;
    }

    public void setMaxTeams(Integer maxTeams) {
        this.maxTeams = maxTeams;
    }

    public int getRegisteredTeamsCount() {
        return registeredTeamsCount;
    }

    public void setRegisteredTeamsCount(int registeredTeamsCount) {
        this.registeredTeamsCount = registeredTeamsCount;
    }

    public TournamentStatus getStatus() {
        return status;
    }

    public void setStatus(TournamentStatus status) {
        this.status = status;
    }

    public TournamentFormat getFormat() {
        return format;
    }

    public void setFormat(TournamentFormat format) {
        this.format = format;
    }

    public Integer getWinPoints() {
        return winPoints;
    }

    public void setWinPoints(Integer winPoints) {
        this.winPoints = winPoints;
    }

    public Integer getDrawPoints() {
        return drawPoints;
    }

    public void setDrawPoints(Integer drawPoints) {
        this.drawPoints = drawPoints;
    }

    public Integer getLossPoints() {
        return lossPoints;
    }

    public void setLossPoints(Integer lossPoints) {
        this.lossPoints = lossPoints;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

