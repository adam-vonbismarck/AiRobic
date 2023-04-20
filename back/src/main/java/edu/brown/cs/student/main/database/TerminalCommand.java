package edu.brown.cs.student.main.database;
import java.io.IOException;


public class TerminalCommand {


  public TerminalCommand(String input) throws IOException, InterruptedException {
    String[] command = input.split(" ");
    for (String comm : command) {
      System.out.println(comm);
    }
    new ProcessBuilder(command).start();
  }
}

