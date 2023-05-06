package edu.brown.cs.student.main.models.exceptions;

/** The NoWorkoutTypeException is thrown when the NONE type of RowingWorkout is ever used. */
public class InvalidDatesException extends Exception {

  /**
   * The constructor for the NoWorkoutTypeException, which takes in a message.
   *
   * @param message The message to be displayed upon error
   */
  public InvalidDatesException(String message) {
    super(message);
  }
}
