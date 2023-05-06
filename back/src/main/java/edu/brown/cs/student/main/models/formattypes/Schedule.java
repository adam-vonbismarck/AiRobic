package edu.brown.cs.student.main.models.formattypes;

import com.squareup.moshi.Json;

import java.util.ArrayList;
import java.util.List;

/** Still ironing out format of these records. */
public record Schedule(
    @Json(name = "type") String type, @Json(name = "weeks") List<Week> weeks, Week example) {

  public int getLength() {
    int workoutCounter = 0;
    for (Week week : this.weeks) {
      for (Day day : week.days()) {
        workoutCounter += day.getNumberOfWorkouts();
      }
    }
    return workoutCounter;
  }

  public FlatSchedule flatten() {
    List<Day> days = new ArrayList<>();
    for (Week week : this.weeks()) {
      for (Day day : week.days()) {
        days.add(day.copy());
      }
    }
    return new FlatSchedule(days);
  }

}
