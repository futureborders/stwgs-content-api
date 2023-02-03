package uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.helper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.StatusCodes;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.collection.DataCollection;

@ExtendWith(MockitoExtension.class)
class StatusCodeServiceHelperTest {
  @Mock private DataCollection statusCodeCollection;
  private StatusCodeServiceHelper statusCodeServiceHelper;

  @BeforeEach
  void setUp() {
    statusCodeServiceHelper = new StatusCodeServiceHelper(statusCodeCollection, new YAMLMapper());
  }

  @Test
  @SneakyThrows
  void shouldReturnStatusCodeDescriptions() {
    // given
    when(statusCodeCollection.getCollectionSource())
        .thenReturn(
            new FileSystemResource(
                (new ClassPathResource("content/appendix5/status-codes").getFile())));
    // when
    final StatusCodes actualStatusCodes = statusCodeServiceHelper.getStatusCodes();
    // then
    assertThat(actualStatusCodes).isNotNull();
    assertThat(actualStatusCodes.getStatusCodeList()).hasSize(2);
  }
}
