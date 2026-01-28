package com.example.employeemanagement.service;

import com.example.employeemanagement.models.dto.EmployeeDto;
import com.example.employeemanagement.models.dto.OrganizationDto;
import com.example.employeemanagement.models.dto.OrganizationResponseDto;
import com.example.employeemanagement.models.dto.UserDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Organization Service
 * 
 * - Sets timestamps (createdOn, updatedOn) automatically
 * - Sets createdBy and updatedBy from JWT token username
 * - Sets active = true by default on create
 * - Allows active = false via PATCH
 * - Handles nested Employees and Users
 */
@Transactional
@Service
@RequiredArgsConstructor
public class OrganizationService extends OrganizationDto.Service {

    private final JwtTokenService jwtTokenService;

    @Override
    public OrganizationResponseDto createOrganization(OrganizationDto payload) {
        Instant now = Instant.now();
        String username = jwtTokenService.getUsername();
        
        // Set active = true by default on create (unless explicitly set)
        if (payload.getActive() == null) {
            payload.active(true);
        }
        
        // Set timestamps for Organization
        if (payload.getCreatedOn() == null) {
            payload.createdOn(now);
        }
        if (payload.getUpdatedOn() == null) {
            payload.updatedOn(now);
        }
        
        // Set createdBy and updatedBy for Organization
        if (payload.getCreatedBy() == null) {
            payload.createdBy(username);
        }
        if (payload.getUpdatedBy() == null) {
            payload.updatedBy(username);
        }
        
        // Handle nested Employees and their Users
        List<EmployeeDto> employees = payload.getEmployees();
        if (employees != null) {
            for (EmployeeDto employeeDto : employees) {
                setEmployeeTimestamps(employeeDto, now, username);
            }
        }
        
        return super.createOrganization(payload);
    }

    @Override
    public OrganizationResponseDto patchOrganization(UUID id, OrganizationDto payload) {
        Instant now = Instant.now();
        String username = jwtTokenService.getUsername();
        
        // Always update the timestamp and updatedBy on patch
        payload.updatedOn(now);
        payload.updatedBy(username);
        
        // Note: active can be set to false via PATCH
        // If active is provided in request, it will be used
        // If not provided, existing value is kept
        
        // Update timestamps and updatedBy for nested Employees and their Users if present
        List<EmployeeDto> employees = payload.getEmployees();
        if (employees != null) {
            for (EmployeeDto employeeDto : employees) {
                employeeDto.updatedOn(now);
                employeeDto.updatedBy(username);
                
                UserDto userDto = employeeDto.getUser();
                if (userDto != null) {
                    userDto.updatedOn(now);
                    userDto.updatedBy(username);
                }
            }
        }
        
        return super.patchOrganization(id, payload);
    }

    /**
     * Helper method to set timestamps, createdBy, updatedBy, and active for Employee and nested User
     */
    private void setEmployeeTimestamps(EmployeeDto employeeDto, Instant now, String username) {
        // Set active = true by default for Employee
        if (employeeDto.getActive() == null) {
            employeeDto.active(true);
        }
        
        // Set timestamps for Employee
        if (employeeDto.getCreatedOn() == null) {
            employeeDto.createdOn(now);
        }
        if (employeeDto.getUpdatedOn() == null) {
            employeeDto.updatedOn(now);
        }
        
        // Set createdBy and updatedBy for Employee
        if (employeeDto.getCreatedBy() == null) {
            employeeDto.createdBy(username);
        }
        if (employeeDto.getUpdatedBy() == null) {
            employeeDto.updatedBy(username);
        }
        
        // Handle nested User
        UserDto userDto = employeeDto.getUser();
        if (userDto != null) {
            // Set active = true by default for User
            if (userDto.getActive() == null) {
                userDto.active(true);
            }
            
            // Set timestamps for nested User
            if (userDto.getCreatedOn() == null) {
                userDto.createdOn(now);
            }
            if (userDto.getUpdatedOn() == null) {
                userDto.updatedOn(now);
            }
            
            // Set createdBy and updatedBy for nested User
            if (userDto.getCreatedBy() == null) {
                userDto.createdBy(username);
            }
            if (userDto.getUpdatedBy() == null) {
                userDto.updatedBy(username);
            }
        }
    }
}
