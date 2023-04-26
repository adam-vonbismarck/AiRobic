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
  private final Integer dayNum;
  private List<String> intensity;
  private List<String> subCategory;

  public Day(@Json(name="type") String type,
      @Json(name="workouts") List<Emission> workouts,
      @Json(name="num") Integer numberOfWorkouts,
      @Json(name="daynumber") Integer dayNum,
      @Json(name="intensity") List<String> intensity,
      @Json(name="subcategory") List<String> subCategory) {
    this.type = type;
    this.workouts = workouts;
    this.numberOfWorkouts = numberOfWorkouts;
    this.dayNum = dayNum;
    this.intensity = intensity;
    this.subCategory = subCategory;
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

  public Integer dayNumber() {
    return this.dayNum;
  }

  public boolean verifyDay() {
    return (this.intensity.size() == 0 || this.intensity.size() == this.numberOfWorkouts) &&
        (this.subCategory.size() == 0 || this.subCategory.size() == this.numberOfWorkouts);
  }

  public List<String> getIntensity() {
    return this.intensity;
  }

}
