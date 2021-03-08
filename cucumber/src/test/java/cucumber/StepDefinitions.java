package cucumber;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.junit.Assert;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


public class StepDefinitions {

    private static Process proc;
    private int randomNum[];
    //This boolean will turn exec on and off, can use to see if tests work when application not running
    private static boolean appRunning = false;
    private static int numRuns = 0;
    private static final int maxRuns =17;
    private static String scName="";
    private static String fName="";

    //Setup and tear down
    @Before
    public void set_up() throws InterruptedException {
        numRuns ++;
        if (!appRunning) {
            try {

                proc = Runtime.getRuntime().exec("java -jar runTodoManagerRestAPI-1.5.5.jar");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Make sure application is running
            System.out.println("Starting Feature: "+fName+"\nScenario: "+scName+"...\n");
            for (int i = 3; i > 0; i--) {
                System.out.println(i);
                Thread.sleep(500);
            }
            RestAssured.baseURI = "http://localhost:4567";
            appRunning=true;
        }
    }

    @After
    public void tear_down() throws InterruptedException {
            HelperFunctions.cleanse();
//            if (numRuns>maxRuns){
//                proc.destroy();
//            }
        proc.destroy();
        appRunning=false;
        Thread.sleep(500);
    }

    //BACKGROUND
    @Given("^the todo application is running")
    public void the_todo_application_is_running() throws InterruptedException {
        assertEquals(true, appRunning);

    }

    @And("these tasks are created")
    public void these_tasks_are_created(List<Map<String, String>> list) {

        for (Map<String, String> map : list) {
            HelperFunctions.createTodo(
                    map.get("title"),
                    HelperFunctions.stringToBool(map.get("doneStatus")),
                    map.get("description")
            );
        }


    }


    ///////////////////////
    // GET ALL SCENARIOS //
    ///////////////////////

    @When("I get the todos")
    public void i_get_the_todos() {
        fName="GetTodos";
        scName="Success Flow";
        Context.getContext().todoContext.put("getTodo", HelperFunctions.getAllTodos(""));

    }

    @Then("I get a status code of {int}")
    public void i_should_get_a_status_code_of(int sc) {
        assertEquals(sc, Context.getContext().statusCode);
    }

    @When("I get the todos that are {string}")
    public void i_get_the_todos_that_are_true(String bool) {
        fName="GetTodos";
        scName="Alternate Flow";
        Context.getContext().todoContext.put("getTodo"+bool, HelperFunctions.getAllTodos("?doneStatus="+bool));
    }

    @Then("all of the todos have a done status of {string}")
    public void all_of_the_todos_should_have_a_done_status_of(String string) {
        List<Todo> todos = Context.getContext().todoContext.get("getTodo"+string);
        for (Todo t : todos) {
            if(t.getDoneStatus()!=Boolean.parseBoolean(string)){
                Assert.fail();
            };

        }
    }

    @When("I get a todo with a bad url {string}")
    public void i_get_a_todo_with_a_bad(String badUrl) {
        fName="GetTodos";
        scName="Error Flow";
        RequestSpecification request = RestAssured.given().baseUri("http://localhost:4567");

        Response response = request.get(badUrl);

        Context.getContext().statusCode = response.getStatusCode();
    }


    ///////////////////////////////
    // GET SPECIFIC ID SCENARIOS //
    ///////////////////////////////

    @When("I get a todo with {int}")
    public void i_get_a_todo_with(Integer id) {
        fName="GetTodoWithId";
        scName="Success Flow and Alternate Flow";
        HelperFunctions.getSingleTodo(id);
    }

    @Then("the todo has a id of {int}")
    public void the_todo_has_a_id_of(Integer id) {
        int cid=Context.getContext().singleTodo.getId();
        assertTrue(cid==id);
    }

    @When("I get a todo with a bad {int}")
    public void i_get_the_todo(Integer badID) {
        fName="GetTodoWithId";
        scName="Error Flow";
        RequestSpecification request = RestAssured.given().baseUri("http://localhost:4567");

        Response response = request.get("/todos/"+badID);

        Context.getContext().statusCode = response.getStatusCode();
        Context.getContext().errorMsg = response.asString();

    }

    @Then("I get and error message of {string}")
    public void i_get_and_error_message_of(String errMsg) {
        assertTrue(Context.getContext().errorMsg.contains(errMsg));
    }

    //////////////////////
    // CREATE SCENARIOS //
    //////////////////////

    @Given("a todo does not exist with {string}")
    public void a_todo_does_not_exist_with(String title) {
        List<Todo> todos = HelperFunctions.getAllTodos("");
        for (Todo t : todos){
            if (t.getTitle().equals(title)){Assert.fail();}
        }
    }

    @When("I create the todo with {string}")
    public void i_create_the_todo_with(String title) {
        fName="CreateTodo";
        scName="Success Flow";
        Context.getContext().singleTodo = HelperFunctions.createTodo(title,false,"");
    }

    @Then("the Todo application has created a todo with {string}")
    public void the_todo_application_has_created_a_todo_with(String title) {
        List<Todo> todos = HelperFunctions.getAllTodos("");
        for (Todo t : todos){
            if (t.getTitle().equals(title)){
                assertEquals(Context.getContext().singleTodo.getTitle(), title);

            }
        }
    }

    @When("I create the todo with {string} and {string}")
    public void i_create_the_todo_with_and(String title, String doneStatus) {
        fName="CreateTodo";
        scName="Alternate Flow";
        Context.getContext().singleTodo = HelperFunctions.createTodo(title,Boolean.parseBoolean(doneStatus),"");
    }

    @Then("the todo has a done status of {string}")
    public void the_todo_has_a_done_status_of(String bool) {
        assertEquals(Context.getContext().singleTodo.getDoneStatus(),Boolean.parseBoolean(bool));


    }

    @When("I create the todo with no title and only {string}")
    public void i_create_the_todo_with_no_title_and_only(String desc) {
        fName="CreateTodo";
        scName="Error Flow";
        RequestSpecification request = RestAssured.given().baseUri("http://localhost:4567");

        JSONObject requestParams = new JSONObject();

        requestParams.put("description", desc);

        request.body(requestParams.toJSONString());

        Response response = request.post("todos");

        Context.getContext().statusCode = response.getStatusCode();
        Context.getContext().errorMsg = response.asString();
    }

    /////////////////
    // DELETE TODO //
    /////////////////

    @Given("a todo exists with {int}")
    public void a_todo_exists_with(int id) {
        List<Todo> todos = HelperFunctions.getAllTodos("");
        boolean found = false;
        for (Todo t : todos){
            if(t.getId()==id){
                found = true;
            }
        }
        assertTrue(found);
    }

    @When("I delete a todo with {int}")
    public void i_delete_a_todo_with(int id) {
        fName="DeleteTodo";
        scName="Success Flow";
        HelperFunctions.deleteTodo(id);
    }

    @Then("the todo with {int} is deleted")
    public void the_todo_with_is_deleted(int id) {
        List<Todo> todos = HelperFunctions.getAllTodos("");
        boolean found = false;
        for (Todo t : todos){
            if(t.getId()==id){
                found = true;
            }
        }
        assertTrue(!found);
    }

    @When("I delete a todo with the highest id")
    public void i_delete_a_todo_with_the_highest_id() {
        fName="DeleteTodo";
        scName="Alternate Flow";
        List<Todo> todos = HelperFunctions.getAllTodos("");
        int temp=0;
        for (Todo t : todos){
            if (t.getId()>temp){
                temp = t.getId();
                Context.getContext().singleTodo=t;
            }
        }
        HelperFunctions.deleteTodo(temp);

    }

    @Then("the todo with the highest id is deleted")
    public void the_todo_with_the_highest_id_is_deleted() {
        List<Todo> todos = HelperFunctions.getAllTodos("");
        int deleted = Context.getContext().singleTodo.getId();
        boolean found = false;
        for (Todo t : todos){
            if(t.getId()==deleted){
                found = true;
            }
        }
        assertTrue(!found);
    }

    @When("I delete a todo with a wrong id {string}")
    public void i_delete_a_todo_with_a_wrong_id(String badId) {
        fName="DeleteTodo";
        scName="Error Flow";
        RequestSpecification request = RestAssured.given().baseUri("http://localhost:4567");

        Response response = request.delete("todos/"+badId);

        Context.getContext().statusCode = response.getStatusCode();
        Context.getContext().errorMsg = response.asString();
    }

    /////////////////
    // UPDATE TODO //
    /////////////////

    @When("I change todo {int} description with {string}")
    public void i_would_like_change_todo_title_with(Integer id, String desc) throws Exception {
        fName="UpdateTodo";
        scName="Success Flow";
        HelperFunctions.updateDescription(id,desc);
    }

    @Then("todo with {int} has new description {string}")
    public void todo_with_has_new_new(Integer id, String input) {

        int cid = Context.getContext().singleTodo.getId();
        assertTrue(cid==id);
        assertTrue(Context.getContext().singleTodo.getDescription().equals(input));

    }

    @When("I change todo {int} doneStatus with {string}")
    public void i_change_todo_done_status_with(Integer id, String ds) {
        fName="UpdateTodo";
        scName="Alternate Flow";
        HelperFunctions.updateDoneStatus(id,HelperFunctions.stringToBool(ds));
    }

    @Then("todo with {int} has new doneStatus {string}")
    public void todo_with_has_new_done_status(Integer id, String input) {

        int cid = Context.getContext().singleTodo.getId();
        assertTrue(cid==id);
        assertTrue(Context.getContext().singleTodo.getDoneStatus()==(Boolean.parseBoolean(input)));

    }

    @When("I update a todo with a wrong id {string}")
    public void i_update_a_todo_with_a_wrong_id(String badId) {
        fName="UpdateTodo";
        scName="Error Flow";
        RequestSpecification request = RestAssured.given().baseUri("http://localhost:4567");

        Response response = request.delete("todos/"+badId);

        Context.getContext().statusCode = response.getStatusCode();
        Context.getContext().errorMsg = response.asString();
    }
}


