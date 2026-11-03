package com.scoretracker.entity;

import com.scoretracker.enums.SportType;
import com.scoretracker.enums.TournamentFormat;
import com.scoretracker.enums.TournamentStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tournaments")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "sport_type", nullable = false, length = 30)
    private SportType sportType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(length = 200)
    private String venue;

    @Column(name = "max_teams")
    private Integer maxTeams;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TournamentStatus status = TournamentStatus.UPCOMING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TournamentFormat format = TournamentFormat.LEAGUE;

    @Column(name = "win_points", nullable = false)
    private Integer winPoints = 3;

    @Column(name = "draw_points", nullable = false)
    private Integer drawPoints = 1;

    @Column(name = "loss_points", nullable = false)
    private Integer lossPoints = 0;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TournamentTeam> tournamentTeams = new ArrayList<>();

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<Match> matches = new ArrayList<>();

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamStanding> standings = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Tournament() {
    }

    public Tournament(String name, SportType sportType, String description, LocalDate startDate,
                      LocalDate endDate, String venue, Integer maxTeams, TournamentStatus status,
                      TournamentFormat format, Integer winPoints, Integer drawPoints, Integer lossPoints) {
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
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    // Getters and Setters
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

    public List<TournamentTeam> getTournamentTeams() {
        return tournamentTeams;
    }

    public void setTournamentTeams(List<TournamentTeam> tournamentTeams) {
        this.tournamentTeams = tournamentTeams;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public List<TeamStanding> getStandings() {
        return standings;
    }

    public void setStandings(List<TeamStanding> standings) {
        this.standings = standings;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

