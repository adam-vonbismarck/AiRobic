package edu.brown.cs.student.main.helpers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DateListCreator {

  public static List<String> getDatesBetween(String startDateStr, String endDateStr) {
    List<String> dates = new ArrayList<>();

    // parse start and end dates
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    LocalDate startDate = LocalDate.parse(startDateStr, formatter);
    LocalDate endDate = LocalDate.parse(endDateStr, formatter);

    // add all dates between start and end dates to list
    while (!startDate.isAfter(endDate)) {
      String dateStr = startDate.format(formatter);
      dates.add(dateStr);
      startDate = startDate.plusDays(1);
    }

    return dates;
  }
}
