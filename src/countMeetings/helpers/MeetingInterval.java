package countMeetings.helpers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;

/**
 * Object class to represent intervals of dates
 * 
 * Uses the Java 8 Date API
 * 
 * @author Alex Lay
 */
public class MeetingInterval {
	LocalDate beginDate;
	LocalDate endDate;
	DayOfWeek dayOfTheWeek; // enum of the days of the week
	
	public MeetingInterval() {
		beginDate = null;
		endDate = null;
		dayOfTheWeek = null;
	}
	
	public MeetingInterval(String begin, String end, String day) {
		beginDate = LocalDate.parse(begin);
		endDate = LocalDate.parse(end);
		dayOfTheWeek = mapDayOfWeek(day);
	}
	
	// Represents a Holiday or Vacation
	public MeetingInterval(String begin, String end) {
		beginDate = LocalDate.parse(begin);
		endDate = LocalDate.parse(end);
		dayOfTheWeek = null;
	}
	
	public MeetingInterval(LocalDate begin, LocalDate end, DayOfWeek day) {
		beginDate = begin;
		endDate = end;
		dayOfTheWeek = day;
	}
	
	public void display() {
		if(!isDayOfTheWeek(dayOfTheWeek)) {
			System.out.println("Vacation(s) from " + beginDate.toString() + " to " + endDate.toString());
		}
		else {
			System.out.println("Meeting(s) from " + beginDate.toString() + " to " + endDate.toString() + " every " + dayOfTheWeek);
		}
	}
	
	/**
	 * Computes the number of meetings in this interval.
	 *   eg. "The number of dayOfTheWeek from beginDate to EndDate"
	 * 
	 * @return the number of meetings in this interval
	 */
	public int getMeetingCount() {
		LocalDate newBeginDate = getFirstDayOfTheWeek();
		if(newBeginDate.compareTo(endDate) > 0) return 0;
		// number of days between beginDate (inclusive) and endDate (exclusive)
		long numDaysBetween = ChronoUnit.DAYS.between(newBeginDate, endDate);
		return Math.toIntExact((numDaysBetween / 7) + 1); // + 1 for first dayOfTheWeek
	}
	/**
	 * Fast-forwards from beginDate to the first instance of dayOfTheWeek
	 * Helper func for getMeetingCount
	 * 
	 * @return the LocalDate object corresponding to the above
	 */
	public LocalDate getFirstDayOfTheWeek() {
		DayOfWeek startDayOfTheWeek = beginDate.getDayOfWeek();
		long daysToAdd = dayOfTheWeek.getValue() - startDayOfTheWeek.getValue();
		if(daysToAdd < 0) {
			daysToAdd += 7;
		}
		return beginDate.plusDays(daysToAdd);
	}
	/**
	 * Converts a string of a day of the week to a DayOfWeek enum
	 * 
	 * @param day is a String of a day of the week (eg. "Monday")
	 * @return DayOfWeek enum
	 */
	public DayOfWeek mapDayOfWeek(String day) {
		// https://stackoverflow.com/questions/18232340/convert-string-to-day-of-week-not-exact-date
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE");
        TemporalAccessor accessor = formatter.parse(day);
        return DayOfWeek.from(accessor);
	}
	
	public boolean isDayOfTheWeek(DayOfWeek d) {
		if(d == null) return false;
		else {
			for (DayOfWeek x : DayOfWeek.values()) {
				if(x == d) return true;
			}
			return false;
		}
	}
	
	/**
	 * Checks if two meetingIntervals are equal
	 * 
	 * If both objects have a dayOfTheWeek, checks that they're equal too
	 * 
	 * @param m = the interval to check if equal to this
	 * @return if the two intervals are equal or not
	 */
	public boolean equals(MeetingInterval m) {
		// self check
	    if (this == m)
	        return true;
	    // null check
	    if (m == null)
	        return false;
	    // type check and cast
	    if (getClass() != m.getClass())
	        return false;
	    // field comparison
	    if(m.beginDate.equals(beginDate) && m.endDate.equals(endDate)) {
	    	if(isDayOfTheWeek(m.dayOfTheWeek) && isDayOfTheWeek(dayOfTheWeek)) {
	    		if(m.dayOfTheWeek == dayOfTheWeek)
	    			return true;
	    	}
	    	else
	    		return true;
	    }
	    return false;
	}
	
	/**
	 * Checks if the two intervals overlap or not
	 * 
	 * @param m = the interval to check if it overlaps with this
	 * @return if m overlaps with this or not
	 */
	public boolean overlaps(MeetingInterval m) {
		if(isDayOfTheWeek(m.dayOfTheWeek) && isDayOfTheWeek(dayOfTheWeek)) {
			// comparing two meetings
			if(dayOfTheWeek != m.dayOfTheWeek) return false;
		}
		// check if overlaps
		if(m.beginDate.compareTo(endDate) <= 0 && beginDate.compareTo(m.endDate) <= 0)
			return true;

		return false;
	}
	
	/**
	 * Merges two intervals together...
	 * 
	 * For example merging [2018-01-02, 2018-01-10] and [2018-01-01, 2018-01-09]
	 *   results in [2018-01-01, 2018-01-10]
	 * 
	 * @param m = the interval to be merged
	 * @return the merged interval
	 */
	public MeetingInterval merge(MeetingInterval m) {
		LocalDate newBeginDate = getMinDate(beginDate, m.beginDate);
		LocalDate newEndDate = getMaxDate(endDate, m.endDate);
		// should have same dayOfTheWeek
		return new MeetingInterval(newBeginDate, newEndDate, dayOfTheWeek);
	}
	
	/**
	 * Splits a meeting interval on vacation
	 * 
	 * 4 cases:
	 *   (1) overlaps all of it
	 *   (2) overlaps beginning of interval
	 *   (3) overlaps end of interval
	 *   (4) splits interval in half
	 * 
	 * @param vacation = the interval to split this object on
	 * @return a list of the resulting split interval (size 0,1, or 2)
	 */
	public List<MeetingInterval> split(MeetingInterval vacation) {
		List<MeetingInterval> meetings = new ArrayList<MeetingInterval>();
		// need to exclude the vacation beginning and end when instantiating new meetingIntervals
		LocalDate vacationEndDate = vacation.endDate.plusDays(1);
		LocalDate vacationBeginDate = vacation.beginDate.minusDays(1);
		
		if(vacation.beginDate.compareTo(beginDate) <= 0 &&
			vacation.endDate.compareTo(endDate) >= 0) {
			// vacation covers whole interval
			return meetings;
		}
		else if(vacation.beginDate.compareTo(beginDate) <= 0) {
			// vacation covers beginning half of interval
			MeetingInterval m = new MeetingInterval(vacationEndDate, endDate, dayOfTheWeek);
			meetings.add(m);
			return meetings;
		}
		else if(vacation.endDate.compareTo(endDate) >= 0) {
			// vacation covers end half of interval
			MeetingInterval m = new MeetingInterval(beginDate, vacationBeginDate, dayOfTheWeek);
			meetings.add(m);
			return meetings;
		}
		else {
			// vacation splits interval in half
			MeetingInterval m1 = new MeetingInterval(beginDate, vacationBeginDate, dayOfTheWeek);
			MeetingInterval m2 = new MeetingInterval(vacationEndDate, endDate, dayOfTheWeek);
			meetings.add(m1);
			meetings.add(m2);
			return meetings;
		}
	}
	
	public boolean isSameInterval(MeetingInterval m) {
		if(m.beginDate.compareTo(beginDate) == 0 &&
			m.endDate.compareTo(endDate) == 0) {
			return true;
		}
		return false;
	}
	
	public LocalDate getMinDate(LocalDate date1, LocalDate date2) {
		if(date1.compareTo(date2) <= 0) {
			return date1;
		}
		else {
			return date2;
		}
	}
	
	public LocalDate getMaxDate(LocalDate date1, LocalDate date2) {
		if(date1.compareTo(date2) >= 0) {
			return date1;
		}
		else {
			return date2;
		}
	}
}
