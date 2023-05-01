package edu.brown.cs.student.main.models;

import com.squareup.moshi.Json;
import edu.brown.cs.student.main.models.formattypes.Day.WorkoutDescription;
import edu.brown.cs.student.main.models.markov.Emission;
import edu.brown.cs.student.main.server.Serializer;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hoping to make this class more generic, and will document once that is complete.
 */
public class WorkoutDistributionByName {

  private final WorkoutDistMap map;

  public WorkoutDistributionByName() throws IOException {
    this.map = Serializer.getDeserializedResponse(WorkoutDistMap.class,
        "./src/main/java/edu/brown/cs/student/main/workoutdata/data/WorkoutDataGoal.json");
  }

  public HashMap<Emission, Double> generateEmissionDistribution(WorkoutDescription name) {
    if (this.map.allData().containsKey(name.intensity())) {
      return this.map.getDist(name.intensity());
    }
    if (name.intensity().equals("UT2")) {
      return WorkoutDistributionByTime.getLowIntensityDistributionByTime(name.minutes());
    }
    return null;
  }

  public record WorkoutDistMap(@Json(name="categories") Map<String, List<EmissionAndProb>> allData) {
    public HashMap<Emission, Double> getDist(String key) {

      HashMap<Emission, Double> dist = new HashMap<>();
      for (EmissionAndProb joined : this.allData.get(key)) {
        dist.put(joined.emission(), joined.probability());
      }
      return dist;
    }

  }

  public record EmissionAndProb(@Json(name="emission") Emission emission,
                                @Json(name="probability") Double probability) {

  }

}
