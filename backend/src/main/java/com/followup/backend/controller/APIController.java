package com.followup.backend.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.followup.backend.model.FollowUp;
import com.followup.backend.model.FollowUpEmployee;
import com.followup.backend.repository.FollowUpEmployeeRepository;

@Controller
@RequestMapping("/api")
public class APIController {

    @Autowired
    FollowUpEmployeeRepository followUpEmployeeRepository;

    @GetMapping("/api/employee/{id}/followups")
    @ResponseBody
    public List<FollowUp> getFollowUpsForEmployee(@PathVariable Long id) {
        FollowUpEmployee employee = followUpEmployeeRepository.findById(id).orElse(null);
        if (employee == null) {
            return Collections.emptyList();
        }
        return employee.getCompletedFollowUps();
    }

}
