package com.example.employeemanagement.controller;

import com.example.employeemanagement.models.dto.BooleanReadByIdResponseDto;
import com.example.employeemanagement.models.dto.ManagerDto;
import com.example.employeemanagement.models.dto.ManagerResponseDto;
import com.example.employeemanagement.service.ManagerService;
import com.example.employeemanagement.utils.ManagerApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ManagerController implements ManagerApi {

    private final ManagerService managerService;

    @Override
    public ResponseEntity<ManagerResponseDto> findByIdManager(UUID id) {
        return ResponseEntity.ok(managerService.findByIdManager(id));
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