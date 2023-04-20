package edu.brown.cs.student.main.models.markov;

import com.squareup.moshi.Json;
import java.time.Duration;

public abstract class Emission {

  public Emission(@Json(name = "workout") String workout,
      @Json(name="minutes") double time,
      @Json(name="completion") boolean completed,
      @Json(name="HR") double heartRate,
      @Json(name="RPE") int rpe) {

  }

  public abstract Emission copy();

  //getWorkout
  //getTotalTime
  //etc.
}
