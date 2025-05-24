package com.followup.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.followup.backend.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
