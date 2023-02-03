package uk.gov.cabinetoffice.bpdg.stwgs.cms.measuretypedescription;

import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.collection.DataCollection;

@Slf4j
@Configuration
public class MeasureTypeDescriptionsConfig {

  @Bean("measureTypeDescriptionsCache")
  public Map<MeasureTypeDescriptionKey, MeasureTypeEntity> measureTypeDescriptionsCache(
      @Qualifier("measureTypeDescriptionCollection")
          final DataCollection measureTypeDescriptionCollection,
      final YAMLMapper yamlMapper) {
    final File measureTypesDir = measureTypeDescriptionCollection.getCollectionSource().getFile();
    final List<File> measureTypeDeclarationFiles =
        Arrays.asList(Objects.requireNonNull(measureTypesDir.listFiles()));
    final Map<MeasureTypeDescriptionKey, MeasureTypeEntity> descriptionConcurrentMap =
        measureTypeDeclarationFiles.stream()
            .map(file -> convertFileToMeasureTypeDescription(file, yamlMapper))
            .collect(
                toMap(
                    (MeasureTypeEntity measureTypeEntity) ->
                        new MeasureTypeDescriptionKey(
                            measureTypeEntity.getMeasureType(), measureTypeEntity.getLocale()),
                    Function.identity()));
    log.info("******* MeasureTypeDescriptions cache has been initialized ***********");
    return new HashMap<>(descriptionConcurrentMap);
  }

  @Bean("measureTypeLocaleMap")
  public Map<String, List<String>> measureTypeLocaleMap(
      @Qualifier("measureTypeDescriptionCollection")
          final DataCollection measureTypeDescriptionCollection) {
    final File measureTypesDir = measureTypeDescriptionCollection.getCollectionSource().getFile();
    final List<File> measureTypeDeclarationFiles =
        Arrays.asList(Objects.requireNonNull(measureTypesDir.listFiles()));
    Map measureTypeLocaleMap = new HashMap<String, List<String>>();
    measureTypeDeclarationFiles.forEach(
        file -> {
          String fileName = file.getName();
          String[] fileNameSegments = fileName.split("_");
          String localeInFileName = fileNameSegments[4].replace(".yml", "");
          String measureTypeInFileName = fileNameSegments[3];
          if (measureTypeLocaleMap.get(measureTypeInFileName) != null) {
            final List<String> localeList =
                (List<String>) measureTypeLocaleMap.get(measureTypeInFileName);
            localeList.add(localeInFileName);
            measureTypeLocaleMap.replace(measureTypeInFileName, localeList);
          } else {
            final List<String> localeList = new ArrayList<>();
            localeList.add(localeInFileName);
            measureTypeLocaleMap.put(measureTypeInFileName, localeList);
          }
        });
    return measureTypeLocaleMap;
  }

  @SneakyThrows
  private MeasureTypeEntity convertFileToMeasureTypeDescription(
      final File file, final YAMLMapper yamlMapper) {
    MeasureTypeEntity measureTypeEntity = yamlMapper.readValue(file, MeasureTypeEntity.class);
    validateFileWithItsContent(file, measureTypeEntity);
    return measureTypeEntity;
  }

  private void validateFileWithItsContent(File file, MeasureTypeEntity measureTypeEntity) {
    Assert.state(
        measureTypeEntity.getMeasureType() != null,
        format("Measure type has to be present in the file %s", file.getName()));
    Assert.state(
        !(StringUtils.isBlank(measureTypeEntity.getImportOverlay())
            && StringUtils.isBlank(measureTypeEntity.getExportOverlay())
            && StringUtils.isBlank(measureTypeEntity.getOverlay())),
        format("Description overlay has to be present in the file %s", file.getName()));

    boolean isImportOrExportOverlay =
        StringUtils.isBlank(measureTypeEntity.getOverlay())
            && (StringUtils.isNotBlank(measureTypeEntity.getExportOverlay())
                || StringUtils.isNotBlank(measureTypeEntity.getImportOverlay()));

    boolean isImportAndExportOverlay =
        StringUtils.isNotBlank(measureTypeEntity.getOverlay())
            && (StringUtils.isBlank(measureTypeEntity.getExportOverlay())
                && StringUtils.isBlank(measureTypeEntity.getImportOverlay()));

    Assert.state(
        isImportOrExportOverlay || isImportAndExportOverlay,
        format(
            "Measure type shouldn't have default overlay in the file %s as it has trade type specific overlay",
            file.getName()));
    Assert.state(
        measureTypeEntity.getLocale() != null,
        format("Locale has to be present in the file %s", file.getName()));

    String fileName = file.getName();
    String[] fileNameSegments = fileName.split("_");
    Assert.state(
        fileNameSegments.length == 5,
        format("File name %s does not match the name pattern", fileName));

    String measureTypeInFileName = fileNameSegments[3];
    Assert.state(
        measureTypeInFileName.equals(measureTypeEntity.getMeasureType()),
        format(
            "Measure type %s in the file does not match the measure type %s in file name %s",
            measureTypeEntity.getMeasureType(), measureTypeInFileName, fileName));

    String localeInFileName = fileNameSegments[4].replace(".yml", "");
    Assert.state(
        localeInFileName.equals(measureTypeEntity.getLocale()),
        format(
            "Locale %s in the file does not match the locale %s in file name %s",
            measureTypeEntity.getLocale(), localeInFileName, fileName));
  }
}
