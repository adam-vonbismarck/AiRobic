package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.database.DatabaseCommands;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.rowing.Workout;
import edu.brown.cs.student.main.server.Serializer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreatePlan implements Route {


  /**
   * NOTE: we need to really error check this code. i.e., before we pass things on, we need to check that they
   * are integers, of the proper format for dates, etc. Also maybe store model types in an enum? just ideas as I'm
   * modifying this
   */
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
      else{ // this branch is gonna make the next part unreachable...but I've added a class for linear model below.
        output.put("result", "error_bad_request");
        output.put("message", "ERROR: Invalid input (no goal).");
        return Serializer.serialize(output);
      }

      // an example of the type of error checking that we need
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
      LocalDate parsedStart;
      LocalDate parsedEnd;
      try {
        parsedStart = LocalDate.parse(startDate, formatter);
      } catch (DateTimeParseException e) {
        // put err to results
        return Serializer.serialize(output);
      }
      try {
        parsedEnd = LocalDate.parse(endDate, formatter);
      } catch (DateTimeParseException e) {
        // put err to results
        return Serializer.serialize(output);
      }

      Schedule built = new GenerateLinearPlan().generate(Integer.parseInt(hoursPerWeek), parsedStart, parsedEnd,
              Workout.TWOKM, Workout.UTT, 0.2);

      // gagagagagag colins code

      new DatabaseCommands().update("{\"sport\":\"" + sport + "\"}", "users/" + username);
      output.put("result", "success");
      output.put("message", "Successfully updated " + username);
    }
    return Serializer.serialize(output);
  }
}
