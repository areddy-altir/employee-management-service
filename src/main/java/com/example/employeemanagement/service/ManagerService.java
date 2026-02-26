package com.example.employeemanagement.service;

import co.altir.dbmanagement.dataaccess.filter.AbstractFilterDto;
import com.example.employeemanagement.integration.keycloak.KeycloakManagerRoleSyncService;
import com.example.employeemanagement.models.dto.BooleanReadByIdResponseDto;
import com.example.employeemanagement.models.dto.ManagerArrayResponseDto;
import com.example.employeemanagement.models.dto.ManagerDto;
import com.example.employeemanagement.models.dto.ManagerResponseDto;
import com.example.employeemanagement.util.AuditPayloadHelper;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ManagerService extends ManagerDto.Service {

  private final KeycloakManagerRoleSyncService keycloakManagerRoleSyncService;


}
