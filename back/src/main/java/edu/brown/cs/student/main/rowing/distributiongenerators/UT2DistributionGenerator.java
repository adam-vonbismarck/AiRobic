package edu.brown.cs.student.main.rowing.distributiongenerators;

import edu.brown.cs.student.main.models.markov.model.Emission;
import java.util.HashMap;

/**
 * The UT2DistributionGenerator generates an emission distribution for the UT2 workout type given
 * how long each UT2 workout should be.
 */
public class UT2DistributionGenerator {

  public static int MAX_INTERVALS = 5;
  public static int MIN_INTERVALS = 2;
  public static int SHORT_REST = 60;
  public static int LONG_REST = 90;

  // This static distribution determines the probability of the steady state being broken up into a
  // certain number
  // intervals.
  public static HashMap<Integer, Double> intervalsProbability =
      new HashMap<>() {
        {
          this.put(2, 0.15);
          this.put(3, 0.5);
          this.put(4, 0.25);
          this.put(5, 0.1);
        }
      };

  /**
   * This method gets a UT2 emission distribution given a number of minutes each workout should be.
   *
   * @param minutes - the length of each UT2 workout.
   * @return the completed emission distribution.
   */
  public static HashMap<Emission, Double> getLowIntensityDistributionByTime(int minutes) {
    HashMap<Emission, Double> newDist = new HashMap<>();
    // generates an emission for each number of intervals, and loads it into the emission
    // distribution to be returned.
    for (int i = MIN_INTERVALS; i < MAX_INTERVALS + 1; i++) {
      newDist.put(
          UT2DistributionGenerator.produceLowIntensityEmission(i, Math.floorDiv(minutes, i)),
          intervalsProbability.get(i));
    }
    return newDist;
  }

  /**
   * A helper method to generate the emissions associated with the distribution returned in
   * getLowIntensityDistributionByTime.
   *
   * @param numIntervals - the number of intervals in the emission.
   * @param intervalLength - the length of each interval.
   * @return an Emission modelling the two parameters inputted.
   */
  private static Emission produceLowIntensityEmission(int numIntervals, int intervalLength) {
    int restTime =
        switch (numIntervals) {
          case 4, 5 -> SHORT_REST;
          default -> LONG_REST;
        };
    int total = numIntervals * intervalLength;
    return new Emission(
        numIntervals + "x" + intervalLength + "min, " + restTime + "s rest at UT2 pace, rate 20.",
        total, numIntervals + "x" + intervalLength);
  }
}
