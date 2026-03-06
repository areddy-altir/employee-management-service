package co.altir.ems.service;

import co.altir.dbmanagement.dataaccess.Projection;
import co.altir.dbmanagement.dataaccess.filter.AbstractFilterDto;
import co.altir.ems.models.dto.BooleanReadByIdResponseDto;
import co.altir.ems.models.dto.EmployeeRelationDto;
import co.altir.ems.models.dto.EmployeeRelationResponseDto;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface EmployeeRelationService {
  EmployeeRelationResponseDto createEmployeeRelations(EmployeeRelationDto payload);

  BooleanReadByIdResponseDto deleteEmployeeRelations(UUID id);

  List<EmployeeRelationDto> find(AbstractFilterDto filter, Pageable pageable, Projection projection);
}

