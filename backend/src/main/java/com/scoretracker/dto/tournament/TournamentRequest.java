package com.scoretracker.dto.tournament;

import com.scoretracker.enums.SportType;
import com.scoretracker.enums.TournamentFormat;
import com.scoretracker.enums.TournamentStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class TournamentRequest {
    @NotBlank(message = "Tournament name is required")
    @Size(min = 2, max = 150, message = "Tournament name must be between 2 and 150 characters")
    private String name;

    @NotNull(message = "Sport type is required")
    private SportType sportType;

    private String description;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private String venue;

    @Min(value = 2, message = "Maximum teams must be at least 2")
    private Integer maxTeams;

    private TournamentStatus status = TournamentStatus.UPCOMING;
    private TournamentFormat format = TournamentFormat.LEAGUE;

    private Integer winPoints = 3;
    private Integer drawPoints = 1;
    private Integer lossPoints = 0;

    public TournamentRequest() {
    }

    public TournamentRequest(String name, SportType sportType, String description,
                             LocalDate startDate, LocalDate endDate, String venue,
                             Integer maxTeams, TournamentStatus status, TournamentFormat format,
                             Integer winPoints, Integer drawPoints, Integer lossPoints) {
        this.name = name;
        this.sportType = sportType;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.venue = venue;
        this.maxTeams = maxTeams;
        this.status = status != null ? status : TournamentStatus.UPCOMING;
        this.format = format != null ? format : TournamentFormat.LEAGUE;
        this.winPoints = winPoints != null ? winPoints : 3;
        this.drawPoints = drawPoints != null ? drawPoints : 1;
        this.lossPoints = lossPoints != null ? lossPoints : 0;
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
}
