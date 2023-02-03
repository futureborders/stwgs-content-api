package uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class TestDocumentCodes {
  @JsonProperty("documentCodes")
  private List<TestDocumentCode> documentCodeList;
}
