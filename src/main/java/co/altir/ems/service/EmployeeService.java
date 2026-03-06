package co.altir.ems.service;

import co.altir.dbmanagement.dataaccess.filter.AbstractFilterDto;
import co.altir.ems.models.dto.BooleanReadByIdResponseDto;
import co.altir.ems.models.dto.EmployeeArrayResponseDto;
import co.altir.ems.models.dto.EmployeeDto;
import co.altir.ems.models.dto.EmployeeHierarchyNodeResponseDto;
import co.altir.ems.models.dto.EmployeeResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

  EmployeeResponseDto findByIdEmployees(UUID id);

  EmployeeArrayResponseDto findEmployees(AbstractFilterDto filter, Pageable pageable);

  EmployeeResponseDto createEmployees(EmployeeDto payload);

  EmployeeResponseDto patchEmployees(UUID id, EmployeeDto payload);

  BooleanReadByIdResponseDto deleteEmployees(UUID id);

  EmployeeResponseDto updateEmployeeRole(UUID employeeId);

  EmployeeArrayResponseDto getSubordinates(UUID employeeId);

  EmployeeResponseDto getSupervisor(UUID employeeId);

  EmployeeHierarchyNodeResponseDto getHierarchy(UUID employeeId);
}
