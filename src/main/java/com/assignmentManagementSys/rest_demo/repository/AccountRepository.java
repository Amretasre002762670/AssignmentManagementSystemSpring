package com.assignmentManagementSys.rest_demo.repository;

import com.assignmentManagementSys.rest_demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
