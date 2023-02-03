package uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.DocumentCode;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.DocumentCodes;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.service.DocumentCodeService;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.config.CmsCoreApiVersion;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.model.ErrorResponse;

@RestController
@RequestMapping(value = CmsCoreApiVersion.APPENDIX5_API_V1_VERSION)
@Validated
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
@Tag(name = "DocumentCode", description = "Endpoints for document codes")
public class DocumentCodeController {

  private final DocumentCodeService documentCodeService;

  @GetMapping(
      value = "/trade_types/{tradeType}/systems/{system}/document-codes/{code}",
      produces = {"application/json"})
  @Operation(
      summary = "Finds a document code",
      description = "Returns a document code",
      tags = {"DocumentCode"},
      responses = {
        @ApiResponse(
            description = "Success",
            responseCode = "200",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DocumentCode.class))),
        @ApiResponse(
            description = "Bad request",
            responseCode = "400",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            description = "Not found",
            responseCode = "404",
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
  public DocumentCode getDocumentCode(
      @Parameter(description = "Document code", example = "2CVF")
          @Size(min = 4, max = 4)
          @NotNull
          @PathVariable
          String code,
      @Parameter(description = "name of the system", example = "CDS")
          @Size(min = 3, max = 5)
          @NotNull
          @Pattern(regexp = "^(?i)CDS|CHIEF$")
          @PathVariable
          String system,
      @Parameter(description = "type of the trade", example = "IMPORT")
          @Size(min = 6, max = 6)
          @NotNull
          @Pattern(regexp = "^(?i)IMPORT|EXPORT$")
          @PathVariable
          String tradeType) {
    return documentCodeService.getDocumentCode(code, system, tradeType);
  }

  @GetMapping(
      value = "/document-codes",
      produces = {"application/json"})
  @Operation(
      summary = "Get all document codes",
      description = "Get all document codes in CDS and CHIEF",
      tags = {"DocumentCode"},
      responses = {
        @ApiResponse(
            description = "Success",
            responseCode = "200",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DocumentCodes.class))),
        @ApiResponse(
            description = "Bad request",
            responseCode = "400",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            description = "Not found",
            responseCode = "404",
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
  public DocumentCodes getDocumentCodes() {
    return documentCodeService.getDocumentCodes();
  }
}
