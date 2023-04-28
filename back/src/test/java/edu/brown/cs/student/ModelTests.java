package edu.brown.cs.student;

import edu.brown.cs.student.main.models.exceptions.InvalidDistributionException;
import edu.brown.cs.student.main.models.markov.Emission;
import edu.brown.cs.student.main.models.markov.HiddenState;
import edu.brown.cs.student.main.models.markov.MarkovModel;
import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ModelTests {

  public HashMap<Emission, Double> validEmissionDist;
  public HashMap<Emission, Double> invalidEmissionDist;

  @BeforeEach
  public void setup() {
    this.validEmissionDist = new HashMap<>() {{
      this.put(new Emission("workout", 60.0, true, 145.5, 1), 0.8);
      this.put(new Emission("workout2", 80.0, false, 145.5, 1), 0.2);
    }};
    this.invalidEmissionDist = new HashMap<>() {{
      this.put(new Emission("workout", 60.0, true, 145.5, 1), 0.8);
      this.put(new Emission("workout2", 80.0, true, 147.5, 10), 0.3);
    }};
  }

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
        "Distribution probabilities did not sum to 1.");
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
        "Distribution probabilities did not sum to 1.");
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
        "The probability associated with the output " + state + " was negative.");

  }

  @Test
  public void testBadStateDistMissing() {
    HiddenState stateOne = new HiddenState("state 1", new HashMap<>(), new HashMap<>());
    HiddenState stateTwo = new HiddenState("state 2", new HashMap<>(), new HashMap<>());
    stateOne.addTransition(stateOne, 0.4);
    stateOne.addTransition(stateTwo, 0.6);
    stateTwo.addTransition(stateOne, 1.0);
    try {
      stateOne.fillEmissions(this.validEmissionDist);
      stateTwo.fillEmissions(this.validEmissionDist);
    } catch (InvalidDistributionException e) {
      Assertions.assertEquals("Distribution to be valid", e.getMessage());
    }

    HashMap<HiddenState, Double> startDist = new HashMap<>() {{
      this.put(stateOne, 0.5);
      this.put(stateTwo, 0.5);
    }};

    Exception exn =
        Assertions.assertThrows(
            InvalidDistributionException.class,
            () -> {
              MarkovModel model = new MarkovModel(startDist);
            });
    Assertions.assertEquals(
        exn.getMessage(),
        "Hidden state " + stateTwo + " had a state distribution that "
            + "contained foreign states or did not contain all states relevant to the start distribution.");

  }

  @Test
  public void testBadStateDistForeign() {
    HiddenState stateOne = new HiddenState("state 1", new HashMap<>(), new HashMap<>());
    HiddenState stateTwo = new HiddenState("state 2", new HashMap<>(), new HashMap<>());
    stateOne.addTransition(stateOne, 0.4);
    stateOne.addTransition(stateTwo, 0.6);
    stateTwo.addTransition(stateOne, 1.0);
    stateTwo.addTransition(stateTwo, 0.0);
    stateTwo.addTransition(new HiddenState("state 3?", new HashMap<>(), new HashMap<>()), 1.0);
    try {
      stateOne.fillEmissions(this.validEmissionDist);
      stateTwo.fillEmissions(this.validEmissionDist);
    } catch (InvalidDistributionException e) {
      Assertions.assertEquals("Distribution to be valid", e.getMessage());
    }

    HashMap<HiddenState, Double> startDist = new HashMap<>() {{
      this.put(stateOne, 0.5);
      this.put(stateTwo, 0.5);
    }};

    Exception exn =
        Assertions.assertThrows(
            InvalidDistributionException.class,
            () -> {
              MarkovModel model = new MarkovModel(startDist);
            });
    Assertions.assertEquals(
        exn.getMessage(),
        "Hidden state " + stateTwo + " had a state distribution that "
            + "contained foreign states or did not contain all states relevant to the start distribution.");

  }

  @Test
  public void testBadStateDistInvalid() {
    HiddenState stateOne = new HiddenState("state 1", new HashMap<>(), new HashMap<>());
    HiddenState stateTwo = new HiddenState("state 2", new HashMap<>(), new HashMap<>());
    stateOne.addTransition(stateOne, 0.4);
    stateOne.addTransition(stateTwo, 0.6);
    stateTwo.addTransition(stateOne, 1.0);
    stateTwo.addTransition(stateTwo, 0.1);
    try {
      stateOne.fillEmissions(this.validEmissionDist);
      stateTwo.fillEmissions(this.validEmissionDist);
    } catch (InvalidDistributionException e) {
      Assertions.assertEquals("Distribution to be valid", e.getMessage());
    }

    HashMap<HiddenState, Double> startDist = new HashMap<>() {{
      this.put(stateOne, 0.5);
      this.put(stateTwo, 0.5);
    }};

    Exception exn =
        Assertions.assertThrows(
            InvalidDistributionException.class,
            () -> {
              MarkovModel model = new MarkovModel(startDist);
            });
    Assertions.assertEquals(
        exn.getMessage(),
        "Hidden state " + stateTwo + " had a state distribution that "
            + "contained foreign states or did not contain all states relevant to the start distribution.");

  }

}
