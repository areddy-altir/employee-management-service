package com.example.employeemanagement.service;

import com.example.employeemanagement.config.AuditHelper;
import com.example.employeemanagement.models.dto.OrganizationDto;
import com.example.employeemanagement.models.dto.OrganizationResponseDto;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class OrganizationService extends OrganizationDto.Service {

  @Override
  public OrganizationResponseDto createOrganization(OrganizationDto payload) {
    String auditor = AuditHelper.getCurrentAuditor();
    payload
        .createdBy(auditor)
        .updatedBy(auditor)
        .active(true);
    return super.createOrganization(payload);
  }

  @Override
  public OrganizationResponseDto patchOrganization(UUID id, OrganizationDto payload) {
    String auditor = AuditHelper.getCurrentAuditor();
    payload.updatedBy(auditor);
    return super.patchOrganization(id, payload);
  }
}
