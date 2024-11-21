package com.assignmentManagementSys.rest_demo.utils;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class PasswordUtils {

    public static boolean validateBase64Password(String rawPassword, String encodedPassword) {
        // Decode the Base64-encoded password from the database
        String decodedPassword = new String(Base64.getDecoder().decode(encodedPassword), StandardCharsets.UTF_8);
        // Compare the decoded password with the raw password
        return rawPassword.equals(decodedPassword);
    }
}
