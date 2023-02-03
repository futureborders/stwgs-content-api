package uk.gov.cabinetoffice.bpdg.stwgs.cms.functionaltests.steps;

import static uk.gov.cabinetoffice.bpdg.stwgs.cms.util.TestDataUtils.gson;

import com.google.gson.JsonElement;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.functionaltests.TestConfig;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.util.TestDataUtils;

@CucumberContextConfiguration
@SpringBootTest(
    classes = TestConfig.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class GenericSteps {
  @LocalServerPort private int port;

  @Autowired private RestTemplate restTemplate;

  private ResponseEntity<String> response;

  public String getBaseUrl() {
    String baseurl = System.getProperty("baseurl");
    if (baseurl.contains("http://localhost")) {
      baseurl = StringUtils.replace(baseurl, "8080", String.valueOf(port));
    }
    return baseurl;
  }

  public String getUrlEndpoint(String reqPath) {
    return getBaseUrl() + reqPath;
  }

  @Given("I send a request to the URL {string}")
  public void iSendARequest(String reqPath) {
    String url = getUrlEndpoint(reqPath);
    response = restTemplate.getForEntity(url, String.class);
  }

  @Given("I send a request to the URL {string} with params {string} and {string}")
  public void iSendARequestWithParams(String reqPath, String param1, String param2) {
    String url;
    String[] locale = param2.split(",");
    if (reqPath.contains("document-code")) {
      if (param2.contains(",")) {
        url = getUrlEndpoint(reqPath) + "?documentCodes=" + param1 + "&locale=" + locale[0];
      } else {
        url = getUrlEndpoint(reqPath) + "?documentCodes=" + param1 + "&locale=" + param2;
      }
    } else {
      if (param2.contains(",")) {
        url = getUrlEndpoint(reqPath) + "?measureTypes=" + param1 + "&locale=" + locale[0];
      } else {
        url = getUrlEndpoint(reqPath) + "?measureTypes=" + param1 + "&locale=" + param2;
      }
    }
    log.info("url: {}", url);
    response = restTemplate.getForEntity(url, String.class);
  }

  @Then("^I will receive a (\\d{3}) response$")
  public void checkResponse(int statusCode) {
    log.info("response: {}", response.getStatusCodeValue());
    Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(statusCode);
  }

  @And(
      "^I will receive a valid response which contains all of the document codes and descriptions$")
  public void checkResponseContainsAllDocumentCodesAndDescriptions() throws Exception {
    JSONArray actualJsonArray = TestDataUtils.getActualDocumentCodesJsonData(response);
    // read expected data from json file and sort based on document code
    JSONArray expectedJsonArray = TestDataUtils.getExpectedDocumentCodesJsonData();
    // compare actual and expected data
    for (int i = 0; i < actualJsonArray.length(); i++) {
      String actualResult = actualJsonArray.getJSONObject(i).toString().trim();
      String expectedResult = expectedJsonArray.getJSONObject(i).toString().trim();
      Assertions.assertThat(actualResult).isEqualTo(expectedResult);
    }
  }

  @And(
      "^I will receive a valid response which contains specific document codes and their descriptions$")
  public void checkResponseContainsSpecificDocumentCodeAndDescription() throws Exception {
    JSONArray actualJsonArray = TestDataUtils.getActualDocumentCodesJsonData(response);
    for (int i = 0; i < actualJsonArray.length(); i++) {
      JSONObject actualJSONResultObj = actualJsonArray.getJSONObject(i);
      String actualDocumentCode = actualJSONResultObj.getString("documentCode");
      String actualLocale = actualJSONResultObj.getString("locale");
      String actualTradeType = actualJSONResultObj.getString("tradeType");
      JSONArray expectedJsonArray = TestDataUtils.getExpectedDocumentCodesJsonData();
      for (int j = 0; j < expectedJsonArray.length(); j++) {
        JSONObject expectedJSONResultObj = expectedJsonArray.getJSONObject(j);
        String expectedDocumentCode = expectedJSONResultObj.getString("documentCode");
        String expectedLocale = expectedJSONResultObj.getString("locale");
        String expectedTradeType = expectedJSONResultObj.getString("tradeType");
        if (expectedDocumentCode.equalsIgnoreCase(actualDocumentCode)
            && expectedLocale.equalsIgnoreCase(actualLocale)
            && expectedTradeType.equalsIgnoreCase(actualTradeType)) {
          log.info("documentCodeActualResult: {}", actualJSONResultObj);
          log.info("documentCodeExpectedResult: {}", expectedJSONResultObj);
          Assertions.assertThat(actualJSONResultObj.toString().trim())
              .isEqualTo(expectedJSONResultObj.toString().trim());
        }
      }
    }
  }

  @And("I will receive an empty response for {string}")
  public void responseShouldNotContainDocumentCodeAndDescription(String descriptions) {
    JsonElement actualJsonElement = gson.toJsonTree(response);
    Assertions.assertThat(actualJsonElement.getAsJsonObject().get(descriptions)).isNull();
  }

  @And(
      "^I will receive a valid response which contains all of the measure types and their description overlays$")
  public void checkResponseContainsAllMeasureTypesAndDescriptions() throws Exception {
    JSONArray actualJsonArray = TestDataUtils.getActualMeasureTypesJsonData(response);
    log.info("measureTypesActualSortedJSONArray: {}", actualJsonArray);
    // read expected data from json file and sort based on document code
    JSONArray expectedJsonArray = TestDataUtils.getExpectedMeasureTypesJsonData();
    log.info("measureTypesExpectedSortedJSONArray: {}", expectedJsonArray);
    // compare actual and expected data
    for (int i = 0; i < actualJsonArray.length(); i++) {
      String actualResult = actualJsonArray.getJSONObject(i).toString().trim();
      String expectedResult = expectedJsonArray.getJSONObject(i).toString().trim();
      Assertions.assertThat(actualResult).isEqualTo(expectedResult);
    }
  }

  @And(
      "^I will receive a valid response which contains specific measure types and their description overlays$")
  public void checkResponseContainsSpecificMeasureTypesAndDescription() throws Exception {
    JSONArray actualJsonArray = TestDataUtils.getActualMeasureTypesJsonData(response);
    for (int i = 0; i < actualJsonArray.length(); i++) {
      JSONObject actualJSONResultObj = actualJsonArray.getJSONObject(i);
      String actualMeasureType = actualJSONResultObj.getString("measureType");
      String actualLocale = actualJSONResultObj.getString("locale");
      String actualTradeType = actualJSONResultObj.getString("tradeType");
      JSONArray expectedJsonArray = TestDataUtils.getExpectedMeasureTypesJsonData();
      for (int j = 0; j < expectedJsonArray.length(); j++) {
        JSONObject expectedJSONResultObj = expectedJsonArray.getJSONObject(j);
        String expectedMeasureType = expectedJSONResultObj.getString("measureType");
        String expectedLocale = expectedJSONResultObj.getString("locale");
        String expectedTradeType = expectedJSONResultObj.getString("tradeType");
        if (expectedMeasureType.equalsIgnoreCase(actualMeasureType)
            && expectedLocale.equalsIgnoreCase(actualLocale)
            && expectedTradeType.equalsIgnoreCase(actualTradeType)) {
          log.info("measureTypeActualResult: {}", actualJSONResultObj);
          log.info("measureTypeExpectedResult: {}", expectedJSONResultObj);
          Assertions.assertThat(actualJSONResultObj.toString().trim())
              .isEqualTo(expectedJSONResultObj.toString().trim());
        }
      }
    }
  }
}
