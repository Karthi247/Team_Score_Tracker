package com.scoretracker.dto.team;

import com.scoretracker.enums.TeamStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TeamRequest {
    @NotBlank(message = "Team name is required")
    @Size(min = 2, max = 100, message = "Team name must be between 2 and 100 characters")
    private String name;

    private String logoUrl;
    private String description;

    @NotBlank(message = "Captain name is required")
    private String captainName;

    private TeamStatus status = TeamStatus.ACTIVE;

    public TeamRequest() {
    }

    public TeamRequest(String name, String logoUrl, String description, String captainName, TeamStatus status) {
        this.name = name;
        this.logoUrl = logoUrl;
        this.description = description;
        this.captainName = captainName;
        this.status = status != null ? status : TeamStatus.ACTIVE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCaptainName() {
        return captainName;
    }

    public void setCaptainName(String captainName) {
        this.captainName = captainName;
    }

    public TeamStatus getStatus() {
        return status;
    }

    public void setStatus(TeamStatus status) {
        this.status = status;
    }
}

