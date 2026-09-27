package com.platform.opportunity.dto;

import com.platform.opportunity.model.Opportunity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class OpportunityDTO {
    private Long id;
    private String title;
    private String organization;
    private String description;
    private String category;
    private Set<String> skillsRequired;
    private String eligibility;
    private String location;
    private Opportunity.Mode mode;
    private LocalDate startDate;
    private LocalDate deadline;
    private BigDecimal stipend;
    private BigDecimal fee;
    private String applicationUrl;
    private String imageUrl;
    private Opportunity.Status status;
    private LocalDateTime createdAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Opportunity.Status getStatus() { return status; }
    public void setStatus(Opportunity.Status status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
