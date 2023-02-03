package uk.gov.cabinetoffice.bpdg.stwgs.cms.cache;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class DataCollectionsCache<CollectionType> {
  private final CacheManager cacheManager;

  public CollectionType lookUpCollectionDataFromCache(final String key, String cacheName) {
    return (CollectionType)
        Objects.requireNonNull(Objects.requireNonNull(cacheManager.getCache(cacheName)).get(key))
            .get();
  }
}
