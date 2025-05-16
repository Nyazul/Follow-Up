package com.followup.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.followup.backend.model.BasicEmployee;

@Repository
public interface BasicEmployeeRepository extends JpaRepository<BasicEmployee, Long> {

    BasicEmployee findByEmailAndPassword(String email, String password);

    BasicEmployee findByEmail(String email);

}
