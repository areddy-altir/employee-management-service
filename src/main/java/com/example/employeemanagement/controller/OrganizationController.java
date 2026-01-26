package com.example.employeemanagement.controller;


import com.example.employeemanagement.models.dto.*;
import com.example.employeemanagement.service.OrganizationService;
import com.example.employeemanagement.utils.OrganizationApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
@RestController
@RequiredArgsConstructor
public class OrganizationController implements OrganizationApi {

    private final OrganizationService organizationService;
    @Override
    public ResponseEntity<OrganizationResponseDto> findByIdOrganization(UUID id) {
        return ResponseEntity.ok(organizationService.findByIdOrganization(id));
    }

    @Override
    public ResponseEntity<OrganizationResponseDto> createOrganization(OrganizationDto organizationDto) {
        return ResponseEntity.ok(organizationService.createOrganization(organizationDto));
    }

    @Override
    public ResponseEntity<OrganizationResponseDto> patchOrganization(UUID id, OrganizationDto organizationDto) {
        return ResponseEntity.ok(organizationService.patchOrganization(id, organizationDto));
    }

    @Override
    public ResponseEntity<BooleanReadByIdResponseDto> deleteOrganization(UUID id) {
        return ResponseEntity.ok(organizationService.deleteOrganization(id));
    }
}
