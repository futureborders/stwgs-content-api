package uk.gov.cabinetoffice.bpdg.stwgs.cms.measuretypedescription;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.config.CmsCoreApiVersion;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.enums.Locale;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.enums.TradeType;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.model.ErrorResponse;

@RestController
@RequestMapping(value = CmsCoreApiVersion.CMS_API_V1_VERSION)
@Validated
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
@Tag(name = "MeasureTypeDescriptions", description = "Endpoint for measure type descriptions")
public class MeasureTypeDescriptionsController {

  private final MeasureTypeDescriptionService measureTypeDescriptionService;

  @GetMapping(
      value = "/measure-type-descriptions",
      produces = {"application/json"})
  @Operation(
      summary = "Finds measure type descriptions by measure types and locale",
      description = "Returns measure type descriptions for the measure types and locale requested",
      tags = {"MeasureTypeDescriptions"},
      responses = {
        @ApiResponse(
            description = "Success",
            responseCode = "200",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MeasureTypeDescriptions.class))),
        @ApiResponse(
            description = "Bad request",
            responseCode = "400",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            description = "Internal error",
            responseCode = "500",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
      })
  public MeasureTypeDescriptions getMeasureTypeDescriptions(
      @Parameter(description = "Comma-separated measure types", example = "143,EHC")
          @RequestParam(required = false)
          List<@Valid @Pattern(regexp = "^[a-zA-Z0-9]*$") String> measureTypes,
      @Parameter(description = "Content locale", example = "EN") @RequestParam(required = false)
          Locale locale,
      @Parameter(description = "Trade Type", example = "IMPORT") @RequestParam(required = false)
          TradeType tradeType) {
    return measureTypeDescriptionService.getMeasureTypeDescriptions(
        measureTypes, locale, tradeType);
  }
}
