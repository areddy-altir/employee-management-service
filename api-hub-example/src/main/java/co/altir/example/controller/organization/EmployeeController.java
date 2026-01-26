package co.altir.example.controller.organization;

import co.altir.dbmanagement.dataaccess.openapidsl.endpoint.HttpMethod;
import co.altir.dbmanagement.dataaccess.openapidsl.endpoint.annotations.DslCrudEndpoints;
import co.altir.example.model.organization.Employee;

// Source cell: Organization (organization)::A24
public interface EmployeeController {

  @DslCrudEndpoints(
      path = "/employee",
      methods = {HttpMethod.GET_BY_ID, HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE})
  Employee crudEndpointsForEmployee();
}
