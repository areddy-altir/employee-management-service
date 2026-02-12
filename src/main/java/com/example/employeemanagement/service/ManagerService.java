package com.example.employeemanagement.service;

import com.example.employeemanagement.models.dto.BooleanReadByIdResponseDto;
import com.example.employeemanagement.models.dto.ManagerDto;
import com.example.employeemanagement.models.dto.ManagerResponseDto;
import com.example.employeemanagement.util.AuditPayloadHelper;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class ManagerService extends ManagerDto.Service {

  @Override
  public ManagerResponseDto createManager(ManagerDto payload) {
    AuditPayloadHelper.applyCreateAudit(payload);
    return super.createManager(payload);
  }

  @Override
  public ManagerResponseDto patchManager(UUID id, ManagerDto payload) {
    AuditPayloadHelper.applyUpdateAudit(payload);
    return super.patchManager(id, payload);
  }

  @Override
  public BooleanReadByIdResponseDto deleteManager(UUID id) {
    return super.deleteManager(id);
  }
}
