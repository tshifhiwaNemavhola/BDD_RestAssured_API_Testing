Feature: Star Wars App Functionality

  Scenario: Sort by Title and Verify Last Movie
    Given I am on the Star Wars App home page
    When I sort the movies by "Title" (ascending)
    Then the last movie displayed should be "The Phantom Menace"

  Scenario: View Movie Details and Check Species
    Given I am on the Star Wars App home page
    When I select the movie "The Empire Strikes Back"
    Then the movie details should be displayed
    And the "Species" list should contain "Wookie"

  Scenario: Verify Planet Not Listed in Movie
    Given I am on the Star Wars App home page
    When I select the movie "The Phantom Menace"
    Then the movie details should be displayed
    And the "Planets" list should not contain "Camino"

  Scenario Outline: Verify Movie Count via API
    Given I send a GET request to "/api/movies"
    When I receive the response
    Then the response status code should be 200
    And the number of movies returned should be <expected_count>

    Examples:
      | expected_count |
      | 6              |

  Scenario Outline: Verify Movie Details via API
    Given I send a GET request to "/api/movies/<movie_id>"
    When I receive the response
    Then the response status code should be 200
    And the movie "<movie_title>" details are returned

    Examples:
      | movie_id  | movie_title  | director        | producers             |
      | 2          | "The Empire Strikes Back" | "Irvin Kershner" | "Gary Kurtz, Rick McCallum" |
      | 4          | "A New Hope"  | "George Lucas"  | "Gary Kurtz, Rick McCallum" |
