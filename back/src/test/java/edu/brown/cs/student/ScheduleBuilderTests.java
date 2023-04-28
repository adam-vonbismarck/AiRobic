package edu.brown.cs.student;

import edu.brown.cs.student.main.models.ScheduleBuilder;
import org.junit.jupiter.api.Test;

public class ScheduleBuilderTests {

  @Test
  public void testScheduleBuilderMedium() {
    System.out.println(new ScheduleBuilder().minutes(1200, 4, 0.2,
        "Tuesday", "Friday", "2k", "UT2"));
  }

}
