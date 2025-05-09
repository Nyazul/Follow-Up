package com.followup.backend.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents an follow-up employee in the application.
 */

 //create class User and extend it

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = true)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", referencedColumnName = "id", nullable = true)
    private Department department;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;



    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<FollowUp> followUps;

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

}
