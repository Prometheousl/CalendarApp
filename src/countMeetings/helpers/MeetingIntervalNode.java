package countMeetings.helpers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.time.DayOfWeek;

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
//		// fix based on right child
//		if(this.right != null) {
//			DayOfWeek rightDayOfWeek = this.right.value.dayOfTheWeek;
//			
//			if(!this.dayMax.containsKey(rightDayOfWeek) ||
//				 this.dayMax.get(rightDayOfWeek).compareTo(this.right.dayMax.get(rightDayOfWeek)) < 0) {
//				this.dayMax.put(rightDayOfWeek, this.right.dayMax.get(rightDayOfWeek));
//			}
//		}
//		// fix based on left child
//		if(this.left != null) {
//			DayOfWeek leftDayOfWeek = this.left.value.dayOfTheWeek;
//			
//			if(!this.dayMax.containsKey(leftDayOfWeek) ||
//				 this.dayMax.get(leftDayOfWeek).compareTo(this.left.dayMax.get(leftDayOfWeek)) < 0) {
//				this.dayMax.put(leftDayOfWeek, this.left.dayMax.get(leftDayOfWeek));
//			}
//		}
//		if(this.dayMax.get(this.value.dayOfTheWeek).compareTo(this.max) < 0)
//			this.dayMax.put(this.value.dayOfTheWeek, this.max);
	}
	
	public boolean isLinear() {
		if(this.isLeftChild() && !parent.isLeftChild())
			return false;
		if(this.isRightChild() && !parent.isRightChild())
			return false;
		return true;
	}
	
	public char color() {
		if(this.value == null) return 'B';
		else return this.color;
	}
	
	public void swap(MeetingIntervalNode other) {
		MeetingInterval temp = this.value;
		this.value = other.value;
		other.value = temp;
	}
	
	// family relationships
	// RBT helper functions
	public MeetingIntervalNode parent() {
		return this.parent;
	}
	
	public MeetingIntervalNode grandparent() {
		return this.parent.parent;
	}
	
	// could just access but this makes it more consistent
	public MeetingIntervalNode leftChild() {
		return this.left;
	}
	
	public MeetingIntervalNode rightChild() {
		return this.right;
	}
	
	public MeetingIntervalNode uncle() {
		if(parent == null) return null;
		if(parent.isLeftChild())
			return grandparent().rightChild();
		else
			return grandparent().leftChild();
	}
	
	public MeetingIntervalNode sibling() {
		if(this.isLeftChild())
			return parent.rightChild();
		else
			return parent.leftChild();
	}
	
	// furthest child of sibling
	public MeetingIntervalNode nephew() {
		if(this.sibling() == null) return null;
		else if(this.isRightChild())
			return sibling().leftChild();
		else
			return sibling().rightChild();
	}
	
	// closest child of sibling
	public MeetingIntervalNode niece() {
		if(this.sibling() == null) return null;
		else if(this.isRightChild())
			return sibling().rightChild();
		else
			return sibling().leftChild();
	}
	
	public boolean isLeftChild() {
		if(this.parent == null) 
			return false;
		else if(this.equals(this.parent.left))
			return true;
		else
			return false;
	}
	
	public boolean isRightChild() {
		if(this.parent == null) 
			return false;
		else if(this.equals(this.parent.right))
			return true;
		else
			return false;
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
