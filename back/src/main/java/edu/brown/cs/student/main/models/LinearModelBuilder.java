package edu.brown.cs.student.main.models;

import edu.brown.cs.student.main.models.exceptions.InvalidDistributionException;
import edu.brown.cs.student.main.models.exceptions.InvalidScheduleException;
import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.Day.WorkoutDescription;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.markov.MarkovModel;

/**
 * The LinearModelBuilder class contains a build method that takes in a schedule and a start day,
 * and builds a MarkovModel where each hidden state represents a workout time within the week. Each
 * MarkovModel the LinearModelBuilder builds has enough hidden states to represent a full week
 * of training (as a loop).
 */
public class LinearModelBuilder {

  private WorkoutDistributionByName dists;

  /**
   * The constructor for the LinearModelBuilder class, which takes in a WorkoutDistributionByName
   * instance that will get an emission distribution given a key name (that represents what category
   * of workout the hidden state should emit).
   *
   * @param dists - the aforementioned WorkoutDistributionByName instance.
   */
  public LinearModelBuilder(WorkoutDistributionByName dists) {
    this.dists = dists;
  }

  /**
   * The build method takes in a schedule and a start day, and builds a "linear" MarkovModel to model
   * one week of training.
   *
   * @param schedule - the framework for the schedule that the LinearModelBuilder should build.
   * @param startDay - the point within the week the model should start from.
   * @return a completely built MarkovModel, ready for random generation.
   * @throws InvalidDistributionException if any distributions in the generation of the model do not
   * sum to 1.
   * @throws InvalidScheduleException if the schedule to be built does not have enough information
   * to build the model.
   */
  public MarkovModel build(Schedule schedule, String startDay)
      throws InvalidDistributionException, InvalidScheduleException {
    ModelBuilder builder = new ModelBuilder();

    // build up hidden states, with emission distributions based on provided key in the schedule
    // framework
    for (Day day : schedule.example().days()) {
      if (!day.verifyDay()) {
        throw new InvalidScheduleException("Day workout numbers and intensity labels do not match: " + day,
            schedule);
      }
      int workoutCounter = 0;
      for (WorkoutDescription intensity : day.getIntensityCopy()) {
        builder.generateNewState(this.encodeDay(day.getName(), workoutCounter));
        builder.setEmissionDistribution(this.encodeDay(day.getName(), workoutCounter),
            this.dists.generateEmissionDistribution(intensity));
      }
    }

    // add linear state transitions to each hidden state
    builder.addLinearTransitions();

    // make sure start day is in keyset of days to ints?

    // use the information provided by startDay to generate a start distribution - make sure to error check
    builder.addStartProbability(this.encodeDay(startDay, 0), 1.0);

    return builder.build();
  }

  /**
   * This method encodes a day and a workout number of that day into a name for a given HiddenState.
   *
   * @param day - the day of the state to be encoded.
   * @param counter - the workout number on that day to be encoded.
   * @return - the encoded HiddenState name.
   */
  public String encodeDay(String day, int counter) {
    return "Day: " + day + ", workout: " + counter;
  }

}
