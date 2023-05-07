package edu.brown.cs.student.main.models.formattypes;

import com.squareup.moshi.Json;
import edu.brown.cs.student.main.models.exceptions.InvalidScheduleException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Week record contains a list of Days, used for storage in the Schedule class and use during
 * modelling.
 */
public record Week(@Json(name = "type") String type, @Json(name = "days") List<Day> days) {

  /**
   * This method returns a defensive copy of a subset of the list of Days in a week.
   *
   * @param indexOne - the start index of the subset.
   * @param indexTwo - the end index of the subset.
   * @return the completed sublist.
   */
  public List<Day> getDaySubset(int indexOne, int indexTwo) throws InvalidScheduleException {
    ArrayList<Day> days = new ArrayList<>();
    try {
      for (Day day : this.days.subList(indexOne, indexTwo)) {
        days.add(day.copy());
      }
    } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
      throw new InvalidScheduleException(
          "Unable to get week sublist with overlapping or out of bounds indices.",
          new Schedule("schedule", List.of(this), this));
    }
    return days;
  }
}
