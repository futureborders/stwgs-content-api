package uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.StatusCode;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.StatusCodes;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.service.StatusCodeService;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.config.CmsCoreApiVersion;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.model.ErrorResponse;

@RestController
@RequestMapping(value = CmsCoreApiVersion.APPENDIX5_API_V1_VERSION)
@Validated
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
@Tag(name = "StatusCode", description = "Endpoints for status codes")
public class StatusCodeController {

  private final StatusCodeService statusCodeService;

  @GetMapping(
      value = "/systems/{system}/status-codes/{code}",
      produces = {"application/json"})
  @Operation(
      summary = "Finds a status code",
      description = "Returns a status code",
      tags = {"StatusCode"},
      responses = {
        @ApiResponse(
            description = "Success",
            responseCode = "200",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StatusCode.class))),
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
  public StatusCode getStatusCode(
      @Parameter(description = "name of the system", example = "CDS")
          @NotBlank
          @Pattern(regexp = "^(?i)CDS|CHIEF$")
          @PathVariable
          String system,
      @Parameter(description = "Status code", example = "AC")
          @NotBlank
          @Pattern(regexp = "^\\w{2}$")
          @PathVariable
          String code) {
    return statusCodeService.getStatusCode(system, code);
  }

  @GetMapping(
      value = "/status-codes",
      produces = {"application/json"})
  @Operation(
      summary = "Get all status codes",
      description = "Get all status codes in CDS and CHIEF",
      tags = {"StatusCode"},
      responses = {
        @ApiResponse(
            description = "Success",
            responseCode = "200",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StatusCodes.class))),
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
  public StatusCodes getStatusCodeDescriptions() {
    return statusCodeService.getStatusCodes();
  }
}
