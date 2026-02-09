package com.example.employeemanagement.service;

import com.example.employeemanagement.util.AuditPayloadHelper;
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
    AuditPayloadHelper.applyCreateAudit(payload);
    return super.createOrganization(payload);
  }

  @Override
  public OrganizationResponseDto patchOrganization(UUID id, OrganizationDto payload) {
    AuditPayloadHelper.applyUpdateAudit(payload);
    return super.patchOrganization(id, payload);
  }
}
