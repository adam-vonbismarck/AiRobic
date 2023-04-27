package edu.brown.cs.student.main.models;

import edu.brown.cs.student.main.models.exceptions.InvalidDistributionException;
import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.markov.MarkovModel;

public class LinearModelBuilder {

  public MarkovModel build(Schedule schedule, String startDay) throws InvalidDistributionException {
    ModelBuilder builder = new ModelBuilder();

    for (Day day : schedule.example().days()) {
      int workoutCounter = 0;
      for (String intensity : day.getIntensity()) {
        builder.generateNewState(this.encodeDay(day.getName(), workoutCounter));
        builder.setEmissionDistribution(this.encodeDay(day.getName(), workoutCounter),
            WorkoutDistributionByName.generateEmissionDistribution(intensity));
      }
    }

    builder.addLinearTransitions();

    builder.addStartProbability(startDay, 1.0);

    return builder.build();
  }

  public String encodeDay(String day, int counter) {
    return "Day: " + day + ", workout: " + counter;
  }

}