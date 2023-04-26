package edu.brown.cs.student.main.database;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TerminalCommand {
  private final String input;

  public TerminalCommand(String input) {this.input = input;}

  // This method runs a terminal command and gets its output
  public String get() throws IOException, InterruptedException {
    String[] command = {"/bin/bash", "-c", this.input};
    ProcessBuilder builder = new ProcessBuilder(command);
    Process process = builder.start();
    InputStream is = process.getInputStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    return reader.readLine();
  }

  // This method runs a terminal command
  public void run() throws IOException, InterruptedException {
    String[] command = {"/bin/bash", "-c", this.input};
    ProcessBuilder builder = new ProcessBuilder(command);
    builder.start();
  }
}

