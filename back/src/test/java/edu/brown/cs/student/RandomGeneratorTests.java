package edu.brown.cs.student;

import edu.brown.cs.student.main.RandomGenerator;
import edu.brown.cs.student.main.models.exceptions.InvalidDistributionException;
import edu.brown.cs.student.main.models.exceptions.InvalidScheduleException;
import edu.brown.cs.student.main.models.formattypes.Day;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RandomGeneratorTests {

  private HashMap<String, Double> validDistOne;
  private HashMap<String, Double> validDistMultiple;
  private HashMap<Day, Double> invalidDistNeg;
  private HashMap<Double, Double> invalidDistNoSumLow;
  private HashMap<Double, Double> invalidDistNoSumHigh;
  private Day negDay;

  private static int NUM_TRIALS = 20;

  /** This method sets up some example probability distributions for testing. */
  @BeforeEach
  public void setupDistributions() throws InvalidScheduleException {

    this.validDistOne =
        new HashMap<>() {
          {
            this.put("this", 1.0);
          }
        };

    this.validDistMultiple =
        new HashMap<>() {
          {
            this.put("this", 0.5);
            this.put("that", 0.2);
            this.put("those", 0.3);
          }
        };

    this.negDay =
        new Day("day", new ArrayList<>(), 7, DayOfWeek.MONDAY, Optional.empty(), new ArrayList<>());

    this.invalidDistNeg =
        new HashMap<>() {
          {
            this.put(
                new Day(
                    "day",
                    new ArrayList<>(),
                    11,
                    DayOfWeek.FRIDAY,
                    Optional.empty(),
                    new ArrayList<>()),
                0.5);
            this.put(
                new Day(
                    "day",
                    new ArrayList<>(),
                    1,
                    DayOfWeek.TUESDAY,
                    Optional.empty(),
                    new ArrayList<>()),
                0.7);
          }
        };

    this.invalidDistNeg.put(this.negDay, -0.2);

    this.invalidDistNoSumLow =
        new HashMap<>() {
          {
            this.put(5.0, 0.1);
            this.put(5.1, 0.89);
          }
        };

    this.invalidDistNoSumHigh =
        new HashMap<>() {
          {
            this.put(5.0, 0.1);
            this.put(5.1, 0.91);
          }
        };
  }

  /** Tests that a valid distribution with a single element emitted with probability 1 is valid. */
  @Test
  public void testValidDistributionSingle() {
    try {
      RandomGenerator.validateDistribution(String.class, this.validDistOne);
    } catch (InvalidDistributionException e) {
      Assertions.assertEquals("Distribution to be valid.", "Distribution was not valid.");
    }
  }

  /** Tests that a valid distribution with a multiple elements is valid. */
  @Test
  public void testValidDistributionMultiple() {
    try {
      RandomGenerator.validateDistribution(String.class, this.validDistMultiple);
    } catch (InvalidDistributionException e) {
      Assertions.assertEquals("Distribution to be valid.", "Distribution was not valid.");
    }
  }

  /**
   * Tests that an invalid distribution with negative probabilities is not validated by
   * RandomGenerator.
   */
  @Test
  public void testInvalidDistributionNeg() {
    Exception exn =
        Assertions.assertThrows(
            InvalidDistributionException.class,
            () -> {
              RandomGenerator.validateDistribution(Day.class, this.invalidDistNeg);
            });
    Assertions.assertEquals(
        exn.getMessage(),
        "The probability associated with the output " + this.negDay + " was negative.");
  }

  /**
   * Tests that an invalid distribution with probabilities summing to more than 1 is not validated.
   */
  @Test
  public void testInvalidDistributionNoSumHigh() {
    Exception exn =
        Assertions.assertThrows(
            InvalidDistributionException.class,
            () -> {
              RandomGenerator.validateDistribution(Double.class, this.invalidDistNoSumHigh);
            });
    Assertions.assertEquals(exn.getMessage(), "Distribution probabilities did not sum to 1.");
  }

  /**
   * Tests that an invalid distribution with probabilities summing to more than 1 is not validated.
   */
  @Test
  public void testInvalidDistributionNoSumLow() {
    Exception exn =
        Assertions.assertThrows(
            InvalidDistributionException.class,
            () -> {
              RandomGenerator.validateDistribution(Double.class, this.invalidDistNoSumLow);
            });
    Assertions.assertEquals(exn.getMessage(), "Distribution probabilities did not sum to 1.");
  }

  /**
   * Tests that a valid distribution with a single element emitted with probability 1 emits that
   * element when RandomGenerator chooses from the distribution.
   */
  @Test
  public void testValidDistributionSingleGenerate() {
    try {
      Assertions.assertEquals(
          "this", RandomGenerator.generateRandomFromDistribution(String.class, this.validDistOne));
    } catch (InvalidDistributionException e) {
      Assertions.assertEquals("Distribution to be valid.", "Distribution was not valid.");
    }
  }

  /**
   * Tests that a valid distribution with multiple elements emits one of those elements when
   * RandomGenerator chooses from the distribution. Runs the generation a few times for extra
   * certainty.
   */
  @Test
  public void testValidDistributionMultipleGenerate() {
    try {
      for (int i = 0; i < NUM_TRIALS; i++) {
        String result =
            RandomGenerator.generateRandomFromDistribution(String.class, this.validDistMultiple);
        Assertions.assertTrue(
            result.equals("this") || result.equals("that") || result.equals("those"));
      }
    } catch (InvalidDistributionException e) {
      Assertions.assertEquals("Distribution to be valid.", "Distribution was not valid.");
    }
  }

  /**
   * Tests that an invalid distribution with negative probabilities is not validated by
   * RandomGenerator under generation circumstances.
   */
  @Test
  public void testInvalidDistributionNegGenerate() {
    Exception exn =
        Assertions.assertThrows(
            InvalidDistributionException.class,
            () -> {
              RandomGenerator.generateRandomFromDistribution(Day.class, this.invalidDistNeg);
            });
    Assertions.assertEquals(
        exn.getMessage(),
        "The probability associated with the output " + this.negDay + " was negative.");
  }

  /**
   * Tests that an invalid distribution with probabilities summing to more than 1 is not validated
   * under generation circumstances.
   */
  @Test
  public void testInvalidDistributionNoSumHighGenerate() {
    Exception exn =
        Assertions.assertThrows(
            InvalidDistributionException.class,
            () -> {
              RandomGenerator.generateRandomFromDistribution(
                  Double.class, this.invalidDistNoSumHigh);
            });
    Assertions.assertEquals(exn.getMessage(), "Distribution probabilities did not sum to 1.");
  }

  /**
   * Tests that an invalid distribution with probabilities summing to more than 1 is not validated
   * under generation circumstances.
   */
  @Test
  public void testInvalidDistributionNoSumLowGenerate() {
    Exception exn =
        Assertions.assertThrows(
            InvalidDistributionException.class,
            () -> {
              RandomGenerator.generateRandomFromDistribution(
                  Double.class, this.invalidDistNoSumLow);
            });
    Assertions.assertEquals(exn.getMessage(), "Distribution probabilities did not sum to 1.");
  }
}
