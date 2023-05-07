package edu.brown.cs.student.genericdeserializertesting.types;

import com.squareup.moshi.Json;
import java.util.List;

/**
 * A test record for deserializing Json files.
 *
 * @param name - the name of the student
 * @param age - the age of the student
 * @param classYear - a list of class years for this student
 * @param stars - the student's favorite stars
 */
public record BrownStudent(
    @Json String name,
    @Json List<Double> classYear,
    @Json Double age,
    @Json List<ComplexStar> stars) {

  /**
   * An example computation for this example type.
   *
   * @return the (not mathematically correct) birth year of the student
   */
  public double getYear() {
    double sum = 0;
    for (Double year : classYear) {
      sum += year;
    }
    for (ComplexStar star : stars) {
      sum += star.getDistance();
    }
    return sum - age;
  }
}
