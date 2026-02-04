package com.example.employeemanagement.util;

import com.example.employeemanagement.models.dto.BaseAuditFieldsDto;

public final class AuditPayloadHelper {

  private AuditPayloadHelper() {}

  /**
   * Sets createdBy, updatedBy and active on the payload for create. No-op if target is null.
   */
  public static void applyCreateAudit(BaseAuditFieldsDto target) {
    if (target == null) {
      return;
    }
    String auditor = AuditHelper.getCurrentAuditor();
    target.createdBy(auditor).updatedBy(auditor).active(true);
  }

  /**
   * Sets updatedBy on the payload for update/patch. No-op if target is null.
   */
  public static void applyUpdateAudit(BaseAuditFieldsDto target) {
    if (target == null) {
      return;
    }
    target.updatedBy(AuditHelper.getCurrentAuditor());
  }
}
