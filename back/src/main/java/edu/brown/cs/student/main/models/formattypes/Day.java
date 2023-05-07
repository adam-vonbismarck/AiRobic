package edu.brown.cs.student.main.models.formattypes;

import edu.brown.cs.student.main.models.exceptions.InvalidScheduleException;
import edu.brown.cs.student.main.models.markov.model.Emission;
import edu.brown.cs.student.main.models.markov.modelbuilding.Workout;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The Day class, which models a singular day in a workout program. This class has the basic set of
 * fields we expect a workout plan to need, but more could be added by extending this class for a
 * particular sport. This class has many accessors and mutators, which we would normally consider
 * poor design, but we are merely using this class as an intermediary before serialization (we do
 * not expect to compute with any of its fields in a meaningful way).
 */
public class Day {

  private final String type;
  private final List<Emission> workouts;
  private Integer numberOfWorkouts;
  private final DayOfWeek day;
  private final List<WorkoutDescription> workoutPlan;
  private Optional<LocalDate> date;

  /**
   * The constructor for the Day class, which takes in a number of parameters modelling a given day
   * in a workout plan.
   *
   * @param type - the type of this object (when serialized), usually day (but could be changed for
   *     extensibility purposes on the front end).
   * @param workouts - the list of workouts on this given day, after a model run.
   * @param numberOfWorkouts - the number of workouts this day has.
   * @param day - the day of the week that this instance models.
   * @param date - the date of this instance.
   * @param workoutPlan - good for making linear models, this list models the types of workout that
   *     should occur on this day.
   * @throws InvalidScheduleException if the day is initialized with unexpected null fields.
   */
  public Day(
      String type,
      List<Emission> workouts,
      int numberOfWorkouts,
      DayOfWeek day,
      Optional<LocalDate> date,
      List<WorkoutDescription> workoutPlan)
      throws InvalidScheduleException {
    this.type = type;
    this.workouts = workouts;
    this.numberOfWorkouts = numberOfWorkouts;
    this.day = day;
    this.workoutPlan = workoutPlan;
    this.date = date;

    if (type == null
        || workouts == null
        || day == null
        || workoutPlan == null
        || date == null
        || numberOfWorkouts < 0) {
      throw new InvalidScheduleException(
          "Day was initialized with bad parameters.",
          new Schedule(
              "schedule", List.of(new Week("week", List.of(this))), new Week("week", List.of())));
    }

    if (date.isPresent() && date.get().getDayOfWeek() != day) {
      throw new InvalidScheduleException(
          "Day was initialized with date not matching the day of the week.",
          new Schedule(
              "schedule", List.of(new Week("week", List.of(this))), new Week("week", List.of())));
    }
  }

  /**
   * This method copies the current Day instance into a new Day instance, for defensive programming
   * purposes.
   *
   * @return the copied Day.
   */
  public Day copy() throws InvalidScheduleException {
    return new Day(
        this.type,
        this.getEmissions(),
        this.numberOfWorkouts,
        this.day,
        this.date,
        this.getPlanCopy());
  }

  /**
   * This method returns a defensive copy of the emissions list.
   *
   * @return the defensive copy.
   */
  public List<Emission> getEmissions() {
    List<Emission> copied = new ArrayList<>();
    for (Emission emission : this.workouts) {
      copied.add(emission.copy());
    }
    return copied;
  }

  /**
   * Returns a defensive copy of the optional date object.
   *
   * @return the copied date.
   */
  public Optional<LocalDate> getDate() {
    return this.date.isEmpty() ? Optional.empty() : Optional.of(this.date.get());
  }

  /** This method increments the number of workouts on this Day. */
  public void incrementNumWorkouts() {
    this.numberOfWorkouts++;
  }

  /**
   * This method adds a new workout to the completed workouts list (for use during model
   * translation).
   *
   * @param toAdd - the workout to add.
   */
  public void addWorkout(Emission toAdd) {
    this.workouts.add(toAdd);
  }

  /**
   * This method returns the number of workouts currently slated for this day.
   *
   * @return the number of workouts.
   */
  public int getNumberOfWorkouts() {
    return this.numberOfWorkouts;
  }

  /**
   * This method returns the day of the week of this Day.
   *
   * @return the day of the week as a DayOfWeek enum value.
   */
  public DayOfWeek getDay() {
    return this.day;
  }

  /**
   * This method verifies that the plan for a given day, given the intensities, aligns with the
   * number of workouts listed. Useful for defensive programming, and can be called to ensure that a
   * Day has the expected values before using it to create or store model results.
   *
   * @return if the day has the same number of workouts as it is planned to.
   */
  public boolean verifyDay() {
    return (this.workoutPlan.size() == 0 || this.workoutPlan.size() == this.numberOfWorkouts);
  }

  /**
   * This method sets the date of this Day instance.
   *
   * @param date - the newly set date of this Day instance.
   */
  public void setDate(LocalDate date) throws InvalidScheduleException {
    if (date.getDayOfWeek() != this.day) {
      throw new InvalidScheduleException(
          "Date did not match this Day's day of the week.",
          new Schedule(
              "schedule", List.of(new Week("week", List.of(this))), new Week("week", List.of())));
    }
    this.date = Optional.of(date);
  }

  /**
   * This method returns a copied list of the workout plans for this day.
   *
   * @return - the copied list.
   */
  public List<WorkoutDescription> getPlanCopy() {
    List<WorkoutDescription> workoutDescriptions = new ArrayList<>();
    for (WorkoutDescription wd : this.workoutPlan) {
      workoutDescriptions.add(wd.copy());
    }
    return workoutDescriptions;
  }

  /**
   * This method adds a new workout workoutType (standin for a workout). Will likely need to add
   * verification that the string is in a constant map of our workout data.
   *
   * @param toAdd - workout to add (workoutType label)
   */
  public void addFirstIntensity(WorkoutDescription toAdd) {
    this.workoutPlan.add(0, toAdd);
  }

  /**
   * This method returns the current number of workouts, according to the workoutType list.
   *
   * @return - the size of the workoutType list.
   */
  public int getIntensityLength() {
    return this.workoutPlan.size();
  }

  /**
   * Overridden toString method so that all fields are visible.
   *
   * @return the String version of this class.
   */
  @Override
  public String toString() {
    return "Day{"
        + "type='"
        + this.type
        + '\''
        + ", workouts="
        + this.workouts
        + ", numberOfWorkouts="
        + this.numberOfWorkouts
        + ", name="
        + this.day
        + ", workoutType="
        + this.workoutPlan
        + ", date="
        + this.date
        + '}';
  }

  /**
   * Overridden equals method so that Day instances are compared on the basis of their fields.
   *
   * @param o - the object to check equality with.
   * @return if o is equal to this instance.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    Day day = (Day) o;
    return Objects.equals(this.type, day.type)
        && Objects.equals(this.workouts, day.workouts)
        && Objects.equals(this.numberOfWorkouts, day.numberOfWorkouts)
        && this.day == day.day
        && Objects.equals(this.workoutPlan, day.workoutPlan)
        && Objects.equals(this.date, day.date);
  }

  /**
   * Overridden hashCode method so that Day instances are hashed on the basis of their fields.
   *
   * @return the hashCode of this Day instance.
   */
  @Override
  public int hashCode() {
    return Objects.hash(
        this.type, this.workouts, this.numberOfWorkouts, this.day, this.workoutPlan);
  }

  /**
   * A record for describing a certain workout in the workoutPlan field.
   *
   * @param workoutType - the type of given workout in the plan (useful for getting static emission
   *     distributions, as this enum aids).
   * @param minutes - the length of the specified workout, if applicable.
   */
  public record WorkoutDescription(Workout workoutType, int minutes) {

    /**
     * This method returns a copy of a given WorkoutDescription.
     *
     * @return the copy.
     */
    public WorkoutDescription copy() {
      return new WorkoutDescription(this.workoutType, this.minutes);
    }
  }
}
