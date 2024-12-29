# DatabaseCommands Class Documentation

[Linked Table of Contents](#table-of-contents)

## Table of Contents

* [1. Introduction](#introduction)
* [2. Class Overview](#class-overview)
* [3. Method Details](#method-details)
    * [3.1 `put(String data, String where)`](#putstring-data-string-where)
    * [3.2 `update(String data, String where)`](#updatestring-data-string-where)
    * [3.3 `delete(String where)`](#deletestring-where)
    * [3.4 `get(String where)`](#getstring-where)
* [4.  Error Handling](#error-handling)


<a name="introduction"></a>
## 1. Introduction

This document provides internal code documentation for the `DatabaseCommands` class. This class facilitates interaction with a Firebase Realtime Database using `curl` commands executed through the terminal.  It offers methods for creating, updating, deleting, and retrieving data from the database.


<a name="class-overview"></a>
## 2. Class Overview

The `DatabaseCommands` class encapsulates database operations, abstracting away the underlying `curl` command execution. It handles differences between Windows and other operating systems in how `curl` commands are formatted.  The database URL is hardcoded within the class.

| Variable           | Type     | Description                                                                 |
|--------------------|----------|-----------------------------------------------------------------------------|
| `DATABASE`         | `String` | Base URL for the database (non-Windows).                                    |
| `DATABASEWIN`      | `String` | Base URL for the database (Windows).                                        |
| `END`              | `String` | Suffix for the database URL (non-Windows).                                  |
| `ENDWIN`           | `String` | Suffix for the database URL (Windows).                                      |


<a name="method-details"></a>
## 3. Method Details


<a name="putstring-data-string-where"></a>
### 3.1 `put(String data, String where)`

This method performs a PUT operation on the specified database branch (`where`).  It completely replaces the existing data at that branch with the provided `data`.

* **Parameters:**
    * `data`: The data to be written to the database (as a String).
    * `where`: The path to the database branch (e.g., "users/123").

* **Algorithm:**
    1. Detects the operating system using `System.getProperty("os.name")`.
    2. Constructs the `curl` command string, differentiating between Windows and other OSes based on the detected OS.  The command uses `curl -X PUT` to perform a PUT request, sending the `data` to the specified `where` location.
    3. Executes the `curl` command using the `TerminalCommand` class.
    4. Throws `IOException` and `InterruptedException` if the `curl` command execution fails.


<a name="updatestring-data-string-where"></a>
### 3.2 `update(String data, String where)`

This method performs a PATCH operation, updating or adding data to the specified database branch (`where`) without deleting existing data.

* **Parameters:**
    * `data`: The data to be updated or added to the database (as a String).
    * `where`: The path to the database branch.

* **Algorithm:**
    1. Detects the operating system.
    2. Constructs the `curl` command using `curl -X PATCH`.  For Windows, it escapes double quotes within the `data` string to prevent issues with the shell.
    3. Executes the `curl` command via `TerminalCommand`.
    4. Throws `IOException` and `InterruptedException` on errors.


<a name="deletestring-where"></a>
### 3.3 `delete(String where)`

This method deletes the specified database branch.

* **Parameters:**
    * `where`: The path to the database branch to be deleted.

* **Algorithm:**
    1. Detects the operating system.
    2. Constructs the `curl` command using `curl -X DELETE`.
    3. Executes the command using `TerminalCommand`.
    4. Throws `IOException` and `InterruptedException` if deletion fails.


<a name="getstring-where"></a>
### 3.4 `get(String where)`

This method retrieves data from the specified database branch.

* **Parameters:**
    * `where`: The path to the database branch from which to retrieve data.

* **Algorithm:**
    1. Detects the operating system.
    2. Constructs the `curl` command (simply `curl` followed by the URL).
    3. Executes the command using `TerminalCommand.get()`, which returns the response.
    4. Returns the retrieved data as a String.
    5. Throws `IOException` and `InterruptedException` if retrieval fails.



<a name="error-handling"></a>
## 4. Error Handling

The `put`, `update`, `delete`, and `get` methods all throw `IOException` and `InterruptedException`.  `IOException` indicates issues with I/O operations (e.g., network problems), while `InterruptedException` suggests that the `curl` command execution was interrupted.  The calling methods are responsible for handling these exceptions.
