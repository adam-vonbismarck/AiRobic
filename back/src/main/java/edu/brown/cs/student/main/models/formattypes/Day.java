package edu.brown.cs.student.main.models.formattypes;

import com.squareup.moshi.Json;
import edu.brown.cs.student.main.models.markov.Emission;
import java.util.List;

public record Day(@Json(name="type") String type,
                  @Json(name="workouts") List<Emission> workouts,
                  @Json(name="num") Integer numberOfWorkouts) {

}
