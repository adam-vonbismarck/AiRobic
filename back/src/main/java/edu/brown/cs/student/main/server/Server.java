package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.handlers.*;

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
    // localhost:3235/adduser?username=alexfake
    Spark.get("deleteuser", new DeleteUser());
    // localhost:3235/deleteuser?username=alexfake
    Spark.get("create-plan", new CreatePlan());
    // localhost:3235/create-plan?username=alexfake&sport=rowing&startDate=27Apr2023&endDate=29Apr2023&hoursPerWeek=10&model=linear&goal=2k
    Spark.get("getuserworkouts", new GetUserWorkouts());
    // localhost:3235/getuserworkouts?username=alexfake
    Spark.get("checkuser", new CheckUser());
    // localhost:3235/checkuser?username=alexfake
    Spark.get("updateworkout", new UpdateWorkout());
    // localhost:3235/updateworkout?username=alexfake&day=0&workout=0&rpe=10&split=2:20.0&distance=6000

    // Initialize and start the Spark server.
    Spark.init();
    Spark.awaitInitialization();

    // Print a message to indicate that the server has started successfully.
    System.out.println("Server started.");

    // Print a message to indicate that the server will not work for Windows.
    String os = System.getProperty("os.name").toLowerCase();
    if (os.contains("win")) {
      System.out.println("This code will not work for Windows");
    }
  }
}
