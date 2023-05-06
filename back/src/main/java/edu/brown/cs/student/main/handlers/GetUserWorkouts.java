package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.database.DatabaseCommands;
import edu.brown.cs.student.main.server.Serializer;
import java.util.HashMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class GetUserWorkouts implements Route {

  @Override
  public Object handle(Request request, Response response) throws Exception {
    String username = request.queryParams("username");
    HashMap<String, Object> output = new HashMap<>();
    if (username == null) {
      output.put("result", "error_bad_request");
      output.put("message", "ERROR: Invalid input.");
    } else {
      String where = "users/" + username + "/schedule";
      String workoutJSON = new DatabaseCommands().get(where);
      System.out.println(workoutJSON);
      output.put("result", "success");
      output.put("message", workoutJSON.replace("\\", ""));
    }
    return Serializer.serialize(output);
  }
}
