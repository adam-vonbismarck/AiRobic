package edu.brown.cs.student.main.models.exceptions;

import edu.brown.cs.student.main.models.formattypes.Schedule;

/**
 * The InvalidScheduleException is thrown when a schedule to be filled in does not have the necessary
 * fields to indicate how it should be filled in.
 */
public class InvalidScheduleException extends Exception {
  final Schedule schedule;

  /**
   * The constructor for the InvalidScheduleException, which takes in a message and the invalid schedule.
   *
   * @param message The message to be displayed upon error
   * @param schedule The invalid schedule (leading to this error)
   */
  public InvalidScheduleException(String message, Schedule schedule) {
    super(message);
    this.schedule = schedule;
  }
}

