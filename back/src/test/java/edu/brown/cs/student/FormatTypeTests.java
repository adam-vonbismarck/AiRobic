package edu.brown.cs.student;

import edu.brown.cs.student.main.models.exceptions.InvalidScheduleException;
import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.formattypes.Week;
import edu.brown.cs.student.main.models.markov.model.Emission;
import edu.brown.cs.student.main.models.markov.modelbuilding.Workout;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests the formatting types we used for Schedules. */
public class FormatTypeTests {

  private Day testDay;

  /** Sets up an example day. */
  @BeforeEach
  public void setup() throws InvalidScheduleException {
    this.testDay =
        new Day(
            "day",
            new ArrayList<>(),
            0,
            DayOfWeek.FRIDAY,
            Optional.of(LocalDate.of(2023, 5, 5)),
            new ArrayList<>());
  }

  /** Tests the getters for the Day class. */
  @Test
  public void testDayGetters() {
    Assertions.assertEquals(this.testDay.getDay(), DayOfWeek.FRIDAY);
    Assertions.assertEquals(this.testDay.getDate(), Optional.of(LocalDate.of(2023, 5, 5)));
    Assertions.assertEquals(this.testDay.getEmissions(), new ArrayList<>());
    Assertions.assertEquals(this.testDay.getIntensityLength(), 0);
    Assertions.assertEquals(this.testDay.getNumberOfWorkouts(), 0);
    Assertions.assertEquals(this.testDay.getPlanCopy(), new ArrayList<>());
  }

  /** Tests some of the setters for the Day class. */
  @Test
  public void testDayDateIntensitySettersValid() throws InvalidScheduleException {
    this.testDay.setDate(LocalDate.of(2023, 5, 12));
    this.testDay.addFirstIntensity(new Day.WorkoutDescription(Workout._30R_20, 30));
    this.testDay.addFirstIntensity(new Day.WorkoutDescription(Workout._2K, 30));
    Assertions.assertEquals(this.testDay.getDay(), DayOfWeek.FRIDAY);
    Assertions.assertEquals(this.testDay.getDate(), Optional.of(LocalDate.of(2023, 5, 12)));
    Assertions.assertEquals(this.testDay.getEmissions(), new ArrayList<>());
    Assertions.assertEquals(this.testDay.getIntensityLength(), 2);
    Assertions.assertEquals(this.testDay.getNumberOfWorkouts(), 0);
    Assertions.assertEquals(
        this.testDay.getPlanCopy(),
        new ArrayList<>(
            List.of(
                new Day.WorkoutDescription(Workout._2K, 30),
                new Day.WorkoutDescription(Workout._30R_20, 30))));
    Assertions.assertFalse(this.testDay.verifyDay());
  }

  /** Tests more of the setters for the Day class, including the numWorkouts setter. */
  @Test
  public void testDayDateNumWorkoutsSetterValid() throws InvalidScheduleException {
    this.testDay.setDate(LocalDate.of(2023, 5, 12));
    this.testDay.incrementNumWorkouts();
    this.testDay.incrementNumWorkouts();
    this.testDay.addFirstIntensity(new Day.WorkoutDescription(Workout._2K, 30));
    Assertions.assertEquals(this.testDay.getDay(), DayOfWeek.FRIDAY);
    Assertions.assertEquals(this.testDay.getDate(), Optional.of(LocalDate.of(2023, 5, 12)));
    Assertions.assertEquals(this.testDay.getEmissions(), new ArrayList<>());
    Assertions.assertEquals(this.testDay.getIntensityLength(), 1);
    Assertions.assertEquals(this.testDay.getNumberOfWorkouts(), 2);
    Assertions.assertEquals(
        this.testDay.getPlanCopy(),
        new ArrayList<>(List.of(new Day.WorkoutDescription(Workout._2K, 30))));
    Assertions.assertFalse(this.testDay.verifyDay());
  }

  /** Tests a valid verified day. */
  @Test
  public void testDayVerifyDayValid() throws InvalidScheduleException {
    this.testDay.setDate(LocalDate.of(2023, 5, 12));
    this.testDay.addFirstIntensity(new Day.WorkoutDescription(Workout._30R_20, 30));
    this.testDay.addFirstIntensity(new Day.WorkoutDescription(Workout._2K, 30));
    this.testDay.incrementNumWorkouts();
    this.testDay.incrementNumWorkouts();
    Assertions.assertEquals(this.testDay.getDay(), DayOfWeek.FRIDAY);
    Assertions.assertEquals(this.testDay.getDate(), Optional.of(LocalDate.of(2023, 5, 12)));
    Assertions.assertEquals(this.testDay.getEmissions(), new ArrayList<>());
    Assertions.assertEquals(this.testDay.getIntensityLength(), 2);
    Assertions.assertEquals(this.testDay.getNumberOfWorkouts(), 2);
    Assertions.assertEquals(
        this.testDay.getPlanCopy(),
        new ArrayList<>(
            List.of(
                new Day.WorkoutDescription(Workout._2K, 30),
                new Day.WorkoutDescription(Workout._30R_20, 30))));
    Assertions.assertTrue(this.testDay.verifyDay());
  }

  @Test
  public void testDayAddingEmissions() throws InvalidScheduleException {
    this.testDay.setDate(LocalDate.of(2023, 5, 12));
    this.testDay.addFirstIntensity(new Day.WorkoutDescription(Workout._30R_20, 30));
    this.testDay.addFirstIntensity(new Day.WorkoutDescription(Workout._2K, 30));
    this.testDay.incrementNumWorkouts();
    this.testDay.incrementNumWorkouts();
    this.testDay.addWorkout(new Emission("workout", 20, "workout1"));
    Assertions.assertEquals(this.testDay.getDay(), DayOfWeek.FRIDAY);
    Assertions.assertEquals(this.testDay.getDate(), Optional.of(LocalDate.of(2023, 5, 12)));
    Assertions.assertEquals(
        this.testDay.getEmissions(),
        new ArrayList<>(List.of(new Emission("workout", 20, "workout1"))));
    Assertions.assertEquals(this.testDay.getIntensityLength(), 2);
    Assertions.assertEquals(this.testDay.getNumberOfWorkouts(), 2);
    Assertions.assertEquals(
        this.testDay.getPlanCopy(),
        new ArrayList<>(
            List.of(
                new Day.WorkoutDescription(Workout._2K, 30),
                new Day.WorkoutDescription(Workout._30R_20, 30))));
    Assertions.assertTrue(this.testDay.verifyDay());
    Assertions.assertEquals(this.testDay, this.testDay.copy());
  }

  @Test
  public void testDayInvalid() {
    Exception exn =
        Assertions.assertThrows(
            InvalidScheduleException.class,
            () -> {
              new Day(
                  null, null, 0, null, Optional.of(LocalDate.of(2023, 4, 5)), new ArrayList<>());
            });
    Assertions.assertEquals(exn.getMessage(), "Day was initialized with bad parameters.");
    exn =
        Assertions.assertThrows(
            InvalidScheduleException.class,
            () -> {
              new Day("day", new ArrayList<>(), 0, DayOfWeek.FRIDAY, null, new ArrayList<>());
            });
    Assertions.assertEquals(exn.getMessage(), "Day was initialized with bad parameters.");
    exn =
        Assertions.assertThrows(
            InvalidScheduleException.class,
            () -> {
              new Day(
                  "day",
                  new ArrayList<>(),
                  0,
                  DayOfWeek.FRIDAY,
                  Optional.of(LocalDate.of(2023, 5, 2)),
                  new ArrayList<>());
            });
    Assertions.assertEquals(
        exn.getMessage(), "Day was initialized with date not matching the day of the week.");
    exn =
        Assertions.assertThrows(
            InvalidScheduleException.class,
            () -> {
              this.testDay.setDate(LocalDate.of(2023, 5, 4));
            });
    Assertions.assertEquals(exn.getMessage(), "Date did not match this Day's day of the week.");
  }

  @Test
  public void testWeekValid() throws InvalidScheduleException {
    List<Day> days = new ArrayList<>();
    days.add(this.testDay);
    days.add(
        new Day(
            "day",
            new ArrayList<>(),
            7,
            DayOfWeek.MONDAY,
            Optional.of(LocalDate.of(2023, 5, 8)),
            new ArrayList<>()));
    Week week = new Week("week", days);
    Assertions.assertEquals(week.getDaySubset(0, 1), List.of(this.testDay));
    Assertions.assertEquals(
        week.getDaySubset(0, 2),
        List.of(
            this.testDay,
            new Day(
                "day",
                new ArrayList<>(),
                7,
                DayOfWeek.MONDAY,
                Optional.of(LocalDate.of(2023, 5, 8)),
                new ArrayList<>())));
  }

  @Test
  public void testWeekInvalid() throws InvalidScheduleException {
    List<Day> days = new ArrayList<>();
    days.add(this.testDay);
    days.add(
        new Day(
            "day",
            new ArrayList<>(),
            7,
            DayOfWeek.MONDAY,
            Optional.of(LocalDate.of(2023, 5, 8)),
            new ArrayList<>()));
    Week week = new Week("week", days);
    Exception exn =
        Assertions.assertThrows(
            InvalidScheduleException.class,
            () -> {
              week.getDaySubset(1, 0);
            });
    Assertions.assertEquals(
        exn.getMessage(), "Unable to get week sublist with overlapping or out of bounds indices.");
    exn =
        Assertions.assertThrows(
            InvalidScheduleException.class,
            () -> {
              week.getDaySubset(-1, 0);
            });
    Assertions.assertEquals(
        exn.getMessage(), "Unable to get week sublist with overlapping or out of bounds indices.");
    exn =
        Assertions.assertThrows(
            InvalidScheduleException.class,
            () -> {
              week.getDaySubset(0, 3);
            });
    Assertions.assertEquals(
        exn.getMessage(), "Unable to get week sublist with overlapping or out of bounds indices.");
  }

  @Test
  public void testScheduleFlattenWithDays() throws InvalidScheduleException {
    List<Day> days = new ArrayList<>();
    days.add(this.testDay);
    days.add(
        new Day(
            "day",
            new ArrayList<>(),
            7,
            DayOfWeek.MONDAY,
            Optional.of(LocalDate.of(2023, 5, 8)),
            new ArrayList<>()));
    List<Day> daysTwo = new ArrayList<>();
    daysTwo.add(
        new Day(
            "day",
            new ArrayList<>(),
            4,
            DayOfWeek.THURSDAY,
            Optional.of(LocalDate.of(2023, 5, 4)),
            new ArrayList<>()));
    Week week = new Week("week", days);
    Week weekTwo = new Week("week", daysTwo);
    Schedule schedule = new Schedule("schedule", List.of(week, weekTwo), week);
    Assertions.assertEquals(
        schedule.flatten().days(),
        List.of(
            this.testDay,
            new Day(
                "day",
                new ArrayList<>(),
                7,
                DayOfWeek.MONDAY,
                Optional.of(LocalDate.of(2023, 5, 8)),
                new ArrayList<>()),
            new Day(
                "day",
                new ArrayList<>(),
                4,
                DayOfWeek.THURSDAY,
                Optional.of(LocalDate.of(2023, 5, 4)),
                new ArrayList<>())));
    Assertions.assertEquals(schedule.getLength(), 11);
  }

  @Test
  public void testScheduleFlattenWithoutDays() throws InvalidScheduleException {
    Week week = new Week("week", new ArrayList<>());
    Schedule schedule = new Schedule("'schedule", List.of(week), week);
    Assertions.assertEquals(schedule.flatten().days(), List.of());
    Assertions.assertEquals(schedule.getLength(), 0);
  }

  @Test
  public void testScheduleFlattenWithoutDaysNull() throws InvalidScheduleException {
    Week week = new Week("week", new ArrayList<>());
    Schedule schedule = new Schedule("'schedule", null, week);
    Assertions.assertEquals(schedule.flatten().days(), List.of());
    Assertions.assertEquals(schedule.getLength(), 0);
  }
}
