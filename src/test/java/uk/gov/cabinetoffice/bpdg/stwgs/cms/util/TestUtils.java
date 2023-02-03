package uk.gov.cabinetoffice.bpdg.stwgs.cms.util;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import com.google.common.testing.FakeTicker;
import java.time.Duration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.core.io.ClassPathResource;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtils {

  @SneakyThrows
  public static void configResources() {
    System.setProperty(
        "stwgs.cms.collections.path.5a-document-codes",
        getClassPathResource("content/appendix5/document-codes"));
    System.setProperty(
        "stwgs.cms.collections.path.5b-status-codes",
        getClassPathResource("content/appendix5/status-codes"));
    System.setProperty(
        "stwgs.cms.collections.path.document-code-descriptions",
        getClassPathResource("content/document-code-descriptions"));
    System.setProperty(
        "stwgs.cms.collections.path.measure-type-descriptions",
        getClassPathResource("content/measure-type-descriptions"));
  }

  @SneakyThrows
  public static void ftContentConfig() {
    System.setProperty(
        "stwgs.cms.collections.path.5a-document-codes",
        getClassPathResource("ft-content/appendix5/document-codes"));
    System.setProperty(
        "stwgs.cms.collections.path.5b-status-codes",
        getClassPathResource("ft-content/appendix5/status-codes"));
    System.setProperty(
        "stwgs.cms.collections.path.document-code-descriptions",
        getClassPathResource("ft-content/document-code-descriptions"));
    System.setProperty(
        "stwgs.cms.collections.path.measure-type-descriptions",
        getClassPathResource("ft-content/measure-type-descriptions"));
  }

  @SneakyThrows
  public static String getClassPathResource(final String path) {
    return new ClassPathResource(path).getFile().getAbsolutePath();
  }

  public static CacheManager cacheManager(String duration) {
    CaffeineCacheManager cacheManager =
        new CaffeineCacheManager("statuscodes", "documentcodes", "documentcodedescriptions");
    cacheManager.setCaffeine(stubCaffeineCacheWithCacheDuration(duration));
    return cacheManager;
  }

  public static CacheManager cacheManager(String duration, FakeTicker fakeTicker) {
    CaffeineCacheManager cacheManager =
        new CaffeineCacheManager(
            "statusCodesCache", "documentCodesCache", "documentCodeDescriptionsCache");
    cacheManager.setCaffeine(stubCaffeineCacheWithCacheDuration(duration, fakeTicker));
    return cacheManager;
  }

  public static Caffeine<Object, Object> stubCaffeineCacheWithCacheDuration(
      String duration, FakeTicker fakeTicker) {
    return Caffeine.newBuilder()
        .expireAfterWrite(Duration.parse(duration))
        .ticker(fakeTicker::read);
  }

  public static Caffeine<Object, Object> stubCaffeineCacheWithCacheDuration(String duration) {
    return Caffeine.newBuilder()
        .expireAfterWrite(Duration.parse(duration))
        .ticker(Ticker.systemTicker());
  }
}
