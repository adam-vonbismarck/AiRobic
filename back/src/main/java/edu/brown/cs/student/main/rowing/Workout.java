package edu.brown.cs.student.main.rowing;

public enum Workout {

  TWOKM, THIRTYRTWENTY, SIXKM, UTT, NONE;

  public static Workout of(String workout) {
    return switch (workout) {
      case "2k" -> TWOKM;
      case "30r20" -> THIRTYRTWENTY;
      case "6k" -> SIXKM;
      case "UT2" -> UTT;
      default -> NONE;
    };
  }

  public static String value(Workout workout) {
    return switch (workout) {
      case TWOKM -> "2k";
      case THIRTYRTWENTY -> "30r20";
      case SIXKM -> "6k";
      case UTT -> "UT2";
      default -> "null";
    };
  }

}
