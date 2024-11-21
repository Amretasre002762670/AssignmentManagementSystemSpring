package com.assignmentManagementSys.rest_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
@RequestMapping("/healthz")
public class HealthCheckController implements ErrorController {

    @Autowired
    private DataSource dataSource;

    @GetMapping
    public ResponseEntity<String> healthCheck() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                return ResponseEntity.ok("Connection to DB is healthy");
            } else {
                return ResponseEntity.status(503).body("Database connection failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(503).body("Database connection failed!");
        }
    }

    private ResponseEntity<String> invalidMethods() {
        return ResponseEntity.status(405).body("Method not allowed!");
    }

    @PostMapping
    public ResponseEntity<String> healthCheckPost() {
        return invalidMethods();
    }

    @PutMapping
    public ResponseEntity<String> healthCheckPut() {
        return invalidMethods();
    }

    @DeleteMapping
    public ResponseEntity<String> healthCheckDelete() {
        return invalidMethods();
    }
}
