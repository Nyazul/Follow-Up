package com.followup.backend.dto;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class NewFollowUpFormDTO {

    // Lead Info
    @NotBlank(message = "Lead name is required")
    private String leadName;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
    private String mobile;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address is too long")
    private String address;

    // Department and Course
    @NotNull(message = "Department must be selected")
    private Long departmentId;

    @NotNull(message = "Course must be selected")
    private Long courseCode;

    // Follow-up Info
    @NotBlank(message = "Follow-up title is required")
    private String followUpTitle;

    @NotBlank(message = "Follow-up description is required")
    private String followUpDescription;

    @NotBlank(message = "Follow-up date is required")
    private String followUpDate;

    // Employee Info
    @NotNull(message = "Employee ID is required")
    private Long employeeId;
}
