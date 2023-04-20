package edu.brown.cs.student.main.models.exceptions;

import edu.brown.cs.student.main.models.markov.Emission;
import java.util.List;

/**
 * The FormatterFailureException is thrown when a given list of Emissions cannot be converted according
 * to the rules of a Formatter provided.
 */
public class FormatterFailureException extends Exception {
  final List<Emission> emissions;

  /**
   * The constructor for the FormatterFailureException, which takes in a message and the list that
   * could not be converted.
   *
   * @param message The message to be displayed upon error
   * @param emissions The list that could not be converted (leading to this error)
   */
  public FormatterFailureException(String message, List<Emission> emissions) {
    super(message);
    this.emissions = emissions;
  }
}

