package edu.brown.cs.student.main.models.exceptions;

import java.util.HashMap;

/**
 * The FactoryFailureException is thrown when a given row cannot be converted to a specified new
 * datatype by the create method in a class implementing the CreatorFromRow interface.
 */
public class InvalidDistributionException extends Exception {
  final Object distribution;

  /**
   * The constructor for the FactoryFailureException, which takes in a message and the row that
   * could not be converted.
   *
   * @param message The message to be displayed upon error
   * @param row The row that could not be converted (leading to this error)
   */
  public InvalidDistributionException(String message, HashMap row) {
    super(message);
    this.distribution = row.clone();
  }
}

