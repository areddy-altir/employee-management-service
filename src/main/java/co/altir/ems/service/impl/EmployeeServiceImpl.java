package co.altir.ems.service.impl;

import co.altir.dbmanagement.dataaccess.Projection;
import co.altir.dbmanagement.dataaccess.filter.AbstractFilterDto;
import co.altir.ems.integration.keycloak.KeycloakAdminClient;
import co.altir.ems.integration.keycloak.KeycloakUserSyncService;
import co.altir.ems.models.dto.BooleanReadByIdResponseDto;
import co.altir.ems.models.dto.EmployeeArrayResponseDto;
import co.altir.ems.models.dto.EmployeeDto;
import co.altir.ems.models.dto.EmployeeHierarchyNodeDto;
import co.altir.ems.models.dto.EmployeeHierarchyNodeResponseDto;
import co.altir.ems.models.dto.EmployeeRelationDto;
import co.altir.ems.models.dto.EmployeeResponseDto;
import co.altir.ems.models.dto.EmployeeRole;
import co.altir.ems.models.dto.UpdateEmployeeRoleRequestDto;
import co.altir.ems.security.AuthorizationService;
import co.altir.ems.service.EmployeeService;
import co.altir.ems.util.SecurityUtil;
import co.altir.ems.util.AuditPayloadHelper;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl extends EmployeeDto.Service implements EmployeeService {

  private static final int DEFAULT_PAGE_SIZE = 20;
  private static final int MAX_PAGE_SIZE = 100;

  private final KeycloakUserSyncService keycloakUserSyncService;
  private final KeycloakAdminClient keycloakAdminClient;
  private final AuthorizationService authorizationService;
  private final SecurityUtil securityUtil;
  private final EmployeeRelationServiceImpl employeeRelationService;

  private static Pageable clampPageable(Pageable pageable) {
    if (pageable == null) {
      return PageRequest.of(0, DEFAULT_PAGE_SIZE);
    }
    int size = pageable.getPageSize();
    if (size <= 0) {
      return PageRequest.of(pageable.getPageNumber(), DEFAULT_PAGE_SIZE, pageable.getSort());
    }
    if (size > MAX_PAGE_SIZE) {
      return PageRequest.of(pageable.getPageNumber(), MAX_PAGE_SIZE, pageable.getSort());
    }
    return pageable;
  }

  private static void applyCreateAuditToPayloadAndUser(EmployeeDto payload) {
    AuditPayloadHelper.applyCreateAudit(payload);
    AuditPayloadHelper.applyCreateAudit(payload.getUser());
  }

  private static void applyUpdateAuditToPayloadAndUser(EmployeeDto payload) {
    AuditPayloadHelper.applyUpdateAudit(payload);
    AuditPayloadHelper.applyUpdateAudit(payload.getUser());
  }

  private static String getUserEmailFromEmployeeResponse(EmployeeResponseDto res) {
    if (res == null || res.getData() == null || res.getData().getUser() == null) {
      return null;
    }
    return res.getData().getUser().getEmail();
  }

  private UUID getMeIdOrThrow() {
    String username = securityUtil.username();
    if (username == null || username.isBlank()) {
      throw new AccessDeniedException("Unauthenticated");
    }
    var filter = EmployeeDto.buildFilter(b -> b.user.email.eq(username));
    EmployeeArrayResponseDto res = super.findEmployees(filter, Pageable.unpaged(), Projection.fields("id"));
    var list = res != null ? res.getData() : null;
    if (list == null || list.isEmpty() || list.get(0) == null || list.get(0).getId() == null) {
      throw new AccessDeniedException("Employee not found for token user");
    }
    return list.get(0).getId();
  }

  private void requireSelfOrAdmin(UUID employeeId) {
    if (securityUtil.hasRole("ADMIN")) {
      return;
    }
    UUID me = getMeIdOrThrow();
    if (!employeeId.equals(me)) {
      throw new AccessDeniedException("Self only");
    }
  }

  private EmployeeResponseDto findEmployeeSummary(UUID employeeId) {
    // Rely on @DslFragmentProjection + @DslScope for response shaping.
    // EmployeeHierarchyNode.employee is forced to SUMMARY to prevent graph explosions.
    return super.findByIdEmployees(employeeId);
  }

  private EmployeeDto findEmployeeSummaryData(UUID employeeId) {
    EmployeeResponseDto res = findEmployeeSummary(employeeId);
    return res != null ? res.getData() : null;
  }

  @Override
  public EmployeeResponseDto findByIdEmployees(UUID id) {
    requireSelfOrAdmin(id);
    return super.findByIdEmployees(id, (Projection) null);
  }

  @Override
  public EmployeeArrayResponseDto findEmployees(AbstractFilterDto filter, Pageable pageable) {
    authorizationService.requireAdmin();
    Pageable safe = clampPageable(pageable);
    return super.findEmployees(filter, safe);
  }

  @Override
  public EmployeeResponseDto createEmployees(EmployeeDto payload) {
    authorizationService.requireAdmin();
    keycloakUserSyncService.syncUserOnCreate(payload.getUser());
    applyCreateAuditToPayloadAndUser(payload);
    EmployeeResponseDto created = super.createEmployees(payload);
    String email = getUserEmailFromEmployeeResponse(created);
    if (email != null && !email.isBlank()) {
      keycloakAdminClient.addRealmRoleToUserByEmail(email, "EMPLOYEE");
    }
    return created;
  }

  @Override
  public EmployeeResponseDto patchEmployees(UUID id, EmployeeDto payload) {
    authorizationService.requireAdmin();
    applyUpdateAuditToPayloadAndUser(payload);
    EmployeeResponseDto result = super.patchEmployees(id, payload);
    if (payload.getUser() != null) {
      keycloakUserSyncService.syncUserOnUpdate(payload.getUser());
    }
    return result;
  }

  @Override
  public BooleanReadByIdResponseDto deleteEmployees(UUID id) {
    authorizationService.requireAdmin();
    EmployeeResponseDto existing = findByIdEmployees(id);
    String email = getUserEmailFromEmployeeResponse(existing);
    BooleanReadByIdResponseDto result = super.deleteEmployees(id);
    keycloakUserSyncService.syncUserOnDelete(email);
    return result;
  }

  @Override
  public EmployeeResponseDto updateEmployeeRole(UUID employeeId, UpdateEmployeeRoleRequestDto payload) {
    authorizationService.requireAdmin();

    EmployeeResponseDto existingRes =
        super.findByIdEmployees(employeeId, Projection.fields("id", "role", "user.email"));
    EmployeeDto existing = existingRes != null ? existingRes.getData() : null;
    if (existing == null || existing.getUser() == null) {
      throw new IllegalArgumentException("Employee not found");
    }

    EmployeeRole oldRole = existing.getRole();
    EmployeeRole newRole = payload != null ? payload.getRole() : null;
    if (newRole == null) {
      throw new IllegalArgumentException("role is required");
    }

    EmployeeResponseDto updated = super.patchEmployees(employeeId, new EmployeeDto().role(newRole));

    String email = existing.getUser().getEmail();
    if (email != null && !email.isBlank()) {
      if (oldRole != null) {
        keycloakAdminClient.removeRealmRoleFromUserByEmail(email, oldRole.name());
      }
      keycloakAdminClient.addRealmRoleToUserByEmail(email, newRole.name());
    }

    return updated;
  }

  @Override
  public EmployeeArrayResponseDto getSubordinates(UUID employeeId) {
    authorizationService.requireManagerOrAdmin();
    requireSelfOrAdmin(employeeId);

    var out = new ArrayList<EmployeeDto>();
    var relFilter = EmployeeRelationDto.buildFilter(b -> b.supervisor.id.eq(employeeId));
    List<EmployeeRelationDto> rels =
        employeeRelationService.find(relFilter, Pageable.unpaged(), Projection.fields("subordinate.id"));
    if (rels != null) {
      for (var rel : rels) {
        if (rel == null || rel.getSubordinate() == null || rel.getSubordinate().getId() == null) continue;
        EmployeeDto sub = findEmployeeSummaryData(rel.getSubordinate().getId());
        if (sub != null) out.add(sub);
      }
    }
    return new EmployeeArrayResponseDto().data(out);
  }

  @Override
  public EmployeeResponseDto getSupervisor(UUID employeeId) {
    requireSelfOrAdmin(employeeId);

    EmployeeDto supervisor = null;
    var relFilter = EmployeeRelationDto.buildFilter(b -> b.subordinate.id.eq(employeeId));
    List<EmployeeRelationDto> rels =
        employeeRelationService.find(relFilter, Pageable.unpaged(), Projection.fields("supervisor.id"));
    if (rels != null && !rels.isEmpty()) {
      var first = rels.get(0);
      if (first != null && first.getSupervisor() != null && first.getSupervisor().getId() != null)
        supervisor = findEmployeeSummaryData(first.getSupervisor().getId());
    }
    return new EmployeeResponseDto().data(supervisor);
  }

  @Override
  public EmployeeHierarchyNodeResponseDto getHierarchy(UUID employeeId) {
    requireSelfOrAdmin(employeeId);

    EmployeeResponseDto rootRes = super.findByIdEmployees(employeeId);
    EmployeeDto rootEmp = rootRes != null ? rootRes.getData() : null;
    if (rootEmp == null) {
      throw new IllegalArgumentException("Employee not found");
    }

    Set<UUID> visited = new HashSet<>();
    EmployeeHierarchyNodeDto root = buildHierarchyNode(rootEmp, visited);
    return new EmployeeHierarchyNodeResponseDto().data(root);
  }

  private EmployeeHierarchyNodeDto buildHierarchyNode(EmployeeDto employee, Set<UUID> visited) {
    UUID id = employee != null ? employee.getId() : null;
    if (id == null) {
      return new EmployeeHierarchyNodeDto(employee);
    }
    if (!visited.add(id)) {
      return new EmployeeHierarchyNodeDto(employee);
    }

    List<EmployeeHierarchyNodeDto> children = new ArrayList<>();
    var relFilter = EmployeeRelationDto.buildFilter(b -> b.supervisor.id.eq(id));
    List<EmployeeRelationDto> rels =
        employeeRelationService.find(relFilter, Pageable.unpaged(), Projection.fields("subordinate.id"));
    if (rels != null) {
      for (var rel : rels) {
        if (rel == null || rel.getSubordinate() == null || rel.getSubordinate().getId() == null) continue;
        EmployeeDto sub = findEmployeeSummaryData(rel.getSubordinate().getId());
        if (sub == null || sub.getId() == null) continue;
        children.add(buildHierarchyNode(sub, visited));
      }
    }

    return new EmployeeHierarchyNodeDto(employee).children(children);
  }
}