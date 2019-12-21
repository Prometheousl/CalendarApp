package countMeetings;

/**
 * This is an interface to count the number of meetings in a csv file.
 * 
 * @author Alex Lay
 */
public interface CountMeetings {
	/**
	 * Given the string to a csv file, this calculates the number of
	 *   meetings in that csv file.
	 * 
	 * @param csvPath = the path to a csv file containing meeting information
	 * @return the number of meetings in the file
	 */
	int countMeetings(String csvPath);
}
