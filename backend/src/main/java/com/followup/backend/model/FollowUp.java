package com.followup.backend.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a follow-up in the application.
 */
@Entity
@Table(name = "followup")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FollowUp {

    public enum Status {
        PENDING, // Follow-up created but not yet acted upon
        IN_PROGRESS, // Follow-up is currently being worked on
        COMPLETED, // Follow-up has been addressed or resolved
        OVERDUE, // Follow-up deadline has passed without completion
        CANCELLED // Follow-up was intentionally cancelled
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @OneToOne
    @JoinColumn(name = "lead_id", referencedColumnName = "id", nullable = false)
    private Lead lead;
    

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @OneToMany(mappedBy = "followUp", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<FollowUpNode> nodes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonBackReference
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    @JsonBackReference
    private FollowUpEmployee employee;

}

/*
 * This method is a placeholder for the actual implementation of marking overdue
 * follow-ups.
 * It should be scheduled to run every hour to check for overdue follow-ups and
 * update their status.
 * 
 * @Scheduled(cron = "0 0 * * * *") // runs every hour
 * public void markOverdueFollowUps() {
 * List<FollowUp> dueFollowUps =
 * followUpRepository.findAllPendingAndInProgressWithPastDueDates();
 * for (FollowUp f : dueFollowUps) {
 * f.setStatus(Status.OVERDUE);
 * }
 * followUpRepository.saveAll(dueFollowUps);
 * }
 * 
 * This method is a placeholder for the actual implementation of marking overdue
 * follow-ups.
 * 
 * @Query("SELECT f FROM FollowUp f WHERE (f.status = 'PENDING' OR f.status = 'IN_PROGRESS') AND f.dueDate < CURRENT_TIMESTAMP"
 * )
 * List<FollowUp> findAllPendingAndInProgressWithPastDueDates();
 * 
 */
