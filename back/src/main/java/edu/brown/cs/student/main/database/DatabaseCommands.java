package edu.brown.cs.student.main.database;

import java.io.IOException;

/** This class generates database commands which are being run in terminal */
public class DatabaseCommands {
  private final String DATABASE = "'https://cs32airobic-default-rtdb.firebaseio.com/";
  private final String DATABASEWIN = "https://cs32airobic-default-rtdb.firebaseio.com/";
  private final String END = ".json'";
  private final String ENDWIN = ".json";

  // https://console.firebase.google.com/u/1/project/cs32airobic/database/cs32airobic-default-rtdb/data/~2F

  // This method puts in information in the database, but deletes everything else in the current
  // branch
  public void put(String data, String where) throws IOException, InterruptedException {
    if (System.getProperty("os.name").toLowerCase().contains("win")) {
      String PUT = "curl -X PUT -d '";
      String s = PUT + data + "' " + this.DATABASEWIN + where + this.ENDWIN;
      System.out.println(s);
      new TerminalCommand(PUT + data + "' " + this.DATABASEWIN + where + this.ENDWIN).run();
    } else {
      String PUT = "curl -X PUT -d '";
      String s = PUT + data + "' " + this.DATABASE + where + this.END;
      System.out.println(s);
      new TerminalCommand(PUT + data + "' " + this.DATABASE + where + this.END).run();
    }
  }

  // This method updates or adds information in the database, without deleting the current
  // information
  public void update(String data, String where) throws IOException, InterruptedException {
    if (System.getProperty("os.name").toLowerCase().contains("win")) {
      String UPDATE = "curl -X PATCH -d \"";
      String newData = data.replaceAll("\"", "\\\\" + "\"");
      String s = UPDATE + newData + "\" " + this.DATABASEWIN + where + this.ENDWIN;
      System.out.println(s);
      new TerminalCommand(s).run();
    } else {
      String UPDATE = "curl -X PATCH -d '";
      String s = UPDATE + data + "' " + this.DATABASE + where + this.END;
      System.out.println(s);
      new TerminalCommand(UPDATE + data + "' " + this.DATABASE + where + this.END).run();
    }
  }

  // This method deletes the specific branch in the database
  public void delete(String where) throws IOException, InterruptedException {
    if (System.getProperty("os.name").toLowerCase().contains("win")) {
      String DELETE = "curl -X DELETE ";
      String s = DELETE + this.DATABASEWIN + where + this.ENDWIN;
      System.out.println(s);
      new TerminalCommand(DELETE + this.DATABASEWIN + where + this.ENDWIN).run();
    } else {
      String DELETE = "curl -X DELETE ";
      String s = DELETE + this.DATABASE + where + this.END;
      System.out.println(s);
      new TerminalCommand(DELETE + this.DATABASE + where + this.END).run();
    }
  }

  // This method returns the information from a specific branch in the database
  public String get(String where) throws IOException, InterruptedException {
    if (System.getProperty("os.name").toLowerCase().contains("win")) {
      String GET = "curl ";
      String s = GET + this.DATABASEWIN + where + this.ENDWIN;
      System.out.println(s);
      return new TerminalCommand(s).get();
    } else {
      String GET = "curl ";
      String s = GET + this.DATABASE + where + this.END;
      System.out.println(s);
      return new TerminalCommand(GET + this.DATABASE + where + this.END).get();
    }
  }
}
