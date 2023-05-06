package edu.brown.cs.student;

import edu.brown.cs.student.main.handlers.ConvertToJson;
import edu.brown.cs.student.main.handlers.GenerateGraphLikePlan;
import edu.brown.cs.student.main.models.exceptions.*;
import edu.brown.cs.student.main.models.formatters.ScheduleFormatter;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.markov.Emission;
import edu.brown.cs.student.main.models.markov.HiddenState;
import edu.brown.cs.student.main.models.markov.MarkovModel;
import edu.brown.cs.student.main.rowing.LinearModelBuilder;
import edu.brown.cs.student.main.rowing.ScheduleBuilder;
import edu.brown.cs.student.main.rowing.Workout;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Set;

import edu.brown.cs.student.main.server.Serializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ModelTests {

  public HashMap<Emission, Double> validEmissionDist;
  public HashMap<Emission, Double> invalidEmissionDist;

  @BeforeEach
  public void setup() {
    this.validEmissionDist =
        new HashMap<>() {
          {
            this.put(new Emission("workout", 60.0, true, 145.5, 1), 0.8);
            this.put(new Emission("workout2", 80.0, false, 145.5, 1), 0.2);
          }
        };
    this.invalidEmissionDist =
        new HashMap<>() {
          {
            this.put(new Emission("workout", 60.0, true, 145.5, 1), 0.8);
            this.put(new Emission("workout2", 80.0, true, 147.5, 10), 0.3);
          }
        };
  }

  @Test
  public void testLinear()
      throws IOException, InvalidScheduleException, InvalidDistributionException,
          FormatterFailureException, NoWorkoutTypeException {
    ScheduleBuilder builder = new ScheduleBuilder();
    Schedule toBuild = null;
    toBuild =
        builder.minutesWithDates(
            300,
            LocalDate.now(),
            LocalDate.of(2023, 5, 25),
            0.2,
            Workout.of("2k"),
            Workout.of("UT2"));
    MarkovModel model = null;
    model = new LinearModelBuilder().build(toBuild, LocalDate.now().getDayOfWeek());
    Schedule schedule =
        model.generateFormattedEmissions(toBuild.getLength(), new ScheduleFormatter(toBuild));
    System.out.println(schedule);
  }

  @Test
  public void testVariable()
      throws IOException, InvalidScheduleException, InvalidDistributionException,
          FormatterFailureException, NoWorkoutTypeException, InvalidDatesException {
    Schedule schedule =
        new GenerateGraphLikePlan()
            .generate(
                420,
                LocalDate.now().plusDays(2),
                LocalDate.of(2023, 5, 8),
                Set.of(Workout._2K, Workout._6K),
                Set.of(Workout.UT_2),
                0.2);
    System.out.println(schedule);
    System.out.println(Serializer.serializeSchedule(schedule));
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
    Assertions.assertEquals(exn.getMessage(), "Distribution probabilities did not sum to 1.");
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
    Assertions.assertEquals(exn.getMessage(), "Distribution probabilities did not sum to 1.");
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
        exn.getMessage(), "The probability associated with the output " + state + " was negative.");
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

    HashMap<HiddenState, Double> startDist =
        new HashMap<>() {
          {
            this.put(stateOne, 0.5);
            this.put(stateTwo, 0.5);
          }
        };

    Exception exn =
        Assertions.assertThrows(
            InvalidDistributionException.class,
            () -> {
              MarkovModel model = new MarkovModel(startDist);
            });
    Assertions.assertEquals(
        exn.getMessage(),
        "Hidden state "
            + stateTwo
            + " had a state distribution that "
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

    HashMap<HiddenState, Double> startDist =
        new HashMap<>() {
          {
            this.put(stateOne, 0.5);
            this.put(stateTwo, 0.5);
          }
        };

    Exception exn =
        Assertions.assertThrows(
            InvalidDistributionException.class,
            () -> {
              MarkovModel model = new MarkovModel(startDist);
            });
    Assertions.assertEquals(
        exn.getMessage(),
        "Hidden state "
            + stateTwo
            + " had a state distribution that "
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

    HashMap<HiddenState, Double> startDist =
        new HashMap<>() {
          {
            this.put(stateOne, 0.5);
            this.put(stateTwo, 0.5);
          }
        };

    Exception exn =
        Assertions.assertThrows(
            InvalidDistributionException.class,
            () -> {
              MarkovModel model = new MarkovModel(startDist);
            });
    Assertions.assertEquals(exn.getMessage(), "Distribution probabilities did not sum to 1.");
  }
}
