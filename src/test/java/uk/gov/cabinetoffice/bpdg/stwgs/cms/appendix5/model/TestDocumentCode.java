package uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestDocumentCode {
  private String code;
  private String tradeType;
  private String description;
  private String detailsOnTheDeclaration;
  private String system;

  private List<String> statusCodes;
}
