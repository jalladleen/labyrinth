
public class Node {
	
	private int name;//The name of a node is an integer value between 0 and n − 1.
	private boolean marked;//initially the value of this variable is false.
		
	
		
	public Node(int nodeName) {//Creates an unmarked node with the given name.
		this.name = nodeName;
		this.marked = false;
	}
	
	public void setMark(boolean mark) {//marks the node with the specified value.
		this.marked = mark;
	}
	boolean getMark() { //returns the value with which the node has been marked.
		return marked;
	}
	
	public int getName() { //returns the name of the node.
		return name;
	}
	

	boolean equals(Node otherNode) {//returns true of this node has the same name as otherNode; returns false otherwise.
		return otherNode.name == (this.name) && otherNode.marked == (this.marked);
	}
}
