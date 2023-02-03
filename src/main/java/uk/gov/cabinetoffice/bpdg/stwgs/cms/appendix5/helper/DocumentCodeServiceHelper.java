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
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.DocumentCode;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.appendix5.model.DocumentCodes;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.collection.DataCollection;

@Service
@Slf4j
public class DocumentCodeServiceHelper {
  private final DataCollection documentCodeCollection;
  private final YAMLMapper yamlMapper;

  public DocumentCodeServiceHelper(
      @Qualifier("documentCodeCollection") DataCollection documentCodeCollection,
      YAMLMapper yamlMapper) {
    this.documentCodeCollection = documentCodeCollection;
    this.yamlMapper = yamlMapper;
  }

  public DocumentCodes getDocumentCodes() {
    final File documentCodesDir = documentCodeCollection.getCollectionSource().getFile();
    final List<File> documentCodeDeclarationsByDocumentCode =
        Arrays.asList(Objects.requireNonNull(documentCodesDir.listFiles()));
    final List<DocumentCode> documentCodes =
        documentCodeDeclarationsByDocumentCode.stream()
            .map(this::convertFileToDocumentCode)
            .collect(Collectors.toList());
    return DocumentCodes.builder().documentCodeList(documentCodes).build();
  }

  @SneakyThrows
  private DocumentCode convertFileToDocumentCode(final File file) {
    return yamlMapper.readValue(file, DocumentCode.class);
  }
}
