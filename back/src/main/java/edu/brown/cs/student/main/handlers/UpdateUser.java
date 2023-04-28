package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.server.Serializer;
import java.util.HashMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class UpdateUser implements Route {

  @Override
  public Object handle(Request request, Response response) throws Exception {
    String username = request.queryParams("username");
    String sport = request.queryParams("sport");
    String startdate = request.queryParams("startdate");
    String enddate = request.queryParams("enddate");
    String time = request.queryParams("time");
    String model = request.queryParams("model");
    String goal = request.queryParams("goal");
    HashMap<String, Object> output = new HashMap<>();

    // Colins code goes here!!!

    output.put("result", "success");
    output.put("message", "Successfully updated " + username);
    return Serializer.serialize(output);
  }
}
