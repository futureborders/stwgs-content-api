@smoke @general @documentcode
Feature: Content API tests for document codes
  As as user I want to run the Content API tests
  So I can determine that the main functionality of the API is working correctly and that most errors are handled correctly
  It is important to keep the same order of the scenarios as there could be side effects otherwise

  Scenario: Verify document codes and their descriptions
    When I send a request to the URL "/api/v1/document-code-descriptions"
    Then I will receive a 200 response
    And I will receive a valid response which contains all of the document codes and descriptions

  Scenario Outline: Verify document codes C644 and 9111 exists
    When I send a request to the URL "/api/v1/document-code-descriptions" with params "<documentCode>" and "<locale>"
    Then I will receive a 200 response
    And I will receive a valid response which contains specific document codes and their descriptions
    Examples:
      | documentCode | locale |
      | C644         | EN     |
      | 9111         | CY     |

  Scenario Outline: Verify document codes and their descriptions when querying multiple document codes
    When I send a request to the URL "/api/v1/document-code-descriptions" with params "<documentCodes>" and "<localeValues>"
    Then I will receive a 200 response
    And I will receive a valid response which contains specific document codes and their descriptions
    Examples:
      | documentCodes  | localeValues |
      | Y928,C400      | EN,CY        |
      | C050,Y058,N853 | EN,CY,EN     |

  Scenario Outline: Verify document code 100000 does not exit and return an empty response
    When I send a request to the URL "/api/v1/document-code-descriptions" with params "<documentCode>" and "<locale>"
    Then I will receive a 200 response
    And I will receive an empty response for "documentCodeDescriptions"
    Examples:
      | documentCode | locale |
      | 100000       | EN     |
      | N5000000     | CY     |

  Scenario Outline: Verify more than two document codes where one does not exist
    When I send a request to the URL "/api/v1/document-code-descriptions" with params "<documentCodes>" and "<localeValues>"
    Then I will receive a 200 response
    And I will receive a valid response which contains specific document codes and their descriptions
    Examples:
      | documentCodes    | localeValues |
      | Y928,C400        | EN,CY        |
      | C001,Y05800,Y945 | EN,CY,EN     |

  Scenario Outline: Verify more than two document codes where they all do not exist
    When I send a request to the URL "/api/v1/document-code-descriptions" with params "<documentCodes>" and "<localeValues>"
    Then I will receive a 200 response
    And I will receive an empty response for "documentCodeDescriptions"
    Examples:
      | documentCodes          | localeValues |
      | Y92800,C40000,N8500009 | EN,EN,CY     |
