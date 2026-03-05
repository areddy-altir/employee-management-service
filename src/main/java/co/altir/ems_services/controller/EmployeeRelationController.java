package co.altir.ems_services.controller;

import co.altir.ems_services.models.dto.BooleanReadByIdResponseDto;
import co.altir.ems_services.models.dto.EmployeeRelationDto;
import co.altir.ems_services.models.dto.EmployeeRelationResponseDto;
import co.altir.ems_services.security.AuthorizationService;
import co.altir.ems_services.service.EmployeeRelationService;
import co.altir.ems_services.utils.EmployeeRelationApi;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeRelationController implements EmployeeRelationApi {

  private final EmployeeRelationService employeeRelationService;
  private final AuthorizationService authorizationService;

  @Override
  public ResponseEntity<EmployeeRelationResponseDto> createEmployeeRelations(EmployeeRelationDto body) {
    authorizationService.requireAdmin();
    return ResponseEntity.ok(employeeRelationService.createEmployeeRelations(body));
  }

  @Override
  public ResponseEntity<BooleanReadByIdResponseDto> deleteEmployeeRelations(UUID id) {
    authorizationService.requireAdmin();
    return ResponseEntity.ok(employeeRelationService.deleteEmployeeRelations(id));
  }
}

