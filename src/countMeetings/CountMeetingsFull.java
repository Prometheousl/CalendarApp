package countMeetings;

import java.time.LocalDate;
import java.util.List;

import countMeetings.helpers.CSVReader;
import countMeetings.helpers.MeetingInterval;
import countMeetings.helpers.MeetingIntervalTree;

/**
 * This is the full version of CountMeetings. It accounts for...
 *   (1) Overlapping Meetings
 *   (2) Holidays and Vacations - default like Christmas?
 *   (3) Singular Meetings
 *   (4) function for haveMeeting(Date) returns true if have a meeting on that day
 * 
 * This implementation should be used when there will be vacations in the input or
 *   if there will possibly be overlapping meetings.
 *   
 * Since it is based off of a Red-Black tree, search, insert, and delete are all O(logN).
 * 
 * @author Alex Lay
 */
public class CountMeetingsFull implements CountMeetings {
	public int countMeetings(String meetingsPath) {
		CSVReader csvReader = new CSVReader();
		List[] csvInfo = csvReader.readMeetingsFull(meetingsPath);
		// csvInfo[0] = an ArrayList of meeting intervals
		// csvInfo[1] = an ArrayList of vacation intervals
		MeetingIntervalTree intervalTree = new MeetingIntervalTree();
		insertMeetings(intervalTree, csvInfo[0]);
		removeVacations(intervalTree, csvInfo[1]);
		
		return countMeetingsInTree(intervalTree);
	}
	
	/**
	 * Counts all of the meetings in the tree
	 * 
	 * O(logN)
	 * 
	 * @param tree = the interval tree
	 * @return the total number of meetings in the tree
	 */
	public Integer countMeetingsInTree(MeetingIntervalTree tree) {
		return tree.countMeetings(tree.getRoot());
	}
	
	/**
	 * Inserts all of the given meetings into the tree
	 * 
	 * O(logN)
	 * 
	 * @param tree = an interval tree
	 * @param meetings = the meetings to insert
	 */
	public void insertMeetings(MeetingIntervalTree tree, List<MeetingInterval> meetings) {
		tree.insertList(meetings);
	}
	
	/**
	 * Removes all of the given vacation intervals from the tree
	 * 
	 * For each vacation...
	 * 		(1) Gets and removes all overlaps from the tree
	 * 		(2) For each vacation...
	 * 				(3) Splits overlap based on vacation
	 * 				(4) Inserts split intervals back into the tree
	 * 
	 * O(logN)
	 * 
	 * @param tree
	 * @param vacations
	 */
	public void removeVacations(MeetingIntervalTree tree, List<MeetingInterval> vacations) {
		for (MeetingInterval vacation : vacations) {
			List<MeetingInterval> overlaps = tree.removeAllOverlaps(vacation);
			// for every overlap, split on vacation and insert back into tree
			for (MeetingInterval overlap : overlaps) {
				List<MeetingInterval> newMeetings = overlap.split(vacation);
				tree.insertList(newMeetings);
			}
		}
	}
	
	/**
	 * This isn't actually needed for this problem.. it's just really easy to implement in 
	 *   case it's needed in the future
	 * 
	 * O(logN)
	 * 
	 * Example of extensibility
	 * 
	 * @param tree = the interval tree
	 * @param date = the date to see if there is a meeting on that day
	 * @return whether or no there is a meeting on that day
	 */
	public boolean haveMeeting(MeetingIntervalTree tree, LocalDate date) {
		MeetingInterval day = new MeetingInterval(date, date, date.getDayOfWeek());
		if(tree.findOverlap(tree.getRoot(), day) != null)
			return true;
		return false;
	}
}
