package edu.brown.cs.student.main.models.exceptions;

import edu.brown.cs.student.main.models.markov.Emission;
import java.util.HashMap;
import java.util.List;

/**
 * The FactoryFailureException is thrown when a given row cannot be converted to a specified new
 * datatype by the create method in a class implementing the CreatorFromRow interface.
 */
public class FormatterFailureException extends Exception {
  final List<Emission> emissions;

  /**
   * The constructor for the FactoryFailureException, which takes in a message and the row that
   * could not be converted.
   *
   * @param message The message to be displayed upon error
   * @param row The row that could not be converted (leading to this error)
   */
  public FormatterFailureException(String message, List<Emission> emissions) {
    super(message);
    this.emissions = emissions;
  }
}

