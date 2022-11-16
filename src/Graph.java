
import java.util.ArrayList;

public class Graph {

	//Class data members
	
	private Edge graphEdges[][]; // store edges in the graph
	private Node graphNodes[];	// store nodes in the graph

	// creates an empty graph with n nodes and no edges.
	public Graph(int n){
		
		graphEdges = new Edge[n][n];
		graphNodes = new Node[n];
		int i = 0;
		
		while(i < n){
			graphNodes[i] = new Node(i);
			i++;
		}
	}
	

	public void insertEdge(Node nodeu, Node nodev, int bRICK) throws GraphException{
		
		// Make sure that the nodes exists and are within the boundaries of the graph 
		
		// if it node does not exist, then throw an exception 
		if (nodeExists(nodeu.getName()) && nodeExists(nodev.getName()) == false)
		{
			throw new GraphException("Error: the node does not exist in the graph");
		}
		
		//Else,
		
		else{
			if(edgeExists(nodev, nodeu))
				throw new GraphException("Error: The Edge does exist in the graph");
			// check if the edge exists or not 
			
			else{
				// if not, then insert the edge in row of node u and v of the matrix
				graphEdges[nodev.getName()][nodeu.getName()] = new Edge(nodev, nodeu, bRICK);
				graphEdges[nodeu.getName()][nodev.getName()] = new Edge(nodeu, nodev, bRICK);
			}
		}
	}

	public Node getNode(int name) throws GraphException {

		
		// if the node with the specified name exists
		if (nodeExists(name)) {
			return graphNodes[name]; //Then, return the node with the specified name	
		}
		
		//Throw an exception if the node does not exist
		throw new GraphException("Error: Node is not in the graph");
		
	}

	public ArrayList<Edge> incidentEdges(Node u) throws GraphException{
		int i = 0;
		
		//Check if node exists
		
		// if the node does not exist then throw an exception 
		if (nodeExists(u.getName()) == false)
			throw new GraphException("Error: Invalid node");
		
		//else,
		else{
			
			//Create an array list to  store the incident edges
			ArrayList<Edge> incidentEdges = new ArrayList<Edge>();
			
			// find the row in the matrix that corresponds to the node u
			while( i < graphNodes.length){
				
				// if the edge is found/exists then add it to the array list
				if (graphEdges[u.getName()][i] != null){
					incidentEdges.add(graphEdges[u.getName()][i]);
				}
				i++;
			}
			
			//Return null if no edges were found
			//else return all the edges from the list
			
			return (incidentEdges.isEmpty()) ? null: incidentEdges;
		}
	}

	public Edge getEdge(Node u, Node v) throws GraphException{
		
		//Make sure that the node exists
		
		// If not, then throw an exception 
		if (nodeExists(u.getName()) && nodeExists(v.getName()) == false)
			throw new GraphException("Error: Invalid node");
		
		//If exists, 
		else{
			//then check if edge exists
			
			// if does not exist, then return an exception 
			if (!edgeExists(u, v))
				throw new GraphException("Error: The Edge does exist in the graph");
			
			//else, return the edge for node u and v
			else
				return graphEdges[u.getName()][v.getName()];
		}

	}


	public boolean areAdjacent(Node u, Node v) throws GraphException{
		
		//Check if nodes exist
		
		//return true if the nodes are adjacent, false otherwise
		if (nodeExists(u.getName()) && nodeExists(v.getName()))
			return graphEdges[u.getName()][v.getName()] != null;
		
		//GraphException Throws exception if the nodes do not exist
		else
			throw new GraphException("Error: Invalid node");

	}
	
	private boolean nodeExists(int u) {
		
		//If the node is outside the boundaries, then return false
		if (u > graphNodes.length - 1){
			return false;
		}
		
		else if (u < 0) {
			return false;
		}
		
		//else, if the node exists then return true 
		return true;
	}
	
	
	private boolean edgeExists(Node v, Node u) {
		
		// if the edge does not exist, then return false 
		if (graphEdges[u.getName()][v.getName()] == null)
		{
			return false;
		}
		
		if(graphEdges[v.getName()][u.getName()] == null)
		{
			return false;
		}
		
		//else, return true;
		return true;
	}
	
	
	
}