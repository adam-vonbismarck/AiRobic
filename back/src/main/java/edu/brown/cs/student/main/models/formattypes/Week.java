package edu.brown.cs.student.main.models.formattypes;

import com.squareup.moshi.Json;
import java.util.ArrayList;
import java.util.List;

/** Still ironing out format of these records. */
public record Week(@Json(name = "type") String type, @Json(name = "days") List<Day> days) {

  public List<Day> getDaySubset(int indexOne, int indexTwo) {
    ArrayList<Day> days = new ArrayList<>();
    for (Day day : this.days.subList(indexOne, indexTwo)) {
      days.add(day.copy());
    }
    return days;
  }
}
