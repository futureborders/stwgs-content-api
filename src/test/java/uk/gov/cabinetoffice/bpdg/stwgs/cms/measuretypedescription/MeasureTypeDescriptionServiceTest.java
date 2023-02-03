package uk.gov.cabinetoffice.bpdg.stwgs.cms.measuretypedescription;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.cabinetoffice.bpdg.stwgs.cms.enums.Locale.CY;
import static uk.gov.cabinetoffice.bpdg.stwgs.cms.enums.Locale.EN;
import static uk.gov.cabinetoffice.bpdg.stwgs.cms.enums.TradeType.EXPORT;
import static uk.gov.cabinetoffice.bpdg.stwgs.cms.enums.TradeType.IMPORT;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.enums.TradeType;

@ExtendWith(MockitoExtension.class)
class MeasureTypeDescriptionServiceTest {
  private static final HashMap<MeasureTypeDescriptionKey, MeasureTypeEntity>
      measureTypeDescriptionCache = new HashMap<>();
  private static Map<String, List<String>> measureTypeLocaleMap = new HashMap<>();
  private MeasureTypeDescriptionService measureTypeDescriptionService;

  @BeforeEach
  void before() {
    measureTypeDescriptionService =
        new MeasureTypeDescriptionService(measureTypeDescriptionCache, measureTypeLocaleMap);
  }

  @AfterEach
  void after() {
    measureTypeDescriptionCache.clear();
  }

  @Test
  @SneakyThrows
  void shouldReturnMeasureTypeDescriptions() {
    // given
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("143").locale("EN").build(),
        MeasureTypeEntity.builder()
            .measureType("143")
            .importOverlay("overlay for 143")
            .locale("EN")
            .build());
    measureTypeLocaleMap.put("143", List.of("EN"));

    // when
    final MeasureTypeDescriptions measureTypeDescriptions =
        measureTypeDescriptionService.getMeasureTypeDescriptions(
            List.of("143"), EN, TradeType.IMPORT);

    // then
    assertThat(measureTypeDescriptions)
        .isNotNull()
        .extracting("measureTypeDescriptions")
        .asList()
        .containsExactly(
            MeasureTypeDescription.builder()
                .measureType("143")
                .descriptionOverlay("overlay for 143")
                .locale("EN")
                .tradeType("IMPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturnAllMeasureTypeDescriptions() {
    // given
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("143").locale("EN").build(),
        MeasureTypeEntity.builder()
            .measureType("143")
            .importOverlay("overlay for 143")
            .locale("EN")
            .build());
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("145").locale("EN").build(),
        MeasureTypeEntity.builder()
            .measureType("145")
            .importOverlay("overlay for 145")
            .locale("EN")
            .build());
    measureTypeLocaleMap.put("143", List.of("EN"));
    measureTypeLocaleMap.put("145", List.of("EN"));

    // when
    final MeasureTypeDescriptions measureTypeDescriptions =
        measureTypeDescriptionService.getMeasureTypeDescriptions(null, null, null);

    // then
    assertThat(measureTypeDescriptions)
        .isNotNull()
        .extracting("measureTypeDescriptions")
        .asList()
        .containsExactlyInAnyOrder(
            MeasureTypeDescription.builder()
                .measureType("143")
                .descriptionOverlay("overlay for 143")
                .locale("EN")
                .tradeType("IMPORT")
                .build(),
            MeasureTypeDescription.builder()
                .measureType("145")
                .descriptionOverlay("overlay for 145")
                .locale("EN")
                .tradeType("IMPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturn145MeasureTypeDescriptions() {
    // given
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("143").locale("EN").build(),
        MeasureTypeEntity.builder()
            .measureType("143")
            .importOverlay("overlay for 143")
            .locale("EN")
            .build());
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("145").locale("EN").build(),
        MeasureTypeEntity.builder()
            .measureType("145")
            .importOverlay("overlay for 145")
            .exportOverlay("export overlay for 145")
            .locale("EN")
            .build());
    measureTypeLocaleMap.put("143", List.of("EN"));
    measureTypeLocaleMap.put("145", List.of("EN"));

    // when
    final MeasureTypeDescriptions measureTypeDescriptions =
        measureTypeDescriptionService.getMeasureTypeDescriptions(List.of("145"), EN, null);

    // then
    assertThat(measureTypeDescriptions)
        .isNotNull()
        .extracting("measureTypeDescriptions")
        .asList()
        .containsExactlyInAnyOrder(
            MeasureTypeDescription.builder()
                .measureType("145")
                .descriptionOverlay("overlay for 145")
                .locale("EN")
                .tradeType("IMPORT")
                .build(),
            MeasureTypeDescription.builder()
                .measureType("145")
                .descriptionOverlay("export overlay for 145")
                .locale("EN")
                .tradeType("EXPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturn145AllLocaleMeasureTypeDescriptions() {
    // given
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("143").locale("EN").build(),
        MeasureTypeEntity.builder()
            .measureType("143")
            .importOverlay("overlay for 143")
            .locale("EN")
            .build());
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("145").locale("EN").build(),
        MeasureTypeEntity.builder()
            .measureType("145")
            .importOverlay("overlay for 145")
            .locale("EN")
            .build());
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("143").locale("CY").build(),
        MeasureTypeEntity.builder()
            .measureType("143")
            .importOverlay("overlay for 143")
            .locale("CY")
            .build());
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("145").locale("CY").build(),
        MeasureTypeEntity.builder()
            .measureType("145")
            .importOverlay("overlay for 145")
            .locale("CY")
            .build());
    measureTypeLocaleMap.put("143", List.of("EN", "CY"));
    measureTypeLocaleMap.put("145", List.of("EN", "CY"));

    // when
    final MeasureTypeDescriptions measureTypeDescriptions =
        measureTypeDescriptionService.getMeasureTypeDescriptions(List.of("145"), null, null);

    // then
    assertThat(measureTypeDescriptions)
        .isNotNull()
        .extracting("measureTypeDescriptions")
        .asList()
        .containsExactlyInAnyOrder(
            MeasureTypeDescription.builder()
                .measureType("145")
                .descriptionOverlay("overlay for 145")
                .locale("EN")
                .tradeType("IMPORT")
                .build(),
            MeasureTypeDescription.builder()
                .measureType("145")
                .descriptionOverlay("overlay for 145")
                .locale("CY")
                .tradeType("IMPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturnMeasureTypeDescriptionsSpecificToENLocaleAndImportTradeType() {
    // given
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("143").locale("EN").build(),
        MeasureTypeEntity.builder()
            .measureType("143")
            .importOverlay("overlay for 143")
            .exportOverlay("export overlay for 143")
            .locale("EN")
            .build());
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("145").locale("EN").build(),
        MeasureTypeEntity.builder()
            .measureType("145")
            .importOverlay("overlay for 145")
            .exportOverlay("export overlay for 145")
            .locale("EN")
            .build());
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("145").locale("CY").build(),
        MeasureTypeEntity.builder()
            .measureType("145")
            .importOverlay("overlay for 145")
            .exportOverlay("export overlay for 145")
            .locale("CY")
            .build());
    measureTypeLocaleMap.put("143", List.of("EN"));
    measureTypeLocaleMap.put("145", List.of("EN", "CY"));

    // when
    final MeasureTypeDescriptions measureTypeDescriptions =
        measureTypeDescriptionService.getMeasureTypeDescriptions(null, EN, TradeType.IMPORT);

    // then
    assertThat(measureTypeDescriptions)
        .isNotNull()
        .extracting("measureTypeDescriptions")
        .asList()
        .containsExactlyInAnyOrder(
            MeasureTypeDescription.builder()
                .measureType("143")
                .descriptionOverlay("overlay for 143")
                .locale("EN")
                .tradeType("IMPORT")
                .build(),
            MeasureTypeDescription.builder()
                .measureType("145")
                .descriptionOverlay("overlay for 145")
                .locale("EN")
                .tradeType("IMPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturnMeasureTypeDescriptionsSpecificToENLocaleAndExportTradeType() {
    // given
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("143").locale("EN").build(),
        MeasureTypeEntity.builder()
            .measureType("143")
            .importOverlay("import overlay for 143")
            .exportOverlay("export overlay for 143")
            .locale("EN")
            .build());
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("145").locale("EN").build(),
        MeasureTypeEntity.builder()
            .measureType("145")
            .importOverlay("import overlay for 145")
            .exportOverlay("export overlay for 145")
            .locale("EN")
            .build());
    measureTypeLocaleMap.put("143", List.of("EN"));
    measureTypeLocaleMap.put("145", List.of("EN"));

    // when
    final MeasureTypeDescriptions measureTypeDescriptions =
        measureTypeDescriptionService.getMeasureTypeDescriptions(null, EN, EXPORT);

    // then
    assertThat(measureTypeDescriptions)
        .isNotNull()
        .extracting("measureTypeDescriptions")
        .asList()
        .containsExactlyInAnyOrder(
            MeasureTypeDescription.builder()
                .measureType("143")
                .descriptionOverlay("export overlay for 143")
                .locale("EN")
                .tradeType("EXPORT")
                .build(),
            MeasureTypeDescription.builder()
                .measureType("145")
                .descriptionOverlay("export overlay for 145")
                .locale("EN")
                .tradeType("EXPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturnMeasureTypeDescriptionsSpecificToCYLocale() {
    // given
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("143").locale("CY").build(),
        MeasureTypeEntity.builder()
            .measureType("143")
            .importOverlay("import overlay for 143")
            .exportOverlay("export overlay for 143")
            .locale("CY")
            .build());
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("145").locale("CY").build(),
        MeasureTypeEntity.builder()
            .measureType("145")
            .importOverlay("import overlay for 145")
            .exportOverlay("export overlay for 145")
            .locale("CY")
            .build());
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("145").locale("EN").build(),
        MeasureTypeEntity.builder()
            .measureType("145")
            .importOverlay("import overlay for 145")
            .exportOverlay("export overlay for 145")
            .locale("EN")
            .build());
    measureTypeLocaleMap.put("143", List.of("CY"));
    measureTypeLocaleMap.put("145", List.of("EN", "CY"));

    // when
    final MeasureTypeDescriptions measureTypeDescriptions =
        measureTypeDescriptionService.getMeasureTypeDescriptions(null, CY, null);

    // then
    assertThat(measureTypeDescriptions)
        .isNotNull()
        .extracting("measureTypeDescriptions")
        .asList()
        .containsExactlyInAnyOrder(
            MeasureTypeDescription.builder()
                .measureType("143")
                .descriptionOverlay("import overlay for 143")
                .locale("CY")
                .tradeType("IMPORT")
                .build(),
            MeasureTypeDescription.builder()
                .measureType("143")
                .descriptionOverlay("export overlay for 143")
                .locale("CY")
                .tradeType("EXPORT")
                .build(),
            MeasureTypeDescription.builder()
                .measureType("145")
                .descriptionOverlay("import overlay for 145")
                .locale("CY")
                .tradeType("IMPORT")
                .build(),
            MeasureTypeDescription.builder()
                .measureType("145")
                .descriptionOverlay("export overlay for 145")
                .locale("CY")
                .tradeType("EXPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturnMeasureTypeDescriptionsSpecificToENLocaleButCYOnlyExists() {
    // given
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("143").locale("CY").build(),
        MeasureTypeEntity.builder()
            .measureType("143")
            .importOverlay("import overlay for 143")
            .exportOverlay("export overlay for 143")
            .locale("CY")
            .build());
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("145").locale("CY").build(),
        MeasureTypeEntity.builder()
            .measureType("145")
            .importOverlay("import overlay for 145")
            .exportOverlay("export overlay for 145")
            .locale("CY")
            .build());
    measureTypeLocaleMap.put("143", List.of("CY"));
    measureTypeLocaleMap.put("145", List.of("CY"));

    // when
    final MeasureTypeDescriptions measureTypeDescriptions =
        measureTypeDescriptionService.getMeasureTypeDescriptions(null, EN, null);

    // then
    assertThat(measureTypeDescriptions)
        .isNotNull()
        .extracting("measureTypeDescriptions")
        .asList()
        .isEmpty();
  }

  @Test
  @SneakyThrows
  void shouldReturnMeasureTypeDescriptionsSpecificToENLocale() {
    // given
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("143").locale("EN").build(),
        MeasureTypeEntity.builder()
            .measureType("143")
            .importOverlay("import overlay for 143")
            .exportOverlay("export overlay for 143")
            .locale("EN")
            .build());
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("143").locale("CY").build(),
        MeasureTypeEntity.builder()
            .measureType("143")
            .importOverlay("import overlay for 143")
            .exportOverlay("export overlay for 143")
            .locale("CY")
            .build());
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("145").locale("EN").build(),
        MeasureTypeEntity.builder()
            .measureType("145")
            .importOverlay("import overlay for 145")
            .exportOverlay("export overlay for 145")
            .locale("EN")
            .build());
    measureTypeLocaleMap.put("143", List.of("EN", "CY"));
    measureTypeLocaleMap.put("145", List.of("EN"));

    // when
    final MeasureTypeDescriptions measureTypeDescriptions =
        measureTypeDescriptionService.getMeasureTypeDescriptions(null, EN, null);

    // then
    assertThat(measureTypeDescriptions)
        .isNotNull()
        .extracting("measureTypeDescriptions")
        .asList()
        .containsExactlyInAnyOrder(
            MeasureTypeDescription.builder()
                .measureType("143")
                .descriptionOverlay("import overlay for 143")
                .locale("EN")
                .tradeType("IMPORT")
                .build(),
            MeasureTypeDescription.builder()
                .measureType("143")
                .descriptionOverlay("export overlay for 143")
                .locale("EN")
                .tradeType("EXPORT")
                .build(),
            MeasureTypeDescription.builder()
                .measureType("145")
                .descriptionOverlay("import overlay for 145")
                .locale("EN")
                .tradeType("IMPORT")
                .build(),
            MeasureTypeDescription.builder()
                .measureType("145")
                .descriptionOverlay("export overlay for 145")
                .locale("EN")
                .tradeType("EXPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturnMeasureTypeDescriptionsSpecificToCYLocaleButENOnlyExists() {
    // given
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("143").locale("EN").build(),
        MeasureTypeEntity.builder()
            .measureType("143")
            .importOverlay("import overlay for 143")
            .exportOverlay("export overlay for 143")
            .locale("EN")
            .build());
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("145").locale("EN").build(),
        MeasureTypeEntity.builder()
            .measureType("145")
            .importOverlay("import overlay for 145")
            .exportOverlay("export overlay for 145")
            .locale("EN")
            .build());
    measureTypeLocaleMap.put("143", List.of("EN"));
    measureTypeLocaleMap.put("145", List.of("EN"));

    // when
    final MeasureTypeDescriptions measureTypeDescriptions =
        measureTypeDescriptionService.getMeasureTypeDescriptions(null, CY, TradeType.IMPORT);

    // then
    assertThat(measureTypeDescriptions)
        .isNotNull()
        .extracting("measureTypeDescriptions")
        .asList()
        .asList()
        .containsExactlyInAnyOrder(
            MeasureTypeDescription.builder()
                .measureType("143")
                .descriptionOverlay("import overlay for 143")
                .locale("EN")
                .tradeType("IMPORT")
                .build(),
            MeasureTypeDescription.builder()
                .measureType("145")
                .descriptionOverlay("import overlay for 145")
                .locale("EN")
                .tradeType("IMPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturnMeasureTypeDescriptionsExportSpecific() {
    // given
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("143").locale("EN").build(),
        MeasureTypeEntity.builder()
            .measureType("143")
            .exportOverlay("export overlay for 143")
            .locale("EN")
            .build());
    measureTypeLocaleMap.put("143", List.of("EN"));

    // when
    final MeasureTypeDescriptions measureTypeDescriptions =
        measureTypeDescriptionService.getMeasureTypeDescriptions(List.of("143"), EN, EXPORT);

    // then
    assertThat(measureTypeDescriptions)
        .isNotNull()
        .extracting("measureTypeDescriptions")
        .asList()
        .containsExactly(
            MeasureTypeDescription.builder()
                .measureType("143")
                .descriptionOverlay("export overlay for 143")
                .locale("EN")
                .tradeType("EXPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturnMeasureTypeDescriptionImportSpecificENDefault() {
    // given
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("143").locale("EN").build(),
        MeasureTypeEntity.builder()
            .measureType("143")
            .importOverlay("import overlay for 143")
            .exportOverlay("export overlay for 143")
            .locale("EN")
            .build());
    measureTypeLocaleMap.put("143", List.of("EN"));

    // when
    final MeasureTypeDescriptions measureTypeDescriptions =
        measureTypeDescriptionService.getMeasureTypeDescriptions(List.of("143"), CY, IMPORT);

    // then
    assertThat(measureTypeDescriptions)
        .isNotNull()
        .extracting("measureTypeDescriptions")
        .asList()
        .containsExactly(
            MeasureTypeDescription.builder()
                .measureType("143")
                .descriptionOverlay("import overlay for 143")
                .locale("EN")
                .tradeType("IMPORT")
                .build());
  }

  @Test
  @SneakyThrows
  void shouldReturnMeasureTypeDescriptionExportSpecificENDefault() {
    // given
    measureTypeDescriptionCache.put(
        MeasureTypeDescriptionKey.builder().measureType("143").locale("EN").build(),
        MeasureTypeEntity.builder()
            .measureType("143")
            .importOverlay("import overlay for 143")
            .exportOverlay("export overlay for 143")
            .locale("EN")
            .build());
    measureTypeLocaleMap.put("143", List.of("EN"));

    // when
    final MeasureTypeDescriptions measureTypeDescriptions =
        measureTypeDescriptionService.getMeasureTypeDescriptions(List.of("143"), CY, EXPORT);

    // then
    assertThat(measureTypeDescriptions)
        .isNotNull()
        .extracting("measureTypeDescriptions")
        .asList()
        .containsExactly(
            MeasureTypeDescription.builder()
                .measureType("143")
                .descriptionOverlay("export overlay for 143")
                .locale("EN")
                .tradeType("EXPORT")
                .build());
  }
}
