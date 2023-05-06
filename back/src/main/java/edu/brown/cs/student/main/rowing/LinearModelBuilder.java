package edu.brown.cs.student.main.rowing;

import edu.brown.cs.student.main.models.ModelBuilder;
import edu.brown.cs.student.main.models.exceptions.InvalidDistributionException;
import edu.brown.cs.student.main.models.exceptions.InvalidScheduleException;
import edu.brown.cs.student.main.models.exceptions.NoWorkoutTypeException;
import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.Day.WorkoutDescription;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.markov.MarkovModel;
import java.io.IOException;
import java.time.DayOfWeek;

/**
 * The LinearModelBuilder class contains a build method that takes in a schedule and a start day,
 * and builds a MarkovModel where each hidden state represents a workout time within the week. Each
 * MarkovModel the LinearModelBuilder builds has enough hidden states to represent a full week of
 * training (as a loop).
 */
public class LinearModelBuilder {

  private final RowingWorkoutByName dists;

  /**
   * The constructor for the LinearModelBuilder class, which loads a RowingWorkoutByName instance
   * that will get an emission distribution given a key name (that represents what category of
   * workout the hidden state should emit).
   */
  public LinearModelBuilder() throws IOException {
    this.dists = new RowingWorkoutByName();
  }

  /**
   * The build method takes in a schedule and a start day, and builds a "linear" MarkovModel to
   * model one week of training.
   *
   * @param schedule - the framework for the schedule that the LinearModelBuilder should build.
   * @param startDay - the point within the week the model should start from.
   * @return a completely built MarkovModel, ready for random generation.
   * @throws InvalidDistributionException if any distributions in the generation of the model do not
   *     sum to 1.
   * @throws InvalidScheduleException if the schedule to be built does not have enough information
   *     to build the model.
   */
  public MarkovModel build(Schedule schedule, DayOfWeek startDay)
      throws InvalidDistributionException, InvalidScheduleException, NoWorkoutTypeException {
    ModelBuilder builder = new ModelBuilder();

    // build up hidden states, with emission distributions based on provided key in the schedule
    // framework
    for (Day day : schedule.example().days()) {
      if (!day.verifyDay()) {
        throw new InvalidScheduleException(
            "Day workout numbers and workoutType labels do not match: " + day, schedule);
      }
      int workoutCounter = 0;
      for (WorkoutDescription intensity : day.getPlanCopy()) {
        builder.generateNewState(this.encodeDay(day.getDay(), workoutCounter));
        builder.setEmissionDistribution(
            this.encodeDay(day.getDay(), workoutCounter),
            this.dists.getEmissionDist(intensity));
      }
    }

    // add linear state transitions to each hidden state
    builder.addLinearTransitions();

    // make sure start day is in keyset of days to ints?

    builder.addStartProbability(this.encodeDay(this.findFirstWorkout(startDay, schedule), 0), 1.0);

    return builder.build();
  }

  /**
   * This method encodes a day and a workout number of that day into a name for a given HiddenState.
   *
   * @param day - the day of the state to be encoded.
   * @param counter - the workout number on that day to be encoded.
   * @return - the encoded HiddenState name.
   */
  private String encodeDay(DayOfWeek day, int counter) {
    return "Day: " + day.toString() + ", workout: " + counter;
  }

  /**
   * This helper method finds the first day of the week with a workout, for the purposes of finding the first
   * potential state in a linear model.
   *
   * @param startDay - the start day of the week of the potential schedule.
   * @param schedule - the schedule to check.
   * @return the DayOfWeek of the first workout of the schedule.
   * @throws InvalidScheduleException if the schedule has no workouts.
   */
  private DayOfWeek findFirstWorkout(DayOfWeek startDay, Schedule schedule) throws InvalidScheduleException {
    for (Day day : schedule.example().days()) {
      if (day.getDay().getValue() < startDay.getValue()) {
        continue;
      }
      if (day.getNumberOfWorkouts() > 0) {
        return day.getDay();
      }
    }
    throw new InvalidScheduleException("Schedule had no workouts, so no model could be built.", schedule);
  }


}
