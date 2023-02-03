package uk.gov.cabinetoffice.bpdg.stwgs.cms.documentcodedescription;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentCodeDescriptions {
  private List<DocumentCodeDescription> documentCodeDescriptions;
}
