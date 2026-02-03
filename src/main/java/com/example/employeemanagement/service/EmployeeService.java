package com.example.employeemanagement.service;

import com.example.employeemanagement.config.KeycloakUserHelper;
import com.example.employeemanagement.models.dto.AddressDto;
import com.example.employeemanagement.models.dto.BooleanReadByIdResponseDto;
import com.example.employeemanagement.models.dto.EmployeeDto;
import com.example.employeemanagement.models.dto.EmployeeResponseDto;
import com.example.employeemanagement.models.dto.UserDto;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class EmployeeService extends EmployeeDto.Service {

  private final KeycloakUserHelper keycloakUserHelper;

  @Override
  public EmployeeResponseDto createEmployee(EmployeeDto payload) {
    UserDto user = payload.getUser();
    if (user != null && user.getEmail() != null && !user.getEmail().isBlank()) {
      String tempPassword = generateTemporaryPassword();
      AddressDto addr = user.getAddress();
      String addressStr = toAddressString(addr);
      String country = addr != null ? addr.getCountry() : null;
      keycloakUserHelper.createUserInKeycloak(
          user.getEmail(),
          user.getName(),
          user.getPhone(),
          addressStr,
          country,
          tempPassword);
      log.info(
          "Temporary password for {} (share with user, they must change on first login): {}",
          user.getEmail(),
          tempPassword);
    }
    return super.createEmployee(payload);
  }

  private static String toAddressString(AddressDto a) {
    if (a == null) return null;
    List<String> parts = new ArrayList<>();
    if (a.getLine1() != null && !a.getLine1().isBlank()) parts.add(a.getLine1());
    if (a.getLine2() != null && !a.getLine2().isBlank()) parts.add(a.getLine2());
    if (a.getCity() != null && !a.getCity().isBlank()) parts.add(a.getCity());
    if (a.getState() != null && !a.getState().isBlank()) parts.add(a.getState());
    if (a.getZip() != null && !a.getZip().isBlank()) parts.add(a.getZip());
    if (a.getCountry() != null && !a.getCountry().isBlank()) parts.add(a.getCountry());
    return parts.isEmpty() ? null : String.join(", ", parts);
  }

  private String generateTemporaryPassword() {
    return "Temp" + UUID.randomUUID().toString().replace("-", "").substring(0, 8) + "!";
  }

  @Override
  public EmployeeResponseDto patchEmployee(UUID id, EmployeeDto payload) {
    EmployeeResponseDto existing = findByIdEmployee(id);
    String email = null;
    if (existing != null && existing.getData() != null && existing.getData().getUser() != null) {
      email = existing.getData().getUser().getEmail();
    }
    EmployeeResponseDto result = super.patchEmployee(id, payload);
    if (email != null && payload.getUser() != null) {
      UserDto userPayload = payload.getUser();
      AddressDto addr = userPayload.getAddress();
      String addressStr = toAddressString(addr);
      String country = addr != null ? addr.getCountry() : null;
      keycloakUserHelper.updateUserInKeycloak(
          email,
          userPayload.getName(),
          userPayload.getPhone(),
          addressStr,
          country);
    }
    return result;
  }

  @Override
  public BooleanReadByIdResponseDto deleteEmployee(UUID id) {
    EmployeeResponseDto existing = findByIdEmployee(id);
    String email = null;
    if (existing != null && existing.getData() != null && existing.getData().getUser() != null) {
      email = existing.getData().getUser().getEmail();
    }
    BooleanReadByIdResponseDto result = super.deleteEmployee(id);
    if (email != null) {
      keycloakUserHelper.deleteUserInKeycloak(email);
    }
    return result;
  }
}
