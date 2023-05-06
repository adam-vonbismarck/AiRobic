package edu.brown.cs.student.main.models.exceptions;

/**
 * The InvalidDatesException is thrown when an invalid date is passed into some context (such as the
 * start date of a schedule being before the end date)
 */
public class InvalidDatesException extends Exception {

  /**
   * The constructor for the InvalidDatesException, which takes in a message.
   *
   * @param message The message to be displayed upon error
   */
  public InvalidDatesException(String message) {
    super(message);
  }
}
