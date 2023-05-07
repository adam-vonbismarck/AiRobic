package edu.brown.cs.student;

import edu.brown.cs.student.main.models.exceptions.FormatterFailureException;
import edu.brown.cs.student.main.models.exceptions.InvalidDistributionException;
import edu.brown.cs.student.main.models.exceptions.InvalidScheduleException;
import edu.brown.cs.student.main.models.exceptions.NoWorkoutTypeException;
import edu.brown.cs.student.main.models.formatters.ScheduleFormatter;
import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.formattypes.Week;
import edu.brown.cs.student.main.models.markov.model.Emission;
import edu.brown.cs.student.main.models.markov.model.MarkovModel;
import edu.brown.cs.student.main.models.markov.modelbuilding.Workout;
import edu.brown.cs.student.main.rowing.modelbuilders.LinearModelBuilder;
import edu.brown.cs.student.main.rowing.modelbuilders.ScheduleBuilder;
import java.io.IOException;
import java.time.LocalDate;

import edu.brown.cs.student.main.server.serializing.Serializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LinearModelTests {

  private boolean validateSchedule(Schedule schedule, int minutes) {
    for (Week week : schedule.weeks()) {
      int minuteCount = 0;
      for (Day day : week.days()) {
        if (day.getEmissions().size() != day.getNumberOfWorkouts() && day.verifyDay()) {
          return false;
        }
        for (Emission emission : day.getEmissions()) {
          minuteCount += emission.getTime();
        }
      }
      if (week.days().size() == 7) {
        if (minuteCount > minutes + 60 || minuteCount < minutes - 60) {
          return false;
        }
      }
    }
    return true;
  }

  @Test
  public void testLinearMax()
      throws IOException, InvalidScheduleException, InvalidDistributionException,
          FormatterFailureException, NoWorkoutTypeException {
    ScheduleBuilder builder = new ScheduleBuilder();
    Schedule toBuild = null;
    toBuild =
        builder.minutesWithDates(
            1200,
            LocalDate.of(2023, 5, 3),
            LocalDate.of(2023, 5, 25),
            0.2,
            Workout.of("2k"),
            Workout.of("UT2"));
    MarkovModel model =
        new LinearModelBuilder().build(toBuild, LocalDate.of(2023, 5, 3).getDayOfWeek());
    Schedule schedule =
        model.generateFormattedEmissions(toBuild.getLength(), new ScheduleFormatter(toBuild));
    Assertions.assertTrue(validateSchedule(schedule, 1200));
  }

  @Test
  public void testLinearShortPlan()
      throws IOException, InvalidScheduleException, InvalidDistributionException,
          FormatterFailureException, NoWorkoutTypeException {
    ScheduleBuilder builder = new ScheduleBuilder();
    Schedule toBuild = null;
    toBuild =
        builder.minutesWithDates(
            600,
            LocalDate.of(2023, 5, 3),
            LocalDate.of(2023, 5, 4),
            0.2,
            Workout.of("2k"),
            Workout.of("UT2"));
    MarkovModel model =
        new LinearModelBuilder().build(toBuild, LocalDate.of(2023, 5, 3).getDayOfWeek());
    Schedule schedule =
        model.generateFormattedEmissions(toBuild.getLength(), new ScheduleFormatter(toBuild));
    Assertions.assertTrue(validateSchedule(schedule, 600));
  }

  @Test
  public void testLinearEndsOnSunday()
      throws IOException, InvalidScheduleException, InvalidDistributionException,
          FormatterFailureException, NoWorkoutTypeException {
    ScheduleBuilder builder = new ScheduleBuilder();
    Schedule toBuild = null;
    toBuild =
        builder.minutesWithDates(
            600,
            LocalDate.of(2023, 5, 3),
            LocalDate.of(2023, 5, 13),
            0.2,
            Workout.of("2k"),
            Workout.of("UT2"));
    MarkovModel model =
        new LinearModelBuilder().build(toBuild, LocalDate.of(2023, 5, 3).getDayOfWeek());
    Schedule schedule =
        model.generateFormattedEmissions(toBuild.getLength(), new ScheduleFormatter(toBuild));
    Assertions.assertTrue(validateSchedule(schedule, 120));
  }

  @Test
  public void testLinearEndsHigherIntensity()
      throws IOException, InvalidScheduleException, InvalidDistributionException,
          FormatterFailureException, NoWorkoutTypeException {
    ScheduleBuilder builder = new ScheduleBuilder();
    Schedule toBuild = null;
    toBuild =
        builder.minutesWithDates(
            600,
            LocalDate.of(2023, 5, 3),
            LocalDate.of(2023, 5, 13),
            0.5,
            Workout.of("2k"),
            Workout.of("UT2"));
    MarkovModel model =
        new LinearModelBuilder().build(toBuild, LocalDate.of(2023, 5, 3).getDayOfWeek());
    Schedule schedule =
        model.generateFormattedEmissions(toBuild.getLength(), new ScheduleFormatter(toBuild));
    Assertions.assertTrue(validateSchedule(schedule, 120));
  }

  @Test
  public void testLinearAgain() throws InvalidDistributionException, InvalidScheduleException, IOException, FormatterFailureException, NoWorkoutTypeException {
    ScheduleBuilder builder = new ScheduleBuilder();
    Schedule toBuild = null;
    toBuild =
            builder.minutesWithDates(
                    360,
                    LocalDate.of(2023, 5, 14),
                    LocalDate.of(2023, 6, 14),
                    0.2,
                    Workout.of("2k"),
                    Workout.of("UT2"));
    MarkovModel model =
            new LinearModelBuilder().build(toBuild, LocalDate.of(2023, 5, 14).getDayOfWeek());
    Schedule schedule =
            model.generateFormattedEmissions(toBuild.getLength(), new ScheduleFormatter(toBuild));
    System.out.println(schedule.flatten());
  }
}
