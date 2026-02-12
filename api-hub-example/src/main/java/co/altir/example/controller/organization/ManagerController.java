package co.altir.example.controller.organization;

import co.altir.dbmanagement.dataaccess.openapidsl.endpoint.HttpMethod;
import co.altir.dbmanagement.dataaccess.openapidsl.endpoint.annotations.DslCrudEndpoints;
import co.altir.example.model.organization.Manager;

// Source cell: Organization (organization)::A30
public interface ManagerController {

  @DslCrudEndpoints(
      path = "/manager",
      methods = {HttpMethod.GET_BY_ID, HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE})
  Manager crudEndpointsForManager();
}
