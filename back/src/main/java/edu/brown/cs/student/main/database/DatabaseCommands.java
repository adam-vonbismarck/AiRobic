package edu.brown.cs.student.main.database;
import java.io.IOException;


public class DatabaseCommands {


  private String PUT = "curl -X PUT -d '";
  private String UPDATE = "curl -X PATCH -d '";
  private String DELETE = "curl -X DELETE";
  private String DATABASE = "' 'https://cs32airobic-default-rtdb.firebaseio.com/";
  private String END = ".json'";


  public void put (String data, String where) throws IOException, InterruptedException {
    String s = PUT + data + DATABASE + where + END;
    System.out.println(s);
    new TerminalCommand(PUT + data + DATABASE + where + END);
  }


  public void update (String data, String where) throws IOException, InterruptedException {
    String s = PUT + data + DATABASE + where + END;
    System.out.println(s);
    new TerminalCommand(PUT + data + DATABASE + where + END);
  }
}

