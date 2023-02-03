package uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.cabinetoffice.bpdg.stwgs.cms.config.CmsCoreApiVersion.APPENDIX5_API_V1_VERSION;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.StatusCodes;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.model.ErrorResponse;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.util.TestUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StatusCodesIntegrationTest {
  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @BeforeAll
  @SneakyThrows
  public static void before() {
    TestUtils.configResources();
  }

  @Test
  void shouldReturnCDSandCHIEFStatusCodes() {

    // when
    final StatusCodes statusCodes =
        this.restTemplate.getForObject(
            "http://localhost:" + port + APPENDIX5_API_V1_VERSION + "/status-codes",
            StatusCodes.class);
    // then
    assertThat(statusCodes.getStatusCodeList()).hasSize(2);
  }

  @Test
  void shouldReturn400ErrorWhenGetStatusCodeCalledWithWrongSystem() {

    // when
    final ErrorResponse errorResponse =
        this.restTemplate.getForObject(
            "http://localhost:" + port + APPENDIX5_API_V1_VERSION + "/systems/XYZ/status-codes/UT",
            ErrorResponse.class);
    // then
    assertThat(errorResponse.getDescription()).isEqualTo("Invalid system");
  }

  @Test
  void shouldReturn404ErrorWhenGetStatusCodeCalledWithWrongCode() {

    // when
    final ErrorResponse errorResponse =
        this.restTemplate.getForObject(
            "http://localhost:" + port + APPENDIX5_API_V1_VERSION + "/systems/CDS/status-codes/FH",
            ErrorResponse.class);
    // then
    assertThat(errorResponse.getDescription())
        .isEqualTo("Resource 'StatusCode' not found with system 'CDS' and code 'FH'");
  }

  @Test
  void shouldReturn404ErrorWhenGetStatusCodeCalledWithWrongSystemAndCode() {

    // when
    final ResponseEntity<String> errorResponse =
        this.restTemplate.getForEntity(
            "http://localhost:" + port + APPENDIX5_API_V1_VERSION + "/systems/status-codes/UT",
            String.class);
    // then
    assertThat(errorResponse.getStatusCodeValue()).isEqualTo(404);
  }
}
