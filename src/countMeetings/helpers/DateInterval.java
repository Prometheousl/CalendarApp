package countMeetings.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Alex Lay
 *
 */
public class DateInterval {
	Date beginDate;
	Date endDate;
	String dayOfTheWeek;
	
	public DateInterval(String begin, String end, String day) {
		beginDate = strToDate(begin);
		endDate = strToDate(end);
		dayOfTheWeek = day;
	}
	
	public void display() {
		System.out.println(beginDate.toString() + " to " + endDate.toString() + ", " + dayOfTheWeek);
	}
	
	public Date strToDate(String str) {
		String pattern = "yyyy-MM-dd";

		DateFormat formatter = new SimpleDateFormat(pattern);
		Date date = new Date();
		try {
			date = formatter.parse(str);
		} catch (ParseException e) {
			System.out.println("Couldn't parse date string.");
			e.printStackTrace();
		}
		return date;
	}
}
