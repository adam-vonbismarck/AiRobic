package edu.brown.cs.student.main.models.formattypes;

import com.squareup.moshi.Json;
import edu.brown.cs.student.main.models.markov.Emission;
import java.util.List;

/**
 * Still ironing out format of these records.
 */
public class Day {

  private final String type;
  private final List<Emission> workouts;
  private Integer numberOfWorkouts;

  public Day(@Json(name="type") String type,
      @Json(name="workouts") List<Emission> workouts,
      @Json(name="num") Integer numberOfWorkouts) {
    this.type = type;
    this.workouts = workouts;
    this.numberOfWorkouts = numberOfWorkouts;
  }

  public void incrementNumWorkouts() {
    this.numberOfWorkouts++;
  }

  public void addWorkout(Emission toAdd) {
    this.workouts.add(toAdd);
  }

  public Integer numberOfWorkouts() {
    return this.numberOfWorkouts;
  }

}
