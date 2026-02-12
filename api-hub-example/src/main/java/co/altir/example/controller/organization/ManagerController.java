package co.altir.example.controller.organization;

import co.altir.dbmanagement.dataaccess.openapidsl.endpoint.HttpMethod;
import co.altir.dbmanagement.dataaccess.openapidsl.endpoint.annotations.DslCrudEndpoints;
import co.altir.dbmanagement.dataaccess.openapidsl.endpoint.annotations.DslEndpoint;
import co.altir.example.model.organization.Employee;
import co.altir.example.model.organization.Manager;
import java.util.List;
import java.util.UUID;

// Source cell: Organization (organization)::A34
public interface ManagerController {

  @DslCrudEndpoints(
      path = "/manager",
      methods = {HttpMethod.GET_BY_ID, HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE})
  Manager crudEndpointsForManager();

  @DslEndpoint(path = "/managers/{managerId}/reportees", method = HttpMethod.GET)
  List<Employee> getManagerReportees(UUID managerId);

  @DslEndpoint(path = "/managers/{managerId}/hierarchy", method = HttpMethod.GET)
  Object getManagerHierarchy(UUID managerId);
}
