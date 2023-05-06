package edu.brown.cs.student.main.database;

import java.io.IOException;

public class DatabaseCommands {

  private final String DATABASE = "'https://cs32airobic-default-rtdb.firebaseio.com/";
  private final String END = ".json'";

  // https://console.firebase.google.com/u/1/project/cs32airobic/database/cs32airobic-default-rtdb/data/~2F

  public void put(String data, String where) throws IOException, InterruptedException {
    String PUT = "curl -X PUT -d '";
    String s = PUT + data + "' " + this.DATABASE + where + this.END;
    System.out.println(s);
    new TerminalCommand(PUT + data + "' " + this.DATABASE + where + this.END).run();
  }

  public void update(String data, String where) throws IOException, InterruptedException {
    String UPDATE = "curl -X PATCH -d '";
    String s = UPDATE + data + "' " + this.DATABASE + where + this.END;
    System.out.println(s);
    new TerminalCommand(UPDATE + data + "' " + this.DATABASE + where + this.END).run();
  }

  public void delete(String where) throws IOException, InterruptedException {
    String DELETE = "curl -X DELETE ";
    String s = DELETE + this.DATABASE + where + this.END;
    System.out.println(s);
    new TerminalCommand(DELETE + this.DATABASE + where + this.END).run();
  }

  public String get(String where) throws IOException, InterruptedException {
    String GET = "curl ";
    String s = GET + this.DATABASE + where + this.END;
    System.out.println(s);
    return new TerminalCommand(GET + this.DATABASE + where + this.END).get();
  }
}
