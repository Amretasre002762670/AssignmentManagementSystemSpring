package com.assignmentManagementSys.rest_demo.repository;

import com.assignmentManagementSys.rest_demo.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {
}
