package edu.brown.cs.student.main.server;

import static spark.Spark.after;
import edu.brown.cs.student.main.handlers.AddNewUser;
import edu.brown.cs.student.main.handlers.DeleteUser;
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

    // Set the port number for the server to listen on.
    Spark.port(3235);

    // Set the headers for cross-origin resource sharing (CORS) to allow any origin and any method.
    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    Spark.get("adduser", new AddNewUser());
    // localhost:3232/adduser?username=alexfake
    Spark.get("deleteuser", new DeleteUser());
    // localhost:3232/deleteuser?username=alexfake
    //Spark.get("updateuser", new UpdateUser());
    // localhost:3232/updateuser?username=alexfake&preferences=blabla
    //Spark.get("getuserworkout", new GetUserWorkout());
    // localhost:3232/updateuser?username=alexfake&date=25apr2023

    // Initialize and start the Spark server.
    Spark.init();
    Spark.awaitInitialization();

    // Print a message to indicate that the server has started successfully.
    System.out.println("Server started.");
  }
}
