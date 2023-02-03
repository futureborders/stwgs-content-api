package uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.collection.DataCollection;

@Configuration
public class CollectionConfig {

  @Bean("documentCodeCollection")
  public DataCollection documentCodeCollection(
      @Value("${stwgs.cms.collections.path.5a-document-codes}")
          String documentCodesCollectionPath) {
    return DataCollection.builder()
        .collectionSource(new FileSystemResource(documentCodesCollectionPath))
        .build();
  }

  @Bean("statusCodeCollection")
  public DataCollection statusCodeCollection(
      @Value("${stwgs.cms.collections.path.5b-status-codes}") String statusCodesCollectionPath) {
    return DataCollection.builder()
        .collectionSource(new FileSystemResource(statusCodesCollectionPath))
        .build();
  }
}
