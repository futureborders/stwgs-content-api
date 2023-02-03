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
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.TestDocumentCodes;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.model.ErrorResponse;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.util.TestUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DocumentCodesIntegrationTest {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @BeforeAll
  @SneakyThrows
  public static void before() {
    TestUtils.configResources();
  }

  @Test
  void shouldReturnCDSandCHIEFDocumentCodes() {

    // when
    final TestDocumentCodes testDocumentCodes =
        this.restTemplate.getForObject(
            "http://localhost:" + port + APPENDIX5_API_V1_VERSION + "/document-codes",
            TestDocumentCodes.class);
    // then
    assertThat(testDocumentCodes.getDocumentCodeList()).hasSize(4);
  }

  @Test
  void shouldReturn400ErrorWhenGetDocumentCodeCalledWithWrongTradeType() {
    // when
    final ErrorResponse errorResponse =
        this.restTemplate.getForObject(
            "http://localhost:"
                + port
                + APPENDIX5_API_V1_VERSION
                + "/trade_types/TEST/systems/CDS/document-codes/U057",
            ErrorResponse.class);
    // then
    assertThat(errorResponse.getDescription()).isEqualTo("Invalid tradeType");
  }

  @Test
  void shouldReturn400ErrorWhenGetDocumentCodeCalledWithWrongSystem() {

    // when
    final ErrorResponse errorResponse =
        this.restTemplate.getForObject(
            "http://localhost:"
                + port
                + APPENDIX5_API_V1_VERSION
                + "/trade_types/IMPORT/systems/XYZ/document-codes/U057",
            ErrorResponse.class);
    // then
    assertThat(errorResponse.getDescription()).isEqualTo("Invalid system");
  }

  @Test
  void shouldReturn404ErrorWhenGetDocumentCodeCalledWithWrongCode() {

    // when
    final ErrorResponse errorResponse =
        this.restTemplate.getForObject(
            "http://localhost:"
                + port
                + APPENDIX5_API_V1_VERSION
                + "/trade_types/IMPORT/systems/CDS/document-codes/A757",
            ErrorResponse.class);
    // then
    assertThat(errorResponse.getDescription())
        .isEqualTo(
            "Resource 'DocumentCode' not found with trade type 'IMPORT', system 'CDS' and code 'A757'");
  }

  @Test
  void shouldReturn400ErrorWhenGetDocumentCodeCalledWithWrongTradeTypeAndSystemAndCode() {

    // when
    final ErrorResponse errorResponse =
        this.restTemplate.getForObject(
            "http://localhost:"
                + port
                + APPENDIX5_API_V1_VERSION
                + "/trade_types/BOTH/systems/XYZ/document-codes/A757",
            ErrorResponse.class);
    // then
    assertThat(errorResponse.getDescription()).isEqualTo("Invalid system, Invalid tradeType");
  }

  @Test
  void shouldReturn400ErrorWhenGetDocumentCodeCalledWithWrongSystemAndCode() {
    // when
    final ErrorResponse errorResponse =
        this.restTemplate.getForObject(
            "http://localhost:"
                + port
                + APPENDIX5_API_V1_VERSION
                + "/trade_types/EXPORT/systems/XYZ/document-codes/A757",
            ErrorResponse.class);
    // then
    assertThat(errorResponse.getDescription()).isEqualTo("Invalid system");
  }
}
