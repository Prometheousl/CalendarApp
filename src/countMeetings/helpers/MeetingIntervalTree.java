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
	 * @param root is the root of the tree
	 * @param total is the total number of meetings (should initially be 0)
	 * @return the total number of meetings in the tree
	 */
	public Integer countMeetings(MeetingIntervalNode root, Integer total) {
		if(root == null) return total;
		
		// preorder traversal... doesn't really matter what order
		total += root.value.getMeetingCount();
		total += countMeetings(root.left, total);
		total += countMeetings(root.right, total);
		return total;
	}
	
	/**
	 * Inserts an interval into the tree
	 * It first checks if the interval overlaps with any other intervals
	 * If it does, it merges all overlapping intervals and puts it back
	 * in the tree
	 * 
	 * @param m The interval to insert
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
	 * @param m the interval to check if anything overlaps with it
	 * @return the new merged interval
	 */
	public MeetingInterval handleOverlaps(MeetingInterval m) {
		// Merge all overlapping meetingIntervals
		List<MeetingInterval> overlaps = removeAllOverlaps(m);
		if(!overlaps.isEmpty()) System.out.println("Overlaps: ");
		for (MeetingInterval overlap : overlaps) {
			overlap.display();
			m = m.merge(overlap);
		}
		if(!overlaps.isEmpty()) System.out.print("Merged interval: "); m.display();
		
		return m;
	}
	
	/**
	 * inserts a list of meeting intervals
	 * 
	 * @param meetings the list to insert
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
	 * @param current the current node
	 * @param m the node to insert
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
			if(node.parent != null && node.parent.color == 'B') break;
			if(node.uncle() != null && node.uncle().color == 'R') {
				node.parent.color = 'B';
				node.uncle().color = 'B';
				node.grandparent().color = 'R';
				node = node.grandparent();
			}
			else { // uncle must be black
				if(!node.isLinear()) {
					MeetingIntervalNode oldNode = node;
					MeetingIntervalNode oldParent = node.parent();
					rotate(node);
					node = oldParent;
					node.parent = oldNode;
				}
				node.parent.color = 'B';
				node.grandparent().color = 'R';
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
	 * @param value the value to check what overlaps with it
	 * @return a list of intervals that overlap with value
	 */
	public List<MeetingInterval> removeAllOverlaps(MeetingInterval value) {
		List<MeetingInterval> overlaps = new ArrayList<MeetingInterval>();
		MeetingIntervalNode overlap = findOverlap(root, value);
		System.out.println("*******Finding overlaps************");
		while(overlap != null) {
			if(overlap != null) {
				overlaps.add(overlap.value);
				remove(overlap);
			}
			printTree();
			overlap = findOverlap(root, value);
		}
		System.out.println("********************************");
		return overlaps;
	}
	
	/**
	 * 
	 * @param current
	 * @param value
	 * @return
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
	
	public void remove(MeetingIntervalNode node) {
		root = removeHelper(root, node);
		if(root != null) {
			root.parent = null;
			if(root.color == 'R') root.color = 'B';
		}
		System.out.println("###############REMOVED NODE######");
		node.display();
//		MeetingIntervalNode leaf = swapToLeaf(oldNode);
//		pruneLeaf(leaf);
		deletionFixUp(node);
		// need to fix dayMaxes in case any ancestors contain info about the deleted node
		if(node.parent != null) node.parent.recalculateMax();
	}
	
	public void removeList(List<MeetingInterval> meetings) {
		for(MeetingInterval meeting : meetings) {
			remove(new MeetingIntervalNode(meeting, null));
		}
	}
	
	public MeetingIntervalNode swapToLeaf(MeetingIntervalNode node) {
		if(node == null) return null;
		if(node.left == null && node.right == null) // It's a leaf!
			return node;
		else { // Recursively swap to leaf based on predecessor or successor
			MeetingIntervalNode node_to_be_swapped = null;
			if(node.left != null) 
				node_to_be_swapped = findPredecessor(node);
			else if(node.right != null && node_to_be_swapped == null)
				node_to_be_swapped = findSuccessor(node);
			node.swap(node_to_be_swapped);
			node = node_to_be_swapped;
		}
		return swapToLeaf(node);
	}
	
	/** Returns the predecessor of the given node
	*
	*	@param node = a node
	*/
	private MeetingIntervalNode findPredecessor(MeetingIntervalNode node) {
		if(node == null) return null;
		else if(node.left != null)// return rightmost left child
			return findLargestValue(node.left);
		// else return the first right child of parent
		MeetingIntervalNode y = node.parent;
		MeetingIntervalNode x = node;
		while(y != null && x == y.left) {
			x = y;
			y = y.parent;
		}
		return y;
	}
	/** Returns the successor of the given node
	*
	*	@param node = a node
	*/
	private MeetingIntervalNode findSuccessor(MeetingIntervalNode node) {
		if(node == null) return null;
		else if(node.right != null)
			return findSmallestValue(node.right);
		// else return the first left child of parent
		MeetingIntervalNode y = node.parent;
		MeetingIntervalNode x = node;
		while(y != null && x == y.right) {
			x = y;
			y = y.parent;
		}
		return y;
	}
	
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
	
	/** Deletes a leaf from the tree
	*
	*	@param t = a bst
	*	@param leaf = The leaf node to be pruned
	*/
	private void pruneLeaf(MeetingIntervalNode leaf) {
		if(leaf == root) {
			//printf("Leaf is root\n");
			root = null;
		}
		else if(leaf.parent.left == leaf) {
			leaf.parent.left = null;
			leaf.parent = null;
		}
		else if(leaf.parent.right == leaf) {
			leaf.parent.right = null;
		}
	}
	
	private void deletionFixUp(MeetingIntervalNode node) {
		while(true) {
			if(root == node) break;
			if(node.color == 'R') break;
			if(node.sibling().color == 'R') {
				node.parent.color = 'R';
				node.sibling().color = 'B';
				rotate(node.sibling());
				// should have black sibling now
			}
			else if(node.nephew().color == 'R') {
				node.sibling().color = node.parent.color;
				node.parent.color = 'B';
				node.nephew().color = 'B';
				rotate(node.sibling());
				// subtree and tree is BH balanced
				break;
			}
			else if(node.niece().color == 'R') {
				// nephew is black
				node.niece().color = 'B';
				node.sibling().color = 'R';
				rotate(node.niece());
				// should have red nephew now
			}
			else { // sibling,niece, and nephew are black
				if(node.sibling() != null)
					node.sibling().color = 'R';
				node = node.parent();
				// Subtree is BH balanced but tree is not
			}
		}
		node.color = 'B';
	}
	
	private void rotate(MeetingIntervalNode node) {
		if(node.isLeftChild() && !node.parent.isLeftChild())
			rightRotate(node.parent);
		else if(node.isRightChild() && !node.parent.isRightChild())
			leftRotate(node.parent());
		else if(node.leftChild() != null && node.leftChild().isLinear())
			rightRotate(node.parent());
		else if(node.rightChild() != null && node.rightChild().isLinear())
			leftRotate(node.parent());
	}
	
	private void leftRotate(MeetingIntervalNode x) {
		MeetingIntervalNode y = x.rightChild();
		x.right = y.leftChild(); // y's left subtree = x's right subtree
		if(y.leftChild() != null)
			y.leftChild().parent = x;
		y.parent = x.parent;
		if(x.parent() == null)
			root = y;
		else {
			if(x.parent.leftChild() == x)
				x.parent.left = y;
			else
				x.parent.right = y;
		}
		y.left = x;
		x.parent = y;
		
		x.recalculateMax();
		y.recalculateMax();
	}
	
	private void rightRotate(MeetingIntervalNode x) {
		MeetingIntervalNode y = x.leftChild();
		x.left = y.rightChild(); // y's left subtree = x's right subtree
		if(y.rightChild() != null)
			y.rightChild().parent = x;
		y.parent = x.parent;
		if(x.parent() == null)
			root = y;
		else {
			if(x.parent.rightChild() == x)
				x.parent.right = y;
			else
				x.parent.left = y;
		}
		y.right = x;
		x.parent = y;
		
		x.recalculateMax();
		y.recalculateMax();
	}
	
	private MeetingIntervalNode findSmallestValue(MeetingIntervalNode root) {
		return root.left == null ? root : findSmallestValue(root.left);
	}
	
	private MeetingIntervalNode findLargestValue(MeetingIntervalNode root) {
		return root.right == null ? root : findLargestValue(root.right);
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
}
