package edu.brown.cs.student;

import edu.brown.cs.student.main.handlers.GenerateGraphLikePlan;
import edu.brown.cs.student.main.models.exceptions.*;
import edu.brown.cs.student.main.models.formattypes.Schedule;
import edu.brown.cs.student.main.models.markov.modelbuilding.Workout;
import edu.brown.cs.student.main.server.serializing.Serializer;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class VariableModelTests {

  @Test
  public void testVariable()
      throws IOException, InvalidScheduleException, InvalidDistributionException,
          FormatterFailureException, NoWorkoutTypeException, InvalidDatesException {
    Schedule schedule =
        new GenerateGraphLikePlan()
            .generate(
                420,
                LocalDate.now().plusDays(2),
                LocalDate.of(2023, 5, 9),
                Set.of(Workout._2K, Workout._6K),
                Set.of(Workout.UT_2),
                0.2);
    System.out.println(schedule);
    System.out.println(Serializer.serializeSchedule(schedule.flatten()));
  }
}
