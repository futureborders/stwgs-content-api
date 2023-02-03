package uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.enums.TradeType.IMPORT;

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
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.DocumentCode;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.DocumentCodes;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.service.DocumentCodeService;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.exception.ResourceNotFoundException;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.model.ErrorResponse;

@ExtendWith(SpringExtension.class)
class DocumentCodeControllerTest {
  @Mock private DocumentCodeService documentCodeService;
  @InjectMocks private DocumentCodeController documentCodeController;

  @Test
  void shouldReturnDocumentCodeDescriptions() {
    // given
    HttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    DocumentCode documentCode =
        DocumentCode.builder().code("U057").tradeType(IMPORT.name()).build();
    DocumentCodes documentCodes =
        DocumentCodes.builder().documentCodeList(List.of(documentCode)).build();
    when(documentCodeService.getDocumentCodes()).thenReturn(documentCodes);
    // when
    DocumentCodes actualDocumentCodes = documentCodeController.getDocumentCodes();

    // then
    assertThat(actualDocumentCodes.getDocumentCodeList()).hasSize(1);
  }

  @Test
  void shouldReturnDocumentCodeDescriptionsByDocumentCode() {
    // given
    HttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    DocumentCode expectedDocumentCode =
        DocumentCode.builder().code("U057").tradeType(IMPORT.name()).build();
    when(documentCodeService.getDocumentCode("U057", "CDS", "IMPORT"))
        .thenReturn(expectedDocumentCode);

    // when
    DocumentCode actualDocumentCode =
        documentCodeController.getDocumentCode("U057", "CDS", "IMPORT");

    // then
    assertThat(actualDocumentCode).isEqualTo(expectedDocumentCode);
  }

  @Test
  void shouldReturnInvalidRequest() {
    // given
    HttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    when(documentCodeService.getDocumentCode("5t", "vfd", "imp"))
        .thenThrow(new ResourceNotFoundException("DocumentCode", "5t", "vfd", "imp"));

    // when+then
    assertThatThrownBy(() -> documentCodeController.getDocumentCode("5t", "vfd", "imp"))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining(
            "Resource 'DocumentCode' not found with trade type 'imp', system 'vfd' and code '5t'");
  }

  @Test
  void shouldReturnNotFoundDocumentCodeDescription() {
    // given
    HttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    when(documentCodeService.getDocumentCode("U051", "CDS", "IMPORT"))
        .thenThrow(new ResourceNotFoundException("DocumentCode", "U051", "CDS", "IMPORT"));

    // when+then
    assertThatThrownBy(() -> documentCodeController.getDocumentCode("U051", "CDS", "IMPORT"))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining(
            "Resource 'DocumentCode' not found with trade type 'IMPORT', system 'CDS' and code 'U051'");
  }

  @Test
  void shouldReturnUnexpectedError() {
    // given
    HttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    when(documentCodeService.getDocumentCode("U051", "CDS", "IMPORT"))
        .thenThrow(new RuntimeException("Error occurred while reading json"));
    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .httpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .httpStatusMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
            .description("Unexpected error")
            .build();

    // when+then
    assertThatThrownBy(() -> documentCodeController.getDocumentCode("U051", "CDS", "IMPORT"))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Error occurred while reading json");
  }

  @Test
  void shouldReturnUnexpectedErrorWhenCalledGetAllDocumentCodeDescriptions() {
    // given
    HttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    when(documentCodeService.getDocumentCodes())
        .thenThrow(new RuntimeException("Error occurred while reading json"));
    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .httpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .httpStatusMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
            .description("Unexpected error")
            .build();

    // when+then
    assertThatThrownBy(() -> documentCodeController.getDocumentCodes())
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Error occurred while reading json");
  }
}
