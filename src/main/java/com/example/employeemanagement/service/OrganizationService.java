package com.example.employeemanagement.service;

import com.example.employeemanagement.config.AuditHelper;
import com.example.employeemanagement.models.dto.OrganizationDto;
import com.example.employeemanagement.models.dto.OrganizationResponseDto;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class OrganizationService extends OrganizationDto.Service {

  @Override
  public OrganizationResponseDto createOrganization(OrganizationDto payload) {
    Instant now = Instant.now();
    String auditor = AuditHelper.getCurrentAuditor();
    payload
        .createdOn(now)
        .updatedOn(now)
        .createdBy(auditor)
        .updatedBy(auditor)
        .active(true);
    return super.createOrganization(payload);
  }

  @Override
  public OrganizationResponseDto patchOrganization(UUID id, OrganizationDto payload) {
    Instant now = Instant.now();
    String auditor = AuditHelper.getCurrentAuditor();
    // Preserve createdOn/createdBy from existing record (don't overwrite with null)
    OrganizationResponseDto existing = findByIdOrganization(id);
    if (existing != null && existing.getData() != null) {
      if (payload.getCreatedOn() == null && existing.getData().getCreatedOn() != null) {
        payload.createdOn(existing.getData().getCreatedOn());
      }
      if (payload.getCreatedBy() == null && existing.getData().getCreatedBy() != null) {
        payload.createdBy(existing.getData().getCreatedBy());
      }
    }
    payload.updatedOn(now).updatedBy(auditor);
    return super.patchOrganization(id, payload);
  }
}
