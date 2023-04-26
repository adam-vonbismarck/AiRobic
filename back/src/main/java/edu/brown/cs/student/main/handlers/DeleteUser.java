package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.database.DatabaseCommands;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;


public class DeleteUser implements Handler, Route {

  public DeleteUser() throws IOException, InterruptedException {
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    String username = request.queryParams("username");
    Map<String, Object> output = new HashMap<>();
    if (username == null) {
      output.put("result", "error_bad_request");
      output.put("message", "ERROR: No filepath");
    }
    else{
      new DatabaseCommands().delete(username + "/");
      output.put("result", "success");
      output.put("message", "Successfully deleted" + username);
    }
    return this.serialize(output);
  }
}
