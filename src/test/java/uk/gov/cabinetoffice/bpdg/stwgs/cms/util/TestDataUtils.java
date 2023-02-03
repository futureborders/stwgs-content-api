package uk.gov.cabinetoffice.bpdg.stwgs.cms.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.json.JSONArray;
import org.springframework.http.ResponseEntity;

public class TestDataUtils {

  public static Gson gson = new Gson();

  public static JSONArray sortJsonData(String respList, String keyDescriptions, String keyName)
      throws Exception {
    JsonElement jsonStr = getJsonElement(respList, keyDescriptions);
    JSONArray jsonArray = new JSONArray(jsonStr.toString());
    JSONArray sortedJsonArray = new JSONArray();
    List<Object> list = new ArrayList<>();
    for (int i = 0; i < jsonArray.length(); i++) {
      list.add(jsonArray.getJSONObject(i));
    }
    list.sort(
        new Comparator() {
          private final String KEY_NAME = keyName;

          @Override
          public int compare(Object a, Object b) {
            String str1;
            String str2;
            JsonElement jsonElement1 = gson.toJsonTree(a).getAsJsonObject().get("nameValuePairs");
            str1 = jsonElement1.getAsJsonObject().get(KEY_NAME).toString();
            JsonElement jsonElement2 = gson.toJsonTree(b).getAsJsonObject().get("nameValuePairs");
            str2 = jsonElement2.getAsJsonObject().get(KEY_NAME).toString();
            return str1.compareTo(str2);
          }
        });
    for (int i = 0; i < jsonArray.length(); i++) {
      sortedJsonArray.put(list.get(i));
    }
    return sortedJsonArray;
  }

  public static JsonElement getJsonElement(String respList, String key) {
    Map attributes = gson.fromJson(respList, Map.class);
    Object docList = attributes.get(key);
    return gson.toJsonTree(docList);
  }

  public static JSONArray getExpectedDocumentCodesJsonData() throws Exception {
    FileReader reader =
        new FileReader(
            TestUtils.getClassPathResource("expected_content/document_codes_descriptions.json"));
    JsonElement expectedJsonContent = JsonParser.parseReader(reader);
    return sortJsonData(expectedJsonContent.toString(), "documentCodeDescriptions", "documentCode");
  }

  public static JSONArray getActualDocumentCodesJsonData(ResponseEntity<String> response)
      throws Exception {
    String respList = List.of(Objects.requireNonNull(response.getBody())).get(0);
    return TestDataUtils.sortJsonData(respList, "documentCodeDescriptions", "documentCode");
  }

  public static JSONArray getExpectedMeasureTypesJsonData() throws Exception {
    FileReader reader =
        new FileReader(
            TestUtils.getClassPathResource("expected_content/measure_types_descriptions.json"));
    JsonElement expectedJsonContent = JsonParser.parseReader(reader);
    return TestDataUtils.sortJsonData(
        expectedJsonContent.toString(), "measureTypeDescriptions", "measureType");
  }

  public static JSONArray getActualMeasureTypesJsonData(ResponseEntity<String> response)
      throws Exception {
    String respList = List.of(Objects.requireNonNull(response.getBody())).get(0);
    return TestDataUtils.sortJsonData(respList, "measureTypeDescriptions", "measureType");
  }
}
