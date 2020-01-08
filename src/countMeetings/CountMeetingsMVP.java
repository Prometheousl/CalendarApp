package countMeetings;

import java.util.List;

import countMeetings.helpers.CSVReader;
import countMeetings.helpers.MeetingInterval;

/**
 * This is the Minimum Viable Product version of CountMeetings
 * This operates under the assumptions that...
 *   (1) There are no overlapping meetings
 *   (2) There are no holidays or vacations
 *   
 * This class simply aggregates all of the rows in the CSV
 * It should be used when you don't have holidays and you don't
 *   need to worry about overlapping meetings.
 * 
 * @author Alex Lay
 */
public class CountMeetingsMVP implements CountMeetings {
	/**
	 * Counts the total number of meetings in the given csv meetings file
	 * 
	 * O(N)
	 *
	 * @param meetingsPath is the path to the csv meetings file
	 * @return the total number of meetings
	 */
	public int countMeetings(String meetingsPath) {
		CSVReader csvReader = new CSVReader();
		List<MeetingInterval> meetings = csvReader.readMeetingsBasic(meetingsPath);
		Integer total = 0;
		for (MeetingInterval i : meetings) {
			total += i.getMeetingCount();
		}
			
		return total;
	}
}
