package uk.gov.cabinetoffice.bpdg.stwgs.cms.functionaltests;

import javax.annotation.PostConstruct;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.util.TestUtils;

@TestConfiguration
public class TestConfig {

  @PostConstruct
  public void configResources() {
    TestUtils.ftContentConfig();
  }

  @Bean
  public RestTemplate getRestTemplate() {
    return new RestTemplate();
  }
}
