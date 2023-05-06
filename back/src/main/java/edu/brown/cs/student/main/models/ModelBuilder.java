package edu.brown.cs.student.main.models;

import edu.brown.cs.student.main.RandomGenerator;
import edu.brown.cs.student.main.models.exceptions.InvalidDistributionException;
import edu.brown.cs.student.main.models.markov.Emission;
import edu.brown.cs.student.main.models.markov.HiddenState;
import edu.brown.cs.student.main.models.markov.MarkovModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The ModelBuilder class contains code for building a MarkovModel, with helpful storage of
 * HiddenStates by a string name for easy generation.
 */
public class ModelBuilder {

  private final List<HiddenState> states;
  private final HashMap<String, HiddenState> nameMap;
  private final HashMap<HiddenState, Double> startDist;

  /** The constructor for the ModelBuilder class, which initializes all of its storage fields. */
  public ModelBuilder() {
    this.states = new ArrayList<>();
    this.nameMap = new HashMap<>();
    this.startDist = new HashMap<>();
  }

  /**
   * Given the current data in states, nameMap, and startDist, attempts to build a MarkovModel.
   *
   * @return the MarkovModel, if successful.
   * @throws InvalidDistributionException if there are any problems with any of the distributions
   *     associated with the new MarkovModel (including state distributions).
   */
  public MarkovModel build() throws InvalidDistributionException {

    // ensures start distribution, at least, contains all relevant states
    for (HiddenState state : this.states) {
      if (!this.startDist.containsKey(state)) {
        this.startDist.put(state, 0.0);
      }
    }

    return new MarkovModel(this.startDist);
  }

  /**
   * Generates a new HiddenState, and puts it into the nameMap.
   *
   * @param name
   * @return
   * @throws InvalidDistributionException
   */
  public HiddenState generateNewState(String name) throws InvalidDistributionException {
    HiddenState state = new HiddenState(name, new HashMap<>(), new HashMap<>());
    this.states.add(state);
    this.nameMap.put(name, state);
    return state;
  }

  // will continue commenting.

  public void setStateDistribution(String originName, HashMap<HiddenState, Double> potentialStates)
      throws InvalidDistributionException {
    RandomGenerator.validateDistribution(HiddenState.class, potentialStates);
    this.nameMap.get(originName).fillTransitions(potentialStates);
  }

  public void setStateDistribution(HiddenState origin, HashMap<HiddenState, Double> potentialStates)
      throws InvalidDistributionException {
    RandomGenerator.validateDistribution(HiddenState.class, potentialStates);
    origin.fillTransitions(potentialStates);
  }

  public void addTransition(String originName, String destName, Double prob) {
    this.nameMap.get(originName).addTransition(this.nameMap.get(destName), prob);
  }

  public void addTransition(HiddenState origin, HiddenState dest, Double prob) {
    origin.addTransition(dest, prob);
  }

  public void setEmissionDistribution(
      String originName, HashMap<Emission, Double> potentialEmissions)
      throws InvalidDistributionException {
    RandomGenerator.validateDistribution(Emission.class, potentialEmissions);
    this.nameMap.get(originName).fillEmissions(potentialEmissions);
  }

  public void setEmissionDistribution(
      HiddenState origin, HashMap<Emission, Double> potentialEmissions)
      throws InvalidDistributionException {
    RandomGenerator.validateDistribution(Emission.class, potentialEmissions);
    origin.fillEmissions(potentialEmissions);
  }

  public void addEmission(String originName, Emission emission, Double prob) {
    this.nameMap.get(originName).addEmission(emission, prob);
  }

  public void addEmission(HiddenState origin, Emission emission, Double prob) {
    origin.addEmission(emission, prob);
  }

  public void setStartDistribution(HashMap<HiddenState, Double> potentialStart)
      throws InvalidDistributionException {
    RandomGenerator.validateDistribution(HiddenState.class, potentialStart);
    this.startDist.clear();
    for (HiddenState key : potentialStart.keySet()) {
      this.startDist.put(key, potentialStart.get(key));
    }
  }

  public void addStartProbability(String stateName, Double prob) {
    this.startDist.put(this.nameMap.get(stateName), prob);
  }

  public void addStartProbability(HiddenState state, Double prob) {
    this.startDist.put(state, prob);
  }

  public void addLinearTransitions() throws InvalidDistributionException {
    for (int i = 0; i < this.states.size() - 1; i++) {
      HashMap<HiddenState, Double> transitionMat = new HashMap<>();
      transitionMat.put(this.states.get(i + 1), 1.0);
      for (HiddenState state : this.states) {
        if (!transitionMat.containsKey(state)) {
          transitionMat.put(state, 0.0);
        }
      }
      this.states.get(i).fillTransitions(transitionMat);
    }
    HashMap<HiddenState, Double> endTransitionMat = new HashMap<>();
    endTransitionMat.put(this.states.get(0), 1.0);
    for (HiddenState state : this.states) {
      if (!endTransitionMat.containsKey(state)) {
        endTransitionMat.put(state, 0.0);
      }
    }
    this.states.get(this.states.size() - 1).fillTransitions(endTransitionMat);
  }
}
