package edu.brown.cs.student.main.models;

import edu.brown.cs.student.main.models.exceptions.InvalidDistributionException;
import edu.brown.cs.student.main.models.exceptions.InvalidScheduleException;
import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.markov.MarkovModel;

public class LinearModelBuilder {

  public MarkovModel build(Schedule schedule, String startDay)
      throws InvalidDistributionException, InvalidScheduleException {
    ModelBuilder builder = new ModelBuilder();

    for (Day day : schedule.example().days()) {
      if (!day.verifyDay()) {
        throw new InvalidScheduleException("Day workout numbers and intensity labels do not match: " + day,
            schedule);
      }
      int workoutCounter = 0;
      for (String intensity : day.getIntensityCopy()) {
        builder.generateNewState(this.encodeDay(day.getName(), workoutCounter));
        builder.setEmissionDistribution(this.encodeDay(day.getName(), workoutCounter),
            WorkoutDistributionByName.generateEmissionDistribution(intensity));
      }
    }

    builder.addLinearTransitions();

    // make sure start day is in keyset of days to ints?

    builder.addStartProbability(this.encodeDay(startDay, 0), 1.0);

    return builder.build();
  }

  public String encodeDay(String day, int counter) {
    return "Day: " + day + ", workout: " + counter;
  }

}
