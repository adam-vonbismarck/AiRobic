package edu.brown.cs.student.main.models.markov.modelbuilding;

import edu.brown.cs.student.main.models.exceptions.InvalidDistributionException;
import edu.brown.cs.student.main.models.markov.model.Emission;
import edu.brown.cs.student.main.models.markov.model.HiddenState;
import edu.brown.cs.student.main.models.markov.model.MarkovModel;
import edu.brown.cs.student.main.server.RandomGenerator;
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
   * @param name - the name of the new state.
   * @return an instance of the newly generated state.
   * @throws InvalidDistributionException if either of the Maps passed into HiddenState are null,
   *     which they never should be.
   */
  public HiddenState generateNewState(String name) throws InvalidDistributionException {
    HiddenState state = new HiddenState(name, new HashMap<>(), new HashMap<>());
    this.states.add(state);
    this.nameMap.put(name, state);
    return state;
  }

  /**
   * This method sets the transition distribution of a given state.
   *
   * @param originName - string name of the state whose transitions should be set.
   * @param potentialStates - the new transition distribution.
   * @throws InvalidDistributionException if the transition distribution is not valid.
   */
  public void setStateDistribution(String originName, HashMap<HiddenState, Double> potentialStates)
      throws InvalidDistributionException {
    if (this.nameMap.get(originName) == null) {
      throw new InvalidDistributionException(
          "State: " + originName + "was not a registered state " + "to add transitions to.",
          potentialStates);
    }
    RandomGenerator.validateDistribution(HiddenState.class, potentialStates);
    this.nameMap.get(originName).fillTransitions(potentialStates);
  }

  /**
   * This method sets the transition distribution of a given state.
   *
   * @param origin - the instance of the state whose transitions should be set.
   * @param potentialStates - the new transition distribution.
   * @throws InvalidDistributionException if the transition distribution is not valid.
   */
  public void setStateDistribution(HiddenState origin, HashMap<HiddenState, Double> potentialStates)
      throws InvalidDistributionException {
    RandomGenerator.validateDistribution(HiddenState.class, potentialStates);
    origin.fillTransitions(potentialStates);
  }

  /**
   * This method adds a transition to a state's transition distribution.
   *
   * @param originName - the name of the state whose distribution should be modified.
   * @param destName - the name of the state for the new transition.
   * @param prob - the probability of transitioning to the destination.
   * @throws InvalidDistributionException if either state names were not registered in the map.
   */
  public void addTransition(String originName, String destName, Double prob)
      throws InvalidDistributionException {
    if (this.nameMap.get(originName) == null) {
      throw new InvalidDistributionException(
          "State: " + originName + "was not a registered state " + "to add a transition to.",
          new HashMap());
    }
    if (this.nameMap.get(destName) == null) {
      throw new InvalidDistributionException(
          "State: " + destName + "was not a registered state " + "to add a transition to.",
          new HashMap());
    }
    this.nameMap.get(originName).addTransition(this.nameMap.get(destName), prob);
  }

  /**
   * This method adds a transition to a state's transition distribution.
   *
   * @param origin - the instance of the state whose distribution should be modified.
   * @param dest - the instance of the state for the new transition.
   * @param prob - the probability of transitioning to the destination.
   */
  public void addTransition(HiddenState origin, HiddenState dest, Double prob) {
    origin.addTransition(dest, prob);
  }

  /**
   * This method sets the emission distribution of a given HiddenState.
   *
   * @param originName - the name of the state for the new distribution.
   * @param potentialEmissions - the new emission distribution.
   * @throws InvalidDistributionException if the new distribution is not valid or the state name is
   *     not registered.
   */
  public void setEmissionDistribution(
      String originName, HashMap<Emission, Double> potentialEmissions)
      throws InvalidDistributionException {
    if (this.nameMap.get(originName) == null) {
      throw new InvalidDistributionException(
          "State: "
              + originName
              + "was not a registered state "
              + "to fill its emission distribution.",
          potentialEmissions);
    }
    RandomGenerator.validateDistribution(Emission.class, potentialEmissions);
    this.nameMap.get(originName).fillEmissions(potentialEmissions);
  }

  /**
   * This method sets the emission distribution of a given HiddenState.
   *
   * @param origin - the instance of the state for the new distribution.
   * @param potentialEmissions - the new emission distribution.
   * @throws InvalidDistributionException if the new distribution is not valid.
   */
  public void setEmissionDistribution(
      HiddenState origin, HashMap<Emission, Double> potentialEmissions)
      throws InvalidDistributionException {
    RandomGenerator.validateDistribution(Emission.class, potentialEmissions);
    origin.fillEmissions(potentialEmissions);
  }

  /**
   * This method adds an emission to a state's emission distribution.
   *
   * @param originName - the name of the state whose distribution should be modified.
   * @param emission - the new emission to be added.
   * @param prob - the probability of emitting the emission.
   * @throws InvalidDistributionException if originName is not registered in the nameMap.
   */
  public void addEmission(String originName, Emission emission, Double prob)
      throws InvalidDistributionException {
    if (this.nameMap.get(originName) == null) {
      throw new InvalidDistributionException(
          "State: "
              + originName
              + "was not a registered state "
              + "to add an emission to its distribution.",
          new HashMap());
    }
    this.nameMap.get(originName).addEmission(emission, prob);
  }

  /**
   * This method adds an emission to a state's emission distribution.
   *
   * @param origin - the instance of the state whose distribution should be modified.
   * @param emission - the new emission to be added.
   * @param prob - the probability of emitting the emission.
   */
  public void addEmission(HiddenState origin, Emission emission, Double prob) {
    origin.addEmission(emission, prob);
  }

  /**
   * This method sets the start distribution for the overall model.
   *
   * @param potentialStart - the start distribution to be set.
   * @throws InvalidDistributionException if the start distribution inputted is invalid.
   */
  public void setStartDistribution(HashMap<HiddenState, Double> potentialStart)
      throws InvalidDistributionException {
    RandomGenerator.validateDistribution(HiddenState.class, potentialStart);
    this.startDist.clear();
    for (HiddenState key : potentialStart.keySet()) {
      this.startDist.put(key, potentialStart.get(key));
    }
  }

  /**
   * This method adds a start state probability to the start distribution.
   *
   * @param stateName - the name of the state to be added.
   * @param prob - the probability with which the state should be transitioned to at the start.
   * @throws InvalidDistributionException if the stateName is not a registered state name.
   */
  public void addStartProbability(String stateName, Double prob)
      throws InvalidDistributionException {
    if (this.nameMap.get(stateName) == null) {
      throw new InvalidDistributionException(
          "State: "
              + stateName
              + "was not a registered state "
              + "to add to the start distribution.",
          this.startDist);
    }
    this.startDist.put(this.nameMap.get(stateName), prob);
  }

  /**
   * This method adds a start state probability to the start distribution.
   *
   * @param state - the instance of the state to be added.
   * @param prob - the probability with which the state should be transitioned to at the start.
   */
  public void addStartProbability(HiddenState state, Double prob) {
    this.startDist.put(state, prob);
  }

  /**
   * A utility method that overwrites all state transition distributions, filling them with linear
   * transitions; i.e. state i in the list of potential states transitions to state i + 1 with
   * probability 1. The last state in the list is special-cased to loop back to the first state.
   *
   * @throws InvalidDistributionException if any of the generated transition distributions turn out
   *     to be invalid or if there are not enough states.
   */
  public void addLinearTransitions() throws InvalidDistributionException {
    if (this.states.size() < 2) {
      throw new InvalidDistributionException(
          "To add linear transitions to a model, "
              + "there must be at least two states registered.",
          new HashMap());
    }

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
