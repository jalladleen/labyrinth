
public class Edge {
	
	//Attributes
	private Node firstEndpoint, secondEndpoint;		
	private int type;
	
	
	//Creates and edge of the given type connecting nodes u
	//and v. For example let edge (u, v) represent a corridor of the labyrinth. The first endpoint of this edge
	//is node u and the second endpoint is node v; the type of the edge is 1.
	
	Edge(Node u, Node v, int edgeType){ 
		this.firstEndpoint = u;
		this.secondEndpoint = v;
		this.type = edgeType;
	}
	
	Node firstEndpoint() {
		return firstEndpoint;//		returns the first endpoint of the edge.

	}
	
	Node secondEndpoint() {
		return secondEndpoint; //returns the second endpoint of the edge.
	}
	
	int getType() {
		return type;//returns the type of the edge.
		
	}
	
	void setType(int newType){ //		sets the type of the edge to the specified value.
		type = newType;
	}
	
	boolean equals(Edge otherEdge){
		//returns true if this Edge object connects the same two nodes as otherEdge.
		if(this.firstEndpoint.equals(otherEdge.firstEndpoint)
				&&this.secondEndpoint.equals(otherEdge.secondEndpoint))

					return true;

		else 
			if(this.firstEndpoint.equals(otherEdge.secondEndpoint) 
					&& this.secondEndpoint.equals(otherEdge.firstEndpoint)) 
					return true;
		
		
		return false;
		
	}


}
