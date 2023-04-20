package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import edu.brown.cs.student.main.database.DatabaseCommands;
import java.io.IOException;
import spark.Spark;

/** The Server class represents the main entry point for running the server. */
public class Server {

  /**
   * The main method initializes and starts the server, setting up necessary endpoints and handlers.
   *
   * @param args command line arguments (not used in this application).
   */
  public static void main(String[] args) throws IOException, InterruptedException {

    // Doesn't work for shit
    //FirebaseOptions options = FirebaseOptions.builder()
    //    .setCredentials(GoogleCredentials.getApplicationDefault())
    //    .setDatabaseUrl("https://cs32airobic-default-rtdb.firebaseio.com/")
    //    .build();
    //FirebaseApp.initializeApp(options);

    // Set the port number for the server to listen on.
    Spark.port(3235);

    // Just testing something here but it clearly doesn't work
    String s = "{ \"alanisawesome\": { \"name\": \"Alan Turing\", \"birthday\": \"June 23, 1912\" } }";
    new DatabaseCommands().put(s, "");
    String a = "alanisawesome/name";
    new DatabaseCommands().get(a);

    // Set the headers for cross-origin resource sharing (CORS) to allow any origin and any method.
    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    // Initialize and start the Spark server.
    Spark.init();
    Spark.awaitInitialization();

    // Print a message to indicate that the server has started successfully.
    System.out.println("Server started.");
  }
}
