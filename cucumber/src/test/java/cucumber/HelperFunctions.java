package cucumber;

import io.cucumber.core.gherkin.messages.internal.gherkin.internal.com.eclipsesource.json.Json;
import io.cucumber.messages.internal.com.google.gson.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HelperFunctions {

    public static Gson gson = new Gson();

    public static List<Todo> getAllTodos(String query) {
        RequestSpecification request = RestAssured.given().baseUri("http://localhost:4567");

        Response response = request.get("/todos"+query);

        Context.getContext().statusCode = response.getStatusCode();

        JsonObject json = new JsonParser().parse(response.asString()).getAsJsonObject();
        JsonArray arr = json.getAsJsonArray("todos");


        List<Todo> result = new ArrayList<>();
        for (JsonElement obj : arr) {
            Todo todo = gson.fromJson(obj.getAsJsonObject(), Todo.class);
            result.add(todo);
        }


        return result;
    }

    public static void getSingleTodo(int id){
        RequestSpecification request = RestAssured.given().baseUri("http://localhost:4567");

        Response response = request.get("todos/"+id);

        TodoReply todos = gson.fromJson(response.asString(), TodoReply.class);

        Todo result = todos.getTodos().get(0);


        Context.getContext().statusCode=response.getStatusCode();
        Context.getContext().singleTodo=result;

    }



    public static Todo createTodo(String title, boolean doneStatus, String description) {
        RequestSpecification request = RestAssured.given().baseUri("http://localhost:4567");

        JSONObject requestParams = new JSONObject();

        requestParams.put("title", title);
        requestParams.put("doneStatus", doneStatus);
        requestParams.put("description", description);

        request.body(requestParams.toJSONString());

        Response response = request.post("/todos");

        Todo result = gson.fromJson(response.asString(), Todo.class);

        Context.getContext().singleTodo=result;

        return result;
    }

    public static void deleteTodo(int todoId) {
        RequestSpecification request = RestAssured.given().baseUri("http://localhost:4567");

        Response response = request.delete("/todos/" + todoId);


    }

    public static void updateDoneStatus(int todoId, boolean doneStatus) {
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("doneStatus", doneStatus);

        request.body(requestParams.toJSONString())
                .baseUri("http://localhost:4567");

        Response response = request.post("/todos/" + todoId);

        Context.getContext().singleTodo = gson.fromJson(response.asString(), Todo.class);
        Context.getContext().statusCode = response.statusCode();

    }

    public static void updateDescription(int todoId, String desc) throws Exception{

        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("description", desc);

        request.body(requestParams.toJSONString())
                .baseUri("http://localhost:4567");

        Response response = request.post("/todos/" + todoId);

        Context.getContext().singleTodo = gson.fromJson(response.asString(), Todo.class);
        Context.getContext().statusCode = response.statusCode();

    }

    private static JSONObject createTodoRequest(String title, String description, Boolean doneStatus) {

        JSONObject request = new JSONObject();
        if (title != null) {
            request.put("title", title);
        }
        if (description != null) {
            request.put("description", description);
        }
        if (doneStatus != null) {
            request.put("doneStatus", doneStatus);
        }
        return request;
    }

    public static Boolean stringToBool(String s) {
        Boolean bool;
        switch (s) {
            case "true":
                bool = true;
                break;
            case "false":
                bool = false;
                break;
            default:
                bool = null;
        }
        return bool;
    }

    public static void cleanse() {
        List<Todo> todos = HelperFunctions.getAllTodos("");
        for (Todo t : todos){
            if(t.getId()>2) {
                HelperFunctions.deleteTodo(t.getId());
            }
        }
        RequestSpecification request1 = RestAssured.given().baseUri("http://localhost:4567");

        JSONObject requestParams1 = new JSONObject();
        requestParams1.put("title", "scan paperwork");
        requestParams1.put("doneStatus", false);
        requestParams1.put("description", "");

        request1.body(requestParams1.toJSONString());

        Response response1 = request1.post("/todos/1");

        RequestSpecification request2 = RestAssured.given().baseUri("http://localhost:4567");

        JSONObject requestParams2 = new JSONObject();
        requestParams2.put("title", "file paperwork");
        requestParams2.put("doneStatus", false);
        requestParams2.put("description", "");

        request2.body(requestParams2.toJSONString());

        Response response2 = request2.post("/todos/2");
    }
}

