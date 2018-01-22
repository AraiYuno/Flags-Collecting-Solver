import java.util.*;
import java.io.*;

//===============================================================
//***************************************************************
// COMP 3190
// Assignment 1 Q2
// Name: Seokjin Kyle Ahn
// A1Q2 class
// Purpose: This program simply solves "Capture the Flags" game. The program
//              starts at 'h' position and tries to collect as many flags as possible.
//              If any flag(s) is(are) in unreachable position, then the search still 
//              continues and collects the rest of the flags. However, since the 
//              search failed ultimately, the program notifies users that the search
//              was failed. 
//            
//              This program uses A* search in order to find the optimal solution 
//              with the minimal cost.
//
// Functions: main, simulateMazeSolver, findPath, followPath, pQueueContains,
//                closedListContains, getClosestFlagIndex, createMaze
//***************************************************************
//===============================================================
public class A1Q2
{
  private static List<Node> paths;
  private static Maze maze;
  private static boolean success;
  private static int rows;
  private static int cols; 
  private static char [][] mazeTxt;
  
  
  public static void main(String [] args ) throws IOException
  {
    simulateMazeSolver();
  }
  
  
  //==============================================================
  // simulateMazeSolve: simply simulates the toy-game.
  //==============================================================
  public static void simulateMazeSolver() throws IOException
  {
    createMaze();
    maze = new Maze( mazeTxt );
    Node startNode = maze.findStartNode();
    List<Node> flagsList = maze.findFlagsNode();
    System.out.println("\n=========================================================\n=========================================================");
    System.out.println("A* Search Initiated.");
    findPath( startNode, flagsList );
    maze.toMarkVisitedNodes( paths );
    maze.toPrint();
    if( success )
    {
      System.out.println("\nTotal Number of Squares Visited: "+paths.size() );
      System.out.println("Total Number of Operations Considered: " + maze.getTotalOps() );
    }
    else
      System.out.println("\nA* Search Failed.");
    System.out.println("\nEnd of the Program");
  }
  
  
  //==============================================================
  // findPath: This function is the key to the program. This function uses A*
  //               search method in order to find the most optimal solution with the
  //               minimal cost.       
  //==============================================================
  public static void findPath( Node startPos, List<Node> targetFlags )
  { 
    int numFlags = targetFlags.size();
    Node startNode = startPos;
    List<Node> targetNodes = targetFlags;
    
    for( int index = 0; index < numFlags; index ++)                                             // we run it as many times as the flags are and we connect each start and end position
    {
      PriorityQueue<Node> openList = new PriorityQueue<Node>();                    // using standard A* openList and closedList 
      HashSet<Node> closedList = new HashSet<Node>();
      openList.add( startNode );
      while( !openList.isEmpty() )
      {
        Node currNode = openList.remove();
        
        // if we find reach '!', then we set '!'s position as new startNode and we look for the next closest '!'.
        if( currNode.getX() == targetNodes.get( getClosestFlagIndex( currNode, targetNodes )).getX() && 
           currNode.getY() == targetNodes.get( getClosestFlagIndex( currNode, targetNodes )).getY() )
        {
          followPath( startPos, currNode );
          startNode = currNode;
          targetNodes.remove( getClosestFlagIndex( currNode, targetNodes ));
          break;
        }
        
        List<Node> neighbours = maze.getNeighbours( currNode );
        for (Iterator<Node> i = neighbours.iterator(); i.hasNext();)
        {
          Node neighbourNode = i.next();
          // if neighbourNode is a wall or is already closed, then we ignore.
          if( !neighbourNode.isWalkable() || closedListContains( closedList, neighbourNode ) )
            continue;
          
          // if the neighbouNode is in neither of the lists, then we create and add it to openList
          if( !pQueueContains(openList, neighbourNode) && !closedListContains( closedList ,neighbourNode) )
          {
            neighbourNode.setGCost( currNode.getGCost() + getDistance( currNode, neighbourNode ));
            neighbourNode.setHCost( getDistance( neighbourNode, targetNodes.get(getClosestFlagIndex( neighbourNode , targetNodes )) ));
            neighbourNode.setParentNode( currNode );
            openList.add( neighbourNode );
          }
          //if neighbourNode's cost is lower, then we update the cost of the neighbour.
          else if( pQueueContains(openList , neighbourNode ) )
          {
            int newMovementCost = currNode.getGCost() + getDistance( currNode, neighbourNode );
            if( newMovementCost < neighbourNode.getGCost() )
            {
              neighbourNode.setGCost( currNode.getGCost() + getDistance( currNode, neighbourNode ));
              neighbourNode.setHCost( getDistance( neighbourNode, targetNodes.get( getClosestFlagIndex( neighbourNode, targetNodes )) ));
              neighbourNode.setParentNode( currNode );
            }
          }
        }
        // add currNode to the closedList.
        closedList.add( currNode );
      }
    }
    // if not all target nodes were collected, then the search failed. Some flag(s) should be unreacheable.
    if( targetNodes.size() == 0 )
      success = true;
    else
      success = false;
  }
  
  //==============================================================
  // pQueueContains: standard contains method. ( I know that list has .contains()
  //                          method, but I prefer this way so I know what I am doing.
  //==============================================================
  public static boolean pQueueContains ( PriorityQueue<Node> pQueue, Node n )
  {
    for (Iterator<Node> i = pQueue.iterator(); i.hasNext();)
    {
      Node tmp = i.next();
      if( tmp.getX() == n.getX() && tmp.getY() == n.getY() )
        return true;
    }
    return false;
  }
  
  //==============================================================
  //closedListContains: standard contains method. ( I know that list has .contains()
  //                          method, but I prefer this way so I know what I am doing.
  //==============================================================
  public static boolean closedListContains( HashSet<Node> list, Node n)
  {
    for (Iterator<Node> i = list.iterator(); i.hasNext();)
    {
      Node tmp = i.next();
      if( tmp.getX() == n.getX() && tmp.getY() == n.getY() )
        return true;
    }
    return false;
  }
  
  //===============================================================
  // followPath: traces the path backwards and we store the path in the "paths"
  //                 node list.
  //===============================================================
  public static void followPath( Node startNode, Node endNode )
  {
    List<Node> path = new LinkedList<Node>();
    List<Node> reversedPath = new LinkedList<Node>();
    Node currNode = endNode;                                                                        // we are tracing the path backwards.
    path.add( currNode );
    while( currNode != startNode )
    {
      path.add( currNode );
      currNode = currNode.getParentNode();
    } // end of while loop
    path.add( startNode );
    
    for( int i = path.size()-1; i > 0; i-- )
    {
      reversedPath.add( path.get(i) );
    }
    paths = reversedPath;
  } // end of followPath function.
  
  //===============================================================
  // getDistance: calculates and returns the distance between two coordinates. 
  //                   !! ONLY CONSIDERING HORIZONTAL AND VERTICAL MOVES !!
  //===============================================================
  public static int getDistance( Node nodeA, Node nodeB )
  {
    int distX = Math.abs( nodeA.getX() - nodeB.getX() );
    int distY = Math.abs( nodeA.getY() - nodeB.getY() );
    
    return distX + distY;
  }
  
  //===========================================================
  // getClosestFlagIndex: finds the closest flag from currNode's position
  //                              and returns the targetNodes' index.
  //===========================================================
  public static int getClosestFlagIndex( Node currNode, List<Node> targetNodes )
  {
    int toCompare = getDistance( currNode, targetNodes.get(0) );
    int index = 0;
    for( int i = 1; i < targetNodes.size(); i++ )
    {
      if( toCompare > getDistance( currNode, targetNodes.get(i)) )
      {
        index = i;
        toCompare = getDistance( currNode, targetNodes.get(i));
      } //if
    } // i for loop
    return index;
  }
  
  //===========================================================
  // createMaze: reads in the input.txt and stores the maze in mazeTxt[][].
  //===========================================================
  public static void createMaze() throws IOException
  {
    Scanner sc = new Scanner( System.in );
    File file = new File( sc.nextLine() );
    sc = new Scanner(file);
    
    String line = sc.next();
    rows = Integer.parseInt( line );
    line = sc.next();
    cols = Integer.parseInt( line );
    mazeTxt = new char [ rows ][ cols ];
    
    for( int i = 0; i < rows; i++ )
    {
      line = sc.next();
      for( int j = 0; j < cols; j++ )
      {
        mazeTxt[i][j] = line.charAt( j );
      }
    } // i for loop
    sc.close();
  }
} // end of A1Q2
