package edu.brown.cs.student.main.models;

import edu.brown.cs.student.main.models.markov.Emission;
import java.util.List;

public interface EmissionFormatter<T> {

  T formatEmissions(List<Emission> emissions);

}
