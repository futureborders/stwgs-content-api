package uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Tag(name = "StatusCodes", description = "StatusCodes")
public class StatusCodes {
  @JsonProperty("statusCodes")
  private List<StatusCode> statusCodeList;
}
