package edu.brown.cs.student.main.models.formatters;

import edu.brown.cs.student.main.models.markov.Emission;
import java.util.ArrayList;
import java.util.List;

public class DefaultFormatter implements EmissionFormatter<List<Emission>> {


  @Override
  public List<Emission> formatEmissions(List<Emission> emissions) {
    ArrayList<Emission> newList = new ArrayList<>();
    for (Emission emission : emissions) {
      newList.add(emission.copy());
    }
    return newList;
  }
}
