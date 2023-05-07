package edu.brown.cs.student;

import edu.brown.cs.student.main.models.exceptions.*;
import edu.brown.cs.student.main.models.formatters.DefaultFormatter;
import edu.brown.cs.student.main.models.formatters.ScheduleFormatter;
import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.formattypes.Week;
import edu.brown.cs.student.main.models.markov.model.Emission;
import edu.brown.cs.student.main.models.markov.model.HiddenState;
import edu.brown.cs.student.main.models.markov.model.MarkovModel;
import java.time.DayOfWeek;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ModelTests {

  public HashMap<Emission, Double> validEmissionDist;
  public HashMap<Emission, Double> invalidEmissionDist;
  public HashSet<Emission> emissionSet;

  @BeforeEach
  public void setup() {
    this.validEmissionDist =
        new HashMap<>() {
          {
            this.put(new Emission("workout", 60.0), 0.8);
            this.put(new Emission("workout2", 80.0), 0.2);
          }
        };
    this.invalidEmissionDist =
        new HashMap<>() {
          {
            this.put(new Emission("workout", 60.0), 0.8);
            this.put(new Emission("workout2", 80.0), 0.3);
          }
        };

    this.emissionSet =
        new HashSet<>() {
          {
            this.add(new Emission("workout", 60.0));
            this.add(new Emission("workout2", 80.0));
          }
        };
  }

  @Test
  public void testBadStartDistLow() throws InvalidDistributionException {
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
  public void testBadStartDistHigh() throws InvalidDistributionException {
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
  public void testBadStartDistNeg() throws InvalidDistributionException {
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
  public void testBadStateDistMissing() throws InvalidDistributionException {
    HiddenState stateOne = new HiddenState("state 1", new HashMap<>(), new HashMap<>());
    HiddenState stateTwo = new HiddenState("state 2", new HashMap<>(), new HashMap<>());
    stateOne.addTransition(stateOne, 0.4);
    stateOne.addTransition(stateTwo, 0.6);
    stateTwo.addTransition(stateOne, 1.0);
    stateOne.fillEmissions(this.validEmissionDist);
    stateTwo.fillEmissions(this.validEmissionDist);

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
  public void testBadStateDistForeign() throws InvalidDistributionException {
    HiddenState stateOne = new HiddenState("state 1", new HashMap<>(), new HashMap<>());
    HiddenState stateTwo = new HiddenState("state 2", new HashMap<>(), new HashMap<>());
    stateOne.addTransition(stateOne, 0.4);
    stateOne.addTransition(stateTwo, 0.6);
    stateTwo.addTransition(stateOne, 1.0);
    stateTwo.addTransition(stateTwo, 0.0);
    stateTwo.addTransition(new HiddenState("state 3?", new HashMap<>(), new HashMap<>()), 1.0);
    stateOne.fillEmissions(this.validEmissionDist);
    stateTwo.fillEmissions(this.validEmissionDist);

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
  public void testBadStateDistInvalid() throws InvalidDistributionException {
    HiddenState stateOne = new HiddenState("state 1", new HashMap<>(), new HashMap<>());
    HiddenState stateTwo = new HiddenState("state 2", new HashMap<>(), new HashMap<>());
    stateOne.addTransition(stateOne, 0.4);
    stateOne.addTransition(stateTwo, 0.6);
    stateTwo.addTransition(stateOne, 1.0);
    stateTwo.addTransition(stateTwo, 0.1);
    stateOne.fillEmissions(this.validEmissionDist);
    stateTwo.fillEmissions(this.validEmissionDist);

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

  @Test
  public void testBadStateEmissionDistMissing() throws InvalidDistributionException {
    HiddenState stateOne = new HiddenState("state 1", new HashMap<>(), new HashMap<>());
    HiddenState stateTwo = new HiddenState("state 2", new HashMap<>(), new HashMap<>());
    stateOne.addTransition(stateOne, 0.4);
    stateOne.addTransition(stateTwo, 0.6);
    stateTwo.addTransition(stateOne, 1.0);
    stateTwo.addTransition(stateTwo, 0.0);

    stateOne.fillEmissions(this.validEmissionDist);

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

  @Test
  public void testTwoStateModel() throws InvalidDistributionException, FormatterFailureException {
    HiddenState stateOne = new HiddenState("state 1", new HashMap<>(), new HashMap<>());
    HiddenState stateTwo = new HiddenState("state 2", new HashMap<>(), new HashMap<>());
    stateOne.addTransition(stateOne, 0.4);
    stateOne.addTransition(stateTwo, 0.6);
    stateTwo.addTransition(stateOne, 0.9);
    stateTwo.addTransition(stateTwo, 0.1);
    stateOne.fillEmissions(this.validEmissionDist);
    stateTwo.fillEmissions(this.validEmissionDist);

    HashMap<HiddenState, Double> startDist =
        new HashMap<>() {
          {
            this.put(stateOne, 0.5);
            this.put(stateTwo, 0.5);
          }
        };

    MarkovModel model = new MarkovModel(startDist);
    for (Emission emission : model.generateFormattedEmissions(10, new DefaultFormatter())) {
      Assertions.assertTrue(this.emissionSet.contains(emission));
    }
  }

  @Test
  public void testTwoStateModelScheduleFormatter()
      throws InvalidDistributionException, FormatterFailureException, InvalidScheduleException {
    HiddenState stateOne = new HiddenState("state 1", new HashMap<>(), new HashMap<>());
    HiddenState stateTwo = new HiddenState("state 2", new HashMap<>(), new HashMap<>());
    stateOne.addTransition(stateOne, 0.4);
    stateOne.addTransition(stateTwo, 0.6);
    stateTwo.addTransition(stateOne, 0.9);
    stateTwo.addTransition(stateTwo, 0.1);
    stateOne.fillEmissions(this.validEmissionDist);
    stateTwo.fillEmissions(this.validEmissionDist);

    HashMap<HiddenState, Double> startDist =
        new HashMap<>() {
          {
            this.put(stateOne, 0.5);
            this.put(stateTwo, 0.5);
          }
        };

    Schedule schedule =
        new Schedule(
            "schedule",
            List.of(
                new Week(
                    "week",
                    List.of(
                        new Day(
                            "day",
                            new ArrayList<>(),
                            3,
                            DayOfWeek.MONDAY,
                            Optional.empty(),
                            List.of()),
                        new Day(
                            "day",
                            new ArrayList<>(),
                            3,
                            DayOfWeek.TUESDAY,
                            Optional.empty(),
                            List.of())))),
            new Week("week", List.of()));

    MarkovModel model = new MarkovModel(startDist);
    Schedule schedule1 = model.generateFormattedEmissions(6, new ScheduleFormatter(schedule));
    for (Week week : schedule1.weeks()) {
      for (Day day : week.days()) {
        for (Emission emission : day.getEmissions()) {
          Assertions.assertTrue(this.emissionSet.contains(emission));
        }
      }
    }
  }

  @Test
  public void testTwoStateModelFailedScheduleFormatter()
      throws InvalidDistributionException, InvalidScheduleException {
    HiddenState stateOne = new HiddenState("state 1", new HashMap<>(), new HashMap<>());
    HiddenState stateTwo = new HiddenState("state 2", new HashMap<>(), new HashMap<>());
    stateOne.addTransition(stateOne, 0.4);
    stateOne.addTransition(stateTwo, 0.6);
    stateTwo.addTransition(stateOne, 0.9);
    stateTwo.addTransition(stateTwo, 0.1);
    stateOne.fillEmissions(this.validEmissionDist);
    stateTwo.fillEmissions(this.validEmissionDist);

    HashMap<HiddenState, Double> startDist =
        new HashMap<>() {
          {
            this.put(stateOne, 0.5);
            this.put(stateTwo, 0.5);
          }
        };

    Schedule schedule =
        new Schedule(
            "schedule",
            List.of(
                new Week(
                    "week",
                    List.of(
                        new Day(
                            "day",
                            new ArrayList<>(),
                            3,
                            DayOfWeek.MONDAY,
                            Optional.empty(),
                            List.of()),
                        new Day(
                            "day",
                            new ArrayList<>(),
                            3,
                            DayOfWeek.TUESDAY,
                            Optional.empty(),
                            List.of())))),
            new Week("week", List.of()));

    MarkovModel model = new MarkovModel(startDist);
    Exception exn =
        Assertions.assertThrows(
            FormatterFailureException.class,
            () -> {
              Schedule schedule1 =
                  model.generateFormattedEmissions(8, new ScheduleFormatter(schedule));
            });
    Assertions.assertEquals(
        exn.getMessage(),
        "Schedule and model emissions did not match in length, "
            + "so the schedule could not be filled in appropriately.");
  }

  @Test
  public void testNoStateModel() throws InvalidDistributionException, FormatterFailureException {
    Exception exn =
        Assertions.assertThrows(
            InvalidDistributionException.class,
            () -> {
              MarkovModel model = new MarkovModel(new HashMap<>());
            });
    Assertions.assertEquals(exn.getMessage(), "Distribution probabilities did not sum to 1.");
  }

  @Test
  public void testMoreComplexStates()
      throws InvalidDistributionException, InvalidScheduleException, FormatterFailureException {
    HiddenState stateOne = new HiddenState("state 1", new HashMap<>(), new HashMap<>());
    HiddenState stateTwo = new HiddenState("state 2", new HashMap<>(), new HashMap<>());
    HiddenState stateThree = new HiddenState("state 3", new HashMap<>(), new HashMap<>());
    HiddenState stateFour = new HiddenState("state 4", new HashMap<>(), new HashMap<>());
    stateOne.addTransition(stateOne, 0.1);
    stateOne.addTransition(stateTwo, 0.6);
    stateOne.addTransition(stateThree, 0.1);
    stateOne.addTransition(stateFour, 0.2);
    stateTwo.addTransition(stateOne, 0.4);
    stateTwo.addTransition(stateTwo, 0.1);
    stateTwo.addTransition(stateThree, 0.5);
    stateTwo.addTransition(stateFour, 0.0);
    stateThree.addTransition(stateOne, 0.7);
    stateThree.addTransition(stateTwo, 0.15);
    stateThree.addTransition(stateThree, 0.05);
    stateThree.addTransition(stateFour, 0.1);
    stateFour.addTransition(stateOne, 0.3);
    stateFour.addTransition(stateTwo, 0.3);
    stateFour.addTransition(stateThree, 0.3);
    stateFour.addTransition(stateFour, 0.1);
    stateOne.fillEmissions(this.validEmissionDist);
    stateTwo.fillEmissions(this.validEmissionDist);
    stateThree.fillEmissions(this.validEmissionDist);
    stateFour.fillEmissions(this.validEmissionDist);

    HashMap<HiddenState, Double> startDist =
        new HashMap<>() {
          {
            this.put(stateOne, 0.7);
            this.put(stateTwo, 0.1);
            this.put(stateThree, 0.05);
            this.put(stateFour, 0.15);
          }
        };

    MarkovModel model = new MarkovModel(startDist);
    for (Emission emission : model.generateFormattedEmissions(10, new DefaultFormatter())) {
      Assertions.assertTrue(this.emissionSet.contains(emission));
    }
  }
}
