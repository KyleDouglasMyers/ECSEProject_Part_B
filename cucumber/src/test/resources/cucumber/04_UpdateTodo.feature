@UpdateTodoStory
Feature: Update a todo
  As a user,
  I would like to update a todo
  So that i can change info about it if my task goals change

  Background:
    Given the todo application is running
    And these tasks are created
      | title | doneStatus | description          |
      | x1    | false      | my first assignment  |
      | x2    | false      | my second assignment |
      | x3    | false      | my first project     |

  #SUCCESS FLOW
  Scenario Outline: Update a todos description (Success Flow)
    Given a todo exists with <id>
    When I change todo <id> description with "<desc>"
    Then I get a status code of <statusCode>
    And todo with <id> has new description "<desc>"
    Examples:
      | id | desc | statusCode |
      | 1  | new  | 200        |

  #ALTERNATE FLOW
  Scenario Outline: Update a todos doneStatus (Alternate Flow)
    Given a todo exists with <id>
    When I change todo <id> doneStatus with "<ds>"
    Then I get a status code of <statusCode>
    And todo with <id> has new doneStatus "<ds>"
    Examples:
      | id | ds   | statusCode |
      | 1  | true | 200        |

  #ERROR FLOW
  Scenario Outline: Update a todo with a bad id (Error Flow)
    When I update a todo with a wrong id "<badId>"
    Then I get a status code of <statusCode>
    And I get and error message of "<errorMsg>"
    Examples:
      | statusCode | errorMsg                                    | badId |
      | 404        | Could not find any instances with todos/400 | 400   |
