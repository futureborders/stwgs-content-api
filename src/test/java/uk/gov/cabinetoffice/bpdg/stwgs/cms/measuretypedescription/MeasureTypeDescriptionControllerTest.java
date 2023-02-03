package uk.gov.cabinetoffice.bpdg.stwgs.cms.measuretypedescription;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.cabinetoffice.bpdg.stwgs.cms.config.CmsCoreApiVersion.CMS_API_V1_VERSION;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.enums.Locale;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.enums.TradeType;

@WebMvcTest(MeasureTypeDescriptionsController.class)
class MeasureTypeDescriptionControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private MeasureTypeDescriptionService measureTypeDescriptionService;

  @Test
  @SneakyThrows
  void shouldReturnMeasureTypeDescriptionsSpecificToMeasureTypeAndLocale() {
    // given
    HttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    MeasureTypeDescription measureTypeDescription =
        MeasureTypeDescription.builder().measureType("123").build();

    when(measureTypeDescriptionService.getMeasureTypeDescriptions(
            List.of("123"), Locale.EN, TradeType.IMPORT))
        .thenReturn(
            MeasureTypeDescriptions.builder()
                .measureTypeDescriptions(List.of(measureTypeDescription))
                .build());

    // when
    this.mockMvc
        .perform(
            get(
                CMS_API_V1_VERSION
                    + "/measure-type-descriptions?measureTypes=123&locale=EN&tradeType=IMPORT"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("123")));
  }

  @Test
  @SneakyThrows
  void shouldReturnAllMeasureTypeDescriptions() {
    // given
    HttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    MeasureTypeDescription measureTypeDescription123 =
        MeasureTypeDescription.builder().measureType("123").build();
    MeasureTypeDescription measureTypeDescription124 =
        MeasureTypeDescription.builder().measureType("124").build();

    when(measureTypeDescriptionService.getMeasureTypeDescriptions(
            List.of("123", "124"), Locale.EN, TradeType.IMPORT))
        .thenReturn(
            MeasureTypeDescriptions.builder()
                .measureTypeDescriptions(
                    List.of(measureTypeDescription123, measureTypeDescription124))
                .build());

    // when
    this.mockMvc
        .perform(
            get(
                CMS_API_V1_VERSION
                    + "/measure-type-descriptions?measureTypes=123,124&locale=EN&tradeType=IMPORT"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("123")))
        .andExpect(content().string(containsString("124")));
  }

  @Test
  @SneakyThrows
  void shouldReturnEmptyMeasureTypeDescriptions() {
    // given
    HttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    when(measureTypeDescriptionService.getMeasureTypeDescriptions(
            List.of("XYZ"), Locale.EN, TradeType.IMPORT))
        .thenReturn(MeasureTypeDescriptions.builder().measureTypeDescriptions(List.of()).build());

    // when
    this.mockMvc
        .perform(
            get(
                CMS_API_V1_VERSION
                    + "/measure-type-descriptions?measureTypes=XYZ&locale=EN&tradeType=IMPORT"))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"measureTypeDescriptions\":[]}", true));
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestResponseWhenBadMeasureTypeIsPassed() {
    // given
    HttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

    // when
    this.mockMvc
        .perform(
            get(CMS_API_V1_VERSION + "/measure-type-descriptions?measureTypes=$9002&locale=EN"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestResponseWhenUnknownLocaleQueryParams() {
    // when
    this.mockMvc
        .perform(get(CMS_API_V1_VERSION + "/measure-type-descriptions?measureTypes=143&locale=FR"))
        .andExpect(status().isBadRequest());
  }
}
