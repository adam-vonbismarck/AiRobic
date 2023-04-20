package edu.brown.cs.student.main.workoutdata.types;

import edu.brown.cs.student.main.models.markov.Emission;
import java.time.Duration;

public class ErgWorkout extends Emission {

  private final String workout;
  private final double time;
  private final boolean completed;
  private final double heartRate;
  private final int rpe;
  private final double watts;
  private final double meters;
  private final Duration split;

  public ErgWorkout(String workout, double time, boolean completed,
      double heartRate, int rpe, double watts, double meters, Duration split) {
    super(workout, time, completed, heartRate, rpe);

    this.workout = workout;
    this.time = time;
    this.completed = completed;
    this.heartRate = heartRate;
    this.rpe = rpe;
    this.watts = watts;
    this.meters = meters;
    this.split = split;
  }

  @Override
  public Emission copy() {
    return new ErgWorkout(this.workout, this.time, this.completed, this.heartRate, this.rpe,
        this.watts, this.meters, this.split);
  }
}
