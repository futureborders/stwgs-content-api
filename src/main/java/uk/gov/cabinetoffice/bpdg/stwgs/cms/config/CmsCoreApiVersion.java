package uk.gov.cabinetoffice.bpdg.stwgs.cms.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class CmsCoreApiVersion {
  public static final String CMS_API_V1_VERSION = "/api/v1";
  public static final String APPENDIX5_API_V1_VERSION = CMS_API_V1_VERSION + "/appendix5";
}
