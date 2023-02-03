package uk.gov.cabinetoffice.bpdg.stwgs.cms.documentcodedescription;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.cabinetoffice.bpdg.stwgs.cms.enums.Locale.CY;
import static uk.gov.cabinetoffice.bpdg.stwgs.cms.enums.Locale.EN;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.enums.Locale;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.enums.TradeType;

@ExtendWith(MockitoExtension.class)
class DocumentCodeDescriptionServiceTest {
  private DocumentCodeDescriptionService documentCodeDescriptionService;
  private static final HashMap<DocumentCodeDescriptionKey, DocumentCodeEntity>
      documentCodeDescriptionCache = new HashMap<>();
  private static final HashMap<String, List<String>> documentCodeLocaleMap = new HashMap<>();

  @BeforeEach
  void before() {
    documentCodeDescriptionService =
        new DocumentCodeDescriptionService(documentCodeDescriptionCache, documentCodeLocaleMap);
  }

  @AfterEach
  void after() {
    documentCodeDescriptionCache.clear();
  }

  @Test
  @SneakyThrows
  void shouldReturnAllDocumentCodeDescriptions() {
    // given
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("EN")
            .importOverlay("Updated Movement certificate EUR-MED")
            .exportOverlay("Export Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "CY"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("CY")
            .importOverlay("Updated Movement certificate EUR-MED")
            .exportOverlay("Export Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9003", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9003")
            .locale("EN")
            .importOverlay("9003 Updated Movement certificate EUR-MED")
            .exportOverlay("Export 9003 Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9003", "CY"),
        DocumentCodeEntity.builder()
            .documentCode("9003")
            .locale("CY")
            .importOverlay("9003 Updated Movement certificate EUR-MED")
            .exportOverlay("Export 9003 Updated Movement certificate EUR-MED")
            .build());
    documentCodeLocaleMap.put("9002", List.of("EN", "CY"));
    documentCodeLocaleMap.put("9003", List.of("EN", "CY"));

    // when
    final DocumentCodeDescriptions documentCodeDescriptions =
        documentCodeDescriptionService.getDocumentCodeDescriptions(null, null, null);
    // then
    assertThat(documentCodeDescriptions)
        .isNotNull()
        .extracting("documentCodeDescriptions")
        .asList()
        .hasSize(8)
        .containsExactlyInAnyOrder(
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("IMPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Export Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("EXPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Updated Movement certificate EUR-MED")
                .locale("CY")
                .tradeType("IMPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Export Updated Movement certificate EUR-MED")
                .locale("CY")
                .tradeType("EXPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9003")
                .descriptionOverlay("9003 Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("IMPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9003")
                .descriptionOverlay("Export 9003 Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("EXPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9003")
                .descriptionOverlay("9003 Updated Movement certificate EUR-MED")
                .locale("CY")
                .tradeType("IMPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9003")
                .descriptionOverlay("Export 9003 Updated Movement certificate EUR-MED")
                .locale("CY")
                .tradeType("EXPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturnAllENLocaleDocumentCodeDescriptions() {
    // given
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("EN")
            .importOverlay("Updated Movement certificate EUR-MED")
            .exportOverlay("Export Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "CY"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("CY")
            .importOverlay("Updated Movement certificate EUR-MED")
            .exportOverlay("Export Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9003", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9003")
            .locale("EN")
            .importOverlay("9003 Updated Movement certificate EUR-MED")
            .exportOverlay("Export 9003 Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9003", "CY"),
        DocumentCodeEntity.builder()
            .documentCode("9003")
            .locale("CY")
            .importOverlay("9003 Updated Movement certificate EUR-MED")
            .exportOverlay("Export 9003 Updated Movement certificate EUR-MED")
            .build());
    documentCodeLocaleMap.put("9002", List.of("EN", "CY"));
    documentCodeLocaleMap.put("9003", List.of("EN", "CY"));

    // when
    final DocumentCodeDescriptions documentCodeDescriptions =
        documentCodeDescriptionService.getDocumentCodeDescriptions(null, EN, null);
    // then
    assertThat(documentCodeDescriptions)
        .isNotNull()
        .extracting("documentCodeDescriptions")
        .asList()
        .hasSize(4)
        .containsExactlyInAnyOrder(
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("IMPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Export Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("EXPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9003")
                .descriptionOverlay("9003 Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("IMPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9003")
                .descriptionOverlay("Export 9003 Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("EXPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturnAllExportTradeTypeDocumentCodeDescriptions() {
    // given
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("EN")
            .importOverlay("Updated Movement certificate EUR-MED")
            .exportOverlay("Export Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "CY"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("CY")
            .importOverlay("Updated Movement certificate EUR-MED")
            .exportOverlay("Export Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9003", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9003")
            .locale("EN")
            .importOverlay("9003 Updated Movement certificate EUR-MED")
            .exportOverlay("Export 9003 Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9003", "CY"),
        DocumentCodeEntity.builder()
            .documentCode("9003")
            .locale("CY")
            .importOverlay("9003 Updated Movement certificate EUR-MED")
            .exportOverlay("Export 9003 Updated Movement certificate EUR-MED")
            .build());
    documentCodeLocaleMap.put("9002", List.of("EN", "CY"));
    documentCodeLocaleMap.put("9003", List.of("EN", "CY"));

    // when
    final DocumentCodeDescriptions documentCodeDescriptions =
        documentCodeDescriptionService.getDocumentCodeDescriptions(null, null, TradeType.EXPORT);
    // then
    assertThat(documentCodeDescriptions)
        .isNotNull()
        .extracting("documentCodeDescriptions")
        .asList()
        .hasSize(4)
        .containsExactlyInAnyOrder(
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Export Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("EXPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Export Updated Movement certificate EUR-MED")
                .locale("CY")
                .tradeType("EXPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9003")
                .descriptionOverlay("Export 9003 Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("EXPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9003")
                .descriptionOverlay("Export 9003 Updated Movement certificate EUR-MED")
                .locale("CY")
                .tradeType("EXPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturnAllImportTradeTypeDocumentCodeDescriptions() {
    // given
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("EN")
            .importOverlay("Updated Movement certificate EUR-MED")
            .exportOverlay("Export Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "CY"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("CY")
            .importOverlay("Updated Movement certificate EUR-MED")
            .exportOverlay("Export Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9003", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9003")
            .locale("EN")
            .importOverlay("9003 Updated Movement certificate EUR-MED")
            .exportOverlay("Export 9003 Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9003", "CY"),
        DocumentCodeEntity.builder()
            .documentCode("9003")
            .locale("CY")
            .importOverlay("9003 Updated Movement certificate EUR-MED")
            .exportOverlay("Export 9003 Updated Movement certificate EUR-MED")
            .build());
    documentCodeLocaleMap.put("9002", List.of("EN", "CY"));
    documentCodeLocaleMap.put("9003", List.of("EN", "CY"));

    // when
    final DocumentCodeDescriptions documentCodeDescriptions =
        documentCodeDescriptionService.getDocumentCodeDescriptions(null, null, TradeType.IMPORT);
    // then
    assertThat(documentCodeDescriptions)
        .isNotNull()
        .extracting("documentCodeDescriptions")
        .asList()
        .hasSize(4)
        .containsExactlyInAnyOrder(
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("IMPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Updated Movement certificate EUR-MED")
                .locale("CY")
                .tradeType("IMPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9003")
                .descriptionOverlay("9003 Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("IMPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9003")
                .descriptionOverlay("9003 Updated Movement certificate EUR-MED")
                .locale("CY")
                .tradeType("IMPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturnImportTradeTypeAndENLocaleDocumentCodeDescriptions() {
    // given
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("EN")
            .importOverlay("Updated Movement certificate EUR-MED")
            .exportOverlay("Export Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "CY"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("CY")
            .importOverlay("Updated Movement certificate EUR-MED")
            .exportOverlay("Export Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9003", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9003")
            .locale("EN")
            .importOverlay("9003 Updated Movement certificate EUR-MED")
            .exportOverlay("Export 9003 Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9003", "CY"),
        DocumentCodeEntity.builder()
            .documentCode("9003")
            .locale("CY")
            .importOverlay("9003 Updated Movement certificate EUR-MED")
            .exportOverlay("Export 9003 Updated Movement certificate EUR-MED")
            .build());
    documentCodeLocaleMap.put("9002", List.of("EN", "CY"));
    documentCodeLocaleMap.put("9003", List.of("EN", "CY"));

    // when
    final DocumentCodeDescriptions documentCodeDescriptions =
        documentCodeDescriptionService.getDocumentCodeDescriptions(null, EN, TradeType.IMPORT);
    // then
    assertThat(documentCodeDescriptions)
        .isNotNull()
        .extracting("documentCodeDescriptions")
        .asList()
        .hasSize(2)
        .containsExactlyInAnyOrder(
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("IMPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9003")
                .descriptionOverlay("9003 Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("IMPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturn9002ImportTradeTypeAndENLocaleDocumentCodeDescription() {
    // given
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("EN")
            .importOverlay("Updated Movement certificate EUR-MED")
            .exportOverlay("Export Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "CY"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("CY")
            .importOverlay("Updated Movement certificate EUR-MED")
            .exportOverlay("Export Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9003", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9003")
            .locale("EN")
            .importOverlay("9003 Updated Movement certificate EUR-MED")
            .exportOverlay("Export 9003 Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9003", "CY"),
        DocumentCodeEntity.builder()
            .documentCode("9003")
            .locale("CY")
            .importOverlay("9003 Updated Movement certificate EUR-MED")
            .exportOverlay("Export 9003 Updated Movement certificate EUR-MED")
            .build());
    documentCodeLocaleMap.put("9002", List.of("EN", "CY"));
    documentCodeLocaleMap.put("9003", List.of("EN", "CY"));

    // when
    final DocumentCodeDescriptions documentCodeDescriptions =
        documentCodeDescriptionService.getDocumentCodeDescriptions(
            List.of("9002"), EN, TradeType.IMPORT);
    // then
    assertThat(documentCodeDescriptions)
        .isNotNull()
        .extracting("documentCodeDescriptions")
        .asList()
        .hasSize(1)
        .containsExactly(
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("IMPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturn9002ImportTradeTypeAndCYLocaleDocumentCodeDescription() {
    // given
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("EN")
            .importOverlay("Updated Movement certificate EUR-MED")
            .exportOverlay("Export Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "CY"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("CY")
            .importOverlay("Updated Movement certificate EUR-MED")
            .exportOverlay("Export Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9003", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9003")
            .locale("EN")
            .importOverlay("9003 Updated Movement certificate EUR-MED")
            .exportOverlay("Export 9003 Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9003", "CY"),
        DocumentCodeEntity.builder()
            .documentCode("9003")
            .locale("CY")
            .importOverlay("9003 Updated Movement certificate EUR-MED")
            .exportOverlay("Export 9003 Updated Movement certificate EUR-MED")
            .build());
    documentCodeLocaleMap.put("9002", List.of("EN", "CY"));
    documentCodeLocaleMap.put("9003", List.of("EN", "CY"));

    // when
    final DocumentCodeDescriptions documentCodeDescriptions =
        documentCodeDescriptionService.getDocumentCodeDescriptions(
            List.of("9002"), CY, TradeType.IMPORT);
    // then
    assertThat(documentCodeDescriptions)
        .isNotNull()
        .extracting("documentCodeDescriptions")
        .asList()
        .hasSize(1)
        .containsExactly(
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Updated Movement certificate EUR-MED")
                .locale("CY")
                .tradeType("IMPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturn9002DocumentCodeImportTradeTypeAndENLocaleDescription() {
    // given
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("EN")
            .overlay("Updated Movement certificate EUR-MED")
            .build());
    documentCodeLocaleMap.put("9002", List.of("EN"));

    // when
    final DocumentCodeDescriptions documentCodeDescriptions =
        documentCodeDescriptionService.getDocumentCodeDescriptions(
            List.of("9002"), Locale.EN, TradeType.IMPORT);
    // then
    assertThat(documentCodeDescriptions)
        .isNotNull()
        .extracting("documentCodeDescriptions")
        .asList()
        .hasSize(1)
        .containsExactly(
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("IMPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturn9002DocumentCodeExportTradeTypeAndENLocaleDescription() {
    // given
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("EN")
            .overlay("Updated Movement certificate EUR-MED")
            .build());
    documentCodeLocaleMap.put("9002", List.of("EN"));

    // when
    final DocumentCodeDescriptions documentCodeDescriptions =
        documentCodeDescriptionService.getDocumentCodeDescriptions(
            List.of("9002"), Locale.EN, TradeType.EXPORT);
    // then
    assertThat(documentCodeDescriptions)
        .isNotNull()
        .extracting("documentCodeDescriptions")
        .asList()
        .hasSize(1)
        .containsExactly(
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("EXPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturn9002DocumentCodeENLocaleDescription() {
    // given
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("EN")
            .exportOverlay("Import Updated Movement certificate EUR-MED")
            .build());
    documentCodeLocaleMap.put("9002", List.of("EN"));

    // when
    final DocumentCodeDescriptions documentCodeDescriptions =
        documentCodeDescriptionService.getDocumentCodeDescriptions(List.of("9002"), null, null);
    // then
    assertThat(documentCodeDescriptions)
        .isNotNull()
        .extracting("documentCodeDescriptions")
        .asList()
        .hasSize(1)
        .containsExactly(
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Import Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("EXPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturn9002DocumentCodeCYAndENLocaleDescriptions() {
    // given
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("EN")
            .importOverlay("Updated Movement certificate EUR-MED")
            .exportOverlay("Export Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "CY"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("CY")
            .importOverlay("Updated Movement certificate EUR-MED")
            .exportOverlay("Export Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9003", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9003")
            .locale("EN")
            .importOverlay("9003 Updated Movement certificate EUR-MED")
            .exportOverlay("Export 9003 Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9003", "CY"),
        DocumentCodeEntity.builder()
            .documentCode("9003")
            .locale("CY")
            .importOverlay("9003 Updated Movement certificate EUR-MED")
            .exportOverlay("Export 9003 Updated Movement certificate EUR-MED")
            .build());
    documentCodeLocaleMap.put("9002", List.of("EN", "CY"));
    documentCodeLocaleMap.put("9003", List.of("EN", "CY"));

    // when
    final DocumentCodeDescriptions documentCodeDescriptions =
        documentCodeDescriptionService.getDocumentCodeDescriptions(
            List.of("9002"), null, TradeType.IMPORT);
    // then
    assertThat(documentCodeDescriptions)
        .isNotNull()
        .extracting("documentCodeDescriptions")
        .asList()
        .hasSize(2)
        .containsExactlyInAnyOrder(
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("IMPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Updated Movement certificate EUR-MED")
                .locale("CY")
                .tradeType("IMPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturnImportTradeTypeAndCYLocaleDocumentCodeDescriptions() {
    // given
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("EN")
            .importOverlay("Updated Movement certificate EUR-MED")
            .exportOverlay("Export Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "CY"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("CY")
            .importOverlay("Updated Movement certificate EUR-MED")
            .exportOverlay("Export Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9003", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9003")
            .locale("EN")
            .importOverlay("9003 Updated Movement certificate EUR-MED")
            .exportOverlay("Export 9003 Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9003", "CY"),
        DocumentCodeEntity.builder()
            .documentCode("9003")
            .locale("CY")
            .importOverlay("9003 Updated Movement certificate EUR-MED")
            .exportOverlay("Export 9003 Updated Movement certificate EUR-MED")
            .build());
    documentCodeLocaleMap.put("9002", List.of("EN", "CY"));
    documentCodeLocaleMap.put("9003", List.of("EN", "CY"));

    // when
    final DocumentCodeDescriptions documentCodeDescriptions =
        documentCodeDescriptionService.getDocumentCodeDescriptions(null, CY, TradeType.IMPORT);
    // then
    assertThat(documentCodeDescriptions)
        .isNotNull()
        .extracting("documentCodeDescriptions")
        .asList()
        .hasSize(2)
        .containsExactlyInAnyOrder(
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Updated Movement certificate EUR-MED")
                .locale("CY")
                .tradeType("IMPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9003")
                .descriptionOverlay("9003 Updated Movement certificate EUR-MED")
                .locale("CY")
                .tradeType("IMPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturnExportTradeTypeAndENLocaleDocumentCodeDescriptions() {
    // given
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("EN")
            .importOverlay("Updated Movement certificate EUR-MED")
            .exportOverlay("Export Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "CY"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("CY")
            .importOverlay("Updated Movement certificate EUR-MED")
            .exportOverlay("Export Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9003", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9003")
            .locale("EN")
            .importOverlay("9003 Updated Movement certificate EUR-MED")
            .exportOverlay("Export 9003 Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9003", "CY"),
        DocumentCodeEntity.builder()
            .documentCode("9003")
            .locale("CY")
            .importOverlay("9003 Updated Movement certificate EUR-MED")
            .exportOverlay("Export 9003 Updated Movement certificate EUR-MED")
            .build());
    documentCodeLocaleMap.put("9002", List.of("EN", "CY"));
    documentCodeLocaleMap.put("9003", List.of("EN", "CY"));

    // when
    final DocumentCodeDescriptions documentCodeDescriptions =
        documentCodeDescriptionService.getDocumentCodeDescriptions(null, EN, TradeType.EXPORT);
    // then
    assertThat(documentCodeDescriptions)
        .isNotNull()
        .extracting("documentCodeDescriptions")
        .asList()
        .hasSize(2)
        .containsExactlyInAnyOrder(
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Export Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("EXPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9003")
                .descriptionOverlay("Export 9003 Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("EXPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturnExportTradeTypeAndCYLocaleDocumentCodeDescriptions() {
    // given
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("EN")
            .importOverlay("Updated Movement certificate EUR-MED")
            .exportOverlay("Export Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "CY"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("CY")
            .importOverlay("Updated Movement certificate EUR-MED")
            .exportOverlay("Export Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9003", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9003")
            .locale("EN")
            .importOverlay("9003 Updated Movement certificate EUR-MED")
            .exportOverlay("Export 9003 Updated Movement certificate EUR-MED")
            .build());
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9003", "CY"),
        DocumentCodeEntity.builder()
            .documentCode("9003")
            .locale("CY")
            .importOverlay("9003 Updated Movement certificate EUR-MED")
            .exportOverlay("Export 9003 Updated Movement certificate EUR-MED")
            .build());
    documentCodeLocaleMap.put("9002", List.of("EN", "CY"));
    documentCodeLocaleMap.put("9003", List.of("EN", "CY"));

    // when
    final DocumentCodeDescriptions documentCodeDescriptions =
        documentCodeDescriptionService.getDocumentCodeDescriptions(null, CY, TradeType.EXPORT);
    // then
    assertThat(documentCodeDescriptions)
        .isNotNull()
        .extracting("documentCodeDescriptions")
        .asList()
        .hasSize(2)
        .containsExactlyInAnyOrder(
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Export Updated Movement certificate EUR-MED")
                .locale("CY")
                .tradeType("EXPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9003")
                .descriptionOverlay("Export 9003 Updated Movement certificate EUR-MED")
                .locale("CY")
                .tradeType("EXPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturnDocumentCodeDescriptionsSpecificToENLocale() {
    // given
    ObjectMapper objectMapper = new ObjectMapper();
    DocumentCodeEntity documentCodeEntity1 =
        objectMapper.readValue(
            "{\"locale\":\"EN\",\"documentCode\":\"9002\",\"importOverlay\":\"Updated Movement certificate EUR-MED\"}",
            DocumentCodeEntity.class);

    DocumentCodeEntity documentCodeEntity2 =
        objectMapper.readValue(
            "{\"locale\":\"EN\",\"documentCode\":\"9100\",\"importOverlay\":\"You need an import licence if your **firearms and ammunition** are:\\r\\n- within either chapter 93 (arms and ammunition) or chapter 97 (works of art, collectors' pieces and antiques) of the UK tariff\\r\\n- made after 31 December 1899\\r\\n\\r\\nContact the Department of International Trade's Import Controls Policy and Licencing team for further guidance.\\r\\n\\r\\nEmail: enquiries.ilb@trade.gov.uk\\r\\n\\r\\nYou need an import licence if your **nuclear material** falls under sub-chapters 2612 and 2844 of the UK Trade Tariff.\\r\\n\\r\\nYou should allow 2 months for your licence application to be processed.\\r\\n\\r\\n[Apply for a licence](https://www.gov.uk/guidance/importing-relevant-nuclear-materials-from-the-eu-licensing-requirements) from the Office for Nuclear Regulation (ONR).\\r\\n\\r\\n\"}",
            DocumentCodeEntity.class);

    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "EN"), documentCodeEntity1);
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9100", "EN"), documentCodeEntity2);
    documentCodeLocaleMap.put("9002", List.of("EN"));
    documentCodeLocaleMap.put("9100", List.of("EN"));

    // when
    final DocumentCodeDescriptions documentCodeDescriptions =
        documentCodeDescriptionService.getDocumentCodeDescriptions(null, EN, TradeType.IMPORT);
    // then
    assertThat(documentCodeDescriptions)
        .isNotNull()
        .extracting("documentCodeDescriptions")
        .asList()
        .hasSize(2)
        .containsExactlyInAnyOrder(
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Updated Movement certificate EUR-MED")
                .locale("EN")
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
  void shouldReturnDocumentCodeDescriptionsSpecificToCYLocale() {
    // given
    ObjectMapper objectMapper = new ObjectMapper();
    DocumentCodeEntity documentCodeEntity1 =
        objectMapper.readValue(
            "{\"locale\":\"CY\",\"documentCode\":\"9002\",\"importOverlay\":\"Updated Movement certificate EUR-MED\"}",
            DocumentCodeEntity.class);

    DocumentCodeEntity documentCodeEntity2 =
        objectMapper.readValue(
            "{\"locale\":\"CY\",\"documentCode\":\"9100\",\"importOverlay\":\"You need an import licence if your **firearms and ammunition** are:\\r\\n- within either chapter 93 (arms and ammunition) or chapter 97 (works of art, collectors' pieces and antiques) of the UK tariff\\r\\n- made after 31 December 1899\\r\\n\\r\\nContact the Department of International Trade's Import Controls Policy and Licencing team for further guidance.\\r\\n\\r\\nEmail: enquiries.ilb@trade.gov.uk\\r\\n\\r\\nYou need an import licence if your **nuclear material** falls under sub-chapters 2612 and 2844 of the UK Trade Tariff.\\r\\n\\r\\nYou should allow 2 months for your licence application to be processed.\\r\\n\\r\\n[Apply for a licence](https://www.gov.uk/guidance/importing-relevant-nuclear-materials-from-the-eu-licensing-requirements) from the Office for Nuclear Regulation (ONR).\\r\\n\\r\\n\"}",
            DocumentCodeEntity.class);

    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "CY"), documentCodeEntity1);
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9100", "CY"), documentCodeEntity2);
    documentCodeLocaleMap.put("9002", List.of("CY"));
    documentCodeLocaleMap.put("9100", List.of("CY"));

    // when
    final DocumentCodeDescriptions documentCodeDescriptions =
        documentCodeDescriptionService.getDocumentCodeDescriptions(null, CY, TradeType.IMPORT);
    // then
    assertThat(documentCodeDescriptions)
        .isNotNull()
        .extracting("documentCodeDescriptions")
        .asList()
        .hasSize(2)
        .containsExactlyInAnyOrder(
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Updated Movement certificate EUR-MED")
                .locale("CY")
                .tradeType("IMPORT")
                .build(),
            DocumentCodeDescription.builder()
                .documentCode("9100")
                .descriptionOverlay(
                    "You need an import licence if your **firearms and ammunition** are:\r\n- within either chapter 93 (arms and ammunition) or chapter 97 (works of art, collectors' pieces and antiques) of the UK tariff\r\n- made after 31 December 1899\r\n\r\nContact the Department of International Trade's Import Controls Policy and Licencing team for further guidance.\r\n\r\nEmail: enquiries.ilb@trade.gov.uk\r\n\r\nYou need an import licence if your **nuclear material** falls under sub-chapters 2612 and 2844 of the UK Trade Tariff.\r\n\r\nYou should allow 2 months for your licence application to be processed.\r\n\r\n[Apply for a licence](https://www.gov.uk/guidance/importing-relevant-nuclear-materials-from-the-eu-licensing-requirements) from the Office for Nuclear Regulation (ONR).\r\n\r\n")
                .locale("CY")
                .tradeType("IMPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturnDocumentCodeDescriptionsSpecificToCYLocaleButENOnlyExists() {
    // given
    documentCodeDescriptionCache.put(
        new DocumentCodeDescriptionKey("9002", "EN"),
        DocumentCodeEntity.builder()
            .documentCode("9002")
            .locale("EN")
            .importOverlay("Import Updated Movement certificate EUR-MED")
            .build());

    documentCodeLocaleMap.put("9002", List.of("EN"));

    // when
    final DocumentCodeDescriptions documentCodeDescriptions =
        documentCodeDescriptionService.getDocumentCodeDescriptions(
            List.of("9002"), CY, TradeType.IMPORT);
    // then
    assertThat(documentCodeDescriptions)
        .isNotNull()
        .extracting("documentCodeDescriptions")
        .asList()
        .hasSize(1)
        .containsExactlyInAnyOrder(
            DocumentCodeDescription.builder()
                .documentCode("9002")
                .descriptionOverlay("Import Updated Movement certificate EUR-MED")
                .locale("EN")
                .tradeType("IMPORT")
                .build());
  }
}
