package edu.brown.cs.student.main.helpers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DayOfTheWeek {

  public static String getDayOfWeek(String dateStr) {
    // parse date
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    LocalDate date = LocalDate.parse(dateStr, formatter);

    // get day of week
    DayOfWeek dayOfWeek = date.getDayOfWeek();

    // return day of week as string
    return dayOfWeek.toString();
  }
}
