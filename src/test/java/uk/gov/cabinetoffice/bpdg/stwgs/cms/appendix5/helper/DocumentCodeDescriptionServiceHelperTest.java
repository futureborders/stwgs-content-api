package uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.helper;

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
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.DocumentCodes;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.collection.DataCollection;

@ExtendWith(MockitoExtension.class)
class DocumentCodeDescriptionServiceHelperTest {
  @Mock private DataCollection documentCodeCollection;
  private DocumentCodeServiceHelper documentCodeServiceHelper;

  @BeforeEach
  void before() {
    documentCodeServiceHelper =
        new DocumentCodeServiceHelper(documentCodeCollection, new YAMLMapper());
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
    final DocumentCodes actualdocumentCodes = documentCodeServiceHelper.getDocumentCodes();
    // then
    assertThat(actualdocumentCodes).isNotNull();
    assertThat(actualdocumentCodes.getDocumentCodeList()).hasSize(4);
  }

  @AfterEach
  void after() {
    documentCodeServiceHelper = null;
  }
}
