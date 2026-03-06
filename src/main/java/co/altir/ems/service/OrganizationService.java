package com.example.employeemanagement.service;

import co.altir.dbmanagement.dataaccess.filter.AbstractFilterDto;
import com.example.employeemanagement.util.AuditPayloadHelper;
import com.example.employeemanagement.models.dto.OrganizationArrayResponseDto;
import com.example.employeemanagement.models.dto.OrganizationDto;
import com.example.employeemanagement.models.dto.OrganizationResponseDto;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class OrganizationService extends OrganizationDto.Service {

  public OrganizationArrayResponseDto findOrganization(AbstractFilterDto filter, Pageable pageable) {
    return super.findOrganization(filter, pageable);
  }

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
