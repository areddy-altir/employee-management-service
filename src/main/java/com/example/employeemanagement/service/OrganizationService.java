package com.example.employeemanagement.service;

import com.example.employeemanagement.models.dto.OrganizationDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Transactional
@Service
@RequiredArgsConstructor
public class OrganizationService extends OrganizationDto.Service {

}
