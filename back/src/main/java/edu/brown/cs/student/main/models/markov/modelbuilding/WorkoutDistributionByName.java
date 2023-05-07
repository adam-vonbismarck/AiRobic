package edu.brown.cs.student.main.models.markov.modelbuilding;

import com.squareup.moshi.Json;
import edu.brown.cs.student.main.models.exceptions.NoWorkoutTypeException;
import edu.brown.cs.student.main.models.markov.model.Emission;
import edu.brown.cs.student.main.server.serializing.Serializer;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The WorkoutDistributionByName class uses our preconceived workout data from the data folders in order to allow us
 * to query it for emission distributions. Contains code to deserialize these files and use Workout types to query
 * for emission distributions.
 */
public class WorkoutDistributionByName {

  private final WorkoutDistMap map;

  /**
   * The constructor for the WorkoutDistributionByName class, which takes in a file to deserialize.
   *
   * @param filename - the file to deserialize.
   * @throws IOException if deserializing fails.
   */
  public WorkoutDistributionByName(String filename) throws IOException {
    this.map = Serializer.getDeserializedResponse(WorkoutDistMap.class, filename);
  }

  /**
   * This method gets the emission distribution associated with a given Workout type.
   *
   * @param name - the Workout type to key on.
   * @return the found emission distribution.
   * @throws NoWorkoutTypeException if the workout type does not have an associated distribution in the read file.
   */
  public HashMap<Emission, Double> generateEmissionDistribution(Workout name)
      throws NoWorkoutTypeException {
    if (this.map.allData().containsKey(Workout.value(name))) {
      return this.map.getDist(Workout.value(name));
    }
    throw new NoWorkoutTypeException(
        "Type: "
            + Workout.value(name)
            + "was not found in the loaded set of workout"
            + "distributions.");
  }

  /**
   * A record for storing data read in from one of our workout files.
   *
   * @param allData - the map of all workout data in the file.
   */
  public record WorkoutDistMap(
      @Json(name = "categories") Map<String, List<EmissionAndProb>> allData) {

    /**
     * This method gets an emission distribution given a string key, if that key exists in the read file.
     *
     * @param key - the string key.
     * @return the emission distribution
     * @throws NoWorkoutTypeException if the key is not found in the stored file.
     */
    public HashMap<Emission, Double> getDist(String key) throws NoWorkoutTypeException {
      if (!this.allData.containsKey(key)) {
        throw new NoWorkoutTypeException(
                "Type: "
                        + key
                        + "was not found in the loaded set of workout"
                        + "distributions.");
      }

      HashMap<Emission, Double> dist = new HashMap<>();
      for (EmissionAndProb joined : this.allData.get(key)) {
        dist.put(joined.getEmission(), joined.probability());
      }
      return dist;
    }
  }

  /**
   * A record for storing an emission and its associated probability. We had trouble getting moshi
   * to deserialize the individual fields in the emission class, so we included the duration and title
   * of the given emission in this class as well.
   *
   * @param emission - an emission.
   * @param probability - the emission's associated probability in its distribution.
   * @param minutes - the length of the workout associated with the emission.
   */
  public record EmissionAndProb(
      @Json(name = "emission") Emission emission,
      @Json(name = "probability") double probability,
      @Json(name = "minutes") double minutes,
      @Json(name = "title") String title) {

    /**
     * Returns an emission linked to its duration (minutes).
     *
     * @return the new emission.
     */
    public Emission getEmission() {
      return this.emission.setTime(this.minutes).setTitle(this.title);
    }
  }
}
