package uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.StatusCode;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.StatusCodes;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.service.StatusCodeService;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.exception.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
class StatusCodeControllerTest {
  @Mock private StatusCodeService statusCodeService;
  @InjectMocks private StatusCodeController statusCodeController;

  @Test
  void shouldReturnStatusCodes() {
    // given
    HttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    StatusCode statusCode =
        StatusCode.builder()
            .code("UP")
            .description(
                "Evidence required is unavailable — part use (applies to this and other entries)")
            .system("CDS")
            .build();
    StatusCodes statusCodes = StatusCodes.builder().statusCodeList(List.of(statusCode)).build();
    when(statusCodeService.getStatusCodes()).thenReturn(statusCodes);

    // when
    StatusCodes actualStatusCodes = statusCodeController.getStatusCodeDescriptions();

    // then
    assertThat(actualStatusCodes.getStatusCodeList()).hasSize(1);
  }

  @Test
  void shouldReturnStatusCodesByCode() {
    // given
    HttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    StatusCode expectedStatusCode =
        StatusCode.builder()
            .code("UP")
            .description(
                "Evidence required is unavailable — part use (applies to this and other entries)")
            .system("CDS")
            .build();

    when(statusCodeService.getStatusCode("CDS", "UP")).thenReturn(expectedStatusCode);

    // when
    StatusCode actualStatusCode = statusCodeController.getStatusCode("CDS", "UP");

    // then
    assertThat(actualStatusCode).isEqualTo(expectedStatusCode);
  }

  @Test
  void shouldReturnInvalidRequest() {

    // given
    HttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    when(statusCodeService.getStatusCode("s", "w"))
        .thenThrow(new ResourceNotFoundException("StatusCode", "w", "s"));

    // when+then
    assertThatThrownBy(() -> statusCodeController.getStatusCode("s", "w"))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("Resource 'StatusCode' not found with system 's' and code 'w'");
  }

  @Test
  void shouldReturnNotFoundStatusCode() {
    // given
    when(statusCodeService.getStatusCode("CDS", "UT"))
        .thenThrow(new ResourceNotFoundException("StatusCode", "UT", "CDS"));

    // when+then
    assertThatThrownBy(() -> statusCodeController.getStatusCode("CDS", "UT"))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("Resource 'StatusCode' not found with system 'CDS' and code 'UT'");
  }

  @Test
  void shouldReturnUnexpectedError() {
    // given
    when(statusCodeService.getStatusCode("CDS", "UO"))
        .thenThrow(new RuntimeException("Error occurred while reading json"));
    // when+then
    assertThatThrownBy(() -> statusCodeController.getStatusCode("CDS", "UO"))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Error occurred while reading json");
  }

  @Test
  void shouldReturnUnexpectedErrorWhenCalledGetAllStatusCodes() {
    // given
    when(statusCodeService.getStatusCodes())
        .thenThrow(new RuntimeException("Error occurred while reading json"));
    // when+then
    assertThatThrownBy(() -> statusCodeController.getStatusCodeDescriptions())
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Error occurred while reading json");
  }

  @Test
  void shouldReturn404ErrorWhenGetStatusCodeCalledWithWrongSystemAndCode() {
    // given
    when(statusCodeService.getStatusCodes())
        .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
    // when+then
    assertThatThrownBy(() -> statusCodeController.getStatusCodeDescriptions())
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("404 NOT_FOUND");
  }
}
