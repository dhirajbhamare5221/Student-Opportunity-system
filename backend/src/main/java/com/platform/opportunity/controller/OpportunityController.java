package com.platform.opportunity.controller;

import com.platform.opportunity.dto.OpportunityDTO;
import com.platform.opportunity.dto.OpportunityRequest;
import com.platform.opportunity.service.OpportunityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/opportunities")
public class OpportunityController {

    @Autowired
    private OpportunityService opportunityService;

    @GetMapping
    public ResponseEntity<Page<OpportunityDTO>> getOpportunities(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(opportunityService.getOpportunities(category, search, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OpportunityDTO> getOpportunityById(@PathVariable Long id) {
        return ResponseEntity.ok(opportunityService.getOpportunityById(id));
    }

    @PostMapping
    public ResponseEntity<OpportunityDTO> createOpportunity(@Valid @RequestBody OpportunityRequest request) {
        return ResponseEntity.ok(opportunityService.createOpportunity(request));
    }
}
