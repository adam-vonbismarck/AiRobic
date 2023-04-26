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

/**
 * Planning to rewrite the way this logic works. Going to rebuild day, schedule, and week so that each
 * schedule has an example week, and the model is built off of this week. So, in addition to ScheduleBuilder,
 * there will be a WeekBuilder that creates an example week for a given schedule (used within ScheduleBuilder,
 * so some code from ScheduleBuilder will be delegated to WeekBuilder).
 *
 * From here, the ModelBuilder will build hidden states with a transition matrix that transitions (and loops
 * according to) the example week. I will likely make this version of ModelBuilder another class up the hierarchy,
 * so something like LinearModelBuilder.
 *
 * Both Week and ScheduleBuilder will have a generic option to build, as well as one that plugs in the intensity
 * of each workout into each Day (in a new field) for use in building the emissions of the Model in the LinearModelBuilder.
 *
 * I will use the Week and ScheduleBuilder non-generic options to build a Schedule for the LinearModelBuilder,
 * and then loop through the built Week to generate the hidden states and emissions. Then, I will run
 * the built model, using the built Schedule and ScheduleFormatter to capture the results. This means the
 * ScheduleBuild will need to be separate from the model build.
 *
 * For the equivalent of the graph, I will build a generic Schedule given the time/workouts inputted by the user
 * and generate hidden states and transition matrices directly from workout types, rather than with a schedule framework.
 *
 * In a separate use of the LinearModelBuilder, I will build a linear model, but only run the model with the length
 * of a single week. Then, I will use the result of this linear model build and use a simple process for increasing
 * the intensity of each progressive week to build the full schedule (applied the number of times equal to the number of
 * weeks remaining).
 *
 * This will all require the creation of different workout classes, and many instances of these workout classes.
 * Gordan will help me with these, and integrating them into the models above.
 *
 * For the LinearModelBuilder, we will allow a random choice from all potential hard workouts for each hard workout,
 * unless otherwise specified. The steady workout will be grouped by time, and their distribution will be placed in
 * each respective day accordingly.
 *
 * The goal parameter will narrow down which workout type LinearModelBuilder uses, and use the progressive increase
 * model to build the schedule.
 *
 */
public class ModelBuilder {

  private final List<HiddenState> states;
  private final HashMap<String, HiddenState> nameMap;
  private final HashMap<HiddenState, Double> startDist;

  public ModelBuilder() {
    this.states = new ArrayList<>();
    this.nameMap = new HashMap<>();
    this.startDist = new HashMap<>();
  }

  public MarkovModel generateDefaultModel(int minutes, int numWeeks, int intensityPercentage) throws InvalidDistributionException {

    Schedule schedule = new ScheduleBuilder().minutes(minutes, numWeeks, intensityPercentage);


    for (Week week : schedule.weeks()) {
      for (Day day : week.days()) {
        this.generateNewState(day.dayNumber().toString());
      }
    }

    for (Week week : schedule.weeks()) {
      for (Day day : week.days()) {
        this.addTransition(day.dayNumber().toString(),
            Integer.toString(day.dayNumber() + 1),
            1.0);
      }
    }

    return new MarkovModel(new HashMap<>());
  }

  public MarkovModel build() throws InvalidDistributionException {
    return new MarkovModel(this.startDist);
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

  public void addLinearTransitions() throws InvalidDistributionException {
    for (int i = 0; i < this.states.size() - 1; i++) {
      HashMap<HiddenState, Double> transitionMat = new HashMap<>();
      transitionMat.put(this.states.get(i + 1), 1.0);
      this.states.get(i).fillTransitions(transitionMat);
    }
    HashMap<HiddenState, Double> endTransitionMat = new HashMap<>();
    endTransitionMat.put(this.states.get(0), 1.0);
    this.states.get(this.states.size() - 1).fillTransitions(endTransitionMat);
  }

}
