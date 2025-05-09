package com.followup.backend.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a node (single follow-up) in the user's follow-up list.
 */
@Entity
@Table(name = "node")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "body", nullable = true)
    private String body;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    // Change String to Employee
    @Column(name = "done_by", nullable = true)
    private String doneBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    //Forign key to FollowUp
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followup_id", nullable = false)
    @JsonBackReference
    private FollowUp followUp;

}
