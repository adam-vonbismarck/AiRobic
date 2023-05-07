package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.database.DatabaseCommands;
import edu.brown.cs.student.main.server.serializing.Serializer;
import java.util.HashMap;
import spark.Request;
import spark.Response;
import spark.Route;

/** This class updates the workout data for a specific workout after user completes it */
public class UpdateWorkout implements Route {

  @Override
  public Object handle(Request request, Response response) throws Exception {
    String username = request.queryParams("username");
    String day = request.queryParams("day");
    String workout = request.queryParams("workout");
    String rpe = request.queryParams("rpe");
    String split = request.queryParams("split");
    String distance = request.queryParams("distance");
    HashMap<String, Object> output = new HashMap<>();
    if (username == null
        || day == null
        || workout == null
        || rpe == null
        || split == null
        || distance == null) {
      output.put("result", "error_bad_request");
      output.put("message", "ERROR: Invalid input.");
      return Serializer.serialize(output);
    }
    String info =
        "{\"rpe\":\""
            + rpe
            + "\",\"split\":\""
            + split
            + "\","
            + "\"distance\":\""
            + distance
            + "\"}";
    String where = "users/" + username + "/schedule/days/" + day + "/workouts/" + workout + "/data";
    new DatabaseCommands().update(info, where);
    output.put("result", "success");
    output.put("message", "Successfully added workout data");
    return Serializer.serialize(output);
  }
}
