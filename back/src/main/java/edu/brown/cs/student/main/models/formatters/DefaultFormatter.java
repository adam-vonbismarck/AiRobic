package edu.brown.cs.student.main.models.formatters;

import edu.brown.cs.student.main.models.markov.Emission;
import java.util.ArrayList;
import java.util.List;

/**
 * The default formatter, which just returns the raw results of a MarkovModel sequence generation.
 */
public class DefaultFormatter implements EmissionFormatter<List<Emission>> {


  /**
   * The format method, which in this case just returns the copied raw results of the Markov sequence.
   *
   * @param emissions - the emissions to be formatted.
   * @return the copied results of formatting.
   */
  @Override
  public List<Emission> formatEmissions(List<Emission> emissions) {
    ArrayList<Emission> newList = new ArrayList<>();
    for (Emission emission : emissions) {
      newList.add(emission.copy());
    }
    return newList;
  }

}
