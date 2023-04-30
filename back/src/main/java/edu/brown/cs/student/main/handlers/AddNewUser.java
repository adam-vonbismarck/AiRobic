package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.database.DatabaseCommands;
import edu.brown.cs.student.main.database.NonSusDatabaseCommands;
import edu.brown.cs.student.main.server.Serializer;
import java.util.HashMap;
import java.util.Objects;
import spark.Request;
import spark.Response;
import spark.Route;

public class AddNewUser implements Route {

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
      String valid = new NonSusDatabaseCommands().get(where);
      if (Objects.equals(valid, "\"true\"")) {
        output.put("result", "error_bad_request");
        output.put("message", "ERROR: User already exists.");
      }
      else{
        String info = "{\"" + username + "\":{\"schedule\":\"\",\"valid\":\"true\"}}";
        new NonSusDatabaseCommands().update(info, "users");
        output.put("result", "success");
        output.put("message", "Successfully added " + username);
      }
    }
    return Serializer.serialize(output);
  }
}
