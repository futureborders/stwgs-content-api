package uk.gov.cabinetoffice.bpdg.stwgs.cms.cache;

import static uk.gov.cabinetoffice.bpdg.stwgs.cms.cache.CacheConstants.DOCUMENT_CODES_CACHE;
import static uk.gov.cabinetoffice.bpdg.stwgs.cms.cache.CacheConstants.DOCUMENT_CODES_KEY;
import static uk.gov.cabinetoffice.bpdg.stwgs.cms.cache.CacheConstants.STATUS_CODES_CACHE;
import static uk.gov.cabinetoffice.bpdg.stwgs.cms.cache.CacheConstants.STATUS_CODES_KEY;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.helper.DocumentCodeServiceHelper;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.helper.StatusCodeServiceHelper;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
@Component
public class CollectionsDataCacheInitializer {
  private final DocumentCodeServiceHelper documentCodeServiceHelper;
  private final StatusCodeServiceHelper statusCodeServiceHelper;
  private final CacheManager cacheManager;

  @EventListener(ApplicationReadyEvent.class)
  public synchronized void initializeCache() {
    Objects.requireNonNull(cacheManager.getCache(DOCUMENT_CODES_CACHE))
        .put(DOCUMENT_CODES_KEY, documentCodeServiceHelper.getDocumentCodes());
    Objects.requireNonNull(cacheManager.getCache(STATUS_CODES_CACHE))
        .put(STATUS_CODES_KEY, statusCodeServiceHelper.getStatusCodes());
    log.info("******* Collection Data Cache has initialized ***********");
  }
}
