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
@JsonIgnoreProperties(ignoreUnknown = true)
@Tag(name = "DocumentCodes", description = "DocumentCodes")
@NoArgsConstructor
@AllArgsConstructor
public class DocumentCodes {
  @JsonProperty("documentCodes")
  private List<DocumentCode> documentCodeList;
}
