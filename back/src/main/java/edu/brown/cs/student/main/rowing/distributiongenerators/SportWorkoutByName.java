package edu.brown.cs.student.main.rowing.distributiongenerators;

import edu.brown.cs.student.main.models.exceptions.NoWorkoutTypeException;
import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.markov.model.Emission;

import java.util.HashMap;

/**
 * This interface allows for multiple different sport distribution generators to be used in different models,
 * such as in the linear model. While we only implemented rowing, this interface would be critical for further
 * expansion.
 */
public interface SportWorkoutByName {

    /**
     * The method that gets a pre-loaded emission distribution given a WorkoutDescription - we could add some more
     * fields to WorkoutDescription to make this even more generic.
     *
     * @param name - the description of the emission distribution requested.
     * @return the resulting emission distribution.
     * @throws NoWorkoutTypeException if the workout description contains a workout that the SportWorkoutByName
     * instance does not have access to (from another sport, etc.).
     */
    HashMap<Emission, Double> getEmissionDist(Day.WorkoutDescription name) throws NoWorkoutTypeException;

}
