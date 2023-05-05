package edu.brown.cs.student.main.database;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/*
Replace "path/to/your/serviceAccountKey.json" with the actual path to your Firebase service
account key JSON file. This file can be obtained from the Firebase console.
 */
public class DatabaseCommandsSDK {
  private static final String DATABASE_URL = "https://cs32airobic-default-rtdb.firebaseio.com/";
  private DatabaseReference ref;

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

  public void put(String data, String where) {
    DatabaseReference targetRef = ref.child(where);
    targetRef.setValueAsync(data);
  }

  public void update(Map<String, Object> data, String where) {
    DatabaseReference targetRef = ref.child(where);
    targetRef.updateChildrenAsync(data);
  }

  public void delete(String where) {
    DatabaseReference targetRef = ref.child(where);
    targetRef.removeValueAsync();
  }

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
}
