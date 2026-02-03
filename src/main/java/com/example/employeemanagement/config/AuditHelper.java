package com.example.employeemanagement.config;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public final class AuditHelper {

  private AuditHelper() {}

  /**
   * Returns the current auditor (user name or email) from the JWT, or "system" for service accounts.
   */
  public static String getCurrentAuditor() {
    return Optional.ofNullable(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .filter(Authentication::isAuthenticated)
        .map(Authentication::getPrincipal)
        .filter(Jwt.class::isInstance)
        .map(Jwt.class::cast)
        .map(AuditHelper::extractAuditorName)
        .orElse("system");
  }

  private static String extractAuditorName(Jwt jwt) {
    String name = jwt.getClaimAsString("name");
    if (name != null && !name.isBlank()) {
      return name;
    }
    String preferredUsername = jwt.getClaimAsString("preferred_username");
    return preferredUsername != null ? preferredUsername : "system";
  }
}
