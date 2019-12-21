/**
 * 
 */
package countMeetings.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Reads the data from a csv and returns it.
 * Two implemenations for MVP (1) and Full (2):
 *   (1) reads meetings in the format beginDate, endDate, dayOfTheWeek
 *         and returns a list of that
 *   (2) reads meetings in the format beginDate, endDate, dayOfTheWeek
 *         and reads vacations in the format beginDate, endDate, ["Vacation" | "Holiday]
 *         and returns both lists
 * 
 * Resources used: https://stackabuse.com/reading-and-writing-csvs-in-java/
 * 
 * @author Alex Lay
 */
public class CSVReader {
	/**
	 * Stores the Date information contained in the given csv file
	 *   in MeetingInterval objects. Input must be in the format
	 *   beginDate, EndDate, DayOfWeek in the csv file.
	 * 
	 * @param csvPath is the path to a csv file
	 * @return a list of MeetingInterval objects 
	 *     (DateTime start, DateTime end, DayOfTheWeek w)
	 */
	public List<MeetingInterval> readMeetingsBasic(String csvPath) {
		List<MeetingInterval> meetings = new ArrayList<MeetingInterval>();
		
		try {
			File csvFile = new File(csvPath);
			BufferedReader csvReader = new BufferedReader(new FileReader(csvFile));
			String row;
			while ((row = csvReader.readLine()) != null) {
			    String[] data = row.split(",");
			    if(data.length != 3) {
			    	throw new IOException("Invalid input file format.");
			    }
			    meetings.add(new MeetingInterval(sanitizeDate(data[0]), sanitizeDate(data[1]), data[2]));
			}
			csvReader.close();
		}
		catch(FileNotFoundException e) {
			System.out.print("Couldn't find file.");
			e.printStackTrace();
		}
		catch(IOException e) {
			System.out.print("Problem reading from file.");
			e.printStackTrace();
		}
		return meetings;
	}
	
	/**
	 * Stores the Date information contained in the given csv file in MeetingInterval objects.
	 * The full reader accepts input of format BeginDate, EndDate, Day
	 *		    	                       and BeginDate, EndDate, { "Holiday" | "Vacation" }
	 * 
	 * Note: This duplicates code... I thought about adding a isFull boolean to the previous
	 *     function but I decided to keep them separate for readability. I could be persuaded
	 *     to combine them, though.
	 * 
	 * https://stackoverflow.com/questions/12947659/how-can-i-return-2-arraylist-from-same-method
	 * 
	 * @param csvPath is the path to a csv file containing meeting information
	 * @return two arraylists, one with meetings and the other with vacations/holidays
	 */
	public List[] readMeetingsFull(String csvPath) {
		List<MeetingInterval> meetings = new ArrayList<MeetingInterval>();
		List<MeetingInterval> vacations = new ArrayList<MeetingInterval>();
		
		try {
			File csvFile = new File(csvPath);
			BufferedReader csvReader = new BufferedReader(new FileReader(csvFile));
			String row;
			while ((row = csvReader.readLine()) != null) {
			    String[] data = row.split(",");
			    if(data.length != 3) {
			    	throw new IOException("Invalid input file format.");
			    }
			    if(data[2].toLowerCase().equals("holiday") || data[2].toLowerCase().equals("vacation")) {
			    	vacations.add(new MeetingInterval(sanitizeDate(data[0]), sanitizeDate(data[1])));
			    }
			    else {
			    	meetings.add(new MeetingInterval(sanitizeDate(data[0]), sanitizeDate(data[1]), data[2]));
			    }
			}
			csvReader.close();
		}
		catch(FileNotFoundException e) {
			System.out.print("Couldn't find file.");
			e.printStackTrace();
		}
		catch(IOException e) {
			System.out.print("Problem reading from file.");
			e.printStackTrace();
		}
		
		// I wish Java could return two variables..
		return new List[] { meetings, vacations };
	}
	
	/**
	 * Regex to remove non-integers and non-hyphens from a given string
	 * My CSV files were giving me weird special characters so I needed to remove them...
	 * 
	 * @param date is the date to be sanitized
	 * @return a date of format 2018-05-02
	 */
	public String sanitizeDate(String date) {
		return date.replaceAll("[^0-9._-]", "");
	}
}
