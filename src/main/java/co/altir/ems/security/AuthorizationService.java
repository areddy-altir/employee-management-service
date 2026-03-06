package co.altir.ems.security;

import co.altir.ems.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

  private final SecurityUtil securityUtil;

  public void requireAdmin() {
    if (!securityUtil.hasRole("ADMIN")) {
      throw new AccessDeniedException("ADMIN only");
    }
  }

  public void requireAdminOrManager() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    System.out.println("Authorities: " + (auth != null ? auth.getAuthorities() : "null"));
    if (!(securityUtil.hasRole("ADMIN") || securityUtil.hasRole("MANAGER"))) {
      throw new AccessDeniedException("ADMIN or MANAGER only");
    }
  }

  public void requireManagerOrAdmin() {
    if (securityUtil.hasRole("ADMIN")) {
      return;
    }
    if (!securityUtil.hasRole("MANAGER") && !securityUtil.hasRole("SENIOR_MANAGER")) {
      throw new AccessDeniedException("Manager only");
    }
  }
}

