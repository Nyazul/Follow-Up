package com.followup.backend.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * DTO for representing the trend of completed follow-ups over time.
 * This is used for generating reports and visualizations.
 */

@Data
@AllArgsConstructor
public class FollowUpTrendDTO {
    private LocalDate date;
    private int completed;
}
