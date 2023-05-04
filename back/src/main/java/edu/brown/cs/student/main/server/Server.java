package edu.brown.cs.student.main.server;

import static spark.Spark.after;
import edu.brown.cs.student.main.handlers.AddNewUser;
import edu.brown.cs.student.main.handlers.CheckUser;
import edu.brown.cs.student.main.handlers.DeleteUser;
import edu.brown.cs.student.main.handlers.GetUserWorkouts;
import edu.brown.cs.student.main.handlers.CreatePlan;
import edu.brown.cs.student.main.helpers.DateListCreator;
import edu.brown.cs.student.main.helpers.DayOfTheWeek;
import java.io.IOException;
import java.util.List;
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

    //String startDateStr = "01-01-2023";
    //String endDateStr = "01-07-2023";
    //List<String> dates = DateListCreator.getDatesBetween(startDateStr, endDateStr);
    //System.out.println(dates);
    //String dateStr = "04-29-2023";
    //String dayOfWeek = DayOfTheWeek.getDayOfWeek(dateStr);
    //System.out.println(dayOfWeek);



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

    // Initialize and start the Spark server.
    Spark.init();
    Spark.awaitInitialization();

    // Print a message to indicate that the server has started successfully.
    System.out.println("Server started.");
  }
}
