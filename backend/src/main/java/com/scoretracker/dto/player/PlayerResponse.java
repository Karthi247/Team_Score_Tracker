package com.scoretracker.dto.player;

import com.scoretracker.enums.PlayerStatus;
import java.time.LocalDateTime;

public class PlayerResponse {
    private Long id;
    private String name;
    private Integer jerseyNumber;
    private Integer age;
    private String position;
    private Long teamId;
    private String teamName;
    private String profileImageUrl;
    private PlayerStatus status;
    private LocalDateTime createdAt;

    public PlayerResponse() {
    }

    public PlayerResponse(Long id, String name, Integer jerseyNumber, Integer age,
                          String position, Long teamId, String teamName,
                          String profileImageUrl, PlayerStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.jerseyNumber = jerseyNumber;
        this.age = age;
        this.position = position;
        this.teamId = teamId;
        this.teamName = teamName;
        this.profileImageUrl = profileImageUrl;
        this.status = status;
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

    public Integer getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(Integer jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

