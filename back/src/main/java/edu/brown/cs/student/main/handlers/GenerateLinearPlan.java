package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.models.LinearModelBuilder;
import edu.brown.cs.student.main.models.ScheduleBuilder;
import edu.brown.cs.student.main.models.formatters.ScheduleFormatter;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.markov.MarkovModel;
import edu.brown.cs.student.main.server.Serializer;
import java.util.HashMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class GenerateLinearPlan implements Route {


  @Override
  public Object handle(Request request, Response response) throws Exception {
    ScheduleBuilder builder = new ScheduleBuilder();
    Schedule toBuild = builder.minutes(420, 4, 0.2,
        "Monday", "Friday", "2k", "UT2");
    MarkovModel model = new LinearModelBuilder().build(toBuild, "start");
    model.generateFormattedEmissions(1, new ScheduleFormatter(toBuild));

    HashMap<String, Object> results = new HashMap<>();
    results.put("schedule", toBuild);
    return Serializer.serialize(results);
  }
}
