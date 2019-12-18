/**
 * 
 */
package countMeetings.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import countMeetings.helpers.CSVReader;
import countMeetings.helpers.MeetingInterval;

/**
 * @author alexl
 *
 */
class CSVReaderTest {
	
	/**
	 * Test method for {@link countMeetings.helpers.CSVReader#readMeetingsBasic(java.lang.String)}.
	 */
	@Test
	void testReadMeetingsBasic() {
		CSVReader csvReader = new CSVReader();
		shouldThrowFileNotFoundException(csvReader);
		shouldThrowIOException(csvReader);
	}
	
	void shouldThrowFileNotFoundException(CSVReader csvReader) {
		String csvPath = "/Not/a/correct/path";

        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(outContent));
	    csvReader.readMeetingsBasic(csvPath);
	    assertEquals("Couldn't find file.", outContent.toString());
	    
	}
	
	void shouldThrowIOException(CSVReader csvReader) {
		String csvPath = "src/countMeetings/csv-files/badInput.csv";

        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(outContent));
	    csvReader.readMeetingsBasic(csvPath);
	    assertEquals("Problem reading from file.", outContent.toString());
	}
}
