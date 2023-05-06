package edu.brown.cs.student.main.models.formattypes;

import com.squareup.moshi.Json;

import java.util.List;

/**
 * This record represents a flattened version of a Schedule, for more simple storage in firebase.
 *
 * @param days - the list of all Days in a given Schedule, when generated using the flatten method.
 */
public record FlatSchedule(List<Day> days) {

}
