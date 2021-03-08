@GetTodosStory
Feature: Get todos

  As a user,
  I would like to see todos
  So that I can see the what needs to be done

  Background: The todo application is running
    Given the todo application is running
    And these tasks are created
      | title    | doneStatus  | description   |
      | p1       | true        | my assignment |
      | p2       | true        | my assignment |
      | project1 | true        | my project    |

  #SUCCESS FLOW
  Scenario Outline: Display todos (Success Flow)
    When I get the todos
    Then I get a status code of <statusCode>
    Examples:
      | statusCode |
      | 200        |

  #ALTERNATE FLOW
  Scenario Outline: Display todos with specific done status (Alternate Flow)
    When I get the todos that are "<doneStatus>"
    Then I get a status code of <statusCode>
    And all of the todos have a done status of "<doneStatus>"
    Examples:
      | statusCode | doneStatus |
      | 200        | true       |
      | 200        | false      |

  #ERROR FLOW
  Scenario Outline: Display a todo with a incorrect url (Error Flow)
    When I get a todo with a bad url "<badUrl>"
    Then I get a status code of <statusCode>
    Examples:
      | statusCode | badUrl |
      | 404        | /todo  |




