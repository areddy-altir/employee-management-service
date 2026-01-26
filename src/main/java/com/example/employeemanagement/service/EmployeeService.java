package com.example.employeemanagement.service;

import com.example.employeemanagement.models.dto.EmployeeDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Transactional
@Service
@RequiredArgsConstructor
public class EmployeeService extends EmployeeDto.Service {
}
