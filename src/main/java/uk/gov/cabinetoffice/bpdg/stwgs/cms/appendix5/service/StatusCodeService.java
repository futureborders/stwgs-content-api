package uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.service;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.StatusCode;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.StatusCodes;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.cache.DataCollectionsCache;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.collection.DataCollection;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.exception.ResourceNotFoundException;

@Service
@Slf4j
@EnableCaching
public class StatusCodeService {

  private final DataCollection statusCodeCollection;
  private final YAMLMapper yamlMapper;
  private final DataCollectionsCache<StatusCodes> statusCodeCache;

  public StatusCodeService(
      @Qualifier("statusCodeCollection") DataCollection statusCodeCollection,
      YAMLMapper yamlMapper,
      DataCollectionsCache<StatusCodes> statusCodeCache) {
    this.statusCodeCollection = statusCodeCollection;
    this.yamlMapper = yamlMapper;
    this.statusCodeCache = statusCodeCache;
  }

  @SneakyThrows
  public StatusCode getStatusCode(final String system, final String statusCode) {
    log.debug(
        "Status code from the request: {} ", URLEncoder.encode(statusCode, StandardCharsets.UTF_8));
    final List<File> statusCodeFiles = readStatusCodes();
    var statusCodes = transformFilesIntoMonoOfStatusCodes(statusCodeFiles);

    var optionalStatusCode =
        filterStatusCodesBySystemAndCode(statusCodes.getStatusCodeList(), system, statusCode);
    return optionalStatusCode.orElseThrow(
        () -> new ResourceNotFoundException("StatusCode", statusCode, system));
  }

  @SneakyThrows
  public StatusCodes getStatusCodes() {
    // get all files in the folder
    final List<File> statusCodeFiles = readStatusCodes();
    return transformFilesIntoMonoOfStatusCodes(statusCodeFiles);
  }

  private StatusCodes transformFilesIntoMonoOfStatusCodes(List<File> statusCodeDeclarations) {
    final List<StatusCode> statusCodes =
        statusCodeDeclarations.stream().map(this::statusCode).collect(Collectors.toList());
    return StatusCodes.builder().statusCodeList(statusCodes).build();
  }

  private Optional<StatusCode> filterStatusCodesBySystemAndCode(
      List<StatusCode> statusCodes, String system, String code) {
    return statusCodes.stream()
        .filter(d -> d.getSystem().equalsIgnoreCase(system))
        .filter(d -> d.getCode().equalsIgnoreCase(code))
        .findAny();
  }

  private List<File> readStatusCodes() {
    final File statusCodeDescriptionsDir = statusCodeCollection.getCollectionSource().getFile();
    return Arrays.asList(Objects.requireNonNull(statusCodeDescriptionsDir.listFiles()));
  }

  @SneakyThrows
  private StatusCode statusCode(final File file) {
    return yamlMapper.readValue(file, StatusCode.class);
  }
}
