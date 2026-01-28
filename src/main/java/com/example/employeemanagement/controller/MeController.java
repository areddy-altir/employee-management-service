package com.example.employeemanagement.controller;

import com.example.employeemanagement.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Example endpoint to demonstrate JWT token usage
 * 
 * GET /me - Returns current user information from JWT token
 * 
 * This endpoint:
 * - Requires authentication (JWT token in Authorization header)
 * - Extracts user info from JWT token
 * - Returns user details without querying database
 */
@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MeController {

    private final JwtTokenService jwtTokenService;

    /**
     * Get current user information from JWT token
     * 
     * Usage:
     * GET /me
     * Authorization: Bearer <jwt-token>
     * 
     * @return User information extracted from JWT token
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        Map<String, Object> userInfo = new HashMap<>();
        
        // Extract information from JWT token
        userInfo.put("keycloakUserId", jwtTokenService.getKeycloakUserId());
        userInfo.put("email", jwtTokenService.getEmail());
        userInfo.put("username", jwtTokenService.getUsername());
        userInfo.put("realmRoles", jwtTokenService.getRealmRoles());
        userInfo.put("allClaims", jwtTokenService.getAllClaims());
        
        return ResponseEntity.ok(userInfo);
    }
}
