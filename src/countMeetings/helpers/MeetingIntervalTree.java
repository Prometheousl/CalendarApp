package countMeetings.helpers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * Date-Based Interval Tree (Augmented Red-Black Tree)
 * 
 * Resources Used:
 * https://www.geeksforgeeks.org/interval-tree/
 * Cormen, Introduction to Algorithms 3rd edition, pg.348 Section 14.3 "Interval Trees"
 *     https://repo.palkeo.com/algo/Introduction%20to%20Algorithms%203rd%20Edition.pdf
 * 
 * My github - I wrote a RBT and BST class in a Data Structures class in C
 *     https://github.com/Prometheousl/School/blob/master/Data_Structures_and_Algorithms/data_structures/rbt.c
 *     https://github.com/Prometheousl/School/blob/master/Data_Structures_and_Algorithms/data_structures/bst.c
 * 
 * @author Alex Lay
 */
public class MeetingIntervalTree {
	private MeetingIntervalNode root;
	
	public MeetingIntervalTree() {
		root = null;
	}
	
	public MeetingIntervalNode getRoot() {
		return root;
	}
	
	/**
	 * Counts the total number of meetings
	 * 
	 * @param root = is the root of the tree
	 * @return the total number of meetings in the tree
	 */
	public Integer countMeetings(MeetingIntervalNode root) {
		if(root == null) return 0;
		
		return root.value.getMeetingCount() + countMeetings(root.right) 
											+ countMeetings(root.left);
	}
	
	/**
	 * Inserts an interval into the tree
	 * It first checks if the interval overlaps with any other intervals
	 * If it does, it merges all overlapping intervals and puts it back
	 * in the tree
	 * 
	 * @param m = The interval to insert
	 */
	public void insert(MeetingInterval m) {
		if(root == null) {
			root = new MeetingIntervalNode(m, null);
		    root.color = 'B';
		}
		else {
			m = handleOverlaps(m);		  // merge overlapping
			MeetingIntervalNode newNode = new MeetingIntervalNode(m, null);
			insertHelper(root, newNode);  // insert merged interval
			insertionFixUp(newNode);
			newNode.recalculateMax();
		}
	}
	
	/**
	 * (1) Removes all intervals that overlap with m
	 * (2) Merges m and the overlapping intervals
	 * 
	 * @param m = the interval to check if anything overlaps with it
	 * @return the new merged interval
	 */
	public MeetingInterval handleOverlaps(MeetingInterval m) {
		// Merge all overlapping meetingIntervals
		List<MeetingInterval> overlaps = removeAllOverlaps(m);
		for (MeetingInterval overlap : overlaps) {
			m = m.merge(overlap);
		}
		
		return m;
	}
	
	/**
	 * inserts a list of meeting intervals
	 * 
	 * @param meetings = the list to insert
	 */
	public void insertList(List<MeetingInterval> meetings) {
		for(MeetingInterval m : meetings) {
			insert(m);
		}
	}
	
	/**
	 * Recursively inserts into the tree
	 * Sorted by beginDate
	 * 
	 * @param current = the current node
	 * @param m = the node to insert
	 */
	private void insertHelper(MeetingIntervalNode current, MeetingIntervalNode m) {
		if(root == null) {
			root = m;
		    root.color = 'B';
		    return;
		}
		// sort by beginDate
		if(m.value.beginDate.compareTo(current.value.beginDate) <= 0) {
			if(current.left == null) {
				m.parent = current;
				current.left = m;
			}
			else
				insertHelper(current.left, m);
		}
		else {
			if(current.right == null) {
				m.parent = current;
				current.right = m;
			}
			else
				insertHelper(current.right, m);
		}
	}
	
	/** Recursively fixes up the colors after an insertion into the tree
	  *
	  * @param node = The current node
	  */
	private void insertionFixUp(MeetingIntervalNode node) {
		while(true) {
			if(root.equals(node)) break;
			if(parent(node) != null && parent(node).color == 'B') break;
			if(uncle(node) != null && uncle(node).color == 'R') {
				parent(node).color = 'B';
				uncle(node).color = 'B';
				grandparent(node).color = 'R';
				node = grandparent(node);
			}
			else { // uncle must be black
				if(!isLinear(node)) {
					MeetingIntervalNode oldNode = node;
					MeetingIntervalNode oldParent = parent(node);
					rotate(node);
					node = oldParent;
					node.parent = oldNode;
				}
				parent(node).color = 'B';
				grandparent(node).color = 'R';
				// rotate parent to grandparent
				rotate(node.parent);
				break;
			}
		}
		root.color = 'B';
	}
	
	/**
	 * Removes all values that overlap with value and returns them in a list
	 * 
	 * @param = value the value to check what overlaps with it
	 * @return a list of intervals that overlap with value
	 */
	public List<MeetingInterval> removeAllOverlaps(MeetingInterval value) {
		List<MeetingInterval> overlaps = new ArrayList<MeetingInterval>();
		MeetingIntervalNode overlap = findOverlap(root, value);
		while(overlap != null) {
			if(overlap != null) {
				overlaps.add(overlap.value);
				remove(overlap);
			}
			overlap = findOverlap(root, value);
		}
		return overlaps;
	}
	
	/**
	 * Finds a node that overlaps with the given value
	 * 
	 * @param current = the current node
	 * @param value = the value to find what overlaps with it
	 * @return a node that overlaps with value
	 */
	public MeetingIntervalNode findOverlap(MeetingIntervalNode current, MeetingInterval value) {
		if(current == null) return null;
		
		// if meetingIntervals overlap, return
		if(current.value.overlaps(value)) {
			return current;
		}
		else if(current.value.dayOfTheWeek != null && value.dayOfTheWeek != null) {
			// dealing with two meetings so should compare the maxes of the same day
			if(current.left != null && current.left.dayMax.containsKey(value.dayOfTheWeek)) {
				LocalDate leftDayMax = current.left.dayMax.get(value.dayOfTheWeek);
				if(leftDayMax.compareTo(value.beginDate) >= 0)
					return findOverlap(current.left, value);
			}
			return findOverlap(current.right, value);
		}
		// if max of left child >= to interval, may overlap with interval in left subtree
		else if(current.left != null && current.left.max.compareTo(value.beginDate) >= 0)
			return findOverlap(current.left, value);
		// else could overlap with right subtree
		return findOverlap(current.right, value);
	}
	
	/**
	 * Removes a given node from the tree
	 * 
	 * @param node = the node to remove
	 */
	public void remove(MeetingIntervalNode node) {
		root = removeHelper(root, node);
		if(root != null) {
			root.parent = null;
			if(root.color == 'R') root.color = 'B';
			deletionFixUp(node);
			// need to fix dayMaxes in case any ancestors contain info about the deleted node
			if(node.parent != null) node.parent.recalculateMax();
		}
	}
	
	/**
	 * Removes a list of intervals from the tree
	 * 
	 * @param meetings = the list of intervals to remove
	 */
	public void removeList(List<MeetingIntervalNode> meetings) {
		for(MeetingIntervalNode meeting : meetings) {
			remove(meeting);
		}
	}
	
	/**
	 * Recursively removes the given node
	 * 
	 * @param current = the current node
	 * @param node = the node to remove
	 * @return the removed node
	 */
	public MeetingIntervalNode removeHelper(MeetingIntervalNode current, MeetingIntervalNode node) {
		if(current == null)
			return null;
		
		if(node.equals(current)) {
			// remove node (4 cases)
			// (1) leaf node
			if(current.left == null && current.right == null)
				return null;
			// (2) no right child
			else if(current.right == null)
				return current.left;
			// (3) no left child
			else if(current.left == null)
				return current.left;
			// (4) 2 children
			else {
				// find smallest value in right subtree to replace the node
				MeetingIntervalNode smallest = findSmallestValue(current.right);
				smallest.left = current.left;
//				smallest.right = current.right;
				current = smallest;
				current.right = removeHelper(current.right, smallest);
				return current;
			}
		}
		
		// if max of left child >= to interval, may overlap with interval in left subtree
		if(current.left != null && current.left.max.compareTo(node.value.beginDate) >= 0)
			current.left = removeHelper(current.left, node);
		
		// else could overlap with right subtree
		current.right = removeHelper(current.right, node);
		
		return current;
	}
	
	/** Recursively fixes up the colors after a deletion into the tree
	  *
	  * @param node = The current node
	  */
	private void deletionFixUp(MeetingIntervalNode node) {
		while(true) {
			if(node == null) return;
			if(root == node) break;
			if(node != null && node.color == 'R') break;
			if(sibling(node) != null && sibling(node).color == 'R') {
				parent(node).color = 'R';
				sibling(node).color = 'B';
				rotate(sibling(node));
				// should have black sibling now
			}
			else if(nephew(node) != null && nephew(node).color == 'R') {
				sibling(node).color = node.parent.color;
				parent(node).color = 'B';
				nephew(node).color = 'B';
				rotate(sibling(node));
				// subtree and tree is BH balanced
				break;
			}
			else if(niece(node) != null && niece(node).color == 'R') {
				// nephew is black
				niece(node).color = 'B';
				sibling(node).color = 'R';
				rotate(niece(node));
				// should have red nephew now
			}
			else { // sibling,niece, and nephew are black
				if(sibling(node) != null)
					sibling(node).color = 'R';
				node = parent(node);
				// Subtree is BH balanced but tree is not
			}
		}
		node.color = 'B';
	}
	
	/**
	 * finds successor
	 * 
	 * @param root = the current node
	 */
	private MeetingIntervalNode findSmallestValue(MeetingIntervalNode root) {
		return root.left == null ? root : findSmallestValue(root.left);
	}
	
	
	public void printTree() {
		System.out.println("************");
		preorder(root);
		System.out.println("************");
	}
	
	// passing in a function might be nice here
	public void inorder(MeetingIntervalNode root) {
		if(root == null) return;
		
		inorder(root.left);
		root.display();
		inorder(root.right);
	}
	
	// passing in a function might be nice here
	public void preorder(MeetingIntervalNode root) {
		if(root == null) return;
		
		root.display();
		preorder(root.left);
		preorder(root.right);
	}
	
	// ************* Red-Black tree helper functions ****************
	/**
	 * Rotates node to its parent
	 * Determines if it should be a right or left rotation
	 * 
	 * @param node = the node to rotate
	 */
	private void rotate(MeetingIntervalNode node) {
		if(isLeftChild(node) && !isLeftChild(parent(node)))
			rightRotate(node.parent);
		else if(isRightChild(node) && !isRightChild(parent(node)))
			leftRotate(parent(node));
		else if(leftChild(node) != null && isLinear(leftChild(node)))
			rightRotate(parent(node));
		else if(rightChild(node) != null && isLinear(rightChild(node)))
			leftRotate(parent(node));
	}
	
	/**
	 * left rotation
	 * 
	 * @param node = the node to rotate
	 */
	private void leftRotate(MeetingIntervalNode x) {
		MeetingIntervalNode y = rightChild(x);
		x.right = leftChild(y); // y's left subtree = x's right subtree
		if(leftChild(y) != null)
			leftChild(y).parent = x;
		y.parent = x.parent;
		if(parent(x) == null)
			root = y;
		else {
			if(leftChild(parent(x)) == x)
				x.parent.left = y;
			else
				x.parent.right = y;
		}
		y.left = x;
		x.parent = y;
		
		x.recalculateMax();
		y.recalculateMax();
	}
	
	/**
	 * right rotation
	 * 
	 * @param node = the node to rotate
	 */
	private void rightRotate(MeetingIntervalNode x) {
		MeetingIntervalNode y = leftChild(x);
		x.left = rightChild(y); // y's left subtree = x's right subtree
		if(rightChild(y) != null)
			rightChild(y).parent = x;
		y.parent = x.parent;
		if(parent(x) == null)
			root = y;
		else {
			if(rightChild(parent(x)) == x)
				x.parent.right = y;
			else
				x.parent.left = y;
		}
		y.right = x;
		x.parent = y;
		
		x.recalculateMax();
		y.recalculateMax();
	}
	
		public boolean isLinear(MeetingIntervalNode node) {
			if(isLeftChild(node) && !isLeftChild(parent(node)))
				return false;
			if(isRightChild(node) && !isRightChild(parent(node)))
				return false;
			return true;
		}
		
		public char getColor(MeetingIntervalNode node) {
			if(node == null) return 'B';
			else return node.color;
		}
		
		// family relationships
		// RBT helper functions
		public MeetingIntervalNode parent(MeetingIntervalNode node) {
			if(node == null) return null;
			return node.parent;
		}
		
		public MeetingIntervalNode grandparent(MeetingIntervalNode node) {
			if(parent(node) == null) return null;
			return parent(parent(node));
		}
		
		// could just access but this makes it more consistent
		public MeetingIntervalNode leftChild(MeetingIntervalNode node) {
			if(node == null) return null;
			return node.left;
		}
		
		public MeetingIntervalNode rightChild(MeetingIntervalNode node) {
			if(node == null) return null;
			return node.right;
		}
		
		public MeetingIntervalNode uncle(MeetingIntervalNode node) {
			if(parent(node) == null ||grandparent(node) == null) return null;
			if(isLeftChild(parent(node)))
				return rightChild(grandparent(node));
			else
				return leftChild(grandparent(node));
		}
		
		public MeetingIntervalNode sibling(MeetingIntervalNode node) {
			if(parent(node) == null) return null;
			if(isLeftChild(node))
				return rightChild(parent(node));
			else
				return leftChild(parent(node));
		}
		
		// furthest child of sibling
		public MeetingIntervalNode nephew(MeetingIntervalNode node) {
			if(sibling(node) == null) return null;
			else if(isRightChild(node))
				return leftChild(sibling(node));
			else
				return rightChild(sibling(node));
		}
		
		// closest child of sibling
		public MeetingIntervalNode niece(MeetingIntervalNode node) {
			if(sibling(node) == null) return null;
			else if(isRightChild(node))
				return rightChild(sibling(node));
			else
				return leftChild(sibling(node));
		}
		
		public boolean isLeftChild(MeetingIntervalNode node) {
			if(parent(node) == null) 
				return false;
			else if(node.equals(leftChild(parent(node))))
				return true;
			else
				return false;
		}
		
		public boolean isRightChild(MeetingIntervalNode node) {
			if(parent(node) == null) 
				return false;
			else if(node.equals(rightChild(parent(node))))
				return true;
			else
				return false;
		}
		
		// *********************************************
}
