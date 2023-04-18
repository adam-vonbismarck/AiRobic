package edu.brown.cs.student.main.database;

public interface DatabaseQuerier<T> {

  <T> T queryDatabase();

}
