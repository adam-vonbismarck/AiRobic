package edu.brown.cs.student;

import edu.brown.cs.student.main.handlers.GenerateGraphLikePlan;
import edu.brown.cs.student.main.models.exceptions.*;
import edu.brown.cs.student.main.models.formatters.DefaultFormatter;
import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.formattypes.Week;
import edu.brown.cs.student.main.models.markov.model.Emission;
import edu.brown.cs.student.main.models.markov.model.MarkovModel;
import edu.brown.cs.student.main.models.markov.modelbuilding.Workout;
import edu.brown.cs.student.main.rowing.distributiongenerators.RowingWorkoutByName;
import edu.brown.cs.student.main.rowing.modelbuilders.VariableModelBuilder;
import edu.brown.cs.student.main.server.RandomGenerator;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * VariableModelTests tests variable model generation, and fuzz tests our final use of the
 * associated ModelBuilder in the GenerateGraphLikePlan class.
 */
public class VariableModelTests {

  /**
   * A method for ensuring that schedules are shaped the way they should be.
   *
   * @param schedule - the schedule to be validated
   * @param minutes - the number of minutes per week the schedule was built on
   * @return whether or not the schedule was valid (boolean)
   */
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

  public static int NUM_TRIALS = 1000;

  @Test
  public void testVariableModelMax()
      throws InvalidDistributionException, InvalidScheduleException, IOException,
          NoWorkoutTypeException {
    MarkovModel model =
        new VariableModelBuilder(new RowingWorkoutByName())
            .build(
                Set.of(Workout.UT_2), Set.of(Workout._30R_20, Workout._6K, Workout._2K), 1200, 0.2);
    Assertions.assertEquals(model.getNumberOfStates(), 4);
  }

  @Test
  public void testVariableModelMin()
      throws InvalidDistributionException, InvalidScheduleException, IOException,
          NoWorkoutTypeException {
    MarkovModel model =
        new VariableModelBuilder(new RowingWorkoutByName())
            .build(
                Set.of(Workout.UT_2), Set.of(Workout._30R_20, Workout._6K, Workout._2K), 120, 0.2);
    Assertions.assertEquals(model.getNumberOfStates(), 4);
  }

  @Test
  public void testVariableModelOutOfBounds()
      throws InvalidDistributionException, InvalidScheduleException, IOException,
          NoWorkoutTypeException {
    Exception exn =
        Assertions.assertThrows(
            InvalidScheduleException.class,
            () -> {
              MarkovModel model =
                  new VariableModelBuilder(new RowingWorkoutByName())
                      .build(
                          Set.of(Workout.UT_2),
                          Set.of(Workout._30R_20, Workout._6K, Workout._2K),
                          1201,
                          0.2);
            });
    Assertions.assertEquals(
        exn.getMessage(), "All schedules must have a number of minutes between 120 and 1200.");
    exn =
        Assertions.assertThrows(
            InvalidScheduleException.class,
            () -> {
              MarkovModel model =
                  new VariableModelBuilder(new RowingWorkoutByName())
                      .build(
                          Set.of(Workout.UT_2),
                          Set.of(Workout._30R_20, Workout._6K, Workout._2K),
                          -1,
                          0.2);
            });
    Assertions.assertEquals(
        exn.getMessage(), "All schedules must have a number of minutes between 120 and 1200.");
    exn =
        Assertions.assertThrows(
            InvalidDistributionException.class,
            () -> {
              MarkovModel model =
                  new VariableModelBuilder(new RowingWorkoutByName())
                      .build(
                          Set.of(Workout.UT_2),
                          Set.of(Workout._30R_20, Workout._6K, Workout._2K),
                          125,
                          1.1);
            });
    Assertions.assertEquals(
        exn.getMessage(),
        "The probability associated with the output Low workoutType was negative.");
  }

  @Test
  public void testVariableMax()
      throws IOException, InvalidScheduleException, InvalidDistributionException,
          FormatterFailureException, NoWorkoutTypeException {
    MarkovModel model =
        new VariableModelBuilder(new RowingWorkoutByName())
            .build(
                Set.of(Workout.UT_2), Set.of(Workout._30R_20, Workout._6K, Workout._2K), 1200, 0.2);
    List<Emission> emissions = model.generateFormattedEmissions(20, new DefaultFormatter());
  }

  @Test
  public void testVariableMin()
      throws IOException, InvalidScheduleException, InvalidDistributionException,
          FormatterFailureException, NoWorkoutTypeException {
    MarkovModel model =
        new VariableModelBuilder(new RowingWorkoutByName())
            .build(
                Set.of(Workout.UT_2), Set.of(Workout._30R_20, Workout._6K, Workout._2K), 120, 0.2);
    List<Emission> emissions = model.generateFormattedEmissions(20, new DefaultFormatter());
  }

  @Test
  public void testVariableShortPlan()
      throws IOException, InvalidScheduleException, InvalidDistributionException,
          FormatterFailureException, NoWorkoutTypeException, InvalidDatesException {
    Schedule schedule =
        new GenerateGraphLikePlan()
            .generate(
                420,
                LocalDate.of(2023, 5, 5),
                LocalDate.of(2023, 5, 6),
                Set.of(Workout._2K, Workout._6K),
                Set.of(Workout.UT_2),
                0.2);
    Assertions.assertTrue(this.validateSchedule(schedule, 420));
  }

  @Test
  public void testVariableBuiltMin()
      throws IOException, InvalidScheduleException, InvalidDistributionException,
          FormatterFailureException, NoWorkoutTypeException, InvalidDatesException {
    Schedule schedule =
        new GenerateGraphLikePlan()
            .generate(
                120,
                LocalDate.of(2023, 5, 5),
                LocalDate.of(2023, 5, 6),
                Set.of(Workout._2K, Workout._6K),
                Set.of(Workout.UT_2),
                0.2);
    Assertions.assertTrue(this.validateSchedule(schedule, 120));
  }

  @Test
  public void testVariableBuiltMax()
      throws IOException, InvalidScheduleException, InvalidDistributionException,
          FormatterFailureException, NoWorkoutTypeException, InvalidDatesException {
    Schedule schedule =
        new GenerateGraphLikePlan()
            .generate(
                1200,
                LocalDate.of(2023, 5, 5),
                LocalDate.of(2023, 5, 6),
                Set.of(Workout._2K, Workout._6K),
                Set.of(Workout.UT_2),
                0.2);
    Assertions.assertTrue(this.validateSchedule(schedule, 1200));
  }

  @Test
  public void testVariableLongPlan()
      throws IOException, InvalidScheduleException, InvalidDistributionException,
          FormatterFailureException, NoWorkoutTypeException, InvalidDatesException {
    Schedule schedule =
        new GenerateGraphLikePlan()
            .generate(
                420,
                LocalDate.of(2023, 5, 5),
                LocalDate.of(2024, 5, 6),
                Set.of(Workout._2K, Workout._6K),
                Set.of(Workout.UT_2),
                0.2);
    Assertions.assertTrue(this.validateSchedule(schedule, 420));
  }

  @Test
  public void testVariableVaryingStartEndDates()
      throws IOException, InvalidScheduleException, InvalidDistributionException,
          FormatterFailureException, NoWorkoutTypeException, InvalidDatesException {
    for (int i = 1; i < 8; i++) {
      for (int j = 10; j < 17; j++) {
        Schedule schedule =
            new GenerateGraphLikePlan()
                .generate(
                    600,
                    LocalDate.of(2023, 5, i),
                    LocalDate.of(2023, 5, j),
                    Set.of(Workout._2K, Workout._6K),
                    Set.of(Workout.UT_2),
                    0.2);
        Assertions.assertTrue(this.validateSchedule(schedule, 600));
      }
    }
  }

  @Test
  public void testVariableMedium()
      throws IOException, InvalidScheduleException, InvalidDistributionException,
          FormatterFailureException, NoWorkoutTypeException, InvalidDatesException {
    Schedule schedule =
        new GenerateGraphLikePlan()
            .generate(
                420,
                LocalDate.of(2023, 5, 5),
                LocalDate.of(2023, 5, 9),
                Set.of(Workout._2K, Workout._6K),
                Set.of(Workout.UT_2),
                0.2);
    Assertions.assertTrue(this.validateSchedule(schedule, 420));
  }

  @Test
  public void fuzzVariableGenerator()
      throws InvalidDatesException, InvalidDistributionException, InvalidScheduleException,
          NoWorkoutTypeException, IOException, FormatterFailureException {
    for (int i = 0; i < NUM_TRIALS; i++) {
      int mins = RandomGenerator.getRandomInt(120, 1200);
      int startMonth = RandomGenerator.getRandomInt(1, 12);
      int startDay = RandomGenerator.getRandomInt(1, 28);
      int startYear = RandomGenerator.getRandomInt(2015, 2023);
      int endMonth = RandomGenerator.getRandomInt(1, 12);
      int endDay = RandomGenerator.getRandomInt(1, 28);
      int endYear = startYear + RandomGenerator.getRandomInt(1, 3);
      double highPercent = RandomGenerator.getRandomPositiveDouble(0.0, 1.0);
      Schedule schedule =
          new GenerateGraphLikePlan()
              .generate(
                  mins,
                  LocalDate.of(startYear, startMonth, startDay),
                  LocalDate.of(endYear, endMonth, endDay),
                  Set.of(Workout._2K, Workout._6K, Workout._30R_20),
                  Set.of(Workout.UT_2),
                  highPercent);
      Assertions.assertEquals(
          schedule.weeks().get(0).days().get(0).getDate().get(),
          LocalDate.of(startYear, startMonth, startDay));
      Assertions.assertEquals(
          schedule
              .weeks()
              .get(schedule.weeks().size() - 1)
              .days()
              .get(schedule.weeks().get(schedule.weeks().size() - 1).days().size() - 1)
              .getDate()
              .get(),
          LocalDate.of(endYear, endMonth, endDay));
    }
  }
}
