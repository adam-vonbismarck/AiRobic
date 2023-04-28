package edu.brown.cs.student.main.models;

import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.formattypes.Week;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScheduleBuilder {

  public static int NUM_DAYS = 7;
  public static int NUM_WORKOUT_DAYS = 6;

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

  public Schedule minutes(int minutes, int numWeeks, double highPercent, String startDay, String endDay,
      String highIntensityLabel, String lowIntensityLabel) {
    long highIntensity = Math.round(Math.max(highPercent*minutes, 60));
    long lowIntensity = minutes - highIntensity;
    long numHighIntensity = Math.min(Math.floorDiv(highIntensity, 60), 4);
    long lowIntensityWorkoutLength = Math.max(60, lowIntensity/(10 - numHighIntensity));
    long numLowIntensity = Math.floorDiv(lowIntensity, lowIntensityWorkoutLength);
    return this.workouts(numHighIntensity, numLowIntensity, numWeeks, startDay, endDay, highIntensityLabel, lowIntensityLabel);
  }

  public Schedule workouts(long highIntensity, long lowIntensity, int numWeeks, String startDay, String endDay,
      String highIntensityLabel, String lowIntensityLabel) {
    long workouts = highIntensity + lowIntensity;
    ArrayList<Week> weeks = new ArrayList<>();
    ArrayList<Day> exampleDays = new ArrayList<>();

    this.distributeWorkouts(exampleDays, workouts);
    this.distributeIntensities(exampleDays, workouts, highIntensity, highIntensityLabel, lowIntensityLabel);

    Week exampleWeek = new Week("week", exampleDays);

    weeks.add(new Week("week", exampleWeek.getDaySubset(daysToInts.get(startDay), NUM_DAYS)));

    for (int i = 0; i < numWeeks - 2; i++) {
      Week newWeek = new Week("week", exampleWeek.getDaySubset(0, NUM_DAYS));
      weeks.add(newWeek);
    }

    weeks.add(new Week("week", exampleWeek.getDaySubset(0, daysToInts.get(endDay))));

    return new Schedule("schedule", weeks, exampleWeek);
  }

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

  private void distributeIntensities(List<Day> days, long workouts, long highIntensity,
      String highIntensityLabel, String lowIntensityLabel) {

    if (highIntensity <= 0) {
      this.fillInLow(days, lowIntensityLabel);
      return;
    }

    while (highIntensity > NUM_WORKOUT_DAYS - 1) {
      for (int k = 0; k < NUM_WORKOUT_DAYS; k++) {
        days.get(k).addFirstIntensity(highIntensityLabel);
        highIntensity--;
      }
    }

    if (highIntensity <= 0) {
      this.fillInLow(days, lowIntensityLabel);
      return;
    }

    int w = 0;
    long remainingHighInt = highIntensity;
    float cumulativeCounter = 0;
    while (w < NUM_WORKOUT_DAYS) {
      days.get(w).addFirstIntensity(highIntensityLabel);
      highIntensity--;
      cumulativeCounter += ((float) NUM_WORKOUT_DAYS/remainingHighInt);
      w = Math.toIntExact(Math.round(Math.floor(cumulativeCounter)));
    }

    this.fillInLow(days, lowIntensityLabel);
  }

  private void fillInLow(List<Day> days, String lowIntensityLabel) {
    for (Day day : days) {
      if (day.getNumberOfWorkouts() <= day.getIntensityLength()) {
        continue;
      }
      for (int i = 0; i <= day.getNumberOfWorkouts() - day.getIntensityLength(); i++) {
        day.addFirstIntensity(lowIntensityLabel);
      }
    }
  }
}
