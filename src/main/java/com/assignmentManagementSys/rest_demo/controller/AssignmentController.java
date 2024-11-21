package com.assignmentManagementSys.rest_demo.controller;

import com.assignmentManagementSys.rest_demo.entity.Account;
import com.assignmentManagementSys.rest_demo.entity.Assignment;
import com.assignmentManagementSys.rest_demo.repository.AccountRepository;
import com.assignmentManagementSys.rest_demo.repository.AssignmentRepository;
import com.assignmentManagementSys.rest_demo.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    @Autowired
    private final AssignmentRepository assignmentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordUtils passwordUtils;

    public AssignmentController(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<Assignment> createAssignment(
            @RequestBody Assignment assignmentRequest,
            @RequestHeader("Authorization") String authorizationHeader) {

        // Check for the Authorization header (Basic Auth)
        if (authorizationHeader == null || !authorizationHeader.startsWith("Basic ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Decode the Base64 credentials from the Authorization header
        String base64Credentials = authorizationHeader.substring("Basic ".length());
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        String[] values = credentials.split(":", 2); // Split into email and password
        String email = values[0];
        String rawPassword = values[1];

        // Find the account by email
        Optional<Account> accountOpt = accountRepository.findByEmail(email);
        if (accountOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();  // Account not found
        }

        Account account = accountOpt.get();

        // Use PasswordUtils to validate the password
        boolean isPasswordValid = PasswordUtils.validateBase64Password(rawPassword, account.getPassword());
        if (!isPasswordValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();  // Invalid password
        }

        // If authentication is successful, create the assignment
        Assignment assignment = new Assignment();
        assignment.setId(UUID.randomUUID());  // Set a new UUID
        assignment.setAccount(account);  // Set the Account object (not just the accountId)
        assignment.setAssignmentName(assignmentRequest.getAssignmentName());
        assignment.setPoints(assignmentRequest.getPoints());
        assignment.setAttempts(assignmentRequest.getAttempts());
        assignment.setDeadline(assignmentRequest.getDeadline());
        assignment.setLastUpdated(assignmentRequest.getLastUpdated());

        // Save the assignment to the database
        Assignment savedAssignment = assignmentRepository.save(assignment);

        // Return the saved assignment in the response
        return ResponseEntity.ok(savedAssignment);
    }

}
