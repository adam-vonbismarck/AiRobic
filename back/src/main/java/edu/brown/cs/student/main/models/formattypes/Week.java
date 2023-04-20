package edu.brown.cs.student.main.models.formattypes;

import com.squareup.moshi.Json;
import java.util.List;

/**
 * Still ironing out format of these records.
 */
public record Week(@Json(name="type") String type, @Json(name="days") List<Day> days) {

}
