package edu.brown.cs.student.main.models.markov;

import java.util.List;

public interface HiddenState {
  Emission emit();
  HiddenState transition();

  List<HiddenState> potentialStates();

}
