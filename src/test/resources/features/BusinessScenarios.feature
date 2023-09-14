Feature: News Validation

  Scenario Outline: Reading a news article on a news website and validating on search engine
    Given I am on the "<website>" news page
    Then I should be on the "<website_title>" page
    When I click on the first news article
    Then I should see the article content
    Given I open search engine "<search_engine>" and verify page title "<search_engine_title>"
    When I search for "<search_query>"
    Then I should see search results
    And I should find at least two relevant results

    Examples:
      | website         | website_title | search_engine | search_engine_title |
      | TheGuardianNews | GuardianTitle | Google        | GoogleTitle         |

