# TerminalCommand Class Documentation

[TOC]

## 1. Introduction

The `TerminalCommand` class provides a mechanism to execute terminal commands from within a Java application.  It handles operating system differences (Windows vs. macOS/Linux) to ensure consistent functionality across platforms.


## 2. Class Overview

| Feature          | Description                                                                |
|-----------------|----------------------------------------------------------------------------|
| **Name**         | `TerminalCommand`                                                          |
| **Package**      | `edu.brown.cs.student.main.database`                                      |
| **Purpose**      | Executes terminal commands and optionally retrieves their output.            |


## 3. Class Variables

| Variable Name | Data Type | Description                                      |
|---------------|------------|--------------------------------------------------|
| `input`       | `String`   | The terminal command to be executed.             |


## 4. Constructor

**`TerminalCommand(String input)`**

* **Parameters:**
    * `input`: A `String` representing the terminal command to be executed.
* **Description:** This constructor initializes the `TerminalCommand` object with the provided command string.


## 5. Methods

### 5.1 `get()` Method

**`public String get() throws IOException, InterruptedException`**

* **Return Value:** A `String` containing the first line of the command's output. Returns `null` if there is an error or the command produces no output.
* **Throws:**
    * `IOException`: If an I/O error occurs during command execution.
    * `InterruptedException`: If the command execution is interrupted.
* **Description:** This method executes the specified terminal command and returns the first line of its standard output.  The algorithm is as follows:

1. **Determine Operating System:** It checks the operating system using `System.getProperty("os.name")`.  If the OS name contains "win", it assumes a Windows environment.

2. **Construct Command Array:** It constructs a `String` array representing the command to be executed.  For macOS/Linux, it uses `/bin/bash -c` to execute the command; for Windows, it uses `cmd.exe /c`.

3. **Execute Command:** It uses `ProcessBuilder` to execute the command.

4. **Capture Output:** It captures the standard output using `InputStream`, `InputStreamReader`, and `BufferedReader`. It reads and returns only the first line of the output using `readLine()`.

5. **Return Output:** The first line of the command output is returned as a `String`.


### 5.2 `run()` Method

**`public void run() throws IOException, InterruptedException`**

* **Throws:**
    * `IOException`: If an I/O error occurs during command execution.
    * `InterruptedException`: If the command execution is interrupted.
* **Description:** This method executes the specified terminal command but does not capture or return its output.  The algorithm mirrors the `get()` method up to the point of capturing output.  It simply initiates the process and does not wait for it to complete or handle its output.  The key difference is the omission of output stream handling.  This method is suitable when only the execution of the command, and not its output, is needed.


## 6. Example Usage

```java
TerminalCommand lsCommand = new TerminalCommand("ls -l");
try {
  String output = lsCommand.get();
  System.out.println("Output: " + output);
} catch (IOException | InterruptedException e) {
  System.err.println("Error executing command: " + e.getMessage());
}

TerminalCommand touchCommand = new TerminalCommand("touch newFile.txt");
try {
    touchCommand.run();
    System.out.println("Command executed successfully.");
} catch (IOException | InterruptedException e) {
    System.err.println("Error executing command: " + e.getMessage());
}

```

