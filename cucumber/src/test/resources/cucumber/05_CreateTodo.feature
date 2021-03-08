@CreateTodoStory
Feature: Create todo
  As a user
  I would like to create a todo
  So that I can track my work that needs to be done

  Background: The todo application is running
    Given the todo application is running

  Scenario Outline: I want to create a todo (Success Flow)
    Given a todo does not exist with "<title>"
    When I create the todo with "<title>"
    Then the Todo application has created a todo with "<title>"
    Examples:
    | title |
    | ct01  |
    | ct02  |

  Scenario Outline: I want to create a already finished todo (Alternate Flow)
    Given a todo does not exist with "<title>"
    When I create the todo with "<title>" and "<doneStatus>"
    Then the Todo application has created a todo with "<title>"
    And the todo has a done status of "<doneStatus>"
    Examples:
    | title | doneStatus |
    | zy01  | true       |
    | zy02  | true       |



    Scenario Outline: I want to create a todo without a title
      When I create the todo with no title and only "<desc>"
      Then  I get a status code of <statusCode>
      And I get and error message of "<errorMessage>"
      Examples:
        |statusCode| desc  | errorMessage |
        |400       | d1    | title : field is mandatory |