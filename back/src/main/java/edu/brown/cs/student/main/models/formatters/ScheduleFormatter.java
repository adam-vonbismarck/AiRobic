package edu.brown.cs.student.main.models.formatters;

import edu.brown.cs.student.main.models.exceptions.FormatterFailureException;
import edu.brown.cs.student.main.models.exceptions.InvalidScheduleException;
import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.formattypes.Week;
import edu.brown.cs.student.main.models.markov.Emission;
import java.util.List;

/**
 * An implementation of the EmissionFormatter interface. This particular implementation loads a
 * sequence of emissions into a Schedule object, ensuring that the Schedule object and list of
 * Emissions match in "length".
 */
public class ScheduleFormatter implements EmissionFormatter<Schedule> {

  private final Schedule schedule;
  private int workoutCount;

  /**
   * The constructor for the ScheduleFormatter class, which takes in a schedule to fill and verifies
   * that it has enough information to be filled appropriately.
   *
   * @param toFill - the Schedule to fill.
   * @throws InvalidScheduleException if the schedule does not have all relevant information for
   *     loading.
   */
  public ScheduleFormatter(Schedule toFill) throws InvalidScheduleException {
    this.schedule = toFill;
    this.workoutCount = this.schedule.getLength();
  }

  /**
   * The particular implementation of formatEmissions for this class. In particular, it sequentially
   * loads each Emission into the first available Schedule slot.
   *
   * @param emissions - the results of the MarkovModel.
   * @return the filled in Schedule.
   * @throws FormatterFailureException if the Schedule and list of Emissions do not match in length.
   */
  @Override
  public Schedule formatEmissions(List<Emission> emissions) throws FormatterFailureException {
    if (emissions.size() != this.workoutCount) {
      throw new FormatterFailureException(
          "Schedule and model emissions did not match in length, so the schedule"
              + " could not be filled in appropriately.",
          emissions);
    }

    for (Week week : this.schedule.weeks()) {
      for (Day day : week.days()) {
        for (int i = 0; i < day.getNumberOfWorkouts(); i++) {
          day.addWorkout(emissions.remove(0));
        }
      }
    }
    return this.schedule;
  }
}
