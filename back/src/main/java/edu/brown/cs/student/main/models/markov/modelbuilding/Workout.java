package edu.brown.cs.student.main.models.markov.modelbuilding;

/**
 * The Workout enum associates an integer (from the enum) with a string key in our workout files.
 * Useful for searching classes like RowingWorkoutByName for a specific workout type.
 */
public enum Workout {
  _2K,
  _30R_20,
  _6K,
  UT_2,
  OVERALL,
  NONE;

  /**
   * This method returns a Workout given its associated string.
   *
   * @param workout - the string associated with a given Workout.
   * @return the Workout instance.
   */
  public static Workout of(String workout) {
    return switch (workout) {
      case "2k" -> _2K;
      case "30r20" -> _30R_20;
      case "6k" -> _6K;
      case "UT2" -> UT_2;
      case "overall" -> OVERALL;
      default -> NONE;
    };
  }

  /**
   * This method returns a string given its associated Workout.
   *
   * @param workout - the Workout associated with a given string.
   * @return the string of the Workout.
   */
  public static String value(Workout workout) {
    return switch (workout) {
      case _2K -> "2k";
      case _30R_20 -> "30r20";
      case _6K -> "6k";
      case UT_2 -> "UT2";
      case OVERALL -> "overall";
      default -> "null";
    };
  }
}
