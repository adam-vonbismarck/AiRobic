package edu.brown.cs.student.main.rowing;

import edu.brown.cs.student.main.models.ModelBuilder;
import edu.brown.cs.student.main.models.exceptions.InvalidDistributionException;
import edu.brown.cs.student.main.models.exceptions.InvalidScheduleException;
import edu.brown.cs.student.main.models.exceptions.NoWorkoutTypeException;
import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.markov.MarkovModel;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class VariableModelBuilder {

  private RowingWorkoutByName dists;

  /** The constructor for the VariableModelBuilder... */
  public VariableModelBuilder() throws IOException {
    this.dists = new RowingWorkoutByName();
  }

  /**
   * The build method takes in: , and builds a "variable" MarkovModel to model one week of training.
   *
   * @return a completely built MarkovModel, ready for random generation.
   * @throws InvalidDistributionException if any distributions in the generation of the model do not
   *     sum to 1.
   * @throws InvalidScheduleException if the schedule to be built does not have enough information
   *     to build the model.
   */
  public MarkovModel build(
      Set<Workout> lowWorkouts, Set<Workout> highWorkouts, int minutes, double highPercent)
      throws InvalidDistributionException, InvalidScheduleException, NoWorkoutTypeException {
    ModelBuilder builder = new ModelBuilder();

    Set<Workout> allWorkouts = new HashSet<>();
    allWorkouts.addAll(lowWorkouts);
    allWorkouts.addAll(highWorkouts);

    double numLow = lowWorkouts.size();
    double numHigh = highWorkouts.size();

    // calculate high intensity minutes, with a minimum of 60
    long highIntensity = Math.round(Math.max(highPercent * minutes, 60));

    // find the length of low intensity workouts, with a minimum of 60 minute sessions, and a
    // maximum
    // of 10 sessions per week
    int lowLength =
        Math.toIntExact(
            Math.max(
                60,
                (minutes - highIntensity)
                    / (10 - (Math.min(Math.floorDiv(highIntensity, 60), 4)))));

    for (Workout wo : lowWorkouts) {
      builder.generateNewState(Workout.value(wo));
      builder.setEmissionDistribution(
          Workout.value(wo),
          this.dists.getEmissionDistNoGoal(new Day.WorkoutDescription(wo, lowLength)));
    }

    for (Workout wo : highWorkouts) {
      builder.generateNewState(Workout.value(wo));
      builder.setEmissionDistribution(
          Workout.value(wo), this.dists.getEmissionDistNoGoal(new Day.WorkoutDescription(wo, 60)));
    }

    for (Workout wo : allWorkouts) {
      for (Workout lowWo : lowWorkouts) {
        builder.addTransition(
            Workout.value(wo), Workout.value(lowWo), (1.0 - highPercent) / numLow);
      }
      for (Workout highWo : highWorkouts) {
        builder.addTransition(Workout.value(wo), Workout.value(highWo), (highPercent) / numHigh);
      }
    }

    for (Workout lowWo : lowWorkouts) {
      builder.addStartProbability(Workout.value(lowWo), (1.0 - highPercent) / (numLow));
    }
    for (Workout highWo : highWorkouts) {
      builder.addStartProbability(Workout.value(highWo), (highPercent) / (numHigh));
    }

    return builder.build();
  }
}
