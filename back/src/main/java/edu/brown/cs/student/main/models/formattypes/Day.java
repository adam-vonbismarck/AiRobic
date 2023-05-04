package edu.brown.cs.student.main.models.formattypes;

import com.squareup.moshi.Json;
import edu.brown.cs.student.main.models.markov.Emission;
import edu.brown.cs.student.main.rowing.Workout;
import java.time.DayOfWeek;
import java.time.LocalDate;
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
  private final DayOfWeek name;
  private final List<WorkoutDescription> intensity;
  private LocalDate date;

  public Day(@Json(name="type") String type,
      @Json(name="workouts") List<Emission> workouts,
      @Json(name="num") Integer numberOfWorkouts,
      @Json(name="name") DayOfWeek name,
      @Json(name="date") LocalDate date,
      @Json(name="intensity") List<WorkoutDescription> intensity) {
    this.type = type;
    this.workouts = workouts;
    this.numberOfWorkouts = numberOfWorkouts;
    this.name = name;
    this.intensity = intensity;
    this.date = date;
  }

  public Day copy() {
    ArrayList<Emission> newWorkouts = new ArrayList<>();
    for (Emission emission : this.workouts) {
      newWorkouts.add(emission.copy());
    }
    return new Day(this.type, newWorkouts, this.numberOfWorkouts, this.name,
            (this.date != null) ? this.date.plusDays(0) : null,
        new ArrayList<>(this.intensity));
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

  public DayOfWeek getName() {
    return this.name;
  }

  public boolean verifyDay() {
    return (this.intensity.size() == 0 || this.intensity.size() == this.numberOfWorkouts);
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  /**
   *
   * @return
   */
  public List<WorkoutDescription> getIntensityCopy() {
    return new ArrayList<>(this.intensity);
  }

  /**
   * This method adds a new workout intensity (standin for a workout). Will likely need to add
   * verification that the string is in a constant map of our workout data.
   *
   * @param toAdd - workout to add (intensity label)
   */
  public void addFirstIntensity(WorkoutDescription toAdd) { this.intensity.add(0, toAdd); }

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
        ", name=" + this.name +
        ", intensity=" + this.intensity +
        ", date=" + this.date +
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
        day.intensity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.type, this.workouts, this.numberOfWorkouts, this.name, this.intensity);
  }

  public record WorkoutDescription(@Json(name="intensity") Workout intensity,
                                   @Json(name="length") Integer minutes) {

  }
}
