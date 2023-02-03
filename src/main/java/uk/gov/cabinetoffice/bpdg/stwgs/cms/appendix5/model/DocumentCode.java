package uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Tag(name = "DocumentCode", description = "DocumentCode")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DocumentCode {
  @Setter private String code;
  @Setter private String tradeType;
  @Setter private String description;
  @Setter private String detailsOnTheDeclaration;
  @Setter private String system;
  private List<String> statusCodes;

  public void setStatusCodes(List<String> statusCodes) {
    if (statusCodes == null) {
      this.statusCodes = Collections.emptyList();
    } else {
      this.statusCodes = statusCodes;
    }
  }
}
