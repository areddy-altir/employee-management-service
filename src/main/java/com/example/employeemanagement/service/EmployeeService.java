package com.example.employeemanagement.service;

import com.example.employeemanagement.config.AuditHelper;
import com.example.employeemanagement.config.KeycloakUserHelper;
import com.example.employeemanagement.models.dto.BooleanReadByIdResponseDto;
import com.example.employeemanagement.models.dto.EmployeeDto;
import com.example.employeemanagement.models.dto.EmployeeResponseDto;
import com.example.employeemanagement.models.dto.UserDto;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class EmployeeService extends EmployeeDto.Service {

  private final KeycloakUserHelper keycloakUserHelper;

  @Override
  public EmployeeResponseDto createEmployee(EmployeeDto payload) {
    UserDto user = payload.getUser();
    if (user != null && user.getEmail() != null && !user.getEmail().isBlank()) {
      String tempPassword = generateTemporaryPassword();
      keycloakUserHelper.createUserInKeycloak(
          user.getEmail(),
          user.getName(),
          user.getPhone(),
          user.getAddress(),
          user.getCountry(),
          tempPassword);
      log.info(
          "Temporary password for {} (share with user, they must change on first login): {}",
          user.getEmail(),
          tempPassword);
    }
    Instant now = Instant.now();
    String auditor = AuditHelper.getCurrentAuditor();
    payload
        .createdOn(now)
        .updatedOn(now)
        .createdBy(auditor)
        .updatedBy(auditor)
        .active(true);
    return super.createEmployee(payload);
  }

  private String generateTemporaryPassword() {
    return "Temp" + UUID.randomUUID().toString().replace("-", "").substring(0, 8) + "!";
  }

  @Override
  public EmployeeResponseDto patchEmployee(UUID id, EmployeeDto payload) {
    EmployeeResponseDto existing = findByIdEmployee(id);
    String email = null;
    if (existing != null && existing.getData() != null && existing.getData().getUser() != null) {
      email = existing.getData().getUser().getEmail();
    }
    Instant now = Instant.now();
    String auditor = AuditHelper.getCurrentAuditor();
    if (existing != null && existing.getData() != null) {
      if (payload.getCreatedOn() == null && existing.getData().getCreatedOn() != null) {
        payload.createdOn(existing.getData().getCreatedOn());
      }
      if (payload.getCreatedBy() == null && existing.getData().getCreatedBy() != null) {
        payload.createdBy(existing.getData().getCreatedBy());
      }
    }
    payload.updatedOn(now).updatedBy(auditor);
    EmployeeResponseDto result = super.patchEmployee(id, payload);
    if (email != null && payload.getUser() != null) {
      UserDto userPayload = payload.getUser();
      keycloakUserHelper.updateUserInKeycloak(
          email,
          userPayload.getName(),
          userPayload.getPhone(),
          userPayload.getAddress(),
          userPayload.getCountry());
    }
    return result;
  }

  @Override
  public BooleanReadByIdResponseDto deleteEmployee(UUID id) {
    EmployeeResponseDto existing = findByIdEmployee(id);
    String email = null;
    if (existing != null && existing.getData() != null && existing.getData().getUser() != null) {
      email = existing.getData().getUser().getEmail();
    }
    BooleanReadByIdResponseDto result = super.deleteEmployee(id);
    if (email != null) {
      keycloakUserHelper.deleteUserInKeycloak(email);
    }
    return result;
  }
}
