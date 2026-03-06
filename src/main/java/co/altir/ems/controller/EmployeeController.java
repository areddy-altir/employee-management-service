package co.altir.ems.controller;

import co.altir.dbmanagement.dataaccess.filter.AbstractFilterDto;
import co.altir.ems.models.dto.BooleanReadByIdResponseDto;
import co.altir.ems.models.dto.EmployeeArrayResponseDto;
import co.altir.ems.models.dto.EmployeeDto;
import co.altir.ems.models.dto.EmployeeHierarchyNodeResponseDto;
import co.altir.ems.models.dto.EmployeeResponseDto;
import co.altir.ems.service.EmployeeService;
import co.altir.ems.utils.EmployeeApi;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeController implements EmployeeApi {

  private final EmployeeService employeeService;

  @Override
  public ResponseEntity<EmployeeResponseDto> findByIdEmployees(UUID id) {
    EmployeeResponseDto response = employeeService.findByIdEmployees(id);
    if (response != null && response.getData() != null && response.getData().getOrganization() != null) {
      response.getData().getOrganization().setEmployees(null);
    }
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<EmployeeArrayResponseDto> findEmployees(AbstractFilterDto filter, Pageable pageable) {
    return ResponseEntity.ok(employeeService.findEmployees(filter, pageable));
  }

  @Override
  public ResponseEntity<EmployeeResponseDto> createEmployees(EmployeeDto employeeDto) {
    return ResponseEntity.ok(employeeService.createEmployees(employeeDto));
  }

  @Override
  public ResponseEntity<EmployeeResponseDto> patchEmployees(UUID id, EmployeeDto employeeDto) {
    return ResponseEntity.ok(employeeService.patchEmployees(id, employeeDto));
  }

  @Override
  public ResponseEntity<BooleanReadByIdResponseDto> deleteEmployees(UUID id) {
    return ResponseEntity.ok(employeeService.deleteEmployees(id));
  }

  @Override
  public ResponseEntity<EmployeeResponseDto> updateEmployeeRole(UUID employeeId) {
    return ResponseEntity.ok(employeeService.updateEmployeeRole(employeeId));
  }

  @Override
  public ResponseEntity<EmployeeArrayResponseDto> getSubordinates(UUID employeeId) {
    return ResponseEntity.ok(employeeService.getSubordinates(employeeId));
  }

  @Override
  public ResponseEntity<EmployeeResponseDto> getSupervisor(UUID employeeId) {
    return ResponseEntity.ok(employeeService.getSupervisor(employeeId));
  }

  @Override
  public ResponseEntity<EmployeeHierarchyNodeResponseDto> getHierarchy(UUID employeeId) {
    return ResponseEntity.ok(employeeService.getHierarchy(employeeId));
  }
}

