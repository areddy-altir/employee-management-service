package co.altir.ems_services.service;

import co.altir.ems_services.models.dto.BooleanReadByIdResponseDto;
import co.altir.ems_services.models.dto.EmployeeRelationDto;
import co.altir.ems_services.models.dto.EmployeeRelationResponseDto;
import java.util.UUID;

public interface EmployeeRelationService {
  EmployeeRelationResponseDto createEmployeeRelations(EmployeeRelationDto payload);

  BooleanReadByIdResponseDto deleteEmployeeRelations(UUID id);
}

