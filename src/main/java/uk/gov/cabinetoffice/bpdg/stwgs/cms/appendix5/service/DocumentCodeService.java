package uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.service;

import static uk.gov.cabinetoffice.bpdg.stwgs.cms.cache.CacheConstants.DOCUMENT_CODES_CACHE;
import static uk.gov.cabinetoffice.bpdg.stwgs.cms.cache.CacheConstants.DOCUMENT_CODES_KEY;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.File;
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
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.DocumentCode;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.DocumentCodes;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.cache.DataCollectionsCache;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.collection.DataCollection;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.exception.ResourceNotFoundException;

@Service
@Tag(name = "document-codes")
@Slf4j
@EnableCaching
public class DocumentCodeService {

  private final DataCollection documentCodeCollection;
  private final DataCollectionsCache<DocumentCodes> documentCodeCache;
  private final YAMLMapper yamlMapper;

  public DocumentCodeService(
      @Qualifier("documentCodeCollection") DataCollection documentCodeCollection,
      YAMLMapper yamlMapper,
      DataCollectionsCache<DocumentCodes> documentCodeCache) {
    this.documentCodeCollection = documentCodeCollection;
    this.yamlMapper = yamlMapper;
    this.documentCodeCache = documentCodeCache;
  }

  @SneakyThrows
  public DocumentCode getDocumentCode(
      final String documentCode, final String system, final String tradeType) {
    final List<File> documentCodeFiles = readDocumentCodes();

    var documentCodes = transformFilesIntoMonoOfDocumentCodes(documentCodeFiles);

    var optionalDocumentCode =
        filterDocumentCodesBySystemAndTradeTypeAndCode(
            system, tradeType, documentCode, documentCodes.getDocumentCodeList());

    return optionalDocumentCode.orElseThrow(
        () -> new ResourceNotFoundException("DocumentCode", documentCode, system, tradeType));
  }

  @SneakyThrows
  public DocumentCodes getDocumentCodes() {
    final List<File> documentCodesFiles = readDocumentCodes();
    var cachedDocumentCodesCache =
        documentCodeCache.lookUpCollectionDataFromCache(DOCUMENT_CODES_KEY, DOCUMENT_CODES_CACHE);
    return cachedDocumentCodesCache != null
        ? cachedDocumentCodesCache
        : transformFilesIntoMonoOfDocumentCodes(documentCodesFiles);
  }

  private DocumentCodes transformFilesIntoMonoOfDocumentCodes(
      List<File> documentCodeDeclarationsByDocumentCode) {
    final List<DocumentCode> documentCodes =
        documentCodeDeclarationsByDocumentCode.stream()
            .map(this::documentCode)
            .collect(Collectors.toList());
    return DocumentCodes.builder().documentCodeList(documentCodes).build();
  }

  private List<File> readDocumentCodes() {
    final File documentCodesDir = documentCodeCollection.getCollectionSource().getFile();
    return Arrays.asList(Objects.requireNonNull(documentCodesDir.listFiles()));
  }

  @SneakyThrows
  private DocumentCode documentCode(final File file) {
    return yamlMapper.readValue(file, DocumentCode.class);
  }

  private Optional<DocumentCode> filterDocumentCodesBySystemAndTradeTypeAndCode(
      String system, String tradeType, String code, List<DocumentCode> documentCodes) {
    return documentCodes.stream()
        .filter(d -> d.getTradeType().equalsIgnoreCase(tradeType))
        .filter(d -> d.getSystem().equalsIgnoreCase(system))
        .filter(d -> d.getCode().equalsIgnoreCase(code))
        .findAny();
  }
}
