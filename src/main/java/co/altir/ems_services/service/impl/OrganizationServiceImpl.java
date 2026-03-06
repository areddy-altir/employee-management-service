package co.altir.ems_services.service.impl;

import co.altir.dbmanagement.dataaccess.filter.AbstractFilterDto;
import co.altir.ems_services.models.dto.BooleanReadByIdResponseDto;
import co.altir.ems_services.models.dto.OrganizationArrayResponseDto;
import co.altir.ems_services.models.dto.OrganizationDto;
import co.altir.ems_services.models.dto.OrganizationResponseDto;
import co.altir.ems_services.service.OrganizationService;
import co.altir.ems_services.util.AuditPayloadHelper;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl extends OrganizationDto.Service implements OrganizationService {

  @Override
  public OrganizationArrayResponseDto findOrganization(AbstractFilterDto filter, Pageable pageable) {
    return super.findOrganization(filter, pageable);
  }

  @Override
  public OrganizationResponseDto findByIdOrganization(UUID id) {
    return super.findByIdOrganization(id);
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

  @Override
  public BooleanReadByIdResponseDto deleteOrganization(UUID id) {
    return super.deleteOrganization(id);
  }
}

