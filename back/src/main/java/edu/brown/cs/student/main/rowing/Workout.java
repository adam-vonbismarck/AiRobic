package edu.brown.cs.student.main.rowing;

public enum Workout {

  _2K, _30R_20, _6K, UT_2, NONE;

  public static Workout of(String workout) {
    return switch (workout) {
      case "2k" -> _2K;
      case "30r20" -> _30R_20;
      case "6k" -> _6K;
      case "UT2" -> UT_2;
      default -> NONE;
    };
  }

  public static String value(Workout workout) {
    return switch (workout) {
      case _2K -> "2k";
      case _30R_20 -> "30r20";
      case _6K -> "6k";
      case UT_2 -> "UT2";
      default -> "null";
    };
  }

}
