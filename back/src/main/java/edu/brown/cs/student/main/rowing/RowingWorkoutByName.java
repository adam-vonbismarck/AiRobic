package edu.brown.cs.student.main.rowing;

import edu.brown.cs.student.main.models.WorkoutDistributionByName;
import edu.brown.cs.student.main.models.exceptions.NoWorkoutTypeException;
import edu.brown.cs.student.main.models.formattypes.Day.WorkoutDescription;
import edu.brown.cs.student.main.models.markov.Emission;
import java.io.IOException;
import java.util.HashMap;

public class RowingWorkoutByName {

  WorkoutDistributionByName goal;
  WorkoutDistributionByName noGoal;

  public RowingWorkoutByName() throws IOException {
    this.goal =
        new WorkoutDistributionByName(
            "./data/WorkoutDataGoal.json");
    this.noGoal =
        new WorkoutDistributionByName(
            "./data/WorkoutDataNoGoal.json");
  }

  public HashMap<Emission, Double> getEmissionDistGoal(WorkoutDescription name)
      throws NoWorkoutTypeException {
    try {
      return this.goal.generateEmissionDistribution(name);
    } catch (NoWorkoutTypeException e) {
      if (name.workoutType().equals(Workout.UT_2)) {
        return UT2DistributionGenerator.getLowIntensityDistributionByTime(name.minutes());
      }
      throw new NoWorkoutTypeException(e.getMessage());
    }
  }

  public HashMap<Emission, Double> getEmissionDistNoGoal(WorkoutDescription name)
      throws NoWorkoutTypeException {
    try {
      return this.noGoal.generateEmissionDistribution(name);
    } catch (NoWorkoutTypeException e) {
      if (name.workoutType().equals(Workout.UT_2)) {
        return UT2DistributionGenerator.getLowIntensityDistributionByTime(name.minutes());
      }
      throw new NoWorkoutTypeException(e.getMessage());
    }
  }
}
