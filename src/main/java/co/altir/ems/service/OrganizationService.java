package co.altir.ems.service;

import co.altir.dbmanagement.dataaccess.filter.AbstractFilterDto;
import co.altir.ems.models.dto.BooleanReadByIdResponseDto;
import co.altir.ems.models.dto.OrganizationArrayResponseDto;
import co.altir.ems.models.dto.OrganizationDto;
import co.altir.ems.models.dto.OrganizationResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface OrganizationService {

  OrganizationArrayResponseDto findOrganization(AbstractFilterDto filter, Pageable pageable);

  OrganizationResponseDto findByIdOrganization(UUID id);

  OrganizationResponseDto createOrganization(OrganizationDto payload);

  OrganizationResponseDto patchOrganization(UUID id, OrganizationDto payload);

  BooleanReadByIdResponseDto deleteOrganization(UUID id);
}
