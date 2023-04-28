package edu.brown.cs.student.main.models.markov;

import com.squareup.moshi.Json;

/**
 * Still ironing out details of Emission class and HiddenState class.
 */
public class Emission {

  public Emission(@Json(name = "workout") String workout,
      @Json(name="minutes") double time,
      @Json(name="completion") boolean completed,
      @Json(name="HR") double heartRate,
      @Json(name="RPE") int rpe) {

  }

  public Emission copy() {
    return null;
  }

  //getWorkout
  //getTotalTime
  //etc.
}
