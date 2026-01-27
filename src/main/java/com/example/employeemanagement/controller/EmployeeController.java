package com.example.employeemanagement.controller;

import com.example.employeemanagement.models.dto.BooleanReadByIdResponseDto;
import com.example.employeemanagement.models.dto.EmployeeDto;
import com.example.employeemanagement.models.dto.EmployeeResponseDto;
import com.example.employeemanagement.service.EmployeeService;
import com.example.employeemanagement.utils.EmployeeApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
public class EmployeeController implements EmployeeApi {

    private final EmployeeService employeeService;

    @Override
    public ResponseEntity<EmployeeResponseDto> findByIdEmployee(UUID id) {
        return ResponseEntity.ok(employeeService.findByIdEmployee(id));
    }

    @Override
    public ResponseEntity<EmployeeResponseDto> createEmployee(EmployeeDto employeeDto) {
        return ResponseEntity.ok(employeeService.createEmployee(employeeDto));
    }

    @Override
    public ResponseEntity<EmployeeResponseDto> patchEmployee(UUID id, EmployeeDto employeeDto) {
        return ResponseEntity.ok(employeeService.patchEmployee(id, employeeDto));
    }

    @Override
    public ResponseEntity<BooleanReadByIdResponseDto> deleteEmployee(UUID id) {
        return ResponseEntity.ok(employeeService.deleteEmployee(id));
    }

}
