package edu.brown.cs.student.main.models.formatters;

import edu.brown.cs.student.main.models.exceptions.FormatterFailureException;
import edu.brown.cs.student.main.models.markov.model.Emission;
import java.util.List;

/**
 * An interface for classes that implement a strategy for converting Markov emission results into
 * some specific format.
 *
 * @param <T> The type that the formatting strategy returns.
 */
public interface EmissionFormatter<T> {

  /**
   * The strategy for formatting the results of a Markov sequence generation.
   *
   * @param emissions - the results of the MarkovModel.
   * @return the formatted results, of a generic type specified before use of the strategy.
   * @throws FormatterFailureException if the formatter is unable to format the emission list given.
   */
  T formatEmissions(List<Emission> emissions) throws FormatterFailureException;
}
