package edu.brown.cs.student.main.models;

import edu.brown.cs.student.main.RandomGenerator;
import edu.brown.cs.student.main.models.exceptions.InvalidDistributionException;
import edu.brown.cs.student.main.models.markov.Emission;
import edu.brown.cs.student.main.models.markov.HiddenState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModelBuilder {

  private final List<HiddenState> states;
  private final HashMap<String, HiddenState> nameMap;
  private final HashMap<HiddenState, Double> startDist;

  public ModelBuilder() {
    this.states = new ArrayList<>();
    this.nameMap = new HashMap<>();
    this.startDist = new HashMap<>();
  }

  public HiddenState generateNewState(String name) {
    HiddenState state = new HiddenState(name, new HashMap<>(), new HashMap<>());
    this.states.add(state);
    this.nameMap.put(name, state);
    return state;
  }

  public void addTransition(String originName, String destName, Double prob) {
    this.nameMap.get(originName).addTransition(this.nameMap.get(destName), prob);
  }

  public void addTransition(HiddenState origin, HiddenState dest, Double prob) {
    origin.addTransition(dest, prob);
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

}
