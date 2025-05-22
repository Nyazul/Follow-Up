package com.followup.backend.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data   
@AllArgsConstructor
@NoArgsConstructor
public class FollowUpDTO {

    private Long id;
    private String description;
    private String leadName;
    private LocalDateTime dueDate;
    private String courseName;

}
