package edu.brown.cs.student.main.models.exceptions;

import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.Schedule;

/**
 * The FactoryFailureException is thrown when a given row cannot be converted to a specified new
 * datatype by the create method in a class implementing the CreatorFromRow interface.
 */
public class InvalidScheduleException extends Exception {
  final Schedule schedule;

  /**
   * The constructor for the FactoryFailureException, which takes in a message and the row that
   * could not be converted.
   *
   * @param message The message to be displayed upon error
   * @param day The row that could not be converted (leading to this error)
   */
  public InvalidScheduleException(String message, Schedule schedule) {
    super(message);
    this.schedule = schedule;
  }
}

