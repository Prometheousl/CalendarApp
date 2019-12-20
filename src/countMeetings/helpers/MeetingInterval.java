package countMeetings.helpers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
	 * Two cases:
	 *   (1) Meeting is the same weekday and in the same interval of time
	 *   (2) Vacation is in the same interval of time
	 * 
	 * https://www.sitepoint.com/implement-javas-equals-method-correctly/
	 */
	@Override
	public boolean equals(Object o) {
		// self check
	    if (this == o)
	        return true;
	    // null check
	    if (o == null)
	        return false;
	    // type check and cast
	    if (getClass() != o.getClass())
	        return false;
	    MeetingInterval meetingInterval = (MeetingInterval) o;
	    // field comparison
	    if(!isDayOfTheWeek(dayOfTheWeek)) { // Vacation
	    	if(meetingInterval.overlaps(this)) {
	    		return true;
	    	}
	    }
	    else if(meetingInterval.dayOfTheWeek == dayOfTheWeek){ // Meeting
	    	if(meetingInterval.overlaps(this)) {
	    		return true;
	    	}
	    }
	    return false;
	}
	

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
	    if(m.beginDate.equals(beginDate) && m.endDate.equals(endDate) && m.dayOfTheWeek == dayOfTheWeek)
	    	return true;
	    return false;
	}
	
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
	
	public MeetingInterval merge(MeetingInterval m) {
		LocalDate newBeginDate = getMinDate(beginDate, m.beginDate);
		LocalDate newEndDate = getMaxDate(endDate, m.endDate);
		// should have same dayOfTheWeek
		return new MeetingInterval(newBeginDate, newEndDate, dayOfTheWeek);
	}
	
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
