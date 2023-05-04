package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.models.exceptions.FormatterFailureException;
import edu.brown.cs.student.main.models.exceptions.InvalidDistributionException;
import edu.brown.cs.student.main.models.exceptions.InvalidScheduleException;
import edu.brown.cs.student.main.models.exceptions.NoWorkoutTypeException;
import edu.brown.cs.student.main.models.formatters.DefaultFormatter;
import edu.brown.cs.student.main.models.formatters.ScheduleFormatter;
import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.formattypes.Week;
import edu.brown.cs.student.main.models.markov.Emission;
import edu.brown.cs.student.main.models.markov.MarkovModel;
import edu.brown.cs.student.main.rowing.LinearModelBuilder;
import edu.brown.cs.student.main.rowing.ScheduleBuilder;
import edu.brown.cs.student.main.rowing.Workout;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GenerateGraphLikePlan {

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

  public Week generateWeek(int minutes, LocalDate startDay, LocalDate endDay,
                           MarkovModel model) throws InvalidDistributionException, FormatterFailureException {

    List<Emission> totalWeekEmissions = new ArrayList<>();
    List<Day> days = new ArrayList<>();

    long numDays = startDay.datesUntil(endDay.plusDays(
            (endDay.getDayOfWeek() == DayOfWeek.SUNDAY) ? 0 : 1
    )).count();

    LocalDate dummyDate = startDay.minusDays(1);
    for (int i = 0; i < numDays; i++) {
      dummyDate = dummyDate.plusDays(1);
      days.add(new Day("day", new ArrayList<>(), 0, dummyDate.getDayOfWeek(), Optional.of(dummyDate),
              new ArrayList<>()));
    }

    while (minutes > 0) {
      List<Emission> singleton = model.generateFormattedEmissions(1, new DefaultFormatter());
      assert(singleton.size() == 1);
      totalWeekEmissions.add(singleton.get(0));
      minutes -= singleton.get(0).getTime();
    }

    while (totalWeekEmissions.size() > numDays) {
      for (int i = 0; i < numDays; i++) {
        totalWeekEmissions.remove(0);
      }
    }

    int w = 0;
    long remainingWorkouts = workouts;
    float cumulativeCounter = 0;
    while (w < NUM_WORKOUT_DAYS) {
      days.get(w).incrementNumWorkouts();
      workouts--;
      cumulativeCounter += ((float) NUM_WORKOUT_DAYS/remainingWorkouts);
      w = Math.toIntExact(Math.round(Math.floor(cumulativeCounter)));
    }
  }
}
