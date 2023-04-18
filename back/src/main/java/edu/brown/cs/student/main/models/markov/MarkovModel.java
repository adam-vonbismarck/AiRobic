package edu.brown.cs.student.main.models.markov;

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
        throw new InvalidDistributionException("The probability associated "
            + "with the hidden state " + key + " was negative.", this.startDistribution);
      }
      sum += currProb;
    }
    if (sum != 1) {
      throw new InvalidDistributionException("Distribution probabilities did not sum to 1.",
          this.startDistribution);
    }
  }

  private HiddenState generateStartState() throws InvalidDistributionException {
    double randDouble = 0.5; // use random generator
    double currSum = 0;
    for (HiddenState key : this.states) {
      currSum += this.startDistribution.get(key);
      if (randDouble < currSum) {
        return key;
      }
    }
    throw new InvalidDistributionException("Distribution probabilities did not sum to 1.",
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
