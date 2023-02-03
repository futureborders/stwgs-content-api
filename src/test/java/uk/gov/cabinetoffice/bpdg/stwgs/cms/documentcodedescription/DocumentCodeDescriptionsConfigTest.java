package uk.gov.cabinetoffice.bpdg.stwgs.cms.documentcodedescription;

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

class DocumentCodeDescriptionsConfigTest {

  private final DocumentCodeDescriptionsConfig documentCodeDescriptionsCacheConfig =
      new DocumentCodeDescriptionsConfig();

  @Test
  void shouldCacheFileContents() {
    DataCollection documentCodeDescriptionCollection =
        DataCollection.builder()
            .collectionSource(
                new FileSystemResource(
                    Objects.requireNonNull(
                            getClass()
                                .getClassLoader()
                                .getResource("unittest-content/document-code-descriptions"))
                        .getFile()))
            .build();
    YAMLMapper yamlMapper = new YAMLMapper();

    Map<DocumentCodeDescriptionKey, DocumentCodeEntity>
        documentCodeDescriptionKeyDocumentCodeDescriptionMap =
            documentCodeDescriptionsCacheConfig.documentCodeDescriptionsCache(
                documentCodeDescriptionCollection, yamlMapper);

    assertThat(documentCodeDescriptionKeyDocumentCodeDescriptionMap).isNotNull();

    DocumentCodeDescriptionKey document_code_9002_en =
        DocumentCodeDescriptionKey.builder().documentCode("9002").locale("EN").build();
    DocumentCodeDescriptionKey document_code_9002_cy =
        DocumentCodeDescriptionKey.builder().documentCode("9002").locale("CY").build();
    DocumentCodeDescriptionKey document_code_9100_en =
        DocumentCodeDescriptionKey.builder().documentCode("9100").locale("EN").build();
    DocumentCodeDescriptionKey document_code_9100_cy =
        DocumentCodeDescriptionKey.builder().documentCode("9100").locale("CY").build();
    assertThat(documentCodeDescriptionKeyDocumentCodeDescriptionMap)
        .containsOnlyKeys(
            document_code_9002_en,
            document_code_9002_cy,
            document_code_9100_en,
            document_code_9100_cy)
        .containsEntry(
            document_code_9002_en,
            DocumentCodeEntity.builder()
                .documentCode("9002")
                .locale("EN")
                .importOverlay("Updated Movement certificate EUR-MED")
                .build())
        .containsEntry(
            document_code_9002_cy,
            DocumentCodeEntity.builder()
                .documentCode("9002")
                .locale("CY")
                .importOverlay("Movement certificate EUR-MED")
                .build())
        .containsEntry(
            document_code_9100_en,
            DocumentCodeEntity.builder()
                .documentCode("9100")
                .locale("EN")
                .importOverlay("9100 overlay")
                .build())
        .containsEntry(
            document_code_9100_cy,
            DocumentCodeEntity.builder()
                .documentCode("9100")
                .locale("CY")
                .importOverlay("9100 overlay CY")
                .build());
  }

  @Test
  void shouldRaiseExceptionWhenFolderContainAnyNonDocumentCodeDescriptionFiles() {
    DataCollection documentCodeDescriptionCollection =
        DataCollection.builder()
            .collectionSource(
                new FileSystemResource(
                    Objects.requireNonNull(
                            getClass()
                                .getClassLoader()
                                .getResource(
                                    "unittest-content/document-code-descriptions-unrecognised-file"))
                        .getFile()))
            .build();
    YAMLMapper yamlMapper = new YAMLMapper();

    IllegalStateException illegalStateException =
        assertThrows(
            IllegalStateException.class,
            () ->
                documentCodeDescriptionsCacheConfig.documentCodeDescriptionsCache(
                    documentCodeDescriptionCollection, yamlMapper));

    assertThat(illegalStateException)
        .hasMessage(
            "Document code has to be present in the file measure_type_description_143_CY.yml");
  }

  @Test
  void shouldRaiseExceptionWhenFolderContainADocumentCodeDescriptionFileWithNoLocale() {
    DataCollection documentCodeDescriptionCollection =
        DataCollection.builder()
            .collectionSource(
                new FileSystemResource(
                    Objects.requireNonNull(
                            getClass()
                                .getClassLoader()
                                .getResource(
                                    "unittest-content/document-code-descriptions-with-no-locale"))
                        .getFile()))
            .build();
    YAMLMapper yamlMapper = new YAMLMapper();

    IllegalStateException illegalStateException =
        assertThrows(
            IllegalStateException.class,
            () ->
                documentCodeDescriptionsCacheConfig.documentCodeDescriptionsCache(
                    documentCodeDescriptionCollection, yamlMapper));

    assertThat(illegalStateException)
        .hasMessage("Locale has to be present in the file document_code_description_9002_CY.yml");
  }

  @Test
  void shouldRaiseExceptionWhenFolderContainADocumentCodeDescriptionFileWithNoOverlay() {
    DataCollection documentCodeDescriptionCollection =
        DataCollection.builder()
            .collectionSource(
                new FileSystemResource(
                    Objects.requireNonNull(
                            getClass()
                                .getClassLoader()
                                .getResource(
                                    "unittest-content/document-code-descriptions-with-no-overlay"))
                        .getFile()))
            .build();
    YAMLMapper yamlMapper = new YAMLMapper();

    IllegalStateException illegalStateException =
        assertThrows(
            IllegalStateException.class,
            () ->
                documentCodeDescriptionsCacheConfig.documentCodeDescriptionsCache(
                    documentCodeDescriptionCollection, yamlMapper));

    assertThat(illegalStateException)
        .hasMessage(
            "Description overlay has to be present in the file document_code_description_9002_CY.yml");
  }

  @Test
  void shouldRaiseExceptionWhenFolderContainADocumentCodeDescriptionFileWithNoDocumentCode() {
    DataCollection documentCodeDescriptionCollection =
        DataCollection.builder()
            .collectionSource(
                new FileSystemResource(
                    Objects.requireNonNull(
                            getClass()
                                .getClassLoader()
                                .getResource(
                                    "unittest-content/document-code-descriptions-with-no-document-code"))
                        .getFile()))
            .build();
    YAMLMapper yamlMapper = new YAMLMapper();

    IllegalStateException illegalStateException =
        assertThrows(
            IllegalStateException.class,
            () ->
                documentCodeDescriptionsCacheConfig.documentCodeDescriptionsCache(
                    documentCodeDescriptionCollection, yamlMapper));

    assertThat(illegalStateException)
        .hasMessage(
            "Document code has to be present in the file document_code_description_9002_CY.yml");
  }

  @Test
  void shouldRaiseExceptionWhenFolderContainADocumentCodeDescriptionFileWithUnRecognisedFileName() {
    DataCollection documentCodeDescriptionCollection =
        DataCollection.builder()
            .collectionSource(
                new FileSystemResource(
                    Objects.requireNonNull(
                            getClass()
                                .getClassLoader()
                                .getResource(
                                    "unittest-content/document-code-descriptions-with-unrecognised-file-name"))
                        .getFile()))
            .build();
    YAMLMapper yamlMapper = new YAMLMapper();

    IllegalStateException illegalStateException =
        assertThrows(
            IllegalStateException.class,
            () ->
                documentCodeDescriptionsCacheConfig.documentCodeDescriptionsCache(
                    documentCodeDescriptionCollection, yamlMapper));

    assertThat(illegalStateException)
        .hasMessage("File name document_code_description_9005.yml does not match the name pattern");
  }

  @Test
  void
      shouldRaiseExceptionWhenFolderContainADocumentCodeDescriptionFileHavingADifferentDocumentCodeToTheFileName() {
    DataCollection documentCodeDescriptionCollection =
        DataCollection.builder()
            .collectionSource(
                new FileSystemResource(
                    Objects.requireNonNull(
                            getClass()
                                .getClassLoader()
                                .getResource(
                                    "unittest-content/document-code-descriptions-with-document-code-mismatch"))
                        .getFile()))
            .build();
    YAMLMapper yamlMapper = new YAMLMapper();

    IllegalStateException illegalStateException =
        assertThrows(
            IllegalStateException.class,
            () ->
                documentCodeDescriptionsCacheConfig.documentCodeDescriptionsCache(
                    documentCodeDescriptionCollection, yamlMapper));

    assertThat(illegalStateException)
        .hasMessage(
            "Document code 9002 in the file does not match the document code 9005 in file name document_code_description_9005_CY.yml");
  }

  @Test
  void
      shouldRaiseExceptionWhenFolderContainADocumentCodeDescriptionFileHavingADifferentLocaleToTheFileName() {
    DataCollection documentCodeDescriptionCollection =
        DataCollection.builder()
            .collectionSource(
                new FileSystemResource(
                    Objects.requireNonNull(
                            getClass()
                                .getClassLoader()
                                .getResource(
                                    "unittest-content/document-code-descriptions-with-locale-mismatch"))
                        .getFile()))
            .build();
    YAMLMapper yamlMapper = new YAMLMapper();

    IllegalStateException illegalStateException =
        assertThrows(
            IllegalStateException.class,
            () ->
                documentCodeDescriptionsCacheConfig.documentCodeDescriptionsCache(
                    documentCodeDescriptionCollection, yamlMapper));

    assertThat(illegalStateException)
        .hasMessage(
            "Locale EN in the file does not match the locale CY in file name document_code_description_9005_CY.yml");
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "unittest-content/document-code-descriptions-with-default-import",
        "unittest-content/document-code-descriptions-with-default-export",
        "unittest-content/document-code-descriptions-with-default-import-export"
      })
  void
      shouldRaiseExceptionWhenFolderContainADocumentCodeDescriptionFileWithDefaultAndTradeTypeSpecificExist(
          String directory) {
    DataCollection documentCodeDescriptionCollection =
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
                documentCodeDescriptionsCacheConfig.documentCodeDescriptionsCache(
                    documentCodeDescriptionCollection, yamlMapper));

    assertThat(illegalStateException)
        .hasMessage(
            "Description shouldn't have default overlay in the file document_code_description_9002_EN.yml as it has trade type specific overlay");
  }
}
