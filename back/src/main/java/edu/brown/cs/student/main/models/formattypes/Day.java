package edu.brown.cs.student.main.models.formattypes;

import com.squareup.moshi.Json;
import edu.brown.cs.student.main.models.markov.Emission;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Still ironing out format of these records.
 */
public class Day {

  private final String type;
  private final List<Emission> workouts;
  private Integer numberOfWorkouts;
  private final String name;
  private final List<String> intensity;
  private final List<String> subCategory;

  public Day(@Json(name="type") String type,
      @Json(name="workouts") List<Emission> workouts,
      @Json(name="num") Integer numberOfWorkouts,
      @Json(name="name") String name,
      @Json(name="intensity") List<String> intensity,
      @Json(name="subcategory") List<String> subCategory) {
    this.type = type;
    this.workouts = workouts;
    this.numberOfWorkouts = numberOfWorkouts;
    this.name = name;
    this.intensity = intensity;
    this.subCategory = subCategory;
  }

  public void incrementNumWorkouts() {
    this.numberOfWorkouts++;
  }

  public void addWorkout(Emission toAdd) {
    this.workouts.add(toAdd);
  }

  public Integer getNumberOfWorkouts() {
    return this.numberOfWorkouts;
  }

  public String getName() {
    return this.name;
  }

  public boolean verifyDay() {
    return (this.intensity.size() == 0 || this.intensity.size() == this.numberOfWorkouts) &&
        (this.subCategory.size() == 0 || this.subCategory.size() == this.numberOfWorkouts);
  }

  /**
   *
   * @return
   */
  public List<String> getIntensityCopy() {
    ArrayList<String> copy = new ArrayList<>();
    copy.addAll(this.intensity);
    return copy;
  }

  /**
   * This method adds a new workout intensity (standin for a workout). Will likely need to add
   * verification that the string is in a constant map of our workout data.
   *
   * @param toAdd - workout to add (intensity label)
   */
  public void addFirstIntensity(String toAdd) { this.intensity.add(0, toAdd); }

  /**
   * This method returns the current number of workouts, according to the intensity list.
   *
   * @return - the size of the intensity list.
   */
  public int getIntensityLength() { return this.intensity.size(); }

  @Override
  public String toString() {
    return "Day{" +
        "type='" + this.type + '\'' +
        ", workouts=" + this.workouts +
        ", numberOfWorkouts=" + this.numberOfWorkouts +
        ", name='" + this.name + '\'' +
        ", intensity=" + this.intensity +
        ", subCategory=" + this.subCategory +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Day day = (Day) o;
    return Objects.equals(this.type, day.type) && Objects.equals(this.workouts,
        day.workouts) && Objects.equals(this.numberOfWorkouts, day.numberOfWorkouts)
        && Objects.equals(this.name, day.name) && Objects.equals(this.intensity,
        day.intensity) && Objects.equals(this.subCategory, day.subCategory);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.type, this.workouts, this.numberOfWorkouts, this.name, this.intensity, this.subCategory);
  }
}
