package edu.brown.cs.student.main.database;

public class Database {

  public Database() {

  }

  public <T> T queryDatabase(Class<T> type, DatabaseQuerier<T> querier) {
    return querier.queryDatabase();
  }

  public <T> void modifyDatabase(Class<T> type, DatabaseModifier<T> modifier, T toPut) {
    modifier.modify(toPut);
  }

}
