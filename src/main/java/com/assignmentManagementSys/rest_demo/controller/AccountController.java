package com.assignmentManagementSys.rest_demo.controller;
import com.assignmentManagementSys.rest_demo.entity.Account;
import com.assignmentManagementSys.rest_demo.exceptions.AccountNotFoundException;
import com.assignmentManagementSys.rest_demo.repository.AccountRepository;
//import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;


@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private final AccountRepository accountRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountRepository.save(account);
    }

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadAccounts(@RequestParam("file") MultipartFile file) {
        List<String> accountsIds = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
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
                    Account account = new Account();
                    account.setId(UUID.randomUUID().toString()); // Generate a unique UUID for the account ID
                    account.setFirstName(fields[0]);
                    account.setLastName(fields[1]);
                    account.setEmail(fields[2]);

                    // Encode password in Base64
                    String encodedPassword = Base64.getEncoder().encodeToString(fields[3].getBytes(StandardCharsets.UTF_8));
                    account.setPassword(encodedPassword);

                    Account savedAccount = accountRepository.save(account);
                    accountsIds.add(savedAccount.getId());
                }
            }
        } catch (
                IOException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(accountsIds);
    }

    @PostMapping("/upload-from-folder")
    public ResponseEntity<List<String>> uploadAccountsFromFolder() {
        List<String> accountIds = new ArrayList<>();
        // Define the path to the folder where the CSV file is stored
        File folder = new File(uploadDir);
        File file = new File(folder, "users.csv");

        if (!file.exists()) {
            return ResponseEntity.status(404).body(null); // File not found
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
                    Account account = new Account();
                    account.setId(UUID.randomUUID().toString()); // Generate a unique UUID for the account ID
                    account.setFirstName(fields[0]);
                    account.setLastName(fields[1]);
                    account.setEmail(fields[2]);

                    // Encode password in Base64
                    String encodedPassword = Base64.getEncoder().encodeToString(fields[3].getBytes(StandardCharsets.UTF_8));
                    account.setPassword(encodedPassword);

                    // Save the account to the database
                    Account savedAccount = accountRepository.save(account);
                    accountIds.add(savedAccount.getId()); // Add the account ID to the list
                }
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }

        return ResponseEntity.ok(accountIds);
    }

//    @GetMapping("{id}")
//    public Account getAccount(@PathVariable String id) {
//        // Customized exceptions are used for meaningful feedback
//        return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account Not Found"));
//    }

    @GetMapping("{id}")
    public ResponseEntity<Account> getAccount(@PathVariable String id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account with id " + id + " not found"));
        return ResponseEntity.ok(account);
    }
}
