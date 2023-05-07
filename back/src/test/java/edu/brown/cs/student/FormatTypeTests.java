package edu.brown.cs.student;

import edu.brown.cs.student.main.models.exceptions.InvalidScheduleException;
import edu.brown.cs.student.main.models.formattypes.Day;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class FormatTypeTests {

    private Day testDay;

    @BeforeEach
    public void setup() throws InvalidScheduleException {
        this.testDay = new Day("day", new ArrayList<>(), 0,
                DayOfWeek.FRIDAY, Optional.of(LocalDate.of(2023, 5, 5)),
                new ArrayList<>());
    }

    @Test
    public void testDayGetters() {
        Assertions.assertEquals(this.testDay.getDay(), DayOfWeek.FRIDAY);
        Assertions.assertEquals(this.testDay.getDate(), Optional.of(LocalDate.of(2023, 5, 5)));
        Assertions.assertEquals(this.testDay.getEmissions(), new ArrayList<>());
        Assertions.assertEquals(this.testDay.getIntensityLength(), 0);
        Assertions.assertEquals(this.testDay.getNumberOfWorkouts(), 0);
    }


}
