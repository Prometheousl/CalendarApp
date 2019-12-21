package countMeetings.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import countMeetings.CountMeetingsFull;
import countMeetings.helpers.MeetingInterval;
import countMeetings.helpers.MeetingIntervalTree;

class CountMeetingsFullTest {

	@Test
	void testCountMeetings() {
		CountMeetingsFull countMeetingsFull = new CountMeetingsFull();
		int count = countMeetingsFull.countMeetings("src/countMeetings/csv-files/regularTest.csv");
		assertEquals(2, count);
		count = countMeetingsFull.countMeetings("src/countMeetings/csv-files/superSimpleTest.csv");
		assertEquals(87, count);
	}
	
	@Test
	void testInsertMeetings() {
		CountMeetingsFull countMeetingsFull = new CountMeetingsFull();
		MeetingIntervalTree tree = new MeetingIntervalTree();
		countMeetingsFull.insertMeetings(tree, getTestMeetings1());
		tree.printTree();
	}
	
	@Test
	void testRemoveVacations() {
		CountMeetingsFull countMeetingsFull = new CountMeetingsFull();
		MeetingIntervalTree tree = new MeetingIntervalTree();
		countMeetingsFull.insertMeetings(tree, getTestMeetings3());
		
		List<MeetingInterval> vacations = new ArrayList<MeetingInterval>();
		MeetingInterval []intervals = {
	        new MeetingInterval("2018-02-02", "2018-02-07"),
			new MeetingInterval("2018-02-16", "2018-02-18"),
			new MeetingInterval("2018-02-10", "2018-02-13"),
		};
		for(MeetingInterval vacation : intervals) {
			vacations.add(vacation);
		}
		countMeetingsFull.removeVacations(tree, vacations);
		tree.printTree();
	}
	
	List<MeetingInterval> getTestMeetings1() {
		List<MeetingInterval> meetings = new ArrayList<MeetingInterval>();
		MeetingInterval []meetingIntervals = {
                new MeetingInterval("2018-02-13", "2018-02-14", "Wednesday"),
                new MeetingInterval("2018-02-10", "2018-02-12", "Tuesday"),
                new MeetingInterval("2018-02-16", "2018-02-20", "Thursday"),
                new MeetingInterval("2018-02-10", "2018-02-16", "Friday"),
                new MeetingInterval("2018-02-02", "2018-02-07", "Saturday"),
                new MeetingInterval("2018-02-01", "2018-02-06", "Saturday"),
        		new MeetingInterval("2018-02-16", "2018-02-18", "Saturday"),
        		new MeetingInterval("2018-01-25", "2018-02-19", "Saturday"),
		};
		for(MeetingInterval m : meetingIntervals) {
			meetings.add(m);
		}
		return meetings;
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

}
