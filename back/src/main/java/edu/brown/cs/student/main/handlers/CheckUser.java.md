# CheckUser Handler Documentation

[Linked Table of Contents](#linked-table-of-contents)

## 1. Overview

This document provides internal code documentation for the `CheckUser` handler class.  This class implements a `spark.Route` to handle requests checking for the existence of a user in the database.  It retrieves user validity from the database and returns a JSON response indicating success or failure.

## 2. Class Structure: `CheckUser`

The `CheckUser` class implements the `spark.Route` interface, requiring the implementation of the `handle` method.


| Member        | Description                                                                                             | Type                     |
|----------------|---------------------------------------------------------------------------------------------------------|--------------------------|
| `handle`      | Processes incoming requests to check user existence.                                                    | `Object handle(Request, Response)` |


## 3. Method Details: `handle(Request request, Response response)`

This method is the core logic of the `CheckUser` handler. It takes a `Request` and `Response` object as input and returns a serialized JSON object.

**Algorithm:**

1. **Extract Username:** The method first extracts the username from the request parameters using `request.queryParams("username")`.

2. **Input Validation:** It checks if the username is null. If so, it indicates an error by setting the `"result"` key to `"error_bad_request"` and a corresponding error message.

3. **Database Query:** If the username is valid, it constructs a database path using string concatenation: `"users/" + username + "/valid"`.  This path is then used to query the database using `new DatabaseCommands().get(where)`. This assumes the database is structured to store a boolean value indicating user validity at this path.

4. **Response Generation:** The method checks the result from the database query.  The string `"\\"true\\""` is explicitly compared against, suggesting the database returns a string representation of a boolean value.

    * If the retrieved value is equal to `"\\"true\\""`, it means the user exists and is valid. The output HashMap is populated with `"result": "success"` and `"message": "True"`.

    * Otherwise, the user either doesn't exist or is not valid. The output HashMap is populated with `"result": "success"` and `"message": "False"`.  Note that both valid and invalid cases return a `"success"` result; the distinction is made through the `"message"` field.


5. **Serialization and Return:** Finally, the method serializes the `output` HashMap into a JSON string using `Serializer.serialize(output)` and returns it.

**Code Example:**

```java
  @Override
  public Object handle(Request request, Response response) throws Exception {
    String username = request.queryParams("username");
    HashMap<String, Object> output = new HashMap<>();
    if (username == null) {
      output.put("result", "error_bad_request");
      output.put("message", "ERROR: Invalid input.");
    } else {
      String where = "users/" + username + "/valid";
      String valid = new DatabaseCommands().get(where);
      if (Objects.equals(valid, "\\"true\\"")) {
        output.put("result", "success");
        output.put("message", "True");
      } else {
        output.put("result", "success");
        output.put("message", "False");
      }
    }
    return Serializer.serialize(output);
  }
```

## <a name="linked-table-of-contents">Linked Table of Contents</a>

* [1. Overview](#1-overview)
* [2. Class Structure: `CheckUser`](#2-class-structure-checkuser)
* [3. Method Details: `handle(Request request, Response response)`](#3-method-details-handlerequest-request-response-response)

