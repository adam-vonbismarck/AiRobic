package edu.brown.cs.student.main.models;

import com.squareup.moshi.Json;
import edu.brown.cs.student.main.models.formattypes.Day.WorkoutDescription;
import edu.brown.cs.student.main.models.markov.Emission;
import edu.brown.cs.student.main.server.Serializer;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class WorkoutDistributionByName {

  private final WorkoutDistMap map;

  public WorkoutDistributionByName() throws IOException {
    this.map = Serializer.getDeserializedResponse(WorkoutDistMap.class,
        "./main/workoutdata/data/WorkoutData.json");
  }

  public HashMap<Emission, Double> generateEmissionDistribution(WorkoutDescription name) {
    if (this.map.allData().containsKey(name.intensity())) {
      return this.map.allData().get(name.intensity()).getDist();
    }
    if (name.equals("UT2")) {
      return WorkoutDistributionByTime.getLowIntensityDistributionByTime(name.minutes());
    }
    return null;
  }

  public record WorkoutDistMap(@Json(name="categories") HashMap<String, WorkoutDistribution> allData) {

  }

  public record WorkoutDistribution(@Json(name="distribution") List<EmissionAndProb> emissions) {

    public HashMap<Emission, Double> getDist() {
      HashMap<Emission, Double> dist = new HashMap<>();
      for (EmissionAndProb joined : this.emissions) {
        dist.put(joined.emission(), joined.probability());
      }
      return dist;
    }

  }

  public record EmissionAndProb(@Json(name="emission") Emission emission,
                                @Json(name="probability") Double probability) {

  }

}
