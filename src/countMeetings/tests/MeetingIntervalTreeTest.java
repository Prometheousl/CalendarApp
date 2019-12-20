package countMeetings.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

import countMeetings.helpers.MeetingInterval;
import countMeetings.helpers.MeetingIntervalTree;

class MeetingIntervalTreeTest {

	@Test
	void testInsert() {
		MeetingIntervalTree tree = new MeetingIntervalTree();
		tree.insertList(getTestMeetings1());
		tree.printTree();
	}
	
	@Test
	void testRemove() {
		MeetingIntervalTree tree = new MeetingIntervalTree();
		tree.insertList(getTestMeetings3());
		
		List<MeetingInterval> vacations = new ArrayList<MeetingInterval>();
		MeetingInterval []intervals = {
	        new MeetingInterval("2018-02-02", "2018-02-07", "Friday"),
			new MeetingInterval("2018-02-16", "2018-02-20", "Wednesday"),
			new MeetingInterval("2018-02-10", "2018-02-12", "Tuesday"),
		};
		for(MeetingInterval vacation : intervals) {
			vacations.add(vacation);
		}
		tree.removeList(vacations);
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
	
	List<MeetingInterval> getTestMeetings2() {
		List<MeetingInterval> meetings = new ArrayList<MeetingInterval>();
		MeetingInterval []meetingIntervals = {
                new MeetingInterval("2018-02-13", "2018-02-14", "Saturday"),
                new MeetingInterval("2018-02-10", "2018-02-12", "Saturday"),
                new MeetingInterval("2018-02-16", "2018-02-20", "Saturday"),
                new MeetingInterval("2018-02-10", "2018-02-16", "Saturday"),
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
