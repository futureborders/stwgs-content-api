package uk.gov.cabinetoffice.bpdg.stwgs.cms.collection;

import lombok.Builder;
import lombok.Getter;
import org.springframework.core.io.FileSystemResource;

@Getter
@Builder
public class DataCollection {
  private FileSystemResource collectionSource;
}
