package uk.gov.cabinetoffice.bpdg.stwgs.cms.config;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.collection.DataCollection;

@Configuration
public class BeansConfig {

  @Bean
  public ObjectMapper objectMapper() {
    var objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return objectMapper;
  }

  @Bean
  public YAMLMapper yamlMapper() {
    var yamlMapper = new YAMLMapper();
    yamlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    yamlMapper.setSerializationInclusion(Include.NON_NULL);
    return yamlMapper;
  }

  @Bean("documentCodeDescriptionCollection")
  public DataCollection documentCodeDescriptionCollection(
      @Value("${stwgs.cms.collections.path.document-code-descriptions}")
          String documentCodeDescriptionsCollectionPath) {
    return DataCollection.builder()
        .collectionSource(new FileSystemResource(documentCodeDescriptionsCollectionPath))
        .build();
  }

  @Bean("measureTypeDescriptionCollection")
  public DataCollection measureTypeDescriptionCollection(
      @Value("${stwgs.cms.collections.path.measure-type-descriptions}")
          String measureTypeDescriptionsCollectionPath) {
    return DataCollection.builder()
        .collectionSource(new FileSystemResource(measureTypeDescriptionsCollectionPath))
        .build();
  }
}
