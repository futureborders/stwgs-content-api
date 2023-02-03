package uk.gov.cabinetoffice.bpdg.stwgs.cms.measuretypedescription;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeasureTypeDescriptions {
  private List<MeasureTypeDescription> measureTypeDescriptions;
}
