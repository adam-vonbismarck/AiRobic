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

/**
 * The GenerateGraphLikePlan class takes in a set of constraints, including the start and end date of the plan,
 * the type of high and low intensity workouts desired, and the percent of high intensity work, and builds a schedule
 * based on the VariableModelGenerator (i.e., using transitions between workout types to generate the plan).
 */
public class GenerateGraphLikePlan {

  public static int MAX_WORKOUTS = 20;

  /**
   * This method generates a variable plan using the VariableModelBuilder, being careful to account for the minutes
   * constraint when reading in new Emissions to the final Schedule.
   *
   * @param minutes              - the amount of time the caller has.
   * @param startDate            - the start date of the plan.
   * @param endDate              - the end date of the plan.
   * @param highIntensityLabels  - the set of high intensity workout types that should be converted into HiddenStates
   *                             in the variable model.
   * @param lowIntensityLabels   - the set of low intensity workout types that should be converted into HiddenStates
   *                             in the variable model.
   * @param highIntensityPercent - the percentage of high intensity work.
   * @return the built schedule.
   * @throws IOException                  if there are problems reading workout files from the data folder.
   * @throws InvalidDistributionException if any distribution is invalid when building the underlying MarkovModel.
   * @throws InvalidScheduleException     if there are problems generating the schedule (i.e. constructing Days).
   * @throws NoWorkoutTypeException       if the sets of workouts do not contain workouts, are null, or contain the null
   *                                      Workout.
   * @throws FormatterFailureException if there are problems producing Emission from the MarkovModel.
   * @throws InvalidDatesException if the start date is before the end date.
   */
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

    // this schedule building requires a significant amount of special casing to keep Sundays as rest days.
    List<Week> weeks = new ArrayList<>();

    LocalDate firstSunday =
        startDate.plusDays(DayOfWeek.SUNDAY.getValue() - startDate.getDayOfWeek().getValue());

    // special case for a short first week
    if (endDate.isBefore(firstSunday.plusDays(1))) {
      // when conditional logic like this is used, it ensures that Sunday is not accounted for when scaling
      // the minutes for a short week
      weeks.add(
          this.generateWeek(
              Math.floorDiv(
                  minutes
                      * (endDate.getDayOfWeek().getValue()
                          - startDate.getDayOfWeek().getValue()
                          + (endDate.getDayOfWeek().equals(DayOfWeek.SUNDAY) ? 0 : 1)),
                  6),
              startDate,
              endDate,
              varModel));
      return new Schedule("schedule", weeks, weeks.get(0));
    }

    // adds the first week, provided it goes through the first sunday
    weeks.add(
        this.generateWeek(
            Math.floorDiv(
                minutes * (DayOfWeek.SUNDAY.getValue() - startDate.getDayOfWeek().getValue()), 6),
            startDate,
            firstSunday,
            varModel));

    LocalDate currDate = firstSunday;

    // adds inner weeks
    while (currDate.plusDays(6).isBefore(endDate)) {
      weeks.add(this.generateWeek(minutes, currDate.plusDays(1), currDate.plusDays(7), varModel));
      currDate = currDate.plusDays(7);
    }

    // adds the final week, special casing for if Sunday is the last day (so it is not included in the minute-scaling
    // computation)
    weeks.add(
        this.generateWeek(
            Math.floorDiv(
                minutes
                    * (endDate.getDayOfWeek().getValue()
                        - DayOfWeek.MONDAY.getValue()
                        + (endDate.getDayOfWeek().equals(DayOfWeek.SUNDAY) ? 0 : 1)),
                6),
            currDate.plusDays(1),
            endDate,
            varModel));

    return new Schedule("schedule", weeks, ((weeks.size() <= 1) ? weeks.get(0) : weeks.get(1)));
  }

  /**
   * This method generates a singular week using the variable model.
   *
   * @param minutes - the minutes the caller wants to work out during this week, roughly.
   * @param startDay - the start date of the week.
   * @param endDay - the end date of the week.
   * @param model - the variable MarkovModel.
   * @return the built Week.
   * @throws InvalidDistributionException if the MarkovModel has any invalid distributions.
   * @throws FormatterFailureException if the MarkovModel emissions cannot be read into the Week.
   * @throws InvalidScheduleException if there are any issues constructing Schedule components, like Days.
   */
  private Week generateWeek(int minutes, LocalDate startDay, LocalDate endDay, MarkovModel model)
      throws InvalidDistributionException, FormatterFailureException, InvalidScheduleException {

    List<Emission> totalWeekEmissions = new ArrayList<>();
    List<Day> days = new ArrayList<>();

    // number of days that include workouts
    long numDays =
        startDay
            .datesUntil(endDay.plusDays((endDay.getDayOfWeek() == DayOfWeek.SUNDAY) ? 0 : 1))
            .count();

    // loops through, creating each day for the new week
    LocalDate dummyDate = startDay.minusDays(1);
    for (int i = 0; i < startDay.datesUntil(endDay.plusDays(1)).count(); i++) {
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

    List<Emission> potentialWorkouts = model.generateFormattedEmissions(MAX_WORKOUTS, new DefaultFormatter());

    // assigns workouts from the Emissions generated on the line above to a new list, that will comply with the
    // minutes constraint.
    while (minutes > 0) {
      assert (potentialWorkouts.size() > 0);
      Emission nextWorkout = potentialWorkouts.remove(0);
      totalWeekEmissions.add(nextWorkout);
      minutes -= nextWorkout.getTime();
    }

    // adding emissions in, distributing in a similar way to DistributeWorkouts in teh ScheduleBuilder class.
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
