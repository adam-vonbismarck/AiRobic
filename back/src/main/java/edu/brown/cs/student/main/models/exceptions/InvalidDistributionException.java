package edu.brown.cs.student.main.models.exceptions;

import java.util.HashMap;

/**
 * The InvalidDistributionException is thrown when a model or hidden state is produced with an
 * invalid probability distribution (probabilities don't sum to 1 and/or negative probabilities).
 */
public class InvalidDistributionException extends Exception {
  final Object distribution;

  /**
   * The constructor for the InvalidDistributionException, which takes in a message and the
   * distribution that was invalid.
   *
   * @param message The message to be displayed upon error
   * @param dist The invalid distribution (leading to this error)
   */
  public InvalidDistributionException(String message, HashMap dist) {
    super(message);
    this.distribution = dist.clone();
  }
}
