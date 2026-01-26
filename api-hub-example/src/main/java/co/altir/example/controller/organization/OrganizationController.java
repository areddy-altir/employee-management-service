package co.altir.example.controller.organization;

import co.altir.dbmanagement.dataaccess.openapidsl.endpoint.HttpMethod;
import co.altir.dbmanagement.dataaccess.openapidsl.endpoint.annotations.DslCrudEndpoints;
import co.altir.dbmanagement.dataaccess.openapidsl.endpoint.annotations.DslEndpoint;
import co.altir.example.model.organization.Organization;

// Source cell: API organization (organization)::A2
public interface OrganizationController {

  @DslCrudEndpoints(
      path = "/organization",
      methods = {HttpMethod.GET_BY_ID, HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE})
  Organization crudEndpointsForOrganization();

  @DslEndpoint(path = "/internal/organization/db-sync", method = HttpMethod.POST)
  void syncOrganizationDatabases();
}
