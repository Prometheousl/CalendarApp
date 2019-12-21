package countMeetings.helpers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.time.DayOfWeek;

/**
 * Represents a node in the interval tree (augmented Red-Black tree)
 * 
 * Like a normal Red-black tree node except...
 * We need to keep track of the max date in each node for the interval
 *   findOverlap algorithm. Additionally, we need to keep track of the max
 *   date for each weekday so the search algorithm can find all overlapping
 *   intervals for each weekday.
 * 
 * @author Alex Lay
 */
public class MeetingIntervalNode {
	// for the interest of saving time I just made these public
	// ideally would be private and have get and set methods
	MeetingInterval value;
	Map<DayOfWeek, LocalDate> dayMax; // keep track of each day's max
	LocalDate max;
	MeetingIntervalNode parent, left, right;
	char color;
	
	// root has parent node null
	public MeetingIntervalNode(MeetingInterval v, MeetingIntervalNode p) {
		value = v;
		max = v.endDate;
		dayMax = new HashMap<DayOfWeek, LocalDate>();
		dayMax.put(v.dayOfTheWeek, max);
		parent = p;
		left = null;
		right = null;
		color = 'R';
	}
	
	/**
	 * Recursively recalculates the max value based on itself, its left child, and its right child
	 * Also recalculates the dayMaxes
	 * 
	 * You can imagine this recalculating the variables up the tree 
	 *   after a rotation, insert, or delete
	 */
	public void recalculateMax() {
		DayOfWeek currentDay = this.value.dayOfTheWeek;
		// recalc regular max
		updateMax();
		// recalc dayMaxes
		this.dayMax = updateDayMax();
		
		// recursively update max up the tree
		if(this.parent != null) {
			parent.recalculateMax();
		}
	}
	
	/**
	 * Updates the regular max
	 * The regular max is used when we don't care about days of the week
	 *   (i.e. vacations or holidays)
	 */
	public void updateMax() {
		LocalDate maxRight = null, maxLeft = null, maxLeftRight = null;
		if(this.right != null) {
			if(this.right.max.compareTo(this.max) > 0)
				maxRight = right.max;
		}
		
		if(this.left != null) {
			if(this.left.max.compareTo(this.max) > 0)
				maxLeft = left.max;
		}
		if(maxRight != null && maxLeft != null) {
			maxLeftRight = maxRight.isAfter(maxLeft) ? maxRight : maxLeft;
			this.max = maxLeftRight.isAfter(this.value.endDate) ? maxLeftRight : this.value.endDate; 
		}
		else if(maxRight != null)
			this.max = maxRight.isAfter(this.value.endDate) ? maxRight : this.value.endDate;
		else if(maxLeft != null)
			this.max = maxLeft.isAfter(this.value.endDate) ? maxLeft : this.value.endDate; 
		else
			this.max = this.value.endDate;
	}
	
	/**
	 * Updates the dayMaxes
	 * 
	 * This map is used when we care about the days of the week
	 * It allows MeetingIntervalTree.findOverlap to find all of the intervals
	 *   that overlap with the given date and have the same day of the week
	 * 
	 * @return a map of the new dayMaxes
	 */
	public Map<DayOfWeek, LocalDate> updateDayMax() {
		Map<DayOfWeek, LocalDate> newDayMax = new HashMap<DayOfWeek, LocalDate>();

		if(this.right != null)
			newDayMax.putAll(this.right.dayMax);
		if(this.left != null)
			newDayMax.putAll(this.left.dayMax);
		
		if(newDayMax.containsKey(this.value.dayOfTheWeek) && newDayMax.get(this.value.dayOfTheWeek).compareTo(this.max) < 0) {
			newDayMax.put(this.value.dayOfTheWeek, this.value.endDate);
		}
		else if(!newDayMax.containsKey(this.value.dayOfTheWeek))
			newDayMax.put(this.value.dayOfTheWeek, this.value.endDate);
		return newDayMax;
	}
	
	// ************** get and set functions **********************
	
	public MeetingInterval getValue() {
		return this.value;
	}
	
	public void setValue(MeetingInterval m) {
		this.value = m;
	}
	
	public Map<DayOfWeek, LocalDate> getDayMax() {
		return this.dayMax;
	}
	
	public void setDayMax(Map<DayOfWeek, LocalDate> d) {
		this.dayMax = d;
	}
	
	public LocalDate getMax() {
		return this.max;
	}
	
	public void setMax(LocalDate d) {
		max = d;
	}
	
	public MeetingIntervalNode getParent() {
		return this.parent;
	}
	
	public void setParent(MeetingIntervalNode n) {
		this.parent = n;
	}
	
	public MeetingIntervalNode getLeft() {
		return this.left;
	}
	
	public void setLeft(MeetingIntervalNode n) {
		this.left = n;
	}
	
	public MeetingIntervalNode getRight() {
		return this.right;
	}
	
	public void setRight(MeetingIntervalNode n) {
		this.right = n;
	}
	
	public char getColor() {
		return this.color;
	}
	
	public void setColor(char c) {
		this.color = c;
	}
	
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
	    MeetingIntervalNode meetingInterval = (MeetingIntervalNode) o;
	    // field comparison
	    if(meetingInterval.value.equals(this.value))
	    	return true;
	    else
	    	return false;
	}
	
	public void display() {
		System.out.println("Node {");
		value.display();
		if(color == 'R')
			System.out.println("\tRed");
		else
			System.out.println("\tBlack");
		System.out.println("\tMax: " + max);
		System.out.println("\tdayMaxes: " + dayMax);
		System.out.print("\tParent - ");
		if(parent != null) parent.value.display();
		else System.out.println("Null");
		System.out.print("\tLeft - ");
		if(left != null) left.value.display();
		else System.out.println("Null");
		System.out.print("\tRight - ");
		if(right != null) right.value.display();
		else System.out.println("Null");
		System.out.println("}");
	}
}
