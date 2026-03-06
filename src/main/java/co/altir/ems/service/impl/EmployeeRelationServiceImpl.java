package co.altir.ems.service.impl;

import co.altir.ems.models.dto.BooleanReadByIdResponseDto;
import co.altir.ems.models.dto.EmployeeRelationDto;
import co.altir.ems.models.dto.EmployeeRelationResponseDto;
import co.altir.ems.service.EmployeeRelationService;
import co.altir.ems.util.AuditPayloadHelper;
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

