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
 * 
 * Resources used: https://stackabuse.com/reading-and-writing-csvs-in-java/
 * 
 * @author Alex Lay
 */
public class CSVReader {
	/**
	 * Stores the Date information contained in the given csv file
	 *   in MeetinInterval objects. Input must be in the format
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
			    // The basic reader accepts only input of format BeginDate, EndDate, Day
			    if(data.length != 3) {
			    	throw new IOException("Invalid input file format.");
			    }
			    meetings.add(new MeetingInterval(sanitizeDate(data[0]), sanitizeDate(data[1]), data[2]));
			}
			csvReader.close();
		}
		catch(FileNotFoundException e) {
			System.out.println("Couldn't find file.");
			e.printStackTrace();
		}
		catch(IOException e) {
			System.out.println("Problem reading from file.");
			e.printStackTrace();
		}
		return meetings;
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
