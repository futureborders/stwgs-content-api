package uk.gov.cabinetoffice.bpdg.stwgs.cms.measuretypedescription;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class MeasureTypeDescriptionKey {
  String measureType;
  String locale;
}
