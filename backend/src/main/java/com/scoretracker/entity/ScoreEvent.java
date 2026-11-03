package com.scoretracker.entity;

import com.scoretracker.enums.ScoreEventType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "score_events")
public class ScoreEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(nullable = false)
    private Integer points;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 30)
    private ScoreEventType eventType;

    @Column(name = "minute_or_period", length = 50)
    private String minuteOrPeriod;

    @Column(length = 255)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public ScoreEvent() {
    }

    public ScoreEvent(Match match, Team team, Player player, Integer points,
                      ScoreEventType eventType, String minuteOrPeriod, String description, User createdBy) {
        this.match = match;
        this.team = team;
        this.player = player;
        this.points = points;
        this.eventType = eventType;
        this.minuteOrPeriod = minuteOrPeriod;
        this.description = description;
        this.createdBy = createdBy;
        this.timestamp = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

