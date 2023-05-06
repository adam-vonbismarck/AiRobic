package edu.brown.cs.student.main.models.formattypes;

import com.squareup.moshi.Json;
import edu.brown.cs.student.main.models.exceptions.InvalidScheduleException;

import java.util.ArrayList;
import java.util.List;

/**
 * The Schedule record models a given workout schedule, with a list of weeks, each of which contains a list of days.
 * It contains an example week for modelling purposes, but this field need not always be used.
 */
public record Schedule(
    String type, List<Week> weeks, Week example) {

  /**
   * Gets the total number of workouts in the schedule.
   *
   * @return the total number of workouts in the schedule.
   */
  public int getLength() {
    int workoutCounter = 0;
    for (Week week : this.weeks) {
      for (Day day : week.days()) {
        workoutCounter += day.getNumberOfWorkouts();
      }
    }
    return workoutCounter;
  }

  /**
   * Converts this schedule into a FlatSchedule by getting rid of the intermediary Week class.
   *
   * @return the converted FlatSchedule.
   */
  public FlatSchedule flatten() throws InvalidScheduleException {
    List<Day> days = new ArrayList<>();
    for (Week week : this.weeks) {
      for (Day day : week.days()) {
        days.add(day.copy());
      }
    }
    return new FlatSchedule(days);
  }

}
