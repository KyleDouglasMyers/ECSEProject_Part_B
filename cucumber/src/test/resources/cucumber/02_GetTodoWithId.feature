@GetTodoWithIdStory
Feature: Get specific todo
  As a user,
  I would like to display a specific todo
  So that I can focus on one task

  Background: The todo application is running
    Given the todo application is running
    And these tasks are created
      | title            | doneStatus | description          |
      | k1               | true       | my first assignment  |
      | k2               | false      | my second assignment |
      | k3               | true       | my first project     |

  #SUCCESS AND ALTERNATE FLOWS (EXAMPLES)
  Scenario Outline: Display specific todo (Success and Alternate Flows)
    Given a todo exists with <id>
    When I get a todo with <id>
    Then I get a status code of <statusCode>
    And the todo has a id of <id>
    Examples:
      |statusCode| id|
      #Success Flow
      |200       | 1 |
      #Alternative Flow
      |200       | 2 |

  #ERROR FLOW
  Scenario Outline: Display a todo with a incorrect id (Error Flow)
    When I get a todo with a bad <id>
    Then I get a status code of <statusCode>
    And I get and error message of "<errorMessage>"
    Examples:
      |statusCode| id  | errorMessage |
      |404       | 100 | Could not find an instance with todos/100 |