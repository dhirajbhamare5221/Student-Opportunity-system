package com.platform.opportunity.service;

import com.platform.opportunity.dto.ProfileDTO;
import com.platform.opportunity.dto.ProfileUpdateRequest;
import com.platform.opportunity.dto.TagsUpdateRequest;
import com.platform.opportunity.model.Category;
import com.platform.opportunity.model.Interest;
import com.platform.opportunity.model.Skill;
import com.platform.opportunity.model.StudentProfile;
import com.platform.opportunity.repository.CategoryRepository;
import com.platform.opportunity.repository.InterestRepository;
import com.platform.opportunity.repository.SkillRepository;
import com.platform.opportunity.repository.StudentProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    @Autowired
    private StudentProfileRepository profileRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public ProfileDTO getProfile(Long userId) {
        StudentProfile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        return mapToDTO(profile);
    }

    @Transactional
    public ProfileDTO updateBasicProfile(Long userId, ProfileUpdateRequest request) {
        StudentProfile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        if (request.getFullName() != null) profile.setFullName(request.getFullName());
        if (request.getCollege() != null) profile.setCollege(request.getCollege());
        if (request.getDegree() != null) profile.setDegree(request.getDegree());
        if (request.getBranch() != null) profile.setBranch(request.getBranch());
        if (request.getYearOfStudy() != null) profile.setYearOfStudy(request.getYearOfStudy());
        if (request.getGraduationYear() != null) profile.setGraduationYear(request.getGraduationYear());

        profile = profileRepository.save(profile);
        return mapToDTO(profile);
    }

    @Transactional
    public ProfileDTO updateTags(Long userId, TagsUpdateRequest request) {
        StudentProfile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // Update Skills
        if (request.getSkills() != null) {
            List<Skill> skills = skillRepository.findAll();
            Set<Skill> userSkills = skills.stream()
                    .filter(s -> request.getSkills().contains(s.getName()))
                    .collect(Collectors.toSet());
            profile.setSkills(userSkills);
        }

        // Update Interests
        if (request.getInterests() != null) {
            List<Interest> interests = interestRepository.findAll();
            Set<Interest> userInterests = interests.stream()
                    .filter(i -> request.getInterests().contains(i.getName()))
                    .collect(Collectors.toSet());
            profile.setInterests(userInterests);
        }

        // Update Preferred Categories
        if (request.getCategories() != null) {
            List<Category> categories = categoryRepository.findAll();
            Set<Category> userCategories = categories.stream()
                    .filter(c -> request.getCategories().contains(c.getName()))
                    .collect(Collectors.toSet());
            profile.setPreferredCategories(userCategories);
        }

        profile = profileRepository.save(profile);
        return mapToDTO(profile);
    }

    private ProfileDTO mapToDTO(StudentProfile profile) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(profile.getId());
        dto.setFullName(profile.getFullName());
        dto.setCollege(profile.getCollege());
        dto.setDegree(profile.getDegree());
        dto.setBranch(profile.getBranch());
        dto.setYearOfStudy(profile.getYearOfStudy());
        dto.setGraduationYear(profile.getGraduationYear());

        dto.setSkills(profile.getSkills().stream().map(Skill::getName).collect(Collectors.toSet()));
        dto.setInterests(profile.getInterests().stream().map(Interest::getName).collect(Collectors.toSet()));
        dto.setPreferredCategories(profile.getPreferredCategories().stream().map(Category::getName).collect(Collectors.toSet()));

        return dto;
    }
}
