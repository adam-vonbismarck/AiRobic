package edu.brown.cs.student.main.server.main;

import static spark.Spark.after;
import spark.Spark;

/** The Server class represents the main entry point for running the server. */
public class Server {

  /**
   * The main method initializes and starts the server, setting up necessary endpoints and handlers.
   *
   * @param args command line arguments (not used in this application).
   */
  public static void main(String[] args) {
    // Set the port number for the server to listen on.
    Spark.port(3235);

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
