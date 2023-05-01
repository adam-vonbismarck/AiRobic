package edu.brown.cs.student.main.models.markov;

import com.squareup.moshi.Json;
import edu.brown.cs.student.main.RandomGenerator;
import edu.brown.cs.student.main.models.exceptions.InvalidDistributionException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

/**
 * Still ironing out details of Emission class and HiddenState class.
 */
public class HiddenState {

  private final HashMap<HiddenState, Double> transitionDistribution;
  private HashMap<Emission, Double> emissionDistribution;
  private final String name;

  public HiddenState(@Json(name="category") String name,
      @Json(name = "transitions") HashMap<HiddenState, Double> transitionDistribution,
      @Json(name = "emissions") HashMap<Emission, Double> emissionDistribution) {
    this.transitionDistribution = transitionDistribution;
    this.emissionDistribution = emissionDistribution;
    this.name = name;
  }

  public Emission emit() throws InvalidDistributionException {
    RandomGenerator.validateDistribution(Emission.class, this.emissionDistribution);
    return RandomGenerator.generateRandomFromDistribution(Emission.class, this.emissionDistribution);
  }

  public HiddenState transition() throws InvalidDistributionException {
    RandomGenerator.validateDistribution(HiddenState.class, this.transitionDistribution);
    return RandomGenerator.generateRandomFromDistribution(HiddenState.class, this.transitionDistribution);
  }

  public Set<HiddenState> potentialStates() {
    return this.transitionDistribution.keySet();
  }

  public void addTransition(HiddenState state, Double prob) {
    this.transitionDistribution.put(state, prob);
    System.out.println(this.transitionDistribution);
  }

  public void fillTransitions(HashMap<HiddenState, Double> transitions)
      throws InvalidDistributionException {
    RandomGenerator.validateDistribution(HiddenState.class, transitions);
    this.transitionDistribution.clear();
    for (HiddenState key : transitions.keySet()) {
      this.transitionDistribution.put(key, transitions.get(key));
    }
  }

  public void addEmission(Emission emission, Double prob) {
    this.emissionDistribution.put(emission, prob);
  }

  public void fillEmissions(HashMap<Emission, Double> emissions)
      throws InvalidDistributionException {
    RandomGenerator.validateDistribution(Emission.class, emissions);
    this.emissionDistribution.clear();
    for (Emission key : emissions.keySet()) {
      this.emissionDistribution.put(key, emissions.get(key));
    }
  }

  public void checkDistributions() throws InvalidDistributionException {
    RandomGenerator.validateDistribution(HiddenState.class, this.transitionDistribution);
    RandomGenerator.validateDistribution(Emission.class, this.emissionDistribution);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    HiddenState that = (HiddenState) o;
    return Objects.equals(this.emissionDistribution, that.emissionDistribution)
        && Objects.equals(this.name, that.name);
  }

  // come back and add in emission dist
  @Override
  public String toString() {
    return "HiddenState{" + "name='" + this.name + '\'' +
        '}';
  }
}
