# NonSusDatabaseCommands Class Documentation

[Linked Table of Contents](#linked-table-of-contents)

## Linked Table of Contents

* [1. Class Overview](#1-class-overview)
* [2. Class Variables](#2-class-variables)
* [3. Method Details](#3-method-details)
    * [3.1 `put(String data, String where)`](#31-putstring-data-string-where)
    * [3.2 `update(String data, String where)`](#32-updatestring-data-string-where)
    * [3.3 `delete(String where)`](#33-deletestring-where)
    * [3.4 `get(String where)`](#34-getstring-where)


## 1. Class Overview

The `NonSusDatabaseCommands` class provides methods for interacting with a remote database, likely a Firebase Realtime Database, using HTTP requests.  The class utilizes `HttpURLConnection` to send PUT, PATCH, DELETE, and GET requests to the specified database endpoint. Note that this class is marked as not currently in use.


## 2. Class Variables

| Variable Name | Type          | Description                                                                 |
|---------------|-----------------|-----------------------------------------------------------------------------|
| `DATABASE`    | `String`       | Base URL for the database.  Set to "https://cs32airobic-default-rtdb.firebaseio.com/". |
| `END`         | `String`       | Suffix added to the URL to complete the endpoint, set to ".json".          |


## 3. Method Details

### 3.1 `put(String data, String where)`

This method sends a PUT request to the database to create or replace a data entry.

**Parameters:**

* `data`:  A `String` representing the JSON data to be sent to the database.
* `where`: A `String` specifying the location within the database where the data should be stored (e.g., a path).

**Algorithm:**

1. Constructs the full URL by concatenating `DATABASE`, `where`, and `END`.
2. Opens an `HttpURLConnection` using the constructed URL and sets the request method to "PUT".
3. Sets the request headers to specify JSON content type and acceptance.
4. Sets `setDoOutput(true)` to indicate that the connection will send data.
5. Writes the provided `data` (converted to bytes using UTF-8 encoding) to the output stream of the connection.
6. Reads the response from the database using a `BufferedReader` and prints it to the console.  Only the first line of the response is captured.  Error handling for non-200 status codes is absent.


### 3.2 `update(String data, String where)`

This method sends a PATCH request to the database to update an existing data entry.

**Parameters:**

* `data`: A `String` representing the JSON data containing the updates.
* `where`: A `String` specifying the location of the data to update in the database.

**Algorithm:**

The algorithm is very similar to the `put` method, except it uses the "PATCH" HTTP method instead of "PUT".  This allows for partial updates of existing data. Error handling for non-200 status codes is absent.


### 3.3 `delete(String where)`

This method sends a DELETE request to the database to remove a data entry.

**Parameters:**

* `where`: A `String` specifying the location of the data to delete in the database.

**Algorithm:**

1. Constructs the full URL using `DATABASE`, `where`, and `END`.
2. Opens an `HttpURLConnection` and sets the request method to "DELETE".
3. Retrieves and prints the HTTP response code. This provides basic indication of success or failure, but lacks detailed error handling.


### 3.4 `get(String where)`

This method sends a GET request to the database to retrieve data.

**Parameters:**

* `where`: A `String` specifying the location of the data to retrieve.

**Return Value:**

* A `String` representing the first line of the JSON response from the database. Returns an empty string if the response is empty.

**Algorithm:**

1. Constructs the full URL.
2. Opens an `HttpURLConnection` and sets the request method to "GET".
3. Sets the Content-Type request header to "application/json".
4. Sets `setDoOutput(true)`.  While seemingly unnecessary for a GET request, this might be a remnant of previous code design or an oversight.  It's unlikely to cause issues but could be optimized.
5. Reads the response using a `BufferedReader` and returns the first line.  The response is also printed to the console.  Error handling for non-200 status codes is absent.
