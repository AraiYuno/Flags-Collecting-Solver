import java.util.*;


//===============================================================
//***************************************************************
// Maze Class 
//
// Purpose: Maze class is keep track of the changes of the maze and it also 
//               keeps track of number of total operations.
//              
//              We generate possible states(nodes) in the maze class using various
//              functions
//
//Functions: findStartNode, findFlagsNode, getNeighbours, toMarkVisitedNodes,
//                toPrint, getters and setters.
//***************************************************************
//===============================================================

public class Maze
{
  private char [][] maze;
  private int maxX;
  private int maxY;
  private int totalOps;
  
  public Maze( char [][] maze )
  {
    this.maze = maze;
    this.maxX = this.maze[0].length;
    this.maxY = this.maze.length;
    this.totalOps = 0;
  }
  
  //================================================================
  // findStartNode: finds a start position of 'h'.
  //================================================================
  public Node findStartNode()
  {
    Node toReturn = null;
    for( int i = 0; i < maxY; i++ )
    {
      for( int j = 0; j < maxX; j++ )
      {
        if( maze[i][j] == 'h' )
        {
          this.totalOps++;
          toReturn = new Node( true, j, i );
        }
      }
    }
    return toReturn;
  }
  
  //================================================================
  // findFlagsNode: finds all the flags in the maze and return a list of them.
  //================================================================
  public List<Node> findFlagsNode()
  {
    List<Node> toReturn = new LinkedList<Node>();
    for( int i = 0; i < maxY; i++ )
    {
      for( int j = 0; j < maxX; j++ )
      {
        if( maze[i][j] == '!' )
        {
          this.totalOps++;
          toReturn.add( new Node( true, j, i ) );
        }
      }
    }
    return toReturn;
  }
  
  //===============================================================
  // getNeighbours: finds all the possible neighbour nodes from left, right, up and 
  //                       down, and returns a list of neighbour nodes of them.
  //                        
  //                       If the neighbour node is a wall, we don't create them.
  //================================================================
  public List<Node> getNeighbours( Node node )
  {
    List<Node> neighbours = new LinkedList<Node>();
    
    for( int x = -1; x <= 1; x++ )
    {
      for( int y = -1; y <= 1; y++ )
      {
        if( (x == 1 && y == 1) || (x == 1 && y == -1 ) || ( x == -1 && y == -1 ) || ( x == -1 && y == 1 ) || ( x == 0 && y == 0 ) )
          continue;
        else // we only consider west, east, north and south
        {
          int tmpX = node.getX() + x;
          int tmpY = node.getY() + y;
          // if tmpX and tmpY are within the maze and its position is not a wall, then we add it to the neighbours.
          if( tmpX >= 0 && tmpX < this.maxX && tmpY >= 0 && tmpY < this.maxY )
          {
            // we only consider possible moves and create their states.
            if( maze[tmpY][tmpX] != '#')
            {
              this.totalOps++;
              Node tmp  = new Node( true, tmpX, tmpY );
              neighbours.add( tmp );
            }
            else if( maze[tmpY][tmpX] == '#' )
            {
              continue;
            }
          }
        }
      }
    }
    return neighbours;
  } // end of getNeighbours function
  
  //============================================================
  // toMarkVisitedNodes: to mark any visited nodes by number of times visited
  //============================================================
  public void toMarkVisitedNodes( List<Node> list )
  {
    for( int i = 0; i < list.size(); i++ )
    {
      if( this.maze[list.get(i).getY()][list.get(i).getX()] == '.' || this.maze[list.get(i).getY()][list.get(i).getX()] == '!' || this.maze[list.get(i).getY()][list.get(i).getX()] == 'h' )
      {
        this.maze[list.get(i).getY()][list.get(i).getX()] = '1';
      }
      else if( Character.isDigit(this.maze[list.get(i).getY()][list.get(i).getX()]) )
      {
        int n = this.maze[list.get(i).getY()][list.get(i).getX()] + 1;
        char c = (char)n;
        this.maze[list.get(i).getY()][list.get(i).getX()] = c;
      }
    }
  }
  
  //============================================================
  // toPrint: simply prints the maze in conside.
  //============================================================
  public void toPrint()
  {
    for( int i = 0; i < this.maxY; i++ )
    {
      System.out.println("");
      for( int j = 0; j < this.maxX; j++ )
      {
        System.out.print( this.maze[i][j] );
      }
    }
  }
  
  //=============================================================
  // Getter and Setters
  //=============================================================
  public char [][] getMaze(){ return this.maze; }
  
  public int getTotalOps(){ return this.totalOps; }
  
} // end of Maze class