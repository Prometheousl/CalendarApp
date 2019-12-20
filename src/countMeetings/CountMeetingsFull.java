package countMeetings;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import countMeetings.helpers.CSVReader;
import countMeetings.helpers.MeetingInterval;
import countMeetings.helpers.MeetingIntervalComparator;
import countMeetings.helpers.MeetingIntervalTree;

/**
 * This is the full version of CountMeetings. It accounts for...
 *   (1) Overlapping Meetings
 *   (2) Holidays and Vacations - default like Christmas?
 *   (3) Singular Meetings
 *   (4) function for haveMeeting(Date) returns true if have a meeting on that day
 * 
 * This implementation should be used when...
 * 
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
	
	public Integer countMeetingsInTree(MeetingIntervalTree tree) {
		Integer total = 0;
		return tree.countMeetings(tree.getRoot(), total);
	}
	
	public void insertMeetings(MeetingIntervalTree tree, List<MeetingInterval> meetings) {
		tree.insertList(meetings);
	}
	
	public void removeVacations(MeetingIntervalTree tree, List<MeetingInterval> vacations) {
		for (MeetingInterval vacation : vacations) {
			System.out.print("Vacation is: "); vacation.display();
			List<MeetingInterval> overlaps = tree.removeAllOverlaps(vacation);
			// for every overlap, split on vacation and insert back into tree
			if(!overlaps.isEmpty()) System.out.println("Overlaps:");
			for (MeetingInterval overlap : overlaps) {
				overlap.display();
				List<MeetingInterval> newMeetings = overlap.split(vacation);
				if(!newMeetings.isEmpty()) {
					for (MeetingInterval newMeeting : newMeetings) {
						tree.printTree();
						System.out.print("New Meeting: "); newMeeting.display();
					}
				}
				tree.insertList(newMeetings);
			}
		}
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public boolean haveMeeting(MeetingIntervalTree tree, LocalDate date) {
		MeetingInterval day = new MeetingInterval(date, date, date.getDayOfWeek());
		if(tree.findOverlap(tree.getRoot(), day) != null)
			return true;
		return false;
	}
}
