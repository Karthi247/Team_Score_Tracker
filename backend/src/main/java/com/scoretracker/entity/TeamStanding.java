package com.scoretracker.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "team_standings", uniqueConstraints = {
    @UniqueConstraint(name = "uk_tournament_standing_team", columnNames = {"tournament_id", "team_id"})
})
public class TeamStanding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(name = "matches_played", nullable = false)
    private Integer matchesPlayed = 0;

    @Column(nullable = false)
    private Integer wins = 0;

    @Column(nullable = false)
    private Integer draws = 0;

    @Column(nullable = false)
    private Integer losses = 0;

    @Column(name = "points_scored", nullable = false)
    private Integer pointsScored = 0;

    @Column(name = "points_conceded", nullable = false)
    private Integer pointsConceded = 0;

    @Column(name = "score_difference", nullable = false)
    private Integer scoreDifference = 0;

    @Column(name = "tournament_points", nullable = false)
    private Integer tournamentPoints = 0;

    @Column(name = "position_rank")
    private Integer positionRank = 0;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public TeamStanding() {
    }

    public TeamStanding(Tournament tournament, Team team) {
        this.tournament = tournament;
        this.team = team;
        this.matchesPlayed = 0;
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
        this.pointsScored = 0;
        this.pointsConceded = 0;
        this.scoreDifference = 0;
        this.tournamentPoints = 0;
        this.positionRank = 0;
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    @PreUpdate
    protected void onPersistOrUpdate() {
        this.scoreDifference = this.pointsScored - this.pointsConceded;
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Integer getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(Integer matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getDraws() {
        return draws;
    }

    public void setDraws(Integer draws) {
        this.draws = draws;
    }

    public Integer getLosses() {
        return losses;
    }

    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    public Integer getPointsScored() {
        return pointsScored;
    }

    public void setPointsScored(Integer pointsScored) {
        this.pointsScored = pointsScored;
    }

    public Integer getPointsConceded() {
        return pointsConceded;
    }

    public void setPointsConceded(Integer pointsConceded) {
        this.pointsConceded = pointsConceded;
    }

    public Integer getScoreDifference() {
        return scoreDifference;
    }

    public void setScoreDifference(Integer scoreDifference) {
        this.scoreDifference = scoreDifference;
    }

    public Integer getTournamentPoints() {
        return tournamentPoints;
    }

    public void setTournamentPoints(Integer tournamentPoints) {
        this.tournamentPoints = tournamentPoints;
    }

    public Integer getPositionRank() {
        return positionRank;
    }

    public void setPositionRank(Integer positionRank) {
        this.positionRank = positionRank;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

