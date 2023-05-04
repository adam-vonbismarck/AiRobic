package edu.brown.cs.student.main.models.formattypes;

import com.squareup.moshi.Json;
import edu.brown.cs.student.main.models.formattypes.Day.WorkoutDescription;
import java.util.List;

/**
 * Still ironing out format of these records.
 */
public record Schedule(@Json(name="type") String type, @Json(name="weeks") List<Week> weeks,
                       @Json(name="exampleWeek") Week example) {

  public int getLength() {
    int workoutCounter = 0;
    for (Week week : this.weeks) {
      for (Day day : week.days()) {
        workoutCounter += day.getNumberOfWorkouts();
      }
    }
    return workoutCounter;
  }

}
