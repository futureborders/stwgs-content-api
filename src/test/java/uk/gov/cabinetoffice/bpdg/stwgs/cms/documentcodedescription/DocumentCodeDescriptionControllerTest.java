package uk.gov.cabinetoffice.bpdg.stwgs.cms.documentcodedescription;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.enums.Locale;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.enums.TradeType;

@ExtendWith(MockitoExtension.class)
class DocumentCodeDescriptionControllerTest {
  @Mock private DocumentCodeDescriptionService documentCodeDescriptionService;

  @InjectMocks private DocumentCodeDescriptionController documentCodeDescriptionController;

  @Test
  @SneakyThrows
  void shouldReturnDocumentCodeDescriptionsSpecificToDocumentCodesAndLocale() {
    // given
    HttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    ObjectMapper objectMapper = new ObjectMapper();
    var documentCodeDescription =
        objectMapper.readValue(
            "{\"id\":131,\"subtext\":\"\",\"locale\":\"EN\",\"url\":\"\",\"document_code\":\"9002\",\"description_overlay\":\"Updated Movement certificate EUR-MED\",\"url_text\":\"\",\"destination_country_restrictions\":[\"GB\",\"XI\"]}",
            DocumentCodeDescription.class);

    when(documentCodeDescriptionService.getDocumentCodeDescriptions(
            List.of("9002"), Locale.EN, TradeType.IMPORT))
        .thenReturn(
            DocumentCodeDescriptions.builder()
                .documentCodeDescriptions(List.of(documentCodeDescription))
                .build());

    // when
    DocumentCodeDescriptions responseEntity =
        documentCodeDescriptionController.getDocumentCodeDescriptions(
            List.of("9002"), Locale.EN, TradeType.IMPORT);

    // then
    assertThat(responseEntity.getDocumentCodeDescriptions()).hasSize(1);
  }

  @Test
  @SneakyThrows
  void shouldReturnAllDocumentCodeDescriptions() {
    // given
    HttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    ObjectMapper objectMapper = new ObjectMapper();
    var documentCodeDescription =
        objectMapper.readValue(
            "{\"id\":131,\"subtext\":\"\",\"locale\":\"EN\",\"url\":\"\",\"document_code\":\"9002\",\"description_overlay\":\"Updated Movement certificate EUR-MED\",\"url_text\":\"\",\"destination_country_restrictions\":[\"GB\",\"XI\"]}",
            DocumentCodeDescription.class);

    when(documentCodeDescriptionService.getDocumentCodeDescriptions(
            List.of("9002"), Locale.EN, TradeType.IMPORT))
        .thenReturn(
            DocumentCodeDescriptions.builder()
                .documentCodeDescriptions(List.of(documentCodeDescription))
                .build());

    // when
    DocumentCodeDescriptions responseEntity =
        documentCodeDescriptionController.getDocumentCodeDescriptions(
            List.of("9002"), Locale.EN, TradeType.IMPORT);

    // then
    assertThat(responseEntity.getDocumentCodeDescriptions()).hasSize(1);
  }

  @Test
  @SneakyThrows
  void shouldReturnEmptyDocumentCodeDescriptions() {
    // given
    HttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    when(documentCodeDescriptionService.getDocumentCodeDescriptions(
            List.of("XYZ"), Locale.EN, TradeType.IMPORT))
        .thenReturn(DocumentCodeDescriptions.builder().documentCodeDescriptions(List.of()).build());

    // when
    DocumentCodeDescriptions responseEntity =
        documentCodeDescriptionController.getDocumentCodeDescriptions(
            List.of("XYZ"), Locale.EN, TradeType.IMPORT);

    // then
    assertThat(responseEntity.getDocumentCodeDescriptions()).isEmpty();
  }
}
