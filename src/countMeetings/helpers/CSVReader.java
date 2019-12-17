/**
 * 
 */
package countMeetings.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Resources used: https://stackabuse.com/reading-and-writing-csvs-in-java/
 * 
 * @author Alex Lay
 */
public class CSVReader {
	public List<DateInterval> readMeetingsBasic(String csvPath) {
		List<DateInterval> meetings = new ArrayList<DateInterval>();
		
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
			    meetings.add(new DateInterval(data[0], data[1], data[2]));
			}
			csvReader.close();
		}
		catch(IOException e) {
			System.out.print("Problem reading from file.");
			e.printStackTrace();
		}
		return meetings;
	}
}
