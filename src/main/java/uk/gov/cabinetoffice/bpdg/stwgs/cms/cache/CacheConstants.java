package uk.gov.cabinetoffice.bpdg.stwgs.cms.cache;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CacheConstants {
  public static final String DOCUMENT_CODE_DESCRIPTIONS_CACHE = "documentCodeDescriptionsCache";
  public static final String DOCUMENT_CODE_DESCRIPTIONS_KEY = "documentCodeDescriptions";
  public static final String DOCUMENT_CODES_CACHE = "documentCodesCache";
  public static final String STATUS_CODES_CACHE = "statusCodesCache";
  public static final String DOCUMENT_CODES_KEY = "documentCodes";
  public static final String STATUS_CODES_KEY = "statusCodes";
}
