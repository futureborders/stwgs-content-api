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
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.StatusCode;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.StatusCodes;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.cache.DataCollectionsCache;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.collection.DataCollection;

@ExtendWith(MockitoExtension.class)
class StatusCodeServiceTest {

  @Mock private DataCollection statusCodeDescriptionCollection;
  @Mock private DataCollectionsCache<StatusCodes> statusCodeCache;
  private StatusCodeService statusCodeService;

  @BeforeEach
  void before() {
    statusCodeService =
        new StatusCodeService(statusCodeDescriptionCollection, new YAMLMapper(), statusCodeCache);
  }

  @Test
  @SneakyThrows
  void shouldReturnStatusCodeDescriptions() {
    // given
    when(statusCodeDescriptionCollection.getCollectionSource())
        .thenReturn(
            new FileSystemResource(
                (new ClassPathResource("content/appendix5/status-codes").getFile())));
    // when
    final StatusCodes statusCodes = statusCodeService.getStatusCodes();
    // then
    assertThat(statusCodes.getStatusCodeList()).hasSize(2);
  }

  @Test
  @SneakyThrows
  void shouldReturnStatusCodeDescription() {
    // given
    when(statusCodeDescriptionCollection.getCollectionSource())
        .thenReturn(
            new FileSystemResource(
                (new ClassPathResource("content/appendix5/status-codes").getFile())));
    // when
    final StatusCode statusCode = statusCodeService.getStatusCode("CDS", "AE");
    // then
    assertThat(statusCode).isNotNull();
  }

  @AfterEach
  void after() {
    statusCodeService = null;
  }
}
