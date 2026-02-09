package com.example.employeemanagement.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * For POST /employee, reads the JSON body once, extracts user.password (writeOnly in OpenAPI;
 * not present on generated UserDto), and stores it in a request attribute so the controller
 * can pass it to the service. Does not log or cache the password.
 */
@Component
@Order(1)
public class CreateEmployeePasswordExtractorFilter extends OncePerRequestFilter {

  public static final String REQUEST_ATTR_PASSWORD = "createEmployeePassword";

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    if (!"POST".equalsIgnoreCase(request.getMethod())) {
      return true;
    }
    String path = request.getRequestURI();
    if (path == null) {
      return true;
    }
    // Match .../employee (with or without context path)
    if (!path.endsWith("/employee")) {
      return true;
    }
    String contentType = request.getContentType();
    if (contentType == null || !contentType.toLowerCase().startsWith("application/json")) {
      return true;
    }
    return false;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    byte[] body = request.getInputStream().readAllBytes();
    String password = null;
    if (body.length > 0) {
      try {
        JsonNode root = OBJECT_MAPPER.readTree(body);
        if (root != null && root.has("user") && root.get("user").has("password")) {
          JsonNode p = root.get("user").get("password");
          if (p != null && !p.isNull()) {
            password = p.asText(null);
          }
        }
      } catch (Exception ignored) {
        // Leave password null; validation will handle invalid JSON elsewhere
      }
    }
    if (password != null) {
      request.setAttribute(REQUEST_ATTR_PASSWORD, password);
    }
    HttpServletRequest wrapped =
        new HttpServletRequestWrapper(request) {
          @Override
          public InputStream getInputStream() {
            return new ByteArrayInputStream(body);
          }
        };
    filterChain.doFilter(wrapped, response);
  }
}
