/**
 * The MazeSolver class runs an application which solves a maze in a given
 * text file and provides FileIO and a user interface for loading in a maze,
 * finding a solution, and printing the pathway from the start to the end.
 * 
 * @author Joshua Kellerman, CS 367
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class MazeSolver {

	public static MazeCell[][] maze; //the maze object
	public static ArrayQueue<MazeCell> mazeQueue = new ArrayQueue<MazeCell>();
	public static int mazeHeight; //the maze's height read-in
	public static int mazeWidth; //the maze's width read-in
	static Scanner mazeIn; //Scanner object to read in txt files
	static boolean solved = false; //maze solved boolean
	static boolean noSolution = false;
	static MazeCell current = null; //current MazeCell object
	static Stack<MazeCell> pathStack; //MazeCell stack to print solution path

	public static void main(String[] args) {

		Scanner stdin = new Scanner(System.in);
		boolean done = false;
		while (!done) {
			System.out.print("Enter option - d, m, p, s, or x: ");
			String input = stdin.nextLine();

			if (input.length() > 0) {
				char choice = input.charAt(0);  // strip off option character
				String remainder = "";  // used to hold the remainder of input
				// trim off any leading or trailing spaces
				remainder = input.substring(1).trim();

				switch (choice) {

				case 'd': //display maze
					if(maze == null){ //check for maze object existence
						System.out.println("No maze specified.");
						break; //exit switch if no maze
					}
					//print out maze as it appears in the maze object
					//using the inherent toString method which is overridden
					for(int i = 0; i < mazeHeight; i++){
						for(int j = 0; j < mazeWidth; j++){
							System.out.print(maze[i][j]);
						}
						System.out.print("\n");
					}	
					break; // end case d

				case 'm': //set maze
					try {
						//read in file with scanner (may throw FnF Exception)
						mazeIn = new Scanner(new File(remainder));
						//read in first int as height
						mazeHeight = mazeIn.nextInt();
						//read in second int as width
						mazeWidth = mazeIn.nextInt();
						//assign new array to maze reference
						maze = new MazeCell[mazeHeight][mazeWidth];
						//clear the rest of the first line
						mazeIn.nextLine();
						String tempLine;
						int type = -1;
						for (int i = 0; i < mazeHeight; i++){
							tempLine = mazeIn.nextLine(); //read in line by line
							for(int j = 0; j < mazeWidth; j++){
								if (tempLine.charAt(j) == 'S'){
									type = MazeCell.START;
								} else if (tempLine.charAt(j) == ' '){
									type = MazeCell.OPEN;
								}else if (tempLine.charAt(j) == '|'){
									type = MazeCell.WALL;
								}else if (tempLine.charAt(j) == 'X'){
									type = MazeCell.END;
								}
								maze[i][j] = new MazeCell(type, i, j);
							}
						}
						solved = false;
					} catch (FileNotFoundException e) {
						System.out.println("Invalid file.");
					}

					break; //end case 'm'

				case 'p': //print path
					if(maze == null){
						System.out.println("No maze specified.");
					} else if(!solved){
						System.out.println("Maze has not yet been solved.");
					} else if(noSolution){
						System.out.println("No solution.");
					} else {
						//print list by popping off of pathStack
						MazeCell temp = null;
						while(!pathStack.isEmpty()){
							temp = pathStack.pop();
						//format in print statement
							System.out.println("(" + temp.row() + "," + 
						temp.col()+ ")");
						}
					}
					break;

				case 's': //solve path
					if(maze == null){
						System.out.println("No maze specified.");
						break;
					}
					//find the start of the maze (anywhere on maze)
					boolean startFound = false;
					for(int i = 0; i < mazeHeight; i++){
						for(int j = 0; j < mazeWidth; j++){
							//cycle through every cell and compare to see if
							//any are START type
							if (maze[i][j].type() == MazeCell.START){
								startFound = true; //for breaking out of loops
								//enqueue the start
								mazeQueue.enqueue(maze[i][j]);
								break; //break out of inner for loop (column)
							}
						}
						if (startFound){ //break out of outer for loop (row)		
							break;
						}
					}
					//now start is found, and we must use the search algorithm
					//to traverse the maze. A while loop will keep it going unless
					//one of the base conditions is interrupted.
					while(!mazeQueue.isEmpty()){
						current = mazeQueue.dequeue();
						//if the current cell is the end:
						if (current.type() == MazeCell.END){
							solved = true;
							System.out.println("Solution found.");
							
							//create the path back to start in stack
							
							pathStack = new Stack<MazeCell>();
							while (current.getPrev() != null){
								pathStack.push(current);
								current = current.getPrev();
							} pathStack.push(current);
							break;
						}
						
						//if there is no crumb in the current cell,
						//drop one in there.
						
						if(!current.hasCrumb()){
							current.dropCrumb();
							
						//now check neighbors for open cells that have not
						//been breadcrumbed.
							
						} // check North
						if (current.row() > 0 && !maze[current.row()-1]
								[current.col()].hasCrumb() && maze[current.row()-1]
										[current.col()].type() != MazeCell.WALL){ 
						  // enqueue North
							maze[current.row()-1] [current.col()].setPrev(current);
							mazeQueue.enqueue(maze[current.row()-1][current.col()]);

						} // check East
						if (current.col() > mazeWidth-1 && !maze[current.row()]
								[current.col()+1].hasCrumb() && maze[current.row()]
										[current.col()+1].type() != MazeCell.WALL){
						  // enqueue East
							maze[current.row()][current.col()+1].setPrev(current);
							mazeQueue.enqueue(maze[current.row()][current.col()+1]);
							
						}// check South
						if (current.row() < mazeHeight-1 && !maze[current.row()+1]
								[current.col()].hasCrumb() && maze[current.row()+1]
										[current.col()].type() != MazeCell.WALL){
						  // enqueue South
							maze[current.row()+1][current.col()].setPrev(current);
							mazeQueue.enqueue(maze[current.row()+1][current.col()]);
							
						}// check West
						if (current.row() < mazeWidth-1 && !maze[current.row()]
								[current.col()-1].hasCrumb() && maze[current.row()]
										[current.col()-1].type() != MazeCell.WALL){
						  // enqueue West
							maze[current.row()][current.col()-1].setPrev(current);
							mazeQueue.enqueue(maze[current.row()][current.col()-1]);
						}
					}
					//base solution for if the maze has no solution
					if (mazeQueue.isEmpty() && !solved){
						noSolution = true;
						break;
					}
					//base solution for if start is never found
					if (current == null){
						System.out.println("No starting position!");
						break;
					}
					break;

				case 'x': //exit
					System.out.println("Exit");
					done = true;
					break;

				default: //for typos
					System.out.println("Unknown command.");
					break;

				} //end switch
			} //end if
		} // end while
	} // end main
} // end class
