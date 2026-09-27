package com.platform.opportunity.dto;

import java.util.Set;

public class TagsUpdateRequest {
    private Set<String> skills;
    private Set<String> interests;
    private Set<String> categories;

    // Getters and Setters
    public Set<String> getSkills() { return skills; }
    public void setSkills(Set<String> skills) { this.skills = skills; }

    public Set<String> getInterests() { return interests; }
    public void setInterests(Set<String> interests) { this.interests = interests; }

    public Set<String> getCategories() { return categories; }
    public void setCategories(Set<String> categories) { this.categories = categories; }
}
