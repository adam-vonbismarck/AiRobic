package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.models.WorkoutDistributionByName;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import okio.Buffer;

/** A static class containing utility methods for serializing and deserializing API responses. */
public class Serializer {

  /**
   * This method serializes the results HashMap for returning as the API response. It also ensures
   * that LocalDates can be serialized, as we use them in our Day objects.
   *
   * @param map - The map to be serialized
   * @return The serialized version of the map
   */
  public static String serialize(HashMap<String, Object> map) {
    Moshi moshi = new Moshi.Builder().add(Types.newParameterizedType(Optional.class, LocalDate.class), new LocalDateJsonAdapter()).build();
    Type genericMap = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(genericMap);
    return adapter.toJson(map);
  }

  /**
   * This method serializes the schedule that needs to be returned.
   *
   * @param schedule - The schedule to be serialized
   * @return The serialized version of the schedule
   */
  public static String serializeSchedule(Schedule schedule) {
    Moshi moshi = new Moshi.Builder().add(Types.newParameterizedType(Optional.class, LocalDate.class), new LocalDateJsonAdapter()).build();
    JsonAdapter<Schedule> adapter = moshi.adapter(Schedule.class);
    return adapter.toJson(schedule);
  }

  /**
   * Found this javadoc, which helped us make this method generic: <a
   * href="https://docs.oracle.com/javase/tutorial/extra/generics/literals.html">...</a>. This
   * method takes in a class type and an input stream, and returns the serialized version of the
   * input stream as the class type. It also ensures that LocalDates can be deserialized, as we use
   * them in our Day objects.
   *
   * @param type - Type of the deserialized response
   * @param inputStream - Input stream to be deserialized
   * @param <T> - The same type as the type parameter, which will be returned by the method
   * @return The deserialized response
   * @throws IOException If there is an error reading from the input stream.
   */
  public static <T> T getDeserializedResponse(Class<T> type, InputStream inputStream)
      throws IOException {
    Moshi moshi =
        new Moshi.Builder()
            .add(
                Types.newParameterizedType(Optional.class, LocalDate.class),
                new LocalDateJsonAdapter()).build();
    JsonAdapter<T> adapter = moshi.adapter(type);
    return adapter.fromJson(new Buffer().readFrom(inputStream));
  }

  /**
   * Similar to the method above, this method takes in a class type and a filename instead of an
   * InputStream, and returns the serialized version of the file.json as the class type.
   *
   * @param type Type of the deserialized response
   * @param filename JSON file.json to be deserialized
   * @param <T> The same type as the type parameter, which will be returned by the method
   * @return The deserialized response
   * @throws IOException If there is an error reading from the file.json
   */
  public static <T> T getDeserializedResponse(Class<T> type, String filename) throws IOException {
    return getDeserializedResponse(type, new FileInputStream(filename));
  }

  /**
   * This method uses the functionality of getDeserializedResponse to complete a full API query
   * (with deserialized response), unchecked for exceptions.
   *
   * @param url The API request to process
   * @param type The type of deserialized response
   * @param <T> The type of deserialized response
   * @return The deserialized response
   * @throws IOException If there is an issue connecting to the server of the API call
   */
  public static <T> T deserializeQuery(String url, Class<T> type) throws Exception {
    URL requestURL = new URL(url);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.connect();
    return Serializer.getDeserializedResponse(type, clientConnection.getInputStream());
  }
}
