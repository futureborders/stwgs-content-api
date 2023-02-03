package uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.helper;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.StatusCode;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.StatusCodes;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.collection.DataCollection;

@Service
@Slf4j
public class StatusCodeServiceHelper {
  private final DataCollection statusCodeCollection;
  private final YAMLMapper yamlMapper;

  public StatusCodeServiceHelper(
      @Qualifier("statusCodeCollection") DataCollection statusCodeCollection,
      YAMLMapper yamlMapper) {
    this.statusCodeCollection = statusCodeCollection;
    this.yamlMapper = yamlMapper;
  }

  public StatusCodes getStatusCodes() {
    final File statusCodeDescriptionsDir = statusCodeCollection.getCollectionSource().getFile();
    final List<File> statusCodeFilesList =
        Arrays.asList(Objects.requireNonNull(statusCodeDescriptionsDir.listFiles()));
    final List<StatusCode> statusCodes =
        statusCodeFilesList.stream()
            .map(this::convertFileToStatusCode)
            .collect(Collectors.toList());
    return StatusCodes.builder().statusCodeList(statusCodes).build();
  }

  @SneakyThrows
  private StatusCode convertFileToStatusCode(final File file) {
    return yamlMapper.readValue(file, StatusCode.class);
  }
}
