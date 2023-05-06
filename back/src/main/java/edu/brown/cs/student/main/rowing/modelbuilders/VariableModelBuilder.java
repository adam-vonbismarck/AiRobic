package edu.brown.cs.student.main.rowing.modelbuilders;

import edu.brown.cs.student.main.server.RandomGenerator;
import edu.brown.cs.student.main.models.markov.modelbuilding.ModelBuilder;
import edu.brown.cs.student.main.models.exceptions.InvalidDistributionException;
import edu.brown.cs.student.main.models.exceptions.InvalidScheduleException;
import edu.brown.cs.student.main.models.exceptions.NoWorkoutTypeException;
import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.formattypes.Week;
import edu.brown.cs.student.main.models.markov.model.MarkovModel;
import edu.brown.cs.student.main.models.markov.modelbuilding.Workout;
import edu.brown.cs.student.main.rowing.distributiongenerators.RowingWorkoutByName;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The VariableModelBuilder class builds a MarkovModel where each hidden state is a particular workout type, with
 * its distribution read from our preconceived files or dynamically generated. Creates more randomness and less
 * structure in the plan.
 */
public class VariableModelBuilder {

  private RowingWorkoutByName dists;
  public static int MIN_LOW_LENGTH = 60;
  public static int HIGH_LENGTH = 60;
  public static int MAX_WORKOUTS_PER_WEEK = 10;
  public static int MAX_HIGH_WORKOUTS = 4;
  public static int MIN_MINUTES = 120;

  public static int MAX_MINUTES = 1200;

  /** The constructor for the VariableModelBuilder, which establishes the RowingWorkoutByName instance to get
   * emission distributions from Workout keys.
   */
  public VariableModelBuilder() throws IOException {
    this.dists = new RowingWorkoutByName();
  }

  /**
   * The build method takes in a set of high/low intensity workouts a number of minutes the caller has, and a
   * high intensity percentage, and builds a "variable" MarkovModel that accommodates these constraints to model
   * one week of training.
   *
   * @param lowWorkouts - the set of low intensity workouts that make up the low intensity states.
   * @param highWorkouts - the set of high intensity workouts that make up the high intensity states.
   * @param minutes - the number of minutes the caller has in their schedule per week, in order to modulate
   *                the length of low intensity sessions (similar process to that in the ScheduleBuilder).
   * @param highPercent - the percentage of high intensity work the caller wants to do, roughly (informs the
   *                    state transitions).
   * @return a completely built MarkovModel, ready for random generation.
   * @throws InvalidDistributionException if any distributions in the generation of the model do not
   *     sum to 1.
   * @throws InvalidScheduleException if the schedule to be built does not have enough information
   *     to build the model.
   */
  public MarkovModel build(
          Set<Workout> lowWorkouts, Set<Workout> highWorkouts, int minutes, double highPercent)
      throws InvalidDistributionException, InvalidScheduleException, NoWorkoutTypeException {

    if (lowWorkouts == null || lowWorkouts.size() == 0 ||
            highWorkouts == null || highWorkouts.size() == 0 ) {
      throw new NoWorkoutTypeException("Low/high intensity workout sets for variable model building were " +
              "null or contained no workout types.");
    }

    if (minutes < MIN_MINUTES || minutes > MAX_MINUTES) {
      throw new InvalidScheduleException("All schedules must have a number of minutes between 120 and 1200.",
              new Schedule("schedule", List.of(), new Week("week", List.of())));
    }

    RandomGenerator.validateDistribution(
            String.class,
            new HashMap<>() {
              {
                this.put("High workoutType", highPercent);
                this.put("Low workoutType", 1 - highPercent);
              }
            });

    ModelBuilder builder = new ModelBuilder();

    Set<Workout> allWorkouts = new HashSet<>();
    allWorkouts.addAll(lowWorkouts);
    allWorkouts.addAll(highWorkouts);

    double numLow = lowWorkouts.size();
    double numHigh = highWorkouts.size();

    // calculate high intensity Workout minutes, with a minimum of 60
    long highIntensity = Math.round(Math.max(highPercent * minutes, HIGH_LENGTH));

    // find the length of low workoutType workouts, with a minimum of 60 minute sessions, and a
    // maximum of 10 sessions per week
    int lowLength =
        Math.toIntExact(
            Math.max(
                MIN_LOW_LENGTH,
                (minutes - highIntensity)
                    / (MAX_WORKOUTS_PER_WEEK -
                        (Math.min(Math.floorDiv(highIntensity, HIGH_LENGTH), MAX_HIGH_WORKOUTS)))));

    // generates emission distribution for all high intensity workouts
    for (Workout wo : lowWorkouts) {
      builder.generateNewState(Workout.value(wo));
      builder.setEmissionDistribution(
          Workout.value(wo),
          this.dists.getEmissionDist(new Day.WorkoutDescription(wo, lowLength)));
    }

    // generates emission distribution for all low intensity workouts
    for (Workout wo : highWorkouts) {
      builder.generateNewState(Workout.value(wo));
      builder.setEmissionDistribution(
          Workout.value(wo), this.dists.getEmissionDist(new Day.WorkoutDescription(wo, 60)));
    }

    // generates transition distribution for all workouts
    for (Workout wo : allWorkouts) {
      for (Workout lowWo : lowWorkouts) {
        builder.addTransition(
            Workout.value(wo), Workout.value(lowWo), (1.0 - highPercent) / numLow);
      }
      for (Workout highWo : highWorkouts) {
        builder.addTransition(Workout.value(wo), Workout.value(highWo), (highPercent) / numHigh);
      }
    }

    // generates start probabilities for all workouts
    for (Workout lowWo : lowWorkouts) {
      builder.addStartProbability(Workout.value(lowWo), (1.0 - highPercent) / (numLow));
    }
    for (Workout highWo : highWorkouts) {
      builder.addStartProbability(Workout.value(highWo), (highPercent) / (numHigh));
    }

    return builder.build();
  }
}
