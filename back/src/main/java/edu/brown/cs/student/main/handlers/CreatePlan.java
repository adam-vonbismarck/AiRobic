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
    if (username == null
        || sport == null
        || startDate == null
        || endDate == null
        || hoursPerWeek == null
        || model == null) {
      output.put("result", "error_bad_request");
      output.put("message", "ERROR: Invalid input.");
      return Serializer.serialize(output);
    }
    // Error checking
    System.out.println(username);
    System.out.println(startDate);
    System.out.println(endDate);
    System.out.println(model);
    System.out.println(hoursPerWeek);
    System.out.println(goal);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate parsedStart;
    LocalDate parsedEnd;
    int parsedHours;
    try {
      parsedStart = LocalDate.parse(startDate, formatter);
    } catch (DateTimeParseException e) {
      output.put("result", "error_bad_request");
      output.put("message", "ERROR: Invalid input (start date).");
      return Serializer.serialize(output);
    }
    try {
      parsedEnd = LocalDate.parse(endDate, formatter);
    } catch (DateTimeParseException e) {
      output.put("result", "error_bad_request");
      output.put("message", "ERROR: Invalid input (end date).");
      return Serializer.serialize(output);
    }
    try {
      parsedHours = Integer.parseInt(hoursPerWeek);
    } catch (NumberFormatException e) {
      output.put("result", "error_bad_request");
      output.put("message", "ERROR: Invalid input (hours per week).");
      return Serializer.serialize(output);
    }
    // Adding the sport to the database

    System.out.println(username);
    System.out.println(parsedStart);
    System.out.println(parsedEnd);
    System.out.println(model);
    System.out.println(parsedHours);
    System.out.println(goal);

    new DatabaseCommands().update("{\"sport\":\"" + sport + "\"}", "users/" + username);
    // Handling the goal oriented model
    switch (model) {
      case "model3" -> {
        if (goal == null) {
          output.put("result", "error_bad_request");
          output.put("message", "ERROR: Invalid input (no goal).");
        } else {
          if (Workout.of(goal) == Workout.NONE || Workout.of(goal) == Workout.UT_2) {
            output.put("result", "error_bad_request");
            output.put("message", "ERROR: Invalid input (wrong goal).");
          } else {
            System.out.println("Reached this line!");
            try {
              Schedule built =
                  new GenerateLinearPlan()
                      .generate(
                          parsedHours, parsedStart, parsedEnd, Workout.of(goal), Workout.UT_2, 0.2);
              new DatabaseCommands()
                  .update(
                      Serializer.serializeSchedule(built.flatten()),
                      "users/" + username + "/schedule");
            } catch (Exception e) {
              System.out.println(e.getMessage());
              output.put("result", "error_bad_request");
              output.put("message", "ERROR: Server output: " + e.getMessage());
              return Serializer.serialize(output);
            }
            System.out.println("Reached this giga line!");
            output.put("result", "success");
            output.put("message", "Successfully updated " + username);
          }
        }
        return Serializer.serialize(output);
      }
        // Handling the classic linear model
      case "model1" -> {
        // change this to overall
        try {
          Schedule built =
              new GenerateLinearPlan()
                  .generate(parsedHours, parsedStart, parsedEnd, Workout._2K, Workout.UT_2, 0.2);
          System.out.println(Serializer.serializeSchedule(built.flatten()));
          new DatabaseCommands()
              .put(
                  Serializer.serializeSchedule(built.flatten()), "users/" + username + "/schedule");
        } catch (Exception e) {
          System.out.println(e.getMessage());
          output.put("result", "error_bad_request");
          output.put("message", "ERROR: Server output: " + e.getMessage());
          return Serializer.serialize(output);
        }
        output.put("result", "success");
        output.put("message", "Successfully updated " + username);
        return Serializer.serialize(output);
      }
        // Handling the variable model
      case "model2" -> {
        try {
          // Schedule built = new GenerateGraphLikePlan().generate(parsedHours, parsedStart,
          // parsedEnd,
          // , , 0.2);
          // System.out.println(Serializer.serializeSchedule(built.flatten()));
          // new DatabaseCommands().put(Serializer.serializeSchedule(built.flatten()), "users/" +
          // username + "/schedule");
        } catch (Exception e) {
          System.out.println(e.getMessage());
          output.put("result", "error_bad_request");
          output.put("message", "ERROR: Server output: " + e.getMessage());
          return Serializer.serialize(output);
        }
        output.put("result", "success");
        output.put("message", "Successfully updated " + username);
        return Serializer.serialize(output);
      }
        // Handling invalid models
      default -> {
        output.put("result", "error_bad_request");
        output.put("message", "ERROR: Invalid input (model).");
        return Serializer.serialize(output);
      }
    }
  }
}
