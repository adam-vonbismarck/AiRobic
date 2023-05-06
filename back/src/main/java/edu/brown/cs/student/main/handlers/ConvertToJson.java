package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.formattypes.Week;
import edu.brown.cs.student.main.models.markov.model.Emission;
import java.time.format.DateTimeFormatter;

public class ConvertToJson {

  public static String convert(Schedule schedule) {
    StringBuilder jsonString = new StringBuilder("{");

    for (Week week : schedule.weeks()) {
      for (Day day : week.days()) {
        assert (!day.getDate().isEmpty());

        jsonString
            .append("\"")
            .append(day.getDate().get().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")))
            .append("\" : {");

        int emissionCounter = 0;
        for (Emission emission : day.getEmissions()) {
          emissionCounter++;
          jsonString
              .append("\"")
              .append(emissionCounter)
              .append("\" : { \"workout\" : \"")
              .append(emission.getWorkout())
              .append("\", \"duration\" : \"")
              .append(emission.getTime())
              .append("\" }");

          if (emissionCounter < day.getEmissions().size()) {
            jsonString.append(",");
          }
        }

        jsonString.append("}");

        if (day.getEmissions().isEmpty()) {
          jsonString.append(",");
        }

        if (day != week.days().get(week.days().size() - 1)) {
          jsonString.append(",");
        }
      }
    }

    jsonString.append("}");

    return jsonString.toString();
  }
}
