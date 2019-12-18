/**
 * 
 */
package countMeetings.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import countMeetings.CountMeetingsMVP;

/**
 * @author alexl
 *
 */
class CountMeetingsMVPTest {

	/**
	 * Test method for {@link countMeetings.CountMeetingsMVP#countMeetings(java.lang.String)}.
	 */
	@Test
	void testCountMeetings() {
		String csvPath = "src/countMeetings/csv-files/simpleTest.csv";
		
		CountMeetingsMVP mvp = new CountMeetingsMVP();
		assertEquals(mvp.countMeetings(csvPath), 87);
	}

}
