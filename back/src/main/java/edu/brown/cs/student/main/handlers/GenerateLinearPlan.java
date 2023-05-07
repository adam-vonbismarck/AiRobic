package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.models.exceptions.FormatterFailureException;
import edu.brown.cs.student.main.models.exceptions.InvalidDistributionException;
import edu.brown.cs.student.main.models.exceptions.InvalidScheduleException;
import edu.brown.cs.student.main.models.exceptions.NoWorkoutTypeException;
import edu.brown.cs.student.main.models.formatters.ScheduleFormatter;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.markov.model.MarkovModel;
import edu.brown.cs.student.main.models.markov.modelbuilding.Workout;
import edu.brown.cs.student.main.rowing.modelbuilders.LinearModelBuilder;
import edu.brown.cs.student.main.rowing.modelbuilders.ScheduleBuilder;
import java.io.IOException;
import java.time.LocalDate;

/**
 * The GenerateLinearPlan class generates a linear plan given the type of workouts the user wants to
 * do, the start and end date of their plan, the amount of time they have, and the amount of high
 * intensity work they want to do.
 */
public class GenerateLinearPlan {

  /**
   * The generate method takes in the constraints with which the schedule should be built, and
   * builds the schedule using the ScheduleBuilder and MarkovModel classes.
   *
   * @param minutes - the amount of time the caller has.
   * @param startDate - the start date of the caller's workout program.
   * @param endDate - the end date of the caller's workout program.
   * @param highIntensityLabel - the type of high intensity workouts the caller wants to do.
   * @param lowIntensityLabel - the type of low intensity workouts the caller wants to do.
   * @param highIntensityPercent - the percentage of high intensity work the caller wants.
   * @return the built Schedule
   * @throws IOException if the workout files can't be read.
   * @throws InvalidDistributionException if there are invalid distributions involved when building
   *     the model.
   * @throws InvalidScheduleException if there are problems building a schedule given the
   *     constraints.
   * @throws NoWorkoutTypeException if the high or low intensity labels are not found.
   * @throws FormatterFailureException if there is an issue reading MarkovModel results into a
   *     schedule.
   */
  public Schedule generate(
      int minutes,
      LocalDate startDate,
      LocalDate endDate,
      Workout highIntensityLabel,
      Workout lowIntensityLabel,
      double highIntensityPercent)
      throws IOException, InvalidDistributionException, InvalidScheduleException,
          NoWorkoutTypeException, FormatterFailureException {

    ScheduleBuilder builder = new ScheduleBuilder();
    Schedule toBuild =
        builder.minutesWithDates(
            minutes,
            startDate,
            endDate,
            highIntensityPercent,
            highIntensityLabel,
            lowIntensityLabel);
    MarkovModel model = new LinearModelBuilder().build(toBuild, startDate.getDayOfWeek());
    model.generateFormattedEmissions(toBuild.getLength(), new ScheduleFormatter(toBuild));
    return toBuild;
  }
}
