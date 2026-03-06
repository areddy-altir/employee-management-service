package co.altir.ems.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

  public String username() {
    Authentication a = SecurityContextHolder.getContext().getAuthentication();
    return a != null ? a.getName() : null;
  }

  public boolean hasRole(String role) {
    Authentication a = SecurityContextHolder.getContext().getAuthentication();
    if (a == null) {
      return false;
    }
    return a.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals("ROLE_" + role));
  }
}

