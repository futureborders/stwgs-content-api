package uk.gov.cabinetoffice.bpdg.stwgs.cms.documentcodedescription;

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
public class DocumentCodeDescriptionsConfig {

  @Bean("documentCodeDescriptionsCache")
  public Map<DocumentCodeDescriptionKey, DocumentCodeEntity> documentCodeDescriptionsCache(
      @Qualifier("documentCodeDescriptionCollection")
          final DataCollection documentCodeDescriptionCollection,
      final YAMLMapper yamlMapper) {
    final File documentCodesDir = documentCodeDescriptionCollection.getCollectionSource().getFile();
    final List<File> documentCodeDeclarationFiles =
        Arrays.asList(Objects.requireNonNull(documentCodesDir.listFiles()));
    final Map<DocumentCodeDescriptionKey, DocumentCodeEntity> descriptionConcurrentMap =
        documentCodeDeclarationFiles.stream()
            .map(file -> convertFileToDocumentCodeEntity(file, yamlMapper))
            .collect(
                toMap(
                    (DocumentCodeEntity entity) ->
                        new DocumentCodeDescriptionKey(
                            entity.getDocumentCode(), entity.getLocale()),
                    Function.identity()));

    log.info("******* DocumentCodeDescriptionsCache Data Cache has initialized ***********");
    return new HashMap<>(descriptionConcurrentMap);
  }

  @Bean("documentCodeLocaleMap")
  public Map<String, List<String>> documentCodeLocaleMap(
      @Qualifier("documentCodeDescriptionCollection")
          final DataCollection documentCodeDescriptionCollection) {
    final File documentCodesDir = documentCodeDescriptionCollection.getCollectionSource().getFile();
    final List<File> documentCodeDeclarationFiles =
        Arrays.asList(Objects.requireNonNull(documentCodesDir.listFiles()));
    Map<String, List<String>> documentCodeLocaleMap = new HashMap<>();
    documentCodeDeclarationFiles.forEach(
        file -> {
          String fileName = file.getName();
          String[] fileNameSegments = fileName.split("_");
          String localeInFileName = fileNameSegments[4].replace(".yml", "");
          String documentCodeInFileName = fileNameSegments[3];
          if (documentCodeLocaleMap.get(documentCodeInFileName) != null) {
            final List<String> localeList = documentCodeLocaleMap.get(documentCodeInFileName);
            localeList.add(localeInFileName);
            documentCodeLocaleMap.replace(documentCodeInFileName, localeList);
          } else {
            final List<String> localeList = new ArrayList<>();
            localeList.add(localeInFileName);
            documentCodeLocaleMap.put(documentCodeInFileName, localeList);
          }
        });
    return documentCodeLocaleMap;
  }

  @SneakyThrows
  private DocumentCodeEntity convertFileToDocumentCodeEntity(
      final File file, final YAMLMapper yamlMapper) {
    DocumentCodeEntity documentCodeEntity = yamlMapper.readValue(file, DocumentCodeEntity.class);
    validateFileWithItsContent(file, documentCodeEntity);
    return documentCodeEntity;
  }

  private void validateFileWithItsContent(File file, DocumentCodeEntity documentCodeEntity) {
    Assert.state(
        documentCodeEntity.getDocumentCode() != null,
        format("Document code has to be present in the file %s", file.getName()));
    Assert.state(
        !(StringUtils.isBlank(documentCodeEntity.getImportOverlay())
            && StringUtils.isBlank(documentCodeEntity.getExportOverlay())
            && StringUtils.isBlank(documentCodeEntity.getOverlay())),
        format("Description overlay has to be present in the file %s", file.getName()));

    boolean isImportOrExportOverlay =
        StringUtils.isBlank(documentCodeEntity.getOverlay())
            && (StringUtils.isNotBlank(documentCodeEntity.getExportOverlay())
                || StringUtils.isNotBlank(documentCodeEntity.getImportOverlay()));

    boolean isImportAndExportOverlay =
        StringUtils.isNotBlank(documentCodeEntity.getOverlay())
            && (StringUtils.isBlank(documentCodeEntity.getExportOverlay())
                && StringUtils.isBlank(documentCodeEntity.getImportOverlay()));

    Assert.state(
        isImportOrExportOverlay || isImportAndExportOverlay,
        format(
            "Description shouldn't have default overlay in the file %s as it has trade type specific overlay",
            file.getName()));
    Assert.state(
        documentCodeEntity.getLocale() != null,
        format("Locale has to be present in the file %s", file.getName()));

    String fileName = file.getName();
    String[] fileNameSegments = fileName.split("_");
    Assert.state(
        fileNameSegments.length == 5,
        format("File name %s does not match the name pattern", fileName));

    String documentCodeInFileName = fileNameSegments[3];
    Assert.state(
        documentCodeInFileName.equals(documentCodeEntity.getDocumentCode()),
        format(
            "Document code %s in the file does not match the document code %s in file name %s",
            documentCodeEntity.getDocumentCode(), documentCodeInFileName, fileName));

    String localeInFileName = fileNameSegments[4].replace(".yml", "");
    Assert.state(
        localeInFileName.equals(documentCodeEntity.getLocale()),
        format(
            "Locale %s in the file does not match the locale %s in file name %s",
            documentCodeEntity.getLocale(), localeInFileName, fileName));
  }
}
