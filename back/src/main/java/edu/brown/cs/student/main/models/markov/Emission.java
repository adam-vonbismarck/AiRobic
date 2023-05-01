package edu.brown.cs.student.main.models.markov;

import com.squareup.moshi.Json;
import java.util.Objects;

/**
 * Still ironing out details of Emission class and HiddenState class.
 */
public class Emission {

  private final String workout;
  private final Double time;
  private boolean completed;
  private Double heartRate;
  private Integer rpe;

  public Emission(@Json(name = "workout") String workout,
      @Json(name="minutes") Double time,
      @Json(name="completion") boolean completed,
      @Json(name="HR") Double heartRate,
      @Json(name="RPE") Integer rpe) {
    this.workout = workout;
    this.time = time;
    this.completed = completed;
    this.heartRate = heartRate;
    this.rpe = rpe;
  }

  public Emission copy() {
    return new Emission(this.workout, this.time, this.completed,
        this.heartRate, this.rpe);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Emission emission = (Emission) o;
    return this.completed == emission.completed && Objects.equals(this.workout, emission.workout)
        && Objects.equals(this.time, emission.time) && Objects.equals(this.heartRate,
        emission.heartRate) && Objects.equals(this.rpe, emission.rpe);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.workout, this.time, this.completed, this.heartRate, this.rpe);
  }

  @Override
  public String toString() {
    return "Emission{" +
        "workout='" + workout + '\'' +
        ", time=" + time +
        ", completed=" + completed +
        ", heartRate=" + heartRate +
        ", rpe=" + rpe +
        '}';
  }

  //getWorkout
  //getTotalTime
  //etc.
}
