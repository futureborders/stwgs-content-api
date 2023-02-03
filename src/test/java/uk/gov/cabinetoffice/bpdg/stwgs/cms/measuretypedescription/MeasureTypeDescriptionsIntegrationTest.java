package uk.gov.cabinetoffice.bpdg.stwgs.cms.measuretypedescription;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.cabinetoffice.bpdg.stwgs.cms.config.CmsCoreApiVersion.CMS_API_V1_VERSION;
import static uk.gov.cabinetoffice.bpdg.stwgs.cms.measuretypedescription.MeasureTypeDescription.builder;

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
public class MeasureTypeDescriptionsIntegrationTest {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @BeforeAll
  @SneakyThrows
  public static void before() {
    TestUtils.configResources();
  }

  @SneakyThrows
  @Test
  void shouldReturnMeasureTypeDescriptionsSpecificToDocumentCodesAndLocale() {
    // when
    final MeasureTypeDescriptions measureTypeDescriptions =
        this.restTemplate.getForObject(
            "http://localhost:"
                + port
                + CMS_API_V1_VERSION
                + "/measure-type-descriptions?measureTypes=143&locale=EN",
            MeasureTypeDescriptions.class);

    // then
    assertThat(measureTypeDescriptions.getMeasureTypeDescriptions()).hasSize(1);
  }

  @SneakyThrows
  @Test
  void shouldReturnMeasureTypeDescriptionsSpecificToDocumentCodesAndLocaleAndTradeType() {
    // when
    final MeasureTypeDescriptions measureTypeDescriptions =
        this.restTemplate.getForObject(
            "http://localhost:"
                + port
                + CMS_API_V1_VERSION
                + "/measure-type-descriptions?measureTypes=143&locale=EN&tradeType=IMPORT",
            MeasureTypeDescriptions.class);

    // then
    assertThat(measureTypeDescriptions.getMeasureTypeDescriptions())
        .containsExactly(
            builder()
                .measureType("143")
                .descriptionOverlay("143 overlay")
                .locale("EN")
                .tradeType("IMPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturnAllMeasureTypeDescriptions() {
    // when
    final MeasureTypeDescriptions measureTypeDescriptions =
        this.restTemplate.getForObject(
            "http://localhost:" + port + CMS_API_V1_VERSION + "/measure-type-descriptions",
            MeasureTypeDescriptions.class);

    // then
    assertThat(measureTypeDescriptions.getMeasureTypeDescriptions()).hasSize(6);
    assertThat(measureTypeDescriptions.getMeasureTypeDescriptions())
        .containsExactlyInAnyOrder(
            builder()
                .measureType("143")
                .descriptionOverlay("143 overlay")
                .locale("EN")
                .tradeType("IMPORT")
                .build(),
            builder()
                .measureType("143")
                .descriptionOverlay("143 overlay CY")
                .locale("CY")
                .tradeType("IMPORT")
                .build(),
            builder()
                .measureType("143")
                .descriptionOverlay("143 overlay CY")
                .locale("CY")
                .tradeType("EXPORT")
                .build(),
            builder()
                .measureType("145")
                .descriptionOverlay("145 overlay")
                .locale("EN")
                .tradeType("EXPORT")
                .build(),
            builder()
                .measureType("145")
                .descriptionOverlay("145 overlay")
                .locale("EN")
                .tradeType("IMPORT")
                .build(),
            builder()
                .measureType("145")
                .descriptionOverlay("145 overlay CY")
                .locale("CY")
                .tradeType("EXPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturnEmptyMeasureTypeDescriptionsWhenOverlayIsNotConfigured() {
    // when
    final MeasureTypeDescriptions measureTypeDescriptions =
        this.restTemplate.getForObject(
            "http://localhost:"
                + port
                + CMS_API_V1_VERSION
                + "/measure-type-descriptions?measureTypes=142&locale=EN",
            MeasureTypeDescriptions.class);

    // then
    assertThat(measureTypeDescriptions.getMeasureTypeDescriptions()).isEmpty();
  }

  @Test
  @SneakyThrows
  void shouldReturnEmptyResponseWhenWrongMeasureTypeIsPasses() {
    // when
    final MeasureTypeDescriptions measureTypeDescriptions =
        this.restTemplate.getForObject(
            "http://localhost:"
                + port
                + CMS_API_V1_VERSION
                + "/measure-type-descriptions?measureTypes=XYZ&locale=EN",
            MeasureTypeDescriptions.class);

    // then
    assertThat(measureTypeDescriptions.getMeasureTypeDescriptions()).isEmpty();
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
                + "/measure-type-descriptions?measureTypes=AD1234DEF&locale=FR",
            ErrorResponse.class);

    // then
    assertThat(errorResponse.getHttpStatusCode()).isEqualTo(400);
  }
}
