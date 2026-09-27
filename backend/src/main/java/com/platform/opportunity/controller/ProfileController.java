package com.platform.opportunity.controller;

import com.platform.opportunity.dto.ProfileDTO;
import com.platform.opportunity.dto.ProfileUpdateRequest;
import com.platform.opportunity.dto.TagsUpdateRequest;
import com.platform.opportunity.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.getProfile(userId));
    }

    @PutMapping("/{userId}/basic")
    public ResponseEntity<ProfileDTO> updateBasicProfile(
            @PathVariable Long userId,
            @RequestBody ProfileUpdateRequest request) {
        return ResponseEntity.ok(profileService.updateBasicProfile(userId, request));
    }

    @PutMapping("/{userId}/tags")
    public ResponseEntity<ProfileDTO> updateTags(
            @PathVariable Long userId,
            @RequestBody TagsUpdateRequest request) {
        return ResponseEntity.ok(profileService.updateTags(userId, request));
    }
}
