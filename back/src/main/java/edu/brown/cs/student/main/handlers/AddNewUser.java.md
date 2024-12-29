# AddNewUser Handler Documentation

[Linked Table of Contents](#linked-table-of-contents)

## Linked Table of Contents

* [1. Overview](#1-overview)
* [2. `handle` Method Detail](#2-handle-method-detail)
    * [2.1 Algorithm](#21-algorithm)
    * [2.2 Error Handling](#22-error-handling)
    * [2.3 Data Serialization](#23-data-serialization)


## 1. Overview

The `AddNewUser` class implements the `Route` interface from the Spark framework and acts as a handler for adding new users to the application's database.  It takes a username as input via a query parameter and adds the user to the database if the username is not already in use. The class utilizes the `DatabaseCommands` class for database interactions and the `Serializer` class for output formatting.


## 2. `handle` Method Detail

The core functionality resides within the `handle` method, which processes incoming requests and returns a JSON response.

### 2.1 Algorithm

The `handle` method follows these steps:

1. **Retrieve Username:** Extracts the username from the request's query parameters using `request.queryParams("username")`.
2. **Input Validation:** Checks if the username is provided. If not, it returns an error response.
3. **User Existence Check:** Queries the database at the path "users/{username}/valid" to check if a user with that name already exists.  The `DatabaseCommands.get()` method is used for this query. The expected value for an existing user is "\"true\"", which is compared using `Objects.equals()` for robust comparison. Note that the use of escaped double quotes is crucial for correct string representation within the JSON structure handled by the database.
4. **User Addition:** If the user does not exist (the database query returns a value other than "\"true\""), a JSON string representing the new user's data (`info`) is constructed. This string includes the username, an initially empty schedule, and a "valid" flag set to "true". The `DatabaseCommands.update()` method then adds this user information to the database's "users" section.
5. **Response Generation:** A HashMap `output` is constructed to hold the result and message.  The result is set to "success" or "error_bad_request" depending on whether the user was added successfully or not.
6. **Serialization and Return:** The `output` HashMap is serialized into a JSON string using `Serializer.serialize()` and returned as the response.

### 2.2 Error Handling

The method incorporates error handling for two scenarios:

| Error Condition                     | Response                                      |
|--------------------------------------|----------------------------------------------|
| Missing Username Parameter          | `{"result": "error_bad_request", "message": "ERROR: Invalid input."}` |
| Username Already Exists             | `{"result": "error_bad_request", "message": "ERROR: User already exists."}` |


These error responses provide informative messages to the client.


### 2.3 Data Serialization

The final response is generated as a JSON string using the `Serializer` class. This ensures that the data is formatted consistently and can be easily parsed by the client application.  The structure of the returned JSON is designed for easy processing, containing a "result" field indicating success or failure, and a "message" field providing additional information.  For example, a successful addition returns  `{"result":"success", "message":"Successfully added username"}`.

| Field      | Data Type | Description                                   |
|-------------|------------|-----------------------------------------------|
| `result`   | String     | "success" or "error_bad_request"              |
| `message`  | String     |  Descriptive message explaining the result     |

The use of a `HashMap` allows for flexible addition of fields to the response as needed in the future.
