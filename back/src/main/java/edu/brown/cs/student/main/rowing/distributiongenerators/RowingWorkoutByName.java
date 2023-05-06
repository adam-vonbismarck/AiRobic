package edu.brown.cs.student.main.rowing.distributiongenerators;

import edu.brown.cs.student.main.models.markov.modelbuilding.Workout;
import edu.brown.cs.student.main.models.markov.modelbuilding.WorkoutDistributionByName;
import edu.brown.cs.student.main.models.exceptions.NoWorkoutTypeException;
import edu.brown.cs.student.main.models.formattypes.Day.WorkoutDescription;
import edu.brown.cs.student.main.models.markov.model.Emission;
import java.io.IOException;
import java.util.HashMap;

/**
 * This class reads in the main rowing workout file, and uses the WorkoutDistributionByName class
 * to access emission distributions based on Workout keys.
 */
public class RowingWorkoutByName {
  WorkoutDistributionByName distributions;

  /**
   * The constructor for the RowingWorkoutByName class, which initializes a WorkoutDistributionByName class
   * with the rowing workout file.
   *
   * @throws IOException if the workout file cannot be read.
   */
  public RowingWorkoutByName() throws IOException {
    this.distributions = new WorkoutDistributionByName("./data/WorkoutData.json");
  }

  /**
   * This method returns an Emission distribution given a WorkoutDescription of a rowing workout. It special
   * cases UT2 rowing workouts, so that UT2 timing can be taken into account (duration within the WorkoutDescription).
   *
   * @param name - the type of Workout distribution wanted.
   * @return the stored distribution.
   * @throws NoWorkoutTypeException if the Workout type was not found in the rowing workout file.
   */
  public HashMap<Emission, Double> getEmissionDist(WorkoutDescription name)
      throws NoWorkoutTypeException {
    try {
      return this.distributions.generateEmissionDistribution(name.workoutType());
    } catch (NoWorkoutTypeException e) {
      // special case: UT2 distributions are dynamically generated. A showcase of how distributions could either
      // be stored statically in a file or generated dynamically based on constraints passed in by a larger wrapper
      // class, like WorkoutDescription.
      if (name.workoutType().equals(Workout.UT_2)) {
        return UT2DistributionGenerator.getLowIntensityDistributionByTime(name.minutes());
      }
      throw new NoWorkoutTypeException(e.getMessage());
    }
  }
}
