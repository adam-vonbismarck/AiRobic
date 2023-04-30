package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.database.DatabaseCommands;
import edu.brown.cs.student.main.server.Serializer;
import java.util.HashMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreatePlan implements Route {

  @Override
  public Object handle(Request request, Response response) throws Exception {
    String username = request.queryParams("username");
    String sport = request.queryParams("sport");
    String startDate = request.queryParams("startDate");
    String endDate = request.queryParams("endDate");
    String hoursPerWeek = request.queryParams("hoursPerWeek");
    String model = request.queryParams("model");
    String goal = request.queryParams("goal");
    HashMap<String, Object> output = new HashMap<>();
    if (username == null || sport == null || startDate == null ||
        endDate == null || hoursPerWeek == null || model == null){
      output.put("result", "error_bad_request");
      output.put("message", "ERROR: Invalid input.");
    }
    else{
      if (model.equals("goaloriented") & goal!=null){

        // gagagagagag colins code

      }
      else{
        output.put("result", "error_bad_request");
        output.put("message", "ERROR: Invalid input (no goal).");
        return Serializer.serialize(output);
      }

      // gagagagagag colins code

      new DatabaseCommands().update("{\"sport\":\"" + sport + "\"}", "users/" + username);
      output.put("result", "success");
      output.put("message", "Successfully updated " + username);
    }
    return Serializer.serialize(output);
  }
}
