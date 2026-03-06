package co.altir.ems.controller;

import co.altir.ems.models.dto.BooleanReadByIdResponseDto;
import co.altir.ems.models.dto.EmployeeRelationDto;
import co.altir.ems.models.dto.EmployeeRelationResponseDto;
import co.altir.ems.security.AuthorizationService;
import co.altir.ems.service.EmployeeRelationService;
import co.altir.ems.utils.EmployeeRelationApi;
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

