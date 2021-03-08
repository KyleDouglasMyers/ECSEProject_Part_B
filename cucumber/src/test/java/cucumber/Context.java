package cucumber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Context {
    public static Context aContext=null;
    public Map<String, List<Todo>> todoContext;
    public Map<String, Integer> statusCodes;
    public Todo singleTodo;
    public int statusCode;
    public String errorMsg;

    public Context (){
        this.todoContext= new HashMap<>();
        this.statusCodes=new HashMap<String,Integer>();
    }

    public static Context getContext() {
        if (aContext == null) {
            aContext = new Context();
        }
        return aContext;
    }

    public static void resetContext() {
        aContext.todoContext.clear();
        aContext.statusCodes.clear();
        aContext = null;
    }





}
