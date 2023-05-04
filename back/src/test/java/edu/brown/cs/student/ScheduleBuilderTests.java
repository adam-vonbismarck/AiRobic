package edu.brown.cs.student;

import edu.brown.cs.student.main.rowing.Workout;
import edu.brown.cs.student.main.rowing.ScheduleBuilder;
import edu.brown.cs.student.main.models.exceptions.InvalidDistributionException;
import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.Day.WorkoutDescription;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.formattypes.Week;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ScheduleBuilderTests {

  /**
   * Tests that building a schedule with an allowed number of minutes (in the middle of the range)
   * produces a valid schedule.
   */
  @Test
  public void testScheduleBuilderMedium() {
    Schedule schedule = null;
    try {
      schedule = new ScheduleBuilder().minutes(600, 4, 0.2,
          DayOfWeek.TUESDAY, DayOfWeek.FRIDAY, Workout.of("2k"), Workout.of("UT2"));
    } catch (InvalidDistributionException e) {
      Assertions.assertEquals("All distributions were valid", e.getMessage());
    }
    Assertions.assertTrue(schedule.weeks().size() == 4);
    Assertions.assertEquals(schedule.example(), new Week("week", List.of(
        new Day("day", new ArrayList<>(), 2, DayOfWeek.MONDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 60),
                new WorkoutDescription(Workout.of("2k"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 2, DayOfWeek.TUESDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 60),
                new WorkoutDescription(Workout.of("UT2"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 1, DayOfWeek.WEDNESDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 2, DayOfWeek.THURSDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 60),
                new WorkoutDescription(Workout.of("2k"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 2, DayOfWeek.FRIDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 60),
                new WorkoutDescription(Workout.of("UT2"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 1, DayOfWeek.SATURDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 0, DayOfWeek.SUNDAY,
            null, List.of(), new ArrayList<>())
    )));
  }

  /**
   * Tests that building a schedule with an allowed number of minutes (at the max end of the range)
   * produces a valid schedule.
   */
  @Test
  public void testScheduleBuilderMax() {
    Schedule schedule = null;
    try {
      schedule = new ScheduleBuilder().minutes(1200, 5, 0.2,
          DayOfWeek.TUESDAY, DayOfWeek.FRIDAY, Workout.of("2k"), Workout.of("UT2"));
    } catch (InvalidDistributionException e) {
      Assertions.assertEquals("All distributions were valid", e.getMessage());
    }
    Assertions.assertTrue(schedule.weeks().size() == 5);
    Assertions.assertEquals(schedule.example(), new Week("week", List.of(
        new Day("day", new ArrayList<>(), 2, DayOfWeek.MONDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 160),
                new WorkoutDescription(Workout.of("2k"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 2, DayOfWeek.TUESDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 160),
                new WorkoutDescription(Workout.of("2k"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 1, DayOfWeek.WEDNESDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 160)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 2, DayOfWeek.THURSDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 160),
                new WorkoutDescription(Workout.of("2k"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 2, DayOfWeek.FRIDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 160),
                new WorkoutDescription(Workout.of("2k"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 1, DayOfWeek.SATURDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 160)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 0, DayOfWeek.SUNDAY,
            null, List.of(), new ArrayList<>())
    )));
  }

  /**
   * Tests that building a schedule with an allowed number of minutes (at the min end of the range)
   * produces a valid schedule.
   */
  @Test
  public void testScheduleBuilderMin() {
    Schedule schedule = null;
    try {
      schedule = new ScheduleBuilder().minutes(120, 4, 0.2,
          DayOfWeek.TUESDAY, DayOfWeek.FRIDAY, Workout.of("2k"), Workout.of("UT2"));
    } catch (InvalidDistributionException e) {
      Assertions.assertEquals("All distributions were valid", e.getMessage());
    }
    Assertions.assertTrue(schedule.weeks().size() == 4);
    Assertions.assertEquals(schedule.example(), new Week("week", List.of(
        new Day("day", new ArrayList<>(), 1, DayOfWeek.MONDAY,
            null, List.of(new WorkoutDescription(Workout.of("2k"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 0, DayOfWeek.TUESDAY,
            null, List.of(), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 0, DayOfWeek.WEDNESDAY,
            null, List.of(), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 1, DayOfWeek.THURSDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 0, DayOfWeek.FRIDAY,
            null, List.of(), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 0, DayOfWeek.SATURDAY,
            null, List.of(), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 0, DayOfWeek.SUNDAY,
            null, List.of(), new ArrayList<>())
    )));
  }

  /**
   * Tests that building a schedule with an allowed number of minutes (in the middle of the range)
   * and varying the high intensity workout percentage produces a valid schedule.
   */
  @Test
  public void testScheduleBuilderMediumChangeHigh() {
    Schedule schedule = null;
    try {
      schedule = new ScheduleBuilder().minutes(600, 4, 0.1,
          DayOfWeek.TUESDAY, DayOfWeek.FRIDAY, Workout.of("2k"), Workout.of("UT2"));
    } catch (InvalidDistributionException e) {
      Assertions.assertEquals("All distributions were valid", e.getMessage());
    }
    Assertions.assertTrue(schedule.weeks().size() == 4);
    Assertions.assertEquals(schedule.example(), new Week("week", List.of(
        new Day("day", new ArrayList<>(), 2, DayOfWeek.MONDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 60),
                new WorkoutDescription(Workout.of("2k"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 2, DayOfWeek.TUESDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 60),
                new WorkoutDescription(Workout.of("UT2"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 1, DayOfWeek.WEDNESDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 2, DayOfWeek.THURSDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 60),
                new WorkoutDescription(Workout.of("UT2"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 2, DayOfWeek.FRIDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 60),
                new WorkoutDescription(Workout.of("UT2"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 1, DayOfWeek.SATURDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 0, DayOfWeek.SUNDAY,
            null, List.of(), new ArrayList<>())
    )));
    Assertions.assertEquals(schedule.weeks().get(1), schedule.example());

    try {
      schedule = new ScheduleBuilder().minutes(600, 4, 0.4,
          DayOfWeek.TUESDAY, DayOfWeek.FRIDAY, Workout.of("2k"), Workout.of("UT2"));
    } catch (InvalidDistributionException e) {
      Assertions.assertEquals("All distributions were valid", e.getMessage());
    }
    Assertions.assertTrue(schedule.weeks().size() == 4);
    Assertions.assertEquals(schedule.example(), new Week("week", List.of(
        new Day("day", new ArrayList<>(), 2, DayOfWeek.MONDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 60),
                new WorkoutDescription(Workout.of("2k"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 2, DayOfWeek.TUESDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 60),
                new WorkoutDescription(Workout.of("2k"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 1, DayOfWeek.WEDNESDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 2, DayOfWeek.THURSDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 60),
                new WorkoutDescription(Workout.of("2k"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 2, DayOfWeek.FRIDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 60),
                new WorkoutDescription(Workout.of("2k"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 1, DayOfWeek.SATURDAY,
            null, List.of(new WorkoutDescription(Workout.of("UT2"), 60)), new ArrayList<>()),
        new Day("day", new ArrayList<>(), 0, DayOfWeek.SUNDAY,
            null, List.of(), new ArrayList<>())
    )));
    Assertions.assertEquals(schedule.weeks().get(1), schedule.example());
  }

  /**
   * Tests that building a schedule with an allowed number of minutes (in the middle of the range)
   * and varying the high intensity workout percentage produces a valid schedule.
   */
  @Test
  public void testScheduleBuilderBadHigh() {
    Exception exn =
        Assertions.assertThrows(
            InvalidDistributionException.class,
            () -> {
              new ScheduleBuilder().minutes(600, 4, -0.1,
                  null, DayOfWeek.FRIDAY, null, Workout.of("UT2"));
            });
    Assertions.assertEquals(
        exn.getMessage(),
        "The probability associated with the output " + "High intensity" + " was negative.");
  }

}
