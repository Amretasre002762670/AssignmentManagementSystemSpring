package com.assignmentManagementSys.rest_demo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)  // Indicates a many-to-one relationship with Account & If changes to an Account (such as deleting it) to cascade to the associated Assignments
    @JoinColumn(name = "Account_Id", referencedColumnName = "id", nullable = false)  // Specifies the foreign key
    private Account account;  // Instead of a string, you reference the Account object directly

    @Column(name="Assignment_Name", nullable = false)
    private String assignmentName;

    @Column(name="Assignment_Points", nullable = false)
    private int points;

    @Column(name="Assignment_Attempts", nullable = false)
    private int attempts;

    @Column(name="Assignment_Deadline", nullable = false)
    private LocalDateTime deadline;

    @Column(name="Assignment_LastUpdated", nullable = false)
    private LocalDateTime lastUpdated;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
