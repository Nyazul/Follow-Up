package com.followup.backend.dto;

import java.util.List;

import com.followup.backend.model.FollowUpNode;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class EditLeadDetailsDTO {


    private Long followUpId;

    private Long leadId;
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

    private List<FollowUpNode> nodes;
}
