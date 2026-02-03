package com.example.employeemanagement.service;

import com.example.employeemanagement.config.KeycloakUserHelper;
import com.example.employeemanagement.models.dto.AddressDto;
import com.example.employeemanagement.models.dto.BooleanReadByIdResponseDto;
import com.example.employeemanagement.models.dto.EmployeeDto;
import com.example.employeemanagement.models.dto.EmployeeResponseDto;
import com.example.employeemanagement.models.dto.UserDto;
import jakarta.transaction.Transactional;
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
      keycloakUserHelper.createUserInKeycloak(
          user.getEmail(),
          user.getName(),
          user.getPhone(),
          formatAddressForKeycloak(user.getAddress()),
          tempPassword);
      log.info(
          "Temporary password for {} (share with user, they must change on first login): {}",
          user.getEmail(),
          tempPassword);
    }
    return super.createEmployee(payload);
  }

  private String generateTemporaryPassword() {
    return "Temp" + UUID.randomUUID().toString().replace("-", "").substring(0, 8) + "!";
  }

  private static String formatAddressForKeycloak(AddressDto address) {
    if (address == null) return "";
    StringBuilder sb = new StringBuilder();
    if (address.getLine1() != null) sb.append(address.getLine1());
    if (address.getLine2() != null && !address.getLine2().isBlank())
      sb.append(", ").append(address.getLine2());
    if (address.getCity() != null) sb.append(", ").append(address.getCity());
    if (address.getState() != null) sb.append(", ").append(address.getState());
    if (address.getZip() != null) sb.append(" ").append(address.getZip());
    if (address.getCountry() != null) sb.append(", ").append(address.getCountry());
    return sb.toString().replaceFirst("^, ", "");
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
      keycloakUserHelper.updateUserInKeycloak(
          email,
          userPayload.getName(),
          userPayload.getPhone(),
          formatAddressForKeycloak(userPayload.getAddress()));
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
