//////Labyrinth object, for building a maze from designated text file
//////Written by Ben Dumais, 250699195, for CS2210 Assignment 5
////
/////* Imports */
////import java.io.BufferedReader;
////import java.io.FileReader;
////import java.util.*;
////
////
////public class Solver {
////
////	/* Attributes */
////	private Graph graph;							//Declare graph object
////	private int width, length, k, start, finish;	//Declare integers for the width, length, walls we can break (k) and the start/finish values of the labyrinth
////	
////	
////	/* Constructor */
////	public Solver(String inputFile) throws LabyrinthException{	
////	 try{
////	      BufferedReader input = new BufferedReader(new FileReader(inputFile));	//Create a reader from the input file
////	     
////	      Integer.parseInt(input.readLine());			//Throw away the first line of the input, as we do not need it
////	      width = Integer.parseInt(input.readLine());	//Set the width the the second line
////	      length = Integer.parseInt(input.readLine());	//Set the Length to the third line
////	      k = Integer.parseInt(input.readLine());		//Set k to the fourth line
////	      
////	      graph = new Graph(width*length);				//Build a graph of appropriate size to hold all nodes/rooms
////	      
////	      String line;									//Declare variable for string
////	      int row = -1;									//Declare and initialize integer for current row of labyrinth we are reading
////	      boolean isRoom;								//Declare a boolean to check if we are reading a room or an edge
////	      
////	      while( (line = input.readLine() ) != null ){	//Loop until we read a null string
////	    	  
////	    	  row++;									//Increment current row
////	    	  
////	    	  for(int i = 0; i < line.length(); i++){	//Loop through current line
////	    		  
////	    		  isRoom = (i%2 == 0 && row%2 == 0);	//If the current character is even and the row number is even, we are reading a room
////	    		  
////	    		  /*
////	    		   * For the following, We can calculate the nodes associated with the current character using the following knowledge:
////	    		   * 	Rooms are every even character in every even row, edges only appear between nodes, and we already know how many nodes are in each row.
////	    		   * 	We can calculate the value of a node within its current row by dividing it by 2 (position 4 is node #2 in that row etc)
////	    		   * 	We can adjust this to the node's actual value within the graph by adding the width of the labyrinth * the row/2
////	    		   */
////	    		  
////	    		  if(isRoom){							//If we are reading a room
////	    			  if(line.charAt(i) == 's')				//If the character we are reading is s, we are reading the start
////	    				  start = (i/2) + (row/2)*width;	//Set start to the corresponding node value
////	    			  else if(line.charAt(i) == 'e')		//If its an e, its the exit
////	    				  finish = (i/2) + (row/2)*width;	//Set Finish to the corresponding node value   				  
////	    		  }
////	    		  else{									//Otherwise we are reading an edge or blank space
////	    			  if(line.charAt(i) == '-')			//If the character is a -, its a horizontal hallway
////	    				  graph.insertEdge(graph.getNode( (i/2) + (row/2)*width ), graph.getNode( (i/2) + (row/2)*width + 1 ), "hall");	//Create a new hall edge from the corresponding nodes
////	    			  else if(line.charAt(i) == '|')	//If its an | its a vertical hall
////	    				  graph.insertEdge(graph.getNode( (i/2) +((row-1)/2)*width ), graph.getNode( (i/2) +((row+1)/2)*width ), "hall");//Create a new hall edge from the corresponding nodes
////	    			  else if(line.charAt(i) == 'h')	//If its an h its a horizontal wall
////	    				  graph.insertEdge(graph.getNode( (i/2) + (row/2)*width ), graph.getNode( (i/2) + (row/2)*width + 1), "wall");	//Create a new wall edge from the corresponding nodes
////	    			  else if(line.charAt(i) == 'v') 	//If its a v its a vertical wall
////	    				  graph.insertEdge(graph.getNode( (i/2) +((row-1)/2)*width ), graph.getNode( (i/2) +((row+1)/2)*width ), "wall"); //Create a new wall edge from the corresponding nodes
////	    		  }
////
////	    	  }
////	    	  
////	      }//End of while line != null
////	      
////	      input.close();	//Close the input reader, as we have read the whole file
////		
////	 }
////	 catch(Exception e){	//Catch any exceptions thrown while trying to create the labyrinth
////		 throw new LabyrinthException("Error creating labyrinth");	//Throw a new labyrinth exception
////	 }
////	}
////
////	/* Methods */
////	
////	//Method to return graph object of this Labyrinth
////	public Graph getGraph() throws LabyrinthException{
////		if(graph == null)	//If the graph does not exist, throw an exception
////			throw new LabyrinthException("Graph is undefined");
////		else	//Otherwise return it
////			return graph;
////	}
////	
////	//Method to solve and return an iterator of the labyrinth
////	public Iterator solve(){
////		
////		/*
////		 * To solve the labyrinth, I decided to use a modified DFS search that utilizes two stacks:
////		 * 		The first stack is "sol" or the solution stack, which will hold the nodes that make up the path to the exit
////		 *		The second is "paths" or the available paths to try, which will store unmarked and traversable nodes we can try next
////		 *
////		 * The method works by placing the entrance in the solution stack, searching all adjacent nodes for possible paths to try and then pushing these nodes
////		 *  onto paths as they are found. Once all adjacent nodes have been looked at and added to paths if untravelled in our current solution, we take the next
////		 *  path and add it to the solution before repeating the search.
////		 *  
////		 * The Stack allows us to search paths and manage nodes in a LIFO manner. This causes the latest node discovered to be added next to the solution, causing
////		 *  the Labyrinth to be search as far down, then as far right, then left, then up as possible (highest to lowest node number). As most mazes follow this pattern
////		 *  (entrance top left exit bottom right) this is a rather efficient means of search.
////		 *  
////		 * In the event that no new paths are found from the current node in solution, we can backtrack by simply popping the solution stack
////		 *  until the new top is adjacent to the next possible path.
////		 */
////			
////		try{
////			Stack paths = new Stack(), sol = new Stack();					//Initialize 2 stacks, one for the solution and the other for the available nodes to try
////			Node cur, s = graph.getNode(start), e = graph.getNode(finish);	//Initialize two nodes for the start and finish, and declare a third node
////			boolean added;													//Declare a boolean variable for if a node was added
////			Edge edge = null;												//Declare an Edge variable for use in the search
////		
////			sol.push(s);				//Push the start of the labyrinth onto the available paths
////			s.setMark(true);			//Mark the start (so we dont read it to the stack)
////		
////			while(!sol.isEmpty()){	//Loop until all nodes are removed from the solution (The entrance does not lead to the exit)
////			
////				cur = (Node) sol.peek();	//Set the current node to the top of the solution stack
////					
////				if(cur == e){				//If the current node is the exit, return the iterator for the solution stack
////					return sol.iterator();
////				}
////				else{						//Other wise we must search the current solution node for available paths
////					
////					Iterator it = graph.incidentEdges(cur);		//get the iterator of incident edges on the current node
////					added = false;								//Set added to false
////					
////					while(it.hasNext()){			//Loop through all the incident edges
////						edge = (Edge)it.next();			//Set the current edge to the next edge from the iterator
////						cur = edge.secondEndpoint();	//Set the current node to the endpoint of the edge
////						
////						if(!cur.getMark()){							//If the node is not marked (is not in the stack already), check the following
////							
////							if(edge.getType() == "wall" && k > 0){		//If the edge is a wall and we can break it, do the following
////								cur.setMark(true);							//Mark this node so we do not readd it
////								paths.push(cur);							//Push this node onto paths
////								added = true;								//Set added to true, as we added a node to the stack (found at least one path from the current solution node)
////							}
////							
////							else if(edge.getType() == "hall"){			//If its a hall
////								cur.setMark(true);							//Mark the node
////								paths.push(cur);							//Push it onto paths stack
////								added = true;								//Update added variable
////							}
////						}			
////					}//End of while more nodes in iterator
////						
////					if(!added){	//If no nodes were added from the current solution path, we must backtrack
////							
////						//Pop solution (remove the current solution node as its a dead end) and get the edge between it and the new top (previous solution node)
////						if(graph.getEdge((Node)sol.pop(), (Node)sol.peek()).getType() == "wall")	
////							k++;				//If it was a wall, increment k so we can break a different wall
////						
////						//Backtrack as long both stacks have nodes in them and the top nodes are not adjacent (the node on the top is not connected to the next available path)
////						while( sol.size() > 0 && paths.size() > 0 && !graph.areAdjacent((Node)sol.peek(), (Node)paths.peek()) ){
////							Node tmpNode = (Node)sol.pop();							//Pop the top node from solution and set it to tmpNode
////							Edge tmpEdge = graph.getEdge(tmpNode, (Node)sol.peek());//Get the edge between the popped node and the new top of the stack
////							tmpNode.setMark(false);									//Unmark the node we popped, as we may need to traverse it later
////							if(tmpEdge.getType() == "wall")							//If the edge between the two nodes was a wall, increment k as we can now break a different wall
////								k++;
////						}
////							
////						/*
////						 * After the above loop is done, any nodes not connected to the top node of paths (next available pathway) are removed from the solution
////						 */
////							
////						//If either stack is now empty (no new paths to try or every node in the solution stack didn't lead to the exit (including the start))
////						if(paths.size() == 0 || sol.size() == 0)
////							return null;	//return null, a solution could not be found
////						
////					}//End of backtracking
////					
////					/* Now we take the latest path found (top of paths stack) and explore it (push it onto solution and continue) */
////
////					//Check if this path takes us through a wall
////					//This check will never cause a negative k, as we do not add a path to the stack if we do not have enough 'bombs' and we regain them from backtracking
////					if(graph.getEdge((Node)sol.peek(), (Node)paths.peek()).getType() == "wall")	//If the edge between the latest node found and the previous node is a wall
////						k--;				//Decrement k
////							
////					sol.push(paths.pop());	//Pop paths and push it onto solution stack
////					
////					//Now we go back to the top and search from this newly added node
////				}
////			
////			}//End of while solution stack is not empty	
////			
////			//If the solution stack was emptied and we did not return an iterator, no solution could be found
////			return null;
////			
////		}//End of try
////		catch(Exception e){	//Catch any exceptions thrown by the above code
////			System.out.println("Error solving labyrinth");	//Print an error message
////			return null;									//Return null
////		}
////		
////	}//End of solve method
////	
////}
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.Iterator;
//import java.util.Stack;
//
///**
// * Class that represents the labyrinth stored in a graph and solved using a
// * graph
// * 
// * @author stephenkim
// *
// */
//public class Solver {
//	// Initializing the instance variables
//	private int width, length, bBombs, aBombs, entrance, exit;
//	private Graph labyrinth;
//	private Stack<Node> solver;
//
//	/**
//	 * Constructor method that builds a labyrinth using the contents of the
//	 * input file
//	 * 
//	 * @param inputFile
//	 *            - file to be used to build the labyrinth
//	 * @throws LabyrinthException
//	 *             if no input file
//	 * @throws GraphException
//	 *             if nodes don't exist, or edge already exists between nodes
//	 */
//	public Solver(String inputFile) throws LabyrinthException, GraphException {
//		solver = new Stack<Node>();
//		int nodeName = 0;
//		try {
//			/* Reads the input file */
//			BufferedReader lab = new BufferedReader(new FileReader(inputFile));
//			/* Skips the first line since not used by program */
//			lab.readLine();
//			width = Integer.parseInt(lab.readLine());
//			length = Integer.parseInt(lab.readLine());
//			bBombs = Integer.parseInt(lab.readLine());
//			aBombs = Integer.parseInt(lab.readLine());
//			/* Creates the labyrinth using the width and length */
//			labyrinth = new Graph(width * length);
//			while (true) {
//				/*
//				 * Goes through the file line by line and breaks when there are
//				 * no more lines to read
//				 */
//				String inputLine = lab.readLine();
//				if (inputLine == null) {
//					lab.close();
//					break;
//				}
//				/*
//				 * Checks each character in the line and builds the dungeon
//				 * using the character values
//				 */
//				for (int i = 0; i < inputLine.length(); i++) {
//					char character = inputLine.charAt(i);
//					// Room nodes
//					if (character == 'b') {
//						entrance = nodeName;
//						nodeName++;
//					} else if (character == 'x') {
//						exit = nodeName;
//						nodeName++;
//					} else if (character == '+') {
//						nodeName++;
//						// Horizontal Edges
//					} else if (character == 'h') {
//						labyrinth.insertEdge(labyrinth.getNode(nodeName - 1), labyrinth.getNode(nodeName), 1);
//					} else if (character == 'H') {
//						labyrinth.insertEdge(labyrinth.getNode(nodeName - 1), labyrinth.getNode(nodeName), 2);
//					} else if (character == 'm') {
//						labyrinth.insertEdge(labyrinth.getNode(nodeName - 1), labyrinth.getNode(nodeName), 3);
//					} else if (character == '-') {
//						labyrinth.insertEdge(labyrinth.getNode(nodeName - 1), labyrinth.getNode(nodeName), 4);
//						// Vertical Edges
//					} else if (character == 'v') {
//						labyrinth.insertEdge(labyrinth.getNode(nodeName - width + ((i + 1) / 2)),
//								labyrinth.getNode(nodeName + ((i + 1) / 2)), 1);
//					} else if (character == 'V') {
//						labyrinth.insertEdge(labyrinth.getNode(nodeName - width + ((i + 1) / 2)),
//								labyrinth.getNode(nodeName + ((i + 1) / 2)), 2);
//					} else if (character == 'M') {
//						labyrinth.insertEdge(labyrinth.getNode(nodeName - width + ((i + 1) / 2)),
//								labyrinth.getNode(nodeName + ((i + 1) / 2)), 3);
//					} else if (character == '|') {
//						labyrinth.insertEdge(labyrinth.getNode(nodeName - width + ((i + 1) / 2)),
//								labyrinth.getNode(nodeName + ((i + 1) / 2)), 4);
//					}
//				}
//			}
//		} catch (IOException e) {
//			throw new LabyrinthException("Cannot build labyrinth, file does not exist!");
//		}
//	}
//
//	/**
//	 * Method that returns a reference to the graph representing the labyrinth
//	 * 
//	 * @return reference to the graph representing the labyrinth
//	 * @throws LabyrinthException
//	 *             if the graph is not defined
//	 */
//	public Graph getGraph() throws LabyrinthException {
//		if (labyrinth == null) {
//			throw new LabyrinthException("Graph not defined!");
//		} else {
//			return labyrinth;
//		}
//	}
//
//	/**
//	 * Method that returns an iterator containing the nodes along the path from
//	 * the entrance to the exit of the labyrinth
//	 * 
//	 * @return iterator containing the path from the entrance to the exit
//	 * @throws LabyrinthException
//	 */
//	public Iterator<Node> solve() throws GraphException {
//		Node adjNode = null;
//		Node prevNode = null;
//		Edge edgeToCheck = null;
//		int prevInd;
//		// Adds the entrance to the stack and marks it
//		Node currentNode = labyrinth.getNode(entrance);
//		solver.push(currentNode);
//		currentNode.setMark(true);
//		while (true) {
//			// Exits loop if the current node is the exit
//			if (currentNode == labyrinth.getNode(exit)) {
//				break;
//			} else {
//				// Creates iterator storing incident edges of the current node
//				Iterator<Edge> edges = labyrinth.incidentEdges(currentNode);
//				while (edges.hasNext()) {
//					edgeToCheck = edges.next();
//					// Checks whether the connected node is marked
//					if (edgeToCheck.secondEndpoint().getMark() == false) {
//						adjNode = edgeToCheck.secondEndpoint();
//						// Checks whether the current edge can be passed
//						if (passable(edgeToCheck)) {
//							solver.push(adjNode);
//							adjNode.setMark(true);
//							currentNode = adjNode;
//							break;
//							// If can't be passed and no more edges to check
//						} else if (!passable(edgeToCheck) && !edges.hasNext()) {
//							prevInd = solver.size() - 2;
//							prevNode = solver.get(prevInd);
//							restoreBombs(currentNode, prevNode, labyrinth);
//							currentNode = prevNode;
//							// Removes the node from the stack
//							solver.pop();
//							break;
//						}
//						// If no more edges to check and the connected node is
//						// marked
//					} else if (!edges.hasNext()) {
//						prevInd = solver.size() - 2;
//						prevNode = solver.get(prevInd);
//						restoreBombs(currentNode, prevNode, labyrinth);
//						currentNode = prevNode;
//						// Removes the node from the stack
//						solver.pop();
//						break;
//					}
//				}
//			}
//		}
//		// If stack is empty or only the entrance remains, no path to exit
//		if (solver.isEmpty() || solver.size() == 1) {
//			return null;
//			// If not empty, returns path to exit
//		} else {
//			return solver.iterator();
//		}
//	}
//
//	/**
//	 * Helper method that restores bombs as needed when retracing through the
//	 * dungeon
//	 * 
//	 * @param curr
//	 *            - current node
//	 * @param prev
//	 *            - previous node
//	 * @param graph
//	 *            - labyrinth
//	 * @throws GraphException
//	 *             if no edge exists between the two nodes or either node
//	 *             doesn't exist
//	 */
//	private void restoreBombs(Node curr, Node prev, Graph graph) throws GraphException {
//		if (graph.getEdge(curr, prev).getType() == 1) {
//			bBombs++;
//		} else if (graph.getEdge(curr, prev).getType() == 2) {
//			bBombs = bBombs + 2;
//		} else if (graph.getEdge(curr, prev).getType() == 3) {
//			aBombs++;
//		}
//	}
//
//	/**
//	 * Helper method that checks whether an edge can be passed with the given
//	 * amount of bombs
//	 * 
//	 * @param edge
//	 *            - edge to check if can be passed
//	 * @return true if edge can be passed, false otherwise
//	 */
//	private boolean passable(Edge edge) {
//		if (edge.getType() == 4) {
//			return true;
//		} else if (edge.getType() == 1 && bBombs != 0) {
//			bBombs--;
//			return true;
//		} else if (edge.getType() == 2 && bBombs >= 2) {
//			bBombs = bBombs - 2;
//			return true;
//		} else if (edge.getType() == 3 && aBombs != 0) {
//			aBombs--;
//			return true;
//		} else {
//			return false;
//		}
//	}
//}

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;


public class Solver {
	Graph Laby = null;// graph for Labyrinth;
	int brickbomb;// the number of brickbomb
	int acidbomb;// the number of acidbomb
	int row;//number of row
	int column;//number of column
	int number;//number of node 
	Node start,exit;//starting node, exit node
	Stack<Node> s = new Stack();//store path node;
	
	public Solver(String inputFile) throws LabyrinthException, GraphException {
		BufferedReader in;//input the file
		
		String line;//the content of the file
	      

		ArrayList<String> temp = new ArrayList<String>();//store the content 
		try {
				in = new BufferedReader(new FileReader(inputFile));
				line = in.readLine();//input the file
				while(line != null){
					temp.add(line);//make sure the line is not empty
					line = in.readLine();
				}
				in.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				throw new LabyrinthException("Error");//if the file is not found, raise the exception;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();//if the input error occurs, raise the exception;
			}
		    row = Integer.parseInt(temp.get(1));//get the row
		    column = Integer.parseInt(temp.get(2));// get the column
		    number = row*column;//node number = row * column;
		    brickbomb = Integer.parseInt(temp.get(3));// get the brickbomb number;
		    acidbomb = Integer.parseInt(temp.get(4));//get the acidbomb number;
		    
			Laby = new Graph(number);// create the new graph 
			
			for(int i = 5; i < temp.size();i = i+2){
				
				if(i+1 > temp.size()){break;}//if the loop reaches the end of table,end the loop;
				
				String line1 = temp.get(i); // the first line;
				
				String line2 = null;
				if (i+1 < temp.size()){
				  line2 = temp.get(i+1); //the second line(edge);
				}
			
				//Vertical edge 
				//indicate the name of the first node in this row;
				int line1name = ((i-5)/2+1)*row - row;
				int line2name = line1name;
				int index = 0;
				//go through line1
				while(index < line1.length()){
					
					
					if (index%2 == 0  ){
						if(line1.charAt(index) =='b'){// store the starting node
							
							start = Laby.getNode(line1name);
						}
						if(line1.charAt(index) =='x'){// store the exit node
							exit = Laby.getNode(line1name);
						}
						if(i+1  <temp.size()){//store the edge between two node
						String type = getEdgeType(line2.charAt(index));
						
						Node u = Laby.getNode(line2name);//linename is node number
						
						Node v = Laby.getNode(line2name+row);
						
						if(type != null){
							Laby.insertEdge(u, v, type);// if there is edge ,add it to the graph
							}
						line2name++;
						}
					}
					//edge for line1  
					else{
						//this is for edge between the nodeline;
						String type = getEdgeType(line1.charAt(index));
						Node u = Laby.getNode(line1name);
						Node v = Laby.getNode(line1name+1);
						if(type != null){
						Laby.insertEdge(u, v, type);
						}
						line1name++;
					}
				
					index++;
				}
				
			}
			//in case the start , exit is at the last line, we have to go through the lsat
			String lastline = temp.get(temp.size()-1);
			
			int index = 0;
			int lastname = number - row;
			while (index < lastline.length()){
				if(index%2 == 0){
					if(lastline.charAt(index) =='b'){//store the starting node
						start = Laby.getNode(lastname);
					}
					if(lastline.charAt(index) =='x'){//store the exit node
						
						exit = Laby.getNode(lastname);
					} 
					lastname ++;
				}
				index++;
			}
		}
	//helper function to indicate the edge type with switch case;
	private String getEdgeType(char c)
	{
		switch(c){
		case 'h':
			return "wall";
		
		case 'H':
			return "thick wall";
		case 'm':
			return "metal wall";
		case 'v':
			return "wall";
		case 'V':
			return "thick wall";
		case 'M': 
			return "metal wall";
		case '-':
			return "corridor";
		case '|':
			return "corridor";
		default:
			return null;
		
	}
	
	}
	public Graph getGraph() throws LabyrinthException{
		if (Laby == null){//if the graph is not defined , throw the exception;
			throw new LabyrinthException("Error");
		}
		//otherwise , return the graph
		return Laby;
	}
	public Iterator solve() throws GraphException{
		
		if (path(start,exit) == false){
			// if there is no path between start and exit return null
			return null;
		}
		//otherwise , return the path iterator.
		Iterator<Node> iter = s.iterator();
		
		
		return iter;
	}
	//helper function for solve()
	private boolean path(Node u, Node v) throws GraphException {
		int default_brickbomb = brickbomb, default_acidbomb = acidbomb;
		u.setMark(true);//Mark first node u;
		s.push(u);//push it into the stack
		if (u == v){// if the u equal to v , it means find the path and return true;
		    return true;
		}
		else{
			//go through each edge of u 
			Iterator<Edge> temp = Laby.incidentEdges(u);
			while (temp.hasNext()){
				Edge e = temp.next();
				Node w = e.secondEndpoint();// w is the other end point of the edge , which is different from u
				if (w.getName() == u.getName()){
					w = e.firstEndpoint();
				}

				// case for corridor
				if(w.getMark() == false) {
				if (e.getType() == 1){					
						if(path(w,v) == true){
							return true;
						}}
				//case for mental wall
				else if(e.getType()== 4){
					if (acidbomb>0){
						acidbomb = acidbomb-- ;
						
						if(path(w,v) == true){
							return true;}
						else {
							//reset the bomb
							acidbomb = default_acidbomb;
							brickbomb = default_brickbomb;
							
						}
						}}
				//case for thick wall
				else if(e.getType()== 2){
					if (brickbomb>=2){
						brickbomb = brickbomb-2;
						
						
						
							if(path(w,v) == true){
								return true;}
						else {
							//reset the bomb
							acidbomb = default_acidbomb;
							brickbomb = default_brickbomb;
							
						}
						}}
				//case for the wall
				else if(e.getType()== 3){
					if (brickbomb>0){
						brickbomb = brickbomb--;
						if(path(w,v) == true){
							return true;}
						else {
							//reset the bomb
							acidbomb = default_acidbomb;
							brickbomb = default_brickbomb;
						
						}
						}}
			

		}	}
			
			s.pop();
			u.setMark(false);// set the mark to false
			
			return false;
			
	}}
}
