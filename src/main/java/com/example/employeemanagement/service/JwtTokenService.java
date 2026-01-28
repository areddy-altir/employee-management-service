package com.example.employeemanagement.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service to extract information from JWT tokens issued by Keycloak
 * 
 * Keycloak JWT tokens contain:
 * - sub: User ID (keycloak_user_id)
 * - email: User email
 * - preferred_username: Username
 * - realm_access.roles: List of realm roles
 * - resource_access.{client-id}.roles: List of client roles
 */
@Service
public class JwtTokenService {

    /**
     * Get the current authenticated JWT token
     */
    private Jwt getCurrentJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken();
        }
        
        throw new IllegalStateException("No JWT token found in security context");
    }

    /**
     * Extract Keycloak User ID (sub claim) from JWT token
     * This is the unique identifier for the user in Keycloak
     * 
     * @return Keycloak user ID (UUID as string)
     */
    public String getKeycloakUserId() {
        Jwt jwt = getCurrentJwt();
        return jwt.getSubject(); // "sub" claim
    }

    /**
     * Extract email from JWT token
     * 
     * @return User email or null if not present
     */
    public String getEmail() {
        Jwt jwt = getCurrentJwt();
        return jwt.getClaimAsString("email");
    }

    /**
     * Extract username from JWT token
     * 
     * @return Username or null if not present
     */
    public String getUsername() {
        Jwt jwt = getCurrentJwt();
        return jwt.getClaimAsString("preferred_username");
    }

    /**
     * Extract realm roles from JWT token
     * These are roles defined at the realm level in Keycloak
     * 
     * Example roles: ADMIN, HR, MANAGER, EMPLOYEE
     * 
     * @return List of realm role names
     */
    @SuppressWarnings("unchecked")
    public List<String> getRealmRoles() {
        Jwt jwt = getCurrentJwt();
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        
        if (realmAccess != null) {
            Object rolesObj = realmAccess.get("roles");
            if (rolesObj instanceof List) {
                return (List<String>) rolesObj;
            }
        }
        
        return List.of();
    }

    /**
     * Extract client roles from JWT token
     * These are roles defined for a specific client in Keycloak
     * 
     * @param clientId The Keycloak client ID
     * @return List of client role names
     */
    @SuppressWarnings("unchecked")
    public List<String> getClientRoles(String clientId) {
        Jwt jwt = getCurrentJwt();
        Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
        
        if (resourceAccess != null) {
            Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(clientId);
            if (clientAccess != null) {
                Object rolesObj = clientAccess.get("roles");
                if (rolesObj instanceof List) {
                    return (List<String>) rolesObj;
                }
            }
        }
        
        return List.of();
    }

    /**
     * Check if the current user has a specific realm role
     * 
     * @param role Role name (e.g., "ADMIN", "HR")
     * @return true if user has the role
     */
    public boolean hasRealmRole(String role) {
        return getRealmRoles().contains(role);
    }

    /**
     * Check if the current user has a specific client role
     * 
     * @param clientId Keycloak client ID
     * @param role Role name
     * @return true if user has the role
     */
    public boolean hasClientRole(String clientId, String role) {
        return getClientRoles(clientId).contains(role);
    }

    /**
     * Get all claims from the JWT token (for debugging)
     * 
     * @return Map of all claims
     */
    public Map<String, Object> getAllClaims() {
        Jwt jwt = getCurrentJwt();
        return jwt.getClaims();
    }
}
