package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.models.exceptions.InvalidDistributionException;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A static class that generates random coordinates and string fields. This class is used for
 * testing and generating random outcomes based on probability distributions (in the models module).
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
   * Generates a random integer.
   *
   * @param min The minimum integer to be outputted
   * @param max The maximum integer to be outputted
   * @return The random integer
   */
  public static int getRandomInt(int min, int max) {
    final ThreadLocalRandom r = ThreadLocalRandom.current();
    return r.nextInt(min, max);
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
   * This static method generates a random object of type T from a probability distribution stored
   * in a HashMap (T to probability as a double). Chooses a random number between 0 and 1 and finds
   * what outcome that number corresponds to on the cumulative distribution function.
   *
   * @param type - the type of the outputs of the distribution
   * @param distribution - the probability distribution of outputs, stored as a HashMap
   * @return the randomly generated object of type T
   * @param <T> the type of the object to be randomly generated (and in the distribution keys)
   * @throws InvalidDistributionException if the distribution is not valid.
   */
  public static <T> T generateRandomFromDistribution(Class<T> type, HashMap<T, Double> distribution)
      throws InvalidDistributionException {
    RandomGenerator.validateDistribution(type, distribution);
    double randDouble = RandomGenerator.getRandomPositiveDouble(0, 1);
    double currSum = 0;
    for (T key : distribution.keySet()) {
      currSum += distribution.get(key);
      if (randDouble < currSum) {
        return key;
      }
    }
    throw new InvalidDistributionException(
        "Distribution probabilities summed to more than 1.", distribution);
  }

  /**
   * This method validates a probability distribution stored as a HashMap by ensuring its
   * probabilities sum to 1 and are all non-negative.
   *
   * @param type - the type of the objects in the keys of the distribution
   * @param distribution - the distribution to be validated
   * @param <T> - the type of the objects in the keys of the distribution
   * @throws InvalidDistributionException if the distribution is invalid
   */
  public static <T> void validateDistribution(Class<T> type, HashMap<T, Double> distribution)
      throws InvalidDistributionException {
    double sum = 0;
    for (T key : distribution.keySet()) {
      double currProb = distribution.get(key);
      if (currProb < 0) {
        throw new InvalidDistributionException(
            "The probability associated " + "with the output " + key + " was negative.",
            distribution);
      }
      sum += currProb;
    }

    if (Math.round(sum * 1000000.0) / 1000000.0 != 1) {
      throw new InvalidDistributionException(
          "Distribution probabilities did not sum to 1.", distribution);
    }
  }
}
