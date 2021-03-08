@DeleteTodoStory
Feature: Delete todo
  As a user,
  I want to delete a todo
  So that i can manage my work

  Background: The application is running
    Given the todo application is running
    And these tasks are created
      | title       | doneStatus | description |
      | a1          | false      | ex          |
      | a2          | false      | ex          |
      | assignment1 | false      | ex          |

  #SUCCESS FLOW
  Scenario Outline: Delete a todo with id (Success Flow)
    Given a todo exists with <id>
    When I delete a todo with <id>
    Then I get a status code of <statusCode>
    And the todo with <id> is deleted
    Examples:
      | id | statusCode |
      | 3  | 200        |

  #ALTERNATE FLOW
  Scenario Outline: Delete a todo with highest id (Alternate Flow)
    When I delete a todo with the highest id
    Then I get a status code of <statusCode>
    And the todo with the highest id is deleted
    Examples:
      | statusCode |
      | 200        |

  #ERROR FLOW
  Scenario Outline: Delete a todo with an incorrect id (Error Flow)
    When I delete a todo with a wrong id "<badId>"
    Then I get a status code of <statusCode>
    And I get and error message of "<errorMsg>"
    Examples:
      | statusCode | errorMsg                                    | badId|
      | 404        | Could not find any instances with todos/200 | 200  |

