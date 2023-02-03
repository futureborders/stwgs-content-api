package uk.gov.cabinetoffice.bpdg.stwgs.cms.measuretypedescription;

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
public class MeasureTypeDescriptionService {
  private final Map<MeasureTypeDescriptionKey, MeasureTypeEntity> measureTypeDescriptionsCache;
  private final Map<String, List<String>> measureTypeLocaleMap;

  public MeasureTypeDescriptions getMeasureTypeDescriptions(
      List<String> measureTypes, Locale locale, TradeType tradeType) {
    final List<MeasureTypeDescription> measureTypeDescriptionList =
        measureTypeDescriptionList(measureTypes, locale, tradeType);

    return MeasureTypeDescriptions.builder()
        .measureTypeDescriptions(measureTypeDescriptionList)
        .build();
  }

  private static Function<String, List<MeasureTypeDescriptionKey>> getMeasureTypeDescriptionKeyList(
      Locale locale) {
    return measureType ->
        locale != null
            ? List.of(new MeasureTypeDescriptionKey(measureType, locale.name()))
            : List.of(
                new MeasureTypeDescriptionKey(measureType, Locale.EN.name()),
                new MeasureTypeDescriptionKey(measureType, Locale.CY.name()));
  }

  private List<MeasureTypeDescription> measureTypeDescriptionList(
      List<String> measureTypes, Locale locale, TradeType tradeType) {

    List<List<MeasureTypeDescription>> listOfMeasureTypeDescriptionsList =
        measureTypes != null
            ? measureTypes.stream()
                .map(getMeasureTypeDescriptionKeyList(locale))
                .flatMap(List::stream)
                .map(measureTypeDescriptionsCache::get)
                .filter(Objects::nonNull)
                .map(this::getMeasureTypeDescriptionList)
                .collect(toList())
            : measureTypeDescriptionsCache.values().stream()
                .filter(d -> locale == null || d.getLocale().equalsIgnoreCase(locale.name()))
                .map(this::getMeasureTypeDescriptionList)
                .collect(toList());

    List<MeasureTypeDescription> measureTypeDescriptions =
        listOfMeasureTypeDescriptionsList.stream().flatMap(List::stream).collect(toList());
    addDefaultENLocaleMeasureTypeDescriptionWhenNoCYLocale(
        measureTypes, locale, tradeType, measureTypeDescriptions);
    return measureTypeDescriptions.stream()
        .filter(d -> tradeType == null || d.getTradeType().equalsIgnoreCase(tradeType.name()))
        .collect(toList());
  }

  private void addDefaultENLocaleMeasureTypeDescriptionWhenNoCYLocale(
      List<String> measureTypes,
      Locale locale,
      TradeType tradeType,
      List<MeasureTypeDescription> measureTypeDescriptions) {
    if (locale != null && locale.equals(Locale.CY)) {
      final List<String> measureTypeCodes =
          measureTypeDescriptions.stream()
              .map(MeasureTypeDescription::getMeasureType)
              .collect(toList());

      if (measureTypes != null) {
        addDefaultENMeasureTypeDescriptionWithMeasureTypesFilter(
            measureTypes, tradeType, measureTypeDescriptions, measureTypeCodes);
      } else {
        addDefaultENMeasureTypeDescriptionDescription(measureTypeDescriptions, measureTypeCodes);
      }
    }
  }

  private void addDefaultENMeasureTypeDescriptionDescription(
      List<MeasureTypeDescription> measureTypeDescriptions, List<String> measureTypeCodes) {
    measureTypeLocaleMap.forEach(
        (key, value) -> {
          if (!measureTypeCodes.contains(key)) {
            final MeasureTypeEntity measureTypeEntity =
                measureTypeDescriptionsCache.get(
                    new MeasureTypeDescriptionKey(key, Locale.EN.name()));
            final List<MeasureTypeDescription> defaultMeasureTypeDescriptions =
                measureTypeEntity != null
                    ? getMeasureTypeDescriptionList(measureTypeEntity)
                    : List.of();
            measureTypeDescriptions.addAll(defaultMeasureTypeDescriptions);
          }
        });
  }

  private void addDefaultENMeasureTypeDescriptionWithMeasureTypesFilter(
      List<String> measureTypes,
      TradeType tradeType,
      List<MeasureTypeDescription> measureTypeDescriptions,
      List<String> measureTypeCodes) {
    final Collection<String> missingMeasureTypesInResult =
        CollectionUtils.subtract(measureTypes, measureTypeCodes);
    final List<MeasureTypeDescription> defaultMeasureTypeDescriptions =
        missingMeasureTypesInResult.stream()
            .map(
                m -> {
                  final MeasureTypeEntity measureTypeEntity =
                      measureTypeDescriptionsCache.get(
                          new MeasureTypeDescriptionKey(m, Locale.EN.name()));
                  return measureTypeEntity != null
                      ? getMeasureTypeDescriptionList(measureTypeEntity)
                      : new ArrayList<MeasureTypeDescription>();
                })
            .flatMap(List::stream)
            .filter(
                measureTypeDescription ->
                    tradeType == null
                        || measureTypeDescription.getTradeType().equalsIgnoreCase(tradeType.name()))
            .collect(toList());
    measureTypeDescriptions.addAll(defaultMeasureTypeDescriptions);
  }

  private List<MeasureTypeDescription> getMeasureTypeDescriptionList(
      MeasureTypeEntity measureTypeEntity) {
    List<MeasureTypeDescription> measureTypeDescriptions = new ArrayList<>();

    boolean isDefaultOverlayOnlyDefined =
        measureTypeEntity.getImportOverlay() == null
            && measureTypeEntity.getExportOverlay() == null
            && measureTypeEntity.getOverlay() != null;

    boolean isExportOverlayOnlyDefined =
        measureTypeEntity.getImportOverlay() == null
            && measureTypeEntity.getExportOverlay() != null
            && measureTypeEntity.getOverlay() == null;

    boolean isImportOverlayOnlyDefined =
        measureTypeEntity.getImportOverlay() != null
            && measureTypeEntity.getExportOverlay() == null
            && measureTypeEntity.getOverlay() == null;

    boolean isImportAndExportOverlayDefined =
        measureTypeEntity.getImportOverlay() != null
            && measureTypeEntity.getExportOverlay() != null
            && measureTypeEntity.getOverlay() == null;

    if (isDefaultOverlayOnlyDefined) {
      addMeasureTypeDescriptionsToTheList(
          measureTypeEntity,
          measureTypeEntity.getOverlay(),
          measureTypeEntity.getOverlay(),
          measureTypeDescriptions);
    } else if (isExportOverlayOnlyDefined
        || isImportOverlayOnlyDefined
        || isImportAndExportOverlayDefined) {
      addMeasureTypeDescriptionsToTheList(
          measureTypeEntity,
          measureTypeEntity.getImportOverlay(),
          measureTypeEntity.getExportOverlay(),
          measureTypeDescriptions);
    }

    return measureTypeDescriptions;
  }

  private void addMeasureTypeDescriptionsToTheList(
      MeasureTypeEntity measureTypeEntity,
      String importOverlay,
      String exportOverlay,
      List<MeasureTypeDescription> measureTypeDescriptions) {

    if (StringUtils.isNotBlank(importOverlay)) {
      measureTypeDescriptions.add(
          MeasureTypeDescription.builder()
              .measureType(measureTypeEntity.getMeasureType())
              .descriptionOverlay(importOverlay)
              .locale(measureTypeEntity.getLocale())
              .tradeType(TradeType.IMPORT.toString())
              .build());
    }
    if (StringUtils.isNotBlank(exportOverlay)) {
      measureTypeDescriptions.add(
          MeasureTypeDescription.builder()
              .measureType(measureTypeEntity.getMeasureType())
              .descriptionOverlay(exportOverlay)
              .locale(measureTypeEntity.getLocale())
              .tradeType(TradeType.EXPORT.toString())
              .build());
    }
  }
}
