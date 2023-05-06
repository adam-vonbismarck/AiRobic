package edu.brown.cs.student.main.rowing;

import edu.brown.cs.student.main.models.markov.Emission;
import java.util.HashMap;

/**
 * Waiting to see if I will include this code in the WorkoutDistributionByName class, or if I will
 * modify the distributions.
 */
public class UT2DistributionGenerator {

  public static int MAX_INTERVALS = 5;
  public static int MIN_INTERVALS = 2;
  public static HashMap<Integer, Double> intervalsProbability =
      new HashMap<>() {
        {
          this.put(2, 0.15);
          this.put(3, 0.5);
          this.put(4, 0.25);
          this.put(5, 0.1);
        }
      };

  public static HashMap<Emission, Double> getLowIntensityDistributionByTime(int minutes) {
    HashMap<Emission, Double> newDist = new HashMap<>();
    for (int i = MIN_INTERVALS; i < MAX_INTERVALS + 1; i++) {
      newDist.put(
          UT2DistributionGenerator.produceLowIntensityEmission(i, Math.floorDiv(minutes, i)),
          intervalsProbability.get(i));
    }
    return newDist;
  }

  private static Emission produceLowIntensityEmission(int numIntervals, int intervalLength) {
    int restTime =
        switch (numIntervals) {
          case 4, 5 -> 60;
          default -> 90;
        };
    int total = numIntervals * intervalLength;
    return new Emission(
        numIntervals + "x" + intervalLength + "min, " + restTime + "s rest at UT2 pace, rate 20.",
        total);
  }
}
