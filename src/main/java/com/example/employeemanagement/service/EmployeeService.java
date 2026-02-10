package com.example.employeemanagement.service;

import com.example.employeemanagement.util.AuditPayloadHelper;
import com.example.employeemanagement.integration.keycloak.KeycloakUserSyncService;
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
    applyCreateAuditToPayloadAndUser(payload);
    return super.createEmployee(payload);
  }

  @Override
  public EmployeeResponseDto patchEmployee(UUID id, EmployeeDto payload) {
    applyUpdateAuditToPayloadAndUser(payload);
    EmployeeResponseDto result = super.patchEmployee(id, payload);
    if (payload.getUser() != null) {
      keycloakUserSyncService.syncUserOnUpdate(payload.getUser());
    }
    return result;
  }

  @Override
  public BooleanReadByIdResponseDto deleteEmployee(UUID id) {
    EmployeeResponseDto existing = findByIdEmployee(id);
    String email = getUserEmailFromEmployeeResponse(existing);
    BooleanReadByIdResponseDto result = super.deleteEmployee(id);
    keycloakUserSyncService.syncUserOnDelete(email);
    return result;
  }

  private static void applyCreateAuditToPayloadAndUser(EmployeeDto payload) {
    AuditPayloadHelper.applyCreateAudit(payload);
    AuditPayloadHelper.applyCreateAudit(payload.getUser());
  }

  private static void applyUpdateAuditToPayloadAndUser(EmployeeDto payload) {
    AuditPayloadHelper.applyUpdateAudit(payload);
    AuditPayloadHelper.applyUpdateAudit(payload.getUser());
  }

  private static String getUserEmailFromEmployeeResponse(EmployeeResponseDto res) {
    if (res == null || res.getData() == null || res.getData().getUser() == null) {
      return null;
    }
    return res.getData().getUser().getEmail();
  }
}
