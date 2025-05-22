package com.followup.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.followup.backend.model.FollowUp;

@Repository
public interface FollowUpRepository extends JpaRepository<FollowUp, Long> {
    
    List<FollowUp> findByCreatedAtAfter(LocalDateTime dateTime);
    List<FollowUp> findByStatus(FollowUp.Status status);
    
}
