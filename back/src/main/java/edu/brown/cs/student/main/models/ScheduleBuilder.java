package edu.brown.cs.student.main.models;

import edu.brown.cs.student.main.RandomGenerator;
import edu.brown.cs.student.main.models.exceptions.InvalidDistributionException;
import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.Day.WorkoutDescription;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.formattypes.Week;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The ScheduleBuilder class builds schedules given a number of minutes and a percentage of high
 * intensity work OR a number of low intensity/high intensity workouts per week. From here, the
 * ScheduleBuilder class builds a reasonable schedule (with intensity labels provided) for use
 * in generating a MarkovModel.
 */
public class ScheduleBuilder {

  public static int NUM_DAYS = 7;
  public static int NUM_WORKOUT_DAYS = 6;

  /**
   * These two HashMaps allow for easy conversion of day to integer – in fact, it might be worth
   * using an enum...?
   */
  public static HashMap<String, Integer> daysToInts = new HashMap<>() {
    {
      this.put("Monday", 0);
      this.put("Tuesday", 1);
      this.put("Wednesday", 2);
      this.put("Thursday", 3);
      this.put("Friday", 4);
      this.put("Saturday", 5);
      this.put("Sunday", 6);
    }
  };

  public static HashMap<Integer, String> intsToDays = new HashMap<>() {
    {
      this.put(0, "Monday");
      this.put(1, "Tuesday");
      this.put(2, "Wednesday");
      this.put(3, "Thursday");
      this.put(4, "Friday");
      this.put(5, "Saturday");
      this.put(6, "Sunday");
    }
  };

  /**
   * The minutes call takes in a host of parameters, and builds a schedule (designed with rowing in
   * mind) that will be reasonable given the time constraint. In particular, it calculates the number
   * of low and high intensity workouts, and passes the problem off to the "workouts" method.
   *
   * @param minutes - approximate minutes per week the user has
   * @param numWeeks - the number of weeks the schedule should be
   * @param highPercent - the percentage of high intensity work to be done
   * @param startDay - the day of the week the schedule should start
   * @param endDay - the day of the week the schedule should end
   * @param highIntensityLabel - the category of workout that should be high intensity
   * @param lowIntensityLabel - the category of workout that should be low intensity
   * @return a schedule that fits these constraints.
   * @throws InvalidDistributionException if the percentage of high intensity work is not between
   * 0 and 1.
   */
  public Schedule minutes(int minutes, int numWeeks, double highPercent, String startDay, String endDay,
      String highIntensityLabel, String lowIntensityLabel) throws InvalidDistributionException {
    RandomGenerator.validateDistribution(String.class, new HashMap<>() {{
      this.put("High intensity", highPercent);
      this.put("Low intensity", 1 - highPercent);
    }});

    // calculate high intensity minutes, with a minimum of 60
    long highIntensity = Math.round(Math.max(highPercent*minutes, 60));

    // calculate low intensity minutes from high intensity minutes
    long lowIntensity = minutes - highIntensity;

    // calculate the number of high intensity workouts, assuming each is 60 minutes, with a maximum of 4
    long numHighIntensity = Math.min(Math.floorDiv(highIntensity, 60), 4);

    // find the length of low intensity workouts, with a minimum of 60 minute sessions, and a maximum
    // of 10 sessions per week
    long lowIntensityWorkoutLength = Math.max(60, lowIntensity/(10 - numHighIntensity));

    // use the low intensity workout length and the number of low intensity minutes to calculate the
    // number of low intensity workouts
    long numLowIntensity = Math.floorDiv(lowIntensity, lowIntensityWorkoutLength);

    // call on the rest of the schedule building
    return this.workouts(numHighIntensity, numLowIntensity, numWeeks, startDay, endDay,
        highIntensityLabel, lowIntensityLabel, lowIntensityWorkoutLength);
  }

  /**
   * This method builds a schedule given a host of parameters, including the number of high and
   * low intensity workouts per week. Using this information, the method distributes these workouts
   * evenly over the course of an example week, and builds the schedule from there.
   *
   * @param highIntensity - the number of high intensity workouts in the schedule
   * @param lowIntensity - the number of low intensity workouts in the schedule
   * @param numWeeks - the number of weeks in the schedule
   * @param startDay - the day of the week the schedule should start
   * @param endDay - the day of the week the schedule should end
   * @param highIntensityLabel - the category of workout that should be high intensity
   * @param lowIntensityLabel - the category of workout that should be low intensity
   * @param lowLength - the length of a given low intensity workout
   * @return the built schedule, given these constraints.
   */
  public Schedule workouts(long highIntensity, long lowIntensity, int numWeeks, String startDay, String endDay,
      String highIntensityLabel, String lowIntensityLabel, long lowLength) {

    // will inline comment once the method is confirmed.

    long workouts = highIntensity + lowIntensity;
    ArrayList<Week> weeks = new ArrayList<>();
    ArrayList<Day> exampleDays = new ArrayList<>();

    this.distributeWorkouts(exampleDays, workouts);
    this.distributeIntensities(exampleDays, workouts, highIntensity, highIntensityLabel,
        lowIntensityLabel, lowLength);

    Week exampleWeek = new Week("week", exampleDays);

    weeks.add(new Week("week", exampleWeek.getDaySubset(daysToInts.get(startDay), NUM_DAYS)));

    for (int i = 0; i < numWeeks - 2; i++) {
      Week newWeek = new Week("week", exampleWeek.getDaySubset(0, NUM_DAYS));
      weeks.add(newWeek);
    }

    weeks.add(new Week("week", exampleWeek.getDaySubset(0, daysToInts.get(endDay) + 1)));

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
  private void distributeWorkouts(ArrayList<Day> days, long workouts) {
    for (int j = 0; j < NUM_DAYS; j++) {
      days.add(new Day("day", new ArrayList<>(), 0, intsToDays.get(j),
          new ArrayList<>(), new ArrayList<>()));
    }

    if (workouts <= 0) {
      return;
    }

    while (workouts > NUM_WORKOUT_DAYS - 1) {
      for (int k = 0; k < NUM_WORKOUT_DAYS; k++) {
        days.get(k).incrementNumWorkouts();
        workouts--;
      }
    }

    if (workouts <= 0) {
      return;
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

    assert(workouts == 0);
  }

  /**
   * Still ensuring that these next two methods are reasonable for schedule generation.
   */
  private void distributeIntensities(List<Day> days, long workouts, long highIntensity,
      String highIntensityLabel, String lowIntensityLabel, long lowLength) {

    if (highIntensity <= 0) {
      this.fillInLow(days, lowIntensityLabel, lowLength);
      return;
    }

    while (highIntensity > NUM_WORKOUT_DAYS - 1) {
      for (int k = 0; k < NUM_WORKOUT_DAYS; k++) {
        days.get(k).addFirstIntensity(new WorkoutDescription(highIntensityLabel, 60));
        highIntensity--;
      }
    }

    if (highIntensity <= 0) {
      this.fillInLow(days, lowIntensityLabel, lowLength);
      return;
    }

    int w = 0;
    long remainingHighInt = highIntensity;
    float cumulativeCounter = 0;
    while (w < NUM_WORKOUT_DAYS) {
      days.get(w).addFirstIntensity(new WorkoutDescription(highIntensityLabel, 60));
      highIntensity--;
      cumulativeCounter += ((float) NUM_WORKOUT_DAYS/remainingHighInt);
      w = Math.toIntExact(Math.round(Math.floor(cumulativeCounter)));
    }

    this.fillInLow(days, lowIntensityLabel, lowLength);
  }

  private void fillInLow(List<Day> days, String lowIntensityLabel, long lowLength) {
    for (Day day : days) {
      if (day.getNumberOfWorkouts() <= day.getIntensityLength()) {
        continue;
      }
      for (int i = 0; i <= day.getNumberOfWorkouts() - day.getIntensityLength(); i++) {
        day.addFirstIntensity(new WorkoutDescription(lowIntensityLabel, Math.toIntExact(lowLength)));
      }
    }
  }
}
