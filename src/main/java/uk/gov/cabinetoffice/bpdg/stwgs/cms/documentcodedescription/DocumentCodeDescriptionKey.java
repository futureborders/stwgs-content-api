package uk.gov.cabinetoffice.bpdg.stwgs.cms.documentcodedescription;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DocumentCodeDescriptionKey {
  String documentCode;
  String locale;
}
