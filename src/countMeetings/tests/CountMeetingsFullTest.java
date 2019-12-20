package countMeetings.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

import countMeetings.CountMeetingsFull;
import countMeetings.helpers.MeetingInterval;
import countMeetings.helpers.MeetingIntervalComparator;
import countMeetings.helpers.MeetingIntervalTree;

class CountMeetingsFullTest {

	@Test
	void testCountMeetings() {
		
	}
	/**
	 * Tests a basic insert that merges two intervals
	 * 
	 * @param tree
	 */
	@Test
	void basicInsertMeetingsTest() {
		MeetingIntervalComparator comparator = new MeetingIntervalComparator();
		TreeMap tree = new TreeMap(comparator); // a Red-Black tree
		MeetingInterval m1 = new MeetingInterval("2018-02-01", "2018-02-05", "Wednesday");
		MeetingInterval m2 = new MeetingInterval("2018-02-05", "2018-02-07", "Wednesday");
		List<MeetingInterval> meetings = new ArrayList<MeetingInterval>();
		meetings.add(m1); meetings.add(m2);
		CountMeetingsFull countMeetings = new CountMeetingsFull();
		tree = countMeetings.insertMeetings(meetings, tree);
		// result should merge two into one meeting
		MeetingInterval result = new MeetingInterval("2018-02-01", "2018-02-07", "Wednesday");
		assertEquals(result, tree.values().toArray()[0]);
	}
	@Test
	void insertMultipleMeetingsTest() {
		MeetingIntervalComparator comparator = new MeetingIntervalComparator();
		TreeMap tree = new TreeMap(comparator); // a Red-Black tree
		MeetingInterval m1 = new MeetingInterval("2018-02-13", "2018-02-14", "Wednesday");
		MeetingInterval m2 = new MeetingInterval("2018-02-10", "2018-02-12", "Tuesday");
		MeetingInterval m3 = new MeetingInterval("2018-02-16", "2018-02-20", "Thursday");
		MeetingInterval m4 = new MeetingInterval("2018-02-10", "2018-02-16", "Friday");
		MeetingInterval m5 = new MeetingInterval("2018-02-02", "2018-02-07", "Saturday");
		List<MeetingInterval> meetings = new ArrayList<MeetingInterval>();
		meetings.add(m1); meetings.add(m2);
		meetings.add(m3); meetings.add(m4); meetings.add(m5);
		CountMeetingsFull countMeetings = new CountMeetingsFull();
		tree = countMeetings.insertMeetings(meetings, tree);
	}
	/**
	 * Tests a more advanced insert that causes a recursive merge
	 * 
	 * @param tree
	 */
	@Test
	void advancedInsertMeetingsTest() {
		MeetingIntervalComparator comparator = new MeetingIntervalComparator();
		TreeMap tree = new TreeMap(comparator); // a Red-Black tree
		MeetingInterval m1 = new MeetingInterval("2018-02-13", "2018-02-15", "Wednesday");
		MeetingInterval m2 = new MeetingInterval("2018-02-11", "2018-02-12", "Wednesday");
		MeetingInterval m3 = new MeetingInterval("2018-02-16", "2018-02-17", "Wednesday");
		MeetingInterval m4 = new MeetingInterval("2018-02-10", "2018-02-16", "Wednesday");
		MeetingInterval m5 = new MeetingInterval("2018-02-02", "2018-02-07", "Wednesday");

		List<MeetingInterval> meetings = new ArrayList<MeetingInterval>();
		meetings.add(m1); meetings.add(m2);
		meetings.add(m3); meetings.add(m4);
		meetings.add(m5); 
		CountMeetingsFull countMeetings = new CountMeetingsFull();
		tree = countMeetings.insertMeetings(meetings, tree);
		// result should merge until there are 2 meetings left
		MeetingInterval result1 = new MeetingInterval("2018-02-02", "2018-02-07", "Wednesday");
		MeetingInterval result2 = new MeetingInterval("2018-02-10", "2018-02-17", "Wednesday");

		assertEquals(result1, tree.values().toArray()[0]);
		assertEquals(result2, tree.values().toArray()[1]);
	}
	
	@Test
	void testRemoveVacations() {
		MeetingIntervalTree tree = new MeetingIntervalTree();
		tree.insertList(getTestMeetings3());
		
		List<MeetingInterval> vacations = new ArrayList<MeetingInterval>();
		MeetingInterval []intervals = {
	        new MeetingInterval("2018-02-02", "2018-02-07"),
			new MeetingInterval("2018-02-16", "2018-02-18"),
			new MeetingInterval("2018-02-10", "2018-02-13"),
		};
		for(MeetingInterval vacation : intervals) {
			vacations.add(vacation);
		}
		tree.removeList(vacations);
		tree.printTree();
	}
	
	List<MeetingInterval> getTestMeetings3() {
		List<MeetingInterval> meetings = new ArrayList<MeetingInterval>();
		MeetingInterval []meetingIntervals = {
                new MeetingInterval("2018-02-13", "2018-02-14", "Monday"),
                new MeetingInterval("2018-02-10", "2018-02-12", "Tuesday"),
                new MeetingInterval("2018-02-16", "2018-02-20", "Wednesday"),
                new MeetingInterval("2018-02-10", "2018-02-16", "Thursday"),
                new MeetingInterval("2018-02-02", "2018-02-07", "Friday"),
		};
		for(MeetingInterval m : meetingIntervals) {
			meetings.add(m);
		}
		return meetings;
	}
	
	
	
	@Test
	void basicRemoveVacationsTest() {
		MeetingIntervalComparator comparator = new MeetingIntervalComparator();
		TreeMap tree = new TreeMap(comparator); // a Red-Black tree
		MeetingInterval m = new MeetingInterval("2018-02-01", "2018-02-16", "Wednesday");
		MeetingInterval v = new MeetingInterval("2018-02-05", "2018-02-10");

		List<MeetingInterval> meetings = new ArrayList<MeetingInterval>();
		List<MeetingInterval> vacations = new ArrayList<MeetingInterval>();
		meetings.add(m);
		vacations.add(v);
		CountMeetingsFull countMeetings = new CountMeetingsFull();
		tree = countMeetings.insertMeetings(meetings, tree);
		tree = countMeetings.removeVacations(vacations, tree);
		MeetingInterval result1 = new MeetingInterval("2018-02-02", "2018-02-04", "Wednesday");
		MeetingInterval result2 = new MeetingInterval("2018-02-11", "2018-02-16", "Wednesday");

		assertEquals(result1, tree.values().toArray()[0]);
		assertEquals(result2, tree.values().toArray()[1]);
	}
	@Test
	void advancedRemoveVacationsTest() {
		MeetingIntervalComparator comparator = new MeetingIntervalComparator();
		TreeMap tree = new TreeMap(comparator); // a Red-Black tree
		MeetingInterval m1 = new MeetingInterval("2018-02-13", "2018-02-14", "Wednesday");
		MeetingInterval m2 = new MeetingInterval("2018-02-10", "2018-02-12", "Tuesday");
		MeetingInterval m3 = new MeetingInterval("2018-02-16", "2018-02-20", "Thursday");
		MeetingInterval m4 = new MeetingInterval("2018-02-10", "2018-02-16", "Friday");
		MeetingInterval m5 = new MeetingInterval("2018-02-02", "2018-02-07", "Saturday");

		// whole interval
		MeetingInterval v1 = new MeetingInterval("2018-02-02", "2018-02-07");
		// partial interval
		MeetingInterval v2 = new MeetingInterval("2018-02-16", "2018-02-18");
		MeetingInterval v3 = new MeetingInterval("2018-02-10", "2018-02-15");

		
		List<MeetingInterval> meetings = new ArrayList<MeetingInterval>();
		List<MeetingInterval> vacations = new ArrayList<MeetingInterval>();
		meetings.add(m1); 
		meetings.add(m2);
		meetings.add(m3); meetings.add(m4);
		meetings.add(m5); 
		vacations.add(v1); vacations.add(v2); vacations.add(v3);
		CountMeetingsFull countMeetings = new CountMeetingsFull();
		tree = countMeetings.insertMeetings(meetings, tree);
		tree = countMeetings.removeVacations(vacations, tree);
		//countMeetings.printTree(tree);
		// result should merge until there are 2 meetings left
//		MeetingInterval result1 = new MeetingInterval("2018-02-02", "2018-02-07", "Wednesday");
//		MeetingInterval result2 = new MeetingInterval("2018-02-10", "2018-02-17", "Wednesday");
//
//		assertEquals(result1, tree.values().toArray()[0]);
//		assertEquals(result2, tree.values().toArray()[1]);
	}
	@Test
	void shouldShortenMeetingInterval() {
		MeetingIntervalComparator comparator = new MeetingIntervalComparator();
		TreeMap tree = new TreeMap(comparator); // a Red-Black tree
		MeetingInterval m = new MeetingInterval("2018-02-10", "2018-02-16", "Friday");
		MeetingInterval v = new MeetingInterval("2018-02-16", "2018-02-18");
		
		List<MeetingInterval> meetings = new ArrayList<MeetingInterval>();
		List<MeetingInterval> vacations = new ArrayList<MeetingInterval>();
		meetings.add(m);
		vacations.add(v);
		CountMeetingsFull countMeetings = new CountMeetingsFull();
		tree = countMeetings.insertMeetings(meetings, tree);
		tree = countMeetings.removeVacations(vacations, tree);
		MeetingInterval result = new MeetingInterval("2018-02-10", "2018-02-15", "Friday");
		assertEquals(result, tree.values().toArray()[0]);
	}
	@Test
	void shouldShortenBothMeetingIntervals() {
		MeetingIntervalComparator comparator = new MeetingIntervalComparator();
		TreeMap tree = new TreeMap(comparator); // a Red-Black tree
		MeetingInterval m = new MeetingInterval("2018-02-10", "2018-02-16", "Friday");
		MeetingInterval m2 = new MeetingInterval("2018-02-16", "2018-02-20", "Thursday");
		MeetingInterval v = new MeetingInterval("2018-02-16", "2018-02-18");
		
		List<MeetingInterval> meetings = new ArrayList<MeetingInterval>();
		List<MeetingInterval> vacations = new ArrayList<MeetingInterval>();
		meetings.add(m);
		meetings.add(m2);
		vacations.add(v);
		CountMeetingsFull countMeetings = new CountMeetingsFull();
		tree = countMeetings.insertMeetings(meetings, tree);
		tree = countMeetings.removeVacations(vacations, tree);
		MeetingInterval result = new MeetingInterval("2018-02-10", "2018-02-15", "Friday");
		assertEquals(result, tree.values().toArray()[0]);
	}

}
