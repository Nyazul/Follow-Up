package com.followup.backend.dto;

import java.util.List;

import com.followup.backend.model.FollowUp;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeReportDTO {

    private Long id;
    private String name;
    private String departmentName;
    private String email;
    private String phoneNumber;
    private List<FollowUp> followUps;
}
