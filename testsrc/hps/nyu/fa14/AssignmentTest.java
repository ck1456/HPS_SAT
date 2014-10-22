package hps.nyu.fa14;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class AssignmentTest {

  @Test
  public void testWrite() throws IOException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();

    Assignment a = new Assignment(10);
    a.write(output);
    // Get the string and compare it to

    assertEquals("-1 -2 -3 -4 -5 -6 -7 -8 -9 -10 0", output.toString());
  }

}
