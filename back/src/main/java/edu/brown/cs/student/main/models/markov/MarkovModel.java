package edu.brown.cs.student.main.models.markov;

import edu.brown.cs.student.main.RandomGenerator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MarkovModel {

  private final Set<HiddenState> states;
  private final HashMap<HiddenState, Double> startDistribution;

  public MarkovModel(HashMap<HiddenState, Double> startDistribution) throws InvalidDistributionException {
    this.states = startDistribution.keySet();
    this.startDistribution = startDistribution;
    RandomGenerator.validateDistribution(HiddenState.class, startDistribution);
    this.checkStateDistributions();
  }

  private void checkStateDistributions() throws InvalidDistributionException {
    for (HiddenState state : this.states) {
      if (!this.states.equals(state.potentialStates())) {
        throw new InvalidDistributionException("Hidden state " + state + "had a state distribution that"
            + "contained foreign states or did not contain all states of this instance of the model: " + this,
            this.startDistribution);
      }
    }
  }

  public List<Emission> generateRandomSequence(int len) throws InvalidDistributionException {
    HiddenState currState = RandomGenerator.generateRandomFromDistribution(HiddenState.class,
        this.startDistribution);
    List<Emission> sequence = List.of();
    for (int i = 0; i < len; i++) {
      sequence.add(currState.emit());
      currState = currState.transition();
    }
    return sequence;
  }

}
