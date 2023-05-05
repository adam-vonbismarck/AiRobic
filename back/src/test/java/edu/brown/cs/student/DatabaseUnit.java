package edu.brown.cs.student;

import edu.brown.cs.student.main.database.DatabaseCommands;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DatabaseUnit {
  private DatabaseCommands database;

  // Function to reset and set up the database before each test
  @BeforeEach
  public void setUp() throws IOException, InterruptedException {
    this.database = new DatabaseCommands();
    this.database.delete("test");
    Thread.sleep(2000);
  }

  // Empty file.json test
  @Test
  public void testEmpty() throws IOException, InterruptedException {
    String actual = this.database.get("test");
    Assertions.assertEquals("null", actual);
  }

  // Test for adding new users and their information
  @Test
  public void testUpdate() throws IOException, InterruptedException {
    String s =
        "{\"test\":{\"alanisawesome\":{\"birthday\":\"June 23, 1912\",\"name\":\"Alan Turing\"}}}";
    this.database.update(s, "");
    Thread.sleep(2000);
    String expected =
        "{\"alanisawesome\":{\"birthday\":\"June 23, 1912\",\"name\":\"Alan Turing\"}}";
    String actual = this.database.get("test");
    Assertions.assertEquals(expected, actual);
  }

  // Getter testing for specific user information
  @Test
  public void testGet() throws IOException, InterruptedException {
    String s =
        "{\"test\":{\"alanisawesome\":{\"birthday\":\"June 23, 1912\",\"name\":\"Alan Turing\"}}}";
    this.database.update(s, "");
    Thread.sleep(2000);
    String expected1 = "\"June 23, 1912\"";
    String actual1 = this.database.get("test/alanisawesome/birthday");
    Assertions.assertEquals(expected1, actual1);
    String expected2 = "\"Alan Turing\"";
    String actual2 = this.database.get("test/alanisawesome/name");
    Assertions.assertEquals(expected2, actual2);
  }

  // User deletion testing
  @Test
  public void testDelete() throws IOException, InterruptedException {
    String s =
        "{\"test\":{\"alanisawesome\":{\"birthday\":\"June 23, 1912\",\"name\":\"Alan Turing\"}}}";
    this.database.update(s, "");
    Thread.sleep(2000);
    this.database.delete("test");
    Thread.sleep(2000);
    String actual = this.database.get("test");
    Assertions.assertEquals("null", actual);
  }

  // Specific user information deletion testing
  @Test
  public void testDeleteSpecific() throws IOException, InterruptedException {
    String s =
        "{\"test\":{\"alanisawesome\":{\"birthday\":\"June 23, 1912\",\"name\":\"Alan Turing\"}}}";
    this.database.update(s, "");
    Thread.sleep(2000);
    this.database.delete("test/alanisawesome/name");
    Thread.sleep(2000);
    String expected = "{\"alanisawesome\":{\"birthday\":\"June 23, 1912\"}}";
    String actual = this.database.get("test");
    Assertions.assertEquals(expected, actual);
  }

  // Correct input testing, for more users
  @Test
  public void testUpdateMultiple() throws IOException, InterruptedException {
    String s1 =
        "{\"test\":{\"alanisawesome\":{\"birthday\":\"June 23, 1912\",\"name\":\"Alan Turing\"}}}";
    this.database.update(s1, "");
    Thread.sleep(2000);
    String s2 = "{\"alexiscool\":{\"birthday\":\"May 24, 2003\",\"name\":\"Alex Fake\"}}";
    this.database.update(s2, "test");
    Thread.sleep(2000);
    String expected1 = "{\"birthday\":\"June 23, 1912\",\"name\":\"Alan Turing\"}";
    String actual1 = this.database.get("test/alanisawesome");
    Assertions.assertEquals(expected1, actual1);
    String expected2 = "{\"birthday\":\"May 24, 2003\",\"name\":\"Alex Fake\"}";
    String actual2 = this.database.get("test/alexiscool");
    Assertions.assertEquals(expected2, actual2);
  }

  // Incorrect input testing, correct way is above
  @Test
  public void testOverwriting() throws IOException, InterruptedException {
    String s1 =
        "{\"test\":{\"alanisawesome\":{\"birthday\":\"June 23, 1912\",\"name\":\"Alan Turing\"}}}";
    this.database.update(s1, "");
    Thread.sleep(2000);
    String s2 =
        "{\"test\":{\"alexiscool\":{\"birthday\":\"May 24, 2003\",\"name\":\"Alex Fake\"}}}";
    this.database.update(s2, "");
    Thread.sleep(2000);
    // even though the information is different, "update" will overwrite the
    // information if it happens to be in the same folder
    String expected1 = "null";
    String actual1 = this.database.get("test/alanisawesome");
    Assertions.assertEquals(expected1, actual1);
    String expected2 = "{\"birthday\":\"May 24, 2003\",\"name\":\"Alex Fake\"}";
    String actual2 = this.database.get("test/alexiscool");
    Assertions.assertEquals(expected2, actual2);
  }
}
