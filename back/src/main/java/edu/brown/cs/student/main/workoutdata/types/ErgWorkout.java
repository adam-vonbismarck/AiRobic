package edu.brown.cs.student.main.workoutdata.types;

import edu.brown.cs.student.main.models.markov.Emission;
import java.time.Duration;

public class ErgWorkout extends Emission {

  public ErgWorkout(String workout, double time, boolean completed, Duration split,
      double heartRate, double watts, double meters, int rpe) {
    super(workout, time, completed, heartRate, rpe);
  }

}
