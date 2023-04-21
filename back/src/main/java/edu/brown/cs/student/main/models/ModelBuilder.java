package edu.brown.cs.student.main.models;

import edu.brown.cs.student.main.RandomGenerator;
import edu.brown.cs.student.main.models.exceptions.InvalidDistributionException;
import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.formattypes.Week;
import edu.brown.cs.student.main.models.markov.Emission;
import edu.brown.cs.student.main.models.markov.HiddenState;
import edu.brown.cs.student.main.models.markov.MarkovModel;
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

  public MarkovModel generateDefaultModel(int minutes, int numWeeks) throws InvalidDistributionException {

    Schedule schedule = new ScheduleBuilder().minutes(minutes, numWeeks, 0.2);

    for (Week week : schedule.weeks()) {
      for (Day day : week.days()) {
        //for (Emission emission : day.emissions) {

        //}
      }
    }

    return new MarkovModel(new HashMap<>());
  }

  public HiddenState generateNewState(String name) {
    HiddenState state = new HiddenState(name, new HashMap<>(), new HashMap<>());
    this.states.add(state);
    this.nameMap.put(name, state);
    return state;
  }

  public void setStateDistribution(String originName,
      HashMap<HiddenState, Double> potentialStates)
      throws InvalidDistributionException {
    RandomGenerator.validateDistribution(HiddenState.class, potentialStates);
    this.nameMap.get(originName).fillTransitions(potentialStates);
  }

  public void setStateDistribution(HiddenState origin,
      HashMap<HiddenState, Double> potentialStates)
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

  public void setEmissionDistribution(String originName,
      HashMap<Emission, Double> potentialEmissions)
      throws InvalidDistributionException {
    RandomGenerator.validateDistribution(Emission.class, potentialEmissions);
    this.nameMap.get(originName).fillEmissions(potentialEmissions);
  }

  public void setEmissionDistribution(HiddenState origin,
      HashMap<Emission, Double> potentialEmissions)
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

}
