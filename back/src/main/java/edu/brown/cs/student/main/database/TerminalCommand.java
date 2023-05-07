package edu.brown.cs.student.main.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/** This class calls the Terminal commands for Database */
public class TerminalCommand {
  private final String input;

  public TerminalCommand(String input) {
    this.input = input;
  }

  // This method runs a terminal command and gets its output
  public String get() throws IOException, InterruptedException {
    // In case user uses IOS
    String[] command = {"/bin/bash", "-c", this.input};
    String os = System.getProperty("os.name").toLowerCase();
    // In case user uses Windows
    if (os.contains("win")) {
      System.out.println("Windows get");
      command = new String[] {"cmd.exe", "/c", this.input};
    }
    System.out.println(this.input);
    ProcessBuilder builder = new ProcessBuilder(command);
    Process process = builder.start();
    InputStream is = process.getInputStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    String r = reader.readLine();
    System.out.println(r);
    return r;
  }

  // This method runs a terminal command
  public void run() throws IOException, InterruptedException {
    // In case user uses IOS
    String[] command = {"/bin/bash", "-c", this.input};
    String os = System.getProperty("os.name").toLowerCase();
    // In case user uses Windows
    if (os.contains("win")) {
      System.out.println("Windows run");
      command = new String[] {"cmd.exe", "/c", this.input};
    }
    ProcessBuilder builder = new ProcessBuilder(command);
    builder.start();
  }
}
