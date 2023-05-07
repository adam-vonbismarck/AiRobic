package edu.brown.cs.student.genericdeserializertesting.types;

import com.squareup.moshi.Json;

/**
 * A test record for deserializing Json files.
 *
 * @param x - x coordinate for the star
 * @param y - y coordinate for the star
 * @param z - z coordinate for the star
 * @param speed - speed of the star
 * @param name - name of the star
 */
public record ComplexStar(
    @Json double x, @Json double y, @Json double z, @Json double speed, @Json String name) {

  /**
   * An example computation for this example type.
   *
   * @return the (not mathematically correct) distance of the star.
   */
  public double getDistance() {
    return (x * x + y * y + z + z) / speed;
  }
}
