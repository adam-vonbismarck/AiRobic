package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.formattypes.Week;
import edu.brown.cs.student.main.models.markov.Emission;

import java.time.format.DateTimeFormatter;

public class ConvertToJson {

    public static String convert(Schedule schedule) {
        String jsonString = "{";
        for (Week week : schedule.weeks()) {
            for (Day day : week.days()) {
                assert(!day.getDate().isEmpty());
                jsonString += "\"" +
                        (day.getDate().get().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))) +
                        "\" : {";
                int workoutCounter = 0;
                if (day.getEmissions().size() == 0) {
                    jsonString += " }";
                }
                for (Emission emission : day.getEmissions()) {
                    workoutCounter++;
                    jsonString += "\"" + workoutCounter + "\" : { \"workout\" : \"" + emission.getWorkout() +
                            "\", \"duration\" : \"" + emission.getTime() + "\" }" +
                            ((day.getEmissions().indexOf(emission) == day.getEmissions().size() - 1) ? " }" : ", ");
                }
                jsonString += ", ";
            }
        }
        return jsonString.substring(0, jsonString.length() - 2);
    }

}
