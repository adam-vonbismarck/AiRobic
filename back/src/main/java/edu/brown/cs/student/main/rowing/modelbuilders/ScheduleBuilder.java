package edu.brown.cs.student.main.rowing.modelbuilders;

import edu.brown.cs.student.main.models.exceptions.InvalidDistributionException;
import edu.brown.cs.student.main.models.exceptions.InvalidScheduleException;
import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.Day.WorkoutDescription;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.formattypes.Week;
import edu.brown.cs.student.main.models.markov.modelbuilding.Workout;
import edu.brown.cs.student.main.server.RandomGenerator;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * The ScheduleBuilder class builds schedules given a number of minutes and a percentage of high
 * workoutType work OR a number of low workoutType/high workoutType workouts per week. From here,
 * the ScheduleBuilder class builds a reasonable schedule (with workoutType labels provided) for use
 * in generating a MarkovModel. One big limitation of this implementation is that it assumes low
 * intensity workouts can be scaled by time – this is not an unreasonable assumption, but it could
 * create problems in other sports.
 */
public class ScheduleBuilder {

  public static int NUM_DAYS = 7;
  public static int NUM_WORKOUT_DAYS = 6;
  public static int MIN_MINUTES = 120;

  public static int MAX_MINUTES = 1200;

  public static int MIN_LOW_LENGTH = 60;
  public static int HIGH_LENGTH = 60;
  public static int MAX_WORKOUTS_PER_WEEK = 10;
  public static int MAX_HIGH_WORKOUTS = 4;

  /**
   * The minutesWithDates method uses the minutes method in order to build a full linear schedule
   * plan, built in with dates.
   *
   * @param minutes - approximate minutes per week the user has
   * @param highPercent - the percentage of high workoutType work to be done
   * @param startDay - the date the schedule should start
   * @param endDay - the date the schedule should end
   * @param highIntensityLabel - the category of workout that should be high workoutType
   * @param lowIntensityLabel - the category of workout that should be low workoutType
   * @return a schedule that fits these constraints.
   * @throws InvalidDistributionException if the percentage of high workoutType work is not between
   *     0 and 1.
   * @throws InvalidScheduleException if the schedule cannot be generated reasonably given the
   *     constraints.
   */
  public Schedule minutesWithDates(
      int minutes,
      LocalDate startDay,
      LocalDate endDay,
      double highPercent,
      Workout highIntensityLabel,
      Workout lowIntensityLabel)
      throws InvalidDistributionException, InvalidScheduleException {

    if (startDay == null || endDay == null) {
      throw new InvalidScheduleException(
          "All schedules must have not null start days and end days in order to be built.",
          new Schedule("schedule", List.of(), new Week("week", List.of())));
    }

    // checking inputs
    if (!startDay.isBefore(endDay)) {
      throw new InvalidScheduleException(
          "All schedules must have start days before end days in order to be built.",
          new Schedule("schedule", List.of(), new Week("week", List.of())));
    }

    RandomGenerator.validateDistribution(
        String.class,
        new HashMap<>() {
          {
            this.put("High workoutType", highPercent);
            this.put("Low workoutType", 1 - highPercent);
          }
        });

    // calculating number of weeks
    int weekCounter = 0;
    int numDays = startDay.datesUntil(endDay.plusDays(1)).toList().size();

    numDays -= NUM_DAYS - (startDay.getDayOfWeek().getValue() - 1);
    weekCounter++;

    while (numDays > NUM_DAYS) {
      numDays -= NUM_DAYS;
      weekCounter++;
    }

    if (numDays > 0) {
      weekCounter++;
    }

    // building schedule
    Schedule schedule =
        this.minutes(
            minutes,
            weekCounter,
            highPercent,
            startDay.getDayOfWeek(),
            endDay.getDayOfWeek(),
            highIntensityLabel,
            lowIntensityLabel);

    // reading in dates
    LocalDate dummyDate = startDay.minusDays(1);
    for (Week week : schedule.weeks()) {
      for (Day day : week.days()) {
        dummyDate = dummyDate.plusDays(1);
        day.setDate(dummyDate);
      }
    }

    assert (dummyDate.equals(endDay));
    return schedule;
  }

  /**
   * The minutes call takes in a host of parameters, and builds a schedule (designed with rowing in
   * mind) that will be reasonable given the time constraint. In particular, it calculates the
   * number of low and high workoutType workouts, and passes the problem off to the "workouts"
   * method.
   *
   * @param minutes - approximate minutes per week the user has
   * @param numWeeks - the number of weeks the schedule should be
   * @param highPercent - the percentage of high workoutType work to be done
   * @param startDay - the day of the week the schedule should start
   * @param endDay - the day of the week the schedule should end
   * @param highIntensityLabel - the category of workout that should be high workoutType
   * @param lowIntensityLabel - the category of workout that should be low workoutType
   * @return a schedule that fits these constraints.
   * @throws InvalidDistributionException if the percentage of high workoutType work is not between
   *     0 and 1.
   * @throws InvalidScheduleException if the schedule cannot be generated reasonably given the
   *     constraints.
   */
  public Schedule minutes(
      int minutes,
      int numWeeks,
      double highPercent,
      DayOfWeek startDay,
      DayOfWeek endDay,
      Workout highIntensityLabel,
      Workout lowIntensityLabel)
      throws InvalidDistributionException, InvalidScheduleException {

    // checking inputs
    RandomGenerator.validateDistribution(
        String.class,
        new HashMap<>() {
          {
            this.put("High workoutType", highPercent);
            this.put("Low workoutType", 1 - highPercent);
          }
        });

    if (minutes < MIN_MINUTES || minutes > MAX_MINUTES) {
      throw new InvalidScheduleException(
          "All schedules must have a number of minutes between 120 and 1200.",
          new Schedule("schedule", List.of(), new Week("week", List.of())));
    }

    if (numWeeks < 1) {
      throw new InvalidScheduleException(
          "All schedules must have a positive number of weeks.",
          new Schedule("schedule", List.of(), new Week("week", List.of())));
    }

    // calculate high workoutType minutes, with a minimum of 60
    long highIntensity = Math.round(Math.max(highPercent * minutes, HIGH_LENGTH));

    // calculate low workoutType minutes from high workoutType minutes
    long lowIntensity = minutes - highIntensity;

    // calculate the number of high workoutType workouts, assuming each is 60 minutes, with a
    // maximum of 4
    long numHighIntensity = Math.min(Math.floorDiv(highIntensity, HIGH_LENGTH), MAX_HIGH_WORKOUTS);

    // find the length of low workoutType workouts, with a minimum of 60 minute sessions, and a
    // maximum of 10 sessions per week
    long lowIntensityWorkoutLength =
        Math.max(MIN_LOW_LENGTH, lowIntensity / (MAX_WORKOUTS_PER_WEEK - numHighIntensity));

    // use the low workoutType workout length and the number of low workoutType minutes to calculate
    // the number of low workoutType workouts
    long numLowIntensity = Math.round((double) lowIntensity / (double) lowIntensityWorkoutLength);

    // call on the rest of the schedule building
    return this.workouts(
        numHighIntensity,
        numLowIntensity,
        numWeeks,
        startDay,
        endDay,
        highIntensityLabel,
        lowIntensityLabel,
        lowIntensityWorkoutLength);
  }

  /**
   * This method builds a schedule given a host of parameters, including the number of high and low
   * workoutType workouts per week. Using this information, the method distributes these workouts
   * evenly over the course of an example week, and builds the schedule from there.
   *
   * @param highIntensity - the number of high workoutType workouts in the schedule
   * @param lowIntensity - the number of low workoutType workouts in the schedule
   * @param numWeeks - the number of weeks in the schedule
   * @param startDay - the day of the week the schedule should start
   * @param endDay - the day of the week the schedule should end
   * @param highIntensityLabel - the category of workout that should be high workoutType
   * @param lowIntensityLabel - the category of workout that should be low workoutType
   * @param lowLength - the length of a given low workoutType workout
   * @return the built schedule, given these constraints.
   */
  public Schedule workouts(
      long highIntensity,
      long lowIntensity,
      int numWeeks,
      DayOfWeek startDay,
      DayOfWeek endDay,
      Workout highIntensityLabel,
      Workout lowIntensityLabel,
      long lowLength)
      throws InvalidScheduleException {

    // checking inputs
    if (highIntensity < 0 || lowIntensity < 0 || numWeeks < 1) {
      throw new InvalidScheduleException(
          "All schedules must have a positive number of weeks, high intensity workouts "
              + "and low intensity workouts.",
          new Schedule("schedule", List.of(), new Week("week", List.of())));
    }

    if (lowLength < 0) {
      throw new InvalidScheduleException(
          "All schedules must have a positive low intensity workout length.",
          new Schedule("schedule", List.of(), new Week("week", List.of())));
    }

    // checks the case where the start day is the before the end day, and only one week is being
    // built.
    if (numWeeks == 1 && startDay.getValue() >= endDay.getValue()) {
      throw new InvalidScheduleException(
          "All schedules must have start days before end days in order to be built.",
          new Schedule("schedule", List.of(), new Week("week", List.of())));
    }

    long workouts = highIntensity + lowIntensity;
    ArrayList<Week> weeks = new ArrayList<>();
    ArrayList<Day> exampleDays = new ArrayList<>();

    // distributing workouts over the example days
    this.distributeWorkouts(exampleDays, workouts);
    this.distributeIntensities(
        exampleDays, highIntensity, highIntensityLabel, lowIntensityLabel, lowLength);

    // filling in each week
    Week exampleWeek = new Week("week", exampleDays);

    if (numWeeks == 1) {
      weeks.add(
          new Week("week", exampleWeek.getDaySubset(startDay.getValue() - 1, endDay.getValue())));
      return new Schedule("schedule", weeks, exampleWeek);
    }

    weeks.add(new Week("week", exampleWeek.getDaySubset(startDay.getValue() - 1, NUM_DAYS)));

    for (int i = 0; i < numWeeks - 2; i++) {
      Week newWeek = new Week("week", exampleWeek.getDaySubset(0, NUM_DAYS));
      weeks.add(newWeek);
    }

    weeks.add(new Week("week", exampleWeek.getDaySubset(0, endDay.getValue())));

    return new Schedule("schedule", weeks, exampleWeek);
  }

  /**
   * This method takes in a list of days and a number of workouts, and distributes these workouts
   * evenly over the course of a week. While the arithmetic is a little messy, this distribution
   * approximation produces consistent results for 7-day weeks.
   *
   * @param days - the list of days over which to distribute
   * @param workouts - the number of workouts to distribute
   */
  private void distributeWorkouts(List<Day> days, long workouts) throws InvalidScheduleException {
    if (days == null || workouts < 0) {
      throw new InvalidScheduleException(
          "Workout distribution requires a list of days to fill and a positive number"
              + "of workouts to distribute.",
          new Schedule("schedule", List.of(), new Week("week", List.of())));
    }

    for (int j = 0; j < NUM_DAYS; j++) {
      days.add(
          new Day(
              "day",
              new ArrayList<>(),
              0,
              DayOfWeek.of(j + 1),
              Optional.empty(),
              new ArrayList<>()));
    }

    if (workouts <= 0) {
      return;
    }

    // begins by adding workouts to every day, if there are enough workouts to span a week
    while (workouts > NUM_WORKOUT_DAYS - 1) {
      for (int k = 0; k < NUM_WORKOUT_DAYS; k++) {
        days.get(k).incrementNumWorkouts();
        workouts--;
      }
    }

    if (workouts <= 0) {
      return;
    }

    // once there are not enough workouts to span a week, we take 6/[number remaining], and look at
    // the sequence
    // of length [number remaining] of successive adds of 6/[number remaining] starting from 0. for
    // example,
    // if there are 4 days remaining, we consider the sequence 0, 1.5, 3, 4.5. We take the floor of
    // each number
    // in the sequence, and distribute to each day according to the resulting indices. This serves
    // as an approximation
    // for distributing workouts evenly.
    int w = 0;
    long remainingWorkouts = workouts;
    float cumulativeCounter = 0;
    while (w < NUM_WORKOUT_DAYS) {
      days.get(w).incrementNumWorkouts();
      workouts--;
      cumulativeCounter += ((float) NUM_WORKOUT_DAYS / remainingWorkouts);
      w = Math.toIntExact(Math.round(Math.floor(cumulativeCounter)));
    }

    assert (workouts == 0);
  }

  /**
   * This method distributes intensity labels over the course of a list of days (length of a week).
   * Uses a similar distribution process to distributeWorkouts.
   *
   * @param days - the days to distribute over.
   * @param highIntensity - the number of high intensity workouts.
   * @param highIntensityLabel - the label of the high intensity workouts.
   * @param lowIntensityLabel - the label of the low intensity workouts.
   * @param lowLength - the length of one low intensity workout.
   */
  private void distributeIntensities(
      List<Day> days,
      long highIntensity,
      Workout highIntensityLabel,
      Workout lowIntensityLabel,
      long lowLength) {

    // given the method is private, these should have been caught at a higher level.
    assert (days.size() == NUM_DAYS);
    assert (highIntensity >= 0);
    assert (lowLength >= 0);

    List<Day> sublistWithWorkouts = new ArrayList<>();
    for (Day day : days) {
      if (day.getNumberOfWorkouts() > 0) {
        sublistWithWorkouts.add(day);
      }
    }

    int numDays = sublistWithWorkouts.size();

    // the next section distributes the high intensity workouts; once they are all distributed at
    // any stage,
    // fillInLow is called to complete filling out all workouts.
    if (highIntensity <= 0) {
      this.fillInLow(days, lowIntensityLabel, lowLength);
      return;
    }

    while (highIntensity > numDays - 1) {
      for (int k = 0; k < numDays; k++) {
        sublistWithWorkouts
            .get(k)
            .addFirstIntensity(new WorkoutDescription(highIntensityLabel, 60));
        highIntensity--;
      }
    }

    if (highIntensity <= 0) {
      this.fillInLow(days, lowIntensityLabel, lowLength);
      return;
    }

    // same comment as in distributeWorkouts
    int w = 0;
    long remainingHighInt = highIntensity;
    float cumulativeCounter = 0;
    while (w < numDays) {
      sublistWithWorkouts.get(w).addFirstIntensity(new WorkoutDescription(highIntensityLabel, 60));
      highIntensity--;
      cumulativeCounter += ((float) numDays / remainingHighInt);
      w = Math.toIntExact(Math.round(Math.floor(cumulativeCounter)));
    }

    this.fillInLow(days, lowIntensityLabel, lowLength);
  }

  /**
   * The fillInLow method takes in a list of days and a low intensity label, and fills in all
   * missing workouts with a low intensity workout (remainder left after distributing high
   * intensity).
   *
   * @param days - the list of days to distribute over.
   * @param lowIntensityLabel - the low intensity workout type.
   * @param lowLength - the length of a low intensity workout.
   */
  private void fillInLow(List<Day> days, Workout lowIntensityLabel, long lowLength) {

    // these conditions should already be checked once they reach here, similar to
    // distributeIntensities
    assert (days.size() == NUM_DAYS);
    assert (lowLength > 0);

    for (Day day : days) {
      if (day.getNumberOfWorkouts() <= day.getIntensityLength()) {
        continue;
      }
      for (int i = 0; i <= day.getNumberOfWorkouts() - day.getIntensityLength(); i++) {
        day.addFirstIntensity(
            new WorkoutDescription(lowIntensityLabel, Math.toIntExact(lowLength)));
      }
    }
  }
}
