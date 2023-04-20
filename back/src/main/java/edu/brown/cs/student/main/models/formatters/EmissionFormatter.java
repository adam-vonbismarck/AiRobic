package edu.brown.cs.student.main.models.formatters;

import edu.brown.cs.student.main.models.exceptions.FormatterFailureException;
import edu.brown.cs.student.main.models.markov.Emission;
import java.util.List;

public interface EmissionFormatter<T> {

  T formatEmissions(List<Emission> emissions) throws FormatterFailureException;

}
