@smoke @general @measuretype
Feature: Content API tests for measure types
  As as user I want to run the Content API tests
  So I can determine that the main functionality of the API is working correctly and that most errors are handled correctly
  It is important to keep the same order of the scenarios as there could be side effects otherwise

  Scenario: Verify measure types and their descriptions
    When I send a request to the URL "/api/v1/measure-type-descriptions"
    Then I will receive a 200 response
    And I will receive a valid response which contains all of the measure types and their description overlays

  Scenario Outline: Verify measure types 708 and 420 exists
    When I send a request to the URL "/api/v1/measure-type-descriptions" with params "<measureType>" and "<locale>"
    Then I will receive a 200 response
    And I will receive a valid response which contains specific measure types and their description overlays
    Examples:
      | measureType | locale |
      | 708         | EN     |
      | 420         | CY     |

  Scenario Outline: Verify measure types and their description overlays when querying multiple measure types
    When I send a request to the URL "/api/v1/measure-type-descriptions" with params "<measureTypes>" and "<localeValues>"
    Then I will receive a 200 response
    And I will receive a valid response which contains specific measure types and their description overlays
    Examples:
      | measureTypes | localeValues |
      | 363,465      | EN,CY        |
      | 706,355,CVD  | EN,CY,EN     |

  Scenario Outline: Verify measure types 1000000 does not exit and return an empty response
    When I send a request to the URL "/api/v1/measure-type-descriptions" with params "<measureType>" and "<locale>"
    Then I will receive a 200 response
    And I will receive an empty response for "measureTypeDescriptions"
    Examples:
      | measureType   | locale |
      | 1000000       | EN     |
      | SAMPLEMEASURE | CY     |

  Scenario Outline: Verify more than two measure types where one does not exist
    When I send a request to the URL "/api/v1/measure-type-descriptions" with params "<measureTypes>" and "<localeValues>"
    Then I will receive a 200 response
    And I will receive a valid response which contains specific measure types and their description overlays
    Examples:
      | measureTypes | localeValues |
      | 465,746      | EN,CY        |
      | 719,Y05,722  | EN,CY,EN     |

  Scenario Outline: Verify more than two measure types where they all do not exist
    When I send a request to the URL "/api/v1/measure-type-descriptions" with params "<measureTypes>" and "<localeValues>"
    Then I will receive a 200 response
    And I will receive an empty response for "measureTypeDescriptions"
    Examples:
      | measureTypes | localeValues |
      | Y92,CVS,N85  | EN,EN,CY     |
