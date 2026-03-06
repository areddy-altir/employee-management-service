package co.altir.ems.controller;

import co.altir.dbmanagement.dataaccess.filter.AbstractFilterDto;
import co.altir.ems.models.dto.*;
import co.altir.ems.service.OrganizationService;
import co.altir.ems.utils.OrganizationApi;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
@RestController
@RequiredArgsConstructor
public class OrganizationController implements OrganizationApi {

    private final OrganizationService organizationService;

    @Override
    public ResponseEntity<OrganizationArrayResponseDto> findOrganization(AbstractFilterDto filter, Pageable pageable) {
        return ResponseEntity.ok(organizationService.findOrganization(filter, pageable));
    }

    @Override
    public ResponseEntity<OrganizationResponseDto> findByIdOrganization(UUID id) {
        return ResponseEntity.ok(organizationService.findByIdOrganization(id));
    }

    @Override
    public ResponseEntity<OrganizationResponseDto> createOrganization(OrganizationDto organizationDto) {
        return ResponseEntity.ok(organizationService.createOrganization(organizationDto));
    }

    @Override
    public ResponseEntity<OrganizationResponseDto> patchOrganization(UUID id, OrganizationDto organizationDto) {
        return ResponseEntity.ok(organizationService.patchOrganization(id, organizationDto));
    }

    @Override
    public ResponseEntity<BooleanReadByIdResponseDto> deleteOrganization(UUID id) {
        return ResponseEntity.ok(organizationService.deleteOrganization(id));
    }
}
