import java.time.DayOfWeek;

import countMeetings.CountMeetingsFull;
import countMeetings.CountMeetingsMVP;
import countMeetings.helpers.MeetingInterval;

/**
 * A demonstration program for both CountMeetingsMVP and CountMeetingsFull
 * 
 * @author Alex Lay
 */
public class Program {

	public static void main(String[] args) {
		String simpleCsvPath = "src/countMeetings/csv-files/superSimpleTest.csv";
		String regularCsvPath = "src/countMeetings/csv-files/regularTest.csv";
		
		// Get count for one interval
		MeetingInterval interval = new MeetingInterval("2019-12-02", "2020-01-01", "Wednesday");
		interval.display();
		System.out.println(interval.getMeetingCount()); // 5
		
		// get count via MVP (simple csv file)
		CountMeetingsMVP mvp = new CountMeetingsMVP();
		int meetingCount = mvp.countMeetings(simpleCsvPath);
		System.out.println(meetingCount); // 87
		
		// get count via Full (simple csv)
		CountMeetingsFull full = new CountMeetingsFull();
		meetingCount = full.countMeetings(simpleCsvPath);
		System.out.println(meetingCount); // 87
		
		// get count via Full (a little harder csv)
		meetingCount = full.countMeetings(regularCsvPath);
		System.out.println(meetingCount); // 2
	}

}
