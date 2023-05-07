package edu.brown.cs.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static spark.Spark.after;

import com.squareup.moshi.Json;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.brown.cs.student.main.database.DatabaseCommands;
import edu.brown.cs.student.main.handlers.*;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

/** This class tests the API handlers */
public class ApiHandlerTests {

  private static Moshi moshi;

  /**
   * Sets up moshi before all tests.
   */
  @BeforeAll
  public static void setupBeforeEverything() {
    moshi = new Moshi.Builder().build();
  }

  /**
   * This method requests a URL and returns a response which is an object of type T.
   *
   * @param apiCall The URL to request.
   * @param tClass  The class of the object to be instantiated.
   * @param <T>     The type of the object to be instantiated.
   * @return An object of type T
   * @throws IOException If the URL is invalid.
   */
  private <T> T requestAndInstantiate(String apiCall, Class<T> tClass) throws IOException {
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.connect();
    assertEquals(200, clientConnection.getResponseCode());

    try (Buffer buf = new Buffer().readFrom(clientConnection.getInputStream())) {
      T response = moshi.adapter(tClass).fromJson(buf);
      clientConnection.disconnect();
      return response;
    }
  }

  /**
   * This method sets up the server before each test.
   */
  @BeforeEach
  public void setup() throws IOException, InterruptedException {
    after(
            (request, response) -> {
              response.header("Access-Control-Allow-Origin", "*");
              response.header("Access-Control-Allow-Methods", "*");
              response.header("Content-Type", "application/json");
            });

    Spark.get("adduser", new AddNewUser());
    Spark.get("deleteuser", new DeleteUser());
    Spark.get("create-plan", new CreatePlan());
    Spark.get("getuserworkouts", new GetUserWorkouts());
    Spark.get("checkuser", new CheckUser());
    Spark.get("updateworkout", new UpdateWorkout());

    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening

    // Deleting the testuser
    this.requestAndInstantiate("deleteuser?username=testuser", LoadResponse.class);
  }

  /**
   * This method tears down the server after each test.
   */
  @AfterEach
  public void teardown() {
    Spark.unmap("adduser");
    Spark.unmap("deleteuser");
    Spark.unmap("create");
    Spark.unmap("getuserworkouts");
    Spark.unmap("checkuser");
    Spark.unmap("updateworkout");
    Spark.awaitStop();
  }

  /**
   * This method tests addition and deletion of a user in the database.
   *
   * @throws IOException If the file is invalid.
   */
  @Test
  public void testAddingAndDeleting() throws IOException, InterruptedException {
    Thread.sleep(2000);
    // Testing if the add function works
    LoadResponse response =
            this.requestAndInstantiate("adduser?username=testuser", LoadResponse.class);
    assertNotNull(response);
    assertEquals("success", response.result);
    assertEquals(
            "Successfully added testuser",
            response.message);
    Thread.sleep(2000);
    // Testing if the user is actually added
    LoadResponse response2 =
            this.requestAndInstantiate("checkuser?username=testuser", LoadResponse.class);
    assertNotNull(response2);
    assertEquals("success", response2.result);
    assertEquals(
            "True",
            response2.message);
    Thread.sleep(2000);
    // Testing if the delete function works
    LoadResponse response3 =
            this.requestAndInstantiate("deleteuser?username=testuser", LoadResponse.class);
    assertNotNull(response3);
    assertEquals("success", response3.result);
    assertEquals(
            "Successfully deleted testuser",
            response3.message);
    Thread.sleep(2000);
    // Testing if the user is actually added
    LoadResponse response4 =
            this.requestAndInstantiate("checkuser?username=testuser", LoadResponse.class);
    assertNotNull(response4);
    assertEquals("success", response4.result);
    assertEquals(
            "False",
            response4.message);
  }

  /**
   * This method tests the workout getter
   *
   * @throws IOException If the file is invalid.
   */
  @Test
  public void testGetUserWorkouts() throws IOException, InterruptedException {
    Thread.sleep(2000);
    // Adding a user
    LoadResponse response =
            this.requestAndInstantiate("adduser?username=testuser", LoadResponse.class);
    assertNotNull(response);
    assertEquals("success", response.result);
    assertEquals(
            "Successfully added testuser",
            response.message);
    Thread.sleep(2000);
    // Adding a mock workout plan to the user
    String mockWorkout = "{\"days\":{\"0\":\"2000m test\", \"1\":\"60min UT2\"}}";
    String where = "users/testuser/schedule";
    new DatabaseCommands().update(mockWorkout, where);
    Thread.sleep(2000);
    // Testing if the workout getter works
    LoadResponse response2 =
            this.requestAndInstantiate("getuserworkouts?username=testuser", LoadResponse.class);
    assertNotNull(response2);
    assertEquals("success", response2.result);
    assertEquals("{\"days\":[\"2000m test\",\"60min UT2\"]}",
            response2.message);
    Thread.sleep(2000);
  }

  /**
   * This method tests the workout getter error case
   *
   * @throws IOException If the file is invalid.
   */
  @Test
  public void testGetUserWorkoutsError() throws IOException, InterruptedException {
    Thread.sleep(2000);
    // Testing if the workout getter works with the wrong input
    LoadResponse response2 =
            this.requestAndInstantiate("getuserworkouts", LoadResponse.class);
    assertNotNull(response2);
    assertEquals("error_bad_request", response2.result);
    assertEquals("ERROR: Invalid input.",
            response2.message);
  }

  /**
   * This method tests the workout getter no user
   *
   * @throws IOException If the file is invalid.
   */
  @Test
  public void testGetUserWorkoutsNoUser() throws IOException, InterruptedException {
    Thread.sleep(2000);
    // Testing if the workout getter works with the wrong input
    LoadResponse response2 =
            this.requestAndInstantiate("getuserworkouts?username=testuser", LoadResponse.class);
    assertNotNull(response2);
    assertEquals("success", response2.result);
    assertEquals("null",
            response2.message);
  }

  /**
   * This method tests the plan creation and updating data for a specific workout
   *
   * @throws IOException If the file is invalid.
   */
  @Test
  public void testCreatePlanModel1AndUpdatePlan() throws IOException, InterruptedException {
    Thread.sleep(2000);
    // Adding a user
    LoadResponse response =
            this.requestAndInstantiate("adduser?username=testuser", LoadResponse.class);
    assertNotNull(response);
    assertEquals("success", response.result);
    assertEquals(
            "Successfully added testuser",
            response.message);
    Thread.sleep(2000);
    // Adding a workout plan to the user
    LoadResponse response2 =
            this.requestAndInstantiate("create-plan?username=testuser&model=model1&hoursPerWeek=600&sport=Rowing&startDate=2023-05-05&endDate=2023-06-05", LoadResponse.class);
    assertNotNull(response2);
    assertEquals("success", response2.result);
    assertEquals(
            "Successfully updated testuser",
            response2.message);
    Thread.sleep(2000);
    // Adding data information about the first workout
    LoadResponse response3 =
            this.requestAndInstantiate("updateworkout?username=testuser&day=0&workout=0&rpe=10&split=2:20.0&distance=6000", LoadResponse.class);
    assertNotNull(response3);
    assertEquals("success", response3.result);
    assertEquals("Successfully added workout data",
            response3.message);
    Thread.sleep(2000);
    // Checking if the workout exists and if the data is updated
    String distance = new DatabaseCommands().get("users/testuser/schedule/days/0/workouts/0/data/distance");
    String split = new DatabaseCommands().get("users/testuser/schedule/days/0/workouts/0/data/split");
    String rpe = new DatabaseCommands().get("users/testuser/schedule/days/0/workouts/0/data/rpe");
    assertEquals("\"6000\"", distance);
    assertEquals("\"2:20.0\"",split);
    assertEquals("\"10\"",rpe);
  }

  /**
   * This method tests the plan creation and updating data for a specific workout
   *
   * @throws IOException If the file is invalid.
   */
  @Test
  public void testCreatePlanModel2AndUpdatePlan() throws IOException, InterruptedException {
    Thread.sleep(2000);
    // Adding a user
    LoadResponse response =
            this.requestAndInstantiate("adduser?username=testuser", LoadResponse.class);
    assertNotNull(response);
    assertEquals("success", response.result);
    assertEquals(
            "Successfully added testuser",
            response.message);
    Thread.sleep(2000);
    // Adding a workout plan to the user
    LoadResponse response2 =
            this.requestAndInstantiate("create-plan?username=testuser&model=model2&hoursPerWeek=600&sport=Rowing&startDate=2023-05-05&endDate=2023-06-05", LoadResponse.class);
    assertNotNull(response2);
    assertEquals("success", response2.result);
    assertEquals(
            "Successfully updated testuser",
            response2.message);
    Thread.sleep(2000);
    // Adding data information about the first workout
    LoadResponse response3 =
            this.requestAndInstantiate("updateworkout?username=testuser&day=0&workout=0&rpe=10&split=2:20.0&distance=6000", LoadResponse.class);
    assertNotNull(response3);
    assertEquals("success", response3.result);
    assertEquals("Successfully added workout data",
            response3.message);
    Thread.sleep(2000);
    // Checking if the workout exists and if the data is updated
    String distance = new DatabaseCommands().get("users/testuser/schedule/days/0/workouts/0/data/distance");
    String split = new DatabaseCommands().get("users/testuser/schedule/days/0/workouts/0/data/split");
    String rpe = new DatabaseCommands().get("users/testuser/schedule/days/0/workouts/0/data/rpe");
    assertEquals("\"6000\"", distance);
    assertEquals("\"2:20.0\"",split);
    assertEquals("\"10\"",rpe);
  }

  /**
   * This method tests the plan creation and updating data for a specific workout
   *
   * @throws IOException If the file is invalid.
   */
  @Test
  public void testCreatePlanModel3AndUpdatePlan() throws IOException, InterruptedException {
    Thread.sleep(2000);
    // Adding a user
    LoadResponse response =
            this.requestAndInstantiate("adduser?username=testuser", LoadResponse.class);
    assertNotNull(response);
    assertEquals("success", response.result);
    assertEquals(
            "Successfully added testuser",
            response.message);
    Thread.sleep(2000);
    // Adding a workout plan to the user
    LoadResponse response2 =
            this.requestAndInstantiate("create-plan?username=testuser&model=model3&hoursPerWeek=600&sport=Rowing&startDate=2023-05-05&endDate=2023-06-05&goal=2k", LoadResponse.class);
    assertNotNull(response2);
    assertEquals("success", response2.result);
    assertEquals(
            "Successfully updated testuser",
            response2.message);
    Thread.sleep(2000);
    // Adding data information about the first workout
    LoadResponse response3 =
            this.requestAndInstantiate("updateworkout?username=testuser&day=0&workout=0&rpe=10&split=2:20.0&distance=6000", LoadResponse.class);
    assertNotNull(response3);
    assertEquals("success", response3.result);
    assertEquals("Successfully added workout data",
            response3.message);
    Thread.sleep(2000);
    // Checking if the workout exists and if the data is updated
    String distance = new DatabaseCommands().get("users/testuser/schedule/days/0/workouts/0/data/distance");
    String split = new DatabaseCommands().get("users/testuser/schedule/days/0/workouts/0/data/split");
    String rpe = new DatabaseCommands().get("users/testuser/schedule/days/0/workouts/0/data/rpe");
    assertEquals("\"6000\"", distance);
    assertEquals("\"2:20.0\"",split);
    assertEquals("\"10\"",rpe);
  }

  /**
   * Json record for the load response.
   *
   * @param result  the result returned by the server
   * @param message the message returned by the server
   */
  public record LoadResponse(
          @Json(name = "result") String result, @Json(name = "message") String message) {
  }
}
