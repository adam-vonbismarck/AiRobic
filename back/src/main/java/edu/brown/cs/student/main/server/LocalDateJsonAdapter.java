package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;

/** A JsonAdapter for the LocalDate class, which we use to store dates in each Day object. */
public class LocalDateJsonAdapter extends JsonAdapter<Optional<LocalDate>> {

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

  /**
   * This method defines how to read a date from a Json file.json.
   *
   * @param jsonReader - the reader which reads said Json file.json.
   * @return the deserialized LocalDate.
   * @throws IOException if the reader errors or if the field is empty.
   */
  @Nullable
  @Override
  public Optional<LocalDate> fromJson(JsonReader jsonReader) throws IOException {
    if (!jsonReader.hasNext()) {
      return Optional.empty();
    }
    String date = jsonReader.nextString();

    // expected behavior, given the field
    if (date.equals("null")) {
      return Optional.empty();
    }

    return Optional.of(LocalDate.parse(date, this.formatter));
  }

  /**
   * The toJson method defines how a LocalDate should be written into a Json file.json.
   *
   * @param jsonWriter - a reference to where this Json should be written.
   * @param localDate - the date to write to this reference.
   * @throws IOException if there is an issue with the JsonWriter.
   */
  @Override
  public void toJson(JsonWriter jsonWriter, @Nullable Optional<LocalDate> localDate)
      throws IOException {
    if (localDate == null || localDate.isEmpty()) {
      jsonWriter.value("null");
    } else {
      jsonWriter.value(this.formatter.format(localDate.get()));
    }
  }
}
