package com.followup.backend.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(callSuper = true)
@DiscriminatorValue("FOLLOWUP_EMPLOYEE")
public class FollowUpEmployee extends User {

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<FollowUp> followUps;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", referencedColumnName = "id", nullable = true)
    private Department department;

    @Transient
    public List<FollowUp> getRemainingFollowUps() {
        return followUps.stream()
                .filter(f -> f.getStatus() == FollowUp.Status.PENDING ||
                        f.getStatus() == FollowUp.Status.IN_PROGRESS ||
                        f.getStatus() == FollowUp.Status.OVERDUE)
                .toList();
    }

    @Transient
    public List<FollowUp> getCompletedFollowUps() {
        return followUps.stream()
                .filter(f -> f.getStatus() == FollowUp.Status.COMPLETED ||
                        f.getStatus() == FollowUp.Status.CANCELLED)
                .toList();
    }

    @Override
    public String getRole() {
        return "FOLLOWUP_EMPLOYEE";
    }


}


