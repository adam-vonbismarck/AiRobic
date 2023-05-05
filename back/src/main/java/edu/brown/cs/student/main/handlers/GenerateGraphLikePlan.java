package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.models.exceptions.*;
import edu.brown.cs.student.main.models.formatters.DefaultFormatter;
import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.formattypes.Week;
import edu.brown.cs.student.main.models.markov.Emission;
import edu.brown.cs.student.main.models.markov.MarkovModel;
import edu.brown.cs.student.main.rowing.VariableModelBuilder;
import edu.brown.cs.student.main.rowing.Workout;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GenerateGraphLikePlan {

  public Schedule generate(
      int minutes,
      LocalDate startDate,
      LocalDate endDate,
      Set<Workout> highIntensityLabels,
      Set<Workout> lowIntensityLabels,
      double highIntensityPercent)
      throws IOException, InvalidDistributionException, InvalidScheduleException,
          NoWorkoutTypeException, FormatterFailureException, InvalidDatesException {

    if (!startDate.isBefore(endDate)) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
      throw new InvalidDatesException(
          "Start date of plan: "
              + startDate.format(formatter)
              + " was not before"
              + " end date of plan: "
              + endDate.format(formatter));
    }

    VariableModelBuilder builder = new VariableModelBuilder();
    MarkovModel varModel =
        builder.build(lowIntensityLabels, highIntensityLabels, minutes, highIntensityPercent);

    List<Week> weeks = new ArrayList<>();

    LocalDate firstSunday =
        startDate.plusDays(DayOfWeek.SUNDAY.getValue() - startDate.getDayOfWeek().getValue());

    weeks.add(
        this.generateWeek(
            Math.floorDiv(
                minutes * (DayOfWeek.SUNDAY.getValue() - startDate.getDayOfWeek().getValue()), 6),
            startDate,
            firstSunday,
            varModel));

    LocalDate currDate = firstSunday;

    while (currDate.plusDays(6).isBefore(endDate)) {
      weeks.add(this.generateWeek(minutes, currDate.plusDays(1), currDate.plusDays(7), varModel));
      currDate = currDate.plusDays(7);
    }

    weeks.add(
        this.generateWeek(
            Math.floorDiv(
                minutes * (endDate.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue() + 1), 6),
            currDate.plusDays(1),
            endDate,
            varModel));

    System.out.println(varModel.toString());

    return new Schedule("schedule", weeks, ((weeks.size() <= 1) ? weeks.get(0) : weeks.get(1)));
  }

  private Week generateWeek(int minutes, LocalDate startDay, LocalDate endDay, MarkovModel model)
      throws InvalidDistributionException, FormatterFailureException {

    List<Emission> totalWeekEmissions = new ArrayList<>();
    List<Day> days = new ArrayList<>();

    long numDays =
        startDay
            .datesUntil(endDay.plusDays((endDay.getDayOfWeek() == DayOfWeek.SUNDAY) ? 0 : 1))
            .count();

    LocalDate dummyDate = startDay.minusDays(1);
    for (int i = 0; i < startDay
            .datesUntil(endDay.plusDays(1))
            .count(); i++) {
      dummyDate = dummyDate.plusDays(1);
      days.add(
          new Day(
              "day",
              new ArrayList<>(),
              0,
              dummyDate.getDayOfWeek(),
              Optional.of(dummyDate),
              new ArrayList<>()));
    }

    // change: generate long seq and work back
    while (minutes > 0) {
      List<Emission> singleton = model.generateFormattedEmissions(1, new DefaultFormatter());
      assert (singleton.size() == 1);
      totalWeekEmissions.add(singleton.get(0));
      minutes -= singleton.get(0).getTime();
    }

    System.out.println(totalWeekEmissions);

    while (totalWeekEmissions.size() > numDays) {
      for (int i = 0; i < numDays; i++) {
        days.get(i).incrementNumWorkouts();
        days.get(i).addWorkout(totalWeekEmissions.remove(0));
      }
    }

    int w = 0;
    long remainingWorkouts = totalWeekEmissions.size();
    float cumulativeCounter = 0;
    while (w < numDays) {
      days.get(w).incrementNumWorkouts();
      days.get(w).addWorkout(totalWeekEmissions.remove(0));
      cumulativeCounter += ((float) numDays / remainingWorkouts);
      w = Math.toIntExact(Math.round(Math.floor(cumulativeCounter)));
    }

    assert (totalWeekEmissions.size() == 0);
    return new Week("week", days);
  }
}
