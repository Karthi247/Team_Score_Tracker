package com.scoretracker.dto.player;

import com.scoretracker.enums.PlayerStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PlayerRequest {
    @NotBlank(message = "Player name is required")
    @Size(min = 2, max = 100, message = "Player name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Jersey number is required")
    @Min(value = 0, message = "Jersey number cannot be negative")
    @Max(value = 999, message = "Jersey number cannot exceed 999")
    private Integer jerseyNumber;

    @Min(value = 5, message = "Age must be at least 5")
    @Max(value = 100, message = "Age must be realistic")
    private Integer age;

    @NotBlank(message = "Position is required")
    private String position;

    @NotNull(message = "Team ID is required")
    private Long teamId;

    private String profileImageUrl;
    private PlayerStatus status = PlayerStatus.ACTIVE;

    public PlayerRequest() {
    }

    public PlayerRequest(String name, Integer jerseyNumber, Integer age, String position,
                         Long teamId, String profileImageUrl, PlayerStatus status) {
        this.name = name;
        this.jerseyNumber = jerseyNumber;
        this.age = age;
        this.position = position;
        this.teamId = teamId;
        this.profileImageUrl = profileImageUrl;
        this.status = status != null ? status : PlayerStatus.ACTIVE;
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
}

