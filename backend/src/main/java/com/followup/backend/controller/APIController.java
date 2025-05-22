package com.followup.backend.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.followup.backend.dto.FollowUpDTO;
import com.followup.backend.model.FollowUp;
import com.followup.backend.model.FollowUpEmployee;
import com.followup.backend.repository.FollowUpEmployeeRepository;

@Controller
@RequestMapping("/api")
public class APIController {

    @Autowired
    FollowUpEmployeeRepository followUpEmployeeRepository;

    @GetMapping("/employee/{id}/followups")
    @ResponseBody
    public List<FollowUpDTO> getFollowUpsForEmployee(@PathVariable Long id) {
        FollowUpEmployee employee = followUpEmployeeRepository.findById(id).orElse(null);
        if (employee == null) {
            return Collections.emptyList();
        }
        List<FollowUpDTO> followUps = employee.getCompletedFollowUps().stream()
                .map(followUp -> new FollowUpDTO(
                        followUp.getId(),
                        followUp.getDescription(),
                        followUp.getLead().getName(),
                        followUp.getDueDate(),
                        followUp.getCourse().getName()))
                .toList();
         
        if (followUps.isEmpty()) {
            return Collections.emptyList();
        }

        return followUps;
    }

}
