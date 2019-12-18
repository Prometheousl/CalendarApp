package countMeetings;

import java.util.List;

import countMeetings.helpers.CSVReader;
import countMeetings.helpers.MeetingInterval;

/**
 * This is the full version of CountMeetings. It accounts for...
 *   (1) Overlapping Meetings
 *   (2) Holidays and Vacations
 *   (3) Singular Meetings
 *   (4) Multiple Days
 *   (5) ??? Every other Tuesday ???? -- stuff like this
 *   (6) function for haveMeeting(Date) returns true if have a meeting on that day
 * 
 * This implementation should be used when...
 * 
 * @author Alex Lay
 */
public class CountMeetingsFull implements CountMeetings {
	public int countMeetings(String meetingsPath) {
//		CSVReader csvReader = new CSVReader();
//		List<MeetingInterval> meetings = csvReader.readMeetingsBasic(meetingsPath);
		Integer total = 0;
//		for (MeetingInterval i : meetings) {
//			total += i.getMeetingCount();
//		}
			
		return total;
	}
}
