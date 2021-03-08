package cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        monochrome = true,
        plugin = {"pretty"},
        //tags = "@GetTodosStory or @GetTodoWithIdStory or @DeleteTodoStory or @UpdateTodoStory or @CreateTodoStory"
        tags = "@GetTodoWithIdStory or @UpdateTodoStory or @CreateTodoStory or @DeleteTodoStory or @GetTodosStory"

)
public class RunCucumberTest {

}
