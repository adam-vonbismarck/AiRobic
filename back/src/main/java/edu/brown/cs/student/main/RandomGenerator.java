package edu.brown.cs.student.main;

import edu.brown.cs.student.main.models.markov.HiddenState;
import edu.brown.cs.student.main.models.markov.InvalidDistributionException;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A static class that generates random coordinates and string fields. This class is used for testing
 * and generating random outcomes based on probability distributions (in the models module).
 */
public class RandomGenerator {

  /**
   * Generates a random positive double.
   *
   * @param min The minimum double to be outputted
   * @param max The maximum double to be outputted
   * @return The random double
   */
  public static double getRandomPositiveDouble(double min, double max) {
    final ThreadLocalRandom r = ThreadLocalRandom.current();
    return r.nextDouble(min, max);
  }

  /**
   * **DIRECTLY TAKEN FROM PROF. NELSON'S CODE**
   *
   * <p>Returns a random string of length "length", where first and last defined the set of accepted
   * ASCII codes for this string.
   *
   * @param length The length of the string to generate
   * @param first the first ASCII-code in the character range allowed
   * @param last the last ASCII-code in the character range allowed
   * @return a random string of length "length"
   */
  public static String getRandomStringBounded(int length, int first, int last) {
    final ThreadLocalRandom r = ThreadLocalRandom.current();
    StringBuilder sb = new StringBuilder();
    for (int iCount = 0; iCount < length; iCount++) {
      int code = r.nextInt(first, last + 1);
      sb.append((char) code);
    }
    return sb.toString();
  }

  /**
   *
   */
  public static <T> T generateRandomFromDistribution(Class<T> type, HashMap<T, Double> distribution)
      throws InvalidDistributionException {
    double randDouble = RandomGenerator.getRandomPositiveDouble(0, 1);
    double currSum = 0;
    for (T key : distribution.keySet()) {
      currSum += distribution.get(key);
      if (randDouble < currSum) {
        return key;
      }
    }
    throw new InvalidDistributionException("Start distribution probabilities summed to more than 1.",
        distribution);
  }

  /**
   *
   */
  public static <T> void validateDistribution(Class<T> type, HashMap<T, Double> distribution)
      throws InvalidDistributionException {
    double sum = 0;
    for (T key : distribution.keySet()) {
      double currProb = distribution.get(key);
      if (currProb < 0) {
        throw new InvalidDistributionException("The start probability associated "
            + "with the hidden state " + key + " was negative.", distribution);
      }
      sum += currProb;
    }
    if (sum != 1) {
      throw new InvalidDistributionException("Start distribution probabilities did not sum to 1.",
          distribution);
    }
  }

}
