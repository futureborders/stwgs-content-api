package uk.gov.cabinetoffice.bpdg.stwgs.cms.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ResourceNotFoundExceptionTest {

  @Test
  void shouldReturnMessageExpectedFormat() {
    // given
    var resourceNotFoundException = new ResourceNotFoundException("DocumentCode", "U567");

    // when+then
    assertThat(resourceNotFoundException.getMessage())
        .isEqualTo("Resource 'DocumentCode' not found with id 'U567'");
  }
}
