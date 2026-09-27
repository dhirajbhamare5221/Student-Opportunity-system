package com.platform.opportunity.dto;

import com.platform.opportunity.model.Opportunity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public class OpportunityRequest {
    
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Organization is required")
    private String organization;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Category name is required")
    private String category;

    private Set<String> skillsRequired;

    private String eligibility;
    private String location;

    @NotNull(message = "Mode is required")
    private Opportunity.Mode mode;

    private LocalDate startDate;

    @NotNull(message = "Deadline is required")
    private LocalDate deadline;

    private BigDecimal stipend;
    private BigDecimal fee;
    private String applicationUrl;
    private String imageUrl;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Set<String> getSkillsRequired() { return skillsRequired; }
    public void setSkillsRequired(Set<String> skillsRequired) { this.skillsRequired = skillsRequired; }

    public String getEligibility() { return eligibility; }
    public void setEligibility(String eligibility) { this.eligibility = eligibility; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Opportunity.Mode getMode() { return mode; }
    public void setMode(Opportunity.Mode mode) { this.mode = mode; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public BigDecimal getStipend() { return stipend; }
    public void setStipend(BigDecimal stipend) { this.stipend = stipend; }

    public BigDecimal getFee() { return fee; }
    public void setFee(BigDecimal fee) { this.fee = fee; }

    public String getApplicationUrl() { return applicationUrl; }
    public void setApplicationUrl(String applicationUrl) { this.applicationUrl = applicationUrl; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
