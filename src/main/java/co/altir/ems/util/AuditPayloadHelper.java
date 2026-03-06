package co.altir.ems.util;

import co.altir.ems.models.dto.BaseAuditFieldsDto;
import java.time.Instant;

public final class AuditPayloadHelper {

  private AuditPayloadHelper() {}

  /**
   * Sets createdBy, updatedBy, createdOn, updatedOn and active on the payload for create. No-op if target is null.
   */
  public static void applyCreateAudit(BaseAuditFieldsDto target) {
    if (target == null) {
      return;
    }
    String auditor = AuditHelper.getCurrentAuditor();
    Instant now = Instant.now();
    target.createdBy(auditor).updatedBy(auditor).createdOn(now).updatedOn(now).active(true);
  }

  /**
   * Sets updatedBy and updatedOn on the payload for update/patch. No-op if target is null.
   */
  public static void applyUpdateAudit(BaseAuditFieldsDto target) {
    if (target == null) {
      return;
    }
    target.updatedBy(AuditHelper.getCurrentAuditor()).updatedOn(Instant.now());
  }
}
