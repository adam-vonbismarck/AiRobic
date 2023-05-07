package edu.brown.cs.student;

import edu.brown.cs.student.main.models.exceptions.InvalidDistributionException;
import edu.brown.cs.student.main.models.exceptions.InvalidScheduleException;
import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.Day.WorkoutDescription;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.formattypes.Week;
import edu.brown.cs.student.main.models.markov.modelbuilding.Workout;
import edu.brown.cs.student.main.rowing.modelbuilders.ScheduleBuilder;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * This class begins by testing the minutes and workouts methods of the ScheduleBuilder, then verifies that
 * minutesWithDates builds appropriately off of the minutes method. It tests normal conditions of minutes/workouts
 * with the minutes call, and separately tests edge cases of the workouts call.
 */
public class ScheduleBuilderTests {

  /**
   * Tests that building a schedule with an allowed number of minutes (in the middle of the range)
   * produces a valid schedule.
   */
  @Test
  public void testScheduleBuilderMedium() throws InvalidScheduleException {
    Schedule schedule = null;
    try {
      schedule =
          new ScheduleBuilder()
              .minutes(
                  600,
                  4,
                  0.2,
                  DayOfWeek.TUESDAY,
                  DayOfWeek.FRIDAY,
                  Workout.of("2k"),
                  Workout.of("UT2"));
    } catch (InvalidDistributionException | InvalidScheduleException e) {
      Assertions.assertEquals("All distributions were valid", e.getMessage());
    }
    Assertions.assertTrue(schedule.weeks().size() == 4);
    Assertions.assertEquals(
        schedule.example(),
        new Week(
            "week",
            List.of(
                new Day(
                    "day",
                    new ArrayList<>(),
                    2,
                    DayOfWeek.MONDAY,
                    Optional.empty(),
                    List.of(
                        new WorkoutDescription(Workout.of("UT2"), 60),
                        new WorkoutDescription(Workout.of("2k"), 60))),
                new Day(
                    "day",
                    new ArrayList<>(),
                    2,
                    DayOfWeek.TUESDAY,
                    Optional.empty(),
                    List.of(
                        new WorkoutDescription(Workout.of("UT2"), 60),
                        new WorkoutDescription(Workout.of("UT2"), 60))),
                new Day(
                    "day",
                    new ArrayList<>(),
                    1,
                    DayOfWeek.WEDNESDAY,
                    Optional.empty(),
                    List.of(new WorkoutDescription(Workout.of("UT2"), 60))),
                new Day(
                    "day",
                    new ArrayList<>(),
                    2,
                    DayOfWeek.THURSDAY,
                    Optional.empty(),
                    List.of(
                        new WorkoutDescription(Workout.of("UT2"), 60),
                        new WorkoutDescription(Workout.of("2k"), 60))),
                new Day(
                    "day",
                    new ArrayList<>(),
                    2,
                    DayOfWeek.FRIDAY,
                    Optional.empty(),
                    List.of(
                        new WorkoutDescription(Workout.of("UT2"), 60),
                        new WorkoutDescription(Workout.of("UT2"), 60))),
                new Day(
                    "day",
                    new ArrayList<>(),
                    1,
                    DayOfWeek.SATURDAY,
                    Optional.empty(),
                    List.of(new WorkoutDescription(Workout.of("UT2"), 60))),
                new Day(
                    "day", new ArrayList<>(), 0, DayOfWeek.SUNDAY, Optional.empty(), List.of()))));
  }

  /**
   * Tests that building a schedule with an allowed number of minutes (at the max end of the range)
   * produces a valid schedule.
   */
  @Test
  public void testScheduleBuilderMax() throws InvalidScheduleException {
    Schedule schedule = null;
    try {
      schedule =
          new ScheduleBuilder()
              .minutes(
                  1200,
                  5,
                  0.2,
                  DayOfWeek.TUESDAY,
                  DayOfWeek.FRIDAY,
                  Workout.of("2k"),
                  Workout.of("UT2"));
    } catch (InvalidDistributionException | InvalidScheduleException e) {
      Assertions.assertEquals("All distributions were valid", e.getMessage());
    }
    Assertions.assertTrue(schedule.weeks().size() == 5);
    Assertions.assertEquals(
        schedule.example(),
        new Week(
            "week",
            List.of(
                new Day(
                    "day",
                    new ArrayList<>(),
                    2,
                    DayOfWeek.MONDAY,
                    Optional.empty(),
                    List.of(
                        new WorkoutDescription(Workout.of("UT2"), 160),
                        new WorkoutDescription(Workout.of("2k"), 60))),
                new Day(
                    "day",
                    new ArrayList<>(),
                    2,
                    DayOfWeek.TUESDAY,
                    Optional.empty(),
                    List.of(
                        new WorkoutDescription(Workout.of("UT2"), 160),
                        new WorkoutDescription(Workout.of("2k"), 60))),
                new Day(
                    "day",
                    new ArrayList<>(),
                    1,
                    DayOfWeek.WEDNESDAY,
                    Optional.empty(),
                    List.of(new WorkoutDescription(Workout.of("UT2"), 160))),
                new Day(
                    "day",
                    new ArrayList<>(),
                    2,
                    DayOfWeek.THURSDAY,
                    Optional.empty(),
                    List.of(
                        new WorkoutDescription(Workout.of("UT2"), 160),
                        new WorkoutDescription(Workout.of("2k"), 60))),
                new Day(
                    "day",
                    new ArrayList<>(),
                    2,
                    DayOfWeek.FRIDAY,
                    Optional.empty(),
                    List.of(
                        new WorkoutDescription(Workout.of("UT2"), 160),
                        new WorkoutDescription(Workout.of("2k"), 60))),
                new Day(
                    "day",
                    new ArrayList<>(),
                    1,
                    DayOfWeek.SATURDAY,
                    Optional.empty(),
                    List.of(new WorkoutDescription(Workout.of("UT2"), 160))),
                new Day(
                    "day", new ArrayList<>(), 0, DayOfWeek.SUNDAY, Optional.empty(), List.of()))));
  }

  /**
   * Tests that building a schedule with an allowed number of minutes (at the min end of the range)
   * produces a valid schedule.
   */
  @Test
  public void testScheduleBuilderMin() throws InvalidScheduleException {
    Schedule schedule = null;
    try {
      schedule =
          new ScheduleBuilder()
              .minutes(
                  120,
                  4,
                  0.2,
                  DayOfWeek.TUESDAY,
                  DayOfWeek.FRIDAY,
                  Workout.of("2k"),
                  Workout.of("UT2"));
    } catch (InvalidDistributionException | InvalidScheduleException e) {
      Assertions.assertEquals("All distributions were valid", e.getMessage());
    }
    Assertions.assertTrue(schedule.weeks().size() == 4);
    Assertions.assertEquals(
        schedule.example(),
        new Week(
            "week",
            List.of(
                new Day(
                    "day",
                    new ArrayList<>(),
                    1,
                    DayOfWeek.MONDAY,
                    Optional.empty(),
                    List.of(new WorkoutDescription(Workout.of("2k"), 60))),
                new Day(
                    "day", new ArrayList<>(), 0, DayOfWeek.TUESDAY, Optional.empty(), List.of()),
                new Day(
                    "day", new ArrayList<>(), 0, DayOfWeek.WEDNESDAY, Optional.empty(), List.of()),
                new Day(
                    "day",
                    new ArrayList<>(),
                    1,
                    DayOfWeek.THURSDAY,
                    Optional.empty(),
                    List.of(new WorkoutDescription(Workout.of("UT2"), 60))),
                new Day("day", new ArrayList<>(), 0, DayOfWeek.FRIDAY, Optional.empty(), List.of()),
                new Day(
                    "day", new ArrayList<>(), 0, DayOfWeek.SATURDAY, Optional.empty(), List.of()),
                new Day(
                    "day", new ArrayList<>(), 0, DayOfWeek.SUNDAY, Optional.empty(), List.of()))));
  }

  /**
   * Tests that building a schedule with an allowed number of minutes (in the middle of the range)
   * and varying the high workoutType workout percentage produces a valid schedule.
   */
  @Test
  public void testScheduleBuilderMediumChangeHigh() throws InvalidScheduleException {
    Schedule schedule = null;
    try {
      schedule =
          new ScheduleBuilder()
              .minutes(
                  600,
                  4,
                  0.1,
                  DayOfWeek.TUESDAY,
                  DayOfWeek.FRIDAY,
                  Workout.of("2k"),
                  Workout.of("UT2"));
    } catch (InvalidDistributionException | InvalidScheduleException e) {
      Assertions.assertEquals("All distributions were valid", e.getMessage());
    }
    Assertions.assertTrue(schedule.weeks().size() == 4);
    Assertions.assertEquals(
        schedule.example(),
        new Week(
            "week",
            List.of(
                new Day(
                    "day",
                    new ArrayList<>(),
                    2,
                    DayOfWeek.MONDAY,
                    Optional.empty(),
                    List.of(
                        new WorkoutDescription(Workout.of("UT2"), 60),
                        new WorkoutDescription(Workout.of("2k"), 60))),
                new Day(
                    "day",
                    new ArrayList<>(),
                    2,
                    DayOfWeek.TUESDAY,
                    Optional.empty(),
                    List.of(
                        new WorkoutDescription(Workout.of("UT2"), 60),
                        new WorkoutDescription(Workout.of("UT2"), 60))),
                new Day(
                    "day",
                    new ArrayList<>(),
                    1,
                    DayOfWeek.WEDNESDAY,
                    Optional.empty(),
                    List.of(new WorkoutDescription(Workout.of("UT2"), 60))),
                new Day(
                    "day",
                    new ArrayList<>(),
                    2,
                    DayOfWeek.THURSDAY,
                    Optional.empty(),
                    List.of(
                        new WorkoutDescription(Workout.of("UT2"), 60),
                        new WorkoutDescription(Workout.of("UT2"), 60))),
                new Day(
                    "day",
                    new ArrayList<>(),
                    2,
                    DayOfWeek.FRIDAY,
                    Optional.empty(),
                    List.of(
                        new WorkoutDescription(Workout.of("UT2"), 60),
                        new WorkoutDescription(Workout.of("UT2"), 60))),
                new Day(
                    "day",
                    new ArrayList<>(),
                    1,
                    DayOfWeek.SATURDAY,
                    Optional.empty(),
                    List.of(new WorkoutDescription(Workout.of("UT2"), 60))),
                new Day(
                    "day", new ArrayList<>(), 0, DayOfWeek.SUNDAY, Optional.empty(), List.of()))));
    Assertions.assertEquals(schedule.weeks().get(1), schedule.example());

    try {
      schedule =
          new ScheduleBuilder()
              .minutes(
                  600,
                  4,
                  0.4,
                  DayOfWeek.TUESDAY,
                  DayOfWeek.FRIDAY,
                  Workout.of("2k"),
                  Workout.of("UT2"));
    } catch (InvalidDistributionException | InvalidScheduleException e) {
      Assertions.assertEquals("All distributions were valid", e.getMessage());
    }
    Assertions.assertTrue(schedule.weeks().size() == 4);
    Assertions.assertEquals(
        schedule.example(),
        new Week(
            "week",
            List.of(
                new Day(
                    "day",
                    new ArrayList<>(),
                    2,
                    DayOfWeek.MONDAY,
                    Optional.empty(),
                    List.of(
                        new WorkoutDescription(Workout.of("UT2"), 60),
                        new WorkoutDescription(Workout.of("2k"), 60))),
                new Day(
                    "day",
                    new ArrayList<>(),
                    2,
                    DayOfWeek.TUESDAY,
                    Optional.empty(),
                    List.of(
                        new WorkoutDescription(Workout.of("UT2"), 60),
                        new WorkoutDescription(Workout.of("2k"), 60))),
                new Day(
                    "day",
                    new ArrayList<>(),
                    1,
                    DayOfWeek.WEDNESDAY,
                    Optional.empty(),
                    List.of(new WorkoutDescription(Workout.of("UT2"), 60))),
                new Day(
                    "day",
                    new ArrayList<>(),
                    2,
                    DayOfWeek.THURSDAY,
                    Optional.empty(),
                    List.of(
                        new WorkoutDescription(Workout.of("UT2"), 60),
                        new WorkoutDescription(Workout.of("2k"), 60))),
                new Day(
                    "day",
                    new ArrayList<>(),
                    2,
                    DayOfWeek.FRIDAY,
                    Optional.empty(),
                    List.of(
                        new WorkoutDescription(Workout.of("UT2"), 60),
                        new WorkoutDescription(Workout.of("2k"), 60))),
                new Day(
                    "day",
                    new ArrayList<>(),
                    1,
                    DayOfWeek.SATURDAY,
                    Optional.empty(),
                    List.of(new WorkoutDescription(Workout.of("UT2"), 60))),
                new Day(
                    "day", new ArrayList<>(), 0, DayOfWeek.SUNDAY, Optional.empty(), List.of()))));
    Assertions.assertEquals(schedule.weeks().get(1), schedule.example());
  }

  /**
   * Tests that building a schedule with an allowed number of minutes (in the middle of the range)
   * and varying the high workoutType workout percentage produces a valid schedule.
   */
  @Test
  public void testScheduleBuilderBadHigh() {
    Exception exn =
        Assertions.assertThrows(
            InvalidDistributionException.class,
            () -> {
              new ScheduleBuilder()
                  .minutes(600, 4, -0.1, null, DayOfWeek.FRIDAY, null, Workout.of("UT2"));
            });
    Assertions.assertEquals(
        exn.getMessage(),
        "The probability associated with the output " + "High workoutType" + " was negative.");
  }

  /**
   * Tests additional edge cases for the ScheduleBuilder minutes() call.
   */
  @Test
  public void testMinutesFailures() {
    Exception exn =
            Assertions.assertThrows(
                    InvalidScheduleException.class,
                    () -> {
                      new ScheduleBuilder()
                              .minutes(0, 3, 0.2,
                                      DayOfWeek.TUESDAY, DayOfWeek.MONDAY, Workout._2K, Workout.UT_2);
                    });
    Assertions.assertEquals(
            exn.getMessage(),
            "All schedules must have a number of minutes between 120 and 1200.");
    exn =
            Assertions.assertThrows(
                    InvalidScheduleException.class,
                    () -> {
                      new ScheduleBuilder()
                              .minutes(2000, 3, 0.2,
                                      DayOfWeek.TUESDAY, DayOfWeek.MONDAY, Workout._2K, Workout.UT_2);
                    });
    Assertions.assertEquals(
            exn.getMessage(),
            "All schedules must have a number of minutes between 120 and 1200.");
    exn =
            Assertions.assertThrows(
                    InvalidScheduleException.class,
                    () -> {
                      new ScheduleBuilder()
                              .minutes(600, 1, 0.2,
                                      DayOfWeek.TUESDAY, DayOfWeek.MONDAY, Workout._2K, Workout.UT_2);
                    });
    Assertions.assertEquals(
            exn.getMessage(),
            "All schedules must have start days before end days in order to be built.");
    exn =
            Assertions.assertThrows(
                    InvalidScheduleException.class,
                    () -> {
                      new ScheduleBuilder()
                              .minutes(600, 0, 0.2,
                                      DayOfWeek.TUESDAY, DayOfWeek.MONDAY, Workout._2K, Workout.UT_2);
                    });
    Assertions.assertEquals(
            exn.getMessage(),
            "All schedules must have a positive number of weeks.");
  }

  /**
   * Tests additional edge cases for the ScheduleBuilder workouts() call.
   */
  @Test
  public void testWorkoutsFailures() {
    Exception exn =
            Assertions.assertThrows(
                    InvalidScheduleException.class,
                    () -> {
                      new ScheduleBuilder()
                              .workouts(-1, 0, 2,
                                      DayOfWeek.TUESDAY, DayOfWeek.MONDAY, Workout._2K, Workout.UT_2,
                                      40);
                    });
    Assertions.assertEquals(
            exn.getMessage(),
            "All schedules must have a positive number of weeks, high intensity workouts " +
                    "and low intensity workouts.");
    exn =
            Assertions.assertThrows(
                    InvalidScheduleException.class,
                    () -> {
                      new ScheduleBuilder()
                              .workouts(2, -1, 2,
                                      DayOfWeek.TUESDAY, DayOfWeek.MONDAY, Workout._2K, Workout.UT_2,
                                      40);
                    });
    Assertions.assertEquals(
            exn.getMessage(),
            "All schedules must have a positive number of weeks, high intensity workouts " +
                    "and low intensity workouts.");
    exn =
            Assertions.assertThrows(
                    InvalidScheduleException.class,
                    () -> {
                      new ScheduleBuilder()
                              .workouts(4, 6, 1,
                                      DayOfWeek.TUESDAY, DayOfWeek.MONDAY, Workout._2K, Workout.UT_2,
                                      40);
                    });
    Assertions.assertEquals(
            exn.getMessage(),
            "All schedules must have start days before end days in order to be built.");
    exn =
            Assertions.assertThrows(
                    InvalidScheduleException.class,
                    () -> {
                      new ScheduleBuilder()
                              .workouts(4, 6, 3,
                                      DayOfWeek.TUESDAY, DayOfWeek.MONDAY, Workout._2K, Workout.UT_2,
                                      -1);
                    });
    Assertions.assertEquals(
            exn.getMessage(),
            "All schedules must have a positive low intensity workout length.");
    exn =
            Assertions.assertThrows(
                    InvalidScheduleException.class,
                    () -> {
                      new ScheduleBuilder()
                              .workouts(4, 6, 0,
                                      DayOfWeek.TUESDAY, DayOfWeek.MONDAY, Workout._2K, Workout.UT_2,
                                      60);
                    });
    Assertions.assertEquals(
            exn.getMessage(),
            "All schedules must have a positive number of weeks, high intensity workouts " +
                    "and low intensity workouts.");
  }

  /**
   * Tests a short plan that requires loaded dates.
   */
  @Test
  public void testMinutesWithDatesShort() {
    Schedule schedule = null;
    try {
      schedule =
              new ScheduleBuilder()
                      .minutesWithDates(
                              600,
                              LocalDate.of(2023,5,2),
                              LocalDate.of(2023,5,5),
                              0.2,
                              Workout.of("2k"),
                              Workout.of("UT2"));
    } catch (InvalidDistributionException | InvalidScheduleException e) {
      Assertions.assertEquals("All distributions were valid", e.getMessage());
    }

    Assertions.assertEquals(schedule.weeks().size(), 1);
    Assertions.assertEquals(schedule.getLength(), 7);
    Assertions.assertEquals(schedule.weeks().get(0).days().get(0).getDate().get(), LocalDate.of(2023,5,2));
    Assertions.assertEquals(schedule.weeks().get(schedule.weeks().size() - 1)
            .days().get(schedule.weeks().get(schedule.weeks().size() - 1).days().size() - 1).getDate().get(), LocalDate.of(2023,5,5));
  }

  /**
   * Tests a medium length plan that requires loaded dates.
   */
  @Test
  public void testMinutesWithDatesMedium() {
    Schedule schedule = null;
    try {
      schedule =
              new ScheduleBuilder()
                      .minutesWithDates(
                              600,
                              LocalDate.of(2023,5,2),
                              LocalDate.of(2023,5,30),
                              0.2,
                              Workout.of("2k"),
                              Workout.of("UT2"));
    } catch (InvalidDistributionException | InvalidScheduleException e) {
      Assertions.assertEquals("All distributions were valid", e.getMessage());
    }

    Assertions.assertEquals(schedule.weeks().size(), 5);
    Assertions.assertEquals(schedule.getLength(), 42);
    Assertions.assertEquals(schedule.weeks().get(0).days().get(0).getDate().get(), LocalDate.of(2023,5,2));
    Assertions.assertEquals(schedule.weeks().get(schedule.weeks().size() - 1)
            .days().get(schedule.weeks().get(schedule.weeks().size() - 1).days().size() - 1).getDate().get(), LocalDate.of(2023,5,30));
  }

  /**
   * Tests a multi-year plan that requires loaded dates.
   */
  @Test
  public void testMinutesWithDatesLong() {
    Schedule schedule = null;
    try {
      schedule =
              new ScheduleBuilder()
                      .minutesWithDates(
                              600,
                              LocalDate.of(2023,5,2),
                              LocalDate.of(2025,2,25),
                              0.2,
                              Workout.of("2k"),
                              Workout.of("UT2"));
    } catch (InvalidDistributionException | InvalidScheduleException e) {
      Assertions.assertEquals("All distributions were valid", e.getMessage());
    }

    Assertions.assertEquals(schedule.weeks().size(), 96);
    Assertions.assertEquals(schedule.getLength(), 952);
    Assertions.assertEquals(schedule.weeks().get(0).days().get(0).getDate().get(),
            LocalDate.of(2023,5,2));
    Assertions.assertEquals(schedule.weeks().get(schedule.weeks().size() - 1)
            .days().get(schedule.weeks().get(schedule.weeks().size() - 1).days().size() - 1).getDate().get(),
            LocalDate.of(2025,2,25));
  }

  /**
   * Tests a short plan that requires loaded dates, with all days of the week for start/end (to catch any
   * edge cases).
   */
  @Test
  public void testMinutesWithDatesAllDaysOfWeekNoAssertionErr() {
    Schedule schedule = null;
    try {
      for (int i = 1; i < 8; i++) {
        for (int j = 10; j < 17; j++) {
          schedule =
                  new ScheduleBuilder()
                          .minutesWithDates(
                                  600,
                                  LocalDate.of(2023, 5, i),
                                  LocalDate.of(2025, 2, j),
                                  0.2,
                                  Workout.of("2k"),
                                  Workout.of("UT2"));
          Assertions.assertEquals(schedule.weeks().get(0).days().get(0).getDate().get(),
                  LocalDate.of(2023,5,i));
          Assertions.assertEquals(schedule.weeks().get(schedule.weeks().size() - 1)
                          .days().get(schedule.weeks().get(schedule.weeks().size() - 1).days().size() - 1).getDate().get(),
                  LocalDate.of(2025,2,j));
        }
      }
    } catch (InvalidDistributionException | InvalidScheduleException e) {
      Assertions.assertEquals("All distributions and schedules were valid", e.getMessage());
    }
  }

  /**
   * Tests additional edge cases for the minutesWithDates() call.
   */
  @Test
  public void testMinutesWithDatesFailures() {
    Exception exn =
            Assertions.assertThrows(
                    InvalidScheduleException.class,
                    () -> {
                      new ScheduleBuilder()
                              .minutesWithDates(
                                      600,
                                      LocalDate.of(2023, 5, 4),
                                      LocalDate.of(2021, 2, 2),
                                      0.2,
                                      Workout.of("2k"),
                                      Workout.of("UT2"));
                    });
    Assertions.assertEquals(
            exn.getMessage(),
            "All schedules must have start days before end days in order to be built.");
    exn =
            Assertions.assertThrows(
                    InvalidScheduleException.class,
                    () -> {
                      new ScheduleBuilder()
                              .minutesWithDates(
                                      600,
                                      null,
                                      LocalDate.of(2021, 2, 2),
                                      0.2,
                                      Workout.of("2k"),
                                      Workout.of("UT2"));
                    });
    Assertions.assertEquals(
            exn.getMessage(),
            "All schedules must have not null start days and end days in order to be built.");
    exn =
            Assertions.assertThrows(
                    InvalidScheduleException.class,
                    () -> {
                      new ScheduleBuilder()
                              .minutesWithDates(
                                      20,
                                      LocalDate.of(2021, 1, 1),
                                      LocalDate.of(2021, 2, 2),
                                      0.2,
                                      Workout.of("2k"),
                                      Workout.of("UT2"));
                    });
    Assertions.assertEquals(
            exn.getMessage(),
            "All schedules must have a number of minutes between 120 and 1200.");
    exn =
            Assertions.assertThrows(
                    InvalidScheduleException.class,
                    () -> {
                      new ScheduleBuilder()
                              .minutesWithDates(
                                      2000,
                                      LocalDate.of(2021, 1, 1),
                                      LocalDate.of(2021, 2, 2),
                                      0.2,
                                      Workout.of("2k"),
                                      Workout.of("UT2"));
                    });
    Assertions.assertEquals(
            exn.getMessage(),
            "All schedules must have a number of minutes between 120 and 1200.");
  }

}
