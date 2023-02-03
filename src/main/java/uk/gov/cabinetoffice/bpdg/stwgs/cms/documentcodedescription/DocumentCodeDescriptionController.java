package uk.gov.cabinetoffice.bpdg.stwgs.cms.documentcodedescription;

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
@Tag(name = "DocumentCodeDescriptions", description = "Endpoint for document code descriptions")
public class DocumentCodeDescriptionController {

  private final DocumentCodeDescriptionService documentCodeService;

  @GetMapping(
      value = "/document-code-descriptions",
      produces = {"application/json"})
  @Operation(
      summary = "Finds document code descriptions by document codes and locale",
      description = "Returns a document code descriptions",
      tags = {"DocumentCodeDescriptions"},
      responses = {
        @ApiResponse(
            description = "Success",
            responseCode = "200",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DocumentCodeDescriptions.class))),
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
  public DocumentCodeDescriptions getDocumentCodeDescriptions(
      @Parameter(description = "Comma-separated document codes", example = "9111,9100")
          @RequestParam(required = false)
          List<@Valid @Pattern(regexp = "^[a-zA-Z0-9]*$") String> documentCodes,
      @Parameter(description = "Content locale", example = "EN") @RequestParam(required = false)
          Locale locale,
      @Parameter(description = "Trade Type", example = "IMPORT") @RequestParam(required = false)
          TradeType tradeType) {
    return documentCodeService.getDocumentCodeDescriptions(documentCodes, locale, tradeType);
  }
}
