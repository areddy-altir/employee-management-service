package co.altir.ems.controller;

import co.altir.dbmanagement.dataaccess.filter.AbstractFilterDto;
import co.altir.ems.models.dto.BooleanReadByIdResponseDto;
import co.altir.ems.models.dto.EmployeeArrayResponseDto;
import co.altir.ems.models.dto.EmployeeDto;
import co.altir.ems.models.dto.EmployeeResponseDto;
import co.altir.ems.service.EmployeeService;
import co.altir.ems.utils.EmployeeApi;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EmployeeController implements EmployeeApi {

    private final EmployeeService employeeService;
    @Override
    public ResponseEntity<EmployeeResponseDto> findByIdEmployee(UUID id) {
        EmployeeResponseDto response = employeeService.findByIdEmployees(id);
        if (response != null && response.getData() != null && response.getData().getOrganization() != null) {
            response.getData().getOrganization().setEmployees(null);
        }
        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<EmployeeArrayResponseDto> findEmployee(AbstractFilterDto filter, Pageable pageable) {
        return ResponseEntity.ok(employeeService.findEmployees(filter, pageable));
    }
   @Override
   public ResponseEntity<EmployeeResponseDto> createEmployee(EmployeeDto employeeDto) {
       return ResponseEntity.ok(employeeService.createEmployees(employeeDto));
   }

    @Override
    public ResponseEntity<EmployeeResponseDto> patchEmployee(UUID id, EmployeeDto employeeDto) {
        return ResponseEntity.ok(employeeService.patchEmployees(id, employeeDto));
    }
  @Override
    public ResponseEntity<BooleanReadByIdResponseDto> deleteEmployee(UUID id) {
        return ResponseEntity.ok(employeeService.deleteEmployees(id));
    }

}

