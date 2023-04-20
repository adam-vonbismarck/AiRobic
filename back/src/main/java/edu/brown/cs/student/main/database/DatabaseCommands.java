package edu.brown.cs.student.main.database;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class DatabaseCommands {


  private String PUT = "curl -X PUT -d '";
  private String UPDATE = "curl -X PATCH -d '";
  private String DELETE = "curl -X DELETE ";
  private String GET = "curl ";
  private String DATABASE = "'https://cs32airobic-default-rtdb.firebaseio.com/";
  private String END = ".json'";


  public void put (String data, String where) throws IOException, InterruptedException {
    String s = PUT + data + "' " + DATABASE + where + END;
    System.out.println(s);
    new TerminalCommand(PUT + data + "' " + DATABASE + where + END);
  }

  public void update (String data, String where) throws IOException, InterruptedException {
    String s = UPDATE + data + "' " + DATABASE + where + END;
    System.out.println(s);
    new TerminalCommand(UPDATE + data + "' " + DATABASE + where + END);
  }

  public void delete (String where) throws IOException, InterruptedException {
    String s = DELETE + DATABASE + where + END;
    System.out.println(s);
    new TerminalCommand(DELETE + DATABASE + where + END);
  }

  public String get (String where) throws IOException, InterruptedException {
    String s = GET + DATABASE + where + END;
    System.out.println(s);
    new TerminalCommand(GET + DATABASE + where + END);
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(System.in));
    return reader.readLine();
  }
}

