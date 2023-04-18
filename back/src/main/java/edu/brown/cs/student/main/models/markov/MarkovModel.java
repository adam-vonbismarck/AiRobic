package edu.brown.cs.student.main.models.markov;

import edu.brown.cs.student.main.RandomGenerator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MarkovModel {

  private final Set<HiddenState> states;
  private final HashMap<HiddenState, Double> startDistribution;

  public MarkovModel(HashMap<HiddenState, Double> startDistribution) throws InvalidDistributionException {
    this.states = startDistribution.keySet();
    this.startDistribution = startDistribution;
    this.checkDistribution();
  }

  private void checkDistribution() throws InvalidDistributionException {
    double sum = 0;
    for (HiddenState key : this.states) {
      double currProb = this.startDistribution.get(key);
      if (currProb < 0) {
        throw new InvalidDistributionException("The start probability associated "
            + "with the hidden state " + key + " was negative.", this.startDistribution);
      }
      sum += currProb;
    }
    if (sum != 1) {
      throw new InvalidDistributionException("Start distribution probabilities did not sum to 1.",
          this.startDistribution);
    }
  }

  private void checkStateDistributions() {
    for (HiddenState state : this.states) {
      if (!this.states.equals(state.potentialStates())) {
      }
    }
  }

  private HiddenState generateStartState() throws InvalidDistributionException {
    double randDouble = RandomGenerator.getRandomPositiveDouble(0, 1);
    double currSum = 0;
    for (HiddenState key : this.states) {
      currSum += this.startDistribution.get(key);
      if (randDouble < currSum) {
        return key;
      }
    }
    throw new InvalidDistributionException("Start distribution probabilities summed to more than 1.",
        this.startDistribution);
  }

  public List<Emission> generateRandomSequence(int len) throws InvalidDistributionException {
    HiddenState currState = this.generateStartState();
    List<Emission> sequence = List.of();
    for (int i = 0; i < len; i++) {
      sequence.add(currState.emit());
      currState = currState.transition();
    }
    return sequence;
  }

}
