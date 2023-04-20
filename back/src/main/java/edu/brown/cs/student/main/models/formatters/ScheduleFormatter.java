package edu.brown.cs.student.main.models.formatters;

import edu.brown.cs.student.main.models.exceptions.FormatterFailureException;
import edu.brown.cs.student.main.models.exceptions.InvalidScheduleException;
import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.formattypes.Week;
import edu.brown.cs.student.main.models.markov.Emission;
import java.util.List;

public class ScheduleFormatter implements EmissionFormatter<Schedule> {

  private final Schedule schedule;
  private int workoutCount;

  public ScheduleFormatter (Schedule toFill) throws InvalidScheduleException {
    this.schedule = toFill;
    this.workoutCount = 0;
    for (Week week : this.schedule.weeks()) {
      for (Day day : week.days()) {
        if (day.numberOfWorkouts() == null) {
          throw new InvalidScheduleException("All days must have a number of workouts pre-listed, "
              + "but " + day + " does not.",
              this.schedule);
        }
        this.workoutCount += day.numberOfWorkouts();
      }
    }
  }

  @Override
  public Schedule formatEmissions(List<Emission> emissions) throws FormatterFailureException {
    if (emissions.size() != this.workoutCount) {
      throw new FormatterFailureException("Schedule and model emissions did not match in length, so the schedule"
          + "could not be filled in appropriately.",
          emissions);
    }
    for (Week week : this.schedule.weeks()) {
      for (Day day : week.days()) {
        day.workouts().add(emissions.remove(0));
      }
    }
    return this.schedule;
  }

}
