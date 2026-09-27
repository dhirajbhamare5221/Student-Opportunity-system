package com.platform.opportunity.dto;

import java.util.Set;

public class ProfileDTO {
    private Long id;
    private String fullName;
    private String college;
    private String degree;
    private String branch;
    private Integer yearOfStudy;
    private Integer graduationYear;
    
    private Set<String> skills;
    private Set<String> interests;
    private Set<String> preferredCategories;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }

    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public Integer getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(Integer yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    public Integer getGraduationYear() { return graduationYear; }
    public void setGraduationYear(Integer graduationYear) { this.graduationYear = graduationYear; }

    public Set<String> getSkills() { return skills; }
    public void setSkills(Set<String> skills) { this.skills = skills; }

    public Set<String> getInterests() { return interests; }
    public void setInterests(Set<String> interests) { this.interests = interests; }

    public Set<String> getPreferredCategories() { return preferredCategories; }
    public void setPreferredCategories(Set<String> preferredCategories) { this.preferredCategories = preferredCategories; }
}
