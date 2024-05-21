Feature: Star Wars API and UI tests

  Scenario Outline: Get list of movies and check count
    Given I have access to the Star Wars API
    When I send a GET request to the films endpoint
    Then I should receive a response with status code 200
    And The response should contain <FILMS_COUNT> movies
    Examples:
      | FILMS_COUNT |
      | 6           |

  Scenario Outline: Get the 3rd movie and check the director of the movie
    Given I have access to the Star Wars API
    When I send a GET request for the movie 3
    Then I should receive a response with "<status code>" 200
    And The director/s of the movie is/are "<DIRECTOR>"
    Examples:
      | DIRECTOR         | status code |
      | Richard Marquand | 200         |

  Scenario: Get the non-existing movie and check the director of the movie
    Given I have access to the Star Wars API
    When I send a GET request for non existing movie
    Then I should receive a response with status code 404


  Scenario Outline: Get the 5th movie and assert that ’Producers’ are not ‘Gary Kurtz, George Lucas'
    Given I have access to the Star Wars API
    When I send a GET request for the movie 5
    Then I should receive a response with status code 200
    And The producer/s of the movie is/are not "<PRODUCER1>" and "<PRODUCER2>"
    Examples:
      | PRODUCER1  | PRODUCER2    |
      | Gary Kurtz | George Lucas |


  Scenario: Sort movies by title and check last movie
    Given I am on the Star Wars movie list page
    When I sort the movies by title
    Then the last movie in the list should be "The Phantom Menace"

  Scenario: View movie details and check species list
    Given I am on the Star Wars movie list page
    When I select the movie "The Empire Strikes Back"
    Then the species list should contain "Wookie"

  Scenario: Check that planet is not in movie
    Given I am on the Star Wars movie list page
    When I select the movie "The Phantom Menace"
    Then the planets list should not contain "Camino"