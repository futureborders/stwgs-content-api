package uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.DocumentCode;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.DocumentCodes;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.cache.DataCollectionsCache;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.collection.DataCollection;

@ExtendWith(MockitoExtension.class)
class DocumentCodeServiceTest {
  @Mock private DataCollection documentCodeCollection;
  @Mock private DataCollectionsCache<DocumentCodes> dataCollectionsCache;
  private DocumentCodeService documentCodeService;

  @BeforeEach
  void before() {
    documentCodeService =
        new DocumentCodeService(documentCodeCollection, new YAMLMapper(), dataCollectionsCache);
  }

  @Test
  @SneakyThrows
  void shouldReturnDocumentCodeDescriptions() {
    // given
    when(documentCodeCollection.getCollectionSource())
        .thenReturn(
            new FileSystemResource(
                new ClassPathResource("content/appendix5/document-codes").getFile()));
    // when
    final DocumentCodes documentCodes = documentCodeService.getDocumentCodes();
    // then
    assertThat(documentCodes.getDocumentCodeList()).hasSize(4);
  }

  @Test
  @SneakyThrows
  void shouldReturnDocumentCodeDescription() {
    // given
    when(documentCodeCollection.getCollectionSource())
        .thenReturn(
            new FileSystemResource(
                new ClassPathResource("content/appendix5/document-codes").getFile()));
    // when
    final DocumentCode documentCode = documentCodeService.getDocumentCode("2CVF", "CDS", "IMPORT");
    // then
    assertThat(documentCode).isNotNull();
  }

  @AfterEach
  void after() {
    documentCodeService = null;
  }
}
