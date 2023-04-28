package edu.brown.cs.student.main.server;

import static spark.Spark.after;
import edu.brown.cs.student.main.handlers.AddNewUser;
import edu.brown.cs.student.main.handlers.CheckUser;
import edu.brown.cs.student.main.handlers.DeleteUser;
import edu.brown.cs.student.main.handlers.GetUserWorkouts;
import edu.brown.cs.student.main.handlers.UpdateUser;
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
    Spark.get("updateuser", new UpdateUser());
    // localhost:3235/updateuser?username=alexfake&sport=rowing&startdate=27Apr2023&enddate=29Apr2023&weeklytime=10&model=linear&goal=2k
    Spark.get("getuserworkouts", new GetUserWorkouts());
    // localhost:3235/getuserworkouts?username=alexfake
    Spark.get("checkuser", new CheckUser());
    // localhost:3235/checkuser?username=alexfake

    // Initialize and start the Spark server.
    Spark.init();
    Spark.awaitInitialization();

    // Print a message to indicate that the server has started successfully.
    System.out.println("Server started.");
  }
}
