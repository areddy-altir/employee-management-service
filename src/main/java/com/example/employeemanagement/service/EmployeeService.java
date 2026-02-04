package com.example.employeemanagement.service;

import com.example.employeemanagement.config.AuditHelper;
import com.example.employeemanagement.keycloak.KeycloakUserSyncService;
import com.example.employeemanagement.models.dto.BooleanReadByIdResponseDto;
import com.example.employeemanagement.models.dto.EmployeeDto;
import com.example.employeemanagement.models.dto.EmployeeResponseDto;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class EmployeeService extends EmployeeDto.Service {

  private final KeycloakUserSyncService keycloakUserSyncService;

  @Override
  public EmployeeResponseDto createEmployee(EmployeeDto payload) {
    keycloakUserSyncService.syncUserOnCreate(payload.getUser());
    String auditor = AuditHelper.getCurrentAuditor();
    payload
        .createdBy(auditor)
        .updatedBy(auditor)
        .active(true);
    if (payload.getUser() != null) {
      payload.getUser()
          .createdBy(auditor)
          .updatedBy(auditor)
          .active(true);
    }
    return super.createEmployee(payload);
  }

  @Override
  public EmployeeResponseDto patchEmployee(UUID id, EmployeeDto payload) {
    String auditor = AuditHelper.getCurrentAuditor();
    payload.updatedBy(auditor);
    if (payload.getUser() != null) {
      payload.getUser().updatedBy(auditor);
    }
    EmployeeResponseDto result = super.patchEmployee(id, payload);
    keycloakUserSyncService.syncUserOnUpdate(
        result != null && result.getData() != null ? result.getData().getUser() : null);
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
    keycloakUserSyncService.syncUserOnDelete(email);
    return result;
  }
}
