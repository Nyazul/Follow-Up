package com.followup.backend.dto;

import lombok.*;

/**
 * DTO for representing a summary of an employee's follow-up activities.
 * This is used for generating reports and visualizations.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSummaryDTO {
    private Long id;
    private String name;
    private String department; // or departmentName

    private Long completedToday;
    private Long completedWeek;
    private Long completedMonth;

    private Long departmentId; // required for filtering
}

