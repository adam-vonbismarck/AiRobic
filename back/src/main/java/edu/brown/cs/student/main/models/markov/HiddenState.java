package edu.brown.cs.student.main.models.markov;

import com.squareup.moshi.Json;
import edu.brown.cs.student.main.RandomGenerator;
import java.util.HashMap;
import java.util.Set;

public class HiddenState {

  private final HashMap<HiddenState, Double> transitionDistribution;
  private final HashMap<Emission, Double> emissionDistribution;

  public HiddenState(@Json(name = "transitions") HashMap<HiddenState, Double> transitionDistribution,
      @Json(name = "emissions") HashMap<Emission, Double> emissionDistribution) throws InvalidDistributionException {
    this.transitionDistribution = transitionDistribution;
    this.emissionDistribution = emissionDistribution;
    RandomGenerator.validateDistribution(HiddenState.class, transitionDistribution);
    RandomGenerator.validateDistribution(Emission.class, emissionDistribution);
  }

  public Emission emit() throws InvalidDistributionException {
    return RandomGenerator.generateRandomFromDistribution(Emission.class, this.emissionDistribution);
  }

  public HiddenState transition() throws InvalidDistributionException {
    return RandomGenerator.generateRandomFromDistribution(HiddenState.class, this.transitionDistribution);
  }

  public Set<HiddenState> potentialStates() {
    return this.transitionDistribution.keySet();
  }

}
