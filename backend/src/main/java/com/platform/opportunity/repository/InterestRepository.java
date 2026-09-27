package com.platform.opportunity.repository;

import com.platform.opportunity.model.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InterestRepository extends JpaRepository<Interest, Long> {
    Optional<Interest> findByName(String name);
}
