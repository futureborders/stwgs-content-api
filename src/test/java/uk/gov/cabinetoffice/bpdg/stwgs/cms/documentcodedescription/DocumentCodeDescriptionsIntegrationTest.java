package uk.gov.cabinetoffice.bpdg.stwgs.cms.documentcodedescription;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.cabinetoffice.bpdg.stwgs.cms.config.CmsCoreApiVersion.CMS_API_V1_VERSION;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.model.ErrorResponse;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.util.TestUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DocumentCodeDescriptionsIntegrationTest {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @BeforeAll
  @SneakyThrows
  public static void before() {
    TestUtils.configResources();
  }

  @Test
  @SneakyThrows
  void shouldReturnDocumentCodeDescriptionsSpecificToDocumentCodesAndLocale() {

    // when
    final DocumentCodeDescriptions documentCodeDescriptions =
        this.restTemplate.getForObject(
            "http://localhost:"
                + port
                + CMS_API_V1_VERSION
                + "/document-code-descriptions?documentCodes=9002&locale=EN",
            DocumentCodeDescriptions.class);

    // then
    assertThat(documentCodeDescriptions.getDocumentCodeDescriptions()).hasSize(1);
    assertThat(documentCodeDescriptions.getDocumentCodeDescriptions())
        .containsExactlyInAnyOrder(
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("IMPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturnDocumentCodeDescriptionsSpecificToDocumentCodesAndLocaleAndTradeType() {

    // when
    final DocumentCodeDescriptions documentCodeDescriptions =
        this.restTemplate.getForObject(
            "http://localhost:"
                + port
                + CMS_API_V1_VERSION
                + "/document-code-descriptions?documentCodes=9002&locale=EN&tradeType=EXPORT",
            DocumentCodeDescriptions.class);

    // then
    assertThat(documentCodeDescriptions.getDocumentCodeDescriptions()).isEmpty();
  }

  @Test
  @SneakyThrows
  void shouldReturnAllDocumentCodeDescriptions() {

    // when
    final DocumentCodeDescriptions documentCodeDescriptions =
        this.restTemplate.getForObject(
            "http://localhost:" + port + CMS_API_V1_VERSION + "/document-code-descriptions",
            DocumentCodeDescriptions.class);

    // then
    assertThat(documentCodeDescriptions.getDocumentCodeDescriptions()).hasSize(4);
    assertThat(documentCodeDescriptions.getDocumentCodeDescriptions())
        .containsExactlyInAnyOrder(
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("IMPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Movement certificate EUR-MED")
                .locale("CY")
                .tradeType("IMPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9100")
                .descriptionOverlay(
                    "You need an import licence if your **firearms and ammunition** are:\r\n- within either chapter 93 (arms and ammunition) or chapter 97 (works of art, collectors' pieces and antiques) of the UK tariff\r\n- made after 31 December 1899\r\n\r\nContact the Department of International Trade's Import Controls Policy and Licencing team for further guidance.\r\n\r\nEmail: enquiries.ilb@trade.gov.uk\r\n\r\nYou need an import licence if your **nuclear material** falls under sub-chapters 2612 and 2844 of the UK Trade Tariff.\r\n\r\nYou should allow 2 months for your licence application to be processed.\r\n\r\n[Apply for a licence](https://www.gov.uk/guidance/importing-relevant-nuclear-materials-from-the-eu-licensing-requirements) from the Office for Nuclear Regulation (ONR).")
                .locale("CY")
                .tradeType("IMPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9100")
                .descriptionOverlay(
                    "You need an import licence if your **firearms and ammunition** are:\r\n- within either chapter 93 (arms and ammunition) or chapter 97 (works of art, collectors' pieces and antiques) of the UK tariff\r\n- made after 31 December 1899\r\n\r\nContact the Department of International Trade's Import Controls Policy and Licencing team for further guidance.\r\n\r\nEmail: enquiries.ilb@trade.gov.uk\r\n\r\nYou need an import licence if your **nuclear material** falls under sub-chapters 2612 and 2844 of the UK Trade Tariff.\r\n\r\nYou should allow 2 months for your licence application to be processed.\r\n\r\n[Apply for a licence](https://www.gov.uk/guidance/importing-relevant-nuclear-materials-from-the-eu-licensing-requirements) from the Office for Nuclear Regulation (ONR).\r\n\r\n")
                .locale("EN")
                .tradeType("IMPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturnEmptyDocumentCodeDescriptions() {

    // when
    final DocumentCodeDescriptions documentCodeDescriptions =
        this.restTemplate.getForObject(
            "http://localhost:"
                + port
                + CMS_API_V1_VERSION
                + "/document-code-descriptions?documentCodes=XYZ&locale=EN",
            DocumentCodeDescriptions.class);

    // then
    assertThat(documentCodeDescriptions.getDocumentCodeDescriptions()).isEmpty();
  }

  @Test
  @SneakyThrows
  void shouldReturnEmptyResponseWhenWrongDocumentCodeQueryParams() {
    // when
    final DocumentCodeDescriptions documentCodeDescriptions =
        this.restTemplate.getForObject(
            "http://localhost:"
                + port
                + CMS_API_V1_VERSION
                + "/document-code-descriptions?documentCodes=XYZ&locale=EN",
            DocumentCodeDescriptions.class);

    // then
    assertThat(documentCodeDescriptions.getDocumentCodeDescriptions()).isEmpty();
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestResponseWhenBadLocaleQueryParams() {
    // when
    final ErrorResponse errorResponse =
        this.restTemplate.getForObject(
            "http://localhost:"
                + port
                + CMS_API_V1_VERSION
                + "/document-code-descriptions?documentCodes=9002&locale=FR",
            ErrorResponse.class);

    // then
    assertThat(errorResponse.getHttpStatusCode()).isEqualTo(400);
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestResponseWhenBadDocumentCodeQueryParams() {
    // when
    final ErrorResponse errorResponse =
        this.restTemplate.getForObject(
            "http://localhost:"
                + port
                + CMS_API_V1_VERSION
                + "/document-code-descriptions?documentCodes=Y£6&locale=EN",
            ErrorResponse.class);

    // then
    assertThat(errorResponse.getHttpStatusCode()).isEqualTo(400);
  }
}
