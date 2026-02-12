package co.altir.example.controller.organization;

import co.altir.dbmanagement.dataaccess.openapidsl.endpoint.HttpMethod;
import co.altir.dbmanagement.dataaccess.openapidsl.endpoint.annotations.DslCrudEndpoints;
import co.altir.dbmanagement.dataaccess.openapidsl.endpoint.annotations.DslEndpoint;
import co.altir.example.model.organization.Employee;
import co.altir.example.model.organization.Manager;
import java.util.UUID;

// Source cell: Organization (organization)::A24
public interface EmployeeController {

  @DslCrudEndpoints(
      path = "/employee",
      methods = {HttpMethod.GET_BY_ID, HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE})
  Employee crudEndpointsForEmployee();

  @DslEndpoint(path = "/employees/{employeeId}/manager", method = HttpMethod.GET)
  Manager getEmployeeManager(UUID employeeId);

  @DslEndpoint(path = "/employees/{employeeId}/manager", method = HttpMethod.PUT)
  Manager updateEmployeeManager(UUID employeeId);

  @DslEndpoint(path = "/employees/{employeeId}/hierarchy", method = HttpMethod.GET)
  Object getEmployeeHierarchy(UUID employeeId);
}
