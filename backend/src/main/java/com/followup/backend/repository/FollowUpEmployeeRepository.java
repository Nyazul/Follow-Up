package com.followup.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.followup.backend.model.FollowUpEmployee;

@Repository
public interface FollowUpEmployeeRepository extends JpaRepository<FollowUpEmployee, Long> {

    FollowUpEmployee findByEmailAndPassword(String email, String password);

    FollowUpEmployee findByEmail(String email);

}
