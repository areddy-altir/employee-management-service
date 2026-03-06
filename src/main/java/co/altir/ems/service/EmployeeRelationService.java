package co.altir.ems.service;

import co.altir.ems.models.dto.BooleanReadByIdResponseDto;
import co.altir.ems.models.dto.EmployeeRelationDto;
import co.altir.ems.models.dto.EmployeeRelationResponseDto;
import java.util.UUID;

public interface EmployeeRelationService {
  EmployeeRelationResponseDto createEmployeeRelations(EmployeeRelationDto payload);

  BooleanReadByIdResponseDto deleteEmployeeRelations(UUID id);
}

