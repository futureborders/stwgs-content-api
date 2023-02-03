package uk.gov.cabinetoffice.bpdg.stwgs.cms.documentcodedescription;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.enums.Locale;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.enums.TradeType;

@Service
@Slf4j
@AllArgsConstructor
public class DocumentCodeDescriptionService {
  private final Map<DocumentCodeDescriptionKey, DocumentCodeEntity> documentCodeDescriptionsCache;
  private final Map<String, List<String>> documentCodeLocaleMap;

  public DocumentCodeDescriptions getDocumentCodeDescriptions(
      List<String> documentCodes, Locale locale, TradeType tradeType) {

    final List<DocumentCodeDescription> documentCodeDescriptionListResult =
        documentCodeDescriptions(documentCodes, locale, tradeType);

    return DocumentCodeDescriptions.builder()
        .documentCodeDescriptions(documentCodeDescriptionListResult)
        .build();
  }

  private static Function<String, List<DocumentCodeDescriptionKey>>
      getDocumentCodeDescriptionKeyList(Locale locale) {
    return documentCode ->
        locale != null
            ? List.of(new DocumentCodeDescriptionKey(documentCode, locale.name()))
            : List.of(
                new DocumentCodeDescriptionKey(documentCode, Locale.EN.name()),
                new DocumentCodeDescriptionKey(documentCode, Locale.CY.name()));
  }

  private List<DocumentCodeDescription> documentCodeDescriptions(
      List<String> documentCodes, Locale locale, TradeType tradeType) {
    List<List<DocumentCodeDescription>> listOfDocumentCodeDescriptionList =
        documentCodes != null
            ? documentCodes.stream()
                .map(getDocumentCodeDescriptionKeyList(locale))
                .flatMap(List::stream)
                .map(documentCodeDescriptionsCache::get)
                .filter(Objects::nonNull)
                .map(this::getDocumentCodeDescriptionList)
                .collect(toList())
            : documentCodeDescriptionsCache.values().stream()
                .filter(d -> locale == null || d.getLocale().equalsIgnoreCase(locale.name()))
                .map(this::getDocumentCodeDescriptionList)
                .collect(toList());

    final List<DocumentCodeDescription> documentCodeDescriptions =
        listOfDocumentCodeDescriptionList.stream()
            .flatMap(List::stream)
            .filter(
                documentCodeDescription ->
                    tradeType == null
                        || documentCodeDescription
                            .getTradeType()
                            .equalsIgnoreCase(tradeType.name()))
            .collect(toList());

    addDefaultENLocaleDocumentCodeDescriptionWhenNoCYLocale(
        documentCodes, locale, tradeType, documentCodeDescriptions);
    return documentCodeDescriptions.stream()
        .filter(d -> tradeType == null || d.getTradeType().equalsIgnoreCase(tradeType.name()))
        .collect(toList());
  }

  private void addDefaultENLocaleDocumentCodeDescriptionWhenNoCYLocale(
      List<String> documentCodes,
      Locale locale,
      TradeType tradeType,
      List<DocumentCodeDescription> documentCodeDescriptions) {
    if (locale != null && locale.equals(Locale.CY)) {
      final List<String> documentCodesBeforeFilter =
          documentCodeDescriptions.stream()
              .map(DocumentCodeDescription::getDocumentCode)
              .collect(toList());

      if (documentCodes != null) {
        addDefaultENDocumentCodeDescriptionWithDocumentCodesFilter(
            documentCodes, tradeType, documentCodeDescriptions, documentCodesBeforeFilter);
      } else {
        addDefaultENDocumentCodeDescription(documentCodeDescriptions, documentCodesBeforeFilter);
      }
    }
  }

  private void addDefaultENDocumentCodeDescription(
      List<DocumentCodeDescription> documentCodeDescriptions,
      List<String> documentCodesBeforeFilter) {
    documentCodeLocaleMap.forEach(
        (key, value) -> {
          if (!documentCodesBeforeFilter.contains(key)) {
            final DocumentCodeEntity documentCodeEntity =
                documentCodeDescriptionsCache.get(
                    new DocumentCodeDescriptionKey(key, Locale.EN.name()));
            final List<DocumentCodeDescription> documentCodeDescriptionList =
                documentCodeEntity != null
                    ? getDocumentCodeDescriptionList(documentCodeEntity)
                    : List.of();
            documentCodeDescriptions.addAll(documentCodeDescriptionList);
          }
        });
  }

  private void addDefaultENDocumentCodeDescriptionWithDocumentCodesFilter(
      List<String> documentCodes,
      TradeType tradeType,
      List<DocumentCodeDescription> documentCodeDescriptions,
      List<String> documentCodesBeforeFilter) {
    final Collection<String> missingDocumentCodesInResult =
        CollectionUtils.subtract(documentCodes, documentCodesBeforeFilter);
    final List<DocumentCodeDescription> defaultDocumentCodeDescriptions =
        missingDocumentCodesInResult.stream()
            .map(
                d -> {
                  final DocumentCodeEntity documentCodeEntity =
                      documentCodeDescriptionsCache.get(
                          new DocumentCodeDescriptionKey(d, Locale.EN.name()));
                  return documentCodeEntity != null
                      ? getDocumentCodeDescriptionList(documentCodeEntity)
                      : new ArrayList<DocumentCodeDescription>();
                })
            .flatMap(List::stream)
            .filter(
                documentCodeDescription ->
                    tradeType == null
                        || documentCodeDescription
                            .getTradeType()
                            .equalsIgnoreCase(tradeType.name()))
            .collect(toList());
    documentCodeDescriptions.addAll(defaultDocumentCodeDescriptions);
  }

  private List<DocumentCodeDescription> getDocumentCodeDescriptionList(
      DocumentCodeEntity documentCodeEntity) {
    List<DocumentCodeDescription> documentCodeDescriptions = new ArrayList<>();

    boolean isDefaultOverlayOnlyDefined =
        documentCodeEntity.getImportOverlay() == null
            && documentCodeEntity.getExportOverlay() == null
            && documentCodeEntity.getOverlay() != null;

    boolean isExportOverlayOnlyDefined =
        documentCodeEntity.getImportOverlay() == null
            && documentCodeEntity.getExportOverlay() != null
            && documentCodeEntity.getOverlay() == null;

    boolean isImportOverlayOnlyDefined =
        documentCodeEntity.getImportOverlay() != null
            && documentCodeEntity.getExportOverlay() == null
            && documentCodeEntity.getOverlay() == null;

    boolean isImportAndExportOverlayDefined =
        documentCodeEntity.getImportOverlay() != null
            && documentCodeEntity.getExportOverlay() != null
            && documentCodeEntity.getOverlay() == null;

    if (isDefaultOverlayOnlyDefined) {
      addDocumentCodeDescriptionToTheList(
          documentCodeEntity,
          documentCodeEntity.getOverlay(),
          documentCodeEntity.getOverlay(),
          documentCodeDescriptions);
    } else if (isExportOverlayOnlyDefined
        || isImportOverlayOnlyDefined
        || isImportAndExportOverlayDefined) {
      addDocumentCodeDescriptionToTheList(
          documentCodeEntity,
          documentCodeEntity.getImportOverlay(),
          documentCodeEntity.getExportOverlay(),
          documentCodeDescriptions);
    }
    return documentCodeDescriptions;
  }

  private void addDocumentCodeDescriptionToTheList(
      DocumentCodeEntity documentCodeEntity,
      String importOverlay,
      String exportOverlay,
      List<DocumentCodeDescription> documentCodeDescriptions) {
    if (StringUtils.isNotBlank(importOverlay)) {
      documentCodeDescriptions.add(
          DocumentCodeDescription.builder()
              .documentCode(documentCodeEntity.getDocumentCode())
              .descriptionOverlay(importOverlay)
              .locale(documentCodeEntity.getLocale())
              .tradeType(TradeType.IMPORT.toString())
              .build());
    }
    if (StringUtils.isNotBlank(exportOverlay)) {
      documentCodeDescriptions.add(
          DocumentCodeDescription.builder()
              .documentCode(documentCodeEntity.getDocumentCode())
              .descriptionOverlay(exportOverlay)
              .locale(documentCodeEntity.getLocale())
              .tradeType(TradeType.EXPORT.toString())
              .build());
    }
  }
}
