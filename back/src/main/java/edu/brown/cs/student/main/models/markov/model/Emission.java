package edu.brown.cs.student.main.models.markov.model;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonClass;
import java.util.Objects;

/**
 * The Emission class contains all workout data of a workout produced by a MarkovModel random
 * generation run.
 */
public class Emission {

  private final String workout;
  private final double time;

  /**
   * The constructor for the Emission class, which takes in a number of parameters representing a
   * workout.
   *
   * @param workout - the string description of the workout.
   * @param time - the time it takes to complete the workout.
   */
  @JsonClass(generateAdapter = true)
  public Emission(@Json(name = "workout") String workout, @Json(name = "minutes") double time) {
    this.workout = workout;
    this.time = time;
  }

  /**
   * Returns an exact copy of an Emission instance.
   *
   * @return the copied Emission.
   */
  public Emission copy() {
    return new Emission(this.workout, this.time);
  }

  /**
   * This method returns the length of the generated workout.
   *
   * @return the length (in minutes) of this workout.
   */
  public double getTime() {
    return this.time;
  }

  /**
   * Returns a copy of the current Emission with a new time.
   *
   * @param time - time of emission copy.
   * @return the copied emission.
   */
  public Emission setTime(double time) {
    return new Emission(this.workout, time);
  }

  /**
   * Returns the string description of the workout of this instance.
   *
   * @return the workout, as a string.
   */
  public String getWorkout() {
    return this.workout;
  }

  /**
   * Overridden equals method, so Emissions are compared by their fields.
   *
   * @param o - the object for comparison.
   * @return whether o is the same as this instance.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Emission emission = (Emission) o;
    return Objects.equals(this.workout, emission.workout)
        && Objects.equals(this.time, emission.time);
  }

  /**
   * Overridden hashCode method, so Emissions are hashed by their fields.
   *
   * @return the hashCode of this Emission.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.workout, this.time);
  }

  /**
   * Overridden toString method, so Emission.toString() returns a string with the relevant fields
   * displayed.
   *
   * @return the string version of this instance.
   */
  @Override
  public String toString() {
    return "Emission{" + "workout='" + this.workout + '\'' + ", time=" + this.time + '}';
  }
}
