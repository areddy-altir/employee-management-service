package co.altir.ems.security;

import co.altir.ems.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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

  public void requireManagerOrAdmin() {
    if (securityUtil.hasRole("ADMIN")) {
      return;
    }
    if (!securityUtil.hasRole("MANAGER") && !securityUtil.hasRole("SENIOR_MANAGER")) {
      throw new AccessDeniedException("Manager only");
    }
  }
}

