package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.models.exceptions.FormatterFailureException;
import edu.brown.cs.student.main.models.exceptions.InvalidDistributionException;
import edu.brown.cs.student.main.models.exceptions.InvalidScheduleException;
import edu.brown.cs.student.main.models.exceptions.NoWorkoutTypeException;
import edu.brown.cs.student.main.rowing.LinearModelBuilder;
import edu.brown.cs.student.main.rowing.Workout;
import edu.brown.cs.student.main.rowing.ScheduleBuilder;
import edu.brown.cs.student.main.models.WorkoutDistributionByName;
import edu.brown.cs.student.main.models.formatters.ScheduleFormatter;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.markov.MarkovModel;
import edu.brown.cs.student.main.server.Serializer;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class GenerateLinearPlan {


  public Schedule generate(int minutes, LocalDate startDate, LocalDate endDate, Workout highIntensityLabel,
                           Workout lowIntensityLabel, double highIntensityPercent) throws IOException, InvalidDistributionException, InvalidScheduleException, NoWorkoutTypeException, FormatterFailureException {
    ScheduleBuilder builder = new ScheduleBuilder();
    Schedule toBuild = builder.minutesWithDates(minutes, startDate,
            endDate, highIntensityPercent, highIntensityLabel,
            lowIntensityLabel);
    MarkovModel model = new LinearModelBuilder().build(toBuild, startDate.getDayOfWeek());
    model.generateFormattedEmissions(toBuild.getLength(), new ScheduleFormatter(toBuild));
    return toBuild;
  }
}
