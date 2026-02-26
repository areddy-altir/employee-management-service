package com.example.employeemanagement.controller;

import co.altir.dbmanagement.dataaccess.filter.AbstractFilterDto;
import com.example.employeemanagement.models.dto.BooleanReadByIdResponseDto;
import com.example.employeemanagement.models.dto.ManagerArrayResponseDto;
import com.example.employeemanagement.models.dto.ManagerDto;
import com.example.employeemanagement.models.dto.ManagerResponseDto;
import com.example.employeemanagement.service.ManagerService;
import com.example.employeemanagement.utils.ManagerApi;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ManagerController implements ManagerApi {

  private final ManagerService managerService;

  @Override
  public ResponseEntity<ManagerResponseDto> findByIdManager(UUID id) {
    return ResponseEntity.ok(managerService.findByIdManager(id));
  }

  @Override
  public ResponseEntity<ManagerArrayResponseDto> findManager(AbstractFilterDto filter, Pageable pageable) {
    return ResponseEntity.ok(managerService.findManager(filter, pageable));
  }

  @Override
  public ResponseEntity<ManagerResponseDto> createManager(ManagerDto managerDto) {
    return ResponseEntity.ok(managerService.createManager(managerDto));
  }

  @Override
  public ResponseEntity<ManagerResponseDto> patchManager(UUID id, ManagerDto managerDto) {
    return ResponseEntity.ok(managerService.patchManager(id, managerDto));
  }

  @Override
  public ResponseEntity<BooleanReadByIdResponseDto> deleteManager(UUID id) {
    return ResponseEntity.ok(managerService.deleteManager(id));
  }
}



