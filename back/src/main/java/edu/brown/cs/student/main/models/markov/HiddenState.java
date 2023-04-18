package edu.brown.cs.student.main.models.markov;

public interface HiddenState {
  Emission emit();
  HiddenState transition();

}
