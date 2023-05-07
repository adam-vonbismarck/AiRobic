package edu.brown.cs.student;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.beust.ah.A;
import edu.brown.cs.student.genericdeserializertesting.types.BrownStudent;
import edu.brown.cs.student.genericdeserializertesting.types.ComplexStar;
import edu.brown.cs.student.main.models.exceptions.InvalidScheduleException;
import edu.brown.cs.student.main.models.formattypes.Day;
import edu.brown.cs.student.main.models.formattypes.FlatSchedule;
import edu.brown.cs.student.main.models.markov.model.Emission;
import edu.brown.cs.student.main.models.markov.modelbuilding.Workout;
import edu.brown.cs.student.main.server.serializing.Serializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * This class contains unit tests for the Serializer util class (FOR GENERIC JSON PARSING). Adapted from REPL
 * code that cbaker20 wrote.
 */
public class SerializerTests {

  /**
   * Tests a valid example of using the getDeserializedResponse method (with both overloaded
   * versions). Also tests the case where there are additional fields, outside of the scope of the
   * type entered.
   */
  @Test
  public void testExampleStudent() {
    try {
      BrownStudent exampleFile =
          Serializer.getDeserializedResponse(
              BrownStudent.class,
              "src/test/java/edu/brown/cs/student/genericdeserializertesting"
                  + "/examples/example-student.json");
      Assertions.assertEquals(2149.853048670803, exampleFile.getYear());
      Assertions.assertEquals("Me", exampleFile.name());
      BrownStudent exampleStream =
          Serializer.getDeserializedResponse(
              BrownStudent.class,
              new FileInputStream(
                  "src/test/java/edu/brown/cs/student/genericdeserializertesting"
                      + "/examples/example-student.json"));
      Assertions.assertEquals(exampleFile, exampleStream);
    } catch (IOException e) {
      Assertions.assertEquals("No error", "IOException when reading file");
    }
  }

  /**
   * Tests an example of using the getDeserializedResponse method (with both overloaded versions),
   * where some list components are empty.
   */
  @Test
  public void testEmptyStudent() {
    try {
      BrownStudent exampleFile =
          Serializer.getDeserializedResponse(
              BrownStudent.class,
              "src/test/java/edu/brown/cs/student/genericdeserializertesting"
                  + "/examples/empty-student.json");
      Assertions.assertEquals(-99, exampleFile.getYear());
      Assertions.assertEquals("Empty", exampleFile.name());
      BrownStudent exampleStream =
          Serializer.getDeserializedResponse(
              BrownStudent.class,
              new FileInputStream(
                  "src/test/java/edu/brown/cs/student/genericdeserializertesting"
                      + "/examples/empty-student.json"));
      Assertions.assertEquals(exampleFile, exampleStream);
    } catch (IOException e) {
      Assertions.assertEquals("No error", "IOException when reading file");
    }
  }

  /**
   * Tests an example of using the getDeserializedResponse method (with both overloaded versions),
   * where some fields of the type inputted are missing.
   */
  @Test
  public void testMissingStar() {
    try {
      ComplexStar exampleFile =
          Serializer.getDeserializedResponse(
              ComplexStar.class,
              "src/test/java/edu/brown/cs/student/genericdeserializertesting"
                  + "/examples/example-star.json");
      Assertions.assertEquals(138.993, exampleFile.getDistance());
      Assertions.assertNull(exampleFile.name());
      ComplexStar exampleStream =
          Serializer.getDeserializedResponse(
              ComplexStar.class,
              new FileInputStream(
                  "src/test/java/edu/brown/cs/student/genericdeserializertesting"
                      + "/examples/example-star.json"));
      Assertions.assertEquals(exampleFile, exampleStream);
    } catch (IOException e) {
      Assertions.assertEquals("No error", "IOException when reading file");
    }
  }

  /**
   * Tests an example of using the getDeserializedResponse method (with both overloaded versions),
   * where all fields of the type inputted are missing.
   */
  @Test
  public void testMissingStudent() {
    try {
      BrownStudent exampleFile =
          Serializer.getDeserializedResponse(
              BrownStudent.class,
              "src/test/java/edu/brown/cs/student/genericdeserializertesting"
                  + "/examples/example-star.json");

      Exception exn =
          Assertions.assertThrows(
              NullPointerException.class,
              () -> {
                exampleFile.getYear();
              });
      Assertions.assertEquals(
          exn.getMessage(),
          "Cannot invoke \"java.util.List.iterator()\" because \"this.classYear\" is null");

      Assertions.assertNull(exampleFile.name());
      BrownStudent exampleStream =
          Serializer.getDeserializedResponse(
              BrownStudent.class,
              new FileInputStream(
                  "src/test/java/edu/brown/cs/student/genericdeserializertesting"
                      + "/examples/example-star.json"));
      Assertions.assertEquals(exampleFile, exampleStream);
    } catch (IOException e) {
      Assertions.assertEquals("No error", "IOException when reading file");
    }
  }

  /**
   * Tests serializing a valid schedule.
   */
  @Test
  public void testSerializeSchedule() throws InvalidScheduleException {
    String serialized = Serializer.serializeSchedule(new FlatSchedule(List.of(
            new Day("day", new ArrayList<>(), 5, DayOfWeek.FRIDAY, Optional.empty(),
                    new ArrayList<>()),
            new Day("day", List.of(new Emission("workout", 20, "workout1")),
                    1, DayOfWeek.FRIDAY, Optional.empty(),
                    List.of(new Day.WorkoutDescription(Workout._30R_20, 20)))
            )));
    Assertions.assertEquals(serialized, "{\"days\":[{\"date\":\"null\",\"day\":" +
            "\"FRIDAY\",\"numberOfWorkouts\":5,\"type\":\"day\",\"workoutPlan\":[],\"workouts\":[]}," +
            "{\"date\":\"null\",\"day\":\"FRIDAY\",\"numberOfWorkouts\":1,\"type\":\"day\"," +
            "\"workoutPlan\":[{\"workoutType\":\"_30R_20\",\"minutes\":20}],\"workouts\":" +
            "[{\"time\":20.0,\"title\":\"workout1\",\"workout\":\"workout\"}]}]}");
  }

  /**
   * Tests serializing an empty schedule.
   */
  @Test
  public void testSerializeScheduleEmpty() throws InvalidScheduleException {
    String serialized = Serializer.serializeSchedule(new FlatSchedule(List.of()));
    Assertions.assertEquals(serialized, "{\"days\":[]}");
  }

}
