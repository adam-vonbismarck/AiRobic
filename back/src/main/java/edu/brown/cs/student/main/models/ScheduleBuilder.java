package edu.brown.cs.student.main.models;

import com.beust.ah.A;
import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.formattypes.Week;
import java.util.ArrayList;

public class ScheduleBuilder {

  public static int NUM_DAYS = 7;
  public static int NUM_WORKOUT_DAYS = 6;

  public Schedule minutes(int minutes, int numWeeks, double highPercent) {
    long highIntensity = Math.round(Math.max(highPercent*minutes, 60));
    long lowIntensity = minutes - highIntensity;
    long numHighIntensity = Math.floorDiv(highIntensity, 60);
    long lowIntensityWorkoutLength = Math.min(60, lowIntensity/(10 - numHighIntensity));
    long numLowIntensity = Math.floorDiv(lowIntensity, lowIntensityWorkoutLength);
    return this.workouts(numHighIntensity, numLowIntensity, numWeeks);
  }

  public Schedule workouts(long highIntensity, long lowIntensity, int numWeeks) {
    long workouts = highIntensity + lowIntensity;
    int dayCounter = 0;
    ArrayList<Week> weeks = new ArrayList<>();
    for (int i = 0; i < numWeeks; i++) {
      Week newWeek = new Week("week", new ArrayList<>());
      weeks.add(newWeek);
      for (int j = 0; j < NUM_DAYS; j++) {
        newWeek.days().add(new Day("day", new ArrayList<>(), 0, dayCounter));
        dayCounter++;
      }

      if (workouts <= 0) {
        continue;
      }

      while (workouts > NUM_WORKOUT_DAYS - 1) {
        for (int k = 0; k < NUM_WORKOUT_DAYS; k++) {
          newWeek.days().get(k).incrementNumWorkouts();
        }
      }

      for (int w = 0; w < NUM_WORKOUT_DAYS; w += Math.floorDiv(NUM_WORKOUT_DAYS, workouts)) {
        newWeek.days().get(w).incrementNumWorkouts();
      }

    }
    return new Schedule("schedule", weeks);
  }

}
