package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.database.DatabaseCommands;
import edu.brown.cs.student.main.database.DatabaseCommandsSDK;
import edu.brown.cs.student.main.database.NonSusDatabaseCommands;
import edu.brown.cs.student.main.server.Serializer;
import java.util.HashMap;
import java.util.Objects;
import spark.Request;
import spark.Response;
import spark.Route;

public class CheckUser implements Route {

  @Override
  public Object handle(Request request, Response response) throws Exception {
    String username = request.queryParams("username");
    HashMap<String, Object> output = new HashMap<>();
    if (username == null) {
      output.put("result", "error_bad_request");
      output.put("message", "ERROR: Invalid input.");
    }
    else{
      String where = "users/" + username + "/valid";
      String valid = new DatabaseCommandsSDK().get(where);
      if (Objects.equals(valid, "\"true\"")) {
        output.put("result", "success");
        output.put("message", "True");
      }
      else {
        output.put("result", "success");
        output.put("message", "False");
      }
    }
    return Serializer.serialize(output);
  }
}
