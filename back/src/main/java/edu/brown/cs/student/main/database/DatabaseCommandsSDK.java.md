# DatabaseCommandsSDK Internal Documentation

[Linked Table of Contents](#linked-table-of-contents)

## Linked Table of Contents

* [1. Introduction](#1-introduction)
* [2. Class Overview: `DatabaseCommandsSDK`](#2-class-overview-databasecommandsdk)
    * [2.1 Constructor: `DatabaseCommandsSDK()`](#21-constructor-databasecommandsdk)
    * [2.2 Method: `put(String data, String where)`](#22-method-putstring-data-string-where)
    * [2.3 Method: `update(Map<String, Object> data, String where)`](#23-method-updatemapstring-object-data-string-where)
    * [2.4 Method: `delete(String where)`](#24-method-deletestring-where)
    * [2.5 Method: `get(String where)`](#25-method-getstring-where)


## 1. Introduction

This document provides internal code documentation for the `DatabaseCommandsSDK` class.  This class interacts with a Firebase Realtime Database. Note that this class is currently marked as unused.

## 2. Class Overview: `DatabaseCommandsSDK`

The `DatabaseCommandsSDK` class provides a simplified interface for common database operations (put, update, delete, get) using the Firebase Realtime Database SDK. It handles Firebase initialization and provides asynchronous methods for data manipulation.

### 2.1 Constructor: `DatabaseCommandsSDK()`

The constructor initializes the Firebase application and obtains a reference to the database.

```java
public DatabaseCommandsSDK() {
    try {
        FileInputStream serviceAccount =
                new FileInputStream(
                        "front/src/private/cs32airobic-firebase-adminsdk-7sap5-9a503e7dc1.json");
        FirebaseOptions options =
                FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl(DATABASE_URL)
                        .build();
        FirebaseApp.initializeApp(options);
        ref = FirebaseDatabase.getInstance().getReference();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

This constructor reads Firebase credentials from a JSON file, initializes the Firebase app using those credentials and the specified database URL (`DATABASE_URL`), and gets a root reference to the database.  Error handling is included to catch potential `IOException` during file reading.


### 2.2 Method: `put(String data, String where)`

This method asynchronously adds or overwrites data at a specified location in the database.

```java
public void put(String data, String where) {
    DatabaseReference targetRef = ref.child(where);
    targetRef.setValueAsync(data);
}
```

It uses `setValueAsync` for asynchronous operation, making the call non-blocking.  The `where` parameter specifies the path within the database where the data should be stored.


### 2.3 Method: `update(Map<String, Object> data, String where)`

This method asynchronously updates existing data at a specified location.

```java
public void update(Map<String, Object> data, String where) {
    DatabaseReference targetRef = ref.child(where);
    targetRef.updateChildrenAsync(data);
}
```

It uses `updateChildrenAsync` to update only the specified keys within the specified location.  The `data` parameter is a map where keys represent the fields to update and values represent their new values. This is also an asynchronous operation.


### 2.4 Method: `delete(String where)`

This method asynchronously deletes data at a specified location.

```java
public void delete(String where) {
    DatabaseReference targetRef = ref.child(where);
    targetRef.removeValueAsync();
}
```

Uses `removeValueAsync` for asynchronous deletion of data at the specified path.


### 2.5 Method: `get(String where)`

This method asynchronously retrieves data from a specified location and returns it as a String.  It uses a `CountDownLatch` to ensure the data is retrieved before returning.

```java
public String get(String where) throws InterruptedException {
    DatabaseReference targetRef = ref.child(where);
    final CountDownLatch latch = new CountDownLatch(1);
    final StringBuilder result = new StringBuilder();

    targetRef.addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        result.append(dataSnapshot.getValue(String.class));
                    }
                    latch.countDown();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.err.println("Error: " + databaseError.getMessage());
                    latch.countDown();
                }
            });

    latch.await();
    return result.toString();
}
```

The method uses a `ValueEventListener` to listen for a single value event.  The `CountDownLatch` synchronizes the execution, making the method wait until the data is received or an error occurs.  The retrieved data (if it exists) is appended to a `StringBuilder` and returned as a String.  Error handling is included to print error messages if the database operation is cancelled.  The use of a `CountDownLatch` ensures that the method waits for the asynchronous operation to complete before returning a result, preventing premature returns with potentially null or incorrect data.

