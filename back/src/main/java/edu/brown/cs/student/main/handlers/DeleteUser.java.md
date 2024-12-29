# DeleteUser Handler Documentation

[TOC]

## 1. Overview

The `DeleteUser` class implements the `Route` interface from the Spark framework.  It handles HTTP requests to delete a user from the database.  This is achieved by extracting the username from the request parameters and using the `DatabaseCommands` class to execute the deletion. The response is then serialized and returned.


## 2. Class Structure

```
public class DeleteUser implements Route {

  public DeleteUser() throws IOException, InterruptedException {}

  @Override
  public Object handle(Request request, Response response) throws Exception {
    // ... (Implementation details below) ...
  }
}
```

The class implements the `Route` interface, requiring the implementation of the `handle` method. The constructor is currently empty but declares potential `IOException` and `InterruptedException` which might be thrown by underlying database interactions (though not currently implemented in this example).


## 3. `handle` Method

The `handle` method is the core of the `DeleteUser` class. It processes incoming requests and performs the user deletion.

| Parameter | Type          | Description                                      |
|------------|---------------|--------------------------------------------------|
| `request`  | `Request`     | The incoming Spark HTTP request.                  |
| `response` | `Response`    | The Spark HTTP response object (not directly used). |

| Return Value   | Type       | Description                                                                 |
|-----------------|------------|-----------------------------------------------------------------------------|
| `Serializer.serialize(output)` | `Object`   | A serialized HashMap containing the result and a message.                      |


The algorithm followed by the `handle` method:

1. **Extract Username:** The username is extracted from the request query parameters using `request.queryParams("username")`.

2. **Input Validation:**  It checks if the username is `null`. If it is, an error response is generated indicating a bad request.

3. **Database Deletion:** If the username is valid (not `null`), it uses `new DatabaseCommands().delete("users/" + username);` to delete the user from the database.  This relies on the `DatabaseCommands` class to handle the actual database interaction (the specifics of this interaction are not detailed in this code).

4. **Response Generation:** A `HashMap` named `output` is created to store the result and a message.  The `result` key is set to "success" or "error_bad_request", and a corresponding message is added using the `message` key.

5. **Serialization and Return:**  Finally, the `HashMap` is serialized using the `Serializer` class and the resulting object is returned.  This serialized object is presumably suitable for sending as an HTTP response.



## 4. Error Handling

The code includes basic error handling for invalid input (null username).  An error message is returned indicating a bad request (`"error_bad_request"`).  More robust error handling might be added to catch and handle exceptions thrown by the `DatabaseCommands` class (e.g., database connection errors).



## 5. Dependencies

The `DeleteUser` class depends on:

* `edu.brown.cs.student.main.database.DatabaseCommands`: For database interaction.
* `edu.brown.cs.student.main.server.serializing.Serializer`: For serializing the response.
* `spark.Request`, `spark.Response`, `spark.Route`: Spark framework classes for handling HTTP requests and responses.
* `java.io.IOException`, `java.util.HashMap`: Standard Java classes for exception handling and data structures.


