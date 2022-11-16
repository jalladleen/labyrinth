import java.io.BufferedReader;
import java.util.ArrayList; // import the ArrayList class
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Solver{
	Graph Labyrinth = null;// graph for Labyrinth;
	private int length = 0; //length of the labyrinth, or the number of rooms in each column of the grid.
	private int width = 0; //the width of the labyrinth. The rooms of the labyrinth are arranged in a grid. 
	private int blastBombs= 0; //the number of blast bombs that the program is allowed to use while looking for a solution 
	private int meltBombs = 0; //  the number of melt bombs that the program is allowed to use while looking for a solution.
	private int row = -1; // used to keep track of the row of the laby
	String line; // used to read the other lines in the file to be able to build the laby
	private Node start; // The start point / node
    private Node exit; // The exit point / node
    private Stack<Node> labyPath; // helps finding the path by holding the nodes from start to exit 
    private Stack<Edge> visited; // this represents the visited edges that are marked;

    
    public Solver(String inputFile) throws LabyrinthException, IOException, GraphException {

    	// Buffer fileReader;
        try {  
        	
        	BufferedReader fileReader = new BufferedReader(new FileReader(inputFile)); //read the inputted file entered by the user
        	labyPath =  new Stack<>(); // initialize the stack for keeping track of the path 
        	visited = new Stack<>(); // initialize the stack for keeping track of the visited spots  
        	
        	Integer.parseInt(fileReader.readLine()); // skip the first line (not needed)
        	width = Integer.parseInt(fileReader.readLine()); // get width from second line
        	length = Integer.parseInt(fileReader.readLine()); // get length from third line
        	blastBombs = Integer.parseInt(fileReader.readLine()); // get the blast bombs number from fourth line
        	meltBombs = Integer.parseInt(fileReader.readLine()); // get the metal bombs number from fifth line
         
        	Labyrinth = new Graph(width * length); // initialize the graph with n = width * length
        	
        	while ((line = fileReader.readLine()) != null) { //Loop through the rest of the lines in the file
        		row++; //Increment the number of rows of the laby
            
        		int i = 0;
	        	while( i < line.length()) { //Loop through each character in the line 
	                
	        		//Deal with the entrance to the labyrinth
	               if(line.charAt(i) == 'e')  // if the current character is e then get the node and set it for the starting position 
	            	   start = Labyrinth.getNode(((row / 2) * width) + (i / 2));
	               
	               						/*HORIZONTALS*/
	               
	               //Deal with the  exit of the labyrinth
	               else if(line.charAt(i) == 'x')
	            	   exit = Labyrinth.getNode(((row / 2) * width) + (i / 2));
	               
	               //Deal with the horizontal brick wall
	               else if(line.charAt(i) == 'b')
	            	   handleChar( i,  row, 'b');  
	               
	             
	               //Deal with the horizontal rock wall
	               else if(line.charAt(i) == 'r')
	            	   handleChar( i,  row, 'r');
	               
	             
	               //Deal with the horizontal metal wall
	               else if(line.charAt(i) == 'm')
	            	   handleChar( i,  row, 'm');
	               
	             //Deal with the horizontal corridor
	               else if(line.charAt(i) == '-')
	            	   handleChar( i,  row, '-');
	               
	               						/*VERTICALS*/
	             
	               //Deal with the vertical brick wall
	               else if(line.charAt(i) == 'B')
	            	   handleChar( i,  row, 'B');
	               
	               //Deal with the vertical rock wall
	               else if(line.charAt(i) == 'R')
	            	   handleChar( i,  row, 'R');
	               
	               //Deal with the vertical metal wall
	               else if(line.charAt(i) == 'M')
	            	   handleChar( i,  row, 'M');
	               
	               //Deal with the vertical corridor
	               else if(line.charAt(i) == '|')
	            	   handleChar( i,  row, '|');
	               
	               //Deal with unbreakable, solid stone block and room
	               else if(line.charAt(i) == 'o' || line.charAt(i) == '*') {
	            	   
	               }
	                i++; //Check the next character in the line
	            }    
	        }
        	
        	fileReader.close(); //Close the file once done processing it
        
        	// if the input file is not found
        }catch (FileNotFoundException e) {   //Then throw and expcetion with a message 
            throw new LabyrinthException("\"%s file not found.".formatted(inputFile));
        }
    }
	
	
	public Graph getGraph() throws LabyrinthException{
		
		if (Labyrinth == null){//if the graph is not defined 
			throw new LabyrinthException("Graph not defined");// Then throw an exception with a message 
		}
		
		//otherwise , return the graph
		return Labyrinth;
	}

	private int getEdgeType(char c)
	{
		//Check the cases of the types an edge can be 
		
		switch(c){
		
		case 'b': // if the character b, then return 2 (brick wall)
			return 2;
		case 'r': // if the character r, then return 3 (rock wall)
			return 3;
		case 'm': // if the character m, then return 4 (metal wall)
			return 4;
		case 'B': // if the character B, then return 2 (brick wall)
			return 2;
		case 'R': // if the character R, then return 2 (rock wall)
			return 3;
		case 'M': // if the character M, then return 4 (metal wall)
			return 4;
		case '-': // if the character -, then return 1 (corridor)
			return 1;
		case '|': // if the character |, then return 1 (corridor)
			return 1;
		case 'o', '*': {
        }
		
		default:
			return 0;
	}
		
	}
	
	public void handleChar(int index, int row, char character) throws GraphException
	{
									/*HORIZONTALS*/
		
		//Deal with the horizontal brick wall
		if(character == 'b')  Labyrinth.insertEdge(
				Labyrinth.getNode(((row / 2) * width) + ((index - 1) / 2)),
        		Labyrinth.getNode(((row / 2) * width) + ((index + 1) / 2)), getEdgeType('b'));

		//Deal with the horizontal rock wall
		if(character == 'r') Labyrinth.insertEdge(
				Labyrinth.getNode(((row / 2) * width) + ((index - 1) / 2)),
        		Labyrinth.getNode(((row / 2) * width) + ((index + 1) / 2)), getEdgeType('r'));

		//Deal with the horizontal metal wall
		if(character == 'm') Labyrinth.insertEdge(
				Labyrinth.getNode(((row / 2) * width) + ((index - 1) / 2)),
        		Labyrinth.getNode(((row / 2) * width) + ((index + 1) / 2)), getEdgeType('m'));
									
								/*VERTICALS*/
		
		//Deal with the vertical brick wall
		if(character == 'B') 
			Labyrinth.insertEdge(
					Labyrinth.getNode((((row - 1) / 2) * width) + (index / 2)),
					Labyrinth.getNode((((row + 1) / 2) * width) + (index / 2)), getEdgeType('B'));

		//Deal with the vertical rock wall
		if(character == 'R') Labyrinth.insertEdge(
				Labyrinth.getNode((((row - 1) / 2) * width) + (index / 2)),
        		Labyrinth.getNode((((row + 1) / 2) * width) + (index / 2)), getEdgeType('R'));

		//Deal with the vertical metal wall
		if(character == 'M') Labyrinth.insertEdge(
				Labyrinth.getNode((((row - 1) / 2) * width) + (index / 2)),
        		Labyrinth.getNode((((row + 1) / 2) * width) + (index / 2)), getEdgeType('M'));

								
								/*CORRIDORS*/
		
        // Deal with Vertical Corridor
        if(character == '|') 
        	Labyrinth.insertEdge(
        			Labyrinth.getNode((((row - 1) / 2) * width) + (index / 2)),
        			Labyrinth.getNode((((row + 1) / 2) * width) + (index / 2)), getEdgeType('|'));

    	// Deal with Deal with the horizontal corridor
        if(character == '-')
        	Labyrinth.insertEdge(
        			Labyrinth.getNode(((row / 2) * width) + ((index - 1) / 2)),
        			Labyrinth.getNode(((row / 2) * width) + ((index + 1) / 2)), getEdgeType('-'));

		}
		
	
    public Iterator<Node> solve() throws GraphException {
    	
    	//Return null, if there's no path found between start and exit 
		if (!findPath(start,exit))
			return null;
		
		//otherwise , return the path iterator.
		Iterator<Node> pathIter = labyPath.iterator();
		return pathIter;

    }

    private boolean findPath(Node u, Node v) throws GraphException {
        
        u.setMark(true); //Mark the node as true
        labyPath.push(u); // push it in the stack that keeps track of the path
        
        
        ArrayList<Edge> incEdges = Labyrinth.incidentEdges(u); // find the incident edges of the node u
        
        if(u == v) // if the entrance and exit are equal to each other 
        	return true; //then return true

        //loop through all of the edges 
        for (Edge edge : incEdges) {
        	
        	Node edgePoint1 = edge.firstEndpoint(); // keep track of the first and second end points of an edge
        	Node edgePoint2 = edge.secondEndpoint();

            
        	//if the edge is an unmarked node 
            if (edgePoint1.getMark() == false) {
                
            	//Then check the type of it 
            	switch (edge.getType()) {
            	
            	case 4: //if it is a metal wall
            		if(meltBombs >= 1) // then at least one melt bomb needs to be used for explosion 
            		{
            			meltBombs--; // if used, decremenet the number of available melt bombs 
            			visited.push(edge); // add the current edge to the stack of the visited edges 
                        if (findPath(edgePoint1, v)) // if an available path is found, then return true
                            return true;
            		}
            		break;
            		
            		
            	case 3: // if it is a rock wall, then we need 2 blast bombs at least for explosion 
            		if(blastBombs >= 2) {
                        visited.push(edge);
                        blastBombs = blastBombs - 2; // decrement the 2 bombs used 
                        if (findPath(edgePoint1, v))
                            return true;
            		}
            		break;
            		
            	case 2: // if it is a brick wall
            		if(blastBombs >= 1) { // then , one blast bomb is needed for explosion 
            			blastBombs--;
            			visited.push(edge);
                        if (findPath(edgePoint1, v))
                        	return true;
            		}
            		break;
            	
            	case 1: // if it is a corridor 
            		visited.push(edge); // then just add the edge to the stack and find the available path 
                    if (findPath(edgePoint1, v))
                        return true;
                    break;

            	}
            }
            
            // check the second end point if it is still not marked yet
            else if (edgePoint2.getMark() == false) {
                
            	//Repeat the process to find an availability of the second end point for an edge
            	switch (edge.getType()) {
            	
            	case 4:
            		if(meltBombs >= 1)
            		{
            			meltBombs--;
            			visited.push(edge);
                        if (findPath(edgePoint2, v))
                            return true;
            		}
            		
                  break;
                  
            	case 3:
            		if(blastBombs >= 2) {
                        visited.push(edge);
                        blastBombs = blastBombs - 2;
                        if (findPath(edgePoint2, v))
                            return true;
            		}
            		break;
            		
            	case 2:
            		if(blastBombs >= 1) {
            			blastBombs--;
            			visited.push(edge);
                        if (findPath(edgePoint2, v))
                        	return true;
            		}
            		
            		break;
            
            	case 1:
            		visited.push(edge);
                    if (findPath(edgePoint2, v))
                        return true;
                    break;            		
            	}
            }
        }

        if (visited.isEmpty() == false) {// if the exit is not reached yet 
            Edge var = visited.pop(); // Then pop the vistsed edges 
            
            switch (var.getType()) {
            //Re-increment the bombs 
            case 2: // The brick wall needs one bomb for explosion 
            	blastBombs++; 
            	break;
            case 3: //The rock wall will need two bombs for explosion 
            	blastBombs = blastBombs + 2;
            	break;
            case 4: // The metal wall needs one melt bomb for explosion 
            	 meltBombs++;
            }
        }

        u.setMark(false); // set the mark of the node to false
        labyPath.pop(); // remove the node 
        return false;
    }
}