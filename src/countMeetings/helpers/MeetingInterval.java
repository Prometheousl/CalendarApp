package countMeetings.helpers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;

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
	
	public MeetingInterval(String begin, String end, String day) {
		beginDate = LocalDate.parse(begin);
		endDate = LocalDate.parse(end);
		dayOfTheWeek = mapDayOfWeek(day);
	}
	
	public void display() {
		System.out.println(beginDate.toString() + " to " + endDate.toString() + ", " + dayOfTheWeek);
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
}
