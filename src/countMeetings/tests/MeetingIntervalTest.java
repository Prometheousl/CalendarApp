/**
 * 
 */
package countMeetings.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import countMeetings.helpers.MeetingInterval;

/**
 * Test class for {@link countMeetings.helpers.MeetingInterval}
 * 
 * @author Alex Lay
 */
class MeetingIntervalTest {

	/**
	 * Test method for {@link countMeetings.helpers.MeetingInterval#getMeetingCount()}.
	 */
	@Test
	void testGetMeetingCount() {
		// base case
		MeetingInterval meetingInterval = new MeetingInterval("2019-12-02", "2020-01-01", "Wednesday");
		assertEquals(meetingInterval.getMeetingCount(), 5);
		// leap year
		meetingInterval = new MeetingInterval("2020-02-01", "2020-03-01", "Sunday");
		assertEquals(meetingInterval.getMeetingCount(), 5);
	}

	/**
	 * Test method for {@link countMeetings.helpers.MeetingInterval#getFirstDayOfTheWeek()}.
	 */
	@Test
	void testGetFirstDayOfTheWeek() {
		MeetingInterval meetingInterval = new MeetingInterval("2019-12-02", "2020-01-01", "Wednesday");
		// should fast-forward to Wednesday of that week
		LocalDate newBeginDate = LocalDate.parse("2019-12-04");
		assertEquals(meetingInterval.getFirstDayOfTheWeek(), newBeginDate);
	}

	/**
	 * Test method for {@link countMeetings.helpers.MeetingInterval#mapDayOfWeek(java.lang.String)}.
	 * Makes sure all days of the week are mapped correctly
	 */
	@Test
	void testMapDayOfWeek() {
		MeetingInterval meetingInterval = new MeetingInterval();
		String daysOfWeek[] = {"Monday", "Tuesday", "Wednesday",
		  "Thursday", "Friday", "Saturday", "Sunday"};
		
		for (int x = 0; x < daysOfWeek.length; x++) {
			String day = daysOfWeek[x];
			DayOfWeek dayOfWeek = meetingInterval.mapDayOfWeek(day);
			// The value should be 1,2,3,4,5,6,7
			assertEquals(dayOfWeek.getValue(), x + 1);
		}
	}

}
