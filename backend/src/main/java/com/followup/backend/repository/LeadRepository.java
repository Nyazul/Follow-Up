package com.followup.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.followup.backend.model.Lead;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {

}
