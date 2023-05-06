package edu.brown.cs.student.main.models.formattypes;

import com.squareup.moshi.Json;

import java.util.List;

public record FlatSchedule(@Json(name="days") List<Day> days) {


}
