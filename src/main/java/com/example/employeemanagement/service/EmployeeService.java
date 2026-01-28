package com.example.employeemanagement.service;

import com.example.employeemanagement.models.dto.EmployeeDto;
import com.example.employeemanagement.models.dto.EmployeeResponseDto;
import com.example.employeemanagement.models.dto.UserDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

/**
 * Employee Service with Keycloak integration
 * 
 * - Automatically links Employee's User to Keycloak user ID from JWT token
 * - Sets timestamps (createdOn, updatedOn) automatically
 * - Sets createdBy and updatedBy from JWT token username
 * - Sets active = true by default on create
 * - Allows active = false via PATCH
 */
@Transactional
@Service
@RequiredArgsConstructor
public class EmployeeService extends EmployeeDto.Service {

    private final JwtTokenService jwtTokenService;

    @Override
    public EmployeeResponseDto createEmployee(EmployeeDto employeeDto) {
        Instant now = Instant.now();
        String username = jwtTokenService.getUsername();
        
        // Set active = true by default on create (unless explicitly set)
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
            // Link to Keycloak user ID from JWT token
            String keycloakUserId = jwtTokenService.getKeycloakUserId();
            userDto.keycloakUserId(keycloakUserId);
            
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
        
        return super.createEmployee(employeeDto);
    }

    @Override
    public EmployeeResponseDto patchEmployee(UUID id, EmployeeDto employeeDto) {
        Instant now = Instant.now();
        String username = jwtTokenService.getUsername();
        
        // Always update the timestamp and updatedBy on patch
        employeeDto.updatedOn(now);
        employeeDto.updatedBy(username);
        
        // Handle nested User if present
        UserDto userDto = employeeDto.getUser();
        if (userDto != null) {
            // Update timestamp and updatedBy for nested User
            userDto.updatedOn(now);
            userDto.updatedBy(username);
            
            // Note: active can be set to false via PATCH
            // If active is provided in request, it will be used
            // If not provided, existing value is kept
        }
        
        return super.patchEmployee(id, employeeDto);
    }
}
