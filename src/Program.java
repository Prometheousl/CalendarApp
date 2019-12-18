import java.time.DayOfWeek;

import countMeetings.CountMeetingsMVP;
import countMeetings.helpers.MeetingInterval;

public class Program {

	public static void main(String[] args) {
		String csvPath = "C:/Users/Alex/Documents/Interviews/raytheon-practice/CalendarApp/src/countMeetings/csv-files/superSimpleTest.csv";
		
		MeetingInterval interval = new MeetingInterval("2019-12-02", "2020-01-01", "Wednesday");
		interval.display();
		System.out.println(interval.getMeetingCount());
		
		CountMeetingsMVP mvp = new CountMeetingsMVP();
		int meetingCount = mvp.countMeetings(csvPath);
		System.out.println(meetingCount);
	}

}
