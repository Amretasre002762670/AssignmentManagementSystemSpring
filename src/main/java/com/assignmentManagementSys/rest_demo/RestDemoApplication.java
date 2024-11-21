package com.assignmentManagementSys.rest_demo;

import com.assignmentManagementSys.rest_demo.entity.Account;
import com.assignmentManagementSys.rest_demo.repository.AccountRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@SpringBootApplication
public class RestDemoApplication {

	@Autowired
	private DataSource dataSource;

	@Value("${file.upload-dir}")
	private String uploadDir;

	@Autowired
	private AccountRepository accountRepository;

	public static void main(String[] args) {
		SpringApplication.run(RestDemoApplication.class, args);
	}

	@PostConstruct
	public void healthCheckOnStartup() {
		try (Connection connection = dataSource.getConnection()) {
			if (connection != null && !connection.isClosed()) {
				System.out.println("Database connection is healthy.");
			} else {
				System.out.println("Database connection failed.");
			}
		} catch (SQLException e) {
			System.err.println("Error while connecting to the database: " + e.getMessage());
		}
	}

	@PostConstruct
	public void uploadAccountsFromFolder() {
		// Define the path to the folder where the CSV file is stored
		File folder = new File(uploadDir);
		File file = new File(folder, "users.csv");

		if (!file.exists()) {
			System.out.println("File doesn't exist: " + file.getAbsolutePath());
			return;
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			boolean isHeader = true;
			while ((line = reader.readLine()) != null) {
				// Skip the header line
				if (isHeader) {
					isHeader = false;
					continue;
				}

				// Parse each line and create Account objects
				String[] fields = line.split(",");
				if (fields.length == 4) {
					String email = fields[2];  // Assuming email is at index 2

					// Check if the email already exists in the database
					Optional<Account> existingAccount = accountRepository.findByEmail(email);
					if (existingAccount.isPresent()) {
						Account account = existingAccount.get();
						System.out.println("Account already exists for email: " + email);
						System.out.println("Id of the account that already exists: " + account.getId());
						continue;  // Skip this account or update it if necessary
					}

					Account account = new Account();
					account.setId(UUID.randomUUID()); // Generate a unique UUID for the account ID
					account.setFirstName(fields[0]);
					account.setLastName(fields[1]);
					account.setEmail(email);

					// Encode password in Base64
					String encodedPassword = Base64.getEncoder().encodeToString(fields[3].getBytes(StandardCharsets.UTF_8));
					account.setPassword(encodedPassword);

					// persist the account, you should save it to the database or collection here
					accountRepository.save(account);

					System.out.println("Account created: " + account.getId());
				} else {
					System.out.println("Skipping invalid line: " + line);
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading file: " + e.getMessage());
		}
	}

}
