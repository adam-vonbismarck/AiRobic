package edu.brown.cs.student;

import edu.brown.cs.student.main.models.exceptions.InvalidDistributionException;
import edu.brown.cs.student.main.models.markov.HiddenState;
import edu.brown.cs.student.main.models.markov.MarkovModel;
import java.io.FileNotFoundException;
import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ModelTests {

  @Test
  public void testBadStartDistLow() {
    HashMap<HiddenState, Double> start = new HashMap<>();
    start.put(new HiddenState("hello", new HashMap<>(), new HashMap<>()), 0.7);
    Exception exn =
        Assertions.assertThrows(
            InvalidDistributionException.class,
            () -> {
              MarkovModel model = new MarkovModel(start);
            });
    Assertions.assertEquals(
        exn.getMessage(),
        "Start distribution probabilities did not sum to 1.");
  }

  @Test
  public void testBadStartDistHigh() {
    HashMap<HiddenState, Double> start = new HashMap<>();
    start.put(new HiddenState("hello", new HashMap<>(), new HashMap<>()), 1.1);
    Exception exn =
        Assertions.assertThrows(
            InvalidDistributionException.class,
            () -> {
              MarkovModel model = new MarkovModel(start);
            });
    Assertions.assertEquals(
        exn.getMessage(),
        "Start distribution probabilities did not sum to 1.");
  }

  @Test
  public void testBadStartDistNeg() {
    HashMap<HiddenState, Double> start = new HashMap<>();
    HiddenState state = new HiddenState("hello", new HashMap<>(), new HashMap<>());
    start.put(state, -0.4);
    Exception exn =
        Assertions.assertThrows(
            InvalidDistributionException.class,
            () -> {
              MarkovModel model = new MarkovModel(start);
            });
    Assertions.assertEquals(
        exn.getMessage(),
        "The start probability associated with the hidden state " + state + " was negative.");

  }

}
