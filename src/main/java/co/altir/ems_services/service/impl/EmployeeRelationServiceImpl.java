package co.altir.ems_services.service.impl;

import co.altir.ems_services.models.dto.BooleanReadByIdResponseDto;
import co.altir.ems_services.models.dto.EmployeeRelationDto;
import co.altir.ems_services.models.dto.EmployeeRelationResponseDto;
import co.altir.ems_services.service.EmployeeRelationService;
import co.altir.ems_services.util.AuditPayloadHelper;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeRelationServiceImpl extends EmployeeRelationDto.Service
    implements EmployeeRelationService {

  @Override
  public EmployeeRelationResponseDto createEmployeeRelations(EmployeeRelationDto payload) {
    AuditPayloadHelper.applyCreateAudit(payload);
    return super.createEmployeeRelations(payload);
  }

  @Override
  public BooleanReadByIdResponseDto deleteEmployeeRelations(UUID id) {
    return super.deleteEmployeeRelations(id);
  }
}

