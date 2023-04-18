package edu.brown.cs.student.main.database;

public interface DatabaseModifier<T> {

  <T> void modify(T toPut);

}
