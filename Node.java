//===============================================================
//***************************************************************
// Node Class 
//
// Purpose: Noce class = state. Standard node class
//
//Functions: compareTo, toString, getters and setters
//***************************************************************
//===============================================================

public class Node implements Comparable<Node>
{
  private int x;
  private int y;
  private int hCost;
  private int gCost;
  private boolean walkable;
  private Node parentNode;
  
  public Node( boolean walkable, int x, int y )
  {
    this.x = x;
    this.y = y;
    this.walkable = walkable;
  }

  
  public int compareTo( Node n )
  {
    int toReturn;
    if( getFCost() > n.getFCost() )
      toReturn = 1;
    else if( getHCost() < n.getFCost() )
      toReturn = -1;
    else
      toReturn = 0;
    return toReturn;
  }
  
  public String toString()
  {
    return "Node Coordinates -> " + "X :" + this.x + "Y :" + this.y +", F Cost: "+getFCost();
  }
  
  //===================================================================
  // Getter and Setter methods
  //===================================================================
  
  //ALL THE SETTERS
  public void setHCost( int h ){ this.hCost = h; }
  
  public void setGCost( int g ){ this.gCost = g; }
  
  public void setParentNode( Node p ){ this.parentNode = p; }
  
 //ALL THE GETTERS 
  public int getFCost(){ return this.gCost + this.hCost; }
 
  public int getHCost(){ return this.hCost; }
  
  public int getGCost(){ return this.gCost; }
  
  public Node getParentNode(){ return this.parentNode; }
  
  public int getX(){ return this.x; }
  
  public int getY(){ return this.y; }
  
  public boolean isWalkable(){ return this.walkable; }
}