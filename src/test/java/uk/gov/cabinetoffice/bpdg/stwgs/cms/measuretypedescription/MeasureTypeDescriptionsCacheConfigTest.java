package uk.gov.cabinetoffice.bpdg.stwgs.cms.measuretypedescription;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.util.Map;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.io.FileSystemResource;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.collection.DataCollection;

class MeasureTypeDescriptionsCacheConfigTest {

  private final MeasureTypeDescriptionsConfig measureTypeDescriptionsCacheConfig =
      new MeasureTypeDescriptionsConfig();

  @Test
  void shouldCacheFileContents() {
    DataCollection measureTypeDescriptionCollection =
        DataCollection.builder()
            .collectionSource(
                new FileSystemResource(
                    Objects.requireNonNull(
                            getClass()
                                .getClassLoader()
                                .getResource("unittest-content/measure-type-descriptions"))
                        .getFile()))
            .build();
    YAMLMapper yamlMapper = new YAMLMapper();

    Map<MeasureTypeDescriptionKey, MeasureTypeEntity>
        measureTypeDescriptionKeyMeasureTypeDescriptionMap =
            measureTypeDescriptionsCacheConfig.measureTypeDescriptionsCache(
                measureTypeDescriptionCollection, yamlMapper);

    assertThat(measureTypeDescriptionKeyMeasureTypeDescriptionMap).isNotNull();

    MeasureTypeDescriptionKey measure_143_en =
        MeasureTypeDescriptionKey.builder().measureType("143").locale("EN").build();
    MeasureTypeDescriptionKey measure_143_cy =
        MeasureTypeDescriptionKey.builder().measureType("143").locale("CY").build();
    MeasureTypeDescriptionKey measure_146_en =
        MeasureTypeDescriptionKey.builder().measureType("146").locale("EN").build();
    MeasureTypeDescriptionKey measure_146_cy =
        MeasureTypeDescriptionKey.builder().measureType("146").locale("CY").build();
    MeasureTypeDescriptionKey measure_147_en =
        MeasureTypeDescriptionKey.builder().measureType("147").locale("EN").build();
    MeasureTypeDescriptionKey measure_147_cy =
        MeasureTypeDescriptionKey.builder().measureType("147").locale("CY").build();
    assertThat(measureTypeDescriptionKeyMeasureTypeDescriptionMap)
        .containsOnlyKeys(
            measure_143_en,
            measure_143_cy,
            measure_146_en,
            measure_146_cy,
            measure_147_en,
            measure_147_cy)
        .containsEntry(
            measure_143_en,
            MeasureTypeEntity.builder()
                .measureType("143")
                .locale("EN")
                .importOverlay("143 overlay")
                .build())
        .containsEntry(
            measure_143_cy,
            MeasureTypeEntity.builder()
                .measureType("143")
                .locale("CY")
                .importOverlay("143 overlay CY")
                .build())
        .containsEntry(
            measure_146_en,
            MeasureTypeEntity.builder()
                .measureType("146")
                .locale("EN")
                .exportOverlay("146 overlay")
                .build())
        .containsEntry(
            measure_146_cy,
            MeasureTypeEntity.builder()
                .measureType("146")
                .locale("CY")
                .exportOverlay("146 overlay CY")
                .build())
        .containsEntry(
            measure_147_en,
            MeasureTypeEntity.builder()
                .measureType("147")
                .locale("EN")
                .overlay("147 overlay")
                .build())
        .containsEntry(
            measure_147_cy,
            MeasureTypeEntity.builder()
                .measureType("147")
                .locale("CY")
                .overlay("147 overlay CY")
                .build());
  }

  @Test
  void shouldRaiseExceptionWhenFolderContainAnyNonMeasureTypeDescriptionFiles() {
    DataCollection measureTypeDescriptionCollection =
        DataCollection.builder()
            .collectionSource(
                new FileSystemResource(
                    Objects.requireNonNull(
                            getClass()
                                .getClassLoader()
                                .getResource(
                                    "unittest-content/measure-type-descriptions-unrecognised-file"))
                        .getFile()))
            .build();
    YAMLMapper yamlMapper = new YAMLMapper();

    IllegalStateException illegalStateException =
        assertThrows(
            IllegalStateException.class,
            () ->
                measureTypeDescriptionsCacheConfig.measureTypeDescriptionsCache(
                    measureTypeDescriptionCollection, yamlMapper));

    assertThat(illegalStateException)
        .hasMessage(
            "Measure type has to be present in the file document_code_description_9002_CY.yml");
  }

  @Test
  void shouldRaiseExceptionWhenFolderContainAMeasureTypeDescriptionFileWithNoLocale() {
    DataCollection measureTypeDescriptionCollection =
        DataCollection.builder()
            .collectionSource(
                new FileSystemResource(
                    Objects.requireNonNull(
                            getClass()
                                .getClassLoader()
                                .getResource(
                                    "unittest-content/measure-type-descriptions-with-no-locale"))
                        .getFile()))
            .build();
    YAMLMapper yamlMapper = new YAMLMapper();

    IllegalStateException illegalStateException =
        assertThrows(
            IllegalStateException.class,
            () ->
                measureTypeDescriptionsCacheConfig.measureTypeDescriptionsCache(
                    measureTypeDescriptionCollection, yamlMapper));

    assertThat(illegalStateException)
        .hasMessage("Locale has to be present in the file measure_type_description_143_CY.yml");
  }

  @Test
  void shouldRaiseExceptionWhenFolderContainAMeasureTypeDescriptionFileWithNoOverlay() {
    DataCollection measureTypeDescriptionCollection =
        DataCollection.builder()
            .collectionSource(
                new FileSystemResource(
                    Objects.requireNonNull(
                            getClass()
                                .getClassLoader()
                                .getResource(
                                    "unittest-content/measure-type-descriptions-with-no-overlay"))
                        .getFile()))
            .build();
    YAMLMapper yamlMapper = new YAMLMapper();

    IllegalStateException illegalStateException =
        assertThrows(
            IllegalStateException.class,
            () ->
                measureTypeDescriptionsCacheConfig.measureTypeDescriptionsCache(
                    measureTypeDescriptionCollection, yamlMapper));

    assertThat(illegalStateException)
        .hasMessage(
            "Description overlay has to be present in the file measure_type_description_143_CY.yml");
  }

  @Test
  void shouldRaiseExceptionWhenFolderContainAMeasureTypeDescriptionFileWithNoMeasureType() {
    DataCollection measureTypeDescriptionCollection =
        DataCollection.builder()
            .collectionSource(
                new FileSystemResource(
                    Objects.requireNonNull(
                            getClass()
                                .getClassLoader()
                                .getResource(
                                    "unittest-content/measure-type-descriptions-with-no-measure-type"))
                        .getFile()))
            .build();
    YAMLMapper yamlMapper = new YAMLMapper();

    IllegalStateException illegalStateException =
        assertThrows(
            IllegalStateException.class,
            () ->
                measureTypeDescriptionsCacheConfig.measureTypeDescriptionsCache(
                    measureTypeDescriptionCollection, yamlMapper));

    assertThat(illegalStateException)
        .hasMessage(
            "Measure type has to be present in the file measure_type_description_143_CY.yml");
  }

  @Test
  void shouldRaiseExceptionWhenFolderContainAMeasureTypeDescriptionFileWithUnRecognisedFileName() {
    DataCollection measureTypeDescriptionCollection =
        DataCollection.builder()
            .collectionSource(
                new FileSystemResource(
                    Objects.requireNonNull(
                            getClass()
                                .getClassLoader()
                                .getResource(
                                    "unittest-content/measure-type-descriptions-with-unrecognised-file-name"))
                        .getFile()))
            .build();
    YAMLMapper yamlMapper = new YAMLMapper();

    IllegalStateException illegalStateException =
        assertThrows(
            IllegalStateException.class,
            () ->
                measureTypeDescriptionsCacheConfig.measureTypeDescriptionsCache(
                    measureTypeDescriptionCollection, yamlMapper));

    assertThat(illegalStateException)
        .hasMessage("File name measure_type_description_143.yml does not match the name pattern");
  }

  @Test
  void
      shouldRaiseExceptionWhenFolderContainAMeasureTypeDescriptionFileHavingADifferentMeasureTypeToTheFileName() {
    DataCollection measureTypeDescriptionCollection =
        DataCollection.builder()
            .collectionSource(
                new FileSystemResource(
                    Objects.requireNonNull(
                            getClass()
                                .getClassLoader()
                                .getResource(
                                    "unittest-content/measure-type-descriptions-with-measure-type-mismatch"))
                        .getFile()))
            .build();
    YAMLMapper yamlMapper = new YAMLMapper();

    IllegalStateException illegalStateException =
        assertThrows(
            IllegalStateException.class,
            () ->
                measureTypeDescriptionsCacheConfig.measureTypeDescriptionsCache(
                    measureTypeDescriptionCollection, yamlMapper));

    assertThat(illegalStateException)
        .hasMessage(
            "Measure type 145 in the file does not match the measure type 143 in file name measure_type_description_143_CY.yml");
  }

  @Test
  void
      shouldRaiseExceptionWhenFolderContainAMeasureTypeDescriptionFileHavingADifferentLocaleToTheFileName() {
    DataCollection measureTypeDescriptionCollection =
        DataCollection.builder()
            .collectionSource(
                new FileSystemResource(
                    Objects.requireNonNull(
                            getClass()
                                .getClassLoader()
                                .getResource(
                                    "unittest-content/measure-type-descriptions-with-locale-mismatch"))
                        .getFile()))
            .build();
    YAMLMapper yamlMapper = new YAMLMapper();

    IllegalStateException illegalStateException =
        assertThrows(
            IllegalStateException.class,
            () ->
                measureTypeDescriptionsCacheConfig.measureTypeDescriptionsCache(
                    measureTypeDescriptionCollection, yamlMapper));

    assertThat(illegalStateException)
        .hasMessage(
            "Locale EN in the file does not match the locale CY in file name measure_type_description_143_CY.yml");
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "unittest-content/measure-type-descriptions-with-default-import",
        "unittest-content/measure-type-descriptions-with-default-export",
        "unittest-content/measure-type-descriptions-with-default-import-export"
      })
  void
      shouldRaiseExceptionWhenFolderContainAMeasureTypeDescriptionWithDefaultAndTradeTypeSpecificExist(
          String directory) {
    DataCollection measureTypeDescriptionCollection =
        DataCollection.builder()
            .collectionSource(
                new FileSystemResource(
                    Objects.requireNonNull(getClass().getClassLoader().getResource(directory))
                        .getFile()))
            .build();
    YAMLMapper yamlMapper = new YAMLMapper();

    IllegalStateException illegalStateException =
        assertThrows(
            IllegalStateException.class,
            () ->
                measureTypeDescriptionsCacheConfig.measureTypeDescriptionsCache(
                    measureTypeDescriptionCollection, yamlMapper));

    assertThat(illegalStateException)
        .hasMessage(
            "Measure type shouldn't have default overlay in the file measure_type_description_143_EN.yml as it has trade type specific overlay");
  }
}
